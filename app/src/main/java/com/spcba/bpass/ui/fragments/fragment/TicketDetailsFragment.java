package com.spcba.bpass.ui.fragments.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.spcba.bpass.R;
import com.spcba.bpass.data.datamodels.Ticket;
import com.spcba.bpass.data.datautils.StringUtils;
import com.spcba.bpass.databinding.FargmentTicketDetailsBinding;
import com.spcba.bpass.ui.fragments.dialog.TicketScannedDialog;
import com.spcba.bpass.ui.viewmodels.LobbyActivityViewModel;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class TicketDetailsFragment extends Fragment {
    private FargmentTicketDetailsBinding binder;
    private ImageView qrCodeIv;
    private TextView ticketDetailsTv;
    private TextView startDestination;
    private TextView endDestination;
    private TextView availability;
    private TextView busNumberTv;
    private TextView departureTimeTv;
    private TextView arriveTimeTv;
    private TextView fareTv;
    private NavController navController;

    private LobbyActivityViewModel viewModel;
    public static final String TICKET_REQ = "Ticket";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = FargmentTicketDetailsBinding.inflate(inflater, container, false);
        return binder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(LobbyActivityViewModel.class);
        navController = Navigation.findNavController(view);
        qrCodeIv = binder.qrCodeIv;
        ticketDetailsTv = binder.ticketDetailsLabel;
        availability = binder.availability;
        startDestination = binder.startDestination;
        endDestination = binder.endDestination;
        busNumberTv = binder.busNumberTv;
        departureTimeTv = binder.departureTimeTv;
        arriveTimeTv = binder.arriveTimeTv;
        fareTv = binder.fareTv;
        ticketDetailsTv.setOnClickListener(btn -> {
            navController.popBackStack();

        });
        viewModel.getSelectedTicketLiveData().observe(getViewLifecycleOwner(), selectedTicketEvent -> {
            if (selectedTicketEvent.isHandled()) return;
            Ticket ticket = selectedTicketEvent.getContentIfNotHandled();
            startDestination.setText(ticket.getDestination().getStartDestination());
            endDestination.setText(ticket.getDestination().getEndDestination());
            busNumberTv.setText(ticket.getBusNumber());
            fareTv.setText("₱" + ticket.getDestination().getFare());
            arriveTimeTv.setText(StringUtils.formatTime(ticket.getDestination().getExpectArriveTime()));
            departureTimeTv.setText(StringUtils.formatTime(ticket.getDestination().getExpectLeaveTime()));
            if (ticket.isUsed()) {
                availability.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_tv_lightgray));
                availability.setText("Used");
            } else {
                availability.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_tv_green));
                availability.setText("Available");
            }


            try {
                String qrCodeValue = TICKET_REQ + "-" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "-" + ticket.getId();
                Glide.with(requireContext()).asBitmap().load(createQrCodeBitMap(qrCodeValue)).into(qrCodeIv);

            } catch (WriterException e) {
                e.printStackTrace();
            }


        });
        viewModel.getTicketAccepted().observe(getViewLifecycleOwner(), getAcceptedTicketEvent -> {
            if (getAcceptedTicketEvent.isHandled()) return;
            if (getAcceptedTicketEvent.getContentIfNotHandled() != null) {
                availability.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_tv_lightgray));
                availability.setText("Used");
                TicketScannedDialog dialog = new TicketScannedDialog();
                dialog.show(getChildFragmentManager(), "Ticket Scanned");

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
