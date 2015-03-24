package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@WebServlet(name = "Mapper", urlPatterns = {"/Mapper"})
public class MappingServlet extends HttpServlet {

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
        String key = "command";
        String value = request.getParameter(key);
        switch(value) {
            case "GET_CONNECTED_DEVICES":
                getAllDevices(response);
                break;
            case "DEVICE_MOVED":
                String MAC = request.getParameter("id");
                float angle = Float.parseFloat(request.getParameter("angle"));
                float stepLength = Float.parseFloat(request.getParameter("steplength"));
                System.out.println(angle);
                moveDevice(MAC, angle, stepLength);
                break;
            case "GET_POSITIONS":
                getPositions(response);
                break;
        }
    }
    
    protected void moveDevice(String MAC, float angle, float stepLength) {
        DeviceManager.getInstance().moveDevice(MAC, angle, stepLength);
    }
    
    protected void getAllDevices(HttpServletResponse response) {
        PrintWriter out;
        try {
            out = response.getWriter();
            ArrayList<Device> devices = DeviceManager.getInstance().getConnectedDevices();
            JsonArrayBuilder array = Json.createArrayBuilder();
            
            for(Device device : devices){
                array.add(Json.createObjectBuilder().add("id", device.getId()).add("name", device.getName()).add("x", device.getX()).add("y", device.getY()));
            }
            out.println(Json.createObjectBuilder().add("devices", array).build());
            
        } catch (IOException ex) {
            Logger.getLogger(MappingServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void getPositions(HttpServletResponse response) {
        PrintWriter out;
        try {
            out = response.getWriter();
            ArrayList<Device> devices = DeviceManager.getInstance().getConnectedDevices();
            JsonArrayBuilder array = Json.createArrayBuilder();
            
            for(Device device : devices) {
                array.add(Json.createObjectBuilder().add("id", device.getId()).add("x", device.getX()).add("y", device.getY()));
            }
            out.println(Json.createObjectBuilder().add("devices", array).build());
            
        } catch (IOException ex) {
            Logger.getLogger(MappingServlet.class.getName()).log(Level.SEVERE, null, ex);
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
}
