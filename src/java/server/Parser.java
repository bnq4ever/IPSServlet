/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.HashMap;

/**
 *
 * @author Hampus
 */
public class Parser {
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
