package com.example.inkscapemobile.application.filetypes;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;

import com.example.inkscapemobile.application.exceptions.FileNameException;

import java.io.File;
import java.io.IOException;

/**
 * The interface which deals as the Strategy-Interface, enabling the usage of the Strategy-Pattern for Project-Exporting.
 */
public interface FileTypeConverter {

    /**
     * The method which has to be implemented by all ConcreteStrategy-classes of the pattern. It should convert the project and export
     * it to the specific file-format.
     * @param bitmap The Bitmap of the project to be exported
     * @param contentResolver The actual ContentResolver for this project
     * @param fileName The file-name which the exported image should have
     * @throws FileNameException
     * @throws IOException
     */
    public void convert(Bitmap bitmap, ContentResolver contentResolver, String fileName) throws FileNameException, IOException;

    /**
     * Checks if the file at the given uri exists.
     *
     * @param fileUri uri to file
     * @return true if the file exists, false otherwise.
     */
    public default boolean checkIfFileExists(Uri fileUri) {
        String pathToFile = fileUri.getPath();

        if (pathToFile == null || pathToFile.isEmpty()) {
            throw new RuntimeException("Invalid file uri given");
        }

        File file = new File(pathToFile);
        return file.exists();

    }
}
