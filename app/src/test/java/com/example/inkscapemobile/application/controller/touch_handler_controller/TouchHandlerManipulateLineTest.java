package com.example.inkscapemobile.application.controller.touch_handler_controller;

import android.view.MotionEvent;

import com.example.inkscapemobile.application.Tool;
import com.example.inkscapemobile.models.GeometricCalculations;
import com.example.inkscapemobile.models.sketches.Line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TouchHandlerManipulateLineTest  extends TouchHandlerMovementControllerTesting {
    private Line testLine;
    private static final float[] LINE_START = {100, 100};
    private static final float[] LINE_END = {400, 400};
    private static final float[] MIDDLE = calculateMiddle();

    @BeforeEach
    void setUp() {
        setAllToInitialValues();
        testLine = new Line(toolbarStatus.getSelectedAttributes(), LINE_START, LINE_END);
        testProjectLayer.addElementOnTop(testLine);
        toolbarStatus.setSelectedElement(null);
    }


    private static float[] calculateMiddle(){
        float[] vector = GeometricCalculations.vectorBetweenTwoPoints(LINE_START, LINE_END);
        float[] middle = {LINE_START[0], LINE_START[1]};
        middle[0] += vector[0]/2;
        middle[1] += vector[1]/2;
        return middle;
    }

    @Test
    void nothingSelected_touchWithinLine_lineSelected() {
        assertNull(toolbarStatus.getSelectedElement());

        setEvent(MotionEvent.ACTION_DOWN, MIDDLE[0], MIDDLE[1]);
        controller.handleCanvasTouchEvent(event);

        assertTrue(toolbarStatus.getSelectedElement() instanceof Line);
    }


    @Test
    void lineSelected_touchBlankSpace_nothingSelected() {
        // select line
        assertNull(toolbarStatus.getSelectedElement());
        setEvent(MotionEvent.ACTION_DOWN, MIDDLE[0], MIDDLE[1]);
        controller.handleCanvasTouchEvent(event);

        assertTrue(toolbarStatus.getSelectedElement() instanceof Line);
        Line selected = (Line) toolbarStatus.getSelectedElement();

        // action
        toolbarStatus.toggleTool(Tool.none);
        setEvent(MotionEvent.ACTION_DOWN, 1300, 2000);
        controller.handleCanvasTouchEvent(event);
        //after
        System.out.println("start: "+ Arrays.toString(selected.getStartPoint())
                + " end: "+ Arrays.toString(selected.getEndPoint()));
        assertNull(toolbarStatus.getSelectedElement());
    }

    @Test
    void testMovingLine() {
        //ensure at x,y [2000, 2000] is nothing
        setEvent(MotionEvent.ACTION_DOWN, 1300, 2000);
        controller.handleCanvasTouchEvent(event);
        assertNull(toolbarStatus.getSelectedElement());

        //move line
        //select line
        setEvent(MotionEvent.ACTION_DOWN, MIDDLE[0], MIDDLE[1]);
        controller.handleCanvasTouchEvent(event);
        assertTrue(toolbarStatus.getSelectedElement() instanceof Line);
        // move
        toolbarStatus.toggleTool(Tool.none);
        setEvent(MotionEvent.ACTION_MOVE, 1200, 1200);
        controller.handleCanvasTouchEvent(event);
        // move
        setEvent(MotionEvent.ACTION_MOVE, 1300, 1300);
        controller.handleCanvasTouchEvent(event);
        // lift finger
        setEvent(MotionEvent.ACTION_UP, 1300, 1300);
        controller.handleCanvasTouchEvent(event);
        assertTrue(testLine.isSelected(1300, 1300));
    }
}
