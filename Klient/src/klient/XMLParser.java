package klient;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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
    
    public static String getValueFromXml(InputStream is, String valueName){
	//get the factory
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        String value = "";
        
	try {
            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            //parse using builder to get DOM representation of the XML file
            Document dom = db.parse(is);
            //powinien być dokładnie jeden taki element
            value = dom.getElementsByTagName(valueName).item(0).getFirstChild().getNodeValue();

	}catch(ParserConfigurationException pce) {
            pce.printStackTrace();
	}catch(SAXException se) {
            se.printStackTrace();
	}catch(IOException ioe) {
            ioe.printStackTrace();
	}
        
        return value;
    }
    
    //TODO: funkcje pobierające dane z XMLów otrzymanych przy pobieraniu subskrypcji
    //i przy odbieraniu danych pomiarowych
    //TODO: funkcja do pobierania wartości odpowiednich węzłów XMLa
    //TODO: funkcja do pobierania atrybutów odpowiednich węzłów XMLa
    
}
