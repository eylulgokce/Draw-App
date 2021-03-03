package com.example.inkscapemobile.models.attributes;

import android.graphics.Color;

import java.util.EnumMap;

/**
 * Class for creating different default attribute configurations.
 * <p>
 * Also creates attribute maps for each sketch type, from a given attribute map.
 * This is technically not necessary, because it would work without these modifications
 * e.g. drawing could also work perfectly fine if it had a font size attribute, because it simply ignores it
 * but it was implement in order of defensive programming
 */
public class AttributeFactory {
    public static EnumMap<AttributeType, Attribute<?>> createDefaultToolbarAttributes() {
        EnumMap<AttributeType, Attribute<?>> attributes = new EnumMap<>(AttributeType.class);
        attributes.put(AttributeType.color, Attribute.createColorAttribute(Color.BLACK));
        attributes.put(AttributeType.stroke, Attribute.createStrokeAttribute(0));
        attributes.put(AttributeType.fontSize, Attribute.createFontSizeAttribute(40));
        attributes.put(AttributeType.width, Attribute.createWidthAttribute(150));
        return attributes;
    }

    public static AttributePaintWrapper createTextAttributes(EnumMap<AttributeType, Attribute<?>> attributes) {
        EnumMap<AttributeType, Attribute<?>> textAttributes = new EnumMap<>(attributes);
        textAttributes.remove(AttributeType.stroke);
        textAttributes.remove(AttributeType.width);
        return new AttributePaintWrapper(textAttributes);
    }

    public static AttributePaintWrapper createDrawingAttributes(EnumMap<AttributeType, Attribute<?>> attributes) {
        EnumMap<AttributeType, Attribute<?>> drawingAttributes = new EnumMap<>(attributes);
        drawingAttributes.remove(AttributeType.width);
        drawingAttributes.remove(AttributeType.fontSize);
        return new AttributePaintWrapper(drawingAttributes);
    }

    public static AttributePaintWrapper createLineAttributes(EnumMap<AttributeType, Attribute<?>> attributes) {
        EnumMap<AttributeType, Attribute<?>> drawingAttributes = new EnumMap<>(attributes);
        drawingAttributes.remove(AttributeType.fontSize);
        drawingAttributes.remove(AttributeType.width);

        return new AttributePaintWrapper(drawingAttributes);
    }

    public static AttributePaintWrapper createGeometricObjectAttributes(EnumMap<AttributeType, Attribute<?>> attributes) {
        EnumMap<AttributeType, Attribute<?>> drawingAttributes = new EnumMap<>(attributes);
        drawingAttributes.remove(AttributeType.fontSize);
        return new AttributePaintWrapper(drawingAttributes);
    }
}
