package monitor;

import java.io.IOException;

/**
 *
 * @author Krzysztof Kutt
 */
public class Start {

    public static void main(String[] args) {
        
        //serwer obsługujący żądania HTTP
        try{
            HTTPServer.start();
        } catch (IOException ex) {
            System.out.println("Nie udalo sie uruchomic serwera HTTP... :(");
            ex.printStackTrace();
        }
           
        
        //monitory obsługujące połączenia TCP
        Monitor monitor1 = new Monitor(Config.Monitor1SensorPort, Config.Monitor1SubsPorts);
        //monitor1.addSensor(new Sensor("host1", "param1"));
        monitor1.addSensor(new Sensor("abc", "cpu"));
        monitor1.addSensor(new Sensor("host1", "param2"));
        monitor1.run();
        
        Monitor monitor2 = new Monitor(Config.Monitor2SensorPort, Config.Monitor2SubsPorts);
        monitor2.addSensor(new Sensor("host2", "param1"));
        monitor2.addSensor(new Sensor("host3", "param1"));
        monitor2.run();
        
        
        //TODO: ma listę podłączonych sensorów - wystarczy jakaś na stałę wklepana lista
        //(nic nie jest napisane o tym, że ma być elastyczny i dynamicznie dodawać sensory)
        //wystarczy lista par: nazwa zasobu (np adres ip) + nazwa metryki (string)
        
        
        //TODO: obsługuje wiele kanałów komunikacji
        //"Do obsługi wielu kanałów komunikacji należy wykorzystać selector (klasa
        //java.nio.channels.Selector)."
        // nie wiem jeszcze jak to działa - trzeba będzie sprawdzić w dokumentacji
        
        
        //TODO: nawiązanie połączenia (TCP)
        //klient ma możliwość nawiązania połączenia, monitor przechowuje listę połączeń (numer subskrybcji, adres ip, port)
        
        
        //TODO: sensor przysyła jakieś dane (TCP)
        //podaje swoje dane (id, albo para nazwa zasobu (np adres ip) + nazwa metryki (string)) + wartość metryki
        //sprawdzamy na liście subskrybcji kto chciał dany zasób
        //jeżeli chciał to szukamy na liście aktywnych połączeń czy ta subskrybcja jest aktywna i jeżeli tak to przesyłamy
        

        
    }
}
