package traffic_light_agent;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.security.SecureRandom;

public class Main_Container {  
    public static void main(String[] args) {
        
        Profile profile = new ProfileImpl(false);
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.GUI, "true");
        
        Runtime runtime = Runtime.instance();
        AgentContainer container = runtime.createMainContainer(profile);
        
        SharedObject sharedObjectTF = new SharedObject();
        SharedObject sharedObjectC1 = new SharedObject();
        SharedObject sharedObjectC2 = new SharedObject();
        SharedObject sharedObjectC2_EM = new SharedObject();
        SharedObject sharedObjectC3 = new SharedObject();
        SharedObject sharedObjectA = new SharedObject();
        SharedObject sharedObjectF = new SharedObject();
        SharedObject sharedObjectP = new SharedObject();
       SharedObject sharedObjectCom1 = new SharedObject();
        
       SecureRandom random = new SecureRandom();
       
        int numAvenues = 12;
        int numStreets = 12;
        boolean Fire_exist = false;
        boolean Robery_exist = false;
        boolean Car2_emergency = false;
        boolean Car2_hospital = false;
        boolean Police_back = false;
        int step = 0;
        
        // Fixed Inicialization
        //int Fire_step = 10;
        //int Robery_step = 16;
        //int Car2_emergency_step = 26;
        //String Avenue_fire = "8";
        //String Street_fire = "4";
        //String Avenue_robery = "2";
        //String Street_robery = "11";
     
       // Random Inicialization
        int Fire_step = random.nextInt(9) + 1;
        int Robery_step = random.nextInt(9) + 11;
        int Car2_emergency_step = 26;
        String Avenue_fire = Integer.toString( random.nextInt(numAvenues -1) + 1 );
        String Street_fire = Integer.toString( random.nextInt(numStreets -1) + 1 );
        String Avenue_robery =Integer.toString( random.nextInt(numAvenues -1) + 1 );
        String Street_robery =Integer.toString( random.nextInt(numStreets -1) + 1 );
     
        System.out.println("Fire will start at step: " +Integer.toString(Fire_step) + " (" + Avenue_fire + ", " + Street_fire + ")");
        System.out.println("Robery will start at step: " +Integer.toString(Robery_step) + " (" + Avenue_robery + ", " + Street_robery + ")");
     
        String Avenue_hospital = "10";
        String Street_hospital = "11";
        String Avenue_police_station = "6";
        String Street_police_station = "7";
        String Avenue_fire_station = "3";
        String Street_fire_station = "4";
        
        String randomMove = "";
        String direction = "";
		
        String Avenue_C1 = "0";
        String Street_C1 = "0";
        String Avenue_C2 = "0";
        String Street_C2 = "0";   
        String Avenue_Ambulance = "0";
        String Street_Ambulance = "0";
        List<String> AvenueList_Ambulance;
        List<String> StreetList_Ambulance;
        String Avenue_C3 = "0";
        String Street_C3 = "0";
        List<String> AvenueList_C3;
        List<String> StreetList_C3;
        String Avenue_Fire = "0";
        String Street_Fire = "0";
        List<String> AvenueList_Fire;
        List<String> StreetList_Fire;
        String Avenue_Police = "0";
        String Street_Police = "0";
        List<String> AvenueList_Police;
        List<String> StreetList_Police;
        List<String> AvenueList_C2_Emergency;
        List<String> StreetList_C2_Emergency;
        List<String> AvenueList_C2_Emergency_Go = Arrays.asList(new String[10]);
        List<String> StreetList_C2_Emergency_Go = Arrays.asList(new String[10]);
        List<List<String>> TrafficLightList = new ArrayList<>();             
        List<AgentController> trafficLightControllers = new ArrayList<>();
        List<String> avenueTrafficLights = new ArrayList<>();
        
        int Index;
        int Tf_index;
        String DirectionC1;
        String DirectionC2;
        String DirectionC3;
        String DirectionA;
        String DirectionF;
        String DirectionP;
        String[] Random_parts;
        
