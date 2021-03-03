package com.example.inkscapemobile.application.filetypes;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.inkscapemobile.application.exceptions.FileNameException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 * ConcreteStrategy-class of the Strategy-pattern implementing the strategy for exporting in PNG-format.
 * Is of course implementing the Strategy-interface.
 */
public class PNGFileConverter implements FileTypeConverter {

    /**
     * The method which has to be implemented based on the implemented strategy-interface.
     * @param bitmap          of the project to export
     * @param contentResolver applications content resolver
     * @param fileName        name for the file, given by the user
     */
    @Override
    public void convert(Bitmap bitmap, ContentResolver contentResolver, String fileName) throws FileNameException, IOException {
        ContentValues values = setValuesForExporting(fileName);

        Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        if (checkIfFileExists(imageUri)) {
            values.clear();
            throw new FileNameException("File already exists.", fileName);
        }
        writeToFile(bitmap, contentResolver, imageUri);
        values.clear();
    }

    /**
     * Method for creating the ContentValues-object for exporting. Such an object is needed, because it includes properties
     * of the exported file, like date, format, or the displayed name. This method is called from inside the convert()-method
     * @param fileName The file-name which should be used for the exported file.
     * @return The ContentValues for the JPEG-file
     */
    private ContentValues setValuesForExporting(String fileName) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName + ".png");

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());

        return values;
    }

    /**
     *  Method for writing to the created file and so for exporting the project to a certain file-format. This method is called from
     *  within convert()-method.
     * @param bitmap The Bitmap to be exported
     * @param contentResolver The ContentResolver for the file
     * @param imageUri The URI pointing to the file and so to the exporting-destination
     * @throws IOException
     */
    private void writeToFile(Bitmap bitmap, ContentResolver contentResolver, Uri imageUri) throws IOException {
        try (OutputStream outputStream = contentResolver.openOutputStream(Objects.requireNonNull(imageUri))) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        }
    }
}
