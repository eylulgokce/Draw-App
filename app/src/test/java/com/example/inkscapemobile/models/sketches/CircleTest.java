package com.example.inkscapemobile.models.sketches;

import android.graphics.Color;

import com.example.inkscapemobile.models.attributes.Attribute;
import com.example.inkscapemobile.models.attributes.AttributeFactory;
import com.example.inkscapemobile.models.attributes.AttributeType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CircleTest {
    private static float centerX=20;
    private static float centerY=20;
    private static EnumMap<AttributeType, Attribute<?>> enumMap;
    private Circle circle;

    @BeforeEach
    void instantiate() {
        enumMap= AttributeFactory.createDefaultToolbarAttributes();
        circle=new Circle(enumMap,centerX,centerY);
    }

    @Test
    void moveCircle_positionChanged() {
        float internalCenterX= (float)ReflectionTestUtils.getField(circle,"centerX");
        float internalCenterY=(float) ReflectionTestUtils.getField(circle,"centerY");
        float internalRadiusBefore=(float) ReflectionTestUtils.getField(circle,"radius");
        assertEquals(centerX,internalCenterX);
        assertEquals(centerY,internalCenterY);

        circle.moveBy(30,30);

        internalCenterX=(float) ReflectionTestUtils.getField(circle,"centerX");
        internalCenterY=(float) ReflectionTestUtils.getField(circle,"centerY");
        float internalRadiusAfter=(float) ReflectionTestUtils.getField(circle,"radius");
        assertEquals(internalCenterX, centerX+30);
        assertEquals(internalCenterY,centerY+30);
        assertEquals(internalRadiusBefore,internalRadiusAfter);
    }

    @Test
    void moveCircleTo_positionChanged() {
        circle.moveTo(centerX, centerY);

        float internalCenterX= (float)ReflectionTestUtils.getField(circle,"centerX");
        float internalCenterY=(float) ReflectionTestUtils.getField(circle,"centerY");
        assertEquals(centerX,internalCenterX);
        assertEquals(centerY,internalCenterY);
    }

    @Test
    void colorAttribute_applyAttributeChanges_checkIfAttributeChanged() {
        Attribute<?> before=enumMap.get(AttributeType.color);
        Attribute<?> changing=Attribute.createColorAttribute(Color.WHITE);

        circle.applyAttributeChanges(changing);
        assertNotEquals(circle.getAttributes().get(AttributeType.color),before);
        assertEquals(circle.getAttributes().get(AttributeType.color),changing);
    }

    @Test
    void deepCopy_differentHashCode() {
        Circle deepCopy=(Circle)circle.deepCopy();
        Circle shallowCopy=circle;

        assertNotEquals(deepCopy.toString(),circle.toString());
        assertEquals(shallowCopy.toString(),circle.toString());
    }
}