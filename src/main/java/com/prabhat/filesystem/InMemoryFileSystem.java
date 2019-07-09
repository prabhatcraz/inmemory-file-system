package com.prabhat.filesystem;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryFileSystem implements FileSystem {
    private final GlobalFileManager globalFileManager;
    private final DirectoryManager directoryManager;
    private final ChangeDirectoryManager changeDirectoryManager;
    private final FileCreator fileCreator;
    private final FileDeleter fileDeleter;

    public InMemoryFileSystem(final GlobalFileManager globalFileManager,
                              final DirectoryManager directoryManager,
                              final ChangeDirectoryManager changeDirectoryManager,
                              final FileCreator fileCreator,
                              final FileDeleter fileDeleter) {
        this.globalFileManager = globalFileManager;
        this.directoryManager = directoryManager;
        this.changeDirectoryManager = changeDirectoryManager;
        this.fileCreator = fileCreator;
        this.fileDeleter = fileDeleter;

        final File root = File.builder().name("/").children(new HashMap<>()).isDirectory(true).build();
        globalFileManager.setRootFile(root);
        globalFileManager.setCurrentDir(root);
    }


    @Override
    public Iterable<String> ls() {
        final List<String> s = new ArrayList<>(globalFileManager.getCurrentDir().getChildren().keySet());
        s.add("."); // add current directory
        s.add(".."); // add parent directoryls

        return s;
    }

    @Override
    public boolean mkdir(String path) {
        try {
            // TODO: lets revisit this and make it false.
            directoryManager.create(path, true);
        } catch (RuntimeException ex) {
            // Ideally we should not swallow exception but if the interface expects a boolean response
            // we need to return a value. Printing stack trace would at least give a chance to debug.
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String touch(String path) {
        return fileCreator.createFile(path);
    }

    @Override
    public String cd(String path) {
        return changeDirectoryManager.changeDir(path);
    }

    @Override
    public String pwd() {
        File currentDir = globalFileManager.getCurrentDir();
        if (currentDir.getParent() == null) return "/";

        final StringBuilder currentDirPath = new StringBuilder();
        while (currentDir.getParent() != null) {
            currentDirPath.insert(0, "/" + currentDir.getName());
            currentDir = currentDir.getParent();
        }
        return currentDirPath.toString();
    }

    @Override
    public boolean rm(String path, boolean recursive) {
        return fileDeleter.remove(path, recursive);
    }
}
