package klient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 *
 * @author Krzysztof Kutt
 */
public class SubscriptionsHandler {
    public static void newSubscription(List<Subscription> subscriptions){
        String xml = "";
        String resourceId = "";
        String metric = "";
        try {
            System.out.println("--- newSubscription ---");

            System.out.println("Podaj resourceId:");
            resourceId = IO.readString();
            System.out.println("Podaj metric:");
            metric = IO.readString();

            //stwórz XMLa
            xml = XMLParser.createNewSubscriptionXML(resourceId, metric);
        } catch (IOException ex) {
            System.out.println("Zle wpisane wartosci!");
        }
        try {
            //wyślij żądanie do serwera
            URL url = new URL(Config.HTTPAddress);
            URLConnection connection = url.openConnection();
            ((HttpURLConnection)connection).setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(xml);
            out.close();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                    connection.getInputStream()));
            String response = in.readLine();
            in.close();
            
            if( response.startsWith("Location") ){
                //stwórz nową subskrypcję, nadaj jej resourceId, metric i location i wrzuć do listy
                Subscription sub = new Subscription();
                sub.setResourceId(resourceId);
                sub.setMetric(metric);
                sub.setLocation(response.replaceFirst("Location: ", ""));
                subscriptions.add(sub);
            } else {
                //nie przypisuj nic (i wyświetl komunikat o niepowodzeniu)
                System.out.println("Blad! Odpowiedz serwera: " + response);
            }
            
        } catch (MalformedURLException ex) {
            System.out.println("Zly adres!");
        } catch (IOException ex) {
            System.out.println("Problemy z polaczeniem!");
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public static void getSubscription(List<Subscription> subscriptions){
        System.out.println("--- getSubscription ---");
        int subIndex = selectSubscription(subscriptions);
        //TODO: jeżeli ta subskrypcja ma już host i port, nie rób nic, tylko wypisz komunikat
        
        
        //TODO: wyślij żądanie do serwera
        
        
        //TODO: w zależności od odpowiedzi:
        //albo nie przypisuj nic (i wyświetl komunikat o niepowodzeniu)
        
        
        //albo wyciągnij host i port z XMLa (funkcja w XMLParser) i przypisz do odpowiedniej subskrypcji na liście
        
        
    }
    
    public static void connectHost(List<Subscription> subscriptions){
        System.out.println("--- connectHost ---");
        int subIndex = selectSubscription(subscriptions);
        //TODO: jeżeli ta subskrypcja nie ma hosta i portu, nie rób nic, tylko wypisz komunikat
        
        
        //TODO: stwórz OSOBNY WĄTEK, który: połączy się z serwerem i w pętli wyświetla
        //napływające dane (tutaj też odpowiednia funkcja w XMLParserze?)
        //WĄTEK może zostać przerwany tylko w sytuacji zamknięcia połączenia przez serwer!
        
        
        //WARN: program będzie działał tak, że będzie wyświetlał te dane na tym samym okienku konsoli
        //na którym będzie menu - jest to trochę mało przyjemne (musisz np wybrać opcję w menu,
        //a tu Ci napływają nowe pomiary), ale tak najprościej to zrobić wg mnie
        
    }
    
    public static void closeSubscription(List<Subscription> subscriptions){
        System.out.println("--- closeSubscription ---");
        int subIndex = selectSubscription(subscriptions);
        //TODO: wyślij żądanie do serwera
        
        
        //skasuj subskrypcję z listy subskrypcji
        subscriptions.remove(subIndex);
    }
    
    private static int selectSubscription(List<Subscription> subscriptions){
        int subIndex = 0;  //w przypadku wpisania liczby wykraczającej poza zakres
                           //albo w przypadku wpisania czegoś co nie jest liczbą
                           //zwróci indeks pierwszej subskrypcji
        
        try {
            System.out.println("Ktora subskrypcje wybierasz?");
            //TODO: wyświetl listę dostępnych subskrypcji (tak, żeby było widać wszystkie dane)
            //ponumeruj je indeksami z listy (użytkownik ma wybrać właśnie indeks)
            
            
            //wczytaj liczbę wpisaną przez użytkownika
            int opt = IO.readInteger();
            
            if( opt > 0 && opt < subscriptions.size())
                subIndex = opt;
            
        } catch (IOException ex) {
            //nie rób nic; zwróci indeks 0
        }
        
        return subIndex;
    }
}
