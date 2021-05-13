package com.spcba.bpass.ui.fragments.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.spcba.bpass.data.datamodels.TopUp;
import com.spcba.bpass.databinding.FragmentTopupDetailsBinding;
import com.spcba.bpass.ui.viewmodels.LobbyActivityViewModel;
import com.spcba.bpass.ui.viewmodels.TopUpFragmentViewModel;

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
    private Button doneBtn;

    private LobbyActivityViewModel viewModel;
    private TopUpFragmentViewModel topUpFragmentViewModel;


    private NavController navController;

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
        topUpFragmentViewModel = new ViewModelProvider(this).get(TopUpFragmentViewModel.class);
        amountToBePaid = binder.amountToBePaidTv;
        fee = binder.feeTv;
        mobileNumber = binder.mobileNumberTv;
        paymentMethod = binder.paymentMethodTv;
        referenceNumber = binder.refNumTv;
        date = binder.dateCreatedTv;
        qrCode = binder.qrCodeIv;
        doneBtn = binder.doneBtn;


        doneBtn.setOnClickListener(btn->{
            navController.popBackStack();
        });

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            mobileNumber.setText(user.getMobileNumber());

        });
        topUpFragmentViewModel.getAddedTopUp().observe(getViewLifecycleOwner(), addedTopUpEvent -> {
            if (addedTopUpEvent.isHandled()) return;
            TopUp top = addedTopUpEvent.getContentIfNotHandled();
            amountToBePaid.setText("₱" +top.getAmount());
            referenceNumber.setText("Ref.No. "+ top.getRefNumber());
            paymentMethod.setText(top.getPaymentMethod());
            date.setText(String.valueOf(top.getDateCreated()));
            try {

            Glide.with(requireContext()).asBitmap().load(createQrCodeBitMap(top.getRefNumber()))
                    .into(qrCode);
            }catch (Exception e){
                Log.d(TAG, "QR Code Created");
            }

        });
    }

    private Bitmap createQrCodeBitMap(String Value) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(Value,
                    BarcodeFormat.QR_CODE, 300, 300, null);
        } catch (IllegalArgumentException iae) {
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
