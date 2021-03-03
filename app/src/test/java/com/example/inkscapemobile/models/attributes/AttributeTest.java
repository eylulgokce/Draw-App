package com.example.inkscapemobile.models.attributes;

import android.graphics.Color;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AttributeTest {
    @Test
    void createNegativeFontSize_validAttributeReturned() {
        Attribute<Integer> fontSize = Attribute.createFontSizeAttribute(-1);
        assertTrue(fontSize.getValue() >= 0);
    }

    @Test
    void createNegativeStroke_validAttributeReturned() {
        Attribute<Float> stroke = Attribute.createStrokeAttribute(-1);
        assertTrue(stroke.getValue() >= 0);
    }

    @Test
    void createColorFromIntMinMax_returnsValidColor() {
        Attribute<Integer> color = Attribute.createColorAttribute(Integer.MIN_VALUE);
        assertEquals(Color.BLACK, color.getValue());
        color = Attribute.createColorAttribute(Integer.MAX_VALUE);
        assertEquals(Color.WHITE, color.getValue());
    }

    @Test
    void createWidthFromNegative_correctWidthReturned() {
        Attribute<Float> width = Attribute.createWidthAttribute(-1);
        assertTrue(width.getValue() >= 0);
    }
}