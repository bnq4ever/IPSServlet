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
    //private ArrayList<MagneticFingerprint> fingerprints; 
    /*
    Particle:   x, y, direction, speed, weight    
    */
    
    public ParticleFilter(double x, double y) {
        for(int i = 0; i < 100; i++) {
            double angle = Math.random()*360;
            //particles.add(new double[]{x+Math.random()*(x-40), y+Math.random()*(y-40), angle, 10*Math.random()});
            x = Math.random()*1000;
            y = Math.random()*1000;
            particles.add(new Particle(x, y, angle, 10*Math.random(), 1)); 
        }
        for(Particle p : particles) {
            System.out.println("Hej: "+p.x);
        }
    }
    
    public MagneticFingerprint resetParticles(double timeDiff) {
        ArrayList<Particle> removed = new ArrayList<>();
        int nbrDeleted = 0;
        ArrayList<Particle> prioritized = new ArrayList<>();
        double highestWeight = Double.MIN_VALUE;
        int indexofhighest = 0;
        for(Particle p : particles) {
            if(p.weight > highestWeight) {
                indexofhighest = particles.indexOf(p);
            }
            if(p.weight < 0.05) {
                removed.add(p);
                nbrDeleted++;
            }else if(p.weight > 0.2) {
                prioritized.add(p);
            }
            //System.out.println("Weight: "+p.weight);
            p.weight = 1;
        }
        prioritized.add(particles.get(indexofhighest));
        particles.removeAll(removed);
        for(int i = 0; i < nbrDeleted; i++) {
//            if(i < prioritized.size()) {
                double angle = Math.random()*360;
                particles.add(new Particle(prioritized.get(i%prioritized.size()).x, prioritized.get(i%prioritized.size()).y, angle, 10*Math.random(), 1));
//            }           
        }
        moveParticles(timeDiff);
        return prioritized.get(0).closestFingerprint;
    }
    
    public void moveParticles(double timeDiff) {
        System.out.println("timeDiff: "+timeDiff);
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
    
    public void updateWeights(MagneticFingerprint measurement) {
        for(Particle p : particles) {
           p.weight = Math.sqrt(Math.pow(p.x - measurement.x, 2) + Math.pow(p.y - measurement.y, 2));       
           p.weight = 100/p.weight; //Normalize weights.
        }
    }
    
    public ArrayList<Particle> getParticles() {
        return particles;
    }
    
    
    public class Particle {
        public double x;
        public double y;
        public double direction;
        public double speed;
        public double weight;
        public MagneticFingerprint closestFingerprint;
        
        private Particle(double x, double y, double direction, double speed, double weight) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.speed = speed;
            this.weight = weight;
            closestFingerprint = new MagneticFingerprint(0, 0, 0, 0, 0);
        }
        
        public void setClosestFingerprint(MagneticFingerprint f) {
            closestFingerprint = f;
        }
    }    
}
