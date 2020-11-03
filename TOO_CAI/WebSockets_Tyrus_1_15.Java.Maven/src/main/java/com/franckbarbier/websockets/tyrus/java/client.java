package com.franckbarbier.websockets.tyrus.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class client {
    private String username;
    private String password;

    public client(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean exist() {
        File userFile = new File("src/main/java/com/franckbarbier/websockets/tyrus/java/UserSaves/" + this.username + "_" + this.password + ".json");
        return userFile.exists();
    }

    public boolean create() {
        File userFile = new File("src/main/java/com/franckbarbier/websockets/tyrus/java/UserSaves/" + this.username + "_" + this.password + ".json");
        System.out.println(userFile.getAbsoluteFile());
        try {
            return userFile.createNewFile();
        } catch (IOException err) {
            System.err.println(err.toString());
            return false;
        }
    }

    public String get() throws FileNotFoundException {
        File userFile =  new File("src/main/java/com/franckbarbier/websockets/tyrus/java/UserSaves/" + this.username + "_" + this.password + ".json");
        StringBuilder fileContent = new StringBuilder();
        if (this.exist()) {
            Scanner reader = new Scanner(userFile);
            while (reader.hasNextLine()) {
                fileContent.append(reader.nextLine());
            }
        }
        return fileContent.toString();
    }

    public boolean save(String value) {
        try {
            FileWriter saveFile = new FileWriter("src/main/java/com/franckbarbier/websockets/tyrus/java/UserSaves/" + this.username + "_" + this.password + ".json");
            saveFile.write(value);
            saveFile.close();
            return true;
        } catch (IOException err) {
            return false;
        }
    }
}
