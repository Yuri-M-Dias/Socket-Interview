package science.yurimdias.socket;

public class SocketMessage {
    public String content;
    public long time;

    /**
     * Uses standard message with the timestamp
     *
     * @param time timestamp for the message
     */
    public SocketMessage(long time) {
        this.content = "To succeed, planning alone is insufficient. One must improvise as well.";
        this.time = time;
    }

    public SocketMessage(String content, long time) {
        this.content = content;
        this.time = time;
    }
}
