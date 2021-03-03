package com.example.inkscapemobile.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.inkscapemobile.R;
import com.example.inkscapemobile.application.ProjectViewModel;
import com.example.inkscapemobile.application.controller.touch_handler_controller.TouchHandlerController;
import com.example.inkscapemobile.application.controller.uicontroller.BottomToolbarUiController;
import com.example.inkscapemobile.application.controller.uicontroller.GroupUiController;
import com.example.inkscapemobile.application.controller.uicontroller.TopToolbarUiController;

import java.util.concurrent.ExecutionException;

/**
 * Activity, where the actual drawing is done.
 * It is started from the home screen, with the projectId of the active project in
 * its Intent. The project with the given id is loaded and all controllers configured with it.
 */
public class DrawingActivity extends AppCompatActivity {
    private ProjectViewModel projectViewModel;
    private TopToolbarUiController topToolbarUiController;
    private BottomToolbarUiController bottomToolbarUiController;
    private GroupUiController groupUiController;
    private DrawView drawView;
    private ErrorDialog errorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        errorDialog = new ErrorDialog(this);
        setActiveProject();
        createUiControllers();
        setDrawView();
    }

    private void setActiveProject() {
        String projectIdFromIntent = getIntent().getStringExtra("projectId");
        if (projectIdFromIntent == null) {
            throw new RuntimeException("missing projectId in intent to draw activity");
        }
        projectViewModel = new ViewModelProvider(this).get(ProjectViewModel.class);

        try {
            Log.d("Drawing activity", "initiate loading of single project from database");
            projectViewModel.setActiveProject(projectIdFromIntent);
            Log.d("Drawing activity", "loading of project successful");
        } catch (ExecutionException | InterruptedException e) {
            Log.e("Drawing activity", "error in loading project with id", e);
            errorDialog.dispatchError("There was an error while opening the project", "go back", new ErrorDialogAction() {
                @Override
                public void action() {
                    finish();
                }
            });
        }
    }

    private void createUiControllers() {
        topToolbarUiController = new TopToolbarUiController(this, projectViewModel.getActiveProject(), projectViewModel.getToolbarStatus());
        bottomToolbarUiController = new BottomToolbarUiController(this, projectViewModel.getActiveProject(), projectViewModel.getToolbarStatus());
        groupUiController = new GroupUiController(this, projectViewModel.getActiveProject(), projectViewModel.getToolbarStatus());
    }

    private void setDrawView() {
        TouchHandlerController touchHandlerController = new TouchHandlerController(
                projectViewModel.getToolbarStatus(), projectViewModel.getActiveProject(), this, bottomToolbarUiController, groupUiController);
        drawView = findViewById(R.id.canvasDrawView);
        drawView.setTouchHandlerController(touchHandlerController);
        drawView.setActiveProject(projectViewModel.getActiveProject());
    }

    public DrawView getDrawView() {
        return drawView;
    }
}