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
    private float x;
    private float y;
    private ArrayList<Float> history;//HISTORICAL COORDINATES
    
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
    
    public float getX() {
        return this.x;
    }
    
    public void setX(float x) {
        this.x = x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public void setY(float y) {
        this.y = y;
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
