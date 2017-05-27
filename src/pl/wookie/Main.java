package pl.wookie;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Main {

    public static final String FOLDER = "C:\\New folder\\Breaking Bad\\Season 4";

    private static void prepareAllFilesInFolder(String folder) {
        StringBuilder list = new StringBuilder();
        StringBuilder tempList = new StringBuilder();

        String pattern = "Breaking Bad - S[0-9]+E[0-9]+ - ([a-zA-Z\\s\\-']+)\\.m4v";
        Pattern r = Pattern.compile(pattern);

        //open all files in folder
        try (Stream<Path> paths = Files.walk(Paths.get(folder))) {
            paths.filter(Files::isRegularFile).forEach(path ->
            {
                String filename = path.getFileName().toString();
                String episodeName = "";
                if (filename.endsWith(".m4v")) {
                    Matcher matcher = r.matcher(filename);
                    if (matcher.find()) {
                        episodeName = matcher.group(1);
                    } else {
                        System.out.println(episodeName = "NO MATCH");
                    }
                    System.out.println(filename + ", " + episodeName);
                    list.append(filename + "\n");
                    tempList.append(makeNameShorter(filename, episodeName) + "\n");
                }
            });
        } catch (IOException e) {
            System.out.println(":(");
        }

        writeListToFile(list, "list1.txt"); //original names
        writeListToFile(tempList, "list2.txt");//shorter names
    }

    static void writeListToFile(StringBuilder list, String nameForFile) {
        try (PrintWriter printWriter = new PrintWriter(nameForFile)) {
            printWriter.println(list);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static final String SEPARATOR = " - ";
    static String makeNameShorter(String originalName, String partToCutOff) {
        return originalName.replaceFirst(SEPARATOR + partToCutOff, "");
    }


    static void matcherTest() {
        String line = "Breaking Bad - S04E01 - Box Cutter.m4v";
        String pattern = "Breaking Bad - S[0-9]+E[0-9]+ - ([a-zA-Z\\s\\-']+)\\.m4v";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(line);
        if (m.find( )) {
            System.out.println("Found value: " + m.group(0) );
            System.out.println("Found value: " + m.group(1) );
        }else {
            System.out.println("NO MATCH");
        }
    }
    public static void main(String[] args) {

        matcherTest();
        prepareAllFilesInFolder(FOLDER);

    }
}
