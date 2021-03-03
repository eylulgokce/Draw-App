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

class TriangleTestBasic {
    private static EnumMap<AttributeType, Attribute<?>> enumMap;
    private static float[] leftPoint;
    private static float[] rightPoint;
    private static float[] topPoint;
    private static Triangle triangle;

    @BeforeEach
    void setUp() {
        enumMap= AttributeFactory.createDefaultToolbarAttributes();
        leftPoint=new float[2];
        rightPoint=new float[2];
        topPoint=new float[2];

        topPoint[0]=20;
        topPoint[1]=20;

        triangle=new Triangle(enumMap,topPoint);
    }

    @Test
    void colorAttribute_useApplyChangesMethod_colorChanged() {
        Attribute<?> before=enumMap.get(AttributeType.color);
        Attribute<?> changingAttribute=Attribute.createColorAttribute(Color.WHITE);

        triangle.applyAttributeChanges(changingAttribute);

        assertNotEquals(before, triangle.getAttributes().get(AttributeType.color));
        assertEquals(changingAttribute, triangle.getAttributes().get(AttributeType.color));
    }

    @Test
    void certainPosition_moveByFloatNumber_checkPosition() {
        float[] topPointBefore= new float[] {triangle.getTopPoint()[0], triangle.getTopPoint()[1]};

        triangle.moveBy(30,30);

        float[] topPointAfter= triangle.getTopPoint();
        assertEquals(topPointBefore[0]+30, topPointAfter[0]);
        assertEquals(topPointBefore[1]+30, topPointAfter[1]);
    }

    @Test
    void certainPosition_moveToPosition_checkPosition() {
        float newx=200;
        float newy=200;
        triangle.moveTo(newx,newy);

        float[] topPointAfter=triangle.getTopPoint();
        assertEquals(topPointAfter[0],newx);
        assertEquals(topPointAfter[1],newy);
    }

    @Test
    void draw() {
    }

    @Test
    void getTopPoint() {
        float[] changing=new float[2];
        changing[0]=450;
        changing[1]=450;

        ReflectionTestUtils.setField(triangle,"topPoint",changing);
        assertEquals(triangle.getTopPoint(),changing);
    }

    @Test
    void getLeftPoint() {
        float[] changing=new float[2];
        changing[0]=600;
        changing[1]=600;

        ReflectionTestUtils.setField(triangle,"leftPoint",changing);
        assertEquals(triangle.getLeftPoint(),changing);
    }

    @Test
    void getRightPoint() {
        float[] changing=new float[2];
        changing[0]=500;
        changing[1]=500;

        ReflectionTestUtils.setField(triangle, "rightPoint",changing);
        assertEquals(triangle.getRightPoint(),changing);
    }

    @Test
    void getHeight() {
        float changing=50;

        ReflectionTestUtils.setField(triangle,"height",changing);
        assertEquals(triangle.getHeight(),changing);
    }

    @Test
    void deepCopy_checkHashCode() {
        Triangle deepCopy=(Triangle)triangle.deepCopy();
        Triangle shallowCopy=triangle;

        assertNotEquals(deepCopy.toString(),triangle.toString());
        assertEquals(shallowCopy.toString(),triangle.toString());
    }
}