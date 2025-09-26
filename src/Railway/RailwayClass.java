/**
 * @author Dinis Raleiras (67819) d.raleiras@campus.fct.unl.pt
 * @author Filipe Nobre (67850) fm.nobre@campus.fct.unl.pt
 * @Fase 2
 */
package Railway;

import dataStructures.*;
import Line.*;
import Schedule.*;
import Station.*;
import Exceptions.*;

import java.io.Serializable;

import Date.*;

public class RailwayClass implements Railway, Serializable{

	// Serial Version UID
	static final long serialVersionUID = 0L;

	// Lines of the railway
	private Dictionary<String, Line> lines;

	// Stations of the railway
	private Dictionary<String, Station> stations;


	/**
	 * Constructor of the class RailwayClass.

	 */
	public RailwayClass() {
		lines = new InsensitiveSepChainHashTable<>();
		stations = new InsensitiveSepChainHashTable<>();
	}


	@Override
	public void addLine(String lineName, Queue<String> stationsNames) throws LineAlreadyExistsException {
		LineUpdate line = (LineUpdate) lines.find(lineName);
		if(line != null)
			throw new LineAlreadyExistsException();
		line = new LineClass(lineName);
		while(!stationsNames.isEmpty()) {
			String stationName = stationsNames.dequeue();
			StationUpdate station = (StationUpdate) stations.find(stationName);
			if(station == null) {
				station = new StationClass(stationName);
				stations.insert(stationName, station);
			}
			line.addStation(station);
			station.addLine(line);
		}
		lines.insert(lineName, line);
	}


	@Override
	public Iterator<Station> lineStationsIterator(String lineName) throws LineDoesNotExistException {
		Line line = lines.find(lineName);
		if(line == null)
			throw new LineDoesNotExistException();
		return line.getStations();
	}


	@Override
	public Iterator<Line> stationLinesIterator(String stationName) throws StationDoesNotExistException {
		Station station = stations.find(stationName);
		if(station == null)
			throw new StationDoesNotExistException();
		return station.getLines();
	}


	@Override
	public void removeLine(String lineName) throws LineDoesNotExistException{
		LineUpdate line = (LineUpdate) lines.remove(lineName);
		if(line == null)
			throw new LineDoesNotExistException();
		line.removeAllSchedules();
		Iterator<Station> stationsIterator = line.getStations();
		while(stationsIterator.hasNext()) {
			StationUpdate station = (StationUpdate) stationsIterator.next();
			station.removeLine((Line) line);
			if(station.hasNoLines()) {
				stations.remove(station.getName());
			}
		}
	}


	@Override
	public void removeSchedule(String lineName, String stationName, int hours, int minutes) throws LineDoesNotExistException, ScheduleDoesNotExistException{
		LineUpdate line = (LineUpdate) lines.find(lineName);
		if(line == null)
			throw new LineDoesNotExistException();
		line.removeSchedule(stationName, new DateClass(hours, minutes));
		
	}


	@Override
	public Iterator<Entry<Date, Schedule>> lineSchedulesIterator(String lineName, String stationName)
			throws LineDoesNotExistException, StationDoesNotExistException {
		LineUpdate line = (LineUpdate) lines.find(lineName);
		if(line == null)
			throw new LineDoesNotExistException();
		return line.getSchedules(stationName);
	}

	@Override
	public Iterator<Entry<Integer, Date>> stationSchedulesIterator(String stationName) throws StationDoesNotExistException {
		StationUpdate station = (StationUpdate) stations.find(stationName);
		if (station == null)
			throw new StationDoesNotExistException();
		return station.getShedules();
	}

	@Override
	public Schedule bestSchedule(String lineName, String departureStation, String arrivalStation, int hours, int minutes) throws LineDoesNotExistException, StationDoesNotExistException, ImpossibleRouteException{
		Line line = lines.find(lineName);
		if(line == null)
			throw new LineDoesNotExistException();
		return line.bestSchedule(departureStation, arrivalStation, new DateClass(hours, minutes));
	}

	@Override
	public void addSchedule(String lineName, int trainNumber, Queue<Entry<String, Entry<Integer, Integer>>> schedule) throws LineDoesNotExistException, InvalidScheduleException {
		LineUpdate line = (LineUpdate) lines.find(lineName);
		if(line == null)
			throw new LineDoesNotExistException();
		line.addSchedule(trainNumber, schedule);
	}
	/**
	 * Writes the object in the ObjectOutputStream. Used due to serialization problems.
	 * @param s
	 * @throws java.io.IOException
	 */
	private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
	    s.writeInt(lines.size());
	    s.writeInt(stations.size());

	    // Serialize the lines
	    for (Iterator<Entry<String, Line>> it = lines.iterator(); it.hasNext(); ) {
	        Entry<String, Line> entry = it.next();
	        s.writeObject(entry.getKey());
	        s.writeObject(entry.getValue());
	    }

	    // Serialize the stations
	    for (Iterator<Entry<String, Station>> it = stations.iterator(); it.hasNext(); ) {
	        Entry<String, Station> entry = it.next();
	        s.writeObject(entry.getKey());
	        s.writeObject(entry.getValue());
	    }
	}

	/**
	 * Reads the object from the ObjectInputStream. Used due to serialization problems.
	 * @param s
	 * @throws java.io.IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
	    int linesSize = s.readInt();
	    int stationsSize = s.readInt();

	    this.lines = new InsensitiveSepChainHashTable<>();
	    this.stations = new InsensitiveSepChainHashTable<>();

	    for (int i = 0; i < linesSize; i++) {
	        String lineName = (String) s.readObject();
	        Line line = (Line) s.readObject();
	        lines.insert(lineName, line);
	    }

	    for (int i = 0; i < stationsSize; i++) {
	        String stationName = (String) s.readObject();
	        Station station = (Station) s.readObject();
	        stations.insert(stationName, station);
	    }

	    for (Iterator<Entry<String, Line>> it = lines.iterator(); it.hasNext(); ) {
	        Entry<String, Line> entry = it.next();
	        Line line = entry.getValue();
	        
	        Iterator<Station> lineStations = line.getStations();
	        while (lineStations.hasNext()) {
	            Station station = lineStations.next();
	            ((StationUpdate)station).addLine((LineUpdate) line);
	        }
	    }
	}
}
