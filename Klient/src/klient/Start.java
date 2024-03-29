package klient;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Start klienta!
 * 
 * 1) żadanie: POST /subscriptions
 *    jako wartość (białe znaki dowolnie, przechodzi to później przez parserXML, więc nie ma to znaczenia):
 *    <subscribe>
 *      <resourceId>host1</resourceId>
 *      <metric>param1</param>
 *    </subscribe>
 * 
 *    w odpowiedzi:
 *    - Location: http://127.0.0.1:8000/subscriptions/190   (numer subskrypcji)
 *    - Error 404, gdy nie ma takiego zasobu/metryki
 * 
 * 2) GET http://127.0.0.1:8000/subscriptions/190   (podane w odpowiedzi w punkcie wyżej)
 * 
 *    w odpowiedzi:
 *    - Error 404, gdy nie ma subskrypcji
 *    - <subscription id="190">
 *        <host>127.0.0.1</host>
 *        <port>11000</port>
 *      </subscription>
 * 
 * 3) connect z podanym hostem i portem
 *    wysłać dane:
 *        subscriptionID-resourceId-metric
 * 
 *    w odpowiedzi będą w losowych momentach czasu napływały pomiary postaci:
 *    <measurement resourceId="host1" metric="param1">
 *      <timestamp>[tutaj timestamp]</timestamp>
 *      <value>0.92</value>
 *    </measurement>
 * 
 * 4) DELETE http://127.0.0.1:8000/subscriptions/190
 * 
 *    w odpowiedzi:
 *    - Error 404 w przypadku braku subskrypcji
 *    - brak odpowiedzi i zamknięcie połączenia (przez serwer) w przypadku powodzenia
 * 
 * 
 * @author Krzysztof Kutt
 */
public class Start {

    public static void main(String[] args) {
        List<Subscription> subscriptions = new LinkedList<Subscription>();
        int opt = -1;
        
        while(opt != 0){
            try {
                printMenu();
                opt = IO.readInteger();
                switch(opt){
                    case 1 :
                        SubscriptionsHandler.newSubscription(subscriptions);
                        break;
                    case 2 :
                        if(subscriptions.size() > 0)
                            SubscriptionsHandler.getSubscription(subscriptions);
                        else
                            System.out.println("Brak subskrypcji!");
                        break;
                    case 3 :
                        if(subscriptions.size() > 0)
                            SubscriptionsHandler.connectHost(subscriptions);
                        else
                            System.out.println("Brak subskrypcji!");
                        break;
                    case 4 :
                        if(subscriptions.size() > 0)
                            SubscriptionsHandler.closeSubscription(subscriptions);
                        else
                            System.out.println("Brak subskrypcji!");
                        break;
                    case 9 :
                        if(subscriptions.size() > 0)
                            SubscriptionsHandler.listSubscriptions(subscriptions);
                        else
                            System.out.println("Brak subskrypcji!");
                        break;
                }
            } catch (IOException ex) {
                //do nothing (została podana zła liczba, wyświetli się ponownie menu)
            } catch (NumberFormatException ex){
                //do nothing (została podana zła liczba, wyświetli się ponownie menu)
            }
        }
        System.out.println("Zegnaj!");
    }
    
    private static void printMenu(){
        System.out.println("------------------------------ MENU ------------------------------");
        System.out.println("1 - nowa sub;     3 - polacz z hostem;  9 - wypisz wszystkie sub");
        System.out.println("2 - pobierz sub;  [[4 - zakoncz sub]];  0 - wyjdz");
        System.out.println("------------------------------------------------------------------");
    }
}
