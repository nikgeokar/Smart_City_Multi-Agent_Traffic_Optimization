package traffic_light_agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class Traffic_light_agent extends Agent {
    private static final long serialVersionUID = 1L;

    private enum State {
        RED, GREEN
    }

    private State currentState;
    private String[] sides = {"North", "East", "South", "West"};
    private int currentGreenIndex;
    private String currentGreen;
    private SharedObject sharedObject;

    protected void setup() {
        System.out.println("Traffic light agent " + getAID().getName() + " is ready.");

        currentGreenIndex = 0;
        currentState = State.RED;

        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            sharedObject = (SharedObject) args[0];
        }
        
        // Add a behavior to control the traffic light
        addBehaviour(new TickerBehaviour(this, 500) { 
            private static final long serialVersionUID = 1L;

            protected void onTick() {
                for (int i = 0; i < sides.length; i++) {
                    if (i == currentGreenIndex || i == (currentGreenIndex + 2) % sides.length) {
                        sendTrafficLightStatus(sides[i], "GREEN");
                    } else {
                        sendTrafficLightStatus(sides[i], "RED");
                    }
                }
                
                switch (currentGreenIndex) {
                    case 0:
                        currentGreen = "North-South";
                        break;
                    case 1:
                        currentGreen = "East-West";
                        break;
                    default:
                        break;
                }
                
                currentGreenIndex = (currentGreenIndex + 1) % sides.length;
            }
        });

        // Add a behavior to handle incoming messages
        addBehaviour(new CyclicBehaviour(this) {
            private static final long serialVersionUID = 1L;

            public void action() {
                ACLMessage msg = receive();

                if (msg != null) {
                    String content = msg.getContent();
                    
                    if (content.equals("Car")) {
                            sharedObject.setGreen(currentGreen);
                    }
                    else if (content.startsWith("911")) {
                        // Extract the coordinates from the message
                        String[] coordinates = content.split(" ");
                        if (coordinates.length == 2) {                            
                            if ("North-South".equals(coordinates[1])){
                                sharedObject.setGreen("North-South");
                            }else if ("East-West".equals(coordinates[1])){
                                currentGreenIndex = 1;
                                sharedObject.setGreen("East-West");
                            }
                        }
                    }
                    
                } else {
                    block();
                }
            }
        });
    }

    protected void takeDown() {
        System.out.println("Traffic light agent " + getAID().getName() + " is terminating.");
    }
    
    private void sendTrafficLightStatus(String side, String color) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("TrafficLight", AID.ISLOCALNAME));
        msg.setContent(side + " " + color);
        send(msg);
    }
}