package science.yurimdias.socket;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class SocketServer {


    private ServerSocketChannel serverSocket;
    private Selector selector;

    public void start(String address, int port) {
        try {
            selector = Selector.open();
            serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(address, port));
            serverSocket.configureBlocking(false);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Server started, listening on port: " + port);
            this.work();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.shutdown();
        }
    }

    public void work() {
        ObjectMapper objectMapper = new ObjectMapper();
        ByteBuffer buffer = ByteBuffer.allocate(512);
        Random generator = new Random();
        try {
            while (true) {
                long staticMessage = System.currentTimeMillis();
                selector.select();
                Set<SelectionKey> selectorKeysActive = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectorKeysActive.iterator();
                System.out.println("Found " + selectorKeysActive.size() + " clients, sending messages...");
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    int interestOps = key.interestOps();
                    if (key.isAcceptable()) {
                        // Need to register intent to write to channel
                        SocketChannel client = serverSocket.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_WRITE);
                    }
                    if (key.isWritable()) {// Send timed message
                        SocketChannel client = (SocketChannel) key.channel();
                        SocketMessage message = new SocketMessage(staticMessage);
                        String messageString = objectMapper.writeValueAsString(message);
                        try {
                            buffer.clear();
                            client.write(buffer.wrap(messageString.getBytes()));
                        } catch (IOException e) {
                            // Can't write, connection closed midway
                            System.out.println("Broken pipe, or client disconnected.");
                            key.cancel();
                        }
                        buffer.clear();
                    }
                    iter.remove();
                }
                Thread.sleep(2000 + generator.nextInt(1000)); // Sleep 5s after each message
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        if (!serverSocket.isOpen()) return;
        try {
            serverSocket.close();
            System.out.println("Server closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
