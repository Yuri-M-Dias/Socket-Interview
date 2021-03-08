package science.yurimdias.socket;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SocketMessage {
    private static List<String> messages = new ArrayList<>() {{
        add("To succeed, planning alone is insufficient. One must improvise as well.");
        add("I move, therefore I am");
        add("The purpose of computing is insight, not numbers.");
        add("If someone does not understand the problem, they should not be allowed to program a solution for it.");
        add("An earnest failure has meaning.");
    }};
    public String content;
    public long time;

    /**
     * Uses standard message with the timestamp
     *
     * @param time timestamp for the message
     */
    public SocketMessage(long time) {
        Random gen = new Random();
        this.content = messages.get(gen.nextInt(messages.size()));
        //this.content = "To succeed, planning alone is insufficient. One must improvise as well.";
        this.time = time;
    }

    /**
     * Default message changed with the socket
     *
     * @param content message to be send
     * @param time    time that it was sent
     */
    public SocketMessage(String content, long time) {
        this.content = content;
        this.time = time;
    }
}
