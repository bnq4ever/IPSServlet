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
@WebServlet(name = "Builder", urlPatterns = {"/Builder"})
public class BuildingServlet extends HttpServlet {

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
        PrintWriter out = response.getWriter();
        String command = request.getParameter("command");
        System.out.println(command);
        switch(command) {
            
            case Command.ADD_REFERENCE_POINT:
                double x = Double.parseDouble(request.getParameter("x"));
                double y = Double.parseDouble(request.getParameter("y"));
                String fingerprintData = request.getParameter("fingerprint");
                addReferencePoint(out, x, y, fingerprintData);
                break;
                
            case Command.ADD_MAGNETIC_FINGERPRINTS:
                String magneticData = request.getParameter("magnetics");
                addMagneticFingerprints(out, magneticData);
                break;
                
        }
    }

    private void addReferencePoint(PrintWriter out, double x, double y, String fingerprintData) {
        RadioMap.getInstance().addReferencePoint(new ReferencePoint(x, y, Parser.parseFingerprint(fingerprintData)));
        out.print(Command.REFERENCE_POINT_ADDED);
    }
    
    private void addMagneticFingerprints(PrintWriter out, String magneticData) {
        RadioMap.getInstance().addMagneticFingerprints(Parser.parseMagnetics(magneticData));
        out.print(Command.MAGNETIC_FINGERPRINTS_ADDED);
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
}