package com.prabhat.filesystem;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class DirectoryManagerTest {
    @Test
    public void testFindingFile() {
        // GIVEN
        final File root = File.builder().name("/").children(new HashMap<>()).isDirectory(true).build();
        final GlobalFileManager globalfileManager = new GlobalFileManager();
        globalfileManager.setRootFile(root);

        final File child1 = File.builder().name("child1").parent(root).children(new HashMap<>()).isDirectory(true).build();
        final File child2 = File.builder().name("child2").parent(root).children(new HashMap<>()).isDirectory(true).build();

        root.getChildren().put("child1", child1);
        root.getChildren().put("child2", child2);

        final DirectoryManager directoryManager = new DirectoryManager(globalfileManager);
        // WHEN

        final File file = directoryManager.toFile("/child1");
        // THEN
        assertEquals("child1", file.getName());
    }

    @Test
    public void testCreatingFilesWithRootAsStartingPoint() {
        // GIVEN
        final File root = File.builder().name("/").children(new HashMap<>()).isDirectory(true).build();
        final GlobalFileManager globalfileManager = new GlobalFileManager();
        globalfileManager.setRootFile(root);

        final DirectoryManager directoryManager = new DirectoryManager(globalfileManager);
        // WHEN
        directoryManager.create("/a/b/c/d", true);

        // THEN
        File a = root.getChildren().get("a");
        assertEquals("a", a.getName());
        File b = a.getChildren().get("b");
        assertEquals("b", b.getName());
        File c = b.getChildren().get("c");
        assertEquals("c", c.getName());

    }

    @Test
    public void testCreatingFilesWithNonRootAsStartingPoint() {
        // GIVEN
        final File root = File.builder().name("/").children(new HashMap<>()).isDirectory(true).build();
        final GlobalFileManager globalfileManager = new GlobalFileManager();
        globalfileManager.setRootFile(root);
        final File file = File.builder().name("a").children(new HashMap<>()).isDirectory(true).build();
        root.getChildren().put("a", file);
        globalfileManager.setCurrentDir(file);

        final DirectoryManager directoryManager = new DirectoryManager(globalfileManager);
        // WHEN
        directoryManager.create("b/c/d", true);

        // THEN
        File a = root.getChildren().get("a");
        assertEquals("a", a.getName());
        File b = a.getChildren().get("b");
        assertEquals("b", b.getName());
        File c = b.getChildren().get("c");
        assertEquals("c", c.getName());
        File d = c.getChildren().get("d");
        assertEquals("d", d.getName());
    }
}