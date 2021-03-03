package com.example.inkscapemobile.application.controller.uicontroller;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.inkscapemobile.R;
import com.example.inkscapemobile.activities.DrawingActivity;
import com.example.inkscapemobile.activities.bottomsheet.FontSizeMenuBottomSheet;
import com.example.inkscapemobile.activities.bottomsheet.LayerMenuBottomSheet;
import com.example.inkscapemobile.activities.bottomsheet.ProjectNameBottomSheet;
import com.example.inkscapemobile.activities.bottomsheet.SketchWidthMenuBottomSheet;
import com.example.inkscapemobile.activities.bottomsheet.StrokeMenuBottomSheet;
import com.example.inkscapemobile.application.ProjectFileHandler;
import com.example.inkscapemobile.application.ToolbarStatus;
import com.example.inkscapemobile.models.Project;
import com.example.inkscapemobile.models.attributes.Attribute;
import com.example.inkscapemobile.models.attributes.AttributeType;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * UiController for the top toolbar when not in group mode.
 * Essentially opens bottom sheets to change the attributes.
 */
public class TopToolbarUiController extends UiController {
    private ProjectFileHandler fileHandler;
    private Button projectName;
    private ImageButton saveProjectBtn;
    private ImageButton layerBtn;
    private ImageButton colorBtn;
    private ImageButton fontSizeBtn;
    private ImageButton strokeBtn;
    private ImageButton widthBtn;
    DrawingActivity mContext;

    public TopToolbarUiController(DrawingActivity mContext, Project activeProject, ToolbarStatus toolbarStatus) {
        super(mContext, activeProject, toolbarStatus);
        fileHandler = new ProjectFileHandler(mContext);
        setUiElements();
        projectName.setText(activeProject.getProjectName());
        setTopToolbarClickListeners();
    }


    private void setTopToolbarClickListeners() {
        projectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjectNameBottomSheet projectNameBottomSheet = new ProjectNameBottomSheet(getActiveProject(), projectName);
                projectNameBottomSheet.show(getContext().getSupportFragmentManager(), "project renaming");
            }
        });

        saveProjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileHandler.saveProject(getActiveProject());
            }
        });

        layerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayerMenuBottomSheet layerMenuBottomSheet = new LayerMenuBottomSheet(getActiveProject(), getToolbarStatus());
                layerMenuBottomSheet.show(getContext().getSupportFragmentManager(), "layer_menu");
            }
        });

        colorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        fontSizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FontSizeMenuBottomSheet fontSizeMenuBottomSheet = new FontSizeMenuBottomSheet(getToolbarStatus());
                fontSizeMenuBottomSheet.show(getContext().getSupportFragmentManager(), "fontsize_menu");
            }
        });

        strokeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrokeMenuBottomSheet strokeMenuBottomSheet = new StrokeMenuBottomSheet(getToolbarStatus());
                strokeMenuBottomSheet.show(getContext().getSupportFragmentManager(), "strokeWidth_menu");
            }
        });

        widthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SketchWidthMenuBottomSheet sketchWidthMenuBottomSheet = new SketchWidthMenuBottomSheet(getToolbarStatus());
                sketchWidthMenuBottomSheet.show(getContext().getSupportFragmentManager(), "sketch_width_menu");
            }
        });
    }

    private void openColorPicker() {
        int currentColor = (Integer) (getToolbarStatus().getSelectedAttributes().get(AttributeType.color).getValue());
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(getContext(), currentColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                getToolbarStatus().selectAttribute(Attribute.createColorAttribute(color));
            }
        });
        colorPicker.show();
    }


    private void setUiElements() {
        projectName = getContext().findViewById(R.id.project_name);
        saveProjectBtn = getContext().findViewById(R.id.drawing_activity_menu_btn);
        layerBtn = getContext().findViewById(R.id.layer_btn);
        colorBtn = getContext().findViewById(R.id.color_btn);
        fontSizeBtn = getContext().findViewById(R.id.fontsiize_btn);
        strokeBtn = getContext().findViewById(R.id.stroke_btn);
        widthBtn = getContext().findViewById(R.id.width_btn);
    }
}
