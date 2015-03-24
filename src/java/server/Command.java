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
    //SERVER STATEMENTS
    public static final int DEVICE_UNKNOWN = 0;
    
    public static final int DEVICE_CONNECTED = 1;
    public static final int DEVICE_ALLREADY_CONNECTED = 2;
    
    public static final int DEVICE_DISCONNECTED = 3;
    public static final int DEVICE_ALLREADY_DISCONNECTED = 4;
    
    public static final int DEVICE_INTRODUCED = 5;
    public static final int DEVICE_ALLREADY_INTRODUCED = 6;
    
    //DEVICE REQUESTS
    public static final int CONNECT_DEVICE = 7;
    public static final int DISCONNECT_DEVICE = 8;
    public static final int INTRODUCING_DEVICE = 9;
    
    //CONNECTION
}
