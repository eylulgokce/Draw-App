package com.example.inkscapemobile.application.controller.touch_handler_controller;

import android.view.MotionEvent;

import com.example.inkscapemobile.application.GroupTool;
import com.example.inkscapemobile.application.ToolbarStatus;
import com.example.inkscapemobile.application.controller.uicontroller.GroupUiController;
import com.example.inkscapemobile.models.GraphicalElement;
import com.example.inkscapemobile.models.Group;
import com.example.inkscapemobile.models.Project;
import com.example.inkscapemobile.models.TestClasses;
import com.example.inkscapemobile.models.sketches.Circle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TouchHandlerGroupControllerTest {
    private TouchHandlerGroupController controller;
    private Project testProject;
    private Group testGroup;
    private ToolbarStatus toolbarStatus;
    private Circle testCircle;
    private final float[] testCircleCenter = {1000, 1000};

    @BeforeEach
    void setUp() {
        toolbarStatus = new ToolbarStatus();
        testProject = TestClasses.createTestProject();
        testCircle = new Circle(toolbarStatus.getSelectedAttributes(), testCircleCenter[0], testCircleCenter[1]);
        testProject.getLayers()[0].addElementOnTop(testCircle);
        testGroup = findTestGroup(testProject);
        toolbarStatus.setSelectedLayer(0);
        GroupUiController uiController = mock(GroupUiController.class);
        doNothing().when(uiController).resetNewGroupButton();
        controller = new TouchHandlerGroupController(null, testProject, toolbarStatus, uiController);
    }

    private MotionEvent createTouchEvent(int action, float x, float y) {
        MotionEvent event = mock(MotionEvent.class);
        when(event.getAction()).thenReturn(action);
        when(event.getX()).thenReturn(x);
        when(event.getY()).thenReturn(y);
        return event;
    }

    private Group findTestGroup(Project testProject){
        for (GraphicalElement element:testProject.getLayers()[0].getContent()) {
            if (element instanceof Group) {
                return (Group)element;
            }
        }
        return null;
    }

    @Test
    void setUpTest_testConditionsValid() {
        assertNotNull(testGroup);
    }

    @Test
    void toolsEnabled_touchOnCircle_groupCreated() {
        //before
        toolbarStatus.setGroupMode(true);
        toolbarStatus.setGroupTool(GroupTool.new_group);
        assertNull(toolbarStatus.getSelectedElement());

        //action
        //Context is null, displaying toast message with throw NPE
        // NPE can be ignored, toast message is not tested
        try {
            MotionEvent touchEvent = createTouchEvent(MotionEvent.ACTION_UP, testCircleCenter[0], testCircleCenter[1]);
            controller.handleCanvasTouchEvent(touchEvent);

        } catch (NullPointerException e) {
            //discard exception
        }

        //after
        assertTrue(toolbarStatus.getSelectedElement() instanceof Group);
        Group createdGroup = (Group)toolbarStatus.getSelectedElement();
        assertTrue(createdGroup.getSketches().contains(testCircle));
    }

    @Test
    void groupModeNotEnabled_eventNotProcessed() {
        //before
        toolbarStatus.setGroupMode(false);
        toolbarStatus.setGroupTool(GroupTool.new_group);
        assertNull(toolbarStatus.getSelectedElement());

        //action
        //Context is null, displaying toast message with throw NPE
        // NPE can be ignored, toast message is not tested
        try {
            MotionEvent touchEvent = createTouchEvent(MotionEvent.ACTION_UP, testCircleCenter[0], testCircleCenter[1]);
            controller.handleCanvasTouchEvent(touchEvent);

        } catch (NullPointerException e) {
            //discard exception
        }
        assertNull(toolbarStatus.getSelectedElement());
    }
}