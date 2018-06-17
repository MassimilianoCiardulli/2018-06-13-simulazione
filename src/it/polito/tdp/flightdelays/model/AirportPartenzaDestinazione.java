package it.polito.tdp.flightdelays.model;

public class AirportPartenzaDestinazione implements Comparable<AirportPartenzaDestinazione>{
	private Airport partenza ;
	private Airport destinazione ;
	private double weight ;
	public AirportPartenzaDestinazione(Airport partenza, Airport destinazione) {
		super();
		this.partenza = partenza;
		this.destinazione = destinazione;
	}
	public Airport getPartenza() {
		return partenza;
	}
	public Airport getDestinazione() {
		return destinazione;
	}
	public String toString() {
		return this.partenza + " verso " + this.destinazione ;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	@Override
	public int compareTo(AirportPartenzaDestinazione other) {
		if(other.weight > this.weight)
			return 1 ;
		if(other.weight < this.weight)
			return -1 ;
		return 0;
	}
}