        List<Integer> carAvenues = new ArrayList<>();
        List<Integer> carStreets = new ArrayList<>();
        List<String> carLists = new ArrayList<>();     
        
        
        try {        
            
            // Command Sender
            AgentController com11Controller = container.createNewAgent("Com", CommandSender.class.getName(), new Object[]{sharedObjectCom1});
            com11Controller.start();
          
            // Car 1
            AgentController agent1Controller = container.createNewAgent("Car1", CarAgent.class.getName(), new Object[]{sharedObjectC1});
            agent1Controller.start();
            container.createNewAgent("senderc1", SenderAgent.class.getName(), new Object[]{"MOVE_TO 8 8", "Car1"}).start();
            Sleep(250);
            List<String> AvenueList_Car1 = sharedObjectC1.getAvenueList();
            List<String> StreetList_Car1 = sharedObjectC1.getStreetList();
            AvenueList_Car1.clear();
            StreetList_Car1.clear();
            Sleep(10);
           
            //Car 2
            AgentController agent2Controller = container.createNewAgent("Car2", CarAgent.class.getName(), new Object[]{sharedObjectC2});
            agent2Controller.start();
            container.createNewAgent("senderc2", SenderAgent.class.getName(), new Object[]{"MOVE_TO 2 8", "Car2"}).start();
            Sleep(250);
            List<String> AvenueList_Car2 = sharedObjectC2.getAvenueList();
            List<String> StreetList_Car2 = sharedObjectC2.getStreetList();
            AvenueList_Car2.clear();
            StreetList_Car2.clear();
            Sleep(10);
            
            AgentController Agent2EmergencyController = container.createNewAgent("C2_Emergency", Vehicle911Agent.class.getName(), new Object[]{sharedObjectC2_EM});
            Agent2EmergencyController.start();
            Sleep(10);
            
             //Car 3          
            AgentController agent3Controller = container.createNewAgent("Car3", CarAgent.class.getName(), new Object[]{sharedObjectC3});
            agent3Controller.start();
            container.createNewAgent("senderc3", SenderAgent.class.getName(), new Object[]{"MOVE_TO 6 1", "Car3"}).start();
            Sleep(250);
            List<String> AvenueList_Car3 = sharedObjectC3.getAvenueList();
            List<String> StreetList_Car3 = sharedObjectC3.getStreetList();
            AvenueList_Car3.clear();
            StreetList_Car3.clear();
            Sleep(10);
                  
            // Ambulance
            AgentController AmbulanceController = container.createNewAgent("Ambulance", Vehicle911Agent.class.getName(), new Object[]{sharedObjectA});
            AmbulanceController.start();            
            container.createNewAgent("sendera4", SenderAgent.class.getName(), new Object[]{"CALL_AMBULANCE 8 3", "Com"}).start();
            Sleep(250);
            List<String> AvenueList_Ambulance_init = sharedObjectA.getAvenueList();
            List<String> StreetList_Ambulance_init = sharedObjectA.getStreetList();
            AvenueList_Ambulance_init.clear();
            StreetList_Ambulance_init.clear();
            Sleep(10);
            container.createNewAgent("sendera5", SenderAgent.class.getName(), new Object[]{"CALL_AMBULANCE HP", "Com"}).start();
            Sleep(250);
            List<String> AvenueList_Ambulance_Go = new ArrayList<>(sharedObjectA.getAvenueList());
            List<String> StreetList_Ambulance_Go = new ArrayList<>(sharedObjectA.getStreetList());
            Sleep(10);
            container.createNewAgent("sendera6", SenderAgent.class.getName(), new Object[]{"CALL_AMBULANCE 8 3", "Com"}).start();
            Sleep(250);  
           
            //Fire truck
            AgentController FireController = container.createNewAgent("Fire", Vehicle911Agent.class.getName(), new Object[]{sharedObjectF});
            FireController.start();
            container.createNewAgent("senderf1", SenderAgent.class.getName(), new Object[]{"CALL_FIRE FD", "Com"}).start();
            Sleep(250);
            List<String> AvenueList_Fire_init = sharedObjectF.getAvenueList();
            List<String> StreetList_Fire_init = sharedObjectF.getStreetList();
            AvenueList_Fire_init.clear();
            StreetList_Fire_init.clear();
            Sleep(10);
            container.createNewAgent("senderf2", SenderAgent.class.getName(), new Object[]{"CALL_FIRE " + Avenue_fire + " " + Street_fire, "Com"}).start();
            Sleep(250);
            container.createNewAgent("senderf3", SenderAgent.class.getName(), new Object[]{"CALL_FIRE FD", "Com"}).start();
            Sleep(250);
            List<String> AvenueList_Fire_Go = new ArrayList<>(sharedObjectF.getAvenueList());
            List<String> StreetList_Fire_Go = new ArrayList<>(sharedObjectF.getStreetList());
            Sleep(10);
            
            //Police 
            AgentController PoliceController = container.createNewAgent("Police", Vehicle911Agent.class.getName(), new Object[]{sharedObjectP});
            PoliceController.start();
            container.createNewAgent("senderp1", SenderAgent.class.getName(), new Object[]{"CALL_POLICE PD", "Com"}).start();
            Sleep(250);
            List<String> AvenueList_Police_init = sharedObjectP.getAvenueList();
            List<String> StreetList_Police_init = sharedObjectP.getStreetList();
            AvenueList_Police_init.clear();
            StreetList_Police_init.clear();
            Sleep(10);
            container.createNewAgent("senderp2", SenderAgent.class.getName(), new Object[]{"CALL_POLICE " + Avenue_robery + " " + Street_robery, "Com"}).start();
            Sleep(250);
            container.createNewAgent("senderp3", SenderAgent.class.getName(), new Object[]{"CALL_POLICE PD", "Com"}).start();
            Sleep(250);
            List<String> AvenueList_Police_Go = new ArrayList<>(sharedObjectP.getAvenueList());
            List<String> StreetList_Police_Go = new ArrayList<>(sharedObjectP.getStreetList());
            Sleep(10);
            
            // Traffic Lights
            for (int i = 1; i <= numAvenues*numStreets; i++) {
               AgentController trafficLightController = container.createNewAgent("TrafficLight" + i, Traffic_light_agent.class.getName(), new Object[]{sharedObjectTF});
               trafficLightController.start();
               trafficLightControllers.add(trafficLightController);
               Sleep(10);
            }
           
            Sleep(250);
            
            //SCENARIO EXECUTION
            outerLoop:
            while (Car2_hospital == false && Police_back == false ) {
                for (int i = 0; i < 60; i++) {
                    
                    carAvenues.clear();
                    carStreets.clear();
                    carLists.clear();
                    
                    carStreets.add(Integer.parseInt(Street_fire_station));
                    carAvenues.add(Integer.parseInt(Avenue_fire_station));
                    carLists.add("FD");
                  
                    carStreets.add(Integer.parseInt(Street_police_station));
                    carAvenues.add(Integer.parseInt(Avenue_police_station));
                    carLists.add("PD");
                    
                    carStreets.add(Integer.parseInt(Street_hospital));
                    carAvenues.add(Integer.parseInt(Avenue_hospital));
                    carLists.add("HP");
                    
                    int h = i;
                    // init set up
                    Sleep(10);
                    step = step + 1;
                    System.out.println("STEP: " + step);
                    if (step == Fire_step) {Fire_exist = true; System.out.println("A fire has just started ");}
                    if (step == Robery_step) {Robery_exist = true; System.out.println("A robery has just started ");}
                    if (step == Car2_emergency_step) {Car2_emergency = true; System.out.println("Car 2 is in emergency mode and will be headed to the hospital");}
                    
                    //Trafic lights
                    avenueTrafficLights.clear();
                    Sleep(10);
                    for (int j = 1; j <= numAvenues*numStreets; j++) {
                        String avenueTrafficLight = "";
                        container.createNewAgent("sender_" + j, SenderAgent.class.getName(), new Object[]{"Car", "TrafficLight" + j}).start();
                        Sleep(10);
                        avenueTrafficLight = sharedObjectTF.getGreen();
                        avenueTrafficLights.add(avenueTrafficLight);
                    }

                    List<List<String>> leftTrafficLightColors = new ArrayList<>();
                    List<List<String>> rightTrafficLightColors = new ArrayList<>();
                    List<List<String>> southTrafficLightColors = new ArrayList<>();
                    List<List<String>> northTrafficLightColors = new ArrayList<>();

                    // Initialize the traffic light colors for a 12x12 grid
                    for (int j = 0; j < numAvenues; j++) {
                        List<String> leftColors = new ArrayList<>();
                        List<String> rightColors = new ArrayList<>();
                        List<String> southColors = new ArrayList<>();
                        List<String> northColors = new ArrayList<>();

                        // Set initial colors to "R" for all lights
                        for (int k = 0; k < numStreets; k++) {
                            leftColors.add("r");
                            rightColors.add("r");
                            southColors.add("r");
                            northColors.add("r");
                        }

                        leftTrafficLightColors.add(leftColors);
                        rightTrafficLightColors.add(rightColors);
                        southTrafficLightColors.add(southColors);
                        northTrafficLightColors.add(northColors);
                    }

                    // Update the traffic light colors based on specific conditions
                    for (int k = 0; k < numAvenues; k++) {
                        for (int j = 0; j < numStreets; j++) {
                            String avenue_TrafficLight = avenueTrafficLights.get(k*numAvenues+j);

                            if (avenue_TrafficLight.equals("East-West")) {
                                leftTrafficLightColors.get(k).set(j, "g");
                                rightTrafficLightColors.get(k).set(j, "g");
                                northTrafficLightColors.get(k).set(j, "r");
                                southTrafficLightColors.get(k).set(j, "r");
                            } else if (avenue_TrafficLight.equals("North-South")) {
                                leftTrafficLightColors.get(k).set(j, "r");
                                rightTrafficLightColors.get(k).set(j, "r");
                                northTrafficLightColors.get(k).set(j, "g");
                                southTrafficLightColors.get(k).set(j, "g");
                            }
                        }
                    }
         
                    //Ambulance
                    String TL_Ambulance = "";
                    if (h < AvenueList_Ambulance_Go.size()){                      
                        Index = h; 
                        if (Index > 0){DirectionA = getDirection(Index,false,AvenueList_Ambulance_Go,StreetList_Ambulance_Go);}
                        else {DirectionA = getDirection(Index,true,AvenueList_Ambulance_Go,StreetList_Ambulance_Go);}
                        // Send message to Trafic Lights
                        if (!"0".equals(Avenue_Ambulance) && !"0".equals(Street_Ambulance) ){
                            //Tf_index = Integer.parseInt(Avenue_Ambulance) * numAvenues + Integer.parseInt(Street_Ambulance);
                            Tf_index = (Integer.parseInt(Street_Ambulance) - 1) * numAvenues + Integer.parseInt(Avenue_Ambulance);
                            container.createNewAgent("sender_" + Tf_index, SenderAgent.class.getName(), new Object[]{"911 " + DirectionA, "TrafficLight" + Tf_index}).start();
                            Sleep(10);
                            TL_Ambulance = sharedObjectTF.getGreen();
                            Sleep(10);
                         }
                        //Move
                        container.createNewAgent("senderA1_\"+Integer.toString(i)", SenderAgent.class.getName(), new Object[]{"MOVE_TO " + AvenueList_Ambulance_Go.get(Index) + " " + StreetList_Ambulance_Go.get(Index), "Ambulance"}).start();                      
                        Sleep(250);
                        AvenueList_Ambulance = sharedObjectA.getAvenueList();
                        StreetList_Ambulance = sharedObjectA.getStreetList();
                        Avenue_Ambulance = AvenueList_Ambulance.get(AvenueList_Ambulance.size() - 1);
                        Street_Ambulance = StreetList_Ambulance.get(StreetList_Ambulance.size() - 1);
                        String Previous_Avenue_Ambulance = AvenueList_Ambulance.get(AvenueList_Ambulance.size() - 2);
                        String Previous_Street_Ambulance = StreetList_Ambulance.get(StreetList_Ambulance.size() - 2);

                        carStreets.add(Integer.parseInt(Street_Ambulance));
                        carAvenues.add(Integer.parseInt(Avenue_Ambulance));
                        carLists.add("A");
                        
                        if (Integer.parseInt(Previous_Street_Ambulance) == numStreets){
                            if ("East-West".equals(TL_Ambulance)) {
                                rightTrafficLightColors.get(Integer.parseInt(Previous_Street_Ambulance)-1).set(Integer.parseInt(Previous_Avenue_Ambulance)-1, "G");
                                northTrafficLightColors.get(Integer.parseInt(Previous_Street_Ambulance)-1).set(Integer.parseInt(Previous_Avenue_Ambulance)-1, "R");
                                southTrafficLightColors.get(Integer.parseInt(Previous_Street_Ambulance)-1).set(Integer.parseInt(Previous_Avenue_Ambulance)-1, "R");
                            } else if ("North-South".equals(TL_Ambulance)) {
                                rightTrafficLightColors.get(Integer.parseInt(Previous_Street_Ambulance)-1).set(Integer.parseInt(Previous_Avenue_Ambulance)-1, "R");
                                northTrafficLightColors.get(Integer.parseInt(Previous_Street_Ambulance)-1).set(Integer.parseInt(Previous_Avenue_Ambulance)-1, "G");
                                southTrafficLightColors.get(Integer.parseInt(Previous_Street_Ambulance)-1).set(Integer.parseInt(Previous_Avenue_Ambulance)-1, "G");
                            }
                        } else{
                            if ("East-West".equals(TL_Ambulance)) {
                                leftTrafficLightColors.get(Integer.parseInt(Previous_Street_Ambulance)).set(Integer.parseInt(Previous_Avenue_Ambulance)-1, "G");
                                rightTrafficLightColors.get(Integer.parseInt(Previous_Street_Ambulance)-1).set(Integer.parseInt(Previous_Avenue_Ambulance)-1, "G");
                                northTrafficLightColors.get(Integer.parseInt(Previous_Street_Ambulance)-1).set(Integer.parseInt(Previous_Avenue_Ambulance)-1, "R");
                                southTrafficLightColors.get(Integer.parseInt(Previous_Street_Ambulance)-1).set(Integer.parseInt(Previous_Avenue_Ambulance)-1, "R");
                            } else if ("North-South".equals(TL_Ambulance)) {
                                leftTrafficLightColors.get(Integer.parseInt(Previous_Street_Ambulance)).set(Integer.parseInt(Previous_Avenue_Ambulance)-1, "R");
                                rightTrafficLightColors.get(Integer.parseInt(Previous_Street_Ambulance)-1).set(Integer.parseInt(Previous_Avenue_Ambulance)-1, "R");
                                northTrafficLightColors.get(Integer.parseInt(Previous_Street_Ambulance)-1).set(Integer.parseInt(Previous_Avenue_Ambulance)-1, "G");
                                southTrafficLightColors.get(Integer.parseInt(Previous_Street_Ambulance)-1).set(Integer.parseInt(Previous_Avenue_Ambulance)-1, "G");
                            } 
                        }             
                        
                    }
          
                    // Fire Truck
                    String TL_Fire = "";
                   if (step >= Fire_step &&  (i-(Fire_step-2)) < AvenueList_Fire_Go.size() ){
                        Index = i-(Fire_step-2);
                        if (Index > 0){DirectionF = getDirection(Index,false,AvenueList_Fire_Go,StreetList_Fire_Go);}
                        else {DirectionF = getDirection(Index,true,AvenueList_Fire_Go,StreetList_Fire_Go);}
                        // Send message to Trafic Lights
                        if (!"0".equals(Avenue_Fire) && !"0".equals(Street_Fire) ){                            
                            //Tf_index = Integer.parseInt(Avenue_Fire) * numAvenues + Integer.parseInt(Street_Fire);
                            Tf_index = (Integer.parseInt(Street_Fire) - 1) * numAvenues + Integer.parseInt(Avenue_Fire);
                            container.createNewAgent("sender_" + Tf_index, SenderAgent.class.getName(), new Object[]{"911 " + DirectionF, "TrafficLight" + Tf_index}).start();
                            Sleep(10);
                            TL_Fire = sharedObjectTF.getGreen();
                            Sleep(10);
                        }
                        //Move
                        container.createNewAgent("senderF1_\"+Integer.toString(i)", SenderAgent.class.getName(), new Object[]{"MOVE_TO " + AvenueList_Fire_Go.get(Index) + " " + StreetList_Fire_Go.get(Index), "Fire"}).start();
                        Sleep(250);
                        AvenueList_Fire = sharedObjectF.getAvenueList();
                        StreetList_Fire = sharedObjectF.getStreetList();
                        Avenue_Fire = AvenueList_Fire.get(AvenueList_Fire.size() - 1);
                        Street_Fire = StreetList_Fire.get(StreetList_Fire.size() - 1);
                        String Previous_Avenue_Fire = AvenueList_Fire.get(AvenueList_Fire.size() - 2);
                        String Previous_Street_Fire = StreetList_Fire.get(StreetList_Fire.size() - 2);

                        carStreets.add(Integer.parseInt(Street_Fire));
                        carAvenues.add(Integer.parseInt(Avenue_Fire));
                        carLists.add("F");
                        
                        carStreets.add(Integer.parseInt(Street_fire));
                        carAvenues.add(Integer.parseInt(Avenue_fire));
                        carLists.add("F!");
                        
                        if (Integer.parseInt(Previous_Street_Fire) == numStreets){
                            if ("East-West".equals(TL_Fire)) {
                                leftTrafficLightColors.get(Integer.parseInt(Previous_Street_Fire)-1).set(Integer.parseInt(Previous_Avenue_Fire)-1, "G");
                                rightTrafficLightColors.get(Integer.parseInt(Previous_Street_Fire)-1).set(Integer.parseInt(Previous_Avenue_Fire)-1, "G");
                                northTrafficLightColors.get(Integer.parseInt(Previous_Street_Fire)-1).set(Integer.parseInt(Previous_Avenue_Fire)-1, "R");
                                southTrafficLightColors.get(Integer.parseInt(Previous_Street_Fire)-1).set(Integer.parseInt(Previous_Avenue_Fire)-1, "R");
                            } else if ("North-South".equals(TL_Fire)) {
                                leftTrafficLightColors.get(Integer.parseInt(Previous_Street_Fire)-1).set(Integer.parseInt(Previous_Avenue_Fire)-1, "R");
                                rightTrafficLightColors.get(Integer.parseInt(Previous_Street_Fire)-1).set(Integer.parseInt(Previous_Avenue_Fire)-1, "R");
                                northTrafficLightColors.get(Integer.parseInt(Previous_Street_Fire)-1).set(Integer.parseInt(Previous_Avenue_Fire)-1, "G");
                                southTrafficLightColors.get(Integer.parseInt(Previous_Street_Fire)-1).set(Integer.parseInt(Previous_Avenue_Fire)-1, "G");
                            }
                         } else{
                             if ("East-West".equals(TL_Fire)) {
                                leftTrafficLightColors.get(Integer.parseInt(Previous_Street_Fire)).set(Integer.parseInt(Previous_Avenue_Fire)-1, "G");
                                rightTrafficLightColors.get(Integer.parseInt(Previous_Street_Fire)-1).set(Integer.parseInt(Previous_Avenue_Fire)-1, "G");
                                northTrafficLightColors.get(Integer.parseInt(Previous_Street_Fire)-1).set(Integer.parseInt(Previous_Avenue_Fire)-1, "R");
                                southTrafficLightColors.get(Integer.parseInt(Previous_Street_Fire)-1).set(Integer.parseInt(Previous_Avenue_Fire)-1, "R");
                            } else if ("North-South".equals(TL_Fire)) {
                                leftTrafficLightColors.get(Integer.parseInt(Previous_Street_Fire)).set(Integer.parseInt(Previous_Avenue_Fire)-1, "R");
                                rightTrafficLightColors.get(Integer.parseInt(Previous_Street_Fire)-1).set(Integer.parseInt(Previous_Avenue_Fire)-1, "R");
                                northTrafficLightColors.get(Integer.parseInt(Previous_Street_Fire)-1).set(Integer.parseInt(Previous_Avenue_Fire)-1, "G");
                                southTrafficLightColors.get(Integer.parseInt(Previous_Street_Fire)-1).set(Integer.parseInt(Previous_Avenue_Fire)-1, "G");
                            }
                        }
                      
                        if (Avenue_Fire.equals(Avenue_fire) && Street_Fire.equals(Street_fire)) {Fire_exist = false; System.out.println("The fire has just gone out!"); Street_fire = "0"; Avenue_fire = "0";}
					
                    }
                    
                    //Police
                    String TL_Police = "";
                    if (step >= Robery_step &&  (i-(Robery_step-2)) < AvenueList_Police_Go.size() ){
                         Index = i-(Robery_step-2); 
                        if (Index > 0){DirectionP = getDirection(Index,false,AvenueList_Police_Go,StreetList_Police_Go);}
                        else {DirectionP = getDirection(Index,true,AvenueList_Police_Go,StreetList_Police_Go);}
                        // Send message to Trafic Lights
                        if (!"0".equals(Avenue_Police) && !"0".equals(Street_Police) ){
                            //Tf_index = Integer.parseInt(Avenue_Police) * numAvenues + Integer.parseInt(Street_Police);
                            Tf_index = (Integer.parseInt(Street_Police) - 1) * numAvenues + Integer.parseInt(Avenue_Police);
                            container.createNewAgent("sender_" + Tf_index, SenderAgent.class.getName(), new Object[]{"911 " + DirectionP, "TrafficLight" + Tf_index}).start();
                            Sleep(10);
                            TL_Police = sharedObjectTF.getGreen();
                            Sleep(10);
                        }
                        //Move
                        container.createNewAgent("senderP1_\"+Integer.toString(i)", SenderAgent.class.getName(), new Object[]{"MOVE_TO " + AvenueList_Police_Go.get(Index) + " " + StreetList_Police_Go.get(Index), "Police"}).start();
                        Sleep(250);
                        AvenueList_Police = sharedObjectP.getAvenueList();
                        StreetList_Police = sharedObjectP.getStreetList();
                        Avenue_Police = AvenueList_Police.get(AvenueList_Police.size() - 1);
                        Street_Police = StreetList_Police.get(StreetList_Police.size() - 1);
                        String Previous_Avenue_Police = AvenueList_Police.get(AvenueList_Police.size() - 2);
                        String Previous_Street_Police = StreetList_Police.get(StreetList_Police.size() - 2);

                        carStreets.add(Integer.parseInt(Street_Police));
                        carAvenues.add(Integer.parseInt(Avenue_Police));
                        carLists.add("P");
                        
                        carStreets.add(Integer.parseInt(Street_robery));
                        carAvenues.add(Integer.parseInt(Avenue_robery));
                        carLists.add("P!");
                        
                        if (Integer.parseInt(Previous_Street_Police) == numStreets){
                            if ("East-West".equals(TL_Police)) {
                                leftTrafficLightColors.get(Integer.parseInt(Previous_Street_Police)-1).set(Integer.parseInt(Previous_Avenue_Police)-1, "G");
                                rightTrafficLightColors.get(Integer.parseInt(Previous_Street_Police)-1).set(Integer.parseInt(Previous_Avenue_Police)-1, "G");
                                northTrafficLightColors.get(Integer.parseInt(Previous_Street_Police)-1).set(Integer.parseInt(Previous_Avenue_Police)-1, "R");
                                southTrafficLightColors.get(Integer.parseInt(Previous_Street_Police)-1).set(Integer.parseInt(Previous_Avenue_Police)-1, "R");
                            } else if ("North-South".equals(TL_Police)) {
                                leftTrafficLightColors.get(Integer.parseInt(Previous_Street_Police)-1).set(Integer.parseInt(Previous_Avenue_Police)-1, "R");
                                rightTrafficLightColors.get(Integer.parseInt(Previous_Street_Police)-1).set(Integer.parseInt(Previous_Avenue_Police)-1, "R");
                                northTrafficLightColors.get(Integer.parseInt(Previous_Street_Police)-1).set(Integer.parseInt(Previous_Avenue_Police)-1, "G");
                                southTrafficLightColors.get(Integer.parseInt(Previous_Street_Police)-1).set(Integer.parseInt(Previous_Avenue_Police)-1, "G");
                            } 
                        } else {
                            if ("East-West".equals(TL_Police)) {
                                leftTrafficLightColors.get(Integer.parseInt(Previous_Street_Police)).set(Integer.parseInt(Previous_Avenue_Police)-1, "G");
                                rightTrafficLightColors.get(Integer.parseInt(Previous_Street_Police)-1).set(Integer.parseInt(Previous_Avenue_Police)-1, "G");
                                northTrafficLightColors.get(Integer.parseInt(Previous_Street_Police)-1).set(Integer.parseInt(Previous_Avenue_Police)-1, "R");
                                southTrafficLightColors.get(Integer.parseInt(Previous_Street_Police)-1).set(Integer.parseInt(Previous_Avenue_Police)-1, "R");
                            } else if ("North-South".equals(TL_Police)) {
                                leftTrafficLightColors.get(Integer.parseInt(Previous_Street_Police)).set(Integer.parseInt(Previous_Avenue_Police)-1, "R");
                                rightTrafficLightColors.get(Integer.parseInt(Previous_Street_Police)-1).set(Integer.parseInt(Previous_Avenue_Police)-1, "R");
                                northTrafficLightColors.get(Integer.parseInt(Previous_Street_Police)-1).set(Integer.parseInt(Previous_Avenue_Police)-1, "G");
                                southTrafficLightColors.get(Integer.parseInt(Previous_Street_Police)-1).set(Integer.parseInt(Previous_Avenue_Police)-1, "G");
                            } 
                        }
           
                        if (Avenue_Police.equals(Avenue_robery) && Street_Police.equals(Street_robery)) {Robery_exist = false; System.out.println("The robber was caught and taken to the police station!"); Street_robery="0"; Avenue_robery="0";} 
                        if (Avenue_Police.equals(Avenue_police_station) && Street_Police.equals(Street_police_station) && step > Robery_step+5) {Police_back = true; System.out.println("The police vehicle returned and the robber was taken to the police station!");}      
                    }
                                      
                     //Car 1
                    Sleep(50);
                    randomMove = getRandomMove();
                    Random_parts = randomMove.split("_");
                    DirectionC1 = Random_parts[1];
                    //Move
                    Avenue_C1 = sharedObjectC1.getAvenue();
                    Street_C1 = sharedObjectC1.getStreet();
                    if (i == 0){
                        Sleep(50);
                        System.out.println("Car1 move:  " + randomMove);
                        container.createNewAgent("senderc1_"+Integer.toString(i), SenderAgent.class.getName(), new Object[]{randomMove, "Car1"}).start();
                        Sleep(50);
                        Avenue_C1 = sharedObjectC1.getAvenue();
                        Street_C1 = sharedObjectC1.getStreet();
                    }
                    
                    direction = ""; 
                    if (!"0".equals(Avenue_C1) && !"0".equals(Street_C1) && i!= 0){
                        //Tf_index = Integer.parseInt(Avenue_C1) * numAvenues + Integer.parseInt(Street_C1);
                        Tf_index = (Integer.parseInt(Street_C1) - 1) * numAvenues + Integer.parseInt(Avenue_C1);
                        if ("MOVE_NORTH".equals(randomMove) || "MOVE_SOUTH".equals(randomMove)){
                            direction = "North-South";
                        }else if ("MOVE_EAST".equals(randomMove) || "MOVE_WEST".equals(randomMove)){
                            direction = "East-West";
                        }
                        
                        if (direction == avenueTrafficLights.get(Tf_index)){
                            Sleep(10);
                            System.out.println("Car1 move:  " + randomMove);
                            container.createNewAgent("senderc1_"+Integer.toString(i), SenderAgent.class.getName(), new Object[]{randomMove, "Car1"}).start();
                            Sleep(10);
                            Avenue_C1 = sharedObjectC1.getAvenue();
                            Street_C1 = sharedObjectC1.getStreet();
                        }     
                        else{
                            Avenue_C1 = sharedObjectC1.getAvenue();
                            Street_C1 = sharedObjectC1.getStreet();
                        }
                        
                        carStreets.add(Integer.parseInt(Street_C1));
                        carAvenues.add(Integer.parseInt(Avenue_C1));
                        carLists.add("C1");
                            
                    }
                                 
                     //Change C2 to 911
                    if (step == Car2_emergency_step){
                        container.createNewAgent("senderC2EM1_\"+Integer.toString(i)", SenderAgent.class.getName(), new Object[]{"MOVE_TO " + Avenue_C2 + " " + Street_C2, "C2_Emergency"}).start();
                        Sleep(250);
                        List<String> AvenueList_C2_Emergency_init = sharedObjectC2_EM.getAvenueList();
                        List<String> StreetList_C2_Emergency_init = sharedObjectC2_EM.getStreetList();
                        AvenueList_C2_Emergency_init.clear();
                        StreetList_C2_Emergency_init.clear();
                        Sleep(10);
                        container.createNewAgent("senderC2EM2_\"+Integer.toString(i)", SenderAgent.class.getName(), new Object[]{"CALL_EMERGENCY HP C2_Emergency", "Com"}).start();
                        Sleep(250);
                        AvenueList_C2_Emergency_Go = new ArrayList<>(sharedObjectC2_EM.getAvenueList());
                        StreetList_C2_Emergency_Go = new ArrayList<>(sharedObjectC2_EM.getStreetList());
                        Sleep(10);
                        container.createNewAgent("senderC2EM3_\"+Integer.toString(i)", SenderAgent.class.getName(), new Object[]{"MOVE_TO " + Avenue_C2 + " " + Street_C2, "C2_Emergency"}).start();
                        Sleep(250);      
                    }
                    
                    //Car 2
                    String TL_Car2 = "";
                    if (Car2_emergency == false && Car2_hospital == false){
                        Sleep(50);
                        randomMove = getRandomMove();
                        Random_parts = randomMove.split("_");
                        DirectionC2 = Random_parts[1];
                        //Move
                        Avenue_C2 = sharedObjectC2.getAvenue();
                        Street_C2= sharedObjectC2.getStreet();
                        if (i == 0){
                            Sleep(50);
                            System.out.println("Car2 move:  " + randomMove);
                            container.createNewAgent("senderc2_"+Integer.toString(i), SenderAgent.class.getName(), new Object[]{randomMove, "Car2"}).start();
                            Sleep(50);
                            Avenue_C2 = sharedObjectC2.getAvenue();
                            Street_C2= sharedObjectC2.getStreet();
                        }

                        direction = "";
                        if (!"0".equals(Avenue_C2) && !"0".equals(Street_C2) && i!= 0){
                            //Tf_index = Integer.parseInt(Avenue_C2) * numAvenues + Integer.parseInt(Street_C2);
                            Tf_index = (Integer.parseInt(Street_C2) - 1) * numAvenues + Integer.parseInt(Avenue_C2);
                            if ("MOVE_NORTH".equals(randomMove) || "MOVE_SOUTH".equals(randomMove)){
                                direction = "North-South";
                            }else if ("MOVE_EAST".equals(randomMove) || "MOVE_WEST".equals(randomMove)){
                                direction = "East-West";
                            }					

                            if (direction == avenueTrafficLights.get(Tf_index)){
                                Sleep(10);
                                System.out.println("Car2 move:  " + randomMove);
                                container.createNewAgent("senderc2_"+Integer.toString(i), SenderAgent.class.getName(), new Object[]{randomMove, "Car2"}).start();
                                Sleep(10);
                                Avenue_C2 = sharedObjectC2.getAvenue();
                                Street_C2= sharedObjectC2.getStreet();
                            }     
                            else{
                                Avenue_C2 = sharedObjectC2.getAvenue();
                                Street_C2= sharedObjectC2.getStreet();
                            }
                        }
                        carStreets.add(Integer.parseInt(Street_C2));
                        carAvenues.add(Integer.parseInt(Avenue_C2));
                        carLists.add("C2");				
                    }
                    else if (Car2_emergency == true && Car2_hospital == false){  
                        Index = i - Car2_emergency_step +1; 
                        if (Index > 0){DirectionC2 = getDirection(Index,false,AvenueList_C2_Emergency_Go,StreetList_C2_Emergency_Go);}
                        else {DirectionC2 = getDirection(Index,true,AvenueList_C2_Emergency_Go,StreetList_C2_Emergency_Go);}
                        // Send message to Trafic Lights
                        if (!"0".equals(Avenue_C2) && !"0".equals(Street_C2) ){
                            //Tf_index = Integer.parseInt(Avenue_C2) * numAvenues + Integer.parseInt(Street_C2);
                            Tf_index = (Integer.parseInt(Street_C2) - 1) * numAvenues + Integer.parseInt(Avenue_C2);
                            container.createNewAgent("sender_" + Tf_index, SenderAgent.class.getName(), new Object[]{"911 " + DirectionC2, "TrafficLight" + Tf_index}).start();
                            Sleep(10);
                            TL_Car2 = sharedObjectTF.getGreen();
                            Sleep(10);
                        }
                        //Move                      
                        container.createNewAgent("senderC2EM4_\"+Integer.toString(i)", SenderAgent.class.getName(), new Object[]{"MOVE_TO " + AvenueList_C2_Emergency_Go.get(Index) + " " + StreetList_C2_Emergency_Go.get(Index), "C2_Emergency"}).start();
                        Sleep(5000);
                        AvenueList_C2_Emergency = sharedObjectC2_EM.getAvenueList();
                        StreetList_C2_Emergency = sharedObjectC2_EM.getStreetList();
                        Avenue_C2 = AvenueList_C2_Emergency.get(AvenueList_C2_Emergency.size() - 1);
                        Street_C2 = StreetList_C2_Emergency.get(StreetList_C2_Emergency.size() - 1); 
                        String Previous_Avenue_C2_Emergency = AvenueList_C2_Emergency.get(AvenueList_C2_Emergency.size() - 2);
                        String Previous_Street_C2_Emergency = StreetList_C2_Emergency.get(StreetList_C2_Emergency.size() - 2);

                        carStreets.add(Integer.parseInt(Street_C2));
                        carAvenues.add(Integer.parseInt(Avenue_C2));
                        carLists.add("C2!");
                        
                        if (Integer.parseInt(Previous_Street_C2_Emergency) == numStreets){
                            if ("East-West".equals(TL_Car2)) {
                                leftTrafficLightColors.get(Integer.parseInt(Previous_Street_C2_Emergency)-1).set(Integer.parseInt(Previous_Avenue_C2_Emergency)-1, "G");
                                rightTrafficLightColors.get(Integer.parseInt(Previous_Street_C2_Emergency)-1).set(Integer.parseInt(Previous_Avenue_C2_Emergency)-1, "G");
                                northTrafficLightColors.get(Integer.parseInt(Previous_Street_C2_Emergency)-1).set(Integer.parseInt(Previous_Avenue_C2_Emergency)-1, "R");
                                southTrafficLightColors.get(Integer.parseInt(Previous_Street_C2_Emergency)-1).set(Integer.parseInt(Previous_Avenue_C2_Emergency)-1, "R");
                            } else if ("North-South".equals(TL_Car2)) {
                                leftTrafficLightColors.get(Integer.parseInt(Previous_Street_C2_Emergency)-1).set(Integer.parseInt(Previous_Avenue_C2_Emergency)-1, "R");
                                rightTrafficLightColors.get(Integer.parseInt(Previous_Street_C2_Emergency)-1).set(Integer.parseInt(Previous_Avenue_C2_Emergency)-1, "R");
                                northTrafficLightColors.get(Integer.parseInt(Previous_Street_C2_Emergency)-1).set(Integer.parseInt(Previous_Avenue_C2_Emergency)-1, "G");
                                southTrafficLightColors.get(Integer.parseInt(Previous_Street_C2_Emergency)-1).set(Integer.parseInt(Previous_Avenue_C2_Emergency)-1, "G");
                            } 
                        } else{
                            if ("East-West".equals(TL_Car2)) {
                               leftTrafficLightColors.get(Integer.parseInt(Previous_Street_C2_Emergency)).set(Integer.parseInt(Previous_Avenue_C2_Emergency)-1, "G");
                               rightTrafficLightColors.get(Integer.parseInt(Previous_Street_C2_Emergency)-1).set(Integer.parseInt(Previous_Avenue_C2_Emergency)-1, "G");
                               northTrafficLightColors.get(Integer.parseInt(Previous_Street_C2_Emergency)-1).set(Integer.parseInt(Previous_Avenue_C2_Emergency)-1, "R");
                               southTrafficLightColors.get(Integer.parseInt(Previous_Street_C2_Emergency)-1).set(Integer.parseInt(Previous_Avenue_C2_Emergency)-1, "R");
                           } else if ("North-South".equals(TL_Car2)) {
                               leftTrafficLightColors.get(Integer.parseInt(Previous_Street_C2_Emergency)).set(Integer.parseInt(Previous_Avenue_C2_Emergency)-1, "R");
                               rightTrafficLightColors.get(Integer.parseInt(Previous_Street_C2_Emergency)-1).set(Integer.parseInt(Previous_Avenue_C2_Emergency)-1, "R");
                               northTrafficLightColors.get(Integer.parseInt(Previous_Street_C2_Emergency)-1).set(Integer.parseInt(Previous_Avenue_C2_Emergency)-1, "G");
                               southTrafficLightColors.get(Integer.parseInt(Previous_Street_C2_Emergency)-1).set(Integer.parseInt(Previous_Avenue_C2_Emergency)-1, "G");
                           } 
                         }
			
                        if (Avenue_C2.equals(Avenue_hospital) && Street_C2.equals(Street_hospital)) {
                            Car2_hospital = true; 
                            Car2_emergency = false;
                            System.out.println("Car 2 has arrived at the hospital!");}
                    }      
                    
                    //Car 3
                    Sleep(50);
                    randomMove = getRandomMove();
                    Random_parts = randomMove.split("_");
                    DirectionC3 = Random_parts[1];
                    //Move
                    Avenue_C3 = sharedObjectC3.getAvenue();
                    Street_C3 = sharedObjectC3.getStreet();
                    if (i == 0){
                        Sleep(50);
                        System.out.println("Car3 move:  " + randomMove);
                        container.createNewAgent("senderc3_"+Integer.toString(i), SenderAgent.class.getName(), new Object[]{randomMove, "Car3"}).start();
                        Sleep(50);
                        Avenue_C3 = sharedObjectC3.getAvenue();
                        Street_C3 = sharedObjectC3.getStreet();
                    }
                    
                    direction = "";
                    if (!"0".equals(Avenue_C3) && !"0".equals(Street_C3) && i!= 0){
                        //Tf_index = Integer.parseInt(Avenue_C3) * numAvenues + Integer.parseInt(Street_C3);
                        Tf_index = (Integer.parseInt(Street_C3) - 1) * numAvenues + Integer.parseInt(Avenue_C3);
                        if ("MOVE_NORTH".equals(randomMove) || "MOVE_SOUTH".equals(randomMove)){
                            direction = "North-South";
                        }else if ("MOVE_EAST".equals(randomMove) || "MOVE_WEST".equals(randomMove)){
                            direction = "East-West";
                        }
                        
                        if (direction == avenueTrafficLights.get(Tf_index)){
                            Sleep(10);
                            System.out.println("Car3 move:  " + randomMove);
                            container.createNewAgent("senderc3_"+Integer.toString(i), SenderAgent.class.getName(), new Object[]{randomMove, "Car3"}).start();
                            Sleep(10);
                            Avenue_C3 = sharedObjectC3.getAvenue();
                            Street_C3 = sharedObjectC3.getStreet();
                        }     
                        else{
                            Avenue_C3 = sharedObjectC3.getAvenue();
                            Street_C3= sharedObjectC3.getStreet();
                        }
                        
                        carStreets.add(Integer.parseInt(Street_C3));
                        carAvenues.add(Integer.parseInt(Avenue_C3));
                        carLists.add("C3");
                            
                    }
                    printGridWorld(numAvenues, numStreets, carStreets, carAvenues, carLists, leftTrafficLightColors, rightTrafficLightColors, southTrafficLightColors, northTrafficLightColors);
                    if (Car2_hospital==true && Police_back == true) {break outerLoop;}
                }
                  
            }
        } catch (StaleProxyException e) {
        }
        
