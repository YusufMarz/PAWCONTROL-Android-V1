package com.example.pawcontrolv1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PetHouseAdapter extends RecyclerView.Adapter<PetHouseAdapter.ViewHolder> {

    private final Context context;
    private List<ShelterVetActivity.PetHouse> petHouseList;
    private final ShelterVetActivity.OnPetHouseClickListener clickListener;

    public PetHouseAdapter(Context context,
                           List<ShelterVetActivity.PetHouse> petHouseList,
                           ShelterVetActivity.OnPetHouseClickListener clickListener) {
        this.context       = context;
        this.petHouseList  = petHouseList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_pet_house, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShelterVetActivity.PetHouse house = petHouseList.get(position);
        holder.tvName.setText(house.getName());
        holder.tvAddress.setText(house.getAddress());
        holder.tvContact.setText("Contact: " + house.getContact());
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) clickListener.onPetHouseClick(house);
        });
    }

    @Override
    public int getItemCount() {
        return petHouseList == null ? 0 : petHouseList.size();
    }

    /** Call this to refresh the list (used by filter buttons) */
    public void updateList(List<ShelterVetActivity.PetHouse> newList) {
        this.petHouseList = newList;
        notifyDataSetChanged();
    }

    // ─────────────────────────────────────────────
    // ViewHolder
    // ─────────────────────────────────────────────

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvContact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName    = itemView.findViewById(R.id.tvPetHouseName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvContact = itemView.findViewById(R.id.tvContact);
        }
    }
}