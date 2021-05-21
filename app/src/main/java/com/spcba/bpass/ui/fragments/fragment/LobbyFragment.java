package com.spcba.bpass.ui.fragments.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.spcba.bpass.data.datautils.ScheduleData;
import com.spcba.bpass.databinding.FragmentLobbyBinding;
import com.spcba.bpass.ui.adapters.DestinationsAdapter;
import com.spcba.bpass.ui.fragments.dialog.CalendarDialog;
import com.spcba.bpass.ui.fragments.dialog.CheckoutDialog;
import com.spcba.bpass.ui.viewmodels.LobbyActivityViewModel;

import java.util.ArrayList;

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
    private ArrayList<Trip> displayedTrips = new ArrayList<>();
    private static final String TAG = "LobbyFragment";

    private ArrayList<Trip> marketMarketSchedule = ScheduleData.getMarketMarketSched();
    private ArrayList<Trip> megaMallSchedule = ScheduleData.getMegaMallSched();
    private ArrayList<Trip> cubaoSchedule = ScheduleData.getCubaoSchedule();

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


        balanceTv.setOnClickListener(btn->{
            navController.navigate(R.id.action_lobbyFragment_to_topUpFragment);
        });
        scheduleBtn.setOnClickListener(btn->{

            showCalendarDialog();
        });
        chipOne.setOnCheckedChangeListener((buttonView, isChecked) -> {
            addMarketMarketSchedule(isChecked);
            destinationsAdapter.insertDestinations(displayedTrips);
        });
        chipTwo.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            addMegMallSchedule(isChecked);
            destinationsAdapter.insertDestinations(displayedTrips);


        }));

        chipThree.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            addCubaoSchedule(isChecked);
            destinationsAdapter.insertDestinations(displayedTrips);
        }));

        chipOne.setChecked(true);

        destinationsAdapter.getDestinationSelected().observe(getViewLifecycleOwner(),onDestinationSelectEvent ->{
                if (onDestinationSelectEvent.isHandled()) return;
                Trip trip = onDestinationSelectEvent.getContentIfNotHandled();
                viewModel.setSelectedDestination(trip);
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

            for (Trip trip: trips){
                Log.d(TAG, "Fetched Trip " + trip.getTripSchedule());
            }

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
    private void addMegMallSchedule(boolean value){
        if (value)
            displayedTrips.addAll(megaMallSchedule);
        else
            displayedTrips.removeAll(megaMallSchedule);

    }
    private void addMarketMarketSchedule(boolean value){
        if (value)
            displayedTrips.addAll(marketMarketSchedule);
        else
            displayedTrips.removeAll(marketMarketSchedule);

    }
    private void addCubaoSchedule(boolean value){
        if (value)
            displayedTrips.addAll(cubaoSchedule);
        else
            displayedTrips.removeAll(cubaoSchedule);

    }
}