         System.out.println("End of Scenario");
    }
    
    
    public static void Sleep(int t){
        try {
            Thread.sleep(t); 
        } catch (InterruptedException e) {
        }
    }


    public static String getRandomMove() {
            String[] choices = {"MOVE_NORTH", "MOVE_SOUTH", "MOVE_WEST", "MOVE_EAST"};
            Random random = new Random();
            int randomIndex = random.nextInt(choices.length);
            return choices[randomIndex];
        }


    public static String getDirection(int Index, boolean init_position, List<String> AvenueList_Go, List<String> StreetList_Go) {
        int nextStreet;
        int nextAvenue;
        int currentStreet;
        int currentAvenue;
        nextStreet = Integer.parseInt(AvenueList_Go.get(Index));
        nextAvenue = Integer.parseInt(StreetList_Go.get(Index));

        if (init_position == false ) {
            currentStreet = Integer.parseInt(AvenueList_Go.get(Index-1));
            currentAvenue = Integer.parseInt(StreetList_Go.get(Index-1));
        } else {
            currentStreet = nextStreet;
            currentAvenue = nextAvenue;
        }

        if (currentAvenue == nextAvenue && currentStreet == nextStreet) {
            return "STAY"; // The agent stays in the same position
        }

        if (currentAvenue == nextAvenue) {
            if (currentStreet < nextStreet) {
                return "North-South";
            } else {
                return "North-South";
            }
        }

        if (currentStreet == nextStreet) {
            if (currentAvenue < nextAvenue) {
                return "East-West";
            } else {
                return "East-West";
            }

        }
        return "UNKNOWN";
    }


    public static void printGridWorld(int numAvenues, int numStreets, List<Integer> carAvenues, List<Integer> carStreets, List<String> carLists, List<List<String>> leftTrafficLightColors, List<List<String>> rightTrafficLightColors, List<List<String>> southTrafficLightColors, List<List<String>> northTrafficLightColors) {
        // Print the top border of the grid
        for (int i = 0; i < numAvenues; i++) {
            System.out.print("-+------------");
        }
        System.out.println("-+");

        for (int street = 1; street <= numStreets; street++) {
            // Print the north traffic light colors
            System.out.print(" | ");
            for (int i = 0; i < numAvenues; i++) {
                System.out.print("     " + northTrafficLightColors.get(i).get(street-1) + "      | ");
            }
            System.out.println("");

            for (int avenue = 1; avenue <= numAvenues; avenue++) {
                boolean isCarPresent = false;
                String car = "";
                for (int i = 0; i < carAvenues.size(); i++) {
                    if (avenue == carAvenues.get(i) && street == carStreets.get(i)) {
                        isCarPresent = true;
                        car = carLists.get(i);
                        break;
                    }
                }

                if (isCarPresent) {
                    if (avenue == 1) {
                        System.out.print(" " + leftTrafficLightColors.get(0).get(street-1) + "     ");
                            System.out.print(car);
                    } else {
                        System.out.print("    " + leftTrafficLightColors.get(avenue-1).get(street-1) + " | " + rightTrafficLightColors.get(avenue-1).get(street-1) + "    ");
                            System.out.print(car);
                    }
                } else {
                    if (avenue == 1) {
                        System.out.print(" " + leftTrafficLightColors.get(0).get(street-1) + "       ");
                    } else {
                        System.out.print("    " + leftTrafficLightColors.get(avenue-1).get(street-1) + " | " + rightTrafficLightColors.get(avenue-1).get(street-1) + "     ");
                    }
                }
            }
            System.out.println("      |" );

            // Print the south traffic light colors
            System.out.print(" | ");
            for (int i = 0; i < numAvenues; i++) {
                System.out.print("     " + southTrafficLightColors.get(i).get(street-1) + "      | ");
            }
            System.out.println("");

            // Print the road separating each street
            if (street < numStreets) {
                for (int i = 0; i < numAvenues; i++) {
                    System.out.print("-+------------");
                }
                System.out.println("-+");
            }
        }

        // Print the bottom border of the grid
        for (int i = 0; i < numAvenues; i++) {
            System.out.print("-+------------");
        }
        System.out.println("-+");
    }

}

