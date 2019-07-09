package com.prabhat.filesystem;


import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class FileDeleterTest {
    private GlobalFileManager globalFileManager;

    @Before
    public void setUp() {
        globalFileManager = new GlobalFileManager();
        globalFileManager.setRootFile(File.builder().name("/").children(new HashMap<>()).build());
        globalFileManager.setCurrentDir(globalFileManager.getRoot());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionForRoot() {
        new FileDeleter(globalFileManager).remove("/", true);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionForCWD() {
        new FileDeleter(globalFileManager).remove(".", true);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionForParent() {
        new FileDeleter(globalFileManager).remove("..", true);
    }

    @Test
    public void shouldRemoveFileFromCurrentDirectory() {
        // GIVEN

        // WHEN

        // THEN
    }
}