package com.prabhat.filesystem;

import com.prabhat.filesystem.directory.ChangeDirectoryManager;
import com.prabhat.filesystem.directory.DirectoryManager;
import com.prabhat.filesystem.directory.GlobalFileManager;
import com.prabhat.filesystem.manipulation.FileCreator;
import com.prabhat.filesystem.manipulation.FileDeleter;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class InMemoryFileSystemTest {
    private GlobalFileManager globalFileManager;
    private DirectoryManager directoryManager;
    private ChangeDirectoryManager changeDirectoryManager;
    private FileCreator fileCreator;
    private FileDeleter fileDeleter;

    private InMemoryFileSystem inMemoryFileSystem;

    @Before
    public void setUp() {
        globalFileManager = new GlobalFileManager();
        directoryManager = new DirectoryManager(globalFileManager);
        changeDirectoryManager = new ChangeDirectoryManager(globalFileManager);
        fileCreator = new FileCreator(globalFileManager);
        fileDeleter = new FileDeleter(globalFileManager);
        inMemoryFileSystem = new InMemoryFileSystem(globalFileManager, directoryManager, changeDirectoryManager, fileCreator, fileDeleter);
    }

    @Test
    public void shouldReturnListOfFiles() {
        // GIVEN
        final File file = File.builder().name("xyz").isDirectory(true).children(new HashMap<>()).build();
        globalFileManager.getRoot().getChildren().put("xyz", file);
        // WHEN

        final List<String> files = new ArrayList<>();
        inMemoryFileSystem.ls().forEach(files::add);

        // THEN
        assertEquals(3, files.size());
        assertTrue(files.contains("xyz"));
        assertTrue(files.contains("."));
        assertTrue(files.contains(".."));
    }

    @Test
    public void shouldBeAbleToCreateDirectoryFromRoot() {
        // GIVEN

        // WHEN
        inMemoryFileSystem.mkdir("/a/b/c");

        // THEN
        File a = globalFileManager.getRoot().getChildren().get("a");
        assertNotNull(a);
        File b = a.getChildren().get("b");
        assertNotNull(b);
        File c = b.getChildren().get("c");
        assertNotNull(c);
    }

    @Test
    public void shouldBeAbleToCreateDirectoryFromCurrentDir() {
        // GIVEN
        File a = File.builder().name("a").children(new HashMap<>()).build();
        globalFileManager.getRoot().getChildren().put("a", a);
        globalFileManager.setCurrentDir(a);
        // WHEN
        inMemoryFileSystem.mkdir("b/c");

        // THEN
        File b = a.getChildren().get("b");
        assertNotNull(b);
        File c = b.getChildren().get("c");
        assertNotNull(c);
    }

    @Test
    public void shouldBeAbleToDeleteAfolderWithAbsolutePath() {
        // GIVEN
        File a = File.builder().name("a").children(new HashMap<>()).build();
        globalFileManager.getRoot().getChildren().put("a", a);
        globalFileManager.setCurrentDir(a);
        inMemoryFileSystem.mkdir("b/c");

        File b = a.getChildren().get("b");
        // WHEN

        inMemoryFileSystem.rm("/a/b/c", true);

        // THEN
        assertEquals(0, b.getChildren().size());
    }

    @Test
    public void shouldBeAbleToDeleteAfolderWithRelativePath() {
        // GIVEN
        File a = File.builder().name("a").children(new HashMap<>()).build();
        globalFileManager.getRoot().getChildren().put("a", a);
        inMemoryFileSystem.mkdir("/a/b/c/d/f");

        File b = a.getChildren().get("b");
        globalFileManager.setCurrentDir(b);
        // WHEN

        inMemoryFileSystem.rm("c/d", true);

        // THEN
        assertEquals(0, b.getChildren().get("c").getChildren().size());
    }

    @Test(expected = RuntimeException.class)
    public void shouldFailToDeleteANonExistingFolder() {
        // GIVEN
        File a = File.builder().name("a").children(new HashMap<>()).build();
        globalFileManager.getRoot().getChildren().put("a", a);
        globalFileManager.setCurrentDir(a);
        inMemoryFileSystem.mkdir("b/c");

        File b = a.getChildren().get("b");
        // WHEN

        inMemoryFileSystem.rm("/a/b/c/d", true);

        // THEN
        assertEquals(0, b.getChildren().size());
    }

    @Test(expected = RuntimeException.class)
    public void shouldFailToDeleteRootFolder() {
        // GIVEN
        File a = File.builder().name("a").children(new HashMap<>()).build();
        globalFileManager.getRoot().getChildren().put("a", a);
        globalFileManager.setCurrentDir(a);
        inMemoryFileSystem.mkdir("b/c");

        File b = a.getChildren().get("b");
        // WHEN

        inMemoryFileSystem.rm("/", true);

        // THEN
        assertEquals(0, b.getChildren().size());
    }

    @Test
    public void shouldReturnFalseIfDeletingADirectoryInNonRecursiveWay() {
        // GIVEN
        File a = File.builder().name("a").children(new HashMap<>()).build();
        globalFileManager.getRoot().getChildren().put("a", a);
        globalFileManager.setCurrentDir(a);
        inMemoryFileSystem.mkdir("b/c");

        File b = a.getChildren().get("b");
        // WHEN

        boolean success = inMemoryFileSystem.rm("/a/b", false);

        // THEN
        assertFalse(success);
    }

    @Test
    public void shouldReturnTheCurrentDirectory() {
        // GIVEN
        inMemoryFileSystem.mkdir("b/c");
        inMemoryFileSystem.cd("b");

        // WHEN
        String currentDir = inMemoryFileSystem.pwd();
        // THEN
        assertEquals("/b", currentDir);
    }

    @Test
    public void shouldBeAbleToCreateAFileWithAbsolutePath() {
        // GIVEN

        inMemoryFileSystem.mkdir("a/b");

        File a = globalFileManager.getRoot().getChildren().get("a");
        File b = a.getChildren().get("b");
        // WHEN
        inMemoryFileSystem.touch("/a/b/c");
        // THEN
        File c = b.getChildren().get("c");
        assertEquals("c", c.getName());
        assertFalse(c.isDirectory());
    }
    @Test
    public void shouldBeAbleToCreateAFileWithRelativePath() {
        // GIVEN

        inMemoryFileSystem.mkdir("a/b");

        File a = globalFileManager.getRoot().getChildren().get("a");
        File b = a.getChildren().get("b");
        globalFileManager.setCurrentDir(b);
        // WHEN
        inMemoryFileSystem.touch("dc");
        // THEN
        File c = b.getChildren().get("dc");
        assertEquals("dc", c.getName());
        assertFalse(c.isDirectory());
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotBeAbleToCreateAFileWithBadRelativePath() {
        // GIVEN

        inMemoryFileSystem.mkdir("a/b");

        File a = globalFileManager.getRoot().getChildren().get("a");
        File b = a.getChildren().get("b");
        globalFileManager.setCurrentDir(b);
        // WHEN
        inMemoryFileSystem.touch("x/dc");
        // THEN
        File c = b.getChildren().get("dc");
        assertEquals("dc", c.getName());
        assertFalse(c.isDirectory());
    }

    
}