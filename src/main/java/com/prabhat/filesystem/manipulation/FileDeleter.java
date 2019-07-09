package com.prabhat.filesystem.manipulation;

import com.prabhat.filesystem.File;
import com.prabhat.filesystem.directory.GlobalFileManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FileDeleter {
    private final GlobalFileManager globalFileManager;

    public boolean remove(final String path, final boolean recursive) {
        if(path.equals(".") || path.equals("..") || path.equals("/")) {
            throw new RuntimeException("Cannot delete directory : " + path);
        }

        File f;
        String[] paths;

        if (path.startsWith("/")) {
            f = globalFileManager.getRoot();
            paths = path.substring(1).split("/");
        } else {
            f = globalFileManager.getCurrentDir();
            paths = path.split("/");
        }

        for(int i=0; i<paths.length - 1; i++) {
            f = f.getChildren().get(paths[i]);
            if (f == null) throw new RuntimeException("Directory does not exist : " + paths[i]);
        }

        // last string could be a file or a directory.
        final File fileTodelete = f.getChildren().get(paths[paths.length - 1]);
        if(fileTodelete == null) throw new RuntimeException("Directory does not exist : " + paths[paths.length - 1]);

        if(fileTodelete.isDirectory() && !recursive) {
            System.out.println(paths[paths.length - 1] + " : is a directory, use recursive");
            return false;
        } else {
            f.getChildren().remove(paths[paths.length - 1]);
        }
        return true;
    }


}
