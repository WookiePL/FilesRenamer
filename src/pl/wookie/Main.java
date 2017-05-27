package pl.wookie;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Main {

    public static final String FOLDER = "C:\\New folder\\Breaking Bad\\Season 4";
    public static final String ORIGINAL_LIST = "list1.txt";
    public static final String SHORTER_LIST = "list2.txt";

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

        writeListToFile(list, ORIGINAL_LIST); //original names
        writeListToFile(tempList, SHORTER_LIST);//shorter names
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

    static void renameAllFilesInFolder(String folder, String filenameOfOriginalList, String filenameOfListForRenaming, boolean isChangingExtension) throws IOException {
        List<String> originalList = Files.readAllLines(Paths.get(filenameOfOriginalList), StandardCharsets.UTF_8);
        List<String> listForRenaming = Files.readAllLines(Paths.get(filenameOfListForRenaming), StandardCharsets.UTF_8);
        originalList.remove("");
        listForRenaming.remove("");

        for (int i = 0; i < originalList.size(); i++) {
            String originalName = folder + "\\" + originalList.get(i);
            String newName = folder + "\\" + listForRenaming.get(i);
            if (isChangingExtension) {
                originalName = originalName.replaceFirst(".m4v", ".txt");
                newName = newName.replaceFirst(".m4v", ".txt");
            }
            if (new File(originalName).renameTo(new File(newName))) {
                System.out.println(originalList.get(i) + " renamed to: " + listForRenaming.get(i));
            } else {
                System.out.println("FAIL");
            }
        }

    }

    static void matcherTest() {
        String line = "Breaking Bad - S04E01 - Box Cutter.m4v";
        String pattern = "Breaking Bad - S[0-9]+E[0-9]+ - ([a-zA-Z\\s\\-']+)\\.m4v";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(line);
        if (m.find()) {
            System.out.println("Found value: " + m.group(0));
            System.out.println("Found value: " + m.group(1));
        } else {
            System.out.println("NO MATCH");
        }
    }

    public static void main(String[] args) {

        matcherTest();

        int PART = 2;

        switch (PART) {
            case 1:
                prepareAllFilesInFolder(FOLDER);
                try {
                    renameAllFilesInFolder(FOLDER, ORIGINAL_LIST, SHORTER_LIST, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    renameAllFilesInFolder(FOLDER, SHORTER_LIST, ORIGINAL_LIST, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    renameAllFilesInFolder(FOLDER, SHORTER_LIST, ORIGINAL_LIST, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }


    }
}
