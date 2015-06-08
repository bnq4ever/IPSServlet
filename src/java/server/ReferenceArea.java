package server;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Hampus on 2015-02-10.
 */
public class ReferenceArea {
    
    public double x;
    public double y;
    public double CANDIDATES_TRESHOLD;
    
    public Map<String, Double> fingerprint;
    public String BTSSID;
    public ArrayList<MagneticPoint> magneticPoints;

    public ReferenceArea(double x, double y, Map fingerprint){
        this.x = x;
        this.y = y;
        this.fingerprint = fingerprint;
        magneticPoints = new ArrayList<>();
        //magnetics = Generate.MagneticFingerprints();
    }
    
    public void addMagneticPoint(MagneticPoint magneticPoint) {
        magneticPoints.add(magneticPoint);
    }
    
    public ArrayList<MagneticPoint> getMagneticPoints() {
        //System.out.println(magneticPoints.size());
        return magneticPoints;
    }
    
    public String getFingerprint() {
        StringBuilder sb = new StringBuilder();

        for (String key : fingerprint.keySet()) {
            sb.append(key).append("/").append(fingerprint.get(key)).append(";");
        }
        
        sb.deleteCharAt(sb.length() - 1);
        
        return sb.toString();
    }
    
    public String getBTSSID() {
        return BTSSID;
    }
}
