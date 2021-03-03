package com.example.inkscapemobile.application.controller.uicontroller;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageButton;

import androidx.core.content.res.ResourcesCompat;

import com.example.inkscapemobile.R;
import com.example.inkscapemobile.activities.DrawingActivity;
import com.example.inkscapemobile.activities.bottomsheet.TextContentCreationBottomSheet;
import com.example.inkscapemobile.application.Tool;
import com.example.inkscapemobile.application.ToolbarStatus;
import com.example.inkscapemobile.models.Project;
import com.example.inkscapemobile.models.sketches.Text;

/**
 * UiController for the bottom toolbar, the tool selection.
 * Toggles the tool of the ToolbarStatus
 * When a tool is active, the background of the button gets blue highlighting
 */
public class BottomToolbarUiController extends UiController {
    private ImageButton removeSketchBtn;
    private ImageButton insertTextBtn;
    private ImageButton insertHandDrawingBtn;
    private ImageButton insertLineBtn;
    private ImageButton insertCircleBtn;
    private ImageButton insertTriangleBtn;
    private ImageButton insertRectangleBtn;
    private ImageButton selectedToolBtn = null;

    public BottomToolbarUiController(DrawingActivity mContext, Project activeProject, ToolbarStatus toolbarStatus) {
        super(mContext, activeProject, toolbarStatus);
        setUiElements();
        setBottomToolbarClickListeners();
    }

    /**
     * resets the highlighting of the selected button. e.g. after a tool is used and the sketch created,
     * the tool is disabled and the highlighting too
     */
    public void resetBottomToolbarButtonHighlighting() {
        selectedToolBtn.setBackgroundColor(Color.TRANSPARENT);
    }

    private void setBottomToolbarClickListeners() {
        // deletes the currently selected sketch
        removeSketchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getToolbarStatus().getSelectedElement() != null) {
                    getActiveProject().getLayers()[getToolbarStatus().getSelectedLayer()].removeElement(getToolbarStatus().getSelectedElement());
                    getToolbarStatus().setSelectedElement(null);
                }
            }
        });
        insertTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomToolbarButtonClicked(insertTextBtn, Tool.text);
            }
        });
        insertHandDrawingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomToolbarButtonClicked(insertHandDrawingBtn, Tool.draw);
            }
        });
        insertLineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomToolbarButtonClicked(insertLineBtn, Tool.line);
            }
        });
        insertCircleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomToolbarButtonClicked(insertCircleBtn, Tool.circle);
            }
        });
        insertTriangleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomToolbarButtonClicked(insertTriangleBtn, Tool.triangle);
            }
        });
        insertRectangleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomToolbarButtonClicked(insertRectangleBtn, Tool.rectangle);
            }
        });
    }

    /**
     * Triggered, when the given ImageButton is clicked, representing the given tool.
     * Functionality put together into this method to prevent code duplication
     *
     * @param btn clicked button
     * @param tool represented by this button
     */
    private void bottomToolbarButtonClicked(ImageButton btn, Tool tool) {
        if (selectedToolBtn != null) {
            resetBottomToolbarButtonHighlighting();
        }

        //if text element selected and text button clicked, modify text instead
        if (tool == Tool.text && getToolbarStatus().getSelectedElement() instanceof Text) {
            TextContentCreationBottomSheet bottomSheet = new TextContentCreationBottomSheet((Text) getToolbarStatus().getSelectedElement());
            bottomSheet.show(getContext().getSupportFragmentManager(), "text content");

        } else {
            //if already selected, un-select
            if (getToolbarStatus().getSelectedTool() == tool) {
                getToolbarStatus().toggleTool(Tool.none);
                resetBottomToolbarButtonHighlighting();
            } else {
                //if not selected, select
                getToolbarStatus().toggleTool(tool);
                getToolbarStatus().setSelectedElement(null);
                selectedToolBtn = btn;
                btn.setBackgroundColor(ResourcesCompat.getColor(getContext().getResources(), R.color.accent_light, null));
            }
        }
    }


    private void setUiElements() {
        removeSketchBtn = getContext().findViewById(R.id.remove_sketch_btn);
        insertTextBtn = getContext().findViewById(R.id.insert_text_sketch_btn);
        insertHandDrawingBtn = getContext().findViewById(R.id.insert_hand_drawing_sketch_btn);
        insertLineBtn = getContext().findViewById(R.id.insert_line_sketch_btn);
        insertCircleBtn = getContext().findViewById(R.id.insert_circle_sketch_btn);
        insertTriangleBtn = getContext().findViewById(R.id.insert_triangle_sketch_btn);
        insertRectangleBtn = getContext().findViewById(R.id.insert_rectangle_sketch_btn);
    }


}
