package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("::::   Welcome to GREP TOOL   ::::");
        while (true) {
            System.out.println("grep [-r] [-v] [-i] <pattern> <directory or filename>");
            System.out.println("---------------------------------------------------------");
            Scanner sc = new Scanner(System.in);
            String userInput = sc.nextLine();
            // if the user want to exit
            if (userInput.equalsIgnoreCase("exit")) {
                System.out.println("THANKS :)");
                break;
            }
            String[] partsInput = userInput.split(" ");

            boolean recursive = false;
            boolean invertSearch = false;
            boolean caseInsensitive = false;
            int patternIndex = 1;
            String pattern = null;
            String path = null;

            // Check for recursive option
            if (userInput.contains("-r")) {
                recursive = true;
                patternIndex++;
            }

            // Check for invert search option
            if (userInput.contains("-v")) {
                invertSearch = true;
                patternIndex++;
            }

            // Check for case-insensitive option
            if (userInput.contains("-i")) {
                caseInsensitive = true;
                patternIndex++;
            }

            pattern = partsInput[patternIndex]; // The pattern to search for
            if(userInput.contains("*")){
                path=new File("src/test/java/challenge-grep").getAbsolutePath();
            }
            else if(pattern.contains("txt")){
                path = partsInput[patternIndex];
            }else{
                path = partsInput[patternIndex + 1];
            }

            try {
                grep(pattern, path, recursive, invertSearch, caseInsensitive);
            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
                System.exit(2);
            }

        }


    }

    private static void grep(String pattern, String path, boolean recursive, boolean invertSearch, boolean caseInsensitive) throws IOException {
        File fileOrDir = new File(path);

        // Handle directory recursively
        if (fileOrDir.isDirectory()) {
            File[] files = fileOrDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    grep(pattern, file.getAbsolutePath(), recursive, invertSearch, caseInsensitive);
                }
            }
        } else { // Handle regular file
            BufferedReader reader = new BufferedReader(new FileReader(fileOrDir));
            String line;

            while ((line = reader.readLine()) != null) {
                boolean patternFound = matchesPattern(line, pattern, caseInsensitive);

                if (invertSearch) {
                    patternFound = !patternFound;
                }

                if (patternFound) {
                    System.out.println(fileOrDir.getAbsolutePath() + ": " + line);
                }
            }

            reader.close();
        }
    }

    private static boolean matchesPattern(String line, String pattern, boolean caseInsensitive) {

        pattern = pattern.replaceAll("\\\\d", "\\\\d");
        pattern = pattern.replaceAll("\\\\w", "\\\\w");
        int flags = caseInsensitive ? Pattern.CASE_INSENSITIVE : 0;
        Pattern regexPattern = Pattern.compile(pattern, flags);
        Matcher matcher = regexPattern.matcher(line);

        return matcher.find();
    }
}