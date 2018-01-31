import java.util.Date;

enum Living {ALIVE, DEAD, UNKNOWN}

/**
 * Represents the status of a node (whether it is alive, dead, or unknown at a certain time).
 */
public class Status {
    private String subject;
    private Living living;
    private Notification notif;

    /**
     * Creates a new status object for the node 'name', based on the provided notification.
     * @param name the subject of the status
     * @param n the notification to use to get the status
     */
    public Status(String name, Notification n) {
        this.subject = name;
        this.notif = n;

        if (this.subject == this.notif.getName()) {
            // If the subject sent the notification, then they are alive
            this.living = Living.ALIVE;
        } else if (this.subject == this.notif.getFriend()) {
            // If the notification is about the subject, determine their status
            switch(notif.getMessage()) {
                case LOST:
                    this.living = Living.DEAD;
                    break;
                case FOUND:
                    this.living = Living.ALIVE;
                    break;
                default:
                    this.living = Living.UNKNOWN;
            }
        }
    }

    public String getSubject() {
        return this.subject;
    }

    public Living getLiving() {
        return this.living;
    }

    public Date getTimeGenerated() {
        return notif.getTimeGenerated();
    }

    public void setLiving(Living l) {
        this.living = l;
    }

    /**
     * A string representation of the status.
     */
    public String toString() {
        String s = new String(subject);
        switch (this.living) {
            case ALIVE:
                s += " ALIVE ";
                break;
            case DEAD:
                s += " DEAD ";
                break;
            default:
                s += " UNKNOWN ";
                break;
        }
        s += notif.toSimpleString();
        return s;
    }
}
