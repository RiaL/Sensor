package klient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Krzysztof Kutt
 */
public class IO {
    public static int readInteger() throws IOException{
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in), 1);
        return Integer.parseInt(stdin.readLine());
    }
    
    public static String readString() throws IOException{
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in), 1);
        return stdin.readLine();
    }
}
