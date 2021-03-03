package com.example.inkscapemobile.application.controller.touch_handler_controller;

import android.view.MotionEvent;

import com.example.inkscapemobile.models.attributes.Attribute;
import com.example.inkscapemobile.models.sketches.Circle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TouchHandlerManipulateCircleTests extends TouchHandlerMovementControllerTesting {
    private Circle testCircle; // at 200, 200 with radius 150 (default toolbar)
    private static final float[] CENTER = {200, 200};
    private static final float RADIUS = 150;

    @BeforeEach
    void setUp() {
        setAllToInitialValues();
        toolbarStatus.selectAttribute(Attribute.createWidthAttribute(RADIUS));
        testCircle = new Circle(toolbarStatus.getSelectedAttributes(), CENTER[0], CENTER[1]);
        testProjectLayer.addElementOnTop(testCircle);
    }


    @Test
    void nothingSelected_touchWithinCircle_circleSelected() {
        assertNull(toolbarStatus.getSelectedElement());

        setEvent(MotionEvent.ACTION_DOWN, CENTER[0], CENTER[1]);
        controller.handleCanvasTouchEvent(event);

        assertTrue(toolbarStatus.getSelectedElement() instanceof Circle);
    }

    @Test
    void circleSelected_touchBlankSpace_nothingSelected() {
        // before
        setEvent(MotionEvent.ACTION_DOWN, CENTER[0], CENTER[1]);
        controller.handleCanvasTouchEvent(event);
        assertNotNull(toolbarStatus.getSelectedElement());

        // action
        setEvent(MotionEvent.ACTION_DOWN, 2000, 2000);
        controller.handleCanvasTouchEvent(event);
        //after
        assertNull(toolbarStatus.getSelectedElement());
    }

    @Test
    void testMovingCircle() {
        //ensure at x,y [2000, 2000] is nothing
        setEvent(MotionEvent.ACTION_DOWN, 2000, 2000);
        controller.handleCanvasTouchEvent(event);
        assertNull(toolbarStatus.getSelectedElement());

        //move circle
        //select circle
        setEvent(MotionEvent.ACTION_DOWN, CENTER[0], CENTER[1]);
        controller.handleCanvasTouchEvent(event);
        Circle circle = (Circle)toolbarStatus.getSelectedElement();
        assertFalse(circle.isSelected(2000, 2000));
        // move
        setEvent(MotionEvent.ACTION_MOVE, 400, 400);
        controller.handleCanvasTouchEvent(event);
        // move
        setEvent(MotionEvent.ACTION_MOVE, 2000, 2000);
        controller.handleCanvasTouchEvent(event);
        // lift finger
        setEvent(MotionEvent.ACTION_UP, 2000, 2000);
        controller.handleCanvasTouchEvent(event);
        assertTrue(circle.isSelected(2000, 2000));
    }
}
