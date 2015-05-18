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
    private final ArrayList<ReferencePoint> referencePoints;
    
    public synchronized static RadioMap getInstance() {
        if (_instance == null) 
            _instance = new RadioMap();
        return _instance;
    }
    
    public RadioMap() {
        Database.getInstance().openConnection();
        referencePoints = Database.getInstance().getRadioMap();
        Database.getInstance().closeConnection();
        System.out.println("referencePoints size: " + referencePoints.size());
    }
    
    public synchronized ArrayList<ReferencePoint> getReferencePoints() {
        ArrayList<ReferencePoint> tmp = new ArrayList<>(referencePoints);
        return tmp;
    }
    
    public synchronized ArrayList<ReferencePoint> getRelevantPoints(double x, double y, HashMap<String, Double> deviceFingerprint) {
        ArrayList<ReferencePoint> result = new ArrayList<>();
        for (ReferencePoint point : referencePoints) {
            for (String key : deviceFingerprint.keySet()) {
                if (!point.fingerprint.containsKey(key) || (Math.sqrt(Math.pow((point.x - x), 2) + Math.pow((point.y - y), 2)) > 100))
                    break;
            }
            result.add(point);
        }
        return result;
    }
    
    public synchronized void addReferencePoint(ReferencePoint p) {
        removeUnreliable(p);
        ReferencePoint point = getReferencePoint(p.x, p.y);
        if (point != null)
            point.fingerprint = p.fingerprint;
        else
            referencePoints.add(p);
        StringBuilder sb = new StringBuilder();
        for (String key : p.fingerprint.keySet()) {
            sb.append(key).append(" ").append(p.fingerprint.get(key));
        }
        Database.getInstance().openConnection();
        Database.getInstance().addReferencePoint(p);
        Database.getInstance().closeConnection();
        System.out.println(sb.toString());
    }
       
    /*
        Adds magnetic fingerprints to map and connects them to nearby RSSReferencepoint.
    */
    public synchronized void addMagneticFingerprints(ArrayList<MagneticFingerprint> magnetics) {
        Database.getInstance().openConnection();
        for(ReferencePoint point : referencePoints) {
            for(MagneticFingerprint magneticFingerprint : magnetics) {                                            //Ändra från 300 sen
                if(Math.sqrt(Math.pow((point.x - magneticFingerprint.x), 2) + Math.pow((point.y - magneticFingerprint.y), 2)) < 3000) {
                    point.addMagnetic(magneticFingerprint);
                    Database.getInstance().addMagneticPoint(magneticFingerprint, point);
                }    
            }
        }
        Database.getInstance().closeConnection();
    }
    
    public synchronized ReferencePoint getReferencePoint(double x, double y) {
        for (ReferencePoint point : referencePoints) {
            if(x == point.x && y == point.y)
                return point;
        }
        return null;
    }
    
    public synchronized void removeReferencePoint(double x, double y) {
        referencePoints.remove(getReferencePoint(x, y));
    }

    private void removeUnreliable(ReferencePoint p) {
        ArrayList<String> toRemove = new ArrayList<>();

        for (String key : p.fingerprint.keySet() ) {
        
            if ((double)p.fingerprint.get(key) < -80)
                toRemove.add(key);
        
        }
        
        for (String key : toRemove)
            p.fingerprint.remove(key);
        
    }
    
}
