/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 *
 * @author Hampus
 */
public class RadioMap {

    private static RadioMap _instance;
    private final ArrayList<ReferenceArea> referenceAreas;
    private HashSet<String> accessPoints;

    public synchronized static RadioMap getInstance() {
        if (_instance == null) {
            _instance = new RadioMap();
        }
        return _instance;
    }

    public RadioMap() {
        Database.getInstance().openConnection();
        referenceAreas = Database.getInstance().getReferenceAreas();
        accessPoints = new HashSet<>();
        
        for(ReferenceArea area : referenceAreas) {
            removeUnreliable(area);
            for(String BSSID : area.fingerprint.keySet()) {
                accessPoints.add(BSSID);
            }
        }
        
        ArrayList<MagneticPoint> tmpPoints = Database.getInstance().getMagneticPoints();
        addMagneticPoints(tmpPoints);
        
        computeTresholds();

        Database.getInstance().closeConnection();
        System.out.println("referencePoints size: " + referenceAreas.size());
    }
    
    public synchronized void addMagneticPoints(ArrayList<MagneticPoint> tmpPoints) {
        for(MagneticPoint p : tmpPoints) {
            addMagneticPoint(p);
        }
    }
    
    public synchronized ArrayList<String> getAccessPoints() {
        return new ArrayList<String>(accessPoints);
    }

    public synchronized ArrayList<ReferenceArea> getReferenceAreas() {
        return new ArrayList<>(referenceAreas);
    }

    public synchronized ArrayList<ReferenceArea> getRelevantAreas(double x, double y, HashMap<String, Double> obtainedFingerprint) {
        ArrayList<ReferenceArea> relevantAreas = new ArrayList<>();
        for (ReferenceArea area : referenceAreas) {
            for (String key : obtainedFingerprint.keySet()) {
                if (!area.fingerprint.containsKey(key) || (Math.sqrt(Math.pow((area.x - x), 2) + Math.pow((area.y - y), 2)) > 100)) {
                    break;
                }
            }
            relevantAreas.add(area);
        }
        return relevantAreas;
    }

    public synchronized void addReferenceArea(ReferenceArea a) {
        removeUnreliable(a);
        ReferenceArea area = getReferenceArea(a.x, a.y);
        if (area != null) {
            area.fingerprint = a.fingerprint;
        } else {
            referenceAreas.add(a);
        }
        StringBuilder sb = new StringBuilder();
        for (String key : a.fingerprint.keySet()) {
            sb.append(key).append(" ").append(a.fingerprint.get(key));
        }
    }
    
    public synchronized void addReferenceAreaBT(ReferenceArea a) {
            referenceAreas.add(a);
    }
    
    public synchronized void addReferenceAreaDBBT(ReferenceArea a) {
        Database.getInstance().openConnection();
        Database.getInstance().addReferenceArea(a);
        Database.getInstance().closeConnection();
    }

    public synchronized void addReferenceAreaDB(ReferenceArea a) {
        removeUnreliable(a);
        Database.getInstance().openConnection();
        Database.getInstance().addReferenceArea(a);
        
        //ADD ACCESSPOINTS
        for (Map.Entry entry : a.fingerprint.entrySet()) {
            if(!accessPoints.contains(entry.getKey()))
                Database.getInstance().addAccessPoint((String)entry.getKey());
        }
        Database.getInstance().closeConnection();
    }

    /*
     Adds magnetic fingerprints to map and connects them to nearby RSSReferencepoint.
     */
    public synchronized void addMagneticPointDB(MagneticPoint magneticPoint) {
        Database.getInstance().openConnection();
        Database.getInstance().addMagneticPoint(magneticPoint);
        Database.getInstance().closeConnection();
    }

    public synchronized void addMagneticPoint(MagneticPoint magneticPoint) {
        for (ReferenceArea area : referenceAreas) {
            if (Math.sqrt(Math.pow((area.x - magneticPoint.x), 2) + Math.pow((area.y - magneticPoint.y), 2)) < 150) {
                area.addMagneticPoint(magneticPoint);
            }
        }
    }

    public synchronized ReferenceArea getReferenceArea(double x, double y) {
        for (ReferenceArea area : referenceAreas) {
            if (x == area.x && y == area.y) {
                return area;
            }
        }
        return null;
    }

    public synchronized void removeReferenceArea(double x, double y) {
        referenceAreas.remove(getReferenceArea(x, y));
    }

    private void removeUnreliable(ReferenceArea area) {
        ArrayList<String> toRemove = new ArrayList<>();

        for (String key : area.fingerprint.keySet()) {

            if ((double) area.fingerprint.get(key) < -90) {
                toRemove.add(key);
            }

        }
        for (String key : toRemove) {
            area.fingerprint.remove(key);
        }

    }

    private void computeTresholds() {
        for(ReferenceArea area : referenceAreas) {
            ArrayList<MagneticPoint> magnetics = area.getMagneticPoints();
            double h = 0;
            double l = Double.MAX_VALUE;
            double temp;
            double mean = 0;
                for(MagneticPoint point : magnetics) {
                    temp = 0;
//                    temp += Math.pow((point.magnitude), 2);
                    //temp += Math.pow((point.zaxis), 2);
                    //temp += Math.pow((point.xyaxis), 2);
//                    temp = Math.sqrt(temp);
                    temp = point.magnitude;
                    if(temp > h) {
                        h = temp;
                    }
                    if(temp < l) {
                        l = temp;
                    }
                    mean += temp;
                    //map.put(distance, point);
            }
            mean /= magnetics.size();
            mean = magnetics.get(Math.round(magnetics.size()/2)).magnitude;
            System.out.println("hightest: "+ h +"\nlowest: "+ l +"\nmean: "+ mean +"\nTreshold: "+ 0.001 * mean * (h - l));
            area.CANDIDATES_TRESHOLD = 0.001 * mean * (h - l);
        }
    }
}
