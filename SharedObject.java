package traffic_light_agent;

import java.util.ArrayList;
import java.util.List;

public class SharedObject {
    private String avenue;
    private String  street;
    private final List<String> avenue_list= new ArrayList<>();
    private final List<String> street_list = new ArrayList<>();
    private String currentGreen;
    
    public synchronized String getAvenue() {
        return avenue;
    }
    
     public synchronized String getStreet() {
        return street;
    }
     
      public synchronized  List<String> getAvenueList() {
        return avenue_list;
    }
    
     public synchronized List<String> getStreetList() {
        return street_list;
    }

    public synchronized void setAvenue(String avenue) {
        this.avenue = avenue;
    }
    
    public synchronized void setStreet(String street) {
        this.street = street;
    }
        
     public synchronized void setAvenueList(String avenue) {
        this.avenue_list.add(avenue);
    }
    
    public synchronized void setStreetList(String street) {
        this.street_list.add(street);
    }
    
    public synchronized String getGreen() {
        return currentGreen;
    }
    
    public synchronized void setGreen(String currentGreen) {
        this.currentGreen = currentGreen;
    }
    
    
}