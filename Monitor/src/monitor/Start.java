package monitor;

import java.io.IOException;

/**
 * Start serwera i monitorów!
 * 
 * ========================================
 * 
 * Jak Klient może korzystać z tego serwera i monitorów?
 * 
 * 1) żadanie: POST /subscriptions
 *    jako wartość (białe znaki dowolnie, przechodzi to później przez parserXML, więc nie ma to znaczenia):
 *    <subscribe>
 *      <resourceId>host1</resourceId>
 *      <metric>param1</param>
 *    </subscribe>
 * 
 *    w odpowiedzi:
 *    - Location: 127.0.0.1/subscriptions/190   (numer subskrypcji)
 *    - Error 404, gdy nie ma takiego zasobu/metryki
 * 
 * 2) GET 127.0.0.1/subscriptions/190   (podane w odpowiedzi w punkcie wyżej)
 * 
 *    w odpowiedzi:
 *    - Error 404, gdy nie ma subskrypcji
 *    - <subscription id="190">
 *        <host>127.0.0.1</host>
 *        <port>11000</port>
 *      </subscription>
 * 
 * 3) connect z podanym hostem i portem
 *    w odpowiedzi będą w losowych momentach czasu napływały pomiary postaci:
 *    <measurement resourceId="host1" metric="param1">
 *      <timestamp>[tutaj timestamp]</timestamp>
 *      <value>0.92</value>
 *    </measurement>
 * 
 * 4) DELETE 127.0.0.1/subscriptions/190
 * 
 *    w odpowiedzi:
 *    - Error 404 w przypadku braku subskrypcji
 *    - brak odpowiedzi i zamknięcie połączenia (przez serwer) w przypadku powodzenia
 * 
 * 
 * ========================================
 * 
 * Jak sensor może wysyłać dane?
 * 
 * 1) Trzeba najpierw ręcznie dodać w poniższym pliku odpowiednie dane sensora (zasób i metryka) do odpowiedniego monitora
 * 
 * 2) Sensor otwiera połączenie z 127.0.0.1 na porcie monitora (np Config.Monitor1SensorPort)
 *    Wysyła dane w XMLu (poniżej)
 *    Połączenie jest zamykane przez Monitor po odebraniu danych
 * 
 *    Format XMLa:
 *    <measurement>
 *      <resourceId>host1</resourceId>
 *      <metric>param1</metric>
 *      <value>0.92</value>
 *    </measurement>
 * 
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
        monitor1.addSensor(new Sensor("host1", "param1"));
        monitor1.addSensor(new Sensor("host1", "param2"));
        monitor1.run();
        
        Monitor monitor2 = new Monitor(Config.Monitor2SensorPort, Config.Monitor2SubsPorts);
        monitor2.addSensor(new Sensor("host2", "param1"));
        monitor2.addSensor(new Sensor("host3", "param1"));
        monitor2.run();
        
        
    }
}
