package com.hit.server;

import com.hit.util.CLI;

import java.io.IOException;

public class CacheUnitServerDriver {

    public static void main(String[] args) {
        CLI cli = null;
        try {
            cli = new CLI(System.in, System.out);
            Server server = new Server();
            cli.addPropertyChangeListener(server);
            new Thread(cli).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}