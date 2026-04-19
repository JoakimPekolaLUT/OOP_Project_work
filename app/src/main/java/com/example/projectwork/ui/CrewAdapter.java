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

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.CrewViewHolder> {

    public interface OnCrewClickListener {
        void onCrewClick(CrewMember crewMember);
    }

    private final List<CrewMember> crewList;
    private final OnCrewClickListener listener;
    private int selectedPosition = -1;

    public CrewAdapter(List<CrewMember> crewList, OnCrewClickListener listener) {
        this.crewList = crewList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CrewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_crew, parent, false);
        return new CrewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CrewViewHolder holder, int position) {
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

        if (position == selectedPosition) {
            holder.itemView.setBackgroundColor(0xFFE0D7F5);
        } else {
            holder.itemView.setBackgroundColor(0x00000000);
        }

        holder.itemView.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();

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