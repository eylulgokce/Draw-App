package com.example.inkscapemobile.models.attributes;

import android.graphics.Paint;

import java.util.EnumMap;

public class AttributePaintWrapper {
    private Paint paint;
    private EnumMap<AttributeType, Attribute<?>> attributes;

    public AttributePaintWrapper(EnumMap<AttributeType, Attribute<?>> attributes) {
        this.attributes = attributes;
        setPaint();
    }

    public void setAttribute(Attribute<?> attribute) {
        attributes.replace(attribute.getType(), attribute);
        setPaint();
    }

    private void setPaint() {
        paint = PaintFactory.createPaintFromAttributes(attributes);
    }

    public Paint getPaint() {
        return paint;
    }

    public EnumMap<AttributeType, Attribute<?>> getAttributes() {
        return attributes;
    }
}
