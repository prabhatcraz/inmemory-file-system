package com.prabhat.filesystem.directory;

import com.prabhat.filesystem.File;
import com.prabhat.filesystem.exceptions.BadPathException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ChangeDirectoryManager {
    private final GlobalFileManager globalFileManager;

    public String changeDir(final String path) {
        // current directory do nothing.
        if(path.equals(".")) {
            return path;
        }

        // Parent directory
        if(path.equals("..")) {
            if(globalFileManager.getCurrentDir().getParent() != null) {
                globalFileManager.setCurrentDir(globalFileManager.getCurrentDir().getParent());
                return path;
            }
        }

        // Go to the root
        if(path.equals("/")) {
            globalFileManager.setCurrentDir(globalFileManager.getRoot());
            return path;
        }

        File f;
        String[] paths;
        if(path.startsWith("/"))  {
            f = globalFileManager.getRoot();
            paths = path.substring(1).split("/");
        } else {
            f = globalFileManager.getCurrentDir();
            paths = path.split("/");
        }

        for(String p : paths) {
            f = f.getChildren().get(p);
            if(f == null || !f.isDirectory()) throw new BadPathException("Wrong path specified.");
            globalFileManager.setCurrentDir(f);
        }
        return path;
    }
}
