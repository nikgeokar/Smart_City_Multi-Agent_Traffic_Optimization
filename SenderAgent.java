package traffic_light_agent;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class SenderAgent extends Agent {
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length == 2) {
            String messageContent = (String) args[0];
            String receiverAgentName = (String) args[1];

            // Create a request message
            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
            request.setContent(messageContent);

            // Set the receiver agent
            AID receiver = new AID(receiverAgentName, AID.ISLOCALNAME);
            request.addReceiver(receiver);

            // Send the request message
            send(request);
        } else {
            System.out.println("Invalid arguments. Please provide message content and receiver agent name.");
        }
        
        doDelete();
        
    }
}