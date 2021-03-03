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
 * Android bottom sheet ui element for changing the stroke width attribute stored in the toolbar
 */
public class StrokeMenuBottomSheet extends BottomSheetDialogFragment {
    private ToolbarStatus toolbarStatus;
    private EditText strokeWidthInputField;
    private SeekBar strokeWithSeekBar;
    private Button confirmButton;

    public StrokeMenuBottomSheet(ToolbarStatus toolbarStatus) {
        this.toolbarStatus = toolbarStatus;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.stroke_menu_bottom_sheet, container, false);
        strokeWidthInputField = mView.findViewById(R.id.stroke_input_field);
        strokeWithSeekBar = mView.findViewById(R.id.stroke_width_seek_bar);
        confirmButton = mView.findViewById(R.id.stroke_menu_btn);
        setToStoredValue();
        setOnClickListener();
        return mView;
    }

    private void setToStoredValue() {
        float storedStrokeWidth = (Float) toolbarStatus.getSelectedAttributes().get(AttributeType.stroke).getValue();
        strokeWidthInputField.setText(("" + (int) storedStrokeWidth));
        strokeWithSeekBar.setProgress((int) storedStrokeWidth);
    }

    private void setOnClickListener() {
        strokeWithSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    strokeWidthInputField.setText(("" + progress));
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
                String insertedText = strokeWidthInputField.getText().toString();
                try {
                    float insertedStrokeWidth = Float.parseFloat(insertedText);
                    if (insertedStrokeWidth >= 0 && insertedStrokeWidth <= 150) {
                        toolbarStatus.selectAttribute(Attribute.createStrokeAttribute(insertedStrokeWidth));
                        dismiss();
                    }
                } catch (NumberFormatException e) {
                    Log.d("strokeWidth", "no valid stroke width given");
                }
            }
        });
    }
}
