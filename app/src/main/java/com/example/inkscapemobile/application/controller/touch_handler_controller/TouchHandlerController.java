package com.example.inkscapemobile.application.controller.touch_handler_controller;

import android.view.MotionEvent;

import com.example.inkscapemobile.activities.DrawingActivity;
import com.example.inkscapemobile.application.ToolbarStatus;
import com.example.inkscapemobile.application.controller.uicontroller.BottomToolbarUiController;
import com.example.inkscapemobile.application.controller.uicontroller.GroupUiController;
import com.example.inkscapemobile.models.Project;

import java.util.ArrayList;


/**
 * TouchHandlerController (Facade Pattern implementation)
 * Acts as facade to process touch events, hide the implementation of the event handling
 * Increases expandability, as more specific touch handlers can be added underneath.
 * Separation of concerns principle
 */
public class TouchHandlerController implements TouchEventHandler {
    private ArrayList<TouchEventHandler> eventHandlers;

    public TouchHandlerController(ToolbarStatus toolbarStatus
            , Project activeProject
            , DrawingActivity mContext
            , BottomToolbarUiController drawingUiController
            , GroupUiController groupUiController) {
        eventHandlers = new ArrayList<>();
        eventHandlers.add(new TouchHandlerMovementController(toolbarStatus, activeProject));
        eventHandlers.add(new TouchHandlerSketchController(mContext, activeProject, toolbarStatus, drawingUiController));
        eventHandlers.add(new TouchHandlerGroupController(mContext, activeProject, toolbarStatus, groupUiController));
        eventHandlers.add(new TouchHandlerDrawingController(activeProject, toolbarStatus, drawingUiController));
    }

    public void handleCanvasTouchEvent(MotionEvent event) {
        for (TouchEventHandler eventHandler : eventHandlers) {
            eventHandler.handleCanvasTouchEvent(event);
        }
    }
}
