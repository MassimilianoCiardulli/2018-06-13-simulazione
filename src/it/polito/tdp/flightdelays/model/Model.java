package it.polito.tdp.flightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.flightdelays.db.FlightDelaysDAO;

public class Model {
	private FlightDelaysDAO dao ;
	private Graph<Airport, DefaultWeightedEdge> graph ;
	private AirportIdMap airportIdMap ;
	private List<AirportPartenzaDestinazione> edges ;

	public List<Airline> getAirlines() {
		dao = new FlightDelaysDAO();
		return dao.loadAllAirlines();
	}

	public void createGraph(Airline airline) {
		dao = new FlightDelaysDAO(); 
		graph = new DirectedWeightedMultigraph<>(DefaultWeightedEdge.class);
		airportIdMap = new AirportIdMap() ;
		List<Airport> airports = dao.loadAllAirports(airportIdMap);
		
		Graphs.addAllVertices(this.graph, airports) ;
		
		edges = dao.getPartenzaDestinazione(airline, airportIdMap) ;
		
		for(AirportPartenzaDestinazione e : edges) {
			double weight = calcolaPeso(e.getPartenza(), e.getDestinazione(), airline) ;
			e.setWeight(weight);
			Graphs.addEdge(this.graph, e.getPartenza(), e.getDestinazione(), weight) ;
		}
		System.out.println(graph.edgeSet().size());
	}

	private double calcolaPeso(Airport partenza, Airport destinazione, Airline airline) {
		dao = new FlightDelaysDAO();
		double avg = dao.getAvgDelay(partenza, destinazione, airline);
		double distance = LatLngTool.distance(new LatLng(partenza.getLatitude(), partenza.getLongitude()), 
				new LatLng(destinazione.getLatitude(), destinazione.getLongitude()), LengthUnit.KILOMETER);
		double weight = avg/distance ;
		return weight;
	}

	public List<AirportPartenzaDestinazione> getWorst10() {
		Collections.sort(edges);
		List<AirportPartenzaDestinazione> worst10 = new ArrayList<>();
		for(int i = 0 ; i < 10 ; i++) {
			worst10.add(edges.get(i));
		}
		return worst10;
	}
}
