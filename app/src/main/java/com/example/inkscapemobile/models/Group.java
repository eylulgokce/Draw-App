package com.example.inkscapemobile.models;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.inkscapemobile.Observable;
import com.example.inkscapemobile.Observer;
import com.example.inkscapemobile.models.attributes.Attribute;
import com.example.inkscapemobile.models.attributes.PaintFactory;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * A group can hold multiple sketches.
 * When the group is modified, modifications are applied to contained sketches.
 */
public class Group implements GraphicalElement, Observer {
    private final LinkedList<Sketch> sketches;
    private float[] centerPoint;
    private Rect footprint;
    private boolean highlighted = false;
    private boolean visible = false;
    private final static Paint visibilityPaint = PaintFactory.createGroupVisibilityPaint();
    private final static Paint highlightingPaint = PaintFactory.createGroupHighlightPaint();

    public Group(LinkedList<Sketch> sketches) {
        this.sketches = sketches;
        for (Sketch sketch : sketches) {
            sketch.registerIndividualObserver(this);
        }

        update();
    }

    public Group(Sketch sketch) {
        sketches = new LinkedList<>();
        sketches.addLast(sketch);
        sketch.registerIndividualObserver(this);
        update();
    }

    /**
     * Adds a sketch and registers as observer to listen for changes,
     *
     * @param sketch sketch to add
     */
    public void addSketch(Sketch sketch) {
        sketch.registerIndividualObserver(this);
        sketches.addFirst(sketch);
        update();
        Observable.notifyGeneralObservers();
    }

    /**
     * remove sketch and detach observer
     *
     * @param sketch sketch to remove
     */
    public void removeSketch(Sketch sketch) {
        sketches.remove(sketch);
        sketch.detachIndividualObserver(this);
        update();
        Observable.notifyGeneralObservers();
    }


    /**
     * creates a rectangle with the footprint of the group to make the group visible
     */
    private void setFootprint() {
        if (sketches.size() == 0) {
            //when group deleted, last sketch is removed from the group and inserted to the layer sketches.
            //for this short period, this group is empty until it is removed too
            return;
        }
        footprint = new Rect(sketches.getFirst().getFootprint());
        for (Sketch sketch : sketches) {
            footprint.union(sketch.getFootprint());
        }
    }


    /**
     * Calculates and returns the center point by the set footprint
     *
     * @return returns the center as array with [x, y] or [0,0] if group is empty
     */
    private void setCenterPoint() {
        if (sketches.size() == 0) {
            centerPoint = new float[]{0, 0};
        } else {
            centerPoint = new float[]{footprint.exactCenterX(), footprint.exactCenterY()};
        }
    }

    /**
     * When a sketch inside the group is changed, the footprint of the group is updated as well
     */
    @Override
    public void update() {
        setFootprint();
        setCenterPoint();
    }

    @Override
    public boolean isSelected(float x, float y) {
        return footprint.contains((int) x, (int) y);
    }

    @Override
    public void moveBy(float x, float y) {
        for (Sketch sketch : sketches) {
            sketch.moveBy(x, y);
        }
    }

    @Override
    public void moveTo(float x, float y) {
        float diffX = x - centerPoint[0];
        float diffY = y - centerPoint[1];

        for (Sketch sketch : sketches) {
            sketch.moveBy(diffX, diffY);
        }
    }

    @Override
    public void applyAttributeChanges(Attribute<?> changedAttribute) {
        for (Sketch sketch : sketches) {
            sketch.applyAttributeChanges(changedAttribute);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        //chose while over descendingIterator.foreachRemaining because of performance of lambda
        Iterator<Sketch> descendingIterator = sketches.descendingIterator();
        while (descendingIterator.hasNext()) {
            descendingIterator.next().draw(canvas);
        }

        if (visible) {
            canvas.drawRect(footprint, visibilityPaint);
        }

        if (highlighted) {
            canvas.drawRect(footprint, highlightingPaint);
        }
    }

    /**
     * Makes a deep copy(copy of all underlying objects) of this group and returns it.
     *
     * @return duplicate group of this one
     */
    public Group duplicateGroup() {
        LinkedList<Sketch> sketchDuplicates = new LinkedList<>();
        for (Sketch sketch : sketches) {
            sketchDuplicates.addLast(sketch.deepCopy());
        }

        return new Group(sketchDuplicates);
    }

    @Override
    public void toggleHighlighting(boolean toggle) {
        highlighted = toggle;
        for (Sketch sketch : sketches) {
            sketch.toggleHighlighting(toggle);
        }
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public LinkedList<Sketch> getSketches() {
        return sketches;
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder("{\"type\":\"Group\",\"sketches\":[");

        for (Sketch sketch : sketches) {
            sb.append(sketch.toJson()).append(',');
        }

        //remove last ,
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.append("]}").toString();
    }
}
