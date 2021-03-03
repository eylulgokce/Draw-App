package com.example.inkscapemobile.application.controller.touch_handler_controller;

import android.view.MotionEvent;

import com.example.inkscapemobile.application.ToolbarStatus;
import com.example.inkscapemobile.models.Project;
import com.example.inkscapemobile.models.TestClasses;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TouchHandlerControllerTest {
    private TouchHandlerController mainController;
    private MotionEvent event;
    ArrayList<TouchEventHandler> eventHandlerMocks;

    @BeforeEach
    void setUp() {
        Project testProject = TestClasses.createTestProject();
        ToolbarStatus toolbarStatus = new ToolbarStatus();
        mainController = new TouchHandlerController(toolbarStatus, testProject, null, null, null);
        setTouchEvent();
    }

    private void setTouchEvent() {
        event = mock(MotionEvent.class);
        when(event.getAction()).thenReturn(MotionEvent.ACTION_UP);
        when(event.getX()).thenReturn(100f);
        when(event.getY()).thenReturn(100f);
    }

    @Test
    void passEvent_allEventHandlersCalled() {
        ArrayList<TouchEventHandler> eventHandlers = (ArrayList<TouchEventHandler>) ReflectionTestUtils.getField(mainController, "eventHandlers");
        eventHandlerMocks = new ArrayList<>();

        //create mocks with same classes
        for(TouchEventHandler eventHandler:eventHandlers) {
            TouchEventHandler eventHandlerMock = mock(eventHandler.getClass());
            eventHandlerMocks.add(eventHandlerMock);
        }

        //replace event handlers with mocks
        ReflectionTestUtils.setField(mainController, "eventHandlers", eventHandlerMocks);
        mainController.handleCanvasTouchEvent(event);

        //methods were called at least once
        for (TouchEventHandler eventHandlerMock:eventHandlerMocks) {
            verify(eventHandlerMock, Mockito.atLeastOnce()).handleCanvasTouchEvent(event);
        }
    }
}