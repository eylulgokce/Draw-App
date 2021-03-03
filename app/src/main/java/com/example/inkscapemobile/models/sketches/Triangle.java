package com.example.inkscapemobile.models.sketches;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;

import com.example.inkscapemobile.models.GeometricCalculations;
import com.example.inkscapemobile.models.Sketch;
import com.example.inkscapemobile.models.attributes.Attribute;
import com.example.inkscapemobile.models.attributes.AttributeFactory;
import com.example.inkscapemobile.models.attributes.AttributeType;

import java.util.EnumMap;

/**
 * A equilateral triangle. Width == side length a
 * We define the center point not as the center of gravity but as point in the middle of the height.
 */
public class Triangle extends Sketch {
    private static final float COMPUTATION_ERROR_TOLERANCE = 1; //for overflow errors
    private final float[] topPoint;
    private final float[] leftPoint;
    private final float[] rightPoint;
    private static final int HIGHLIGHTING_OFFSET = 10;
    private float height;
    private Path triangle;

    public Triangle(EnumMap<AttributeType, Attribute<?>> attributes, float[] topPoint) {
        attributeWrapper = AttributeFactory.createGeometricObjectAttributes(attributes);
        this.topPoint = topPoint;
        leftPoint = new float[2];
        rightPoint = new float[2];
        generateTriangleFromTopPoint();
        setFootprint();
        Log.d("Graphical Element", "Triangle: created");
    }

    @Override
    public void applyAttributeChanges(Attribute<?> changedAttribute) {
        attributeWrapper.setAttribute(changedAttribute);
        if (changedAttribute.getType() == AttributeType.width) {
            generateTriangleFromTopPoint();
        }
        setFootprint();
        notifyObservers();
    }

    private void generateTriangleFromTopPoint() {
        float width = (Float) attributeWrapper.getAttributes().get(AttributeType.width).getValue();
        height = (width * (float) (Math.sqrt(3) / 2));

        leftPoint[0] = topPoint[0] - width / 2;
        leftPoint[1] = topPoint[1] + height;
        rightPoint[0] = leftPoint[0] + width;
        rightPoint[1] = leftPoint[1];
        triangle = new Path();
        triangle.moveTo(topPoint[0], topPoint[1]);
        triangle.lineTo(leftPoint[0], leftPoint[1]);
        triangle.lineTo(rightPoint[0], rightPoint[1]);
        triangle.lineTo(topPoint[0], topPoint[1]);
        triangle.lineTo(leftPoint[0], leftPoint[1]);
    }

    /**
     * This method seems to be duplicate code, which could be written in the superclass
     * and applied everywhere instead, but it is not.
     * Different sketches need different computation of the stroke and highlighting offset.
     * e.g text has no stroke at all
     */
    private void setFootprint() {
        float strokeCompensation = ((Float) attributeWrapper.getAttributes()
                .get(AttributeType.stroke)
                .getValue()) / 2;
        footprint = new Rect((int) (leftPoint[0] - strokeCompensation - HIGHLIGHTING_OFFSET)
                , (int) (topPoint[1] - strokeCompensation - HIGHLIGHTING_OFFSET)
                , (int) (rightPoint[0] + strokeCompensation + HIGHLIGHTING_OFFSET)
                , (int) (rightPoint[1] + strokeCompensation + HIGHLIGHTING_OFFSET));
    }

    /**
     * Uses theorem of viviani to tell if point is within triangle
     *
     * @param x coordinate
     * @param y coordinate
     * @return
     */
    @Override
    public boolean isSelected(float x, float y) {
        float[] pointP = {x, y};
        float distanceACP = GeometricCalculations.distanceLineToPoint(leftPoint, topPoint, pointP);
        float distanceABP = GeometricCalculations.distanceLineToPoint(leftPoint, rightPoint, pointP);
        float distanceBCP = GeometricCalculations.distanceLineToPoint(rightPoint, topPoint, pointP);

        float vivianiTheorem = Math.abs(distanceACP + distanceABP + distanceBCP - height);
        return vivianiTheorem < COMPUTATION_ERROR_TOLERANCE;

    }

    @Override
    public void moveBy(float x, float y) {
        topPoint[0] += x;
        topPoint[1] += y;
        generateTriangleFromTopPoint();
        setFootprint();
        notifyObservers();
    }

    @Override
    public void moveTo(float x, float y) {
        topPoint[0] = x;
        topPoint[1] = y;
        generateTriangleFromTopPoint();
        setFootprint();
        notifyObservers();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawPath(triangle, attributeWrapper.getPaint());
    }

    public float[] getTopPoint() {
        return topPoint;
    }

    public float[] getLeftPoint() {
        return leftPoint;
    }

    public float[] getRightPoint() {
        return rightPoint;
    }

    public float getHeight() {
        return height;
    }

    @Override
    public Sketch deepCopy() {
        EnumMap<AttributeType, Attribute<?>> attributes = new EnumMap<>(AttributeType.class);
        attributes.putAll(attributeWrapper.getAttributes());
        return new Triangle(attributes, new float[]{topPoint[0], topPoint[1]});
    }

    @Override
    public String toJson() {
        return "{\"type\":\"Triangle\", \"attributes\":" + attributesToJson(attributeWrapper.getAttributes()) +
                ",\"topPoint\":[" + topPoint[0] + ',' + topPoint[1] + ']' + "}";
    }
}
