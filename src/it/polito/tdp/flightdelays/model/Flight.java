package it.polito.tdp.flightdelays.model;

import java.time.LocalDateTime;

public class Flight {

	private int id;
	private String airlineId;
	private int flightNumber;
	private String originAirportId;
	private String destinationAirportId;
	private LocalDateTime scheduledDepartureDate;
	private LocalDateTime arrivalDate;
	private int departureDelay;
	private int arrivalDelay;
	private int airTime;
	private int distance;

	public Flight(int id, String airlineId, int flightNumber, String originAirportId, String destinationAirportId,
			LocalDateTime scheduledDepartureDate, LocalDateTime arrivalDate, int departureDelay, int arrivalDelay,
			int airTime, int distance) {
		this.id = id;
		this.airlineId = airlineId;
		this.flightNumber = flightNumber;
		this.originAirportId = originAirportId;
		this.destinationAirportId = destinationAirportId;
		this.scheduledDepartureDate = scheduledDepartureDate;
		this.arrivalDate = arrivalDate;
		this.departureDelay = departureDelay;
		this.arrivalDelay = arrivalDelay;
		this.airTime = airTime;
		this.distance = distance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAirlineId() {
		return airlineId;
	}

	public void setAirlineId(String airlineId) {
		this.airlineId = airlineId;
	}

	public int getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(int flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getOriginAirportId() {
		return originAirportId;
	}

	public void setOriginAirportId(String originAirportId) {
		this.originAirportId = originAirportId;
	}

	public String getDestinationAirportId() {
		return destinationAirportId;
	}

	public void setDestinationAirportId(String destinationAirportId) {
		this.destinationAirportId = destinationAirportId;
	}

	public LocalDateTime getScheduledDepartureDate() {
		return scheduledDepartureDate;
	}

	public void setScheduledDepartureDate(LocalDateTime scheduledDepartureDate) {
		this.scheduledDepartureDate = scheduledDepartureDate;
	}

	public LocalDateTime getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(LocalDateTime arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public int getDepartureDelay() {
		return departureDelay;
	}

	public void setDepartureDelay(int departureDelay) {
		this.departureDelay = departureDelay;
	}

	public int getArrivalDelay() {
		return arrivalDelay;
	}

	public void setArrivalDelay(int arrivalDelay) {
		this.arrivalDelay = arrivalDelay;
	}

	public int getAirTime() {
		return airTime;
	}

	public void setAirTime(int airTime) {
		this.airTime = airTime;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Flight [id=");
		builder.append(id);
		builder.append(", airlineId=");
		builder.append(airlineId);
		builder.append(", flightNumber=");
		builder.append(flightNumber);
		builder.append(", originAirportId=");
		builder.append(originAirportId);
		builder.append(", destinationAirportId=");
		builder.append(destinationAirportId);
		builder.append(", scheduledDepartureDate=");
		builder.append(scheduledDepartureDate);
		builder.append(", arrivalDate=");
		builder.append(arrivalDate);
		builder.append(", departureDelay=");
		builder.append(departureDelay);
		builder.append(", arrivalDelay=");
		builder.append(arrivalDelay);
		builder.append(", airTime=");
		builder.append(airTime);
		builder.append(", distance=");
		builder.append(distance);
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + airTime;
		result = prime * result + ((airlineId == null) ? 0 : airlineId.hashCode());
		result = prime * result + ((arrivalDate == null) ? 0 : arrivalDate.hashCode());
		result = prime * result + arrivalDelay;
		result = prime * result + departureDelay;
		result = prime * result + ((destinationAirportId == null) ? 0 : destinationAirportId.hashCode());
		result = prime * result + distance;
		result = prime * result + flightNumber;
		result = prime * result + id;
		result = prime * result + ((originAirportId == null) ? 0 : originAirportId.hashCode());
		result = prime * result + ((scheduledDepartureDate == null) ? 0 : scheduledDepartureDate.hashCode());
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
		Flight other = (Flight) obj;
		if (airTime != other.airTime)
			return false;
		if (airlineId == null) {
			if (other.airlineId != null)
				return false;
		} else if (!airlineId.equals(other.airlineId))
			return false;
		if (arrivalDate == null) {
			if (other.arrivalDate != null)
				return false;
		} else if (!arrivalDate.equals(other.arrivalDate))
			return false;
		if (arrivalDelay != other.arrivalDelay)
			return false;
		if (departureDelay != other.departureDelay)
			return false;
		if (destinationAirportId == null) {
			if (other.destinationAirportId != null)
				return false;
		} else if (!destinationAirportId.equals(other.destinationAirportId))
			return false;
		if (distance != other.distance)
			return false;
		if (flightNumber != other.flightNumber)
			return false;
		if (id != other.id)
			return false;
		if (originAirportId == null) {
			if (other.originAirportId != null)
				return false;
		} else if (!originAirportId.equals(other.originAirportId))
			return false;
		if (scheduledDepartureDate == null) {
			if (other.scheduledDepartureDate != null)
				return false;
		} else if (!scheduledDepartureDate.equals(other.scheduledDepartureDate))
			return false;
		return true;
	}

}
