package com.example.inkscapemobile.application.controller.uicontroller;

import com.example.inkscapemobile.activities.DrawingActivity;
import com.example.inkscapemobile.application.ToolbarStatus;
import com.example.inkscapemobile.models.Project;

/**
 * The superclass of a specific UiController providing internals(project, toolbar, context) for its
 * subclasses, avoiding code duplication.
 */
public abstract class UiController {
    private DrawingActivity mContext;
    private Project activeProject;
    private ToolbarStatus toolbarStatus;

    public UiController(DrawingActivity mContext, Project activeProject, ToolbarStatus toolbarStatus) {
        this.mContext = mContext;
        this.activeProject = activeProject;
        this.toolbarStatus = toolbarStatus;
    }

    public DrawingActivity getContext() {
        return mContext;
    }

    public Project getActiveProject() {
        return activeProject;
    }

    public ToolbarStatus getToolbarStatus() {
        return toolbarStatus;
    }
}
