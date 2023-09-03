package traffic_light_agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.*;

public class Vehicle911Agent extends Agent {
    private int avenue;
    private int street;
    private int priority;
    private SharedObject sharedObject;
    private int max_avenue;
    private int max_street;

    protected void setup() {
        System.out.println("911 Vehicle agent " + getAID().getName() + " is ready.");
        // Initialize the starting position and priority of the car agent 
        avenue = 1;
        street = 1;
        priority = 2;
        max_avenue = 12;
        max_street = 12;
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            sharedObject = (SharedObject) args[0];
        }
        // Add a behavior to handle incoming messages
        addBehaviour(new Vehicle911Behaviour());
    }

    private class Vehicle911Behaviour extends CyclicBehaviour {
        public void action() {
            // Listen for incoming messages
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            ACLMessage msg = receive(mt);

            if (msg != null) {
                // Process the request message
                String content = msg.getContent();
                
                System.out.println("911 Vehicle agent" + getAID().getName() + "  received message: " + content);

                if (content.equals("MOVE_NORTH")) {
                    // Move the car agent to the north
                    moveNorth();
                } else if (content.equals("MOVE_SOUTH")) {
                    // Move the car agent to the south
                    moveSouth();
                } else if (content.equals("MOVE_EAST")) {
                    // Move the car agent to the east
                    moveEast();
                } else if (content.equals("MOVE_WEST")) {
                    // Move the car agent to the west
                    moveWest();
                } else if (content.equals("QUERY")) {
                    // Send the current location as a response
                    sendLocation();
                }				
                else if (content.equals("SEND_AVENUE")) {
                    // Send the current location as a response
                    sendAvenue();
                }
                else if (content.equals("SEND_STREET")) {
                    // Send the current location as a response
                    sendStreet();
                }								
                else if  (content.startsWith("MOVE_TO")) {
                    // Extract the coordinates from the message
                    String[] coordinates = content.split(" ");
                    if (coordinates.length == 3) {
                        int targetAvenue = Integer.parseInt(coordinates[1]);
                        int targetStreet = Integer.parseInt(coordinates[2]);
                        // Move the car agent to the target coordinates
                        moveTo(targetAvenue, targetStreet);
                    }
                    else if (coordinates.length == 4) {
                        System.out.println("4");
                    }
                }else if  (content.startsWith("SEND_PRIORITY")) {
                    // Send priority to TrafficLightAgent                    
                    msg.addReceiver(getVehicle911AgentAID());
                    msg.setContent(Integer.toString(priority));
                    send(msg);
                    System.out.println("911 Vehicle agent " + getAID().getName() + " sent priority: " + priority);
                }
                block();
            }
        }

        private void moveNorth() {
            if (avenue > 1) {
                avenue--;
                System.out.println("911 Vehicle agent  " + getAID().getName() + " moved north to position (" + avenue + ", " + street + ")");
                sharedObject.setAvenue(Integer.toString(avenue));
                sharedObject.setStreet(Integer.toString(street));				
            } else {
                System.out.println("911 Vehicle agent  " + getAID().getName() + " cannot move north. Already at the northernmost position.");
                sharedObject.setAvenue(Integer.toString(avenue));
                sharedObject.setStreet(Integer.toString(street));
            }
        }

        private void moveSouth() {
            if (avenue < max_avenue) {
                avenue++;
                System.out.println("911 Vehicle agent  " + getAID().getName() + " moved south to position (" + avenue + ", " + street + ")");
                sharedObject.setAvenue(Integer.toString(avenue));
                sharedObject.setStreet(Integer.toString(street));			
            }else {
                System.out.println("911 Vehicle agent  " + getAID().getName() + " cannot move south. Already at the southernmost position.");
                sharedObject.setAvenue(Integer.toString(avenue));
                sharedObject.setStreet(Integer.toString(street));
                }
        }

        private void moveWest() {
            if (street > 1) {
                street--;
                System.out.println("911 Vehicle agent  " + getAID().getName() + " moved west to position (" + avenue + ", " + street + ")");
                sharedObject.setAvenue(Integer.toString(avenue));
                sharedObject.setStreet(Integer.toString(street));				
            } else {
                System.out.println("911 Vehicle agent  " + getAID().getName() + " cannot move west. Already at the westernmost position.");
                sharedObject.setAvenue(Integer.toString(avenue));
                sharedObject.setStreet(Integer.toString(street));
            }
        }
        
        private void moveEast() {
            if (street < max_street) {
                street++;
                System.out.println("911 Vehicle agent  " + getAID().getName() + " moved east to position (" + avenue + ", " + street + ")");
                sharedObject.setAvenue(Integer.toString(avenue));
                sharedObject.setStreet(Integer.toString(street));		
            } else {
                System.out.println("911 Vehicle agent  " + getAID().getName() + " cannot move east. Already at the easternmost position.");
                sharedObject.setAvenue(Integer.toString(avenue));
                sharedObject.setStreet(Integer.toString(street));
            }
        }
        
        private void sendAvenue() {
            ACLMessage positionMsg = new ACLMessage(ACLMessage.INFORM);
            positionMsg.setContent(Integer.toString(avenue));
            positionMsg.addReceiver(getComAID());
            send(positionMsg);
        }
        
        private void sendStreet() {
            ACLMessage positionMsg = new ACLMessage(ACLMessage.INFORM);
            positionMsg.setContent(Integer.toString(street));
            positionMsg.addReceiver(getComAID());
            send(positionMsg);
        }
        
        private void sendLocation() {
            ACLMessage positionMsg = new ACLMessage(ACLMessage.INFORM);
            positionMsg.setContent("Position: (" + avenue + ", " + street + ")");
            positionMsg.addReceiver(getComAID());
            send(positionMsg);
        }
        
        private void moveTo(int targetAvenue, int targetStreet) {
            List<Position> shortestPath = new ArrayList<>();
		
            if (isValidPosition(targetAvenue, targetStreet)) {
                // Find the shortest path using Dijkstra's algorithm
                shortestPath = findShortestPath(avenue, street, targetAvenue, targetStreet);

                if (shortestPath != null) {
                    // Move the car agent along the shortest path
                    for (Position position : shortestPath) {
                        avenue = position.getAvenue();
                        street = position.getStreet();
                        System.out.println("911 Vehicle agent  " + getAID().getName() + " moved to position (" + avenue + ", " + street + ")");
                        
                        sharedObject.setAvenueList(Integer.toString(position.getAvenue()));
                        sharedObject.setStreetList(Integer.toString(position.getStreet()));
                        					
                        // Pause for a moment to visualize the movement
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("911 Vehicle agent  " + getAID().getName() + " could not find a path to position (" + targetAvenue + ", " + targetStreet + ")");
                    sharedObject.setAvenueList(Integer.toString(targetAvenue));
                    sharedObject.setStreetList(Integer.toString(targetStreet));
                }
            } else {
                System.out.println("911 Vehicle agent  " + getAID().getName() + " cannot move to invalid position (" + targetAvenue + ", " + targetStreet + ")");
                sharedObject.setAvenueList(Integer.toString(targetAvenue));
                sharedObject.setStreetList(Integer.toString(targetStreet));
            }
        }

        private boolean isValidPosition(int avenue, int street) {
            return avenue >= 1 && avenue <= max_avenue && street >= 1 && street <= max_street;
        }
        
        private List<Position> findShortestPath(int startAvenue, int startStreet, int targetAvenue, int targetStreet) {
            // Create a grid with the distances initialized to infinity
            double[][] distances = new double[max_avenue + 1][max_street + 1];
            for (double[] row : distances) {
                Arrays.fill(row, Double.POSITIVE_INFINITY);
            }

            // Create a grid to track the previous positions in the shortest path
            Position[][] previousPositions = new Position[max_avenue + 1][max_street + 1];

            // Create a set to store the unvisited positions
            Set<Position> unvisited = new HashSet<>();
            for (int i = 1; i <= max_avenue; i++) {
                for (int j = 1; j <= max_street; j++) {
                    unvisited.add(new Position(i, j));
                }
            }

            // Set the distance of the start position to 0
            distances[startAvenue][startStreet] = 0;

            // Iterate until all positions are visited
            while (!unvisited.isEmpty()) {
                // Find the position with the minimum distance
                Position current = null;
                double minDistance = Double.POSITIVE_INFINITY;
                for (Position position : unvisited) {
                    double distance = distances[position.getAvenue()][position.getStreet()];
                    if (distance < minDistance) {
                        current = position;
                        minDistance = distance;
                    }
                }

                // Stop if the target position is reached
                if (current.getAvenue() == targetAvenue && current.getStreet() == targetStreet) {
                    break;
                }

                // Remove the current position from the unvisited set
                unvisited.remove(current);

                // Explore the neighbors of the current position (north, south, east, west)
                int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
                for (int[] direction : directions) {
                    int neighborAvenue = current.getAvenue() + direction[0];
                    int neighborStreet = current.getStreet() + direction[1];

                    if (isValidPosition(neighborAvenue, neighborStreet)) {
                        Position neighbor = new Position(neighborAvenue, neighborStreet);
                        if (unvisited.contains(neighbor)) {
                            double distance = distances[current.getAvenue()][current.getStreet()] + 1;
                            if (distance < distances[neighbor.getAvenue()][neighbor.getStreet()]) {
                                distances[neighbor.getAvenue()][neighbor.getStreet()] = distance;
                                previousPositions[neighbor.getAvenue()][neighbor.getStreet()] = current;
                            }
                        }
                    }
                }
            }

            // Build the shortest path by following the previous positions from the target to the start
            List<Position> shortestPath = new ArrayList<>();
            Position current = new Position(targetAvenue, targetStreet);
            while (current != null) {
                shortestPath.add(current);
                current = previousPositions[current.getAvenue()][current.getStreet()];
            }
            Collections.reverse(shortestPath);

            // Return the shortest path
            return shortestPath;
        }
   
        private AID getVehicle911AgentAID() {
            // Specify the AID of the 911 Vehicle agent
            return new AID("911 Vehicle agent", AID.ISLOCALNAME);
         } 
       
        private AID getComAID() {
            // Assuming the main program agent has the local name "Com"
            return new AID("Com", AID.ISLOCALNAME);
        }
    
    }
}