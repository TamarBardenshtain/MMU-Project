package com.hit.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.DataModel;
import com.hit.services.CacheUnitController;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class HandleRequest<T> implements Runnable {

    private final Socket socket;
    private final CacheUnitController<T> controller;
    private String res;
    private boolean suc;
    Scanner reader;
    PrintWriter writer;

    public HandleRequest(Socket s, CacheUnitController<T> controller) {
        this.socket = s;
        this.controller = controller;
    }


    @Override
    public void run() {
        System.out.println("handling request");
        Gson gson = new Gson();
        try {
            boolean statsRequest = false;

            reader = new Scanner(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            String req = reader.nextLine();
            Type ref = new TypeToken<Request<DataModel<T>[]>>(){}.getType();
            Request<DataModel<T>[]> request = new Gson().fromJson(req,ref);
            Map<String, String> headers = request.getHeaders();
            DataModel<T>[] body = request.getBody();

            String command = headers.get("action");
            switch (command) {
                case "GET":
                    suc = controller.get(body) != null;
                    break;

                case "DELETE":
                    suc = controller.delete(body);
                    break;

                case "UPDATE":
                    suc = controller.update(body);
                    break;

                case "STATS":
                    res = controller.getStatistics();
                    System.out.println(res);
                    statsRequest = true;
                    break;
            }
            if(!statsRequest){
                if(suc){
                    res = "true";
                }
                else res = "false";
            }
            writer.println(res);
            writer.flush();
            writer.close();
            reader.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}