package com.prabhat.filesystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class InmemoryFileSystemApplication implements CommandLineRunner {

    private final FileSystem fileSystem;

    public InmemoryFileSystemApplication(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    public static void main(String[] args) {
        SpringApplication.run(InmemoryFileSystemApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("****** Start ******");
        while (true) {
            String input = scanner.nextLine();
            System.out.println(" --------------- ");
            try {
                if (input.equals("ls")) {
                    for (String s : fileSystem.ls()) {
                        System.out.println(s);
                    }
                } else if (input.startsWith("cd ")) {
                    System.out.println(fileSystem.cd(input.substring(3)));
                } else if (input.equals("pwd")) {
                    System.out.println(fileSystem.pwd());
                } else if (input.startsWith("rm -r ")) {
                    fileSystem.rm(input.substring(6), true);
                } else if (input.startsWith("rm ")) {
                    fileSystem.rm(input.substring(3), false);
                } else if(input.startsWith("mkdir ")) {
                    fileSystem.mkdir(input.substring(6));
                } else if(input.startsWith("touch ")) {
                    fileSystem.touch(input.substring(6));
                } else {
                    System.out.println("Not a valid command, try again!!");
                }
                System.out.println(" --------------- ");

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        }

    }
}
