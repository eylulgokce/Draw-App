package com.example.inkscapemobile.activities.bottomsheet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.inkscapemobile.R;
import com.example.inkscapemobile.application.ToolbarStatus;
import com.example.inkscapemobile.models.attributes.Attribute;
import com.example.inkscapemobile.models.attributes.AttributeType;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * Android bottom sheet ui element for changing the sketch width attribute stored in the toolbar
 */
public class SketchWidthMenuBottomSheet extends BottomSheetDialogFragment {
    private ToolbarStatus toolbarStatus;
    private EditText sketchWidthInputField;
    private SeekBar sketchWidthSeekBar;
    private Button confirmButton;

    public SketchWidthMenuBottomSheet(ToolbarStatus toolbarStatus) {
        this.toolbarStatus = toolbarStatus;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.sketch_width_menu_bottom_sheet, container, false);
        sketchWidthInputField = mView.findViewById(R.id.sketch_width_input_field);
        sketchWidthSeekBar = mView.findViewById(R.id.sketch_width_seek_bar);
        confirmButton = mView.findViewById(R.id.sketch_width_menu_btn);
        setToStoredValue();
        setOnClickListener();
        return mView;
    }

    private void setToStoredValue() {
        float storedSketchWidth = (Float) toolbarStatus.getSelectedAttributes().get(AttributeType.width).getValue();
        sketchWidthInputField.setText(("" + (int) storedSketchWidth));
        sketchWidthSeekBar.setProgress((int) storedSketchWidth);
    }

    private void setOnClickListener() {
        sketchWidthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    sketchWidthInputField.setText(("" + progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String insertedText = sketchWidthInputField.getText().toString();
                try {
                    int insertedSketchWidth = Integer.parseInt(insertedText);
                    if (insertedSketchWidth >= 20 && insertedSketchWidth <= 2000) {
                        toolbarStatus.selectAttribute(Attribute.createWidthAttribute(insertedSketchWidth));
                        dismiss();
                    }
                } catch (NumberFormatException e) {
                    Log.d("sketchwidth", "no valid sketch width given");
                }
            }
        });
    }

}
