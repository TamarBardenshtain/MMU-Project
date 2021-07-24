package com.hit.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CLI implements Runnable{

    private static final List<String> legalCommands = new ArrayList<>();
    private final PropertyChangeSupport support;
    private final InputStream in;
    private final OutputStream out;
    private final Scanner reader;
    private String command = "";

    static {
        legalCommands.add("START");
        legalCommands.add("SHUTDOWN");
    }

    public CLI(InputStream in, OutputStream out) throws IOException {
        reader = new Scanner(new InputStreamReader(in));
        this.in = in;
        this.out = out;
        support = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl){
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl){
        support.removePropertyChangeListener(pcl);
    }

    public void write(String string) throws IOException {
        System.out.println(string);
    }

    public boolean isLegalCommand(String command){
        return legalCommands.contains(command);
    }

    @Override
    public void run() {
        try {
            write("Please enter your command!");
            while (true) {
                String s = reader.nextLine().toUpperCase();
                System.out.println(s);
                if (s.length() > 0) {
                    if (!isLegalCommand(s)) {
                        write("Command \"" + s + "\"not found");
                    } else {

                        support.firePropertyChange("command", command, s);
                        command = s;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
