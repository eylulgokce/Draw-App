package com.example.inkscapemobile.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.inkscapemobile.Observable;
import com.example.inkscapemobile.Observer;
import com.example.inkscapemobile.application.controller.touch_handler_controller.TouchHandlerController;
import com.example.inkscapemobile.models.Layer;
import com.example.inkscapemobile.models.Project;

/**
 * A custom view holding the canvas to draw on.
 * Acts as a general observer, meaning it observes every observable, since every change to a observable
 * must be drawn onto the canvas, to visualize its changes.
 */
public class DrawView extends View implements Observer {
    private Project activeProject;
    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private TouchHandlerController touchHandlerController;

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Observable.registerGeneralObserver(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        bitmap.eraseColor(Color.WHITE);
    }

    /**
     * draws the whole project to the canvas
     *
     * @param canvas canvas to draw on
     */
    @Override
    protected void onDraw(Canvas canvas) {
        Layer[] layers = activeProject.getLayers();
        layers[2].draw(canvas);
        layers[1].draw(canvas);
        layers[0].draw(canvas);
    }

    /**
     * passes incoming touch events to the touch handler controller
     *
     * @param event touch event
     * @return if event has been processed, which is always true
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchHandlerController.handleCanvasTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            performClick();
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public void update() {
        invalidate();
    }

    public void setActiveProject(Project activeProject) {
        this.activeProject = activeProject;
    }

    public void setTouchHandlerController(TouchHandlerController touchHandlerController) {
        this.touchHandlerController = touchHandlerController;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
