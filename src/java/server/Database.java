package server;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    private Connection conn;
    private PreparedStatement preparedStatement;
    private static Database _instance;
    
    public Database() {
        conn = null;
    }
    
    public synchronized static Database getInstance() {
        if (_instance == null) 
            _instance = new Database();
        return _instance;
    }    

//    public void addDevice(String deviceId, String deviceName) {
//        String sql = "INSERT INTO USERS "
//                + "(MAC, NAME) VALUES"
//                + "(?, ?)";
//        try {
//            preparedStatement = conn.prepareStatement(sql);
//            preparedStatement.setString(1, deviceId);
//            preparedStatement.setString(2, deviceName);
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        } finally {
//            closePreparedStatement();
//        }
//    }

    //Working
    public boolean addReferenceArea(ReferenceArea area) {
        Set<String> keys = area.fingerprint.keySet();
        String sql = "INSERT INTO REFERENCE_AREAS "
                + "(x, y, fingerprint) VALUES"
                + "(?, ?, ?)";
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setDouble(1, area.x);
            preparedStatement.setDouble(2, area.y);
            preparedStatement.setString(3, area.getFingerprint());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            //return false;
            System.out.println(e.getMessage());
        } finally {
            closePreparedStatement();
        }
        return false;
    }
    
        //Working
    public boolean addReferenceAreaBT(ReferenceArea area) {
        String sql = "INSERT INTO REFERENCE_AREAS "
                + "(x, y, fingerprint) VALUES"
                + "(?, ?, ?)";
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setDouble(1, area.x);
            preparedStatement.setDouble(2, area.y);
            preparedStatement.setString(3, area.BTSSID);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            //return false;
            System.out.println(e.getMessage());
        } finally {
            closePreparedStatement();
        }
        return false;
    }
    
    public HashSet<String> getAccessPoints() {
        HashSet<String> accessPoints = new HashSet<>();
        
        String sql = "Select BSSID FROM ACCESS_POINTS";
        
        try {
            preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery(sql);
            while(rs.next()) {
                accessPoints.add(rs.getString("BSSID"));                
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closePreparedStatement();
        }
        
        return accessPoints;
    }
    
    public void addAccessPoint(String BSSID) {
        String sql = "INSERT INTO ACCESS_POINTS "
                + "(BSSID) VALUES"
                + "(?)";
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, BSSID);
            
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            //return false;
            System.out.println(e.getMessage());
        } finally {
            closePreparedStatement();
        }
    }
    
    //Working
    public boolean addMagneticPoint(MagneticPoint magneticPoint) {
        String sql = "INSERT INTO MAGNETIC_POINTS "
                + "(x, y, magnitude, zvalue, xyvalue) VALUES"
                + "(?, ?, ?, ?, ?)";
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setDouble(1, magneticPoint.x);
            preparedStatement.setDouble(2, magneticPoint.y);
            preparedStatement.setDouble(3, magneticPoint.magnitude);
            preparedStatement.setDouble(4, magneticPoint.zaxis);
            preparedStatement.setDouble(5, magneticPoint.xyaxis);
            
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            //return false;
            System.out.println(e.getMessage());
        } finally {
            closePreparedStatement();
        }
        return false;
    }

    
    public ArrayList<ReferenceArea> getReferenceAreas() {
        ArrayList<ReferenceArea> referenceAreas = new ArrayList<>();
        String sql = "Select * FROM REFERENCE_AREAS";
        
        try {
            preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery(sql);
            while(rs.next()) {
                
                ReferenceArea area = new ReferenceArea(
                        rs.getDouble("x"), 
                        rs.getDouble("y"), 
                        Parser.parseFingerprint(rs.getString("fingerprint")));
                
                referenceAreas.add(area);
                //System.out.println(rs.getDouble("x") + " " + rs.getDouble("y") + " " + Parser.parseFingerprint(rs.getString("fingerprint") + " " + p.magnetics.size()));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closePreparedStatement();
        }
        return referenceAreas;
    }
    
    public ArrayList<ReferenceArea> getReferenceAreasBT() {
        ArrayList<ReferenceArea> referenceAreas = new ArrayList<>();
        String sql = "Select * FROM REFERENCE_AREAS";
        
        try {
            preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery(sql);
            while(rs.next()) {
                
                ReferenceArea area = new ReferenceArea(
                        rs.getDouble("x"), 
                        rs.getDouble("y"),
                        rs.getString("fingerprint"));
                
                referenceAreas.add(area);
                //System.out.println(rs.getDouble("x") + " " + rs.getDouble("y") + " " + Parser.parseFingerprint(rs.getString("fingerprint") + " " + p.magnetics.size()));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closePreparedStatement();
        }
        return referenceAreas;
    }
    
    public ArrayList<MagneticPoint> getMagneticPoints() {
        ArrayList<MagneticPoint> magneticPoints = new ArrayList<>();
        String sql = "SELECT * FROM MAGNETIC_POINTS";
        try {
            preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                //System.out.println(rs.getDouble("x") + " " + rs.getDouble("y") + " " + rs.getDouble("magnitude") + " " + rs.getDouble("zvalue") + " " + rs.getDouble("xyvalue"));
                magneticPoints.add(
                        new MagneticPoint(
                            rs.getDouble("x"), 
                            rs.getDouble("y"), 
                            rs.getDouble("magnitude"), 
                            rs.getDouble("zvalue"), 
                            rs.getDouble("xyvalue")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closePreparedStatement();
        }
        return magneticPoints;
    }
    
//    public ArrayList<MagneticPoint> getMagneticPoints(double x, double y) {
//        ArrayList<MagneticPoint> magneticPoints = new ArrayList<>();
//        String sql = "SELECT * FROM MAGNETIC_POINTS "
//                + "WHERE REFERENCE_X=? AND "
//                + "REFERENCE_Y=?";
//        try {
//            preparedStatement = conn.prepareStatement(sql);
//            preparedStatement.setDouble(1, x);
//            preparedStatement.setDouble(2, y);
//            ResultSet rs = preparedStatement.executeQuery();
//            while(rs.next()) {
//                //System.out.println(rs.getDouble("x") + " " + rs.getDouble("y") + " " + rs.getDouble("magnitude") + " " + rs.getDouble("zvalue") + " " + rs.getDouble("xyvalue"));
//                magneticPoints.add(
//                        new MagneticPoint(
//                            rs.getDouble("x"), 
//                            rs.getDouble("y"), 
//                            rs.getDouble("magnitude"), 
//                            rs.getDouble("zvalue"), 
//                            rs.getDouble("xyvalue")));
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        } finally {
//            closePreparedStatement();
//        }
//        return magneticPoints;
//    }
    
    public void setDeviceName(String deviceId, String deviceName) {
        String sql = "UPDATE USERS "
                + "SET NAME=? "
                + "WHERE MAC=?";
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, deviceName);
            preparedStatement.setString(2, deviceId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closePreparedStatement();
        }
    }
    
    public boolean isIntroduced(String deviceId) {
        String sql = "SELECT NAME FROM USERS "
                + "WHERE MAC=?";
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, deviceId);
            ResultSet rs = preparedStatement.executeQuery();
            
            rs.next();
            if (rs.getString("NAME") != null)
                return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closePreparedStatement();
        }
        return false;
    }

    public String getDevice(String deviceId) {
        String sql = "SELECT NAME FROM USERS "
                + "WHERE MAC=?";
        String deviceName = "";
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, deviceId);
            ResultSet rs = preparedStatement.executeQuery(sql);
            while (rs.next()) {
                deviceName = rs.getString("NAME");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closePreparedStatement();
        }
        return deviceName;
    }

    public ArrayList<Device> getAllDevices() {
        ArrayList<Device> allDevices = new ArrayList<>();
        String sql = "SELECT MAC, NAME FROM USERS "
                + "ORDER BY MAC";
        try {
            preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery(sql);
            while (rs.next()) {
                allDevices.add(
                        new Device(
                            rs.getString("MAC"), 
                            rs.getString("NAME")));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closePreparedStatement();
        }
        return allDevices;
    }
    
    public void deleteDevice(String deviceId) {
        String sql = "DELETE USERS WHERE "
                + "MAC = ?";
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, deviceId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closePreparedStatement();
        }
    }

    public void openConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();
            return;
        }

        System.out.println("MySQL JDBC Driver Registered!");

        try {
            //conn = (Connection) DriverManager.getConnection("jdbc:mysql://10.90.130.110:3306/db_ips", "username", "password");
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/db_ips", "username", "password");

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }

        if (isConnected()) {
            System.out.println("You made it, take control your database now!");
        } else {
            System.out.println("Failed to make connection!");
        }
    }

    public boolean isConnected() {
        return conn != null;
    }

    public void closeConnection() {
        if (preparedStatement == null)
            return;
        try {
            conn.close();
        } catch (SQLException e) {

        }
        conn = null;
    }

    private void closePreparedStatement() {
        if (preparedStatement == null)
            return;
        try {
            preparedStatement.close();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
