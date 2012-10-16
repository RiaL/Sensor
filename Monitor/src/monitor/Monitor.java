package monitor;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author Krzysztof Kutt
 */
public class Monitor implements Runnable {
    
    int sensorPort;
    int subscriptionStartPort;
    HashMap<Integer, Sensor> subscriptions;  //numer portu, sensor z którego dane ma tam wysyłać
    LinkedList<ServerSocketChannel> clientChannels;
    
    Selector selector;

    public Monitor(int sensorPort, int subscriptPort) {
        this.sensorPort = sensorPort;
        this.subscriptionStartPort = subscriptPort;
        
        subscriptions = new HashMap<Integer, Sensor>();
        clientChannels = new LinkedList<ServerSocketChannel>();
    }

    @Override
    public void run() {
        try {
            this.selector = Selector.open();
            
            ServerSocketChannel sensorChannel = ServerSocketChannel.open();
            sensorChannel.socket().bind(new InetSocketAddress(sensorPort));
            sensorChannel.configureBlocking(false);
            sensorChannel.register(selector, SelectionKey.OP_ACCEPT);
            
            while(true){
                selector.select();
                
                Set<SelectionKey> keys = selector.selectedKeys();
                boolean sensorData = false;
                
                //sprawdź czy przyszło coś z sensora
                for (Iterator<SelectionKey> i = keys.iterator(); i.hasNext();) {
                    SelectionKey key = (SelectionKey) i.next();
                    Channel c = (Channel) key.channel();
                    
                    //czy port sensora jest otwarty do czytania?
                    if(key.isAcceptable() && c == sensorChannel){
                        sensorData = true;
                        i.remove();
                        break;
                    }
                }
                
                //przyszło coś z sensora (sensorChannel)
                if(sensorData){
                    //DO TESTOWANIA:
                    System.out.println("Sa dane z sensora!");
                    
                    //pobranie wiadomości z socketa
                    byte[] bytes = new byte[4096];
                    ByteBuffer bb = ByteBuffer.wrap(bytes);
                    SocketChannel sensorSocket = sensorChannel.accept();
                    sensorSocket.read(bb);
                    String msg = new String(bb.array());

                    //odczytać który to sensor
                    InputStream is = new StringBufferInputStream(msg);
                    Sensor sensor = XMLParser.getSensorFromXml(is);
                    
                    //przygotować dane do wysłania
                    String value = XMLParser.getValueFromXml(is);
                    String msgForClient = XMLParser.createMeasurementInfoForClient(sensor, value);
                    
                    //DO TESTOWANIA:
                    System.out.println("Wiadomosc z sensora: \n" + msg + "\n\n");
                    System.out.println("Wiadomosc dla klientow: \n" + msgForClient + "\n\n");
                    
                    //sprawdzić na liście subskrypcji kto to ma i wysłać na podane porty
                    for (Iterator<SelectionKey> i = keys.iterator(); i.hasNext();) {
                        SelectionKey key = (SelectionKey) i.next();
                        i.remove();
                        SocketChannel sc = (SocketChannel) key.channel();
                        
                        int index = clientChannels.indexOf(sc);
                    
                        //czy dany port jest otwarty do pisania i czy jest na liście klientów?
                        if(key.isWritable() && ( index != -1 ) ){
                            int port = clientChannels.get(index).socket().getLocalPort();
                            Sensor sensorZListy = subscriptions.get(new Integer(port));
                            
                            if( sensor.equals(sensorZListy) ){
                                //TAK! on ma dostać subskrybcję!
                                sc.configureBlocking(false);
                                ByteBuffer buf = ByteBuffer.allocateDirect(4096);
	                        buf.clear();
	                        buf.put(msgForClient.getBytes());
	                        buf.flip();
		                sc.write(buf);
                            }
                        }
                    }
                }
            }
            
        } catch (IOException ex) {
            //zrobić coś z wyjątkami?
            ex.printStackTrace();
        }
        
    }
    
    public void addSensor(Sensor s){
        //wystarczy jak jest w rejestrze w serwerze, nie trzeba tutaj robić dodatkowej listy, która to będzie powielać
        HTTPServer.register.put(s, this);
    }
    
    /**
     * @param s Sensor, na którym ktoś chce nasłuchiwać
     * @return port, na którym będzie mógł nasłuchiwać
     */
    public int addSubscription(Sensor s) throws IOException{
        Integer i = new Integer(subscriptionStartPort);
        //znajdź pierwszy wolny port
        while( subscriptions.containsKey(i) ){
            i++;
        }
        
        //otwarcie socketa i dodanie do selectora
        ServerSocketChannel clientChannel = ServerSocketChannel.open();
        clientChannel.socket().bind(new InetSocketAddress(i));
        clientChannel.configureBlocking(false);
        clientChannels.add(clientChannel);
        clientChannel.register(selector, SelectionKey.OP_ACCEPT);
        
        subscriptions.put(i, s);
        
        return i;
    }
    
    public void removeSubscription(int port) throws IOException{
        
        for( int i = 0; i < clientChannels.size(); i++ ){
            if( clientChannels.get(i).socket().getLocalPort() == port ){
                clientChannels.get(i).close();
                clientChannels.remove(i);
            }
        }
        subscriptions.remove(new Integer(port));
    }
    
    @Override
    public boolean equals(Object o) {
	if (!(o instanceof Monitor))
            return false;
	Monitor s = (Monitor) o;
	if ( (this.sensorPort == s.sensorPort) && (this.subscriptionStartPort == s.subscriptionStartPort) )
            return true;
	return false;
    }
    
    
}
