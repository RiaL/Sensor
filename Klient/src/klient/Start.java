package klient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
 * @author Krzysztof Kutt
 */
public class Start {

    public static void main(String[] args) {
        System.out.println("Witaj w Kliencie!");
        int opt = -1;
        while(opt != 0){
            try {
                printMenu();
                opt = readInteger();
                switch(opt){
                    case 1 :
                        System.out.println("opcja 1");
                        //TODO: tutaj obsługa
                        break;
                    case 2 :
                        System.out.println("opcja 2");
                        //TODO: tutaj obsługa
                        break;
                    case 3 :
                        System.out.println("opcja 3");
                        //TODO: tutaj obsługa
                        break;
                    case 4 :
                        System.out.println("opcja 4");
                        //TODO: tutaj obsługa
                        break;
                }
            } catch (IOException ex) {
                //do nothing (została podana zła liczba, wyświetli się ponownie menu)
            }
        }
        System.out.println("Zegnaj!");
    }
    
    private static void printMenu(){
        System.out.println("-----------------------");
        System.out.println("Co chcesz zrobic?");
        System.out.println("1 - nowa subskrypcja");
        System.out.println("2 - pobierz subskrypcje");
        System.out.println("3 - polacz z hostem");
        System.out.println("4 - zakoncz subskrypcje");
        System.out.println("0 - wyjdz");
        System.out.println("-----------------------");
    }
    
    private static int readInteger() throws IOException{
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in), 1);
        return Integer.parseInt(stdin.readLine());
    }
}
