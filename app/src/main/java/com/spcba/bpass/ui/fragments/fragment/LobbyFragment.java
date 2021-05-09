package com.spcba.bpass.ui.fragments.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.spcba.bpass.data.datamodels.Destination;
import com.spcba.bpass.data.datautils.ScheduleData;
import com.spcba.bpass.databinding.FragmentLobbyBinding;
import com.spcba.bpass.ui.adapters.DestinationsAdapter;
import com.spcba.bpass.ui.fragments.dialog.CheckoutDialog;
import com.spcba.bpass.ui.viewmodels.LobbyActivityViewModel;

import java.util.ArrayList;

public class LobbyFragment extends Fragment {
    private FragmentLobbyBinding binder;
    private RecyclerView destinationsRv;
    private TextView nameTv;
    private TextView balanceTv;
    private CircularImageView userProfilePic;
    private DestinationsAdapter destinationsAdapter;
    private Chip chipOne;
    private Chip chipTwo;
    private Chip chipThree;
    private ArrayList<Destination> displayedDestinations = new ArrayList<>();
    private static final String TAG = "LobbyFragment";

    private ArrayList<Destination> marketMarketSchedule = ScheduleData.getMarketMarketSched();
    private ArrayList<Destination> megaMallSchedule = ScheduleData.getMegaMallSched();
    private ArrayList<Destination> cubaoSchedule = ScheduleData.getCubaoSchedule();

    private LobbyActivityViewModel viewModel;
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
        destinationsRv = binder.destinationsRv;
        nameTv = binder.userName;
        balanceTv = binder.balanceTv;
        userProfilePic = binder.userProfilePic;
        destinationsAdapter = new DestinationsAdapter();
        destinationsRv.setAdapter(destinationsAdapter);


        chipOne = binder.chipOne;
        chipTwo = binder.chipTwo;
        chipThree = binder.chipThree;


        chipOne.setOnCheckedChangeListener((buttonView, isChecked) -> {
            addMarketMarketSchedule(isChecked);
            destinationsAdapter.insertDestinations(displayedDestinations);
        });
        chipTwo.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            addMegMallSchedule(isChecked);
            destinationsAdapter.insertDestinations(displayedDestinations);


        }));

        chipThree.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            addCubaoSchedule(isChecked);
            destinationsAdapter.insertDestinations(displayedDestinations);
        }));

        chipOne.setChecked(true);

        destinationsAdapter.getDestinationSelected().observe(getViewLifecycleOwner(),onDestinationSelectEvent ->{
                if (onDestinationSelectEvent.isHandled()) return;
                Destination destination = onDestinationSelectEvent.getContentIfNotHandled();
                viewModel.setSelectedDestination(destination);
                showCheckoutDialog();

        });

        viewModel.getUser().observe(getViewLifecycleOwner(),user -> {
            if (user == null) return;
            nameTv.setText("Welcome "+ user.getName()+"!");
            balanceTv.setText(String.format("₱%.2f", user.getBalance()));
            Glide.with(requireContext()).load(Uri.parse(user.getProfilePicUrl())).into(userProfilePic);
            fadeOutView(nameTv);

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
    private void showCheckoutDialog(){
        CheckoutDialog dialog = new CheckoutDialog();
        dialog.show(getChildFragmentManager(),"Checkout");


    }
    private void addMegMallSchedule(boolean value){
        if (value)
            displayedDestinations.addAll(megaMallSchedule);
        else
            displayedDestinations.removeAll(megaMallSchedule);

    }
    private void addMarketMarketSchedule(boolean value){
        if (value)
            displayedDestinations.addAll(marketMarketSchedule);
        else
            displayedDestinations.removeAll(marketMarketSchedule);

    }
    private void addCubaoSchedule(boolean value){
        if (value)
            displayedDestinations.addAll(cubaoSchedule);
        else
            displayedDestinations.removeAll(cubaoSchedule);

    }
}
