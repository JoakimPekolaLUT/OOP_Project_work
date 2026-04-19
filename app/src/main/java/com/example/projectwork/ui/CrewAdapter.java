package com.example.projectwork.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectwork.R;
import com.example.projectwork.model.CrewMember;

import java.util.List;

// This adapter connects the crew member data to the RecyclerView.
// It is used to display crew members in screens like Quarters and Simulator.
public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.CrewViewHolder> {

    // Interface for handling clicks on crew members
    public interface OnCrewClickListener {
        void onCrewClick(CrewMember crewMember);
    }

    private final List<CrewMember> crewList;
    private final OnCrewClickListener listener;

    // Stores which item is currently selected
    private int selectedPosition = -1;

    // Creates the adapter with a list of crew members and a click listener
    public CrewAdapter(List<CrewMember> crewList, OnCrewClickListener listener) {
        this.crewList = crewList;
        this.listener = listener;
    }

    // Creates a new row view when RecyclerView needs one
    @NonNull
    @Override
    public CrewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_crew, parent, false);
        return new CrewViewHolder(view);
    }

    // Binds crew member data to one RecyclerView row
    @Override
    public void onBindViewHolder(@NonNull CrewViewHolder holder, int position) {
        CrewMember crewMember = crewList.get(position);

        // Show crew member name and class
        holder.textName.setText(crewMember.getType() + " - " + crewMember.getName());

        // Show important stats in the row
        holder.textStats.setText(
                "LVL: " + crewMember.getLevel() +
                        " | XP: " + crewMember.getXp() +
                        " | Skill: " + crewMember.getBaseSkill() +
                        " | Def: " + crewMember.getDefense() +
                        " | Energy: " + crewMember.getEnergy() + "/" + crewMember.getMaxEnergy()
        );

        // Show the correct image depending on the crew member class
        holder.imageCrew.setImageResource(getImageForType(crewMember.getType()));

        // Highlight the selected crew member
        if (position == selectedPosition) {
            holder.itemView.setBackgroundColor(0xFFE0D7F5);
        } else {
            holder.itemView.setBackgroundColor(0x00000000);
        }

        // Handle row click
        holder.itemView.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();

            if (listener != null) {
                listener.onCrewClick(crewMember);
            }
        });
    }

    // Returns how many crew members are in the list
    @Override
    public int getItemCount() {
        return crewList.size();
    }

    // Returns the correct image resource for each crew member type
    private int getImageForType(String type) {
        switch (type) {
            case "Pilot":
                return R.drawable.pilot;
            case "Engineer":
                return R.drawable.engineer;
            case "Medic":
                return R.drawable.medic;
            case "Scientist":
                return R.drawable.scientist;
            case "Soldier":
                return R.drawable.soldier;
            default:
                return R.drawable.pilot;
        }
    }

    // ViewHolder stores references to the views in one row
    public static class CrewViewHolder extends RecyclerView.ViewHolder {
        ImageView imageCrew;
        TextView textName;
        TextView textStats;

        public CrewViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCrew = itemView.findViewById(R.id.imageCrew);
            textName = itemView.findViewById(R.id.textName);
            textStats = itemView.findViewById(R.id.textStats);
        }
    }
}