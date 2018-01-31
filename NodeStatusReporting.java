import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Stack;
import java.io.BufferedReader;
import java.io.FileReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.DataFormatException;

class NodeStatusReporting {
    public static void main(String[] args) {
        BufferedReader inputFile;
        String line;
        ArrayList<Notification> notifications = new ArrayList<Notification>();
        HashMap<String, Stack<Status>> statuses = new HashMap<String, Stack<Status>>();

        // Check number of arguments
        if (args.length != 1) {
            System.err.println("Usage: java NodeStatusReporting <input>");
            System.exit(1);
        }

        // Try to open input file
        try {
            inputFile = new BufferedReader(new FileReader(args[0]));

            // Read the file line-by-line
            line = inputFile.readLine();
            while (line != null) {
                // Create a Notification object for each line and add it to the list
                Notification n = new Notification(line);
                notifications.add(n);

                line = inputFile.readLine();
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: the file '" + args[0] + "' could not be found");
            System.exit(2);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(3);
        } catch (DataFormatException e) {
            System.err.println("Error: bad input file");
            System.exit(4);
        } catch (NumberFormatException e) {
            System.err.println("Error: bad input file");
            System.exit(4);
        }

        // Sort the notifications by the time they were generated
        Collections.sort(notifications);

        // Iterate through each notification and generate statuses for each
        for(Notification n : notifications) {
            // First, make a status for the node who sent the notification
            Status stat = new Status(n.getName(), n);

            // (we may have to also deal with the node who was lost or found)
            for (int i = 0; i < 2; i++) {
                // Check if there is already a notification for this node
                if (statuses.containsKey(stat.getSubject())) {
                    Stack<Status> stack = statuses.get(stat.getSubject());

                    // If the previous notification was less than 50ms before this one...
                    if (Math.abs(stat.getTimeGenerated().getTime() - stack.peek().getTimeGenerated().getTime()) <= 50) {
                        // ...then we can't be sure of the order in which they occurred. So if the
                        // statuses differ...
                        if (stat.getLiving() != stack.peek().getLiving()) {
                            // ...set both statuses to UNKNOWN...
                            stack.peek().setLiving(Living.UNKNOWN);
                            stat.setLiving(Living.UNKNOWN);
                        }
                    }
                    // ...and add the new status to the stack
                    statuses.get(stat.getSubject()).push(stat);
                } else {
                    // Add new node to the status list
                    Stack<Status> stack = new Stack<Status>();
                    stack.push(stat);
                    statuses.put(stat.getSubject(), stack);
                }

                // If the notification is not a HELLO, process the status of the friend
                if (n.getMessage() != Message.HELLO) {
                    stat = new Status(n.getFriend(), n);
                } else {
                    // If the notification was a HELLO, then we only need to produce one status
                    break;
                }
            }
        }

        // Output the statuses
        for (Stack<Status> statusStack : statuses.values()) {
            // Print only the most recent status for each node
            System.out.println(statusStack.peek());
        }
    }
}
