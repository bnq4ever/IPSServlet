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
public class Locator {
    private static Locator _instance;
    
    public static synchronized Locator getInstance() {
        if(_instance == null)
            _instance = new Locator();
        return _instance;
    }
    
    public Locator() {
        
    }
    
    public synchronized ReferencePoint locateDevice(String MAC, HashMap<String, Double> fingerprint) {
        ArrayList<ReferencePoint> relevantPoints = RadioMap.getInstance().getRelevantPoints(fingerprint);
        //System.out.println("relevantPoints.size(): " + relevantPoints.size());
        System.out.println(fingerprint.size());
        for (String key : fingerprint.keySet()) {
            System.out.println(key + " " + fingerprint.get(key));
        }
        double distance = Integer.MAX_VALUE;
        ReferencePoint result = null;
        for (ReferencePoint p : relevantPoints) {
            double pointDistance = getEuclidean(fingerprint, p);
            //System.out.println(p.getFingerprint());
            System.out.println("x: " + p.x + " y: " + p.y + " distance: " + pointDistance);
            if ( pointDistance < distance ) {
                distance = pointDistance;
                result = p;
            }
        }
        
        System.out.println("BEST POINT");
        System.out.println("x: " + result.x + " y: " + result.y);
        return result;
    }
    
    private synchronized double getEuclidean(HashMap<String, Double> fingerprint, ReferencePoint p) {
        double pointDistance = 0;
        //System.out.println("euclidian");
        for ( String key : fingerprint.keySet() ) {
//            System.out.println(fingerprint.get(key) + " - " + p.fingerprint.get(key));
            pointDistance += Math.pow(fingerprint.get(key) - p.fingerprint.get(key), 2);
        }
        return Math.sqrt(pointDistance);
    }
}
