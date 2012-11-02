package monitor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Krzysztof Kutt
 */
public class Monitor implements Runnable {
    private static Map<Sensor, String> sensorsValues;
    private int clientPortNumber;
    private int sensorPortNumber;
    private ByteBuffer buffer;
    private ByteBuffer bufferWithValue;
    private Selector selector;
    private ServerSocketChannel clientChannel;
    private ServerSocketChannel sensorChannel;
    public static Map<SocketChannel, Sensor> subscriptionsMap = new HashMap<SocketChannel, Sensor>();

    public Monitor(int clientPortNumber, int sensorPortNumber) {
        sensorsValues = new HashMap<Sensor, String>();
        buffer = ByteBuffer.allocate(Config.MAX_BUFFER_CAPACITY);
        bufferWithValue = ByteBuffer.allocate(Config.MAX_BUFFER_CAPACITY);
        this.clientPortNumber = clientPortNumber;
        this.sensorPortNumber = sensorPortNumber;
    }
    
    @Override
    public void run() {

        try {
            SocketAddress socketClientAddress = new InetSocketAddress(clientPortNumber);
            clientChannel = ServerSocketChannel.open();

            clientChannel.configureBlocking(false);
            clientChannel.socket().bind(socketClientAddress);

            SocketAddress socketSensorAddress = new InetSocketAddress(sensorPortNumber);
            sensorChannel = ServerSocketChannel.open();
            sensorChannel.configureBlocking(false);
            sensorChannel.socket().bind(socketSensorAddress);

            this.selector = Selector.open();
            clientChannel.register(this.selector, SelectionKey.OP_ACCEPT, "Client");
            sensorChannel.register(this.selector, SelectionKey.OP_ACCEPT, "Sensor");

            while (true) {
                this.selector.select();

                Set<SelectionKey> selectedKeysSet = this.selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeysSet.iterator();

                // iterate through all SelectionKeys
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();
                    
                    if (!key.isValid()) {
                        continue;
                    }

                    if (key.isAcceptable()) {
                        this.accept(key);
                    } else if (key.isWritable()) {
                        writeValueToClient(key);
                    } else if (key.isReadable()) {
                        if (key.attachment().equals("Client")) {
                            newSubscription(key);
                        } else if (key.attachment().equals("Sensor")) {
                            newSensorData(key);
                        }
                    }
                }
                
                Thread.sleep(Config.TIME_INTERVAL);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /** accept Channel and register READ operation */
    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        System.out.println("Accepted connection from " + socketChannel);
        socketChannel.configureBlocking(false);
        socketChannel.register(this.selector, SelectionKey.OP_READ, key.attachment());
    }

    private void writeValueToClient(SelectionKey key) throws IOException, InterruptedException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        Sensor sensorData = (Sensor) key.attachment();
        
        String sensorValue = getLatestValueFromSensor(sensorData);
        String message = XMLParser.createMeasurementInfoForClient(sensorData, sensorValue);
        bufferWithValue.clear();
        bufferWithValue.put(message.getBytes());
        bufferWithValue.flip();

        if (!socketChannel.socket().isClosed()) {
            socketChannel.write(bufferWithValue);
        }
    }

    private void newSubscription(SelectionKey key) throws IOException {
        //brak obsługi przypadku gdy klient poda zły numer subskrypcji - zakładamy, że zawsze dobry
        
        SocketChannel socketChannel = (SocketChannel) key.channel();
        String text = readFromChannel(key);

        String[] userChoice = text.split("-");
        Integer i = Integer.parseInt(userChoice[0]);
        Sensor sensorData = new Sensor(userChoice[1], userChoice[2]);
        
        //add subscription and register in selector
        addSubscription(socketChannel, sensorData);
        socketChannel.register(this.selector, SelectionKey.OP_WRITE, sensorData);
        
        //add socketChannel to subscription in register in HTTPServer
        Subscription sub = HTTPServer.subscriptions.get(i);
        HTTPServer.subscriptions.remove(i);
        sub.setChannel(socketChannel);
        HTTPServer.subscriptions.put(i,sub);
    }

    private static void addSubscription(SocketChannel socketChannel, Sensor sensorData) throws IOException {
        subscriptionsMap.put(socketChannel, sensorData);
    }

    private void newSensorData(SelectionKey key) throws IOException {
        String text = readFromChannel(key);
        String[] fromSensor = text.split(":");
        Sensor sensorData = new Sensor(fromSensor[0], fromSensor[1]);
        String value = fromSensor[2];
        value = value.trim();
        System.out.println("*INFO* New entry in sensorsMap. Key: " + sensorData + " Value: " + value);
        sensorsValues.put(sensorData, value);
        System.out.println("*INFO* SensorsMap size is now: " + sensorsValues.size());
    }
    
    private String readFromChannel(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        byte[] bytes = new byte[Config.MAX_BUFFER_CAPACITY];
        this.buffer = ByteBuffer.wrap(bytes);
        this.buffer.clear();
        int numRead;
        String text;
        try {
            numRead = socketChannel.read(this.buffer);
            text = new String(buffer.array());
        } catch (IOException e) {
            key.cancel();
            socketChannel.close();
            return null;
        }

        if (numRead == -1) {
            key.channel().close();
            key.cancel();
            return null;
        }
        return text;
    }

    private String getLatestValueFromSensor(Sensor sensorData) {
        String valueForMetric = null;

        if (sensorsValues.containsKey(sensorData)) {
            valueForMetric = sensorsValues.get(sensorData);
            System.out.println("*INFO* Retrieved value from sensorMap for key is " + valueForMetric);
        } else {
            valueForMetric = "There is no subscription for this host and metrics.";
            System.out.println("*INFO* No value retrieved.");
        }
        return valueForMetric;
    }

    public int getClientPortNumber() {
        return clientPortNumber;
    }
    
    public void removeSubscription(SocketChannel removeSocketChannel) throws IOException {
        Set<SelectionKey> keysSet = selector.keys();
        Iterator<SelectionKey> keyIterator = keysSet.iterator();

        // iterate through all SelectionKeys
        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();
            keyIterator.remove();
            SocketChannel socketChannel = (SocketChannel) key.channel();
            
            if (socketChannel.equals(removeSocketChannel)) {
                //close connection
                socketChannel.socket().close();
                
                //remove from subscriptionsMap
                subscriptionsMap.remove(socketChannel);
                return;
            }
        }
    }
    
    @Override
    public boolean equals(Object o) {
	if (!(o instanceof Monitor))
            return false;
	Monitor s = (Monitor) o;
	if ( (this.sensorPortNumber == s.sensorPortNumber) && (this.clientPortNumber == s.clientPortNumber) )
            return true;
	return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + this.clientPortNumber;
        hash = 53 * hash + this.sensorPortNumber;
        return hash;
    }
    
}
