package com.example.inkscapemobile.application.controller.uicontroller;

import com.example.inkscapemobile.application.ToolbarStatus;
import com.example.inkscapemobile.models.Project;
import com.example.inkscapemobile.models.TestClasses;

import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

class BottomToolbarUiControllerTest {
    private BottomToolbarUiController uiController;
    private Project testProject;
    private ToolbarStatus toolbarStatus;

    @BeforeEach
    void setUp() {
        testProject = TestClasses.createTestProject();
        toolbarStatus = new ToolbarStatus();
        uiController = mock(BottomToolbarUiController.class);
    }

}