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
 *
 */
public class Line extends Sketch {
    private static final float TOUCH_SELECTION_TOLERANCE = 30;
    private static final int HIGHLIGHTING_OFFSET = 10;
    private float[] startPoint;
    private float[] endPoint;

    public Line(EnumMap<AttributeType, Attribute<?>> attributes, float[] startPoint, float[] endPoint) {
        attributeWrapper = AttributeFactory.createLineAttributes(attributes);
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        setFootprint();
        Log.d("Graphical Element", "Line: created");
    }

    /**
     * When creating the footprint, the stroke has to be respected.
     * Start or End can be either the leftmost or rightmost point.
     */
    private void setFootprint() {
        float strokeCompensation = ((Float) attributeWrapper.getAttributes().get(AttributeType.stroke).getValue()) / 2;
        int left = (int) (Math.min(startPoint[0], endPoint[0]) - strokeCompensation - HIGHLIGHTING_OFFSET);
        int top = (int) (Math.min(startPoint[1], endPoint[1]) - strokeCompensation - HIGHLIGHTING_OFFSET);
        int right = (int) (Math.max(startPoint[0], endPoint[0]) + strokeCompensation + HIGHLIGHTING_OFFSET);
        int bottom = (int) (Math.max(startPoint[1], endPoint[1]) + strokeCompensation + HIGHLIGHTING_OFFSET);
        this.footprint = new Rect(left, top, right, bottom);
    }

    @Override
    public void applyAttributeChanges(Attribute<?> changedAttribute) {
        attributeWrapper.setAttribute(changedAttribute);
        notifyObservers();
    }

    /**
     * Calculate if given point is on the line, with the included TOUCH_SELECTION_TOLERANCE as
     * no one would be able to select the exact pixel on that line
     * <p>
     * NOTE: the selection is done via geometric calculations, distance to point function,
     * which is the orthogonal projection of the point on that line.
     * However, in geometry lines(Geraden) don't have an start and end point as they are endless long.
     * This fact could result in selection of the line, when user clicks on the exact vector,
     * but farther outside the line itself, in practice this doesn't influence usability but should be
     * accorded for in unit testing.
     *
     * @param x coordinate
     * @param y coordinate
     * @return if the line is selected
     */
    @Override
    public boolean isSelected(float x, float y) {
        return GeometricCalculations.distanceLineToPoint(startPoint, endPoint, new float[]{x, y}) < TOUCH_SELECTION_TOLERANCE;
    }

    @Override
    public void moveBy(float x, float y) {
        startPoint[0] += x;
        startPoint[1] += y;
        endPoint[0] += x;
        endPoint[1] += y;
        setFootprint();
        notifyObservers();
    }

    @Override
    public void moveTo(float x, float y) {
        float[] vector = GeometricCalculations.vectorBetweenTwoPoints(startPoint, endPoint);
        startPoint[0] = x;
        startPoint[1] = y;
        endPoint[0] = x + vector[0];
        endPoint[1] = y + vector[1];
        setFootprint();
        notifyObservers();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawLine(startPoint[0], startPoint[1], endPoint[0], endPoint[1], attributeWrapper.getPaint());
    }

    public void setStartPoint(float[] startPoint) {
        this.startPoint = startPoint;
        setFootprint();
        notifyObservers();
    }

    public void moveStartPointBy(float[] vector) {
        startPoint[0] += vector[0];
        startPoint[1] += vector[1];
        setFootprint();
        notifyObservers();
    }

    public void setEndPoint(float[] endPoint) {
        this.endPoint = endPoint;
        setFootprint();
        notifyObservers();
    }

    public void moveEndPointBy(float[] vector) {
        endPoint[0] += vector[0];
        endPoint[1] += vector[1];
        setFootprint();
        notifyObservers();
    }

    public float[] getStartPoint() {
        return startPoint;
    }

    public float[] getEndPoint() {
        return endPoint;
    }

    @Override
    public Sketch deepCopy() {
        EnumMap<AttributeType, Attribute<?>> attributes = new EnumMap<>(AttributeType.class);
        attributes.putAll(attributeWrapper.getAttributes());
        return new Line(attributes, new float[]{startPoint[0], startPoint[1]}, new float[]{endPoint[0], endPoint[1]});
    }

    @Override
    public String toJson() {
        return "{\"type\":\"Line\", \"attributes\":" + attributesToJson(attributeWrapper.getAttributes()) +
                ",\"startPoint\":[" + startPoint[0] + ',' + startPoint[1] + ']' +
                ",\"endPoint\":[" + endPoint[0] + ',' + endPoint[1] + ']' +
                "}";
    }
}
