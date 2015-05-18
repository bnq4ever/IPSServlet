/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import server.ParticleFilter.Particle;

/**
 *
 * @author fredrik
 */
public class ParticleFilterSet {
    //private ArrayList<double[]> particles = new ArrayList<double[]>();
    private TreeSet<Particle> particles;
    private static final double SCATTER = 2;
    private static final double LOWER_TRESHOLD = 0.2;
    private static final double UPPER_TRESHOLD = 0.9;

    private static final int MAP_WIDTH = 1200;
    private static final int MAP_HEIGHT = 1200;
    private static final int NBR_OF_PARTICLES = 200;
    private double normalizer;
    
    //private ArrayList<MagneticFingerprint> fingerprints;

    
    /*
    Particle:   x, y, direction, speed, weight    
    */
    
    public ParticleFilterSet(double x, double y) {
        particles = new TreeSet<>(new WeightComparator());
        for(int i = 0; i < NBR_OF_PARTICLES; i++) {
            double angle = Math.random()*360;
            //particles.add(new double[]{x+Math.random()*(x-40), y+Math.random()*(y-40), angle, 10*Math.random()});
            x = Math.random()*MAP_WIDTH;
            y = Math.random()*MAP_HEIGHT;
            particles.add(new Particle(x, y, angle, SCATTER*Math.random(), 1));
        }
    }
    
    public MagneticFingerprint Estimate(MagneticFingerprint location, ArrayList<MagneticFingerprint> fingerprints) {
        //for(MagneticFingerprint f : locations) {
        updateWeights(location);
        findClosestFingerprints(fingerprints); 
        return resetParticles();
    }
    
    
    public MagneticFingerprint resetParticles() {
        ArrayList<Particle> removed = new ArrayList<>();
        int nbrDeleted = 0; //antal particlar som tas bort. Samma som removed.size().
        ArrayList<Particle> prioritized = new ArrayList<>();
        double highestWeight = Double.MIN_VALUE;
        int indexofhighest = 0;
        
        Iterator<Particle> itr = particles.iterator();
        
        while (itr.hasNext()){
            Particle p = itr.next();
            if (p.weight / normalizer < LOWER_TRESHOLD)
                itr.remove();
            //Placera om istället för att ta bort och skapa ny?
            
            itr.next();
        }
    }
        
        for(Particle p : particles) {
            if(p.weight > highestWeight) {
                indexofhighest = particles.indexOf(p);
                highestWeight = p.weight;
            }
            } else if (p.weight > UPPER_TRESHOLD) {
                prioritized.add(p);
            }
            p.weight = 1;
        }
        prioritized.add(particles.get(indexofhighest));
        for(int i = 0; i < nbrDeleted; i++) {
                double angle = Math.random()*360;
                particles.add(new Particle(prioritized.get(i%prioritized.size()).x, prioritized.get(i%prioritized.size()).y, angle, SCATTER*Math.random(), 0));          
        }
        moveParticles();
        return filteredLocation(prioritized);
        //return toReturn;
    }
    
    
    public MagneticFingerprint filteredLocation(ArrayList<Particle> prios) {
        MagneticFingerprint toReturn = null;
        int maxNeighbours = 0;
        //for(Particle prio : prios) {
          for(Particle prio : particles) {
            int counter = 0;
            for(Particle p : particles) {
                if(Math.sqrt(Math.pow(prio.x - p.x, 2) + Math.pow(prio.y - p.y, 2)) < SCATTER*10){
                    counter++;
                }
            }
            if(counter > maxNeighbours) {
                toReturn = prio.closestFingerprint;
                maxNeighbours = counter;
            }
        }
        return toReturn;
    }
    
    
    public void moveParticles() {
        //for(double[] p : particles) {
        for(Particle p : particles) {
            p.x += p.speed*Math.cos(Math.toRadians(p.direction));
            p.y += p.speed*Math.sin(Math.toRadians(p.direction));
        }  
    }
    
    public void findClosestFingerprints(ArrayList<MagneticFingerprint> fingerprints) {
        //Tänka om? 
        //Anamma Stefans idé om att tolka partikelfiltret och förenkla beräkningar.
        for(Particle p : particles) {
            double shortest = Double.MAX_VALUE;
            for(MagneticFingerprint f : fingerprints) {
                double dist = Math.sqrt(Math.pow(p.x - f.x, 2) + Math.pow(p.y-f.y, 2));
                if(dist < shortest) {
                    shortest = dist;
                    p.closestFingerprint = f;
                }
            }
        }
    }
    
    public void updateWeights(MagneticFingerprint measurement) {
        for(Particle p : particles)
            p.weight = Math.sqrt(Math.pow(p.x - measurement.x, 2) + Math.pow(p.y - measurement.y, 2));
        
        normalizer = particles.first().weight;
    }
    
    public TreeSet<Particle> getParticles() {
        return particles;
    }

    public synchronized JsonObject toJSON() {
        
        JsonArrayBuilder array = Json.createArrayBuilder();
        ArrayList<Particle> tmp = new ArrayList<>(particles);
        for (Particle p : tmp)
            array.add(Json.createObjectBuilder().add("x", p.x).add("y", p.y).add("weight", p.weight).add("direction", p.direction).add("speed", p.speed));
                    
        return Json.createObjectBuilder().add("particles", array).build();
    }   
    
    public class Particle {
        public double x;
        public double y;
        public double direction;
        public double speed;
        public double weight;
        public MagneticFingerprint closestFingerprint;
        private int lives = 3;
        private int prioritity = 0;
        
        Particle(double x, double y, double direction, double speed, double weight) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.speed = speed;
            this.weight = weight;
            closestFingerprint = new MagneticFingerprint(0, 0, 0, 0, 0);
        }
        
        public boolean kill() {
            lives--;
            return (lives <= 0);
        }
        /*
            This method is not used. might need later.
        */
        //public boolean prioritize() {
            //prioritization++;
            //return (prioritization >= 3);
        //}
    }
    
    public class WeightComparator implements Comparator<Particle> {

        @Override
        public int compare(Particle p1, Particle p2) {
            if (p1.weight > p2.weight)
                return 1;
            if (p1.weight < p2.weight)
                return -1;
            return 0;
        }
        
    }
}
