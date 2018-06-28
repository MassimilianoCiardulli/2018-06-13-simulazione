package it.polito.tdp.flightdelays.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.flightdelays.model.Airline;
import it.polito.tdp.flightdelays.model.Airport;
import it.polito.tdp.flightdelays.model.AirportIdMap;
import it.polito.tdp.flightdelays.model.Flight;
import it.polito.tdp.flightdelays.model.PairAirportsCount;

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

	public List<PairAirportsCount> getPairs(Airline airline, AirportIdMap airportIdMap) {
		final String sql = "select distinct f1.ORIGIN_AIRPORT_ID, f2.DESTINATION_AIRPORT_ID, (avg(f1.DEPARTURE_DELAY)+avg(f1.DEPARTURE_DELAY))/count(*) as avg " + 
				"from flights as f1, flights as f2 " + 
				"where f1.ID = f2.ID " + 
				"and f1.AIRLINE = f2.AIRLINE " + 
				"and f1.AIRLINE = ? " + 
				"and f1.ORIGIN_AIRPORT_ID <> f2.DESTINATION_AIRPORT_ID " + 
				"group by f1.ORIGIN_AIRPORT_ID, f2.DESTINATION_AIRPORT_ID " + 
				"order by f1.ORIGIN_AIRPORT_ID";
		List<PairAirportsCount> result = new ArrayList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, airline.getId());
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Airport a1 = airportIdMap.get(rs.getString("ORIGIN_AIRPORT_ID"));
				Airport a2 = airportIdMap.get(rs.getString("DESTINATION_AIRPORT_ID"));
				if(a1!=null && a2!=null)
					result.add(new PairAirportsCount(a1, a2, rs.getDouble("avg")));
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}

	public LocalDateTime getDepDate(Airline ar, Airport a) {
		final String sql = "select f.SCHEDULED_DEP_DATE, f.DEPARTURE_DELAY " + 
				"from flights as f " + 
				"where f.AIRLINE = ? " + 
				"and f.ORIGIN_AIRPORT_ID = ? "
				+ "and year(SCHEDULED_DEP_DATE) = 2015";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ar.getId());
			st.setString(2, a.getId());
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				LocalDateTime ld = rs.getTimestamp("SCHEDULED_DEP_DATE").toLocalDateTime();
				ld.plusMinutes(rs.getInt("DEPARTURE_DELAY"));
				return ld ;
			}
			
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		return null;
			
	}

	public List<Airport> airportsof(Airline airline, AirportIdMap airportIdMap) {
		final String sql = "select distinct f.ORIGIN_AIRPORT_ID, f.DESTINATION_AIRPORT_ID " + 
				"from flights f " + 
				"where f.AIRLINE=?" ;
		List<Airport> result = new ArrayList<Airport>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Airport airport1 = airportIdMap.get(rs.getString("ORIGIN_AIRPORT_ID")) ;
				Airport airport2 = airportIdMap.get(rs.getString("DESTINATION_AIRPORT_ID")) ;
				if(airport1!=null && !result.contains(airport1))
					result.add(airportIdMap.get(airport1));
				if(airport2!=null && !result.contains(airport2))
					result.add(airportIdMap.get(airport2));
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Flight> flights2015() {
		final String sql = "select distinct * " + 
				"from flights f " + 
				"where year(f.SCHEDULED_DEP_DATE)=2015";
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

	public Flight getFirstFlightFrom(Airport airport) {
		final String sql = "select * " + 
				"from flights f " + 
				"where f.ORIGIN_AIRPORT_ID=? "
				+ "and year(SCHEDULED_DEP_DATE) = 2015 " + 
				"LIMIT 1";
		Flight result = null ;

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, airport.getId());
			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				result = new Flight(rs.getInt("id"), rs.getString("airline"), rs.getInt("flight_number"),
						rs.getString("origin_airport_id"), rs.getString("destination_airport_id"),
						rs.getTimestamp("scheduled_dep_date").toLocalDateTime(),
						rs.getTimestamp("arrival_date").toLocalDateTime(), rs.getInt("departure_delay"),
						rs.getInt("arrival_delay"), rs.getInt("air_time"), rs.getInt("distance"));
				
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}

	public Flight getNextFlight(Airport airport, Flight flight) {
		final String sql = "select * " + 
				"from flights f " + 
				"where f.ORIGIN_AIRPORT_ID=? " + 
				"and year(SCHEDULED_DEP_DATE) = 2015 " + 
				"and ARRIVAL_DATE > ? " + 
				"LIMIT 1";
		Flight result = null ;

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, airport.getId());
			
			st.setDate(2, java.sql.Date.valueOf(flight.getArrivalDate().toLocalDate()));
			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				result = new Flight(rs.getInt("id"), rs.getString("airline"), rs.getInt("flight_number"),
						rs.getString("origin_airport_id"), rs.getString("destination_airport_id"),
						rs.getTimestamp("scheduled_dep_date").toLocalDateTime(),
						rs.getTimestamp("arrival_date").toLocalDateTime(), rs.getInt("departure_delay"),
						rs.getInt("arrival_delay"), rs.getInt("air_time"), rs.getInt("distance"));
				
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
