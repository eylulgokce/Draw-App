package com.example.inkscapemobile.application.controller.uicontroller;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.example.inkscapemobile.R;
import com.example.inkscapemobile.activities.DrawingActivity;
import com.example.inkscapemobile.application.GroupTool;
import com.example.inkscapemobile.application.ToolbarStatus;
import com.example.inkscapemobile.models.Group;
import com.example.inkscapemobile.models.Project;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Special UiController for the group toolbar when in group mode.
 * Toggles the group tool in the toolbarStatus
 */
public class GroupUiController extends UiController {
    private Toolbar bottomToolbar;
    private LinearLayout groupToolbar;
    private ImageButton enableGroupModeBtn;
    private FloatingActionButton disableGroupModeBtn;
    private FloatingActionButton newGroupBtn;
    private FloatingActionButton addSketchToGroupBtn;
    private FloatingActionButton removeSketchFromGroupBtn;
    private FloatingActionButton duplicateGroupBtn;
    private FloatingActionButton enabledButton;

    public GroupUiController(DrawingActivity mContext, Project activeProject, ToolbarStatus toolbarStatus) {
        super(mContext, activeProject, toolbarStatus);
        setUiElements();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        enableGroupModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getToolbarStatus().setGroupMode(true);
                bottomToolbar.setVisibility(View.GONE);
                enableGroupModeBtn.setVisibility(View.GONE);
                groupToolbar.setVisibility(View.VISIBLE);
                getActiveProject().getLayers()[getToolbarStatus().getSelectedLayer()].setGroupVisibility(true);
            }
        });

        disableGroupModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getToolbarStatus().setGroupMode(false);
                bottomToolbar.setVisibility(View.VISIBLE);
                enableGroupModeBtn.setVisibility(View.VISIBLE);
                groupToolbar.setVisibility(View.GONE);
                getToolbarStatus().setSelectedElement(null);
                getActiveProject().getLayers()[getToolbarStatus().getSelectedLayer()].setGroupVisibility(false);
            }
        });

        newGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGroupTool(newGroupBtn, GroupTool.new_group);
            }
        });

        addSketchToGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGroupTool(addSketchToGroupBtn, GroupTool.add_sketch);
            }
        });

        removeSketchFromGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGroupTool(removeSketchFromGroupBtn, GroupTool.remove_sketch);
            }
        });

        duplicateGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getToolbarStatus().getSelectedElement() instanceof Group) {
                    Group duplicate = ((Group) getToolbarStatus().getSelectedElement()).duplicateGroup();
                    duplicate.moveBy(100, 100);
                    duplicate.setVisible(true);
                    getActiveProject().getLayers()[getToolbarStatus().getSelectedLayer()].addElementOnTop(duplicate);
                    getToolbarStatus().setSelectedElement(duplicate);
                }
            }
        });

    }

    public void resetNewGroupButton() {
        if (enabledButton == newGroupBtn) {
            selectGroupTool(newGroupBtn, GroupTool.new_group);
        }
    }

    private void selectGroupTool(FloatingActionButton button, GroupTool groupTool) {
        if (getToolbarStatus().getGroupTool() == groupTool) {
            getToolbarStatus().setGroupTool(GroupTool.none);
            enabledButton = null;
            toggleGroupButtonColor(button, false);
        } else {
            if (enabledButton != null) {
                toggleGroupButtonColor(enabledButton, false);
            }
            enabledButton = button;
            toggleGroupButtonColor(button, true);
            getToolbarStatus().setGroupTool(groupTool);
        }
    }

    private void toggleGroupButtonColor(FloatingActionButton button, boolean toggle) {
        if (toggle) {
            button.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getContext().getResources(), R.color.accent, null)));
            button.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        } else {
            button.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getContext().getResources(), R.color.accent_light, null)));
            button.setImageTintList(ColorStateList.valueOf(Color.BLACK));
        }
    }

    private void setUiElements() {
        bottomToolbar = getContext().findViewById(R.id.bottomDrawToolbar);
        groupToolbar = getContext().findViewById(R.id.group_btn_layout);
        enableGroupModeBtn = getContext().findViewById(R.id.group_enable_btn);
        disableGroupModeBtn = getContext().findViewById(R.id.group_disable_btn);
        newGroupBtn = getContext().findViewById(R.id.group_new_group_btn);
        addSketchToGroupBtn = getContext().findViewById(R.id.group_add_sketch_btn);
        removeSketchFromGroupBtn = getContext().findViewById(R.id.group_remove_sketch_btn);
        duplicateGroupBtn = getContext().findViewById(R.id.group_duplicate_btn);
    }
}
