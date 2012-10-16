package monitor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;

/**
 *
 * @author Krzysztof Kutt
 */
public class HTTPServer {
    static HashMap<Sensor, Monitor> register;  //który sensor jest przy którym monitorze
    static HashMap<Integer, Subscription> subscriptions;  //numer subskrypcji i jej dane
    
    public static void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(Config.HTTPServerPort), 0);
        
        HTTPServer.register = new HashMap<Sensor, Monitor>();
        HTTPServer.subscriptions = new HashMap<Integer, Subscription>();
        
        //lista obslugiwanych zasobów:
        server.createContext(Config.HTTPSubscriptionsResourceName, new SubscriptionsHandler());
        
        server.setExecutor(null); //default executor
        server.start();
    }
    
    static class SubscriptionsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String method = t.getRequestMethod();
            String path = t.getRequestURI().getPath();
            
            /***** nowa subskrypcja *****/
            if( method.equalsIgnoreCase("POST") && path.equalsIgnoreCase(Config.HTTPSubscriptionsResourceName) ){
                //TODO: klient chce subskrybcję określonego zasobu i metryki
                //UTWORZENIE SUBSKRYPCJI NA MONITORZE! każda subskrypcja na monitorze ma osobny port
                //UTWORZENIE SUBSKRYPCJI W REJESTRZE!
                
                //jeżeli taka jest to zostaje dopisana do listy, a klient dostaje jej adres
                //jeżeli nie ma to odpowiednie info zwrotne
            
            /***** pobierz subskrypcję *****/
            } else if( method.equalsIgnoreCase("GET") && !path.replace(Config.HTTPSubscriptionsResourceName,"").isEmpty() ){
                int resourceId = Integer.parseInt(path.replace(Config.HTTPSubscriptionsResourceName+"/", ""));
                
                //OTWARCIE PORTU NA MONITORZE
                
                
                
                String response = "This is the response";
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            
            /***** usuń subskrypcję *****/
            } else if( method.equalsIgnoreCase("DELETE") && !path.replace(Config.HTTPSubscriptionsResourceName,"").isEmpty() ){
                //TODO: klient chce skasować subskrybcję
                //monitor ją wykreśla z listy subskrybcji
                //i zamyka połączenie + kasuje je z listy aktywnych połączeń
                
            } else {
                //TODO: błędne zapytanie - zwrócić 404
                
            }
            
            
            /*
            System.out.println(t.getRequestURI().getPath().replace("/subscriptions/", ""));
            if( method.equalsIgnoreCase("GET") ){
                String response = "This is the response";
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }*/
            
        }
    }

}
