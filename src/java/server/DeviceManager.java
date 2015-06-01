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
        return devices.contains(getDevice(deviceId));
    }
    
    public synchronized void addDevice(String deviceId, String deviceName) {
        //System.out.println("addDevice");
//        db.openConnection();
//        db.addDevice(deviceId, name);
//        db.closeConnection();
        Device device = new Device(deviceId, deviceName);
        device.startFilter();
        devices.add(device);
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
    
    protected synchronized Device getDevice(String deviceId) {
        for(Device d : devices) {
            if(d.getID().equals(deviceId)) {
                return d;
            }
        }
        //return devices.get(devices.indexOf(new Device(deviceId)));
        return null;
    }
    
    //Update DB data in interval or at shutdown
    public synchronized void setDeviceName(String deviceId, String deviceName) {
        //System.out.println("setDeviceName");
//        db.openConnection();
//        db.setDeviceName(deviceId, name);
//        db.closeConnection();
        getDevice(deviceId).setName(deviceName);
    }
    
    protected synchronized ArrayList<Device> getConnectedDevices() {
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
    

    protected synchronized void setPosition(String deviceId, MagneticPoint magneticPoint) {
        Device device = getDevice(deviceId);
        device.setX(magneticPoint.x);
        device.setY(magneticPoint.y);
//        device.addPreviousPosition(fingerprint);
    }
    
    public synchronized void updateReferencePosition(String deviceId, ReferenceArea area) {
        Device device = getDevice(deviceId);
        device.setReferenceArea(area);
//        device.setX(area.x);
//        device.setY(area.y);
    }
    
    public synchronized void deleteDevice(String deviceId) {
        Device device = getDevice(deviceId);
        connectedDevices.remove(device);
        devices.remove(device);
//        db.openConnection();
//        db.deleteDevice(deviceId);
//        db.closeConnection();
    }
}
