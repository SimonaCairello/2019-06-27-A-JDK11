package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}

	public List<String> getOffenseCategory(){
		String sql = "SELECT DISTINCT offense_category_id " + 
				"FROM `events`" ;
		
		try {
			List<String> list = new ArrayList<>();
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(res.getString("offense_category_id"));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> getYears(){
		String sql = "SELECT DISTINCT YEAR(reported_date) AS year " + 
				"FROM `events` " + 
				"ORDER BY reported_date asc" ;
		
		try {
			List<Integer> list = new ArrayList<>();
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(res.getInt("year"));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> getOffenseType(String offenseCategory, Integer year){
		String sql = "SELECT DISTINCT offense_type_id AS type " + 
				"FROM `events` " + 
				"WHERE offense_category_id = ? AND YEAR(reported_date) = ?" ;
		
		try {
			List<String> list = new ArrayList<>();
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, offenseCategory);
			st.setInt(2, year);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(res.getString("type"));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Adiacenza> getAdiacenze(String offenseCategory, Integer year){
		String sql = "SELECT e1.offense_type_id AS id1, e2.offense_type_id AS id2, COUNT(e1.district_id) AS peso " + 
				"FROM `events` AS e1, `events` AS e2 " + 
				"WHERE e1.offense_type_id > e2.offense_type_id AND e1.offense_category_id = ? AND e1.district_id = e2.district_id " + 
				"AND e2.offense_category_id = ? AND YEAR(e1.reported_date) = ? AND YEAR(e2.reported_date) = ? " + 
				"GROUP BY e1.offense_type_id, e2.offense_type_id" ;
		
		try {
			List<Adiacenza> list = new ArrayList<>();
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, offenseCategory);
			st.setString(2, offenseCategory);
			st.setInt(3, year);
			st.setInt(4, year);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Adiacenza(res.getString("id1"), res.getString("id2"), res.getInt("peso")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
}
