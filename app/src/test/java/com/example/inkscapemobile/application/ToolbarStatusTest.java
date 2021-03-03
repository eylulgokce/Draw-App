package com.example.inkscapemobile.application;

import android.graphics.Color;

import com.example.inkscapemobile.models.GraphicalElement;
import com.example.inkscapemobile.models.attributes.Attribute;
import com.example.inkscapemobile.models.attributes.AttributeFactory;
import com.example.inkscapemobile.models.attributes.AttributeType;
import com.example.inkscapemobile.models.sketches.Circle;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

class ToolbarStatusTest {
    ToolbarStatus toolbarStatus;
    GraphicalElement element;
    EnumMap<AttributeType, Attribute<?>> attributeEnumMap;

    @BeforeEach
    void setUp() {
        toolbarStatus=new ToolbarStatus();
        attributeEnumMap= AttributeFactory.createDefaultToolbarAttributes();
    }

    @Test
    void noElementSelected_setCircleElementSelected_elementSelected() {
        float centerX=20;
        float centerY=20;
        element=new Circle(attributeEnumMap,centerX,centerY);
        GraphicalElement elementBefore=toolbarStatus.getSelectedElement();

        assertNull(elementBefore);

        toolbarStatus.setSelectedElement(element);
        GraphicalElement elementAfter=toolbarStatus.getSelectedElement();

        assertEquals(element, elementAfter);
    }

    @Test
    void defaultLayerSelected_setNewSelectedLayer_checkLayer() {
        int layerDefault=1;
        int selectedBefore=toolbarStatus.getSelectedLayer();
        assertEquals(layerDefault,selectedBefore);

        int newLayer=3;
        toolbarStatus.setSelectedLayer(newLayer);

        int selectedAfter=toolbarStatus.getSelectedLayer();
        assertEquals(selectedAfter,newLayer);
        assertNotEquals(selectedAfter, selectedBefore);
    }

    @Test
    void CircleDefaultColorAttribute_selectAttributeColor_checkIfSelected() {
        float centerX=20;
        float centerY=20;
        element=new Circle(attributeEnumMap,centerX,centerY);

        toolbarStatus.setSelectedElement(element);
        Attribute<?> selectedAttribute=Attribute.createColorAttribute(Color.BLACK);

        toolbarStatus.selectAttribute(selectedAttribute);

        EnumMap<AttributeType, Attribute<?>> afterMap=toolbarStatus.getSelectedAttributes();
        assertEquals(afterMap.get(selectedAttribute.getType()),selectedAttribute);
    }

    @Test
    void noCertainTool_toggleTool_checkIfToggled_ToggleAgain_CheckIfNone() {
        Tool selectedBefore=toolbarStatus.getSelectedTool();
        assertEquals(selectedBefore,Tool.none);

        Tool newSelection=Tool.circle;
        toolbarStatus.toggleTool(newSelection);

        assertEquals(newSelection,toolbarStatus.getSelectedTool());
        toolbarStatus.toggleTool(newSelection);
        assertEquals(toolbarStatus.getSelectedTool(), Tool.none);
    }
}