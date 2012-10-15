package monitor;

/**
 *
 * @author Krzysztof Kutt
 */
public class Monitor {

    public static void main(String[] args) {
        
        //TODO: ta klasa może być od razu prostym serwerem HTTP (o ile zakładamy, że będzie tylko jeden Monitor to na pewno
        //nie ma z tym problemu - przy większej ilości nie wiem
        //http://docs.oracle.com/javase/6/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/package-summary.html
        //tutaj opis prostego serwera: http://stackoverflow.com/questions/3732109/simple-http-server-in-java-using-only-java-se-api
        //(nie przejmuj się tym, że go zjechali, żeby z tego nie korzystać - to weszło do Javy 7 pod tą samą nazwą, więc Oracle
        //zdecydował się dalej to wspierać ;) )
        
        
        //TODO: ma listę podłączonych sensorów - wystarczy jakaś na stałę wklepana lista
        //(nic nie jest napisane o tym, że ma być elastyczny i dynamicznie dodawać sensory)
        //wystarczy lista par: nazwa zasobu (np adres ip) + nazwa metryki (string)
        
        
        //TODO: ma listę subskrybcji, na początku pustą
        //id subskrybcji, [ nazwa zasobu (np adres ip) + nazwa metryki (string) ] - te dwa mogą być zamienione na jakieś id sensora
        
        
        //TODO: obsługuje wiele kanałów komunikacji
        //"Do obsługi wielu kanałów komunikacji należy wykorzystać selector (klasa
        //java.nio.channels.Selector)."
        // nie wiem jeszcze jak to działa - trzeba będzie sprawdzić w dokumentacji
        
        
        //TODO: dodawanie nowej subskrybcji (HTTP)
        //klient chce subskrybcję określonego zasobu i metryki
        //jeżeli taka jest to zostaje dopisana do listy, a klient dostaje o niej info
        //jeżeli nie ma to odpowiednie info zwrotne
        
        
        //TODO: pobranie subskrybcji (HTTP)
        //klient wchodzi na adres, który dostał przy dodawaniu subskrybcji
        //dostaje w odpowiedzi host i port na którym ma nasłuchiwać
        
        
        //TODO: nawiązanie połączenia (TCP)
        //klient ma możliwość nawiązania połączenia, monitor przechowuje listę połączeń (numer subskrybcji, adres ip, port)
        
        
        //TODO: sensor przysyła jakieś dane (TCP)
        //podaje swoje dane (id, albo para nazwa zasobu (np adres ip) + nazwa metryki (string)) + wartość metryki
        //sprawdzamy na liście subskrybcji kto chciał dany zasób
        //jeżeli chciał to szukamy na liście aktywnych połączeń czy ta subskrybcja jest aktywna i jeżeli tak to przesyłamy
        
        
        //TODO: usuwanie subskrybcji (HTTP)
        //klient chce skasować subskrybcję
        //monitor ją wykreśla z listy subskrybcji
        //i zamyka połączenie + kasuje je z listy aktywnych połączeń
        
        
    }
}
