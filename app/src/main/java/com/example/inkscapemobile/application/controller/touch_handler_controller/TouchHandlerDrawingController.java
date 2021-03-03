package com.example.inkscapemobile.application.controller.touch_handler_controller;

import android.view.MotionEvent;

import com.example.inkscapemobile.application.Tool;
import com.example.inkscapemobile.application.ToolbarStatus;
import com.example.inkscapemobile.application.controller.uicontroller.BottomToolbarUiController;
import com.example.inkscapemobile.models.Project;
import com.example.inkscapemobile.models.Sketch;
import com.example.inkscapemobile.models.sketches.Drawing;

/**
 * handles events to create a new hand drawing
 * to draw, the draw tool must be enabled, it can be toggled on or off by the user
 * <p>
 * when no graphical element is selected, the drawing toolbar enabled and the screen touched, a new drawing is created
 * <p>
 * when a hand drawing sketch is selected, the drawing toolbar enabled and the screen touched, points are added to the
 * selected drawing
 */
public class TouchHandlerDrawingController implements TouchEventHandler {
    private Project activeProject;
    private ToolbarStatus toolbarStatus;
    private BottomToolbarUiController bottomToolbarUiController;

    public TouchHandlerDrawingController(Project activeProject, ToolbarStatus toolbarStatus, BottomToolbarUiController bottomToolbarUiController) {
        this.activeProject = activeProject;
        this.toolbarStatus = toolbarStatus;
        this.bottomToolbarUiController = bottomToolbarUiController;
    }

    @Override
    public void handleCanvasTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN
                && toolbarStatus.getSelectedTool() == Tool.draw
                && (toolbarStatus.getSelectedElement() == null || toolbarStatus.getSelectedElement() instanceof Drawing)) {

            Drawing handDrawing = createNewDrawing(event);
            addSketchToProject(handDrawing);
        }

        if ((event.getAction() == MotionEvent.ACTION_MOVE)
                && toolbarStatus.getSelectedTool() == Tool.draw
                && toolbarStatus.getSelectedElement() instanceof Drawing) {
            addPointsToDrawing(event);
        }
        if ((event.getAction() == MotionEvent.ACTION_UP)
                && toolbarStatus.getSelectedTool() == Tool.draw
                && toolbarStatus.getSelectedElement() instanceof Drawing) {
            Drawing selectedDrawing = (Drawing) toolbarStatus.getSelectedElement();
            selectedDrawing.finish(event.getX(), event.getY());
            resetBottomToolbar();
        }


    }

    /**
     * creates and returns a new drawing containing coordinates of the given event
     * <p>
     * NOTE: the event might contain a history with many coordinates which are aggregated by android
     * we need to test if we can leave out the history, which also could create holes in the drawing,
     * as many coordinates may be skipped
     *
     * @param event
     * @return
     */
    private Drawing createNewDrawing(MotionEvent event) {
        Drawing draw = new Drawing(toolbarStatus.getSelectedAttributes(), event.getX(), event.getY());
        return draw;
    }

    /**
     * adds points of the event to the selected drawing
     * NOTE: the event might contain a history with many coordinates which are aggregated by android
     * we need to test if we can leave out the history, which also could create holes in the drawing,
     * as many coordinates may be skipped
     *
     * @param event
     */
    private void addPointsToDrawing(MotionEvent event) {
        Drawing selectedDrawing = (Drawing) toolbarStatus.getSelectedElement();
        selectedDrawing.addToDrawing(event.getX(), event.getY());
    }

    private void addSketchToProject(Sketch sketch) {
        activeProject.getLayers()[toolbarStatus.getSelectedLayer()].addElementOnTop(sketch);
        toolbarStatus.setSelectedElement(sketch);
        activeProject.logProject();
    }

    private void resetBottomToolbar() {
        toolbarStatus.toggleTool(Tool.none);
        bottomToolbarUiController.resetBottomToolbarButtonHighlighting();
    }

}
