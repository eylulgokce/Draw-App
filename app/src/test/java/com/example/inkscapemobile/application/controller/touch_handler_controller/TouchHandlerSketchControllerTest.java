package com.example.inkscapemobile.application.controller.touch_handler_controller;


import android.graphics.Rect;
import android.view.MotionEvent;

import com.example.inkscapemobile.application.Tool;
import com.example.inkscapemobile.application.ToolbarStatus;
import com.example.inkscapemobile.application.controller.uicontroller.BottomToolbarUiController;
import com.example.inkscapemobile.models.GraphicalElement;
import com.example.inkscapemobile.models.Group;
import com.example.inkscapemobile.models.Layer;
import com.example.inkscapemobile.models.Project;
import com.example.inkscapemobile.models.Sketch;
import com.example.inkscapemobile.models.TestClasses;
import com.example.inkscapemobile.models.sketches.Circle;
import com.example.inkscapemobile.models.sketches.Line;
import com.example.inkscapemobile.models.sketches.Rectangle;
import com.example.inkscapemobile.models.sketches.Text;
import com.example.inkscapemobile.models.sketches.Triangle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * When calling methods of the controller,
 * ui elements are toggled which here are whether existent nor matter, since they are tested in other tests
 */
class TouchHandlerSketchControllerTest {
    private TouchHandlerSketchController controller;
    private ToolbarStatus toolbarStatus;
    private Project testProject;
    private Layer testProjectLayer;
    private MotionEvent event;

    @BeforeEach
    void setUp() {
        toolbarStatus = new ToolbarStatus();
        toolbarStatus.setSelectedLayer(0);
        testProject = TestClasses.createTestProject();
        testProjectLayer = testProject.getLayers()[0];
        BottomToolbarUiController bottomToolbarUiController = mock(BottomToolbarUiController.class);
        doNothing().when(bottomToolbarUiController).resetBottomToolbarButtonHighlighting();

        controller = new TouchHandlerSketchController(null,
                testProject, toolbarStatus, bottomToolbarUiController);
        event = mock(MotionEvent.class);
    }

    private void setEvent(int action, float x, float y) {
        when(event.getAction()).thenReturn(action);
        when(event.getX()).thenReturn(x);
        when(event.getY()).thenReturn(y);
    }

    private boolean hasObjectWithFootprint(Class<?> o, Rect footprint) {
        for (GraphicalElement element : testProjectLayer.getContent()) {
            if (element instanceof Group) {
                Group group = (Group) element;
                for (Sketch sketch : group.getSketches()) {
                    if (o.isInstance(sketch)
                            && sketch.getFootprint().equals(footprint)) {
                        return true;
                    }
                }
            } else {
                Sketch sketch = (Sketch) element;
                if (o.isInstance(sketch)
                        && sketch.getFootprint().equals(footprint)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean layerContainsExactObject(Sketch sketch) {
        for (GraphicalElement element : testProjectLayer.getContent()) {
            if (element instanceof Group) {
                Group group = (Group) element;
                for (Sketch groupSketch : group.getSketches()) {
                    if (groupSketch == sketch) {
                        return true;
                    }
                }
            } else {
                if (element == sketch) {
                    return true;
                }
            }
        }
        return false;
    }

    @Test
    void sketchSelectedAfterCreation() {
            toolbarStatus.toggleTool(Tool.circle);
            assertNull(toolbarStatus.getSelectedElement());
            setEvent(MotionEvent.ACTION_UP, 100, 100);
            controller.handleCanvasTouchEvent(event);
            assertTrue(toolbarStatus.getSelectedElement() instanceof Circle);
    }

    @Test
    void useCircleTool_circleCreated() {
        //Before
        toolbarStatus.toggleTool(Tool.circle);
        assertNull(toolbarStatus.getSelectedElement());

        //action
        setEvent(MotionEvent.ACTION_UP, 100, 100);
        controller.handleCanvasTouchEvent(event);

        //after
        assertTrue(toolbarStatus.getSelectedElement() instanceof Circle);
        assertTrue(layerContainsExactObject((Sketch)toolbarStatus.getSelectedElement()));
    }

    @Test
    void useTriangleTool_triangleCreated() {
        //Before
        toolbarStatus.toggleTool(Tool.triangle);
        assertNull(toolbarStatus.getSelectedElement());

        //action
        setEvent(MotionEvent.ACTION_UP, 140, 140);
        controller.handleCanvasTouchEvent(event);

        //after
        assertTrue(toolbarStatus.getSelectedElement() instanceof Triangle);
        assertTrue(layerContainsExactObject((Sketch)toolbarStatus.getSelectedElement()));
    }

    @Test
    void useRectangleTool_rectangleCreated() {
        //Before
        toolbarStatus.toggleTool(Tool.rectangle);
        assertNull(toolbarStatus.getSelectedElement());

        //action
        setEvent(MotionEvent.ACTION_UP, 140, 140);
        controller.handleCanvasTouchEvent(event);

        //after
        assertTrue(toolbarStatus.getSelectedElement() instanceof Rectangle);
        assertTrue(layerContainsExactObject((Sketch)toolbarStatus.getSelectedElement()));
    }

    @Test
    void useTextTool_textCreated() {
        //Before
        toolbarStatus.toggleTool(Tool.text);
        assertNull(toolbarStatus.getSelectedElement());

        //action
        setEvent(MotionEvent.ACTION_UP, 140, 140);
        try {
            //tries to open ui element insert field, but is whether needed nor -
            // tested here and exception discarded
            controller.handleCanvasTouchEvent(event);
        } catch (NullPointerException e){
            //discard exception from ui element
        }

        //after
        assertTrue(toolbarStatus.getSelectedElement() instanceof Text);
        assertTrue(layerContainsExactObject((Sketch)toolbarStatus.getSelectedElement()));
    }

    @Test
    void useLineTool_simulateTouches_lineCreated() {
        //Before
        toolbarStatus.toggleTool(Tool.line);
        assertNull(toolbarStatus.getSelectedElement());

        //start draging
        setEvent(MotionEvent.ACTION_DOWN, 100, 100);
        controller.handleCanvasTouchEvent(event);

        //drag on screen
        setEvent(MotionEvent.ACTION_MOVE, 110, 110);
        controller.handleCanvasTouchEvent(event);

        setEvent(MotionEvent.ACTION_MOVE, 120, 120);
        controller.handleCanvasTouchEvent(event);

        //lift "finger"
        setEvent(MotionEvent.ACTION_UP, 120, 120);
        controller.handleCanvasTouchEvent(event);

        //after
        assertTrue(toolbarStatus.getSelectedElement() instanceof Line);
        Line createdLine = (Line)toolbarStatus.getSelectedElement();
        assertTrue(layerContainsExactObject(createdLine));

        assertEquals(100, createdLine.getStartPoint()[0]);
        assertEquals(100, createdLine.getStartPoint()[1]);
        assertEquals(120, createdLine.getEndPoint()[0]);
        assertEquals(120, createdLine.getEndPoint()[1]);
    }
}