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
        ArrayList<ReferencePoint> relevantPoints = RadioMap.getInstance().getRelevantPoints(DeviceManager.getInstance().getDevice(MAC).getX(), DeviceManager.getInstance().getDevice(MAC).getY(),fingerprint);
        //ArrayList<ReferencePoint> relevantPoints = RadioMap.getInstance().getReferencePoints();
        System.out.println(fingerprint.size());
        for (String key : fingerprint.keySet()) {
            System.out.println(key + " " + fingerprint.get(key));
        }
        double distance = Integer.MAX_VALUE;
        ReferencePoint result = null;
        for (ReferencePoint point : relevantPoints) {
            double pointDistance = getRSSEuclidean(fingerprint, point);
            //System.out.println(p.getFingerprint());
            System.out.println("x: " + point.x + " y: " + point.y + " distance: " + pointDistance);
            if ( pointDistance < distance ) {
                distance = pointDistance;
                result = point;
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
            if(p.fingerprint.get(key) != null) {
                pointDistance += Math.pow(fingerprint.get(key) - p.fingerprint.get(key), 2);
            }
            /*
            else {
                pointDistance += Math.pow(fingerprint.get(key) + 90, 2); //unknown APs
            }
            */
        }
        return Math.sqrt(pointDistance);
    }
    
    /*
        Locates device using both RSS and magnetic. Requires both RSS and magnetic as parameters.
    */
    public synchronized void updatePosition(String MAC, double[] fingerprint) {
        MagneticFingerprint location = getNearestMagnetic(MAC, fingerprint);
        DeviceManager.getInstance().updatePosition(MAC, location);
    }
    
    
    /*
        Euclidean from magneticfingerprint to other magneticfingerprints. Returns nearest MagneticFingerprint
    */
    private synchronized MagneticFingerprint getNearestMagnetic(String MAC, double[] fingerprint) {
        double magnitude = fingerprint[0];
        double zaxis = fingerprint[1];
        double xyaxis = fingerprint[2];
        //System.out.println(DeviceManager.getInstance().getDevice(MAC).getReferencePoint());
        ArrayList<MagneticFingerprint> fingerprints = DeviceManager.getInstance().getDevice(MAC).getReferencePoint().getMagnetics();
        double compare = Float.MAX_VALUE;
        MagneticFingerprint nearest = new MagneticFingerprint(90, 1000, 0, 0, 0);
        double distance;
        //for (Fingerprint fp : fingerprints) {
        for(int i = 0; i < fingerprints.size(); i++) {
            distance = 0;
            distance += Math.pow((magnitude - fingerprints.get(i).magnitude), 2);
            distance += Math.pow((zaxis - fingerprints.get(i).zaxis), 2);
            distance += Math.pow((xyaxis - fingerprints.get(i).xyaxis), 2);
            distance = (double) Math.sqrt(distance);
//            distance = (float) (distance*priorities.get(i));
            if (distance < compare) {
                nearest = fingerprints.get(i);
                compare = distance;
            }
        }
        return nearest;
    }
}
