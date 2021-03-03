package com.example.inkscapemobile.application.controller.touch_handler_controller;

import android.view.MotionEvent;
import android.widget.Toast;

import com.example.inkscapemobile.activities.DrawingActivity;
import com.example.inkscapemobile.application.GroupTool;
import com.example.inkscapemobile.application.ToolbarStatus;
import com.example.inkscapemobile.application.controller.uicontroller.GroupUiController;
import com.example.inkscapemobile.models.GraphicalElement;
import com.example.inkscapemobile.models.Group;
import com.example.inkscapemobile.models.Project;
import com.example.inkscapemobile.models.Sketch;

import java.util.LinkedList;

/**
 * Specific touch handler for group management, accessed by the facade (TouchHandlerController)
 * Listens for actions to initiate creation of groups, including and removing sketches from groups
 *
 *
 *
 */
public class TouchHandlerGroupController implements TouchEventHandler {
    private DrawingActivity mContext;
    private Project activeProject;
    private ToolbarStatus toolbarStatus;
    private GroupUiController groupUiController;

    public TouchHandlerGroupController(DrawingActivity mContext, Project activeProject, ToolbarStatus toolbarStatus, GroupUiController groupUiController) {
        this.mContext = mContext;
        this.activeProject = activeProject;
        this.toolbarStatus = toolbarStatus;
        this.groupUiController = groupUiController;
    }

    @Override
    public void handleCanvasTouchEvent(MotionEvent event) {
        if (!toolbarStatus.isGroupMode()) {
            return;
        }

        if (event.getAction() == MotionEvent.ACTION_UP && toolbarStatus.getGroupTool() == GroupTool.new_group) {
            createNewGroup(event.getX(), event.getY());
            Toast.makeText(mContext, "Group created", Toast.LENGTH_SHORT).show();
        }

        if (event.getAction() == MotionEvent.ACTION_UP
                && toolbarStatus.getSelectedElement() instanceof Group) {
            switch (toolbarStatus.getGroupTool()) {
                case add_sketch:
                    addSketchToGroup(event.getX(), event.getY());
                    break;
                case remove_sketch:
                    removeSketchFromGroup(event.getX(), event.getY());
                    break;
            }
        }
    }

    /**
     * Creates a new group with the sketch at the given touch position.
     * When there is no sketch, not already part of a group, no group is created.
     *
     * @param touchPositionX position of the touch event
     * @param touchPositionY position of the touch event
     */
    private void createNewGroup(float touchPositionX, float touchPositionY) {
        Sketch selectedSketch = searchForGrouplessSketch(touchPositionX, touchPositionY);
        if (selectedSketch != null) {
            Group createdGroup = activeProject.getLayers()[toolbarStatus.getSelectedLayer()].sketchAsGroup(selectedSketch);
            toolbarStatus.setSelectedElement(createdGroup);
            groupUiController.resetNewGroupButton();
        }
    }

    /**
     * If there is a group less sketch at the given position, add it to the selected group
     * @param touchPositionX position of the touch event
     * @param touchPositionY position of the touch event
     */
    private void addSketchToGroup(float touchPositionX, float touchPositionY) {
        Sketch selectedSketch = searchForGrouplessSketch(touchPositionX, touchPositionY);
        if (selectedSketch != null) {
            activeProject.getLayers()[toolbarStatus.getSelectedLayer()].getContent().remove(selectedSketch);
            ((Group) toolbarStatus.getSelectedElement()).addSketch(selectedSketch);
            selectedSketch.toggleHighlighting(true);
            Toast.makeText(mContext, selectedSketch.getClass().getSimpleName() + " added to Group", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * If there is a sketch on the touch position, within the selected group, remove it from the group.
     * If it is the last sketch of that group, delete the group instead (sketch must be extracted before, done in the Layer class)
     * @param touchPositionX position of the touch event
     * @param touchPositionY position of the touch event
     */
    private void removeSketchFromGroup(float touchPositionX, float touchPositionY) {
        Group selectedGroup = (Group) toolbarStatus.getSelectedElement();
        Sketch selectedSketch = searchForSketchInGroup(selectedGroup, touchPositionX, touchPositionY);
        if (selectedSketch != null) {
            activeProject.getLayers()[toolbarStatus.getSelectedLayer()].removeSketchFromGroup(selectedGroup, selectedSketch);
            selectedSketch.toggleHighlighting(false);
            if (selectedGroup.getSketches().size() == 0) {
                activeProject.getLayers()[toolbarStatus.getSelectedLayer()].removeElement(selectedGroup);
                Toast.makeText(mContext, "Group deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, selectedSketch.getClass().getSimpleName() + " removed from Group", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Search and return the first sketch, found at the given coordinates, which is not part of a group
     * Return null when no sketch is found
     *
     * @param touchPositionX position of the touch event
     * @param touchPositionY position of the touch event
     * @return sketch without a group or null if none found
     */
    private Sketch searchForGrouplessSketch(float touchPositionX, float touchPositionY) {
        LinkedList<GraphicalElement> elements = activeProject.getLayers()[toolbarStatus.getSelectedLayer()].getContent();
        for (GraphicalElement element : elements) {
            if (element instanceof Sketch) {
                if (element.isSelected(touchPositionX, touchPositionY)) {
                    return (Sketch) element;
                }
            }
        }
        return null;
    }

    /**
     * Search for a sketch within the given group.
     *
     * @param group to search in
     * @param touchPositionX position of the touch event
     * @param touchPositionY position of the touch event
     * @return Sketch at the position, within the group or null of none found
     */
    private Sketch searchForSketchInGroup(Group group, float touchPositionX, float touchPositionY) {
        for (Sketch sketch : group.getSketches()) {
            if (sketch.isSelected(touchPositionX, touchPositionY)) {
                return sketch;
            }
        }
        return null;
    }
}
