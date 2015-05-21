/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Hampus
 */
public class RadioMap {
    private static RadioMap _instance;
    private final ArrayList<ReferenceArea> referenceAreas;
    
    public synchronized static RadioMap getInstance() {
        if (_instance == null) 
            _instance = new RadioMap();
        return _instance;
    }
    
    public RadioMap() {
        Database.getInstance().openConnection();
        referenceAreas = Database.getInstance().getRadioMap();
        Database.getInstance().closeConnection();
        System.out.println("referencePoints size: " + referenceAreas.size());
    }
    
    public synchronized ArrayList<ReferenceArea> getReferenceAreas() {
        return new ArrayList<>(referenceAreas);
    }
    
    public synchronized ArrayList<ReferenceArea> getRelevantAreas(double x, double y, HashMap<String, Double> obtainedFingerprint) {
        ArrayList<ReferenceArea> relevantAreas = new ArrayList<>();
        for (ReferenceArea area : referenceAreas) {
            for (String key : obtainedFingerprint.keySet()) {
                if (!area.fingerprint.containsKey(key) || (Math.sqrt(Math.pow((area.x - x), 2) + Math.pow((area.y - y), 2)) > 100))
                    break;
            }
            relevantAreas.add(area);
        }
        return relevantAreas;
    }
    
    public synchronized void addReferenceArea(ReferenceArea a) {
        removeUnreliable(a);
        ReferenceArea area = getReferenceArea(a.x, a.y);
        if (area != null)
            area.fingerprint = a.fingerprint;
        else
            referenceAreas.add(a);
        StringBuilder sb = new StringBuilder();
        for (String key : a.fingerprint.keySet()) {
            sb.append(key).append(" ").append(a.fingerprint.get(key));
        }
        Database.getInstance().openConnection();
        Database.getInstance().addReferenceArea(a);
        Database.getInstance().closeConnection();
    }
       
    /*
        Adds magnetic fingerprints to map and connects them to nearby RSSReferencepoint.
    */
    public synchronized void addMagneticPoints(ArrayList<MagneticPoint> magneticPoints) {
        Database.getInstance().openConnection();
        for(ReferenceArea area : referenceAreas) {
            
            for(MagneticPoint magneticPoint :  magneticPoints) {                                            //Ändra från 300 sen
                if(Math.sqrt(Math.pow((area.x - magneticPoint.x), 2) + Math.pow((area.y - magneticPoint.y), 2)) < 3000) {
                    area.addMagneticPoint(magneticPoint);
                    Database.getInstance().addMagneticPoint(magneticPoint, area);
                }    
            }
        
        }
        Database.getInstance().closeConnection();
    }
    
    public synchronized ReferenceArea getReferenceArea(double x, double y) {
        for (ReferenceArea area : referenceAreas) {
            if(x == area.x && y == area.y)
                return area;
        }
        return null;
    }
    
    public synchronized void removeReferenceArea(double x, double y) {
        referenceAreas.remove(getReferenceArea(x, y));
    }

    private void removeUnreliable(ReferenceArea area) {
        ArrayList<String> toRemove = new ArrayList<>();

        for (String key : area.fingerprint.keySet() ) {
        
            if ((double)area.fingerprint.get(key) < -80)
                toRemove.add(key);
        
        }
        for (String key : toRemove)
            area.fingerprint.remove(key);
        
    }
    
}