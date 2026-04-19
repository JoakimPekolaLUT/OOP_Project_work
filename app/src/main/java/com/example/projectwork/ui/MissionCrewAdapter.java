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

public class MissionCrewAdapter extends RecyclerView.Adapter<MissionCrewAdapter.MissionCrewViewHolder> {

    public interface OnCrewClickListener {
        void onCrewClick(CrewMember crewMember);
    }

    private final List<CrewMember> crewList;
    private final OnCrewClickListener listener;

    private int selectedCrew1Id = -1;
    private int selectedCrew2Id = -1;

    public MissionCrewAdapter(List<CrewMember> crewList, OnCrewClickListener listener) {
        this.crewList = crewList;
        this.listener = listener;
    }

    public void setSelectedCrewIds(int crew1Id, int crew2Id) {
        this.selectedCrew1Id = crew1Id;
        this.selectedCrew2Id = crew2Id;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MissionCrewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_crew, parent, false);
        return new MissionCrewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MissionCrewViewHolder holder, int position) {
        CrewMember crewMember = crewList.get(position);

        holder.textName.setText(crewMember.getType() + " - " + crewMember.getName());
        holder.textStats.setText(
                "LVL: " + crewMember.getLevel() +
                        " | XP: " + crewMember.getXp() +
                        " | Skill: " + crewMember.getBaseSkill() +
                        " | Def: " + crewMember.getDefense() +
                        " | Energy: " + crewMember.getEnergy() + "/" + crewMember.getMaxEnergy()
        );

        holder.imageCrew.setImageResource(getImageForType(crewMember.getType()));

        if (crewMember.getId() == selectedCrew1Id) {
            holder.itemView.setBackgroundColor(0xFFD1E7FF); // light blue
            holder.textSelectedLabel.setVisibility(View.VISIBLE);
            holder.textSelectedLabel.setText("CREW 1");
        } else if (crewMember.getId() == selectedCrew2Id) {
            holder.itemView.setBackgroundColor(0xFFDFF5D8); // light green
            holder.textSelectedLabel.setVisibility(View.VISIBLE);
            holder.textSelectedLabel.setText("CREW 2");
        } else {
            holder.itemView.setBackgroundColor(0x00000000);
            holder.textSelectedLabel.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCrewClick(crewMember);
            }
        });
    }

    @Override
    public int getItemCount() {
        return crewList.size();
    }

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