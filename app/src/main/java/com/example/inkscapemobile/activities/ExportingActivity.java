package com.example.inkscapemobile.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.inkscapemobile.R;
import com.example.inkscapemobile.application.ProjectFileHandler;
import com.example.inkscapemobile.application.SketchExportHandler;
import com.example.inkscapemobile.application.filetypes.FileType;
import com.example.inkscapemobile.models.Layer;
import com.example.inkscapemobile.models.Project;

import java.util.concurrent.ExecutionException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Activity for managing the exporting-functionality. It is using the content-view of activity_exporting.
 * This means choosing the format and exporting the project, but also included the possibility to delete a project.
 */
public class ExportingActivity extends AppCompatActivity {
    private ProjectFileHandler fileHandler;
    private Project selectedProject;
    private Bitmap outputBitmap;

    /**
     * On the onCreate-method of this Activity, the required elements are instanciated from the content-view and onClick-handlers
     * are implemented for applying the required functionality.
     * @param savedInstanceState Bundle-instace, as always in overritten onCreate-method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(!hasPermission(getApplicationContext())) {
            String[] permissions=new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE};
            //The RequestCode is
            int requestCode = 110;
            requestPermissions(permissions,requestCode);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exporting);
        fileHandler=new ProjectFileHandler(this);

        TextView projectNameTextView = (TextView)findViewById(R.id.activity_export_project_name);


        EditText fileNameInput=findViewById(R.id.activity_export_file_name);

        RadioGroup exportRadioGgroup=findViewById(R.id.activity_export_file_type_toggle);
        RadioButton radioPNG=findViewById(R.id.activity_export_PNG_toggle);
        RadioButton radioJPG=findViewById(R.id.activity_export_JPEG_toggle);
        RadioButton radioPDF=findViewById(R.id.activity_export_PDF_toggle);

        Button exportBtn=findViewById(R.id.activity_export_export_btn);
        Button deleteBtn=findViewById(R.id.activity_export_delete_btn);
        Button closeBtn=findViewById(R.id.activity_export_close_btn);

        String projectID=getIntent().getStringExtra("projectID");
        try {
            selectedProject = fileHandler.loadProjectByID(projectID);
            projectNameTextView.setText(selectedProject.getProjectName());
            outputBitmap = renderBitmapFromProject(selectedProject);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        exportBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * OnClick-method of the exporting-button. Checks which filetype was chosen by the user and then starts the processing
             * of the project to the chosen format. (PNG, JPG or PDF)
             * @param v The current View
             */
            @Override
            public void onClick(View v) {

                String fileName=fileNameInput.getText().toString();
                if(exportRadioGgroup.getCheckedRadioButtonId()!=-1) {
                    if(radioPNG.isChecked()) {
                        FileType fileType=FileType.PNG;
                        SketchExportHandler.exportToFile(v,getContentResolver(),outputBitmap, fileType, fileName);
                    }
                    if(radioJPG.isChecked()) {
                        FileType fileType=FileType.JPEG;
                        SketchExportHandler.exportToFile(v,getContentResolver(),outputBitmap, fileType, fileName);
                    }
                    if(radioPDF.isChecked()) {
                        FileType fileType=FileType.PDF;
                        SketchExportHandler.exportToFile(v,getContentResolver(),outputBitmap, fileType, fileName);
                    }
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * OnClick-method of the delete-button of this Content-View. Deletes the currently selected project and the user is lead
             * back to the HomeScreenActivity.
             * @param v
             */
            @Override
            public void onClick(View v) {
                try {
                    fileHandler.deleteProject(selectedProject);
                    Intent intent=new Intent(ExportingActivity.this,HomeScreenActivity.class);
                    startActivity(intent);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * OnClick-method of the Close-Button of this Content-View. Leads the user back to the HomeScreenActivity.
             * @param v The current View
             */
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ExportingActivity.this,HomeScreenActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Boolean-method which checks whether we already have all the permissions needed for exporting granted or not
     * @param context The context in which the permissions should be checked
     * @return Boolean which is true when the permissions are already granted, and false if not.
     */
    private boolean hasPermission(Context context) {
        int write = ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE);

        if (write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    /**
     * Overritten method from Activity-classes
     * This method is automatically called when the user accepts or declines a request for a permission. So it is called
     * basically only one time: when the user grants (or declines) the permissions when entering the exporting-activity
     * and trying to export for the first time. In out case automatically inside of requestPermissions()
     * @param requestCode The requestCode for checking, which permission called this function. The value can be set by us when calling the method.
     * @param permissions String-Array of permissions to be granted
     * @param grantResults The results of the decision of the user - so the permissions which were granted successfully
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permission : grantResults) {
            Toast.makeText(getApplicationContext(), "Permission was granted", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method for receiving the Bitmap-object out of a given Project (in our case out of the loaded project)
     * @param loadedProject The loaded project (as Project-instance)
     * @return The created Bitmap for exporting.
     */
    private Bitmap renderBitmapFromProject(Project loadedProject) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        drawProjectOntoBitmap(loadedProject, bitmap);
        return bitmap;
    }

    /**
     * Method for drawing the loaded Project into the given Bitmap. This method is called from inside the renderBitmapFromProject()-method
     * @param loadedProject The loaded project as Project-file
     * @param bitmap The Bitmap-object in which the project shall be drawn.
     */
    private void drawProjectOntoBitmap(Project loadedProject, Bitmap bitmap) {
        Canvas bitmapCanvas = new Canvas(bitmap);
        bitmap.eraseColor(Color.WHITE);
        Layer[] layers = loadedProject.getLayers();
        layers[2].draw(bitmapCanvas);
        layers[1].draw(bitmapCanvas);
        layers[0].draw(bitmapCanvas);
    }
}