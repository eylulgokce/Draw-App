package com.example.inkscapemobile.activities.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inkscapemobile.R;
import com.example.inkscapemobile.activities.ExportingActivity;
import com.example.inkscapemobile.application.storage.ProjectNameTuple;

import java.util.List;

/**
 * Adapter for the project recycler view in the HomeScreenActivity
 */
public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder> {
    private List<ProjectNameTuple> projectList;
    private OnItemClickListener clickListener;

    /**
     * nested interface for click listeners, which are created in the HomeScreenActivity in android
     * style(setting Click listeners by creating anonymous object)
     */
    public interface OnItemClickListener {
        void onItemClick(int position, Context context);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     * nested class ProjectViewHolder is part of the recycler view
     */
    public static class ProjectViewHolder extends RecyclerView.ViewHolder {
        public String projectId;
        public TextView projectNameTextView;
        public ImageButton projectOptionsBtn;

        public ProjectViewHolder(@NonNull View itemView, final OnItemClickListener itemClickListener) {
            super(itemView);
            projectNameTextView = itemView.findViewById(R.id.recycler_view_item_project_name);
            projectOptionsBtn = itemView.findViewById(R.id.recycler_view_item_options_btn);
            /*
             * On click listener for the options menu (icon with 3 dots).
             * Opens the ExportActivity with the projectId in the Intent
             */
            projectOptionsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(itemView.getContext(), ExportingActivity.class);
                    intent.putExtra("projectID",projectId);
                    itemView.getContext().startActivity(intent);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        int thisItemPosition = getAdapterPosition();
                        if (thisItemPosition != RecyclerView.NO_POSITION) {
                            itemClickListener.onItemClick(thisItemPosition, v.getContext());
                        }
                    }
                }
            });

        }
    }

    public ProjectsAdapter(List<ProjectNameTuple> projectList) {
        this.projectList = projectList;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_list_item, parent, false);
        ProjectViewHolder viewHolder = new ProjectViewHolder(v, clickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        ProjectNameTuple projectNameTuple = projectList.get(position);
        holder.projectId = projectNameTuple.projectId;
        holder.projectNameTextView.setText(projectNameTuple.projectName);
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }
}