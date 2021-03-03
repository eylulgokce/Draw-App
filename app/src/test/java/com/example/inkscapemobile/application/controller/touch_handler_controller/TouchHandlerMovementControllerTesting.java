package com.example.inkscapemobile.application.controller.touch_handler_controller;

import android.view.MotionEvent;

import com.example.inkscapemobile.application.Tool;
import com.example.inkscapemobile.application.ToolbarStatus;
import com.example.inkscapemobile.models.Layer;
import com.example.inkscapemobile.models.Project;
import com.example.inkscapemobile.models.TestClasses;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Test project contains a Circle at [100, 100] with radius 150
 */
abstract class TouchHandlerMovementControllerTesting {
    public ToolbarStatus toolbarStatus;
    public Project testProject;
    public Layer testProjectLayer;
    public TouchHandlerMovementController controller;
    public MotionEvent event;

    /**
     * Set initial state, before testing.
     *  default toolbar, empty project
     */
    public void setAllToInitialValues() {
        toolbarStatus = new ToolbarStatus();
        toolbarStatus.setSelectedLayer(0);
        toolbarStatus.toggleTool(Tool.none);
        testProject = TestClasses.createEmptyTestProject();
        testProjectLayer = testProject.getLayers()[0];
        controller = new TouchHandlerMovementController(toolbarStatus, testProject);
        event = mock(MotionEvent.class);
    }

    public void setEvent(int action, float x, float y) {
        when(event.getAction()).thenReturn(action);
        when(event.getX()).thenReturn(x);
        when(event.getY()).thenReturn(y);
    }

}