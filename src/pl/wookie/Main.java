package pl.wookie;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {

    public static final String FOLDER = "C:\\New folder\\Breaking Bad\\Season 4";

    private static void renameAllFilesInFolder(String folder) {
        StringBuilder list = new StringBuilder();
        //open all files in folder
        try (Stream<Path> paths = Files.walk(Paths.get(folder))) {
            paths.filter(Files::isRegularFile).forEach(path ->
            {
                String filename = path.getFileName().toString();
                if (filename.endsWith(".m4v")) {
                    list.append(path.getFileName() + "\n");
                    System.out.println(path.getFileName());
                }
            });
        } catch (IOException e) {
            System.out.println(":(");
        }

        try (PrintWriter printWriter = new PrintWriter("list.txt")) {
            printWriter.println(list);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
	// write your code here
        renameAllFilesInFolder(FOLDER);

    }
}
