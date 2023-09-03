package traffic_light_agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.*;

public class CommandSender extends Agent {
        String Avenue_hospital;
        String Street_hospital;
        String Avenue_police_station;
        String Street_police_station;
        String Avenue_fire_station;
        String Street_fire_station;
        
    protected void setup() {
        System.out.println("CommandSender " + getAID().getName() + " is ready.");
        Avenue_hospital = "10";
        Street_hospital = "11";
        Avenue_police_station = "6";
        Street_police_station = "7";
        Avenue_fire_station = "3";
        Street_fire_station = "4";
        
        // Add a behavior to handle incoming messages
        addBehaviour(new CommandSenderBehaviour());
    }

    private class CommandSenderBehaviour extends CyclicBehaviour {
        public void action() {
            // Listen for incoming messages
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            ACLMessage msg = receive(mt);

            if (msg != null) {
                // Process the request message
                String content = msg.getContent();
                
                System.out.println("Command Sender agent " + getAID().getName() + " received message: " + content);

                if  (content.startsWith("CALL_AMBULANCE")) {
                    // Extract the coordinates from the message
                    String[] coordinates = content.split(" ");
                    if (coordinates.length == 2) {                        
                        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                        request.setContent("MOVE_TO " + Avenue_hospital + " " + Street_hospital);
                        // Set the receiver agent
                        request.addReceiver( new AID("Ambulance", AID.ISLOCALNAME));
                        // Send the request message
                        send(request);               
                    } 
                    else if (coordinates.length == 3) {             
                        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                        request.setContent("MOVE_TO " + coordinates[1] + " " + coordinates[2] );
                        // Set the receiver agent
                        request.addReceiver( new AID("Ambulance", AID.ISLOCALNAME));
                        // Send the request message
                        send(request);  
                    }
                }else if  (content.startsWith("CALL_POLICE")) {
                        // Extract the coordinates from the message
                        String[] coordinates = content.split(" ");
                        if (coordinates.length == 2) {                        
                            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                            request.setContent("MOVE_TO " + Avenue_police_station + " " + Street_police_station);
                            // Set the receiver agent
                            request.addReceiver( new AID("Police", AID.ISLOCALNAME));
                            // Send the request message
                            send(request);               
                        } 
                        else if (coordinates.length == 3) {
                            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                            request.setContent("MOVE_TO " + coordinates[1] + " " + coordinates[2] );
                            // Set the receiver agent
                            request.addReceiver( new AID("Police", AID.ISLOCALNAME));
                            // Send the request message
                            send(request);    
                        }
                }else if  (content.startsWith("CALL_FIRE")) {
                        // Extract the coordinates from the message
                        String[] coordinates = content.split(" ");
                        if (coordinates.length == 2) {                        
                            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                            request.setContent("MOVE_TO " + Avenue_fire_station + " " + Street_fire_station);
                            // Set the receiver agent
                            request.addReceiver( new AID("Fire", AID.ISLOCALNAME));
                            // Send the request message
                            send(request);               
                        } 
                        else if (coordinates.length == 3) {
                            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                            request.setContent("MOVE_TO " + coordinates[1] + " " + coordinates[2] );
                            // Set the receiver agent
                            request.addReceiver( new AID("Fire", AID.ISLOCALNAME));
                            // Send the request message
                            send(request);    
                        }
                }else if  (content.startsWith("CALL_EMERGENCY")) {
                        // Extract the coordinates from the message
                        String[] coordinates = content.split(" ");
                        if (coordinates.length == 3) {                        
                            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                            request.setContent("MOVE_TO 10 11");
                            // Set the receiver agent
                            request.addReceiver( new AID(coordinates[2], AID.ISLOCALNAME));
                            // Send the request message
                            send(request);               
                        } 
                }
                block();
            }
        }

        
    }
}
