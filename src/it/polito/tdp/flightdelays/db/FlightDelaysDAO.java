package it.polito.tdp.flightdelays.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.flightdelays.model.Airline;
import it.polito.tdp.flightdelays.model.Airport;
import it.polito.tdp.flightdelays.model.AirportIdMap;
import it.polito.tdp.flightdelays.model.AirportPartenzaDestinazione;
import it.polito.tdp.flightdelays.model.Flight;

public class FlightDelaysDAO {

	public List<Airline> loadAllAirlines() {
		String sql = "SELECT id, airline from airlines order by airline asc";
		List<Airline> result = new ArrayList<Airline>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Airline(rs.getString("ID"), rs.getString("airline")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Airport> loadAllAirports(AirportIdMap airportIdMap) {
		String sql = "SELECT id, airport, city, state, country, latitude, longitude FROM airports";
		List<Airport> result = new ArrayList<Airport>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Airport airport = new Airport(rs.getString("id"), rs.getString("airport"), rs.getString("city"),
						rs.getString("state"), rs.getString("country"), rs.getDouble("latitude"), rs.getDouble("longitude"));
				result.add(airportIdMap.get(airport));
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Flight> loadAllFlights() {
		String sql = "SELECT id, airline, flight_number, origin_airport_id, destination_airport_id, scheduled_dep_date, "
				+ "arrival_date, departure_delay, arrival_delay, air_time, distance FROM flights";
		List<Flight> result = new LinkedList<Flight>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Flight flight = new Flight(rs.getInt("id"), rs.getString("airline"), rs.getInt("flight_number"),
						rs.getString("origin_airport_id"), rs.getString("destination_airport_id"),
						rs.getTimestamp("scheduled_dep_date").toLocalDateTime(),
						rs.getTimestamp("arrival_date").toLocalDateTime(), rs.getInt("departure_delay"),
						rs.getInt("arrival_delay"), rs.getInt("air_time"), rs.getInt("distance"));
				result.add(flight);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<AirportPartenzaDestinazione> getPartenzaDestinazione(Airline airline, AirportIdMap airportIdMap) {
		final String sql = "select distinct f1.ORIGIN_AIRPORT_ID, f2.DESTINATION_AIRPORT_ID " + 
				"from flights as f1, flights as f2 " + 
				"where f1.AIRLINE = f2.AIRLINE " + 
				"and f1.AIRLINE = ? "
				+ "and f1.ORIGIN_AIRPORT_ID<>f2.DESTINATION_AIRPORT_ID " + 
				"order by f1.ORIGIN_AIRPORT_ID " ;
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, airline.getId());
			ResultSet rs = st.executeQuery();
			List<AirportPartenzaDestinazione> result = new ArrayList<>();
			
			while (rs.next()) {
				if(airportIdMap.get(rs.getString("ORIGIN_AIRPORT_ID"))!=null && 
						airportIdMap.get(rs.getString("DESTINATION_AIRPORT_ID"))!=null) {
					AirportPartenzaDestinazione apd = new AirportPartenzaDestinazione(airportIdMap.get(rs.getString("ORIGIN_AIRPORT_ID")),
							airportIdMap.get(rs.getString("DESTINATION_AIRPORT_ID")));
					System.out.println(apd);
					result.add(apd) ;
				}
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public double getAvgDelay(Airport partenza, Airport destinazione, Airline airline) {
		final String sql = "select (avg(f.DEPARTURE_DELAY) + avg(f.ARRIVAL_DELAY))/count(*) as avg " + 
				"from flights as f " + 
				"where f.AIRLINE=? " + 
				"and f.ORIGIN_AIRPORT_ID=? " + 
				"and f.DESTINATION_AIRPORT_ID=? " + 
				"";
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, airline.getId());
			st.setString(2, partenza.getId());
			st.setString(3, destinazione.getId());
			ResultSet rs = st.executeQuery();
			
			double result = 0 ;
			
			if(rs.next()) {
				result = rs.getDouble("avg") ;
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
	
}
