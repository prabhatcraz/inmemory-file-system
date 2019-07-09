package com.prabhat.filesystem;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class File {
  private String name;
  private File parent;
  private Map<String, File> children;
  private boolean isDirectory;
}
