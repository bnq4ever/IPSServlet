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
    private ArrayList<Float> history;//HISTORICAL COORDINATES
    private float direction;
    
    public Device(String id) {
        this.id = id;
        this.name = "";
    }
    
    public Device(String id, String name) {
        this.id = id;
        this.name = name;
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
    
    public void setDirection(float direction) {
        this.direction = direction;
    }
    
    public float getDirection() {
        return direction;
    }
    
//    @Override
//    public boolean equals(Device device) {
//        return id.equals(device.getId());
//    }
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Device)) return false;
        Device other = (Device) o;
        return this.id.equals(other.getId());
    }
}
