/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author Hampus
 */

public class Particle {
    public double x;
    public double y;
    public double direction;
    public double speed;
    public double weight;
    public MagneticFingerprint closestFingerprint;

    public Particle(double x, double y, double direction, double speed, double weight) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = speed;
        this.weight = weight;
    }
    
    public void setClosestFingerprint(MagneticFingerprint fingerprint) {
        this.closestFingerprint = fingerprint;
    }

}
