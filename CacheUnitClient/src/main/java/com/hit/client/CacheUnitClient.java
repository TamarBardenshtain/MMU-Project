package com.hit.client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CacheUnitClient {

    public CacheUnitClient()  {}

    public String send(String request) throws ClassNotFoundException
    {
        String res = "Empty";
        try {
            int ipPort = 12345;
            Socket s = new Socket("localhost", ipPort);//Creating Socket over "LocalHost"
            Scanner reader = new Scanner(new InputStreamReader(s.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            writer.println(request);
            writer.flush();

            res = (String) reader.nextLine();//Convert  the response to String
            writer.close();
            reader.close();
            s.close();
        }catch(IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
