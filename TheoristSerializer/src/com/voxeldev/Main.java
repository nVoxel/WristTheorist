package com.voxeldev;

import com.google.gson.Gson;
import com.voxeldev.models.TheoristBook;
import com.voxeldev.models.TheoristNote;
import com.voxeldev.models.TheoristSubject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        System.out.println("Please, avoid using dots in notes filenames (except file extensions)");
        System.out.println("Enter full path to your book folder");
        
        File bookFolder;
        
        while (true){
            bookFolder = new File(new Scanner(System.in).nextLine());
            
            if (!bookFolder.exists()){
                System.out.println("This directory doesn't exist. Try another one!");
                continue;
            }
            
            break;
        }
        
        var book = new TheoristBook(bookFolder.getName());
        
        var subjectFolders = getDirectoryContents(bookFolder, true);
        if (subjectFolders == null || subjectFolders.length == 0){
            return;
        }
        
        for (var subjectFolder : subjectFolders){
            var subject = new TheoristSubject(subjectFolder.getName());
            
            var noteFiles = getDirectoryContents(subjectFolder, false);
            if (noteFiles == null || noteFiles.length == 0){
                book.subjectList.add(subject);
                continue;
            }
            
            for (var noteFile : noteFiles){
                try{
                    String noteFileName = noteFile.getName().split("\\.")[0];
                    subject.noteList.add(new TheoristNote(noteFileName, readFile(noteFile),
                            noteFileName.toLowerCase().contains("img")));
                }
                catch (Exception e){
                    var message = e.getMessage();
                    System.out.println((message == null || message.isEmpty()) ? e.getClass().getName() : message);
                    System.out.println("File: " + noteFile.getName());
                }
            }
            
            book.subjectList.add(subject);
        }
    
        try{
            var path = String.format("%s\\%s.json", bookFolder.getPath(), bookFolder.getName());
            writeFile(path, new Gson().toJson(book));
            System.out.println("Your book has been serialised and written to: " + path);
        }
        catch (Exception e){
            var message = e.getMessage();
            System.out.println("Error while serializing: " + (message == null || message.isEmpty() ? e.getClass().getName() : message));
            System.out.println("Try running program as administrator");
        }
    }
    
    private static File[] getDirectoryContents(File directory, boolean subdirectories) {
        return directory.listFiles((file, s) -> subdirectories ? new File(file, s).isDirectory() : new File(file, s).isFile());
    }
    
    private static String readFile(File file) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();
        
        Scanner scanner = new Scanner(reader);
        while (scanner.hasNextLine()){
            stringBuilder.append(scanner.nextLine());
            if (scanner.hasNextLine()) {
                stringBuilder.append("\n");
            }
        }
        scanner.close();
        
        return stringBuilder.toString();
    }
    
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void writeFile(String path, String data) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
        writer.write(data);
        writer.close();
    }
}
