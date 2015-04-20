/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;

/**
 *
 * @author fredrik
 */
public class ParticleFilter {
    //private ArrayList<double[]> particles = new ArrayList<double[]>();
    private ArrayList<Particle> particles = new ArrayList<Particle>();
    /*
    Particle:   x, y, direction, speed, weight    
    */
    
    public ParticleFilter() {
        
    }
    
    public void resetParticles(double x, double y) {
        for(int i = 0; i < 100; i++) {
            double angle = Math.random()*360;
            //particles.add(new double[]{x+Math.random()*(x-40), y+Math.random()*(y-40), angle, 10*Math.random()});
            particles.add(new Particle(x+Math.random()*(x-40), y+Math.random()*(y-40), angle, 10*Math.random(), 1));
        }
    }
    
    public void moveParticles(double timeDiff) {
        //for(double[] p : particles) {
        for(Particle p : particles) {
            p.x = timeDiff*p.speed*Math.cos(Math.toRadians(p.direction));
            p.y = timeDiff*p.speed*Math.sin(Math.toRadians(p.direction));
        }
    }
    
    public void findClosestFingerprints(ArrayList<MagneticFingerprint> fingerprints) {
        for(Particle p : particles) {
            double shortest = Double.MAX_VALUE;
            for(MagneticFingerprint f : fingerprints) {
                double dist = Math.sqrt(Math.pow(p.x-f.x, 2) + Math.pow(p.y-f.y, 2));
                if(dist < shortest) {
                    shortest = dist;
                    p.setClosestFingerprint(f);
                }
            }
        }
    }
    
    public void updateWeights() {
        
    }
    
    
    private class Particle {
        public double x;
        public double y;
        public double direction;
        public double speed;
        public double weight;
        public MagneticFingerprint closestFingerprint;
        
        private Particle(double x, double y, double direction, double speed, double weight) {
            this.x = x;
            this.x = y;
            this.x = direction;
            this.x = speed;
            this.x = weight;
        }
        
        public void setClosestFingerprint(MagneticFingerprint f) {
            closestFingerprint = f;
        }
    }    
}
