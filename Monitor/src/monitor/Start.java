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
 * ========================================
 * 
 * Jak sensor może wysyłać dane?
 * 
 *    Sensor otwiera połączenie z 127.0.0.1 na porcie monitora (np Config.M1_SENSOR_PORT)
 *    Wysyła dane w formacie:
 *          resourceId:metric:value
 * 
 * 
 * @author Krzysztof Kutt
 */
public class Start {

    public static void main(String[] args) {
        
        try{
            HTTPServer.start();
        } catch (IOException ex) {
            System.out.println("Nie udalo sie uruchomic serwera HTTP... :(");
            ex.printStackTrace();
        }
        
        
        Runnable monitor1 = new Monitor(Config.M1_CLIENT_PORT, Config.M1_SENSOR_PORT);
        Thread thread1 = new Thread(monitor1);
	thread1.start();
        
        Monitor monitor2 = new Monitor(Config.M2_CLIENT_PORT, Config.M2_SENSOR_PORT);
        Thread thread2 = new Thread(monitor2);
	thread2.start();
        
    }
}
