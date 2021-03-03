package com.example.inkscapemobile.models.sketches;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;

import com.example.inkscapemobile.models.Sketch;
import com.example.inkscapemobile.models.attributes.Attribute;
import com.example.inkscapemobile.models.attributes.AttributeFactory;
import com.example.inkscapemobile.models.attributes.AttributeType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;

/**
 * A free hand drawing, essentially a collection of coordinates, which represent the drawn path.
 * Is created by the
 */
//FIXME moving the drawing is not working
public class Drawing extends Sketch {
    private ArrayList<Path> drawnPath; // change if necessary
    private ArrayList<float[]> pathPoints;
    private static final float TOUCH_TOLERANCE = 4;
    private float mX, mY;
    private float left, right, top, bottom;
    private Path myPath;
    private Region region;

    /**
     * creates a new complete drawing with the given points/paths
     *
     * @param attributes
     * @param pathPoints  placeholder parameter, to be swapped with internals of this class
     */
    public Drawing(EnumMap<AttributeType, Attribute<?>> attributes, ArrayList<float[]> pathPoints) {
//        attributeWrapper = AttributeFactory.createDrawingAttributes(attributes);
        this(attributes, pathPoints.get(0)[0], pathPoints.get(0)[1]);

        for (float[] point: pathPoints.subList(1, pathPoints.size())) {
            this.touch_move(point[0], point[1]);
        }

        this.finish(pathPoints.get(pathPoints.size() -1 )[0], pathPoints.get(pathPoints.size() -1 )[1]);

        setFootprint();
        notifyObservers();
    }

    /**
     * Creates a new unfinished drawing with the given starting position.
     *
     * @param attributes
     * @param startX
     * @param startY
     */
    public Drawing(EnumMap<AttributeType, Attribute<?>> attributes, float startX, float startY) {
        attributeWrapper = AttributeFactory.createDrawingAttributes(attributes);
        this.drawnPath = new ArrayList<>();
        this.pathPoints = new ArrayList<>();

        touch_start(startX, startY);
        this.applyAttributeChanges(Attribute.createStrokeAttribute(10));
        setFootprint();
        notifyObservers();
    }

    /**
     * adds the given point to the drawing
     *
     * @param pointX x position where the user touched
     * @param pointY y position where the user touched
     */
    public void addToDrawing(float pointX, float pointY) {
        touch_move(pointX, pointY);
        setFootprint();
        notifyObservers();
    }


    //TODO add methods and constructors if necessary

    @Override
    public void applyAttributeChanges(Attribute<?> changedAttribute) {
        attributeWrapper.setAttribute(changedAttribute);
        notifyObservers();
    }

    /**
     * creates and assigns the footprint rect
     */
    private void setFootprint() {

        footprint = new Rect((int) left, (int) top, (int) right, (int) bottom);

        setRegion();

    }


    private void setRegion() {
        RectF rectF = new RectF();
        myPath.computeBounds(rectF, true);
        region = new Region();
        region.setPath(myPath, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
    }

    @Override
    public Rect getFootprint() {
        return footprint;
    }

    /**
     * checks if the given coordinates of where the user touched, is on the drawing line
     * NOTE: a touch tolerance to make the selection area larger might be needed
     *
     * @param x coordinate
     * @param y coordinate
     * @return true if the position is on a line of this drawing
     */
    @Override
    public boolean isSelected(float x, float y) {

        return region.contains((int) x, (int) y);
    }

    @Override
    public void moveBy(float x, float y) {

        for( Path p: drawnPath){
            p.offset(x, y);
        }
        left+=x;
        right+=x;
        top+=y;
        bottom+=y;

        setFootprint();
        notifyObservers();
    }

    @Override
    public void moveTo(float x, float y) {

        for( Path p: drawnPath)
            p.moveTo(x,y);

        float width= right-left;
        left = x-width/2;
        right= x+width/2;

        float height= top-bottom;
        bottom= y-height/2;
        top= y+height/2;

        setFootprint();
        notifyObservers();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawPath(myPath, attributeWrapper.getPaint());
        for (Path p : drawnPath)
            canvas.drawPath(p, attributeWrapper.getPaint());
    }

    @Override
    public Sketch deepCopy() {
        ArrayList<float[]> new_drawn = new ArrayList<>(pathPoints);
        EnumMap<AttributeType, Attribute<?>> attributes = new EnumMap<>(AttributeType.class);
        attributes.putAll(attributeWrapper.getAttributes());
        return new Drawing(attributes, new_drawn);
    }

    private void touch_start(float x, float y) {
        this.pathPoints.add(new float[]{x, y});
        myPath = new Path();
        myPath.moveTo(x, y);
        mX = x;
        mY = y;
        left = right = x;
        top = bottom = y;
    }

    private void touch_move(float x, float y) {
        this.pathPoints.add(new float[]{x, y});
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            myPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;

            if (x < left)
                left = x;
            if (x > right)
                right = x;
            if (y < bottom)
                bottom = y;
            if (y > top)
                top = y;

        }
    }

    public void finish(float x, float y) {
        drawnPath.add(myPath);
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder("{\"type\":\"Drawing\", \"attributes\":");
        sb.append(attributesToJson(attributeWrapper.getAttributes()))
                .append(", \"drawnPath\":[");

        for (float[] points : pathPoints) {
            sb.append('[').append(points[0]).append(',').append(points[1]).append("],");
        }

        //remove last ,
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.append("]}").toString();
    }

}
