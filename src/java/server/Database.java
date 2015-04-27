package server;

import java.sql.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
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

    
    
//    public void addDevice(String deviceId, String name) {
//        String sql = "INSERT INTO USERS "
//                + "(MAC, NAME) VALUES"
//                + "(?, ?)";
//        try {
//            preparedStatement = conn.prepareStatement(sql);
//            preparedStatement.setString(1, deviceId);
//            preparedStatement.setString(2, name);
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        } finally {
//            closePreparedStatement();
//        }
//    }

    //Working
    public boolean addReferencePoint(ReferencePoint p) {
        Set<String> keys = p.fingerprint.keySet();
        String fingerprint = p.getFingerprint();
        String sql = "INSERT INTO REFERENCE_POINTS "
                + "(x, y, fingerprint) VALUES"
                + "(?, ?, ?)";
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setDouble(1, p.x);
            preparedStatement.setDouble(2, p.y);
            preparedStatement.setString(3, fingerprint);
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
    public boolean addMagneticPoint(MagneticFingerprint fingerprint, ReferencePoint p) {
        String sql = "INSERT INTO MAGNETIC_POINTS "
                + "(x, y, magnitude, zvalue, xyvalue, reference_x, reference_y) VALUES"
                + "(?, ?, ?, ?, ?, ?, ?)";
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setDouble(1, fingerprint.x);
            preparedStatement.setDouble(2, fingerprint.y);
            preparedStatement.setDouble(3, fingerprint.magnitude);
            preparedStatement.setDouble(4, fingerprint.zaxis);
            preparedStatement.setDouble(5, fingerprint.xyaxis);
            preparedStatement.setDouble(6, p.x);
            preparedStatement.setDouble(7, p.y);
            
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

    
    public ArrayList<ReferencePoint> getRadioMap() {
        ArrayList<ReferencePoint> referencePoints = new ArrayList<>();
        String sql = "Select * FROM REFERENCE_POINTS";
        
        try {
            preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery(sql);
            while(rs.next()) {
                ReferencePoint p = new ReferencePoint(rs.getDouble("x"), rs.getDouble("y"), Parser.parseFingerprint(rs.getString("fingerprint")));
                p.magnetics = getMagneticPoints(rs.getDouble("x"), rs.getDouble("y"));
                referencePoints.add(p);
                //System.out.println(rs.getDouble("x") + " " + rs.getDouble("y") + " " + Parser.parseFingerprint(rs.getString("fingerprint") + " " + p.magnetics.size()));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closePreparedStatement();
        }
        return referencePoints;
    }
    
    public ArrayList<MagneticFingerprint> getMagneticPoints(double x, double y) {
        ArrayList<MagneticFingerprint> magnetics = new ArrayList<>();
        String sql = "SELECT * FROM MAGNETIC_POINTS "
                + "WHERE REFERENCE_X=? AND "
                + "REFERENCE_Y=?";
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setDouble(1, x);
            preparedStatement.setDouble(2, y);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                //System.out.println(rs.getDouble("x") + " " + rs.getDouble("y") + " " + rs.getDouble("magnitude") + " " + rs.getDouble("zvalue") + " " + rs.getDouble("xyvalue"));
                magnetics.add(new MagneticFingerprint(rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("magnitude"), rs.getDouble("zvalue"), rs.getDouble("xyvalue")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closePreparedStatement();
        }
        return magnetics;
    }
    
    public void setDeviceName(String deviceId, String name) {
        String sql = "UPDATE USERS "
                + "SET NAME=? "
                + "WHERE MAC=?";
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
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

    public String getDevice(String MAC) {
        String sql = "SELECT NAME FROM USERS "
                + "WHERE MAC=?";
        String name = "";
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, MAC);
            ResultSet rs = preparedStatement.executeQuery(sql);
            while (rs.next()) {
                name = rs.getString("NAME");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closePreparedStatement();
        }
        return name;
    }

    public ArrayList<Device> getAllDevices() {
        ArrayList<Device> devices = new ArrayList<>();
        String sql = "SELECT MAC, NAME FROM USERS "
                + "ORDER BY MAC";
        try {
            preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery(sql);
            while (rs.next()) {
                devices.add(new Device(rs.getString("MAC"), rs.getString("NAME")));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closePreparedStatement();
        }
        return devices;
    }
    
    public void deleteDevice(String MAC) {
        String sql = "DELETE USERS WHERE "
                + "MAC = ?";
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, MAC);
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
