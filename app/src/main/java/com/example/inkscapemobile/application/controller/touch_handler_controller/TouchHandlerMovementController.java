package com.example.inkscapemobile.application.controller.touch_handler_controller;

import android.view.MotionEvent;

import com.example.inkscapemobile.application.GroupTool;
import com.example.inkscapemobile.application.Tool;
import com.example.inkscapemobile.application.ToolbarStatus;
import com.example.inkscapemobile.models.GeometricCalculations;
import com.example.inkscapemobile.models.GraphicalElement;
import com.example.inkscapemobile.models.Group;
import com.example.inkscapemobile.models.Project;
import com.example.inkscapemobile.models.Sketch;
import com.example.inkscapemobile.models.sketches.Line;

import java.util.LinkedList;

/**
 * Specific touch handler for graphical element movement (sketches and groups).
 * accessed by the facade (TouchHandlerController)
 * Listens for actions to select and move graphical elements
 */
public class TouchHandlerMovementController implements TouchEventHandler {
    private ToolbarStatus toolbarStatus;
    private Project activeProject;
    private float[] previousTouchPosition = new float[2];

    public TouchHandlerMovementController(ToolbarStatus toolbarStatus, Project activeProject) {
        this.toolbarStatus = toolbarStatus;
        this.activeProject = activeProject;
    }

    @Override
    public void handleCanvasTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            handleGraphicalElementSelection(event);
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE
                && toolbarStatus.getSelectedElement() != null) {
            handleGraphicalElementMovement(event);
        }
    }

    /**
     * Select a graphical element
     * select a group if in group-mode
     * select a sketch if not in group-mode
     *
     * @param event touch event
     */
    private void handleGraphicalElementSelection(MotionEvent event) {
        if (toolbarStatus.isGroupMode() && toolbarStatus.getGroupTool() == GroupTool.none) {
            toolbarStatus.setSelectedElement(searchForSelectedGroup(event.getX(), event.getY()));
            previousTouchPosition[0] = event.getX();
            previousTouchPosition[1] = event.getY();

        } else if (!toolbarStatus.isGroupMode() && toolbarStatus.getSelectedTool() == Tool.none) {
            selectSketch(event.getX(), event.getY());
            previousTouchPosition[0] = event.getX();
            previousTouchPosition[1] = event.getY();
        }
    }

    /**
     * move a graphical element to the touch position.
     * Movement is not done by moving anchor points of all elements to the coordinates,
     * but by moving them relative to the touch dragging.
     * calculating the vector between the previous touch position, the current one and
     * moving the elements according to that vector.
     *
     * @param event touch event
     */
    private void handleGraphicalElementMovement(MotionEvent event) {
        if (toolbarStatus.isGroupMode()
                && toolbarStatus.getGroupTool() == GroupTool.none
                && toolbarStatus.getSelectedElement() instanceof Group) {
            // move group
            toolbarStatus.getSelectedElement().moveBy(event.getX() - previousTouchPosition[0], event.getY() - previousTouchPosition[1]);

        } else if (!toolbarStatus.isGroupMode() && toolbarStatus.getSelectedTool() == Tool.none) {
            if (toolbarStatus.getSelectedElement() instanceof Line) {
                Line line = (Line) toolbarStatus.getSelectedElement();
                moveLine(new float[]{event.getX(), event.getY()}, line);
            } else {
                moveElement(event.getX(), event.getY());
            }
        }
        previousTouchPosition[0] = event.getX();
        previousTouchPosition[1] = event.getY();

    }

    /**
     * assign the selection to the sketch (if any) found at the touch position
     *
     * @param touchPositionX position of the touch event
     * @param touchPositionY position of the touch event
     */
    private void selectSketch(float touchPositionX, float touchPositionY) {
        Sketch target = searchForSelectedSketch(touchPositionX, touchPositionY);
        toolbarStatus.setSelectedElement(target);
        if (target != null) {
            toolbarStatus.setSelectedAttributes(target.getAttributes());
        }
    }

    /**
     * Find and return the sketch found at the touch position
     *
     * @param touchPositionX position of the touch event
     * @param touchPositionY position of the touch event
     * @return sketch at the position or null otherwise
     */
    private Sketch searchForSelectedSketch(float touchPositionX, float touchPositionY) {
        LinkedList<GraphicalElement> elements = activeProject.getLayers()[toolbarStatus.getSelectedLayer()].getContent();
        for (GraphicalElement element : elements) {
            if (element instanceof Group) {
                //if element is a group, look through group
                for (Sketch sketch : ((Group) element).getSketches()) {
                    if (sketch.isSelected(touchPositionX, touchPositionY)) {
                        return sketch;
                    }
                }
            } else {
                if (element.isSelected(touchPositionX, touchPositionY)) {
                    return (Sketch) element;
                }
            }
        }

        return null;
    }

    /**
     * Find and return the group found at the touch position
     *
     * @param touchPositionX position of the touch event
     * @param touchPositionY position of the touch event
     * @return group at the position or null otherwise
     */
    private Group searchForSelectedGroup(float touchPositionX, float touchPositionY) {
        LinkedList<GraphicalElement> elements = activeProject.getLayers()[toolbarStatus.getSelectedLayer()].getContent();
        for (GraphicalElement element : elements) {
            if (element instanceof Group) {
                if (element.isSelected(touchPositionX, touchPositionY)) {
                    return (Group) element;
                }
            }
        }
        return null;
    }

    private void moveElement(float touchPositionX, float touchPositionY) {
        toolbarStatus.getSelectedElement().moveBy(touchPositionX - previousTouchPosition[0], touchPositionY - previousTouchPosition[1]);
    }

    /**
     * If touched near starting point of line, only move that point.
     * The same applies to the end point, but if touched in the center of the line, move the whole line
     * according to the movement vector.
     * <p>
     * The user touched near a point, if the touch position not farther away that one fifth of the line length
     *
     * @param touchPosition coordinates to the touch
     * @param line          line to move
     */
    private void moveLine(float[] touchPosition, Line line) {
        float[] movementVector = {touchPosition[0] - previousTouchPosition[0], touchPosition[1] - previousTouchPosition[1]};
        float distanceLineStartTouchPosition = GeometricCalculations.distanceBetweenTwoPoints(touchPosition, line.getStartPoint());
        float distanceLineEndTouchPosition = GeometricCalculations.distanceBetweenTwoPoints(touchPosition, line.getEndPoint());
        float selectionMargin = GeometricCalculations.distanceBetweenTwoPoints(line.getStartPoint(), line.getEndPoint()) / 5;

        if (distanceLineStartTouchPosition < selectionMargin) {
            line.moveStartPointBy(movementVector);
        } else if (distanceLineEndTouchPosition < selectionMargin) {
            line.moveEndPointBy(movementVector);
        } else {
            line.moveBy(movementVector[0], movementVector[1]);
        }
    }
}
