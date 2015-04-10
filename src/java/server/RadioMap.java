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
    
    public synchronized ArrayList<ReferencePoint> getPoints() {
        ArrayList<ReferencePoint> tmp = new ArrayList<>(points);
        return tmp;
    }
    
    public synchronized ArrayList<ReferencePoint> getRelevantPoints(HashMap<String, Double> deviceFingerprint) {
        ArrayList<ReferencePoint> result = new ArrayList<>();
        for (ReferencePoint p : points) {
            for (String key : deviceFingerprint.keySet()) {
                if (!p.fingerprint.containsKey(key))
                    break;
            }
            result.add(p);
        }
        return result;
    }
    
    public synchronized void addPoint(ReferencePoint point) {
        ReferencePoint p = getPoint(point.x, point.y);
        if (p != null)
            p.fingerprint = point.fingerprint;
        else
            points.add(point);
        StringBuilder sb = new StringBuilder();
        for (String key : point.fingerprint.keySet()) {
            sb.append(key).append(" ").append(point.fingerprint.get(key));
        }
        System.out.println(sb.toString());
    }
    
    public synchronized ReferencePoint getPoint(double x, double y) {
        for (ReferencePoint p : points) {
            if(x == p.x && y == p.y)
                return p;
        }
        return null;
    }
    
    public synchronized void removePoint(double x, double y) {
        points.remove(getPoint(x, y));
    }
}
