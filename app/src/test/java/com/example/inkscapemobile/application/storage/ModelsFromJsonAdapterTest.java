package com.example.inkscapemobile.application.storage;

import com.example.inkscapemobile.models.GraphicalElement;
import com.example.inkscapemobile.models.Layer;
import com.example.inkscapemobile.models.Project;
import com.example.inkscapemobile.models.TestClasses;
import com.example.inkscapemobile.models.attributes.AttributeFactory;
import com.example.inkscapemobile.models.attributes.AttributePaintWrapper;
import com.example.inkscapemobile.models.attributes.AttributeType;
import com.example.inkscapemobile.models.sketches.Circle;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelsFromJsonAdapterTest {
    private Project testProject;

    @Test
    void convertToJson_parseFromJson_noException() {
        testProject = TestClasses.createTestProject();
        String layersJson = ModelsToJsonAdapter.layersToJson(testProject.getLayers());
        assertDoesNotThrow(()->ModelsFromJsonAdapter.layersFromJson(layersJson));
    }

    @Test
    void parseLayerWithCircle_correctCircleReturned() {
        Layer[] expectedLayer = TestClasses.createEmptyTestProject().getLayers();

        Circle expectedCircle = new Circle(AttributeFactory.createDefaultToolbarAttributes(), 100, 100);
        expectedLayer[0].addElementOnTop(expectedCircle);

        String jsonOfExpected = "[["+expectedCircle.toJson()+"],[],[]]";

        Layer[] actual = ModelsFromJsonAdapter.layersFromJson(jsonOfExpected);
        assertEquals(3, actual.length);

        LinkedList<GraphicalElement> actualLayerContent = actual[0].getContent();
        assertEquals(1, actualLayerContent.size());

        Circle actualCircle = (Circle)actualLayerContent.get(0);
        Float actualCenterX = (Float) ReflectionTestUtils.getField(actualCircle, "centerX");
        Float actualCenterY = (Float) ReflectionTestUtils.getField(actualCircle, "centerY");
        AttributePaintWrapper actualAttributeWrapper =
                (AttributePaintWrapper) ReflectionTestUtils.getField(actualCircle, "attributeWrapper");

        assertEquals(100, actualCenterX);
        assertEquals(100, actualCenterY);

        assertEquals(AttributeFactory.createDefaultToolbarAttributes().get(AttributeType.width).getValue()
                , actualAttributeWrapper.getAttributes().get(AttributeType.width).getValue());
        assertEquals(AttributeFactory.createDefaultToolbarAttributes().get(AttributeType.color).getValue()
                , actualAttributeWrapper.getAttributes().get(AttributeType.color).getValue());
        assertEquals(AttributeFactory.createDefaultToolbarAttributes().get(AttributeType.stroke).getValue()
                , actualAttributeWrapper.getAttributes().get(AttributeType.stroke).getValue());

    }
}