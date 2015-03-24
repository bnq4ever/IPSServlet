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
public class DeviceManager {
    
    private static DeviceManager _instance;
    
    private static ArrayList<Device> devices = new ArrayList<>();
    private static ArrayList<Device> connectedDevices = new ArrayList<>();
    private static final Database db = new Database();
    
    public synchronized static DeviceManager getInstance() {
        if (_instance == null) _instance = new DeviceManager();
        return _instance;
    }
    
    private DeviceManager() {
        db.openConnection();
        devices = db.getAllDevices();
        db.closeConnection();
        System.out.println(devices.size());
    }
    
    public synchronized static boolean exists(String deviceId) {
        return devices.contains(new Device(deviceId));
    }
    
    public synchronized static void addExistence(String deviceId) {
        db.openConnection();
        db.addDeviceExistence(deviceId);
        db.closeConnection();
        
        devices.add(new Device(deviceId));
    }
    
    public synchronized static boolean isConnected(String deviceId) {
        return connectedDevices.contains(new Device(deviceId));
    }
    
    public synchronized static void connectDevice(String deviceId) {
        if (!isConnected(deviceId))
            connectedDevices.add(devices.get(devices.indexOf(new Device(deviceId))));
    }
    
    public synchronized static void disconnectDevice(String deviceId) {
        connectedDevices.remove(new Device(deviceId));
    }
    
    //Update DB data in interval or at shutdown
    public synchronized static void setDeviceName(String deviceId, String name) {
        db.openConnection();
        db.setDeviceName(deviceId, name);
        db.closeConnection();
        devices.get(devices.indexOf(new Device(deviceId))).setName(name);
    }
    
    public synchronized static ArrayList<Device> getConnectedDevices() {
        return new ArrayList<>(connectedDevices);
    }
    
    public synchronized static int nbrOfDevicesConnected() {
        return connectedDevices.size();
    }
    
    public synchronized static int nbrOfDevices() {
        return devices.size();
    }
    
    public synchronized static void moveDevice(String MAC, float angle, float stepLength) {
        Device device = connectedDevices.get(connectedDevices.indexOf(new Device(MAC)));
        
        float x = device.getX();
        float y = device.getY();
        
        x += (float) Math.cos(Math.toRadians(angle))*stepLength;
        y += (float) Math.sin(Math.toRadians(angle))*stepLength;
        
        device.setX(x);
        device.setY(y);
    }
}
