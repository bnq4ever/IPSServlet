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
class Device {
    private String name;
    private final String id;
    private double x;
    private double y;
    //private ArrayList<MagneticPoint> previousPoints;//HISTORICAL COORDINATES
    //private double direction;
    private ReferenceArea referenceArea;
    private ParticleFilter filter;
    public double lastMeasurement = 0;
    
    public Device(String id) {
        this.id = id;
        this.name = "";
        //filter = new ParticleFilter(0,0);
    }
    
    public Device(String id, String name) {
        this.id = id;
        this.name = name;
        //filter = new ParticleFilter(0,0);
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
    
//    public void setDirection(double direction) {
//        this.direction = direction;
//    }
    
//    public double getDirection() {
//        return direction;
//    }
    
    public void setReferenceArea(ReferenceArea referenceArea) {
        this.referenceArea = referenceArea;
    }
    
    public ReferenceArea getReferenceArea() {
        return this.referenceArea;
    }
    
//    public void addPreviousPosition(MagneticPoint fingerprint) {
//        previousPoints.add(fingerprint);
//    }
    
    
    void startFilter() {
        filter = new ParticleFilter(0,0);
    }
    
    public ParticleFilter getFilter() {
        return filter;
    }
   
    
//    @Override
//    public boolean equals(Device device) {
//        return id.equals(device.getId());
//    }
    
//    @Override
//    public boolean equals(Object o) {
//        if(!(o instanceof Device)) return false;
//        Device other = (Device) o;
//        return this.id.equals(other.getId());
//    }
}
