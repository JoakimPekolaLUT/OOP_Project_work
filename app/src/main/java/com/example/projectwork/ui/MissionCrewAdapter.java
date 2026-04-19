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

// This adapter is used in Mission Control to display crew members.
// It supports selecting two crew members
// and visually shows which ones are selected.
public class MissionCrewAdapter extends RecyclerView.Adapter<MissionCrewAdapter.MissionCrewViewHolder> {

    // Interface for handling clicks on crew members
    public interface OnCrewClickListener {
        void onCrewClick(CrewMember crewMember);
    }

    private final List<CrewMember> crewList;
    private final OnCrewClickListener listener;

    // Stores the selected crew members by their id
    private int selectedCrew1Id = -1;
    private int selectedCrew2Id = -1;

    // Creates the adapter with crew data and click listener
    public MissionCrewAdapter(List<CrewMember> crewList, OnCrewClickListener listener) {
        this.crewList = crewList;
        this.listener = listener;
    }

    // Updates which crew members are selected and refreshes the UI
    public void setSelectedCrewIds(int crew1Id, int crew2Id) {
        this.selectedCrew1Id = crew1Id;
        this.selectedCrew2Id = crew2Id;
        notifyDataSetChanged();
    }

    // Creates a new row view for the RecyclerView
    @NonNull
    @Override
    public MissionCrewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_crew, parent, false);
        return new MissionCrewViewHolder(view);
    }

    // Binds crew member data to each row
    @Override
    public void onBindViewHolder(@NonNull MissionCrewViewHolder holder, int position) {
        CrewMember crewMember = crewList.get(position);

        // Show name and type
        holder.textName.setText(crewMember.getType() + " - " + crewMember.getName());

        // Show stats
        holder.textStats.setText(
                "LVL: " + crewMember.getLevel() +
                        " | XP: " + crewMember.getXp() +
                        " | Skill: " + crewMember.getBaseSkill() +
                        " | Def: " + crewMember.getDefense() +
                        " | Energy: " + crewMember.getEnergy() + "/" + crewMember.getMaxEnergy()
        );

        // Show correct image
        holder.imageCrew.setImageResource(getImageForType(crewMember.getType()));

        // Highlight Crew 1
        if (crewMember.getId() == selectedCrew1Id) {
            holder.itemView.setBackgroundColor(0xFFD1E7FF); // light blue
            holder.textSelectedLabel.setVisibility(View.VISIBLE);
            holder.textSelectedLabel.setText("CREW 1");

            // Highlight Crew 2
        } else if (crewMember.getId() == selectedCrew2Id) {
            holder.itemView.setBackgroundColor(0xFFDFF5D8); // light green
            holder.textSelectedLabel.setVisibility(View.VISIBLE);
            holder.textSelectedLabel.setText("CREW 2");

            // Not selected
        } else {
            holder.itemView.setBackgroundColor(0x00000000);
            holder.textSelectedLabel.setVisibility(View.GONE);
        }

        // Handle click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCrewClick(crewMember);
            }
        });
    }

    // Returns number of crew members
    @Override
    public int getItemCount() {
        return crewList.size();
    }

    // Returns correct image based on crew type
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

    // ViewHolder for one row in the RecyclerView
    public static class MissionCrewViewHolder extends RecyclerView.ViewHolder {
        ImageView imageCrew;
        TextView textName;
        TextView textStats;
        TextView textSelectedLabel;

        public MissionCrewViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCrew = itemView.findViewById(R.id.imageCrew);
            textName = itemView.findViewById(R.id.textName);
            textStats = itemView.findViewById(R.id.textStats);
            textSelectedLabel = itemView.findViewById(R.id.textSelectedLabel);
        }
    }
}