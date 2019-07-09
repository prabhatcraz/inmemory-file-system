package com.prabhat.filesystem;

public interface FileSystem {
  Iterable<String> ls();

  /**
   * Support nested path
   */
  boolean mkdir(String path);

  /**
   * Return the absolute path
   */
  String touch(String path);

  /**
   * Support nested path, return absolute path
   */
  String cd(String path);

  String pwd();

  boolean rm(String path, boolean recursive);
}
