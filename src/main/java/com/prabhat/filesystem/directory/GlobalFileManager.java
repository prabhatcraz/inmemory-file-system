package com.prabhat.filesystem.directory;

import com.prabhat.filesystem.File;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class GlobalFileManager {
  @Getter
  private File root = null;

  @Getter
  @Setter
  private File currentDir;

  public GlobalFileManager() {
    this.currentDir = root;
  }

  public void setRootFile(final File file) {
    if (root == null) {
      this.root = file;
      return;
    } else {
      throw new UnsupportedOperationException("Attempt to re-write the root directory!!");
    }
  }
}
