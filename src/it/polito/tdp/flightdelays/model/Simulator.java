package it.polito.tdp.flightdelays.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.flightdelays.db.FlightDelaysDAO;

public class Simulator {
	
	enum EventType {
		PASSEGGERO_PARTITO,
		PASSEGGERO_ARRIVATO
	}
	
	class Event implements Comparable<Event>{
		private EventType eventtype ;
		private Flight flight ;
		private Passeggero passeggero ;
		public Event(EventType eventtype, Flight flight, Passeggero passeggero) {
			super();
			this.eventtype = eventtype;
			this.flight = flight ;
			this.passeggero = passeggero;
		}
		public EventType getEventtype() {
			return eventtype;
		}
		public void setEventtype(EventType eventtype) {
			this.eventtype = eventtype;
		}
		
		public Passeggero getPasseggero() {
			return passeggero;
		}
		public Flight getFlight() {
			return flight;
		}
		public void setFlight(Flight flight) {
			this.flight = flight;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((flight == null) ? 0 : flight.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Event other = (Event) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (flight == null) {
				if (other.flight != null)
					return false;
			} else if (!flight.equals(other.flight))
				return false;
			return true;
		}
		private Simulator getOuterType() {
			return Simulator.this;
		}
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Event [eventtype=");
			builder.append(eventtype);
			builder.append(", flight=");
			builder.append(flight);
			builder.append(", passeggero=");
			builder.append(passeggero);
			builder.append("]");
			return builder.toString();
		}
		@Override
		public int compareTo(Event o) {
			return flight.getScheduledDepartureDate().compareTo(o.getFlight().getScheduledDepartureDate());
		}
		
	}
	
	private int K ;
	private int V ;
	private PriorityQueue<Event> queue ;
	private List<Airport> airports ;
	private AirportIdMap airportIdMap ;
	private List<Passeggero> passeggeri ;
	
	public void init(int K, int V, List<Airport> airports, AirportIdMap airportidMap) {
		this.K = K ;
		this.V = V ;
		this.airports = airports ;
		Collections.shuffle(this.airports);
		queue = new PriorityQueue<>() ;
		FlightDelaysDAO dao = new FlightDelaysDAO() ;
		passeggeri = new ArrayList<>();
		int i = 0;
		while(i < K) {
			Passeggero p = new Passeggero(i, 0);
			p.setCurrentAirport(this.airports.get(i));
			passeggeri.add(p) ;
			Flight flight = dao.getFirstFlightFrom(this.airports.get(i));
			Event e = new Event(EventType.PASSEGGERO_PARTITO, flight, p) ;
			queue.add(e) ;
			i++ ;
		}
		this.airportIdMap = airportidMap ;
		
	}
	public void run() {
		Event e ;
		try {
			while((e = queue.poll())!=null) {
				processEvent(e) ;
			}
		} catch(NullPointerException npe) {
			
		}
	}
	
	private void processEvent(Event e) {
		FlightDelaysDAO dao = new FlightDelaysDAO() ;

		switch(e.getEventtype()) {
		case PASSEGGERO_PARTITO:
			e.getPasseggero().setRitardo(e.getPasseggero().getRitardo()+e.getFlight().getArrivalDelay());
			Flight flight = dao.getNextFlight(airportIdMap.get(e.getFlight().getDestinationAirportId()), e.getFlight()) ;
			
			//segnalo che è arrivato
			Event arrivo = new Event(EventType.PASSEGGERO_ARRIVATO, flight, e.getPasseggero()) ;
			queue.add(arrivo) ;
			
			break;
			
		case PASSEGGERO_ARRIVATO:
			
			e.getPasseggero().setCurrentAirport(airportIdMap.get(e.getFlight().getDestinationAirportId())); 
			e.getPasseggero().setRitardo(e.getPasseggero().getRitardo()+e.getFlight().getDepartureDelay());
			Flight flight1 = dao.getNextFlight(airportIdMap.get(e.getFlight().getDestinationAirportId()), e.getFlight()) ;
			if(e.getPasseggero().getFlights().size() < V || flight1 == null) {
				//lo faccio partire
				Event partenza = new Event(EventType.PASSEGGERO_PARTITO, flight1, e.getPasseggero()) ;
				queue.add(partenza) ;
			}
			break;
		}
	}
	
	public List<Passeggero> getPassengerDelays() {
		return this.passeggeri ;
	}
	
}
