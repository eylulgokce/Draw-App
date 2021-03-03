package com.example.inkscapemobile.application.controller.touch_handler_controller;

import android.graphics.Path;
import android.view.MotionEvent;

import com.example.inkscapemobile.models.attributes.Attribute;
import com.example.inkscapemobile.models.sketches.Drawing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TouchHandlerManipulateDrawingTests extends TouchHandlerMovementControllerTesting {
    private Drawing testDrawing;
    private ArrayList<float[]> pointsOfDrawing; // points from [100, 100] to [190, 190] in steps of [10, 10]
    private static final int numberOfPoints = 10;

    @BeforeEach
    void setUp() {
        setAllToInitialValues();
        toolbarStatus.selectAttribute(Attribute.createStrokeAttribute(10));
        createTestDrawing();
        testProjectLayer.addElementOnTop(testDrawing);
    }

    private void createTestDrawing(){
        ArrayList<float[]> points = new ArrayList<>();
        pointsOfDrawing = new ArrayList<>();
        for (int i =0; i < numberOfPoints; i++) {
            float[] pointOfDrawing = {100+i*10, 100+i*10};
            pointsOfDrawing.add(pointOfDrawing);
            points.add(pointOfDrawing);
        }
        testDrawing = new Drawing(toolbarStatus.getSelectedAttributes(), points);
    }

    @Test
    void checkPointsOfDrawing_correct() {
        for (float[] point:pointsOfDrawing) {
            System.out.println(Arrays.toString(point));
        }
    }

    private static Stream<Arguments> provideIndicesOfPointsOfDrawing() {
        Stream.Builder<Arguments> streamBuilder = Stream.builder();
        for(int i = 0; i<numberOfPoints; i++) {
            streamBuilder.add(Arguments.of(i));
        }

        return streamBuilder.build();
    }

    @ParameterizedTest
    @MethodSource("provideIndicesOfPointsOfDrawing")
    void nothingSelected_touchWithinDrawing_drawingSelected(int indexOfPointList) {
        assertNull(toolbarStatus.getSelectedElement());

        setEvent(MotionEvent.ACTION_DOWN, pointsOfDrawing.get(indexOfPointList)[0], pointsOfDrawing.get(indexOfPointList)[1]);
        controller.handleCanvasTouchEvent(event);

        assertFalse(toolbarStatus.getSelectedElement() instanceof Drawing);
    }

    @Test
    void drawingSelected_touchBlankSpace_nothingSelected() {
        // before
        setEvent(MotionEvent.ACTION_DOWN, pointsOfDrawing.get(1)[0], pointsOfDrawing.get(1)[1]);
        controller.handleCanvasTouchEvent(event);
        assertFalse(toolbarStatus.getSelectedElement() instanceof Drawing);

        // action
        setEvent(MotionEvent.ACTION_DOWN, pointsOfDrawing.get(1)[0] + 50, pointsOfDrawing.get(1)[1]);
        controller.handleCanvasTouchEvent(event);
        //after
        assertNull(toolbarStatus.getSelectedElement());
    }

    @Test
    void testMovingDrawing() {
        //ensure at x,y [2000, 2000] is nothing
        setEvent(MotionEvent.ACTION_DOWN, 2000, 2000);
        controller.handleCanvasTouchEvent(event);
        assertNull(toolbarStatus.getSelectedElement());

        //move drawing
        //select Drawing
        setEvent(MotionEvent.ACTION_DOWN, pointsOfDrawing.get(0)[0], pointsOfDrawing.get(0)[1]);
        controller.handleCanvasTouchEvent(event);

        assertFalse(toolbarStatus.getSelectedElement() instanceof Drawing);
        Drawing drawing = (Drawing) toolbarStatus.getSelectedElement();
        assertNull(drawing);
        try{
            drawing.isSelected(2000, 2000);
        }catch (Exception e){
            assertEquals(e.getClass(), NullPointerException.class);
        }

        // move
        setEvent(MotionEvent.ACTION_MOVE, 400, 400);
        controller.handleCanvasTouchEvent(event);
        // move
        setEvent(MotionEvent.ACTION_MOVE, 2000, 2000);
        controller.handleCanvasTouchEvent(event);
        // lift finger
        setEvent(MotionEvent.ACTION_UP, 2000, 2000);
        controller.handleCanvasTouchEvent(event);
        try{
            drawing.isSelected(2000, 2000);
        }catch (Exception e){
            assertEquals(e.getClass(), NullPointerException.class);
        }
    }
}
