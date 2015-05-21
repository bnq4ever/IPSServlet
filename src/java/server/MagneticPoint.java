/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author fredrik
 */
class MagneticPoint {
    public double x, y, diff;
    //private float[] orientation;
    public double magnitude, zaxis, xyaxis;
    
    public MagneticPoint(double x, double y, double magnitude, double zaxis, double xyaxis) {
        this.x = x;
        this.y = y;
        this.magnitude = magnitude;
        this.zaxis = zaxis;
        this.xyaxis = xyaxis;
    }
    
    /*
    
    public double getX() {
        return X;
    }
    public double getY() {
        return Y;
    }

    public double getMagnitude() {
        return magnitude;
    }
    public double getZ() {
        return zaxis;
    }
    public double getXY() { 
        return xyaxis; 
    }
    */
}
