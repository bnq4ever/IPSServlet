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
        ArrayList<MagneticFingerprint> magnetics = new ArrayList<MagneticFingerprint>();
        String[] split = magneticString.split(";");
        for(String s : split) {
            String[] data = s.split("/");
            //    ;x/y/magnitude/zaxis/xyaxis;
            magnetics.add(new MagneticFingerprint(Float.parseFloat(data[0]), Float.parseFloat(data[1]), Float.parseFloat(data[2]), Float.parseFloat(data[3]), Float.parseFloat(data[4])));
        }
        return magnetics;
    }
    
    public static HashMap parseFingerprint(String fingerprintString) {
        System.out.println(fingerprintString);
        String[] list = fingerprintString.split(";");
        HashMap<String, Double> fingerprint = new HashMap<>();
        for (String str : list) {
            String[] key_value = str.split("/");
            fingerprint.put(key_value[0], Double.parseDouble(key_value[1]));
        }
        return fingerprint;
    }
}
