package com.prabhat.filesystem.directory;

import com.prabhat.filesystem.File;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@AllArgsConstructor
public class DirectoryManager {
    private final GlobalFileManager globalfileManager;

    /**
     * @param path      path of the file.
     * @param recursive similar to -p parameter or linux.
     */
    public boolean create(final String path, final boolean recursive) {
        // We need to check if the starting point is root or currentDir.
        File currentFile;
        final String[] paths;
        boolean createFiles = false;
        if(path.startsWith("/")) { // This means that we don't have to create files as long as path is matching with existing files
            currentFile = globalfileManager.getRoot();
            paths = path.substring(1).split("/");
        } else {
            currentFile = globalfileManager.getCurrentDir();
            paths = path.split("/");
            createFiles = true;
        }

        for (String p : paths) {

            if (createFiles) { // b signifies we have to create files now.
                final File file = File.builder().name(p).parent(currentFile).children(new HashMap<>()).isDirectory(true).build();
                currentFile.getChildren().put(p, file);
                currentFile = file;
            } else {
                if (currentFile.getChildren().get(p) != null) {
                    currentFile = currentFile.getChildren().get(p);
                } else {
                    final File file = File.builder().name(p).parent(currentFile).children(new HashMap<>()).isDirectory(true).build();
                    currentFile.getChildren().put(p, file);
                    currentFile = file;
                    createFiles = true;
                }
            }
        }

        return true;
    }
}
