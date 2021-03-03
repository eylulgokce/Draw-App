package com.example.inkscapemobile.application;

import android.util.Log;

import com.example.inkscapemobile.models.GraphicalElement;
import com.example.inkscapemobile.models.attributes.Attribute;
import com.example.inkscapemobile.models.attributes.AttributeFactory;
import com.example.inkscapemobile.models.attributes.AttributeType;

import java.util.EnumMap;

/**
 * Class for representing the current display-status of the toolbar. This is including the handling of the current selection regarding
 * layer and graphical element, but also grouping.
 */
public class ToolbarStatus {
    private GraphicalElement selectedElement;
    private int selectedLayer;
    private Tool selectedTool;
    private EnumMap<AttributeType, Attribute<?>> selectedAttributes;
    private boolean groupMode;
    private GroupTool groupTool;

    /**
     * Constructor for initializing the default ToolbarStatus which has no selected graphical elements and no tools selected.
     * Uses the AtributeFactory inside for creating Toolbar-attributes.
     */
    public ToolbarStatus() {
        selectedElement = null;
        selectedLayer = 1;
        selectedTool = Tool.none;
        groupMode = false;
        groupTool = GroupTool.none;
        selectedAttributes = AttributeFactory.createDefaultToolbarAttributes();
    }

    /**
     * Method for selecting a certain graphical element of the toolbar.
     * @param selectedElement The GraphicalElement to be selected
     */
    public void setSelectedElement(GraphicalElement selectedElement) {
        //remove highlighting of old element
        if (this.selectedElement != null) {
            this.selectedElement.toggleHighlighting(false);
        }
        //set element selection
        this.selectedElement = selectedElement;

        //highlight newly selected element
        if (selectedElement != null) {
            this.selectedElement.toggleHighlighting(true);
            Log.d("toolbarstatus", selectedElement.getClass().getSimpleName() + " selected");
        } else {
            Log.d("toolbarstatus", "no element selected");
        }
    }

    public void setSelectedLayer(int selectedLayer) {
        this.selectedLayer = selectedLayer;
        Log.d("toolbarstatus", "Layer " + selectedLayer + " selected");
    }

    public void selectAttribute(Attribute<?> attribute) {
        selectedAttributes.replace(attribute.getType(), attribute);
        if (selectedElement != null) {
            selectedElement.applyAttributeChanges(attribute);
        }
        Log.d("toolbarstatus", "changed toolbar attribute " + attribute.getType());
    }

    public void setSelectedAttributes(EnumMap<AttributeType, Attribute<?>> selectedAttributes) {
        this.selectedAttributes.putAll(selectedAttributes);
        Log.d("toolbarstatus", "set toolbar attributes: " + selectedAttributes.keySet().toString());
    }

    /**
     * If a tool is selected, the tool set in the status.
     * If the same tool as currently in the status bar was selected, status tool is none(selection removed)
     *
     * @param selectedTool
     */
    public void toggleTool(Tool selectedTool) {
        if (this.selectedTool == selectedTool) {
            this.selectedTool = Tool.none;
        } else {
            this.selectedTool = selectedTool;
        }
        Log.d("toolbarstatus", "selected tool " + this.selectedTool);
    }

    public void setGroupMode(boolean groupMode) {
        this.groupMode = groupMode;
        Log.d("toolbarstatus", "groupmode:  " + groupMode);
    }

    public boolean isGroupMode() {
        return groupMode;
    }

    public GroupTool getGroupTool() {
        return groupTool;
    }

    public void setGroupTool(GroupTool groupTool) {
        this.groupTool = groupTool;
        Log.d("toolbarstatus", "selected group tool:  " + groupTool);
    }

    public GraphicalElement getSelectedElement() {
        return selectedElement;
    }

    public int getSelectedLayer() {
        return selectedLayer;
    }

    public Tool getSelectedTool() {
        return selectedTool;
    }

    public EnumMap<AttributeType, Attribute<?>> getSelectedAttributes() {
        return selectedAttributes;
    }
}
