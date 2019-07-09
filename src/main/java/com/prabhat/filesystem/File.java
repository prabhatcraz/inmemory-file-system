package com.prabhat.filesystem;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class File {
  String name;
  File parent;
  Map<String, File> children;
  boolean isDirectory;
}
