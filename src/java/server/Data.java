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
public class Data {
    private String key; 
    private double value;
    
    public Data(String key, double value) {
        this.key = key; 
        this.value = value;
    }
    
    public String getKey() {
        return key;
    }
    
    public double getValue() {
        return value;
    }
}
