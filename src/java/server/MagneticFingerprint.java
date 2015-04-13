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
class MagneticFingerprint {
    public float x, y;
    //private float[] orientation;
    public float magnitude, zaxis, xyaxis;
    
    public MagneticFingerprint(float x, float y, float magnitude, float zaxis, float xyaxis) {
        this.x = x;
        this.y = y;
        this.magnitude = magnitude;
        this.zaxis = zaxis;
        this.xyaxis = xyaxis;
    }
    
    /*
    
    public float getX() {
        return X;
    }
    public float getY() {
        return Y;
    }

    public float getMagnitude() {
        return magnitude;
    }
    public float getZ() {
        return zaxis;
    }
    public float getXY() { 
        return xyaxis; 
    }
    */
}
