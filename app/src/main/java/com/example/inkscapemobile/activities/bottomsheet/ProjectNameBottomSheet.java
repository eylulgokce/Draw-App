package com.example.inkscapemobile.activities.bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.inkscapemobile.R;
import com.example.inkscapemobile.models.Project;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * Android bottom sheet ui element for changing the project name
 */
public class ProjectNameBottomSheet extends BottomSheetDialogFragment {
    private Project activeProject;
    private Button confirmButton;
    private EditText projectNameEditText;
    private Button projectRenamingButton;


    public ProjectNameBottomSheet(Project activeProject, Button projectRenamingButton) {
        this.activeProject = activeProject;
        this.projectRenamingButton = projectRenamingButton;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.project_renaming_bottom_sheet, container, false);
        projectNameEditText = mView.findViewById(R.id.project_name_edit_text);
        confirmButton = mView.findViewById(R.id.project_name_confirm_btn);
        setToStoredValue();
        setOnClickListener();
        return mView;
    }


    private void setToStoredValue() {
        projectNameEditText.setText(activeProject.getProjectName());
    }

    private void setOnClickListener() {
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeProject.setProjectName(projectNameEditText.getText().toString());
                projectRenamingButton.setText(activeProject.getProjectName());
                dismiss();
            }
        });
    }
}
