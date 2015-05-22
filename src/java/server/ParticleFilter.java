/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 *
 * @author fredrik
 */
public class ParticleFilter {
    //private ArrayList<double[]> particles = new ArrayList<double[]>();
    private final ArrayList<Particle> particles;
    private static final double SCATTER = 3;
    private static final double LOWER_TRESHOLD = 0.89;
    private static final double UPPER_TRESHOLD = 0.97;
    private double SCATTER_DIRECTION = 0;

    private static final int MAP_WIDTH = 1200;
    private static final int MAP_HEIGHT = 1200;
    private static final int NBR_OF_PARTICLES = 300;
    
    //private ArrayList<MagneticFingerprint> fingerprints;

    
    /*
    Particle:   x, y, direction, speed, weight    
    */
    
    public ParticleFilter(double x, double y) {
        particles = new ArrayList<>();
        for(int i = 0; i < NBR_OF_PARTICLES; i++) {
            double angle = Math.random()*360;
            //particles.add(new double[]{x+Math.random()*(x-40), y+Math.random()*(y-40), angle, 10*Math.random()});
            x = Math.random()*MAP_WIDTH;
            y = Math.random()*MAP_HEIGHT;
            particles.add(new Particle(x, y, angle, SCATTER*Math.random(), 1));
        }
    }
    
    public MagneticPoint estimate(ArrayList<MagneticPoint> locations, ArrayList<MagneticPoint> fingerprints) {
        if(locations.isEmpty()) {
            moveParticles();
            return null;
        }else{
            updateWeights(locations);
            findClosestPoints(fingerprints);
            return resampleParticles();
        }  
    }
    
    /*
        Resampling particles.
    */
    public MagneticPoint resampleParticles() {    
        ArrayList<Particle> removed = new ArrayList<>();
        int nbrDeleted = 0; //antal particlar som tas bort. Samma som removed.size().
        ArrayList<Particle> prioritized = new ArrayList<>();
        double highestWeight = Double.MIN_VALUE;
        int indexofhighest = 0;
        for(Particle p : particles) {
            if(p.weight > highestWeight) {
                indexofhighest = particles.indexOf(p);
                highestWeight = p.weight;
            }
            if(p.weight < LOWER_TRESHOLD) {
                if(p.kill()) {
                    removed.add(p);
                    nbrDeleted++;
                }
            } else if (p.weight > UPPER_TRESHOLD) {
                prioritized.add(p);
            }
            p.weight = 1;
        }
        System.out.println("nbr of prioritized: " + prioritized.size() + " nbr of removed: "+removed.size());
        prioritized.add(particles.get(indexofhighest));
        particles.removeAll(removed);
        for(int i = 0; i < nbrDeleted; i++) {
                double angle = Math.random()*360;
                particles.add(new Particle(prioritized.get(i%prioritized.size()).x, prioritized.get(i%prioritized.size()).y, angle, SCATTER*Math.random(), 0));          
        }
        moveParticles();
        return filteredLocation(prioritized);
        //return toReturn;
    }
    
    /*
            returns a filtered location.
    */
    public MagneticPoint filteredLocation(ArrayList<Particle> prios) {
        MagneticPoint toReturn = null;
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
            //p.direction = Math.random()*360;
            p.x += p.speed*Math.cos(Math.toRadians(p.direction));
            p.y += p.speed*Math.sin(Math.toRadians(p.direction));
        }  
    }
    
    public void findClosestPoints(ArrayList<MagneticPoint> fingerprints) {
        for(Particle p : particles) {
            double shortest = Double.MAX_VALUE;
            for(MagneticPoint f : fingerprints) {
                double dist = Math.sqrt(Math.pow(p.x-f.x, 2) + Math.pow(p.y-f.y, 2));
                if(dist < shortest) {
                    shortest = dist;
                    p.closestFingerprint = f;
                }
            }
        }
    }
    
    public void updateWeights(ArrayList<MagneticPoint> measurements) {
        double dist = 0;
        MagneticPoint calcWeight = null;
        for(Particle p : particles) {
            //dist = Double.MAX_VALUE;
            for(MagneticPoint m : measurements) {
                double euclidean = Math.sqrt(Math.pow(p.x - m.x, 2) + Math.pow(p.y - m.y, 2));
                if(euclidean > dist) {
                    dist = euclidean;
                    calcWeight = m;
                }
            }
        }
        
        for(Particle p : particles) {
            double max = Double.MAX_VALUE;
            for(MagneticPoint m : measurements) {
                double euclidean = Math.sqrt(Math.pow(p.x - m.x, 2) + Math.pow(p.y - m.y, 2));
                if(euclidean < max) {
                    max = euclidean;
                    calcWeight = m;
                }
            }
                p.weight = 1 - (Math.sqrt(Math.pow(p.x - calcWeight.x, 2) + Math.pow(p.y - calcWeight.y, 2)) / dist);
        }
        
        /*
        for(MagneticFingerprint m : measurements) {
            double dist = 0;
            for(Particle p : particles) {
                double euclidean = Math.sqrt(Math.pow(p.x - m.x, 2) + Math.pow(p.y - m.y, 2));
                if(euclidean > dist) {
                    dist = euclidean;
                }
            }
            for(Particle p : particles) {
                p.weight += 1 - (Math.sqrt(Math.pow(p.x - m.x, 2) + Math.pow(p.y - m.y, 2)) / dist);
            }
        }
        for(Particle p : particles) {
                p.weight = p.weight / measurements.length;
                System.out.println("PARTICLE WEIGHT: "+p.weight);
        } 
        */
    }
    
    public ArrayList<Particle> getParticles() {
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

        public MagneticPoint closestFingerprint;
        private int lives = 4;
        private int prioritity = 0;
        
        Particle(double x, double y, double direction, double speed, double weight) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.speed = speed;
            this.weight = weight;
            closestFingerprint = new MagneticPoint(0, 0, 0, 0, 0);
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
}
