package com.spcba.bpass.ui.fragments.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.spcba.bpass.R;
import com.spcba.bpass.data.datamodels.Trip;
import com.spcba.bpass.databinding.FragmentLobbyBinding;
import com.spcba.bpass.ui.adapters.DestinationsAdapter;
import com.spcba.bpass.ui.fragments.dialog.CalendarDialog;
import com.spcba.bpass.ui.fragments.dialog.CheckoutDialog;
import com.spcba.bpass.ui.viewmodels.LobbyActivityViewModel;

public class LobbyFragment extends Fragment {
    private FragmentLobbyBinding binder;
    private RecyclerView destinationsRv;
    private TextView nameTv;
    private TextView balanceTv;
    private TextView locationTv;
    private CircularImageView userProfilePic;
    private DestinationsAdapter destinationsAdapter;
    private ImageButton scheduleBtn;
    private Chip chipOne;
    private Chip chipTwo;
    private Chip chipThree;
    private Chip chipFour;



    private static final String ALABANG ="Alabang Starmall";
    private static final String MARKET_MARKET ="Market-Market";
    private static final String CUBAO ="Cubao";
    private static final String MEGAMALL ="Megamall";


    private static final String TAG = "LobbyFragment";


    private LobbyActivityViewModel viewModel;

    private NavController navController;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = FragmentLobbyBinding.inflate(inflater,container,false);
        return binder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(LobbyActivityViewModel.class);
        navController = Navigation.findNavController(view);
        destinationsRv = binder.destinationsRv;
        nameTv = binder.userName;
        balanceTv = binder.balanceTv;
        locationTv = binder.locationTv;
        userProfilePic = binder.userProfilePic;
        destinationsAdapter = new DestinationsAdapter();
        destinationsRv.setAdapter(destinationsAdapter);
        scheduleBtn = binder.scheduleBtn;

        chipOne = binder.chipOne;
        chipTwo = binder.chipTwo;
        chipThree = binder.chipThree;
        chipFour = binder.chipFour;

        balanceTv.setOnClickListener(btn->{
            navController.navigate(R.id.action_lobbyFragment_to_topUpFragment);
        });
        scheduleBtn.setOnClickListener(btn->{

            showCalendarDialog();
        });
        chipOne.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.filter(MARKET_MARKET,isChecked);

        });
        chipTwo.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            viewModel.filter(MEGAMALL,isChecked);

        }));

        chipThree.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            viewModel.filter(CUBAO,isChecked);

        }));
        chipFour.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            viewModel.filter(ALABANG,isChecked);

        }));

        destinationsAdapter.getSelectedTrip().observe(getViewLifecycleOwner(), onTripSelectedEvent ->{
                if (onTripSelectedEvent.isHandled()) return;
                Trip destination = onTripSelectedEvent.getContentIfNotHandled();
                viewModel.setSelectedTrip(destination);
                showCheckoutDialog();

        });

        viewModel.getUser().observe(getViewLifecycleOwner(),user -> {
            if (user == null) return;
            nameTv.setText("Welcome "+ user.getName()+"!");
            balanceTv.setText(String.format("₱%.2f", user.getBalance()));
            Glide.with(requireContext()).load(Uri.parse(user.getProfilePicUrl())).into(userProfilePic);
            fadeOutView(nameTv);

        });

        viewModel.getTripsLiveData().observe(getViewLifecycleOwner(),trips -> {
            destinationsAdapter.insertDestinations(trips);


        });

        viewModel.getLocationLiveData().observe(getViewLifecycleOwner(),locationUpdates ->{
                    locationTv.setText(locationUpdates);

        });


    }
    private void fadeOutView(View view){
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);
        view.startAnimation(fadeOut);
        view.setVisibility(View.GONE);
    }
    private void showCalendarDialog(){
        CalendarDialog dialog = new CalendarDialog();
        dialog.show(getChildFragmentManager(),"Schedule");

    }
    private void showCheckoutDialog(){
        CheckoutDialog dialog = new CheckoutDialog();
        dialog.show(getChildFragmentManager(),"Checkout");

    }


}
