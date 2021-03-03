package com.example.inkscapemobile.application;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.inkscapemobile.application.storage.StorageSavingProcess;
import com.example.inkscapemobile.models.Project;

import java.util.concurrent.ExecutionException;

/**
 * Viewmodel to store the active Project and the toolbarStatus, to persist application
 * state over configuration changes.
 */
public class ProjectViewModel extends AndroidViewModel {
    private ToolbarStatus toolbarStatus;
    private Project activeProject = null;
    private ProjectFileHandler fileHandler;

    public ProjectViewModel(@NonNull Application application) {
        super(application);
        fileHandler = new ProjectFileHandler(application);
        toolbarStatus = new ToolbarStatus();
    }

    public void setActiveProject(String projectId) throws ExecutionException, InterruptedException {
        if (activeProject == null || !activeProject.getProjectId().equals(projectId)) {
            activeProject = loadProjectWithId(projectId);
        }
    }

    /**
     * Loading the project from the database
     * <p>
     * when there is currently a ongoing saving process
     * of the project with the id we just want to open, we wait until the saving process is finished
     * to load the most recent data
     *
     * @param projectId project to open
     * @return
     */
    private Project loadProjectWithId(String projectId) throws ExecutionException, InterruptedException {
        StorageSavingProcess savingProcess = ProjectFileHandler.ongoingSavingProcess;
        if (savingProcess != null && !savingProcess.getProcess().isDone()
                && savingProcess.getProjectId().equals(projectId)) {
            //wait for saving process to finish
            savingProcess.getProcess().get();
        }

        return fileHandler.loadProjectByID(projectId);
    }

    public ToolbarStatus getToolbarStatus() {
        return toolbarStatus;
    }

    public Project getActiveProject() {
        return activeProject;
    }
}
