package it.polito.tdp.flightdelays.model;

import java.util.ArrayList;
import java.util.List;

public class Passeggero {
	private int idPasseggero ;
	private double ritardo ;
	private List<Flight> flights ;
	private Airport currentAirport ;
	
	public Passeggero(int idPasseggero, double ritardo) {
		super();
		this.idPasseggero = idPasseggero;
		this.ritardo = ritardo;
		flights = new ArrayList<>();
	}
	public int getIdPasseggero() {
		return idPasseggero;
	}
	public void setIdPasseggero(int idPasseggero) {
		this.idPasseggero = idPasseggero;
	}
	public double getRitardo() {
		return ritardo;
	}
	public void setRitardo(double ritardo) {
		this.ritardo = ritardo;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idPasseggero;
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
		Passeggero other = (Passeggero) obj;
		if (idPasseggero != other.idPasseggero)
			return false;
		return true;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Passeggero [idPasseggero=");
		builder.append(idPasseggero);
		builder.append(", ritardo=");
		builder.append(ritardo);
		builder.append("]");
		return builder.toString();
	}
	public List<Flight> getFlights() {
		return flights;
	}
	
	public void addFlight(Flight f) {
		flights.add(f);
	}
	public Airport getCurrentAirport() {
		return currentAirport;
	}
	public void setCurrentAirport(Airport firstAirport) {
		this.currentAirport = firstAirport;
	}
}
