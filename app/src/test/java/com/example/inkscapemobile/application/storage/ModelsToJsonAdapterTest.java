package com.example.inkscapemobile.application.storage;

import com.example.inkscapemobile.models.Layer;
import com.example.inkscapemobile.models.Project;
import com.example.inkscapemobile.models.TestClasses;
import com.example.inkscapemobile.models.attributes.AttributeFactory;
import com.example.inkscapemobile.models.sketches.Circle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelsToJsonAdapterTest {
    private Project testProject;

    @Test
    void emptyProject_toJson_validJson() {
        testProject = TestClasses.createEmptyTestProject();
        String actual = ModelsToJsonAdapter.layersToJson(testProject.getLayers());
        String expected = "[[],[],[]]";
        assertEquals(expected, actual);
    }

    @Test
    void circlesOnEachLayer_toJson_correctJson() {
        Layer[] testLayers = TestClasses.createEmptyTestProject().getLayers();
        Circle circle = new Circle(AttributeFactory.createDefaultToolbarAttributes(), 100, 100);
        for(int i = 0; i < testLayers.length; i++) {
            testLayers[i].addElementOnTop(circle);
        }

        String actual = ModelsToJsonAdapter.layersToJson(testLayers);
        String expected = "[["+circle.toJson()+"],["+circle.toJson()+"],["+circle.toJson()+"]]";
        assertEquals(expected, actual);
    }
}