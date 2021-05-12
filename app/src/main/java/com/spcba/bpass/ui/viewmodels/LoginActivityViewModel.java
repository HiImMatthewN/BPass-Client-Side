package com.spcba.bpass.ui.viewmodels;

import android.app.Activity;
import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.spcba.bpass.data.datautils.Event;
import com.spcba.bpass.data.datamodels.User;
import com.spcba.bpass.repository.UserRepository;
import com.spcba.bpass.repository.StorageRepository;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivityViewModel extends AndroidViewModel {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private UserRepository userRepository = UserRepository.getInstance();
    private StorageRepository storageRepository = StorageRepository.getInstance();

    private PhoneAuthOptions option;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private String mVerificationId;

    private MutableLiveData<Event<Boolean>> onCodeReceived = new MutableLiveData<>();
    private MutableLiveData<Boolean> onCodeSending = new MutableLiveData<>();
    private MutableLiveData<Event<String>> firebaseAuthException = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> signInStatusLiveData = new MutableLiveData<>();


    private MutableLiveData<Event<Boolean>> invalidInput = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> invalidName = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> invalidEmail = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> invalidMobileNumber = new MutableLiveData<>();

    private User currentUser = null;
    private Uri profilePicUri = null;


    private static final String TAG = "LoginActivityViewModel";

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "LoginActivityViewModel: Created");
    }

    public void verifyPhoneNumber(String number, Activity activity) {
        if (checkIfNumberIsValid(number)) {
            Log.d(TAG, "verifyPhoneNumber: Called Number " + number);
            createOnVerificationCallback(activity);
            option = PhoneAuthOptions.newBuilder(mAuth)
                    .setPhoneNumber(number)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(activity)
                    .setCallbacks(mCallbacks)
                    .build();
            PhoneAuthProvider.verifyPhoneNumber(option);
            onCodeSending.setValue(true);
        } else
            invalidInput.postValue(new Event<>(true));
    }

    public void verifyCode(String code, Activity activity) {
        if (checkIfCodeIsValid(code)) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            signInWithPhoneAuthCredential(credential, activity);
        } else
            invalidInput.postValue(new Event<>(true));
    }

    public void checkIfBasicInfoValid(String name, String mobileNumber, String email) {

        if (!checkIfNameIsValid(name) || !isEmailValid(email) || !checkIfNumberIsValid(mobileNumber)) {
            invalidName.postValue(new Event<>(checkIfNameIsValid(name)));
            invalidEmail.postValue(new Event<>(isEmailValid(email)));
            invalidMobileNumber.postValue(new Event<>(checkIfNumberIsValid(mobileNumber)));

        } else {
            checkIfNumberExists(mobileNumber);
        }
    }
    public void checkIfNumberExists(String number){

        userRepository.checkIfMobileNumberExists(number);


    }
    private void createOnVerificationCallback(Activity activity) {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);

                if (credential == null) {
                    onCodeReceived.setValue(new Event<>(false));
                    return;
                }
                onCodeReceived.setValue(new Event<>(true));
                signInWithPhoneAuthCredential(credential, activity);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.e(TAG, "onVerificationFailed " + e.getMessage());
                onCodeReceived.setValue(new Event<>(false));

            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);
                onCodeReceived.setValue(new Event<>(true));

                mVerificationId = verificationId;
                resendToken = token;
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                onCodeReceived.setValue(new Event<>(false));

            }
        };

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, Activity activity) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(activity, task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "onComplete: Sign  in with credential Success");
                signInStatusLiveData.setValue(new Event<>(true));
            } else {
                signInStatusLiveData.setValue(new Event<>(false));
                Log.d(TAG, "onComplete: Sign  in with credential Failed");
            }
        });
    }

    public void setProfilePicUri(Uri profilePicUri) {
        this.profilePicUri = profilePicUri;
    }
    /**
     * Will upload profile pic to firebase storage
     * when user have selected a profile pic
     */
    public void uploadProfilePic(){
        if (currentUser!= null){
            if (profilePicUri != null)
                storageRepository.saveToStorage(profilePicUri);
            else
                saveUser(FirebaseAuth.getInstance().getCurrentUser().getUid(),Uri.parse(""));
        }

    }
    public void createUser(String name,String mobileNum,String email){
         currentUser = new User(name,mobileNum,email,""
                ,"",0.0);
    }
    public void saveUser(String uid,Uri uri){
        if (currentUser != null){
            currentUser.setUid(uid);
            currentUser.setProfilePicUrl(String.valueOf(uri));
            userRepository.saveUserToDb(currentUser);
        }
    }
    private boolean checkIfNumberIsValid(String input) {
        return input != null && !input.trim().equals("") && (input.length() == 13);
    }

    private boolean checkIfCodeIsValid(String codeEntered) {
        return codeEntered != null && !codeEntered.trim().equals("") && codeEntered.length() == 6;
    }

    private boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean checkIfNameIsValid(String name) {
        return name != null && !name.trim().equals("");

    }

    public LiveData<Event<Boolean>> getOnCodeReceived() {
        return onCodeReceived;
    }

    public LiveData<Boolean> getOnCodeSending() {
        return onCodeSending;
    }

    public LiveData<Event<String>> getFirebaseAuthException() {
        return firebaseAuthException;
    }

    public LiveData<Event<Boolean>> getSignInStatusLiveData() {
        return signInStatusLiveData;
    }

    public LiveData<Event<Boolean>> getIfNumberAlreadyExists() {
        return userRepository.getCheckIfNumberAlreadyExists();
    }

    public LiveData<Event<Boolean>> getSaveUserStatus() {
        return userRepository.getSaveUserLiveData();
    }

    public LiveData<Event<Boolean>> getInvalidInput() {
        return invalidInput;
    }

    public LiveData<Event<Boolean>> getInvalidName() {
        return invalidName;
    }

    public LiveData<Event<Boolean>> getInvalidEmail() {
        return invalidEmail;
    }

    public LiveData<Event<Boolean>> getInvalidMobileNumber() {
        return invalidMobileNumber;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    /**
     *
     * A Livedata that returns the
     * result of downloaded uri,
     * This happens after user registers
     * their account
     * @return
     */
    public LiveData<Event<Uri>> getDownloadedUri(){
       return storageRepository.getDownloadUri();
    }
}
