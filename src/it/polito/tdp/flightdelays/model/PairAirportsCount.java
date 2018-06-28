package it.polito.tdp.flightdelays.model;

import java.util.Comparator;

public class PairAirportsCount {
	static class OrderByAvg implements Comparator<PairAirportsCount> {

		@Override
		public int compare(PairAirportsCount a, PairAirportsCount b) {
			if(b.avg > a.avg)
				return 1 ;
			if(b.avg < a.avg)
				return -1 ;
			return 0;
		}

	}
	private Airport airport1 ;
	private Airport airport2 ;
	private double avg ;
	public PairAirportsCount(Airport airport1, Airport airport2, double avg) {
		super();
		this.airport1 = airport1;
		this.airport2 = airport2;
		this.avg = avg;
	}
	public Airport getAirport1() {
		return airport1;
	}
	public void setAirport1(Airport airport1) {
		this.airport1 = airport1;
	}
	public Airport getAirport2() {
		return airport2;
	}
	public void setAirport2(Airport airport2) {
		this.airport2 = airport2;
	}
	public double getAvg() {
		return avg;
	}
	public void setAvg(double avg) {
		this.avg = avg;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((airport1 == null) ? 0 : airport1.hashCode());
		result = prime * result + ((airport2 == null) ? 0 : airport2.hashCode());
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
		PairAirportsCount other = (PairAirportsCount) obj;
		if (airport1 == null) {
			if (other.airport1 != null)
				return false;
		} else if (!airport1.equals(other.airport1))
			return false;
		if (airport2 == null) {
			if (other.airport2 != null)
				return false;
		} else if (!airport2.equals(other.airport2))
			return false;
		return true;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PairAirportsCount [airport1=");
		builder.append(airport1);
		builder.append(", airport2=");
		builder.append(airport2);
		builder.append(", avg=");
		builder.append(avg);
		builder.append("]");
		return builder.toString();
	}
	
}	
