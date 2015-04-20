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
public final class DeviceManager {
    
    private static DeviceManager _instance;
    
    private final ArrayList<Device> devices = new ArrayList<>();
    private final ArrayList<Device> connectedDevices = new ArrayList<>();
    private final Database db = new Database();
    
    public synchronized static DeviceManager getInstance() {
        if (_instance == null) 
            _instance = new DeviceManager();
        return _instance;
    }
    
    private DeviceManager() {
//        db.openConnection();
//        devices = db.getAllDevices();
//        db.closeConnection();
//        System.out.println(devices.size());
    }
    
    public synchronized boolean exists(String deviceId) {
        //System.out.println("exists");
        return devices.contains(new Device(deviceId));
    }
    
    public synchronized void addDevice(String deviceId, String name) {
        //System.out.println("addDevice");
//        db.openConnection();
//        db.addDevice(deviceId, name);
//        db.closeConnection();
        
        devices.add(new Device(deviceId, name));
    }
    
    public synchronized boolean isConnected(String deviceId) {
        //System.out.println("isConnected");
        return connectedDevices.contains(getDevice(deviceId));
    }
    
    public synchronized boolean isIntroduced(String deviceId) {
        //System.out.println("isIntroduced");
//        db.openConnection();
//        boolean isIntroduced = db.isIntroduced(deviceId);
//        db.closeConnection();
        if (!exists(deviceId))
            return false;
        //System.out.println(getDevice(deviceId).getName());
        return !getDevice(deviceId).getName().equals("");
    }
    
    public synchronized String connectDevice(String deviceId) {
        //System.out.println("connectDevice");
        if (!exists(deviceId))
            return Command.DEVICE_NOT_EXISTING;
        else if (isConnected(deviceId))
            return Command.DEVICE_ALREADY_CONNECTED;
        
        connectedDevices.add(getDevice(deviceId));
        return Command.DEVICE_CONNECTED;
    }
    
    public synchronized String disconnectDevice(String deviceId) {
        //System.out.println("disconnectDevice");
        if (!exists(deviceId))
            return Command.DEVICE_NOT_EXISTING;
        else if (!isConnected(deviceId))
            return Command.DEVICE_ALREADY_DISCONNECTED;
        
        connectedDevices.remove(getDevice(deviceId));
        return Command.DEVICE_DISCONNECTED;
    }
    
    public synchronized Device getDevice(String deviceId) {
        return devices.get(devices.indexOf(new Device(deviceId)));
    }
    
    //Update DB data in interval or at shutdown
    public synchronized void setDeviceName(String deviceId, String name) {
        //System.out.println("setDeviceName");
//        db.openConnection();
//        db.setDeviceName(deviceId, name);
//        db.closeConnection();
        getDevice(deviceId).setName(name);
    }
    
    public synchronized ArrayList<Device> getConnectedDevices() {
        return new ArrayList<>(connectedDevices);
    }
    
    public synchronized int nbrOfConnectedDevices() {
        //System.out.println("nbrOfConnectedDevices");
        return connectedDevices.size();
    }
    
    public synchronized int nbrOfDevices() {
        //System.out.println("nbrOfDevices");
        return devices.size();
    }
    
//    public synchronized void moveDevice(String MAC, float angle, float stepLength) {
//        Device device = connectedDevices.get(connectedDevices.indexOf(new Device(MAC)));
//        device.setDirection(device.getDirection() + angle);
//        double x = device.getX();
//        double y = device.getY();
//        
//        x += (double) Math.cos(Math.toRadians(device.getDirection()))*stepLength;
//        y += (double) Math.sin(Math.toRadians(device.getDirection()))*stepLength;
//        
//        device.setX(x);
//        device.setY(y);
//    }
    

    public synchronized void updatePosition(String MAC, MagneticFingerprint fingerprint) {
        Device device = connectedDevices.get(connectedDevices.indexOf(new Device(MAC)));
        device.setX(fingerprint.x);
        device.setY(fingerprint.y);
        //device.addPreviousPosition(fingerprint);
    }
    
    public synchronized void updateReferencePosition(String MAC, ReferencePoint newReferencePoint) {
        Device device = getDevice(MAC);
        device.setReferencePoint(newReferencePoint);
        //System.out.println(newReferencePoint.x + " " + newReferencePoint.y);
    }
    
    public synchronized void deleteDevice(String MAC) {
        Device d = getDevice(MAC);
        connectedDevices.remove(d);
        devices.remove(d);
//        db.openConnection();
//        db.deleteDevice(MAC);
//        db.closeConnection();
    }
}
