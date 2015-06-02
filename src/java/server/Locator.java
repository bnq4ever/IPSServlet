/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 *
 * @author Hampus
 */
public class Locator {
    private static Locator _instance;
    private HashMap<String, ArrayList<MagneticPoint>> bestCandidates;
    //private ArrayList<MagneticPoint> bestCandidates;
    private static double CANDIDATES_TRESHOLD = 25; //magnetude only: 2    3-size vector: 8
    public static synchronized Locator getInstance() {
        if(_instance == null)
            _instance = new Locator();
        return _instance;
    }
    
    public Locator() {
        bestCandidates = new HashMap<>();
    }
    
    public ArrayList<MagneticPoint> getBestCandidates(String deviceId) {
        return bestCandidates.get(deviceId);
    }
    
    public synchronized ReferenceArea locateReferenceArea(String deviceId, HashMap<String, Double> areaFingerprint) {
        ArrayList<ReferenceArea> candidates = RadioMap.getInstance().getRelevantAreas(
                DeviceManager.getInstance().getDevice(deviceId).getX(), 
                DeviceManager.getInstance().getDevice(deviceId).getY(),
                areaFingerprint);
        
        double bestDistance = Integer.MAX_VALUE;
        ReferenceArea bestCandidate = null;
        
        for (ReferenceArea candidate : candidates) {
            double candidateDistance = getRSSEuclidean(areaFingerprint, candidate);
            System.out.println("x: " + candidate.x + ", y: " + candidate.y + " distance: " + candidateDistance);
            if ( candidateDistance < bestDistance ) {
                bestDistance = candidateDistance;
                bestCandidate = candidate;
            }
        }
        System.out.println("BEST POINT " + "x: " + bestCandidate.x + " y: " + bestCandidate.y);
        System.out.println(" ");
        return bestCandidate;
    }
    
    private synchronized double getRSSEuclidean(HashMap<String, Double> fingerprint, ReferenceArea candidate) {
        double candidateDistance = 0;
        //System.out.println("euclidian");
        for ( String key : fingerprint.keySet() ) {
            if(candidate.fingerprint.get(key) != null) {
                candidateDistance += Math.pow(fingerprint.get(key) - candidate.fingerprint.get(key), 2);
            } else {
                candidateDistance += Math.pow(fingerprint.get(key) - (-90), 2); //unknown APs
            }
        }
        return Math.sqrt(candidateDistance);
    }
    

    public synchronized void updatePosition(String deviceId, double[] fingerprint) {
        ArrayList<MagneticPoint> bestLocations = getNearestMagnetic(deviceId, fingerprint);
        MagneticPoint filteredlocation; // = bestLocations.get(0);
        filteredlocation = DeviceManager.getInstance().getDevice(deviceId).getFilter().estimate(bestLocations, DeviceManager.getInstance().getDevice(deviceId).getReferenceArea().getMagneticPoints());
        if(filteredlocation != null) {
            DeviceManager.getInstance().setPosition(deviceId, filteredlocation);
        }
    }
    
    
    /*
        Euclidean from magneticfingerprint to other magneticfingerprints. Returns nearest MagneticFingerprint.
    
        UPDATE: Method returns the top 5 magneticfingerprints. 
    */
    private synchronized ArrayList<MagneticPoint> getNearestMagnetic(String deviceId, double[] magneticFingerprint) {
        double magnitude = magneticFingerprint[0];
        double zaxis = magneticFingerprint[1];
        double xyaxis = magneticFingerprint[2];
        TreeMap<Double, MagneticPoint> map = new TreeMap<>();

        ArrayList<MagneticPoint> magneticPoints = DeviceManager.getInstance().getDevice(deviceId).getReferenceArea().getMagneticPoints();

        double compare = Float.MAX_VALUE;
//        MagneticPoint[] bestCandidates = new MagneticPoint[5];
//        ArrayList<MagneticPoint> bestCandidates = DeviceManager.getInstance().getDevice(deviceId).bestCandidates;
        bestCandidates.put(deviceId, new ArrayList<MagneticPoint>());
        double distance;
        for(MagneticPoint point : magneticPoints) {
            distance = 0;
            distance += Math.pow((magnitude - point.magnitude), 2);
            //distance += Math.pow((zaxis - point.zaxis), 2);
            //distance += Math.pow((xyaxis - point.xyaxis), 2);
            distance = (double) Math.sqrt(distance);
            map.put(distance, point);
        }
        System.out.println("Checking first key: "+map.firstKey()+"\nLast key is: "+map.lastKey());
        for(int i = 0; i < map.size(); i++) {
            if(map.firstKey() < DeviceManager.getInstance().getDevice(deviceId).getReferenceArea().CANDIDATES_TRESHOLD) {
                bestCandidates.get(deviceId).add(map.pollFirstEntry().getValue());
            }else{
                break;
            }
        }
//        bestCandidates.get(deviceId).add(map.pollFirstEntry().getValue());
//        bestCandidates.get(deviceId).add(map.pollFirstEntry().getValue());
//        bestCandidates.get(deviceId).add(map.pollFirstEntry().getValue());
        System.out.println("Number of candidates: "+bestCandidates.get(deviceId).size());
        return bestCandidates.get(deviceId);
    }
}
