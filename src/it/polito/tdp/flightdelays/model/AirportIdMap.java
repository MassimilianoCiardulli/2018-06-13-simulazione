package it.polito.tdp.flightdelays.model;

import java.util.HashMap;
import java.util.Map;

public class AirportIdMap {
	private Map<String, Airport> map ;
	
	public AirportIdMap() {
		map = new HashMap<>();
	}
	public Airport get(String id) {
		return map.get(id);
	}
	
	public Airport get(Airport airport) {
		Airport old = map.get(airport.getId());
		if(old == null) {
			map.put(airport.getId(), airport);
			return airport ;
		} else {
			return old ;
		}
	}
	
	public void put(String id, Airport object) {
		map.put(id, object);
	}
}
