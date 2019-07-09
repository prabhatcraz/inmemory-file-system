package com.prabhat.filesystem;


import org.junit.Test;

import java.util.HashMap;

public class GlobalFileManagerTest {

    @Test
    public void shouldGetRootDirectory() {
        // GIVEN
        final GlobalFileManager globalFileManager = new GlobalFileManager();
        final File root = File.builder().name("/").children(new HashMap<>()).isDirectory(true).build();
        globalFileManager.setRootFile(root);

        // WHEN


        // THEN
    }
}