package com.example.inkscapemobile.activities.bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.inkscapemobile.R;
import com.example.inkscapemobile.application.ToolbarStatus;
import com.example.inkscapemobile.models.Project;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * Android bottom sheet ui element for switching layers or toggling their visibility
 */
public class LayerMenuBottomSheet extends BottomSheetDialogFragment {
    private Project activeProject;
    private ToolbarStatus toolbarStatus;
    private RadioGroup layerSelectionGroup;
    private ToggleButton visibilityTopLayer;
    private ToggleButton visibilityMiddleLayer;
    private ToggleButton visibilityBottomLayer;

    public LayerMenuBottomSheet(Project activeProject, ToolbarStatus toolbarStatus) {
        this.activeProject = activeProject;
        this.toolbarStatus = toolbarStatus;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.layer_menu_bottom_sheet, container, false);
        layerSelectionGroup = mView.findViewById(R.id.layer_toggle_group);
        visibilityTopLayer = mView.findViewById(R.id.top_layer_visibility);
        visibilityMiddleLayer = mView.findViewById(R.id.middle_layer_visibility);
        visibilityBottomLayer = mView.findViewById(R.id.bottom_layer_visibility);
        setViewToCurrentToolbarStatus();
        setClickListeners();
        return mView;
    }

    private void setViewToCurrentToolbarStatus() {
        switch (toolbarStatus.getSelectedLayer()) {
            case 0:
                layerSelectionGroup.check(R.id.top_layer_toggle);
                break;
            case 1:
                layerSelectionGroup.check(R.id.middle_layer_toggle);
                break;
            case 2:
                layerSelectionGroup.check(R.id.bottom_layer_toggle);
                break;
        }
        visibilityTopLayer.setChecked(activeProject.getLayers()[0].isVisible());
        visibilityMiddleLayer.setChecked(activeProject.getLayers()[1].isVisible());
        visibilityBottomLayer.setChecked(activeProject.getLayers()[2].isVisible());
    }

    private void setClickListeners() {
        layerSelectionGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.top_layer_toggle:
                        toolbarStatus.setSelectedLayer(0);
                        break;
                    case R.id.middle_layer_toggle:
                        toolbarStatus.setSelectedLayer(1);
                        break;
                    case R.id.bottom_layer_toggle:
                        toolbarStatus.setSelectedLayer(2);
                        break;
                }
            }
        });

        visibilityTopLayer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                activeProject.getLayers()[0].setVisibility(isChecked);
            }
        });
        visibilityMiddleLayer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                activeProject.getLayers()[1].setVisibility(isChecked);
            }
        });
        visibilityBottomLayer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                activeProject.getLayers()[2].setVisibility(isChecked);
            }
        });
    }
}
