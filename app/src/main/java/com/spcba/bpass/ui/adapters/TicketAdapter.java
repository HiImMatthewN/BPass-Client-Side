package com.spcba.bpass.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.spcba.bpass.data.datautils.Event;
import com.spcba.bpass.R;
import com.spcba.bpass.data.datamodels.Ticket;
import com.spcba.bpass.databinding.ItemTicketBinding;

import java.util.ArrayList;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketAdapterViewHolder> {
    private List<Ticket> tickets = new ArrayList<>();
    private static final String TAG = "TicketAdapter";
    private MutableLiveData<Event<Ticket>> selectedTicket = new MutableLiveData<>();

    public TicketAdapter() {
    }

    public void insertData(List<Ticket> tickets) {
        this.tickets = tickets;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public TicketAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        ItemTicketBinding binder = ItemTicketBinding.bind(view);
        return new TicketAdapterViewHolder(binder);

    }

    @Override
    public void onBindViewHolder(@NonNull TicketAdapterViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);
        holder.startDestination.setText(ticket.getTrip().getStartDestination());
        holder.endDestination.setText(ticket.getTrip().getEndDestination());
        if (ticket.isUsed()) {
            holder.availability.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_ticket_used));
            holder.availability.setText("Used");
        } else {
            holder.availability.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_ticket_available));
            holder.availability.setText("Available");
        }
        holder.bind(ticket);

    }

    public LiveData<Event<Ticket>> getSelectedTicket() {
        return selectedTicket;
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    class TicketAdapterViewHolder extends RecyclerView.ViewHolder {
        private View itemLayout;
        private TextView startDestination;
        private TextView endDestination;
        private TextView availability;

        public TicketAdapterViewHolder(@NonNull ItemTicketBinding binder) {
            super(binder.getRoot());
            itemLayout = binder.itemLayout;
            startDestination = binder.startDestinationTv;
            endDestination = binder.endDestinationTv;
            availability = binder.availability;
        }

        public void bind(Ticket ticket) {
            itemLayout.setOnClickListener(view -> {
                selectedTicket.postValue(new Event<>(ticket));

            });

        }
    }
}
