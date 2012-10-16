package monitor;

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
    public static Sensor getSensorFromXml(InputStream is){
	//get the factory
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        String resourceId = "";
        String metric = "";
        
	try {
            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            //parse using builder to get DOM representation of the XML file
            Document dom = db.parse(is);
            //powinien być dokładnie jeden taki element
            resourceId = dom.getElementsByTagName("resourceId").item(0).getNodeValue();
            metric = dom.getElementsByTagName("metric").item(0).getNodeValue();

	}catch(ParserConfigurationException pce) {
            pce.printStackTrace();
	}catch(SAXException se) {
            se.printStackTrace();
	}catch(IOException ioe) {
            ioe.printStackTrace();
	}
        
        return new Sensor(resourceId, metric);
    }
    
    public static String createSubscriptionInfoXml(int id, String host, int port){
        String xml = "";
        xml += ( "<subscription id=\"" + id + "\">" );
        xml += ( "<host>" + host + "</host>" );
        xml += ( "<port>" + port + "</port>" );
        xml += "</subscription>";
        return xml;
    }
}
