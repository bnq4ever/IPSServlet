/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
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
            case Command.ADD_REFERENCE_AREA:
                handleReferenceAreaData(out, 
                        Double.parseDouble(request.getParameter("x")), 
                        Double.parseDouble(request.getParameter("y")),
                        request.getParameter("fingerprint"));
                break;
                
            case Command.ADD_MAGNETIC_POINTS:
                handleMagneticPointsData(out, 
                        request.getParameter("magnetics"));
                break;
                
            case Command.GET_ALL_POINTS:
                getAllPoints(out);
                break;
        }
    }
    
    private void getAllPoints(PrintWriter out) {
        ArrayList<ReferenceArea> referenceAreas = RadioMap.getInstance().getReferenceAreas();
        JsonArrayBuilder array = Json.createArrayBuilder();

        for (ReferenceArea area : referenceAreas) {
            
            JsonArrayBuilder magnetics = Json.createArrayBuilder();
            for(MagneticPoint magnetic : area.getMagneticPoints()) {
                magnetics.add(Json.createObjectBuilder()
                        .add("x", magnetic.x)
                        .add("y", magnetic.y));
            }
            array.add(Json.createObjectBuilder()
                    .add("x", area.x)
                    .add("y", area.y)
                    .add("magnetics", magnetics));
        }
        out.println(Json.createObjectBuilder()
                .add("referencePoints", array)
                .build());
    }
    

    private void handleReferenceAreaData(PrintWriter out, double x, double y, String areaData) {
        ReferenceArea area = new ReferenceArea(x, y, Parser.parseFingerprint(areaData));
        
        RadioMap.getInstance().addReferenceArea(area);
        RadioMap.getInstance().addReferenceAreaDB(area);
        
        out.print(Command.REFERENCE_AREA_ADDED);
    }
    
    private void handleMagneticPointsData(PrintWriter out, String magneticPointData) {
        ArrayList<MagneticPoint> points = Parser.parseMagnetics(magneticPointData);
        
        for(MagneticPoint p : points) {
            RadioMap.getInstance().addMagneticPoint(p);
            RadioMap.getInstance().addMagneticPointDB(p);
        }
        
        out.print(Command.MAGNETIC_POINTS_ADDED);
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