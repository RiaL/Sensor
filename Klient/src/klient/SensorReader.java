package klient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.CharBuffer;

/**
 *
 * @author Krzysztof Kutt
 */
public class SensorReader implements Runnable {
    String host;
    String port;
    String resourceId;
    String metric;
    String subscriptionId;

    public SensorReader(String host, String port, String resourceId, String metric, String subscriptionId) {
        this.host = host;
        this.port = port;
        this.resourceId = resourceId;
        this.metric = metric;
        this.subscriptionId = subscriptionId;
    }

    @Override
    public void run() {
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            socket = new Socket(host, Integer.parseInt(port));
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.out.println("Nieznany host: " + host);
            return;
        } catch (IOException e) {
            System.out.println("Problemy z wejsciem/wyjsciem przy polaczeniu z hostem: " + host);
            e.printStackTrace();
            return;
        }

        out.println(subscriptionId + "-" + resourceId + "-" + metric + "-");

        try {
            String response;
            CharBuffer target = CharBuffer.allocate(4096);
            int i = 0;
            
            //pętla odczytująca napływające dane
            //TODO: czy zamknie się jak zamknie się połączenie z drugiej strony?
            while ( (i = in.read()) != -1) {
                in.read(target);
                response = new String(target.array());
                System.out.println("********* SENSOR DATA:\n" + response);
                target.clear();
            }

            out.close();
            in.close();
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
