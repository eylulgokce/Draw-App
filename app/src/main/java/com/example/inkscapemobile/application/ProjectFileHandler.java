package com.example.inkscapemobile.application;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.example.inkscapemobile.application.storage.AppDatabase;
import com.example.inkscapemobile.application.storage.ProjectDao;
import com.example.inkscapemobile.application.storage.ProjectNameTuple;
import com.example.inkscapemobile.application.storage.StorageSavingProcess;
import com.example.inkscapemobile.models.Project;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Central access point for the storage mechanism. A file handler can be created in any activity
 * by it's context. All CRUD methods are practically synchronous, except the Update (saving) function.
 *
 * CRUD Functions are performed in a separate thread, but we need to wait until they are finished anyway,
 * because we need display the data, or the updated state. But what we do truly async and in the background
 * is saving (updating) the project in the database, so the user can continue drawing.
 *
 *
 */
public class ProjectFileHandler {
    private ProjectDao projectDao;
    private AppDatabase db;
    public static StorageSavingProcess ongoingSavingProcess;

    public ProjectFileHandler(Context context) {
        db = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "app_database")
                .fallbackToDestructiveMigration()
                .build();
        projectDao = db.projectDao();
    }

    /**
     * Stores the newly created project synchronously into the database
     * @param project newly created project to store
     * @throws ExecutionException when the database failed to write
     * @throws InterruptedException when the background thread is interrupted
     */
    public void storeNewProject(Project project) throws ExecutionException, InterruptedException {
        projectDao.insert(project).get();
    }

    /**
     * Saves the already existing project asynchronously into the database
     * also sets the ongoingSavingProcess where it can be checked, if the saving process is
     * still running
     *

     * @param project project to save in FS
     */
    public void saveProject(Project project) {
        long timeStarted = System.currentTimeMillis();
        ongoingSavingProcess = new StorageSavingProcess(projectDao.update(project), project.getProjectId());

        /*
        Small benchmark: a thread, that waits in the background until the transaction
        finished and logs the time, it needed. Just to see how long the process really takes and
        if the asynchronous updating gives us any benefit, or if creating a separate thread is in
        fact more overhead than benefit.
         */
        new Thread(() -> { //TODO remove benchmark in production
            try {
                ongoingSavingProcess.getProcess().get();
                long timeNeeded = System.currentTimeMillis() - timeStarted;
                Log.d("saving mechanism", "project saving complete! took: " + timeNeeded + " ms to save");
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Synchronously deletes the given project from the database
     * @param project this project is deleted
     * @throws ExecutionException when the database failed to write
     * @throws InterruptedException when the background thread is interrupted
     */
    public void deleteProject(Project project) throws ExecutionException, InterruptedException {
        projectDao.delete(project).get();
    }

    /**
     * Load all project ids and names from the database.
     * The name of each project is mapped to the project id
     *
     */
    public List<ProjectNameTuple> loadAllProjects() throws ExecutionException, InterruptedException {
        return projectDao.getAll().get();
    }


    /**
     * get the project with the given id from the database
     * @param id of the requested project
     * @throws ExecutionException when the database failed to write
     * @throws InterruptedException when the background thread is interrupted
     * @return The requested project
     */
    public Project loadProjectByID(String id) throws ExecutionException, InterruptedException {
        return projectDao.loadProjectById(id).get();
    }
}
