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
public class Parser {
    public static ArrayList parseMagnetics(String magneticString) {
        ArrayList<MagneticFingerprint> magnetics = new ArrayList<>();
        String[] split = magneticString.split(";");
        for(String s : split) {
            String[] data = s.split("/");
            //    ;x/y/magnitude/zaxis/xyaxis;
            
            System.out.println(Double.parseDouble(data[0]) + " " +  Double.parseDouble(data[1]) + " " + Double.parseDouble(data[2]) + " " + Double.parseDouble(data[3]) + " " + Double.parseDouble(data[4]));
            magnetics.add(new MagneticFingerprint(Double.parseDouble(data[0]), Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]), Double.parseDouble(data[4])));
        }
        return magnetics;
    }
    
    public static double[] parseMagneticFingerprint(String magneticString) {
        
        String[] data = magneticString.split("/");
        double[] magneticValues = new double[3];
        
            //    magnitude/zaxis/xyaxis
        magneticValues[0] = Double.parseDouble(data[0]);
        magneticValues[1] = Double.parseDouble(data[1]);
        magneticValues[2] = Double.parseDouble(data[2]);
        
        return magneticValues;
    }
    
    public static HashMap parseFingerprint(String fingerprintString) {
        String[] list = fingerprintString.split(";");
        HashMap<String, Double> fingerprint = new HashMap<>();
        for (String str : list) {
            String[] key_value = str.split("/");
            fingerprint.put(key_value[0], Double.parseDouble(key_value[1]));
        }
        return fingerprint;
    }
}
