package com.example.inkscapemobile.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inkscapemobile.R;
import com.example.inkscapemobile.activities.adapters.ProjectsAdapter;
import com.example.inkscapemobile.application.ProjectFileHandler;
import com.example.inkscapemobile.application.storage.ProjectNameTuple;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * The Home-screen Activity and starting point of the app. Here, a new project can be created and is then listed in this overview.
 */
public class HomeScreenActivity extends AppCompatActivity {
    private RecyclerView projectsRecyclerView;
    private ProjectsAdapter projectsAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ProjectFileHandler fileHandler;
    private List<ProjectNameTuple> projectList;
    private ErrorDialog errorDialog;

    /**
     * The list of stored projects is loaded and shown, using the instances of ProjectFileHandler
     * @param savedInstanceState Bundle, as always in onCreate-method.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        errorDialog = new ErrorDialog(this);
        fileHandler = new ProjectFileHandler(getApplicationContext());
        try {
            Log.d("home screen activity", "initiate project list loading from database");
            projectList = fileHandler.loadAllProjects();
            Log.d("home screen activity", "loading project list successful");
        } catch (ExecutionException | InterruptedException e) {
            Log.e("home screen activity", "Error in loading project list", e);
            errorDialog.dispatchError("There was an error while loading the project list");
        }

        configureProjectRecyclerView();

        // new project button
        findViewById(R.id.createNewProject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProjectCreationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void configureProjectRecyclerView() {
        projectsRecyclerView = findViewById(R.id.project_list_recycler_view);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        projectsAdapter = new ProjectsAdapter(projectList);
        projectsRecyclerView.setHasFixedSize(true);

        projectsRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        projectsRecyclerView.setAdapter(projectsAdapter);

        projectsAdapter.setOnItemClickListener(new ProjectsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Context context) {
                Intent intent = new Intent(context, DrawingActivity.class);
                intent.putExtra("projectId", projectList.get(position).projectId);
                startActivity(intent);
            }
        });
    }
}