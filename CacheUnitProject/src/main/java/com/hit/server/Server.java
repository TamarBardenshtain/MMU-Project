package com.hit.server;

import com.hit.services.CacheUnitController;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable, PropertyChangeListener {

    public final int port = 12345;
    private boolean isRunning = false;
    private  ServerSocket serverSocket;

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        switch ((String) event.getNewValue()){
            case "START":
                if (!isRunning)
                {
                    isRunning = true;
                    new Thread(this).start();
                }
                else System.out.println("Server is already up\n");
                break;

            case "SHUTDOWN":
                if(!isRunning)
                    System.out.println("Server is already down\n");
                else
                {
                    try {
                        isRunning = false;
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Shutdown server ");
                }
                break;
        }
    }

    @Override
    public void run() {
        try{
            serverSocket = new ServerSocket(port);
            System.out.println("Server is running....");
            ExecutorService executor = Executors.newFixedThreadPool(20);
            CacheUnitController<String> cacheUnitController = new CacheUnitController<>();

            while (isRunning) {
                Socket request = serverSocket.accept();
                HandleRequest<String> reqHandler = new HandleRequest<>(request, cacheUnitController);
                executor.execute(new Thread(reqHandler));

            }
            executor.shutdown();

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(serverSocket != null)
                    serverSocket.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
