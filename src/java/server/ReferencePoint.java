package server;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Hampus on 2015-02-10.
 */
public class ReferencePoint {
    
    public double x;
    public double y;
    
    public Map<String, Double> RSSfingerprint;
    public ArrayList<MagneticFingerprint> magnetics;

    public ReferencePoint(double x, double y, Map RSSfingerprint){
        this.x = x;
        this.y = y;
        this.RSSfingerprint = RSSfingerprint;
    }
    
    public void addMagnetic(MagneticFingerprint mf) {
        magnetics.add(mf);
    }
    
    public ArrayList<MagneticFingerprint> getMagnetics() {
        return magnetics;
    }
    
    public String getFingerprint() {
        StringBuilder sb = new StringBuilder();

        for (String key : RSSfingerprint.keySet()) {
            sb.append(key).append(": ").append(RSSfingerprint.get(key)).append(" ");
        }
        
        return sb.toString();
    }
}
