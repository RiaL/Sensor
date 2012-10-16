package monitor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
            ServerSocketChannel sensorChannel = ServerSocketChannel.open();
            sensorChannel.socket().bind(new InetSocketAddress(sensorPort));
            sensorChannel.configureBlocking(false);
            sensorChannel.register(selector, SelectionKey.OP_READ);
            
            while(true){
                selector.select();
                
                Set<SelectionKey> keys = selector.selectedKeys();
                boolean sensorData = false;
                
                //sprawdź czy przyszło coś z sensora
                for (Iterator<SelectionKey> i = keys.iterator(); i.hasNext();) {
                    SelectionKey key = (SelectionKey) i.next();
                    Channel c = (Channel) key.channel();
                    
                    //czy port sensora jest otwarty do czytania?
                    if(key.isReadable() && c == sensorChannel){
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
                    
                    
                    //sprawdzić na liście subskrypcji kto to ma
                    //wysłać na dane porty
                    
                }
                
                /*
                
		        		if (key.isReadable() && c == udpserver) {
		        			byte[] buffer = new byte[4096];
		        			SocketAddress address = udpserver.receive(receiveBuffer);
		        			buffer = receiveBuffer.array();
		        			String msg = new String(buffer);
		        			processSensorMessage(address, msg);
		        		} else 
			        		if (key.isReadable() && c == tcpserver) {
			        			System.out.println("READ");
			        			
			        		}
		        		
		        		if (key.isAcceptable()) {
		        			SocketChannel client = tcpserver.accept();
		        			client.configureBlocking(false);
		        			
		                    if (client != null) {
		                    	processClientMessage(client, null);
		                    }
		                    
		        		}
		        		
		        		if (key.isReadable() && c != udpserver) {
		        			SocketChannel client = (SocketChannel) key.channel();

		        	        ByteBuffer buffer = ByteBuffer.allocate(4096);
		        	        int numRead = -1;
		        	        try {
		        	            numRead = client.read(buffer);
		        	        }
		        	        catch (IOException e) {
		        	            e.printStackTrace();
		        	        }

		        	        if (numRead != -1) {
			        			processClientMessage(client, buffer);
		        	        }
		        		}
		        		
		        		if (key.isWritable()) {
		                    SocketChannel client = (SocketChannel) key.channel();
		                    client.configureBlocking(false);
	                        ByteBuffer buf = ByteBuffer.allocateDirect(4096);
	                        String msg = (String)key.attachment();
	                        if (msg.equals("Close")) {
	                        	client.close();
	                        } else {
	                            buf.clear();
	                            buf.put(msg.getBytes());
	                            buf.flip();
		                        client.write(buf);
		                        
		        				key.interestOps(SelectionKey.OP_READ);
	                        }
		        		}
		        		receiveBuffer.flip();
				  } */
                
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
        clientChannel.register(selector, SelectionKey.OP_READ);
        
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
