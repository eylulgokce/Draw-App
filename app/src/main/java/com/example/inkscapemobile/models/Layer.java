package com.example.inkscapemobile.models;

import android.graphics.Canvas;

import com.example.inkscapemobile.Observable;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * A layer is a carrier for all containing sketches.
 * There can be layers above or underneath one layer and they can be made
 * visible oder hidden. A user can only interact with one layer at the time.
 * Graphical elements can cover another up, where ordering is done top to bottom
 * from content list start to content list end. Meaning the first element is the
 * one above all others and the last element beneath all other elements
 */
public class Layer extends Observable implements Drawable {
    private boolean visible;
    /**
     * graphical elements from high to low as list start to end
     */
    private final LinkedList<GraphicalElement> content;

    public Layer(LinkedList<GraphicalElement> content) {
        visible = true;
        this.content = content;
    }

    /**
     * Draw all graphical elements onto the canvas, from bottom up
     *
     * @param canvas canvas to draw on
     */
    @Override
    public void draw(Canvas canvas) {
        if (visible) {
            Iterator<GraphicalElement> descendingIterator = content.descendingIterator();
            while (descendingIterator.hasNext()) {
                descendingIterator.next().draw(canvas);
            }
        }
    }

    /**
     * enables/shows or disables/hides layer
     *
     * @param visible true = show, false = hide
     */
    public void setVisibility(boolean visible) {
        this.visible = visible;
        notifyObservers();
    }

    public boolean isVisible() {
        return visible;
    }

    /**
     * add an Graphical Element to this layer
     *
     * @param element element to add
     */
    public void addElementOnTop(GraphicalElement element) {
        content.addFirst(element);
        notifyObservers();
    }

    /**
     * Remove the given element from the layer.
     * If the element was the last sketch in a group, remove the empty group too.
     *
     * @param element to remove
     */
    public void removeElement(GraphicalElement element) {
        if (!content.remove(element)) {
            //if element is contained in group
            for (GraphicalElement graphicalElement : content) {
                //find all groups
                if (graphicalElement instanceof Group && element instanceof Sketch) {
                    Group group = (Group) graphicalElement;
                    //if element is within this group
                    if (group.getSketches().contains(element)) {
                        if (group.getSketches().size() == 1) {
                            content.remove(group);
                        } else {
                            group.removeSketch((Sketch) element);
                        }
                        return;
                    }
                }
            }
        }
        notifyObservers();
    }

    /**
     * Create a group with the given sketch and replace the sketch with that group
     *
     * @param sketch
     * @return
     */
    public Group sketchAsGroup(Sketch sketch) {
        Group group = new Group(sketch);
        group.setVisible(true);
        int sketchIndex = content.indexOf(sketch);
        content.set(sketchIndex, group);
        notifyObservers();
        return group;
    }

    /**
     * Removes the given sketch from the given group in a sense
     * , that the sketch is extracted from the group and put back into the layer as a "standalone"
     * sketch.
     *
     * @param group  group with the sketch
     * @param sketch the sketch to extract
     */
    public void removeSketchFromGroup(Group group, Sketch sketch) {
        if (group.getSketches().contains(sketch)) {
            int groupIndex = content.indexOf(group);
            content.add(groupIndex, sketch);
            group.removeSketch(sketch);
        }
    }

    public void setGroupVisibility(boolean visible) {
        for (GraphicalElement element : content) {
            if (element instanceof Group) {
                ((Group) element).setVisible(visible);
            }
        }
        notifyObservers();
    }

    public LinkedList<GraphicalElement> getContent() {
        return content;
    }
}
