/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;

/**
 *
 * @author Hampus
 */
public class Generate {
    
    public static ArrayList<MagneticPoint> MagneticFingerprints() {
        ArrayList<MagneticPoint> list = new ArrayList<>();
        for( int row = 0; row < 10; row++ ) {
            for(int col = 0; col < 10; col++) {
                list.add(new MagneticPoint(col*10, row*10, value(), value(), value()));
            }
        }
        return list;
    }
    
    public static double value() {   
        return (double)(Math.random() * 10) + 50;
    }
}
