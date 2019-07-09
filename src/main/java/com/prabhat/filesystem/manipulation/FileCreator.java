package com.prabhat.filesystem.manipulation;

import com.prabhat.filesystem.File;
import com.prabhat.filesystem.directory.GlobalFileManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FileCreator {
    private final GlobalFileManager globalFileManager;

    public String createFile(final String path) {
        if(path.trim().length() == 0) throw new RuntimeException("Please provide a valid name");

        File folderToCreateFile = globalFileManager.getCurrentDir();
        String fileName = path;
        if (path.contains("/")) {
            final int i = path.lastIndexOf("/");
            folderToCreateFile = findDirToCreateFile(path.substring(0, i));
            fileName = path.substring(i + 1);
        }
        final File file = File.builder().name(fileName).parent(folderToCreateFile).isDirectory(false).build();
        folderToCreateFile.getChildren().put(file.getName(), file);
        return fileName;
    }

    private File findDirToCreateFile(final String path) {
        File f;
        String[] paths;

        if (path.startsWith("/")) {
            f = globalFileManager.getRoot();
            paths = path.substring(1).split("/");
        } else {
            f = globalFileManager.getCurrentDir();
            paths = path.split("/");
        }

        for (String p : paths) {
            f = f.getChildren().get(p);
            if (f == null) throw new RuntimeException("Directory does not exist : " + p);
        }
        return f;
    }
}
