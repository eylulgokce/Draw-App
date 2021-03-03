package com.example.inkscapemobile.application.controller.touch_handler_controller;

import android.view.MotionEvent;

import com.example.inkscapemobile.activities.DrawingActivity;
import com.example.inkscapemobile.activities.bottomsheet.TextContentCreationBottomSheet;
import com.example.inkscapemobile.application.Tool;
import com.example.inkscapemobile.application.ToolbarStatus;
import com.example.inkscapemobile.application.controller.uicontroller.BottomToolbarUiController;
import com.example.inkscapemobile.models.Project;
import com.example.inkscapemobile.models.Sketch;
import com.example.inkscapemobile.models.sketches.Circle;
import com.example.inkscapemobile.models.sketches.Line;
import com.example.inkscapemobile.models.sketches.Rectangle;
import com.example.inkscapemobile.models.sketches.Text;
import com.example.inkscapemobile.models.sketches.Triangle;

/**
 * Specific touch handler for creating sketches, accessed by the facade (TouchHandlerController)
 * Listens for actions to initiate creation of sketches.
 *
 * Sketches are created, when their tool is selected and touched on the canvas
 *
 * A line is created with touching the screen to set the start point, draging the line, and lifting to create the end point
 *
 */
public class TouchHandlerSketchController implements TouchEventHandler {
    private DrawingActivity mContext;
    private Project activeProject;
    private ToolbarStatus toolbarStatus;
    private BottomToolbarUiController bottomToolbarUiController;

    public TouchHandlerSketchController(DrawingActivity mContext, Project activeProject, ToolbarStatus toolbarStatus, BottomToolbarUiController bottomToolbarUiController) {
        this.mContext = mContext;
        this.activeProject = activeProject;
        this.toolbarStatus = toolbarStatus;
        this.bottomToolbarUiController = bottomToolbarUiController;
    }

    @Override
    public void handleCanvasTouchEvent(MotionEvent event) {
        if (toolbarStatus.isGroupMode()) {
            return;
        }
        if (event.getAction() == MotionEvent.ACTION_UP && toolbarStatus.getSelectedElement() == null) {
            switch (toolbarStatus.getSelectedTool()) {
                case text:
                    createText(event.getX(), event.getY());
                    break;
                case circle:
                    createCircle(event.getX(), event.getY());
                    break;
                case triangle:
                    createTriangle(event.getX(), event.getY());
                    break;
                case rectangle:
                    createRectangle(event.getX(), event.getY());
                    break;
            }
        }

        handleLineCreation(event);
    }

    /**
     * A line is created with the line tool, and then taping on the screen, dragging the line and lifting the cursor
     *
     * @param event touch event
     */
    private void handleLineCreation(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN
                && toolbarStatus.getSelectedElement() == null
                && toolbarStatus.getSelectedTool() == Tool.line) {
            createLine(event.getX(), event.getY());
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE
                && toolbarStatus.getSelectedTool() == Tool.line
                && toolbarStatus.getSelectedElement() instanceof Line) {
            ((Line) toolbarStatus.getSelectedElement()).setEndPoint(new float[]{event.getX(), event.getY()});
        }

        if (event.getAction() == MotionEvent.ACTION_UP
                && toolbarStatus.getSelectedTool() == Tool.line
                && toolbarStatus.getSelectedElement() instanceof Line) {

            resetBottomToolbar();
        }

    }

    /**
     * Creating a text sketch at the touch position and open bottom sheet to set the text content
     *
     * @param touchPositionX coordinates of the touch event
     * @param touchPositionY coordinates of the touch event
     */
    private void createText(float touchPositionX, float touchPositionY) {
        Text text = new Text(toolbarStatus.getSelectedAttributes(), touchPositionX, touchPositionY, "New Text");
        addSketchToProject(text);
        TextContentCreationBottomSheet bottomSheet = new TextContentCreationBottomSheet(text);
        bottomSheet.show(mContext.getSupportFragmentManager(), "text content");
    }

    /**
     * Initially create the line with the starting coordinate but don't release the tool
     *
     * @param touchPositionX coordinates of the touch event
     * @param touchPositionY coordinates of the touch event
     */
    private void createLine(float touchPositionX, float touchPositionY) {
        float[] startPoint = {touchPositionX, touchPositionY};
        float[] endPoint = {touchPositionX, touchPositionY};
        Line insertedLine = new Line(toolbarStatus.getSelectedAttributes(), startPoint, endPoint);
        activeProject.getLayers()[toolbarStatus.getSelectedLayer()].addElementOnTop(insertedLine);
        toolbarStatus.setSelectedElement(insertedLine);
        activeProject.logProject();
    }

    /**
     * create and insert a circle sketch
     *
     * @param touchPositionX coordinates of the touch event
     * @param touchPositionY coordinates of the touch event
     */
    private void createCircle(float touchPositionX, float touchPositionY) {
        Circle circle = new Circle(toolbarStatus.getSelectedAttributes(), touchPositionX, touchPositionY);
        addSketchToProject(circle);
    }

    /**
     * create and insert a triangle sketch
     *
     * @param touchPositionX coordinates of the touch event
     * @param touchPositionY coordinates of the touch event
     */
    private void createTriangle(float touchPositionX, float touchPositionY) {
        Triangle triangle = new Triangle(toolbarStatus.getSelectedAttributes(), new float[]{touchPositionX, touchPositionY});
        addSketchToProject(triangle);
    }

    /**
     * create and insert a rectangle sketch
     *
     * @param touchPositionX coordinates of the touch event
     * @param touchPositionY coordinates of the touch event
     */
    private void createRectangle(float touchPositionX, float touchPositionY) {
        Rectangle rectangle = new Rectangle(toolbarStatus.getSelectedAttributes(), touchPositionX, touchPositionY);
        addSketchToProject(rectangle);
    }

    private void addSketchToProject(Sketch sketch) {
        activeProject.getLayers()[toolbarStatus.getSelectedLayer()].addElementOnTop(sketch);
        toolbarStatus.setSelectedElement(sketch);
        resetBottomToolbar();
        activeProject.logProject();
    }

    private void resetBottomToolbar() {
        toolbarStatus.toggleTool(Tool.none);
        bottomToolbarUiController.resetBottomToolbarButtonHighlighting();
    }

}
