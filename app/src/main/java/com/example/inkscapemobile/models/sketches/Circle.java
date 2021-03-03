package com.example.inkscapemobile.models.sketches;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.example.inkscapemobile.models.GeometricCalculations;
import com.example.inkscapemobile.models.Sketch;
import com.example.inkscapemobile.models.attributes.Attribute;
import com.example.inkscapemobile.models.attributes.AttributeFactory;
import com.example.inkscapemobile.models.attributes.AttributeType;

import java.util.EnumMap;

/**
 * A drawable circle defined by it's center and radius.
 * The center point is also the objects anchor point inside a canvas.
 */
public class Circle extends Sketch {
    private float centerX;
    private float centerY;
    private float radius;
    private static final int HIGHLIGHTING_OFFSET = 5;

    public Circle(EnumMap<AttributeType, Attribute<?>> attributes, float centerX, float centerY) {
        attributeWrapper = AttributeFactory.createGeometricObjectAttributes(attributes);
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = ((Float) attributes.get(AttributeType.width).getValue()) / 2;
        setFootprint();
        Log.d("Graphical Element", "Circle: created");
    }

    /**
     * When creating the footprint the stroke has to be respected,
     * because the element appears to be larger, but it's not, still the footprint must contain and
     * not cover the element
     */
    private void setFootprint() {
        float strokeCompensation = ((Float) attributeWrapper.getAttributes().get(AttributeType.stroke).getValue()) / 2;
        this.footprint = new Rect((int) (centerX - radius - strokeCompensation - HIGHLIGHTING_OFFSET)
                , (int) (centerY - radius - strokeCompensation - HIGHLIGHTING_OFFSET)
                , (int) (centerX + radius + strokeCompensation + HIGHLIGHTING_OFFSET)
                , (int) (centerY + radius + strokeCompensation + HIGHLIGHTING_OFFSET));
    }


    @Override
    public boolean isSelected(float x, float y) {
        return GeometricCalculations.distanceBetweenTwoPoints(new float[]{x, y}, new float[]{centerX, centerY}) < radius;
    }

    @Override
    public void moveBy(float x, float y) {
        centerX += x;
        centerY += y;
        setFootprint();
        notifyObservers();
    }

    @Override
    public void moveTo(float x, float y) {
        centerX = x;
        centerY = y;
        setFootprint();
        notifyObservers();
    }

    /**
     * Additionally radius
     *
     * @param changedAttribute new attributes which should be applied
     */
    @Override
    public void applyAttributeChanges(Attribute<?> changedAttribute) {
        attributeWrapper.setAttribute(changedAttribute);
        if (changedAttribute.getType() == AttributeType.width) {
            radius = (Float) changedAttribute.getValue() / 2;
        }
        setFootprint();
        notifyObservers();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawCircle(centerX, centerY, radius, attributeWrapper.getPaint());
    }

    @Override
    public Sketch deepCopy() {
        EnumMap<AttributeType, Attribute<?>> attributes = new EnumMap<>(AttributeType.class);
        attributes.putAll(attributeWrapper.getAttributes());
        return new Circle(attributes, centerX, centerY);
    }

    @Override
    public String toJson() {
        return "{\"type\":\"Circle\", \"attributes\":"
                + attributesToJson(attributeWrapper.getAttributes())
                + ",\"centerX\":" + centerX
                + ",\"centerY\":" + centerY
                + "}";
    }
}