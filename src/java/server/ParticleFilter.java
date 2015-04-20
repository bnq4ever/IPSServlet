/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

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
    
    public ParticleFilter(double x, double y) {
        for(int i = 0; i < 100; i++) {
            double angle = Math.random()*360;
            //particles.add(new double[]{x+Math.random()*(x-40), y+Math.random()*(y-40), angle, 10*Math.random()});
            particles.add(new Particle(x+Math.random()*(x-40), y+Math.random()*(y-40), angle, 10*Math.random(), 1));
        }
    }
    
    public void resetParticles(double x, double y) {
        //ArrayList<Particle> removed = new ArrayList<>();
        int nbrDeleted = 0;
        ArrayList<Particle> prioritized = new ArrayList<>();
        for(Particle p : particles) {
            if(p.weight < 0.5) {
                particles.remove(p);
                nbrDeleted++;
            }else if(p.weight > 0.8) {
                prioritized.add(p);
            }
            p.weight = 1;
        }
        
        for(int i = 0; i < nbrDeleted; i++) {
            if(i < prioritized.size()-1) {
                double angle = Math.random()*360;
                particles.add(new Particle(prioritized.get(i).x, prioritized.get(i).y, angle, 10*Math.random(), 1));
            }else{
                i=0;
            }
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
    
    public void updateWeights(MagneticFingerprint measurement) {
        for(Particle p : particles) {
           p.weight = Math.sqrt(Math.pow(p.x - measurement.x, 2) + Math.pow(p.y - measurement.y, 2));
           p.weight = 1 / particles.size(); //Normalize weights.
        }
    }
    
    public ArrayList<Particle> getParticles() {
        return particles;
    }
    
    public JsonObject toJSON() {
        
        JsonArrayBuilder array = Json.createArrayBuilder();

        for (Particle p : particles) {
            array.add(Json.createObjectBuilder().add("x", p.x).add("y", p.y).add("weight", p.weight).add("direction", p.direction).add("speed", p.speed));
        }
        
        return Json.createObjectBuilder().add("particles", array).build();
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
