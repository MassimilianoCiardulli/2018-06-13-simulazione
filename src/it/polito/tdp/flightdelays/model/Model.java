package it.polito.tdp.flightdelays.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.flightdelays.db.FlightDelaysDAO;

public class Model {
	
	private FlightDelaysDAO dao ;
	private Graph<Airport, DefaultWeightedEdge> graph ;
	private AirportIdMap airportIdMap ;
	private List<PairAirportsCount> pairs ;
	private List<Airport> airports ;
	
	public Model() {
		
	}

	public List<Airline> getAirlines() {
		dao = new FlightDelaysDAO() ;
		return dao.loadAllAirlines();
	}

	public void createGraph(Airline airline) {
		this.graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		dao = new FlightDelaysDAO() ;
		this.airportIdMap = new AirportIdMap() ;
		
		airports = dao.loadAllAirports(airportIdMap) ;
		
		Graphs.addAllVertices(this.graph, airports) ;
		
		System.out.println(graph.vertexSet().size());
		
		pairs = dao.getPairs(airline, airportIdMap) ;
		
		for(PairAirportsCount pac : pairs) {
			double weight = getWeight(pac) ;
			pac.setAvg(weight);
			Graphs.addEdge(this.graph, pac.getAirport1(), pac.getAirport2(), weight) ;
		}
		
		System.out.println(graph.edgeSet().size());
	}

	private double getWeight(PairAirportsCount pac) {
		double distance = LatLngTool.distance(new LatLng(pac.getAirport1().getLatitude(), pac.getAirport1().getLongitude()), 
				new LatLng(pac.getAirport2().getLatitude(), pac.getAirport2().getLongitude()), LengthUnit.KILOMETER) ;
		double weight = pac.getAvg()/distance ;
		return weight;
	}

	public List<PairAirportsCount> getWorst10() {
		Collections.sort(this.pairs, new PairAirportsCount.OrderByAvg());		
		return new ArrayList<PairAirportsCount>(pairs.subList(0, 10));
	}

	public List<Passeggero> simula(int k, int v, Airline airline) {
		Simulator s = new Simulator() ;
		List<Airport> a = dao.loadAllAirports(airportIdMap);
		s.init(k, v, airports, airportIdMap);
		s.run();

		return s.getPassengerDelays();
	}

}
