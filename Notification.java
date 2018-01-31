import java.util.Date;
import java.util.zip.DataFormatException;

/**
 * The message that was sent in the notification.
 */
enum Message { HELLO, LOST, FOUND }

/**
 * Represents a notification that was received from a node.
 */
public class Notification implements Comparable<Notification> {
    private Date timeReceived;
    private Date timeGenerated;
    private String name;
    private Message msg;
    private String friend;
    
    public Notification() {
        timeReceived = new Date();
        timeGenerated = new Date();
        name = new String();
    }

    /**
     * Creates a new Notification object from the status line that was sent by the node.
     * @param line the notification text
    */
    public Notification(String line) throws DataFormatException {
        this();
        
        String[] fields = line.split("\\s+");
        if (fields.length != 4 && fields.length != 5) {
            throw new DataFormatException("Too many fields given");
        }
        timeReceived.setTime(Long.parseLong(fields[0], 10));
        timeGenerated.setTime(Long.parseLong(fields[1], 10));
        name = fields[2];
        switch(fields[3]) {
            case "HELLO":
                msg = Message.HELLO;
                if (fields.length != 4) {
                    throw new DataFormatException("Wrong number of fields given");
                }
                break;
            case "LOST":
                msg = Message.LOST;
                if (fields.length != 5) {
                    throw new DataFormatException("Wrong number of fields given");
                }
                friend = new String(fields[4]);
                break;
            case "FOUND":
                msg = Message.FOUND;
                if (fields.length != 5) {
                    throw new DataFormatException("Wrong number of fields given");
                }
                friend = new String(fields[4]);
                break;
            default:
                throw new DataFormatException("Unknown message: " + fields[3]);
        }
    }

    public String getName() {
        return this.name;
    }

    public Message getMessage() {
        return this.msg;
    }

    public String getFriend() {
        return this.friend;
    }
    
    public String toString() {
        String s = "Notification\n------------";
        s += "\nTime generated: " + timeGenerated;
        s += "\nTime received : " + timeReceived;
        s += "\nNode name: " + name;
        s += "\nMessage: ";
        switch(this.msg) {
            case HELLO:
                s += "hello";
                break;
            case LOST:
                s += "lost " + friend;
                break;
            case FOUND:
                s += "found " + friend;
                break;
        }
        s += "\n";
        
        return s;
    }

    public String toSimpleString() {
        String s = new String();
        s += timeReceived.getTime();
        s += " " + name;
        switch (this.msg) {
            case HELLO:
                s += " HELLO";
                break;
            case LOST:
                s += " LOST " + friend;
                break;
            case FOUND:
                s += " FOUND " + friend;
                break;
        }
        return s;
    }

    public Date getTimeGenerated() {
        return this.timeGenerated;
    }

    @Override
    /**
     * Notifications are sorted chronologically by the time that they were generated.
     */
    public int compareTo(Notification n) {
        return timeGenerated.compareTo(n.timeGenerated);
    }
}
