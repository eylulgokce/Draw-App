package com.example.inkscapemobile.models;

import com.example.inkscapemobile.models.attributes.Attribute;
import com.example.inkscapemobile.models.attributes.AttributeFactory;
import com.example.inkscapemobile.models.attributes.AttributeType;
import com.example.inkscapemobile.models.sketches.Circle;
import com.example.inkscapemobile.models.sketches.Rectangle;
import com.example.inkscapemobile.models.sketches.Triangle;

import java.util.EnumMap;
import java.util.LinkedList;

public class TestClasses {
    /**
     * > Rectangle center [400, 600] width 150
     * > Triangle top point [500, 500] width 150
     * > Circle center [400, 400] radius 150
     *
     * @return
     */
    public static Group createTestGroup() {
        LinkedList<Sketch> sketches = new LinkedList<>();
        EnumMap<AttributeType, Attribute<?>> attributes = AttributeFactory.createDefaultToolbarAttributes();
        sketches.add(new Circle(attributes, 400, 400));
        sketches.add(new Triangle(attributes, new float[]{500, 500}));
        sketches.add(new Rectangle(attributes, 400, 600));
        return new Group(sketches);
    }

    /**
     * Project has graphical elements on layer 0:
     * > Circle [100, 100] radius 150
     * > Group: (test group)
     * > > Rectangle center [400, 600] width 150
     * > > Triangle top point [500, 500] width 150
     * > > Circle center [400, 400] radius 150
     *
     * @return
     */
    public static Project createTestProject() {
        Project testProject = createEmptyTestProject();
        testProject.getLayers()[0].addElementOnTop(
                new Circle(AttributeFactory.createDefaultToolbarAttributes(), 100, 100));
        testProject.getLayers()[0].addElementOnTop(createTestGroup());

        return testProject;
    }

    public static Project createEmptyTestProject() {
        Layer[] testLayers = new Layer[3];
        testLayers[0] = new Layer(new LinkedList<>());
        testLayers[1] = new Layer(new LinkedList<>());
        testLayers[2] = new Layer(new LinkedList<>());

        return new Project("test project", "test project id", testLayers);
    }
}
