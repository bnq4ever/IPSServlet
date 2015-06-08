package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
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
    
    public static final String LOCATE_DEVICE = "LOCATE_DEVICE";
    public static final String DEVICE_LOCATED = "DEVICE_LOCATED";
    
    public static final String GET_CONNECTED_DEVICES = "GET_CONNECTED_DEVICES";
    public static final String GET_POSITIONS = "GET_POSITIONS";
    public static final String GET_PARTICLES = "GET_PARTICLES";
    public static final String GET_BEST_CANDIDATES = "GET_BEST_CANDIDATES";
    
    //private PrintWriter out;
    //private ScriptEngineManager manager = new ScriptEngineManager();
    //private ScriptEngine engine = manager.getEngineByName("JavaScript");
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
        //Timer timer = new Timer();
        
        
        /*
            For running javascript functions. STUPID
 
        //ScriptEngineManager manager = new ScriptEngineManager();
        //ScriptEngine engine = manager.getEngineByName("JavaScript");
        try {
            // read script file
            engine.eval(Files.newBufferedReader(Paths.get("/home/fredrik/NetBeansProjects/IPSServlet/web/js/online.js"), StandardCharsets.UTF_8));
        } catch (ScriptException ex) {
            Logger.getLogger(MappingServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        
        /*
            Pushing to clients. STUPID
  
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Invocable inv = (Invocable) engine;
                try {
                    // call function from script file
                    inv.invokeFunction("pushDevices", getAllDevices(out));
                } catch (ScriptException ex) {
                    Logger.getLogger(MappingServlet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchMethodException ex) {
                    Logger.getLogger(MappingServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, 100, 100);
        */
        
        
        String command = request.getParameter("command");
        switch (command) {
            
            case GET_CONNECTED_DEVICES:
                getAllDevices(out);
                break;
                
            case GET_POSITIONS:
                getAllPositions(out);
                break;
                
            case LOCATE_DEVICE:
                handleLocateDeviceRequest(out, request.getParameter("id"), 
                        request.getParameter("dataType"), 
                        request.getParameter("data"));
                break;
                
            case GET_PARTICLES:
                if(DeviceManager.getInstance().isConnected(request.getParameter("id"))) {
                    out.println(DeviceManager.getInstance()
                            .getDevice(request.getParameter("id"))
                            .getFilter().toJSON());
                }
                break;
            
            case GET_BEST_CANDIDATES:
                String deviceId = request.getParameter("id");
                if(DeviceManager.getInstance().isConnected(deviceId)) {
                    ArrayList<MagneticPoint> points = Locator.getInstance().getBestCandidates(deviceId);
                    if(points == null) {
                        out.println("");
                        return;
                    }
                    JsonArrayBuilder array = Json.createArrayBuilder();
                    for (MagneticPoint point : points) {
                        array.add(Json.createObjectBuilder().add("x", point.x).add("y", point.y));
                    }

                    out.println(Json.createObjectBuilder().add("candidates", array).build());
                    
                }
                break;
        }
    }
    
//    private void getBestCandidates(PrintWriter out, String deviceId) {
//        ArrayList<ReferenceArea> referenceAreas = RadioMap.getInstance().getReferenceAreas();
//        JsonArrayBuilder array = Json.createArrayBuilder();
//
//        for (ReferenceArea area : referenceAreas) {
//            
//            JsonArrayBuilder magnetics = Json.createArrayBuilder();
//            if(Locator.getInstance().getBestCandidates(deviceId) != null) {
//                for(MagneticPoint magnetic : area.magneticPoints) {
//                
//                    magnetics.add(Json.createObjectBuilder()
//                            .add("x", magnetic.x)
//                            .add("y", magnetic.y));
//                }
//            }
//            array.add(Json.createObjectBuilder()
//                    .add("x", area.x)
//                    .add("y", area.y)
//                    .add("magnetics", magnetics));
//        }
//        out.println(Json.createObjectBuilder()
//                .add("referencePoints", array)
//                .build());
//    }
    
    private void handleLocateDeviceRequest(PrintWriter out, String deviceId, String dataType, String data) {
        System.out.println();
        switch(dataType) {
            case "REFERENCE_AREA":
                HashMap<String, Double> areaFingerprint = removeUnreliable(Parser.parseFingerprint(data));
//                System.out.println(data);
//                HashMap<String, Double> areaFingerprint = Parser.parseFingerprint(data);
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
            array.add(Json.createObjectBuilder().add("id", device.getId()).add("name", device.getName()).add("x", device.getX()).add("y", device.getY()).add("color", device.getColor()));
        }
        out.println(Json.createObjectBuilder().add("devices", array).build());
        
    }
    
    /*
    protected String getAllDevices(PrintWriter out) {

        ArrayList<Device> devices = DeviceManager.getInstance().getConnectedDevices();
        JsonArrayBuilder array = Json.createArrayBuilder();

        for (Device device : devices) {
            array.add(Json.createObjectBuilder().add("id", device.getId()).add("name", device.getName()).add("x", device.getX()).add("y", device.getY()));
        }
        return Json.createObjectBuilder().add("devices", array).build().toString();
        
    }
    */
    
    

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
        
            if ((double)areaFingerprint.get(key) < -90)
                toRemove.add(key);
        }
        
        for (String key : toRemove)
            areaFingerprint.remove(key);
        
        return areaFingerprint;
    }

}
