package com.spcba.bpass.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.spcba.bpass.Event;
import com.spcba.bpass.R;
import com.spcba.bpass.databinding.ItemDestinationBinding;
import com.spcba.bpass.data.datamodels.Destination;

import java.util.ArrayList;
import java.util.List;

public class DestinationsAdapter extends RecyclerView.Adapter<DestinationsAdapter.DestinationsViewHolder> {
    private List<Destination> destinations = new ArrayList<>();
    private MutableLiveData<Event<Destination>> onDestinationSelected = new MutableLiveData<>();
    public DestinationsAdapter() {

    }
    public void insertDestinations(List<Destination> destinations){

        this.destinations = destinations;
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
        Destination destination = destinations.get(position);
        holder.startDestination.setText(destination.getStartDestination());
        holder.endDestination.setText(destination.getEndDestination());
        holder.leaveTime.setText(destination.getExpectLeaveTime());
        holder.arriveTime.setText(destination.getExpectArriveTime());
        holder.fare.setText("₱"+destination.getFare());

        holder.bind(destination);
    }

    @Override
    public int getItemCount() {
        return destinations.size();
    }

    public LiveData<Event<Destination>> getDestinationSelected() {
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
        public void bind(Destination destination){
            itemLayout.setOnClickListener(view ->{
                onDestinationSelected.setValue(new Event<>(destination));
            });

        }
    }

}
