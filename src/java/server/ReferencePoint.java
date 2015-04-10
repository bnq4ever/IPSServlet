package server;

import java.util.Map;

/**
 * Created by Hampus on 2015-02-10.
 */
public class ReferencePoint {
    
    public double x;
    public double y;
    
    public Map<String, Double> fingerprint;

    public ReferencePoint(double x, double y, Map fingerprint){
        
        this.x = x;
        this.y = y;
        this.fingerprint = fingerprint;
    
    }
    
    public String getFingerprint() {
        StringBuilder sb = new StringBuilder();

        for (String key : fingerprint.keySet()) {
            sb.append(key).append(": ").append(fingerprint.get(key)).append(" ");
        }
        
        return sb.toString();
    }
}
