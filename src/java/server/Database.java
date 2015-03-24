package server;

import java.sql.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
	private Connection conn;
	private PreparedStatement preparedStatement;

	public Database() {
		conn = null;
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
			conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/db_ips","username", "password");

		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}

		if (conn != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}
	}

	public void addAP(String MAC, String coord) {
            String sql = "INSERT INTO ACCESSPOINTS"
			+ "(MAC, COORD) VALUES"
			+ "(?, ?)";
            try {
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, MAC);
                preparedStatement.setString(2, coord);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                closePreparedStatement();
            }
	}

        public void addDeviceExistence(String deviceId) {
	String sql = "INSERT INTO USERS "
			+ "(MAC, NAME) VALUES"
			+ "(?, ?)";
            try {
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setString(1, deviceId);
		preparedStatement.setString(2, "");
		preparedStatement.executeUpdate();
            } catch (SQLException e) {
		System.out.println(e.getMessage());
            } finally {
                closePreparedStatement();
            }
	}

        public void setDeviceName(String deviceId, String name){
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

	public void deleteUser(String MAC) {
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

	public ArrayList<Device> getAllDevices(){
		ArrayList<Device> devices = new ArrayList<>();
		String sql = "SELECT MAC, NAME FROM USERS "
				+ "ORDER BY MAC";
		try {
                    preparedStatement = conn.prepareStatement(sql);
                    ResultSet rs = preparedStatement.executeQuery(sql);
                    while (rs.next())
                        devices.add(new Device(rs.getString("MAC"), rs.getString("NAME")));

		} catch (SQLException e) {
                    System.out.println(e.getMessage());

		} finally {
                   closePreparedStatement();
                }
            return devices;
	}

	public ArrayList<Map.Entry<String, String>> getAccessPoints() {
            ArrayList<Map.Entry<String, String>> accessPoints = new ArrayList<Map.Entry<String, String>>();
            String sql = "SELECT MAC, COORD FROM ACCESSPOINTS "
                        	+ "ORDER BY MAC";
            try {
		preparedStatement = conn.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery(sql);
		while (rs.next()) {
			accessPoints.add(new AbstractMap.SimpleEntry<String, String>(rs.getString("MAC"), rs.getString("COORD")));
		}
            } catch (SQLException e) {
			System.out.println(e.getMessage());
            } finally {
                closePreparedStatement();
            }
            return accessPoints;
	}

	public boolean isConnected() {
		return conn != null;
	}

	public void closeConnection() {
            if (conn != null) {
		try {
                    conn.close();
		} catch (SQLException e) {
            
                }
                conn = null;
            }
	}
        
        private void closePreparedStatement() {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
}