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
public final class RadioMap {
    private static RadioMap _instance;
    private ArrayList<ReferencePoint> points;
    
    public synchronized static RadioMap getInstance() {
        if (_instance == null) 
            _instance = new RadioMap();
        return _instance;
    }
    
    public RadioMap() {
        points = new ArrayList<>();
    }
    
    public synchronized ArrayList<ReferencePoint> getReferencePoints() {
        ArrayList<ReferencePoint> tmp = new ArrayList<>(points);
        return tmp;
    }
    
    public synchronized ArrayList<ReferencePoint> getRelevantPoints(HashMap<String, Double> deviceFingerprint) {
        ArrayList<ReferencePoint> result = new ArrayList<>();
        for (ReferencePoint p : points) {
            for (String key : deviceFingerprint.keySet()) {
                if (!p.RSSfingerprint.containsKey(key))
                    break;
            }
            result.add(p);
        }
        return result;
    }
    
    public synchronized void addReferencePoint(ReferencePoint point) {
        ReferencePoint p = getReferencePoint(point.x, point.y);
        if (p != null)
            p.RSSfingerprint = point.RSSfingerprint;
        else
            points.add(point);
        StringBuilder sb = new StringBuilder();
        for (String key : point.RSSfingerprint.keySet()) {
            sb.append(key).append(" ").append(point.RSSfingerprint.get(key));
        }
        System.out.println(sb.toString());
    }
    
    
    /*
        Adds magnetic fingerprints to map and connects them to nearby RSSReferencepoint.
    */
    public synchronized void addMagneticFingerprints(ArrayList<MagneticFingerprint> magnetics) {
        for(ReferencePoint p : points) {
            for(MagneticFingerprint mf : magnetics) {
                if(Math.sqrt(Math.pow((p.x - mf.x), 2)+Math.pow((p.y - mf.y), 2)) < 150) {
                        p.addMagnetic(mf);
                }    
            }
        }
    }
    
    public synchronized ReferencePoint getReferencePoint(double x, double y) {
        for (ReferencePoint p : points) {
            if(x == p.x && y == p.y)
                return p;
        }
        return null;
    }
    
    public synchronized void removeReferencePoint(double x, double y) {
        points.remove(getReferencePoint(x, y));
    }
}
