package com.example.pawcontrolv1.ShelterVet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.pawcontrolv1.R;

import java.util.List;

public class PetServiceAdapter extends RecyclerView.Adapter<PetServiceAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(PetService service);
    }

    private List<PetService> items;
    private final OnItemClickListener listener;

    public PetServiceAdapter(List<PetService> items, OnItemClickListener listener) {
        this.items    = items;
        this.listener = listener;
    }

    /** Swap the dataset and refresh the list */
    public void updateData(List<PetService> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pet_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PetService service = items.get(position);
        holder.tvName.setText(service.getName());
        holder.tvAddress.setText(service.getAddress());
        holder.tvContact.setText("Contact: " + service.getContact());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(service));
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvContact;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName    = itemView.findViewById(R.id.tvServiceName);
            tvAddress = itemView.findViewById(R.id.tvServiceAddress);
            tvContact = itemView.findViewById(R.id.tvServiceContact);
        }
    }
}