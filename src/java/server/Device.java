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
class Device {
    private String name;
    private final String id;
    private double x;
    private double y;
    private String color;
    //private ArrayList<MagneticPoint> previousPoints;//HISTORICAL COORDINATES
    //private double direction;
    private ReferenceArea referenceArea;
    private ParticleFilter filter;
    public double lastMeasurement = 0;
    public ArrayList<MagneticPoint> bestCandidates;
    
    public Device(String id) {
        this.id = id;
        this.name = "";
        //filter = new ParticleFilter(0,0);
        color = Device.generateColor();
    }
    
    public Device(String id, String name) {
        this.id = id;
        this.name = name;
        //filter = new ParticleFilter(0,0);
        color = Device.generateColor();
    }
    
    public String getColor() {
        return color;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getId() {
        return this.id;
    }
    
    public double getX() {
        return this.x;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public String getID() {
        return id;
    }
    
    public void setReferenceArea(ReferenceArea referenceArea) {
        this.referenceArea = referenceArea;
    }
    
    public ReferenceArea getReferenceArea() {
        return this.referenceArea;
    }
    
    
    void startFilter() {
        filter = new ParticleFilter(0,0);
    }
    
    public ParticleFilter getFilter() {
        return filter;
    }
    
    public static String generateColor() {
        String[] letters = "0123456789ABCDEF".split("");
        String color = "#";
        for (int i = 0; i < 6; i++) {
            color += letters[(int)Math.floor(Math.random()*16)];
        }
        return color;
    }
    
}
