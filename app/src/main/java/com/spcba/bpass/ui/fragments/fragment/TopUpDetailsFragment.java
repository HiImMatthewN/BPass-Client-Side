package com.spcba.bpass.ui.fragments.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.spcba.bpass.data.datamodels.TopUp;
import com.spcba.bpass.databinding.FragmentTopupDetailsBinding;
import com.spcba.bpass.ui.fragments.dialog.ReceiptScannedDialog;
import com.spcba.bpass.ui.viewmodels.LobbyActivityViewModel;
import com.spcba.bpass.ui.viewmodels.TopUpDetailsViewModel;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class TopUpDetailsFragment extends Fragment {
    private FragmentTopupDetailsBinding binder;
    private TextView amountToBePaid;
    private TextView fee;
    private TextView mobileNumber;
    private TextView paymentMethod;
    private TextView referenceNumber;
    private TextView date;
    private ImageView qrCode;
    private TextView backTv;

    private LobbyActivityViewModel viewModel;
    private TopUpDetailsViewModel topUpDetailsViewModel;


    private NavController navController;

    public static final String RECEIPT_REQ = "Receipt";
    private static final String TAG = "TopUpDetailsFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = FragmentTopupDetailsBinding.inflate(inflater, container, false);
        return binder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(requireActivity()).get(LobbyActivityViewModel.class);
        topUpDetailsViewModel = new ViewModelProvider(requireActivity()).get(TopUpDetailsViewModel.class);
        amountToBePaid = binder.amountToBePaidTv;
        fee = binder.feeTv;
        mobileNumber = binder.mobileNumberTv;
        paymentMethod = binder.paymentMethodTv;
        referenceNumber = binder.refNumTv;
        date = binder.dateCreatedTv;
        qrCode = binder.qrCodeIv;
        backTv = binder.backTv;


        backTv.setOnClickListener(btn -> {
            navController.popBackStack();
        });

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            mobileNumber.setText(user.getMobileNumber());

        });
        topUpDetailsViewModel.getSelectedTopUpToView().observe(getViewLifecycleOwner(), selectedTopUpEvent -> {
            if (selectedTopUpEvent.isHandled()) return;
            TopUp top = selectedTopUpEvent.getContentIfNotHandled();
            Log.d(TAG, "Selected Top Up  Amount " + top.getAmount());
            Log.d(TAG, "Selected Top Up  Payment Method " + top.getPaymentMethod());
            Log.d(TAG, "Selected Top Up  Ref Num " + top.getRefNumber());
            Log.d(TAG, "Selected Top Up  Date " + top.getDateCreated());

            amountToBePaid.setText("₱" + top.getAmount());
            referenceNumber.setText("Ref.No. " + top.getRefNumber());
            paymentMethod.setText(top.getPaymentMethod());
            date.setText(String.valueOf(top.getDateCreated()));


            String qrCodeValue = RECEIPT_REQ + "-" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "-" + top.getRefNumber();
            Glide.with(requireContext()).asBitmap().load(createQrCodeBitMap(qrCodeValue))
                    .into(qrCode);


        });

        topUpDetailsViewModel.getTopUpAccepted().observe(getViewLifecycleOwner(),receiptScannedEvent->{
                        if (receiptScannedEvent.isHandled()) return;
                        if (receiptScannedEvent.getContentIfNotHandled()){
                            ReceiptScannedDialog dialog = new ReceiptScannedDialog();
                            dialog.show(getChildFragmentManager(),"Receipt Scanned");
                            Log.d(TAG, "Showing Dialog");
                        }


        });
    }

    private Bitmap createQrCodeBitMap(String Value) {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(Value,
                    BarcodeFormat.QR_CODE, 300, 300, null);
        } catch (IllegalArgumentException | WriterException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 300, 0, 0, w, h);
        return bitmap;
    }
}
