package klient;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author Krzysztof Kutt
 */
public class SubscriptionsHandler {
    public static void newSubscription(List<Subscription> subscriptions){
        try {
            System.out.println("--- newSubscription ---");
            
            System.out.println("Podaj resourceId:");
            String resourceId = IO.readString();
            System.out.println("Podaj metric:");
            String metric = IO.readString();
            
            //stwórz XMLa
            String xml = XMLParser.createNewSubscriptionXML(resourceId, metric);
                    
            //TODO: wyślij żądanie do serwera (Config.HTTPAddress)
            
            
            //TODO: w zależności od odpowiedzi
            //albo nie przypisuj nic (i wyświetl komunikat o niepowodzeniu)
            
            
            //albo stwórz nową subskrypcję, nadaj jej resourceId, metric i location i wrzuć do listy
            
            
        } catch (IOException ex) {
            System.out.println("Zle wpisane wartosci!");
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
