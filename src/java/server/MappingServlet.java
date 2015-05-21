package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
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
        PrintWriter out = response.getWriter();
        String command = request.getParameter("command");
        switch (command) {
            
            case Command.GET_CONNECTED_DEVICES:
                getAllDevices(out);
                break;
                
            case Command.GET_POSITIONS:
                getAllPositions(out);
                break;
                
            case Command.LOCATE_DEVICE:
                handleLocateDeviceRequest(out, request.getParameter("id"), 
                        request.getParameter("dataType"), 
                        request.getParameter("data"));
                break;
                
            case Command.GET_PARTICLES:
                if(DeviceManager.getInstance().isConnected(request.getParameter("id"))) {
                    out.println(DeviceManager.getInstance()
                            .getDevice(request.getParameter("id"))
                            .getFilter().toJSON());
                }
                break;
                
        }
    }
    
    private void handleLocateDeviceRequest(PrintWriter out, String deviceId, String dataType, String data) {
        switch(dataType) {
            case "REFERENCE_AREA":
                HashMap<String, Double> areaFingerprint = removeUnreliable(Parser.parseFingerprint(data));
                ReferenceArea locatedArea = Locator.getInstance().locateReferenceArea(deviceId, areaFingerprint);
                DeviceManager.getInstance().updateReferencePosition(deviceId, locatedArea);

                out.println(locatedArea.x + "," + locatedArea.y);
                break;
                
            case "MAGNETIC_POINT":
                
                double[] magneticFingerprint = Parser.parseMagneticFingerprint(data);
                Locator.getInstance().updatePosition(deviceId, magneticFingerprint);
                break;
        }
    }    

    protected void getAllDevices(PrintWriter out) {

        ArrayList<Device> devices = DeviceManager.getInstance().getConnectedDevices();
        JsonArrayBuilder array = Json.createArrayBuilder();

        for (Device device : devices) {
            array.add(Json.createObjectBuilder().add("id", device.getId()).add("name", device.getName()).add("x", device.getX()).add("y", device.getY()));
        }
        out.println(Json.createObjectBuilder().add("devices", array).build());

    }

    protected void getAllPositions(PrintWriter out) {
        
        ArrayList<Device> devices = DeviceManager.getInstance().getConnectedDevices();
        JsonArrayBuilder array = Json.createArrayBuilder();

        for (Device device : devices)
            array.add(Json.createObjectBuilder().add("id", device.getId()).add("x", device.getX()).add("y", device.getY()));
        
        out.println(Json.createObjectBuilder().add("devices", array).build());
        
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

    private HashMap<String, Double> removeUnreliable(HashMap<String, Double> areaFingerprint) {
        
        ArrayList<String> toRemove = new ArrayList<>();

        for (String key : areaFingerprint.keySet() ) {
        
            if ((double)areaFingerprint.get(key) < -80)
                toRemove.add(key);
            
        }
        
        for (String key : toRemove)
            areaFingerprint.remove(key);
        
        return areaFingerprint;
    }

}
