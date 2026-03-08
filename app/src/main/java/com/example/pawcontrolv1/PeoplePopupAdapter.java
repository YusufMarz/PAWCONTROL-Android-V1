package com.example.pawcontrolv1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PeoplePopupAdapter extends RecyclerView.Adapter<PeoplePopupAdapter.PersonViewHolder> {

    public interface OnPersonClickListener {
        void onPersonClick(Person person);
    }

    private final List<Person>          people;
    private final OnPersonClickListener listener;

    public PeoplePopupAdapter(List<Person> people, OnPersonClickListener listener) {
        this.people   = people;
        this.listener = listener;
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView  name;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.personAvatar);
            name   = itemView.findViewById(R.id.personName);
        }
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_person, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        Person person = people.get(position);
        holder.name.setText(person.getName());
        holder.avatar.setImageResource(person.getAvatarRes());
        holder.itemView.setOnClickListener(v -> listener.onPersonClick(person));
    }

    @Override
    public int getItemCount() { return people.size(); }
}