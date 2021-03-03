package com.example.inkscapemobile.models.sketches;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.example.inkscapemobile.models.Sketch;
import com.example.inkscapemobile.models.attributes.Attribute;
import com.example.inkscapemobile.models.attributes.AttributeFactory;
import com.example.inkscapemobile.models.attributes.AttributeType;

import java.util.EnumMap;

//I'd suggest to make squares and not rectangles, because we only have width as attribute
public class Rectangle extends Sketch {
    private float centerX;
    private float centerY;
    private Rect rectangle;
    private static final int HIGHLIGHTING_OFFSET = 10;

    public Rectangle(EnumMap<AttributeType, Attribute<?>> attributes, float centerX, float centerY) {
        attributeWrapper = AttributeFactory.createGeometricObjectAttributes(attributes);
        this.centerX = centerX;
        this.centerY = centerY;
        setRect();
        setFootprint();
        Log.d("Graphical Element", "Rectangle: created");
    }

    private void setRect() {
        float halfWidth = (Float) (attributeWrapper.getAttributes().get(AttributeType.width).getValue()) / 2;
        this.rectangle = new Rect((int) (centerX - halfWidth), (int) (centerY - halfWidth), (int) (centerX + halfWidth), (int) (centerY + halfWidth));
    }

    /**
     * When creating the footprint the stroke has to be respected,
     * because the element appears to be larger, but it's not, still the footprint must contain and
     * not cover the element
     */
    private void setFootprint() {
        float strokeCompensation = ((Float) attributeWrapper.getAttributes().get(AttributeType.stroke).getValue()) / 2;
        this.footprint = new Rect(rectangle.left - (int) strokeCompensation - HIGHLIGHTING_OFFSET
                , rectangle.top - (int) strokeCompensation - HIGHLIGHTING_OFFSET
                , rectangle.right + (int) strokeCompensation + HIGHLIGHTING_OFFSET
                , rectangle.bottom + (int) strokeCompensation + HIGHLIGHTING_OFFSET);
    }

    @Override
    public void applyAttributeChanges(Attribute<?> changedAttribute) {
        attributeWrapper.setAttribute(changedAttribute);
        if (changedAttribute.getType() == AttributeType.width) {
            setRect();
        }
        setFootprint();
        notifyObservers();
    }

    @Override
    public boolean isSelected(float x, float y) {
        return rectangle.contains((int) x, (int) y);
    }

    @Override
    public void moveBy(float x, float y) {
        centerX += x;
        centerY += y;
        setRect();
        setFootprint();
        notifyObservers();
    }

    @Override
    public void moveTo(float x, float y) {
        centerX = x;
        centerY = y;
        setRect();
        setFootprint();
        notifyObservers();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRect(rectangle, attributeWrapper.getPaint());
    }

    @Override
    public Sketch deepCopy() {
        EnumMap<AttributeType, Attribute<?>> attributes = new EnumMap<>(AttributeType.class);
        attributes.putAll(attributeWrapper.getAttributes());
        return new Rectangle(attributes, centerX, centerY);
    }

    @Override
    public String toJson() {
        return "{\"type\":\"Rectangle\", \"attributes\":" + attributesToJson(attributeWrapper.getAttributes()) +
                ",\"centerX\":" + centerX +
                ",\"centerY\":" + centerX +
                "}";
    }
}
