package com.example.inkscapemobile.models.attributes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Factory returns a AttributeWrapper, which creates a android paint object.
 * Android classes throw a runtime exception, when they are not mocked.
 * <p>
 * These tests do not depend on android paint, so the exception is discarded
 */

class AttributeFactoryTest {
    private EnumMap<AttributeType, Attribute<?>> toolbarAttributes;

    @BeforeEach
    void setUp() {
        toolbarAttributes = AttributeFactory.createDefaultToolbarAttributes();
    }

    @Test
    void createDefaultToolbarAttributes_noAttributeMissing() {
        for (AttributeType type : AttributeType.values()) {
            assertTrue(toolbarAttributes.containsKey(type));
        }
    }

    @Test
    void createTextAttributes_containsTextSpecificAttributes() {

        EnumMap<AttributeType, Attribute<?>> createdAttributes = AttributeFactory
                .createTextAttributes(toolbarAttributes)
                .getAttributes();
        assertTrue(createdAttributes.containsKey(AttributeType.fontSize));
        assertTrue(createdAttributes.containsKey(AttributeType.color));
        assertFalse(createdAttributes.containsKey(AttributeType.width));
        assertFalse(createdAttributes.containsKey(AttributeType.stroke));

    }

    @Test
    void createDrawingAttributes_containsDrawingSpecificAttributes() {

        EnumMap<AttributeType, Attribute<?>> createdAttributes = AttributeFactory
                .createDrawingAttributes(toolbarAttributes)
                .getAttributes();
        assertFalse(createdAttributes.containsKey(AttributeType.fontSize));
        assertTrue(createdAttributes.containsKey(AttributeType.color));
        assertFalse(createdAttributes.containsKey(AttributeType.width));
        assertTrue(createdAttributes.containsKey(AttributeType.stroke));

    }

    @Test
    void createLineAttributes_containsLineSpecificAttributes() {

        EnumMap<AttributeType, Attribute<?>> createdAttributes = AttributeFactory
                .createLineAttributes(toolbarAttributes)
                .getAttributes();
        assertFalse(createdAttributes.containsKey(AttributeType.fontSize));
        assertTrue(createdAttributes.containsKey(AttributeType.color));
        assertFalse(createdAttributes.containsKey(AttributeType.width));
        assertTrue(createdAttributes.containsKey(AttributeType.stroke));

    }

    @Test
    void createGeometricObjectAttributes_containsShapeSpecificAttributes() {

        EnumMap<AttributeType, Attribute<?>> createdAttributes = AttributeFactory
                .createGeometricObjectAttributes(toolbarAttributes)
                .getAttributes();
        assertFalse(createdAttributes.containsKey(AttributeType.fontSize));
        assertTrue(createdAttributes.containsKey(AttributeType.color));
        assertTrue(createdAttributes.containsKey(AttributeType.width));
        assertTrue(createdAttributes.containsKey(AttributeType.stroke));

    }
}