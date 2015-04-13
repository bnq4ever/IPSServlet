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
    
    public synchronized ReferencePoint locateReferenceArea(String MAC, HashMap<String, Double> fingerprint) {
        ArrayList<ReferencePoint> relevantPoints = RadioMap.getInstance().getRelevantPoints(fingerprint);
        //System.out.println("relevantPoints.size(): " + relevantPoints.size());
        System.out.println(fingerprint.size());
        for (String key : fingerprint.keySet()) {
            System.out.println(key + " " + fingerprint.get(key));
        }
        double distance = Integer.MAX_VALUE;
        ReferencePoint result = null;
        for (ReferencePoint p : relevantPoints) {
            double pointDistance = getRSSEuclidean(fingerprint, p);
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
    
    private synchronized double getRSSEuclidean(HashMap<String, Double> fingerprint, ReferencePoint p) {
        double pointDistance = 0;
        //System.out.println("euclidian");
        for ( String key : fingerprint.keySet() ) {
//            System.out.println(fingerprint.get(key) + " - " + p.fingerprint.get(key));
            pointDistance += Math.pow(fingerprint.get(key) - p.RSSfingerprint.get(key), 2);
        }
        return Math.sqrt(pointDistance);
    }
    
    /*
        Locates device using both RSS and magnetic. Requires both RSS and magnetic as parameters.
    */
    public synchronized MagneticFingerprint locateDevice(String MAC, HashMap<String, Double> RSSfingerprint, float magnitude, float zaxis, float xyaxis, ArrayList<MagneticFingerprint> magneticfingerprints/*, ArrayList<Double> priorities*/) {
        ReferencePoint area = locateReferenceArea(MAC, RSSfingerprint);
        ArrayList<Double> priorities = new ArrayList<Double>();
        for(MagneticFingerprint f : magneticfingerprints) {
            priorities.add(1.0);
        }
        MagneticFingerprint location = getNearestMagnetic(magnitude, zaxis, xyaxis, magneticfingerprints, priorities);
        return location;
    }
    
    
    /*
        Euclidean from magneticfingerprint to other magneticfingerprints. Returns nearest MagneticFingerprint
    */
    private synchronized MagneticFingerprint getNearestMagnetic(float magnitude, float zaxis, float xyaxis, ArrayList<MagneticFingerprint> fingerprints, ArrayList<Double> priorities) {
        float compare = Float.MAX_VALUE;
        MagneticFingerprint nearest = new MagneticFingerprint(90, 1000, 0, 0, 0);
        float distance = 0;
        //for (Fingerprint fp : fingerprints) {
        for(int i = 0; i < fingerprints.size(); i++) {
            distance = 0;
            distance += Math.pow((magnitude - fingerprints.get(i).magnitude), 2);
            distance += Math.pow((zaxis - fingerprints.get(i).zaxis), 2);
            distance += Math.pow((xyaxis - fingerprints.get(i).xyaxis), 2);
            distance = (float) Math.sqrt(distance);
            distance = (float) (distance*priorities.get(i));
            if (distance < compare) {
                nearest = fingerprints.get(i);
                compare = distance;
            }
        }
        return nearest;
    }
}
