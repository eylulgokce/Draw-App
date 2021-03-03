package com.example.inkscapemobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inkscapemobile.R;
import com.example.inkscapemobile.application.ProjectFileHandler;
import com.example.inkscapemobile.models.Project;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Activity to create a new Project.
 * After creation the project is stored into the database.
 * Then the user is directed back to the home screen, where he/she can open the
 * newly created Project
 *
 */
public class ProjectCreationActivity extends AppCompatActivity {
    private EditText projectNameInputField;
    private Button confirmButton;
    private ProjectFileHandler fileHandler;
    private ErrorDialog errorDialog;

    /**
     * onCreate-method setting up the instance-variables and connecting to the ContentView-objects.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_creation);
        fileHandler = new ProjectFileHandler(this);
        errorDialog = new ErrorDialog(this);

        projectNameInputField = findViewById(R.id.create_project_name_input);
        confirmButton = findViewById(R.id.create_new_project_btn);
        setOnClickListener();
    }

    /**
     * When the confirm button is clicked and the text field not empty,
     * creation, storage and activity change is done.
     */
    private void setOnClickListener() {
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String insertedText = projectNameInputField.getText().toString();
                if (!insertedText.isEmpty()) {
                    Project project = createNewProject(insertedText);
                    storeNewProject(project);
                    Log.d("new project", "project created");
                    returnToHomeScreen();
                }
            }
        });
    }

    /**
     * Creates a new instance of Project-class
     * @param projectName The name which the new project should have
     * @return The created Project-instance
     */
    private Project createNewProject(String projectName) {
        String projectId = UUID.randomUUID().toString();
        return new Project(projectId, projectName);
    }

    /**
     * Stores a Project-instance to the database, using the ProjectFileHandler-class
     * @param project The project to be stored
     */
    private void storeNewProject(Project project) {
        try {
            Log.d("project creation", "save newly created project to database");
            fileHandler.storeNewProject(project);
            Log.d("project creation", "saving successful");
        } catch (ExecutionException | InterruptedException e) {
            Log.e("project creation", "Error in storing new project", e);
            errorDialog.dispatchError("There was an error while opening the project", "go back", new ErrorDialogAction() {
                @Override
                public void action() {
                    finish();
                }
            });
        }
    }

    /**
     * Method for simply returning to the home-screen.
     */
    private void returnToHomeScreen() {
        Intent intent = new Intent(this, HomeScreenActivity.class);
        startActivity(intent);
    }
}