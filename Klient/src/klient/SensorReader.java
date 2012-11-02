package klient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

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
            boolean run = true;
            //pętla odczytująca napływające dane
            //TODO: czy zamknie się jak zamknie się połączenie z drugiej strony?
            while (run && (i = in.read()) != -1) {
               
                if (stdIn.ready()) {
                    String command = stdIn.readLine();
                    if (command.equalsIgnoreCase("end")) {
                        try {
                            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            wr.write(command);
                            wr.flush();
                            run = false;
                        } catch (IOException e) {
                        }
                        Thread.sleep(1000);
                        continue;
                    }
                }

                in.read(target);
                response = new String(target.array());
                System.out.println(response);
                target.clear();
            }

            out.close();
            in.close();
            socket.close();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
