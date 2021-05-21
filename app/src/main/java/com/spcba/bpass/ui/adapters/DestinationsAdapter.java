package com.spcba.bpass.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.spcba.bpass.data.datamodels.Trip;
import com.spcba.bpass.data.datautils.Event;
import com.spcba.bpass.R;
import com.spcba.bpass.databinding.ItemDestinationBinding;

import java.util.ArrayList;
import java.util.List;

public class DestinationsAdapter extends RecyclerView.Adapter<DestinationsAdapter.DestinationsViewHolder> {
    private List<Trip> trips = new ArrayList<>();
    private MutableLiveData<Event<Trip>> onDestinationSelected = new MutableLiveData<>();
    public DestinationsAdapter() {

    }
    public void insertDestinations(List<Trip> trips){

        this.trips = trips;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public DestinationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_destination,parent,false);
        ItemDestinationBinding binder = ItemDestinationBinding.bind(view);
        return new DestinationsViewHolder(binder);

    }

    @Override
    public void onBindViewHolder(@NonNull DestinationsViewHolder holder, int position) {
        Trip trip = trips.get(position);
        holder.startDestination.setText(trip.getStartDestination());
        holder.endDestination.setText(trip.getEndDestination());
        holder.leaveTime.setText(trip.getExpectLeaveTime());
        holder.arriveTime.setText(trip.getExpectArriveTime());
        holder.fare.setText("₱"+ trip.getFare());

        holder.bind(trip);
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public LiveData<Event<Trip>> getDestinationSelected() {
        return onDestinationSelected;
    }

    class DestinationsViewHolder extends RecyclerView.ViewHolder{
        private View itemLayout;
        private TextView startDestination;
        private TextView endDestination;
        private TextView leaveTime;
        private TextView arriveTime;
        private TextView fare;
        public DestinationsViewHolder(@NonNull ItemDestinationBinding binder) {
            super(binder.getRoot());
            itemLayout = binder.itemLayout;
            startDestination = binder.startDestinationTv;
            endDestination = binder.endDestinationTv;
            leaveTime = binder.leaveTimeTv;
            arriveTime = binder.arriveTimeTv;
            fare = binder.fareTv;
        }
        public void bind(Trip trip){
            itemLayout.setOnClickListener(view ->{
                onDestinationSelected.setValue(new Event<>(trip));
            });

        }
    }

}
