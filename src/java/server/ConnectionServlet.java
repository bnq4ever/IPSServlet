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
        String MAC = request.getParameter("id");
        //System.out.println(command);
        switch(command) {
            case Command.CONNECT_DEVICE:
                connectDevice(out, MAC);
                break;
            case Command.DISCONNECT_DEVICE:
                disconnectDevice(out, MAC);
                break;
            case Command.INTRODUCING_DEVICE:
                String name = request.getParameter("name");
                introduction(out, MAC, name);
                break;
            case Command.DELETE_DEVICE:
                deleteDevice(out, MAC);
                break;
        }
    }
    
    private void deleteDevice(PrintWriter out, String MAC) {
        if (DeviceManager.getInstance().exists(MAC)) {
            DeviceManager.getInstance().deleteDevice(MAC);
            out.print(Command.DEVICE_DELETED);
        } else {
            out.print(Command.DEVICE_NOT_EXISTING);
        }
    }

    private void connectDevice(PrintWriter out, String MAC) {
        if (!DeviceManager.getInstance().isIntroduced(MAC)) {
            out.print(Command.DEVICE_UNKNOWN);
        } else if (DeviceManager.getInstance().isConnected(MAC)) {
            out.print(Command.DEVICE_CONNECTED);
        } else {
            DeviceManager.getInstance().connectDevice(MAC);
            out.print(Command.DEVICE_CONNECTED);
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

    private void disconnectDevice(PrintWriter out, String MAC) {
        if (DeviceManager.getInstance().isConnected(MAC)) {
            DeviceManager.getInstance().disconnectDevice(MAC);
            out.print(Command.DEVICE_DISCONNECTED);
        } else {
            out.print(Command.DEVICE_ALREADY_DISCONNECTED);
        }
    }

    private void introduction(PrintWriter out, String MAC, String name) {
        DeviceManager.getInstance().addDevice(MAC, name);
        out.print(Command.DEVICE_INTRODUCED);
    }

}
