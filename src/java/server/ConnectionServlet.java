/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Hampus
 */
@WebServlet(name = "Connector", urlPatterns = {"/Connector"})
public class ConnectionServlet extends HttpServlet {
    
    public static final String CONNECT_DEVICE = "CONNECT_DEVICE";
    public static final String DISCONNECT_DEVICE = "DISCONNECT_DEVICE";
    public static final String INTRODUCING_DEVICE = "INTRODUCING_DEVICE";
    public static final String DELETE_DEVICE = "DELETE_DEVICE";
    
    public static final String DEVICE_UNKNOWN = "DEVICE_UNKNOWN";
    public static final String DEVICE_CONNECTED = "DEVICE_CONNECTED";
    public static final String DEVICE_ALREADY_CONNECTED = "DEVICE_ALLREADY_CONNECTED";
    public static final String DEVICE_DISCONNECTED = "DEVICE_DISCONNECTED";
    public static final String DEVICE_ALREADY_DISCONNECTED = "DEVICE_ALLREADY_DISCONNECTED";
    public static final String DEVICE_INTRODUCED = "DEVICE_INTRODUCED";
    public static final String DEVICE_ALREADY_INTRODUCED = "DEVICE_ALLREADY_INTRODUCED";
    public static final String DEVICE_DELETED = "DEVICE_DELETED";
    public static final String DEVICE_NOT_EXISTING = "DEVICE_NOT_EXISTING";
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        PrintWriter out = response.getWriter();
        
        String command = request.getParameter("command");
        System.out.println(command);
        switch(command) {
            case CONNECT_DEVICE:
                connectDevice(out, request.getParameter("id"));
                break;
            case DISCONNECT_DEVICE:
                disconnectDevice(out, request.getParameter("id"));
                break;
            case INTRODUCING_DEVICE:
                String deviceName = request.getParameter("name");
                introduction(out, request.getParameter("id"), deviceName);
                break;
            case DELETE_DEVICE:
                deleteDevice(out, request.getParameter("id"));
                break;
        }
    }
    
    private void deleteDevice(PrintWriter out, String deviceMAC) {
        if (DeviceManager.getInstance().exists(deviceMAC)) {
            DeviceManager.getInstance().deleteDevice(deviceMAC);
            out.print(DEVICE_DELETED);
        } else {
            out.print(DEVICE_NOT_EXISTING);
        }
    }

    private void connectDevice(PrintWriter out, String deviceMAC) {
        if (!DeviceManager.getInstance().isIntroduced(deviceMAC)) {
            out.print(DEVICE_UNKNOWN);
        } else if (DeviceManager.getInstance().isConnected(deviceMAC)) {
            out.print(DEVICE_CONNECTED);
        } else {
            DeviceManager.getInstance().connectDevice(deviceMAC);
            out.print(DEVICE_CONNECTED);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void disconnectDevice(PrintWriter out, String deviceMAC) {
        if (DeviceManager.getInstance().isConnected(deviceMAC)) {
            DeviceManager.getInstance().disconnectDevice(deviceMAC);
            out.print(DEVICE_DISCONNECTED);
        } else {
            out.print(DEVICE_ALREADY_DISCONNECTED);
        }
    }

    private void introduction(PrintWriter out, String deviceMAC, String deviceName) {
        DeviceManager.getInstance().addDevice(deviceMAC, deviceName);
        out.print(DEVICE_INTRODUCED);
    }

}
