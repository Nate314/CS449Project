package com.nathangawith.umkc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class FileIO {

    /**
     * reads file
     * code from https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
     * @param filename file to read
     * @return array of lines from the file
     */
    public static String[] readFileLines(String filename) throws Exception {
        String result = "";
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file)); 
        String st;
        while ((st = br.readLine()) != null) result += st + '\n';
        br.close();
        return result.split("\n");
    }

    /**
     * writes file
     * code from https://www.w3schools.com/java/java_files_create.asp
     * @param filename file name to write to
     * @param output string to write to the specified file name
     */
    public void writeFile(String filename, String output) {
        try {
            File myObj = new File(filename);
            myObj.createNewFile();
        } catch (IOException e) {
            System.out.println("An error occurred when creating the file.");
            e.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(output);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred when writing the file contents.");
            e.printStackTrace();
        }
    }
}
