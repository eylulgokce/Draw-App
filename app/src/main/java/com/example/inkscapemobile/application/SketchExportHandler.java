package com.example.inkscapemobile.application;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.inkscapemobile.application.exceptions.FileNameException;
import com.example.inkscapemobile.application.filetypes.FileType;
import com.example.inkscapemobile.models.Project;

import java.io.IOException;

public class SketchExportHandler {
    /**
     * Exports a project to a file with the given parameters.
     *
     * @param view             to display toast messages on
     * @param contentResolver  content resolver
     * @param projectBitmap  bitmap to export
     * @param selectedFileType user selected file type
     * @param fileName         user provided name
     * @return true on success, false otherwise
     */
    public static boolean exportToFile(View view, ContentResolver contentResolver
            , Bitmap projectBitmap, FileType selectedFileType, String fileName) {
        String toastMessage = "";

        try {
            selectedFileType
                    .getConverter()
                    .convert(projectBitmap, contentResolver, fileName);
            toastMessage = "Export Successful";
            return true;

        } catch (FileNameException e) {
            Log.e("SketchExportHandler", e.getMessage() + " - " + e.getFilename(), e);
            toastMessage = "File " + e.getFilename() + " already exists.";

        } catch (RuntimeException e) {
            Log.e("SketchExportHandler", e.getMessage(), e);
            toastMessage = "Could not export project.";

        } catch (IOException e) {
            Log.e("FileConverter", "writeToFile: ", e);
            toastMessage = "Error in Output-Streaming.";
        } finally {
            Toast.makeText(view.getContext(), toastMessage, Toast.LENGTH_SHORT).show();
        }

        return false;
    }
}
