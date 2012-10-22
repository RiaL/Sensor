package klient;

/**
 *
 * @author Krzysztof Kutt
 */
public class XMLParser {
    public static String createNewSubscriptionXML(String resourceId, String metric){
        String xml = "";
        xml = xml + "<subscribe>";
        xml = xml + "<resourceId>" + resourceId + "</resourceId>";
        xml = xml + "<metric>" + metric + "</metric>";
        xml = xml + "</subscribe>";
        return xml;
    }
    
    //TODO: funkcje pobierające dane z XMLów otrzymanych przy pobieraniu subskrypcji
    //i przy odbieraniu danych pomiarowych
    //TODO: funkcja do pobierania wartości odpowiednich węzłów XMLa
    //TODO: funkcja do pobierania atrybutów odpowiednich węzłów XMLa
    
}
