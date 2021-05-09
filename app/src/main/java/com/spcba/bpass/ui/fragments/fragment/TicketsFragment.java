package com.spcba.bpass.ui.fragments.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.spcba.bpass.R;
import com.spcba.bpass.databinding.FragmentTicketsBinding;
import com.spcba.bpass.ui.adapters.TicketAdapter;
import com.spcba.bpass.ui.viewmodels.LobbyActivityViewModel;

public class TicketsFragment extends Fragment {
    private FragmentTicketsBinding binder;
    private RecyclerView ticketRv;
    private TicketAdapter ticketAdapter;
    private LobbyActivityViewModel viewModel;
    private NavController navController;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = FragmentTicketsBinding.inflate(inflater,container,false);
        return binder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(requireActivity()).get(LobbyActivityViewModel.class);
        ticketRv = binder.ticketsRv;
        ticketAdapter = new TicketAdapter();
        ticketRv.setAdapter(ticketAdapter);

        viewModel.getTickets().observe(getViewLifecycleOwner(),tickets -> {
            ticketAdapter.insertData(tickets);
        });
        ticketAdapter.getSelectedTicket().observe(getViewLifecycleOwner(),selectedTicketEvent->{
                    if (selectedTicketEvent.isHandled()) return;
                    navController.navigate(R.id.action_transactionHistoryFragment_to_ticketDetailsFragment);



        });


        


    }
}
