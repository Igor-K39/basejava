package com.urise.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class MainFile {
    public static void main(String[] args) throws IOException {
        String filePath = ".\\.gitignore";

        File file = new File(filePath);
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }

        File dir = new File("./src/com/urise/webapp");
        System.out.println(dir.isDirectory());
        String[] list = dir.list();
        if (list != null) {
            for (String name : list) {
                System.out.println(name);
            }
        }

        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            System.out.println(fileInputStream.read());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }

        System.out.println("\nHW_8 starts here");
        String project_dir = System.getProperty("user.dir");
        printContent(new File(project_dir));
    }

    private static void printContent(File directory) throws IOException {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            System.out.println(file.getCanonicalPath());
            if (file.isDirectory()) {
                printContent(file);
            }
        }
    }
}
