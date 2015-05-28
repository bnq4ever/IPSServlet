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
public class Command {
    //CONNECTION STATEMENTS
    public static final String DEVICE_UNKNOWN = "DEVICE_UNKNOWN";
    public static final String DEVICE_CONNECTED = "DEVICE_CONNECTED";
    public static final String DEVICE_ALREADY_CONNECTED = "DEVICE_ALLREADY_CONNECTED";
    public static final String DEVICE_DISCONNECTED = "DEVICE_DISCONNECTED";
    public static final String DEVICE_ALREADY_DISCONNECTED = "DEVICE_ALLREADY_DISCONNECTED";
    public static final String DEVICE_INTRODUCED = "DEVICE_INTRODUCED";
    public static final String DEVICE_ALREADY_INTRODUCED = "DEVICE_ALLREADY_INTRODUCED";
    public static final String DEVICE_DELETED = "DEVICE_DELETED";
    public static final String DEVICE_NOT_EXISTING = "DEVICE_NOT_EXISTING";
    
    public static final String ADD_MAGNETIC_POINTS = "ADD_MAGNETIC_POINTS";
    public static final String MAGNETIC_POINTS_ADDED = "MAGNETIC_POINTS_ADDED";
    public static final String MAGNETIC_POINTS_NOT_ADDED = "MAGNETIC_POINTS_NOT_ADDED";
    public static final String ADD_REFERENCE_AREA = "ADD_REFERENCE_AREA";
    public static final String REFERENCE_AREA_ADDED = "REFERENCE_AREA_ADDED";
    public static final String REFERENCE_AREA_NOT_ADDED = "REFERENCE_AREA_NOT_ADDED";
    
    //DEVICE REQUESTS
    public static final String CONNECT_DEVICE = "CONNECT_DEVICE";
    public static final String DISCONNECT_DEVICE = "DISCONNECT_DEVICE";
    public static final String INTRODUCING_DEVICE = "INTRODUCING_DEVICE";
    public static final String DELETE_DEVICE = "DELETE_DEVICE";
    public static final String BUILD_FINGERPRINT = "BUILD_FINGERPRINT";
    public static final String LOCATE_DEVICE = "LOCATE_DEVICE";
    public static final String DEVICE_LOCATED = "DEVICE_LOCATED";
    
    //WEBPAGE REQUESTS
    public static final String GET_CONNECTED_DEVICES = "GET_CONNECTED_DEVICES";
    public static final String GET_POSITIONS = "GET_POSITIONS";
    
    //PARTICLE FILTER
    public static final String GET_PARTICLES = "GET_PARTICLES";
    public static final String GET_CANDIDATES = "GET_CANDIDATES";
    
    public static final String GET_ALL_POINTS = "GET_ALL_POINTS";
    public static final String GET_BEST_CANDIDATES = "GET_BEST_CANDIDATES";
}
