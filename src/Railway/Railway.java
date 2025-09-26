/**
 * @author Dinis Raleiras (67819) d.raleiras@campus.fct.unl.pt
 * @author Filipe Nobre (67850) fm.nobre@campus.fct.unl.pt
 * @Fase 2
 */
package Railway;
import java.io.Serializable;
import Date.*;
import Exceptions.*;
import Line.*;
import Schedule.*;
import Station.*;
import dataStructures.*;

public interface Railway extends Serializable{

	/**
	 * Adds a line to the railway.
	 * @param lineName
	 * @param stationsNames
	 * @throws LineAlreadyExistsException
	 */
	void addLine(String lineName, Queue<String> stationsNames) throws LineAlreadyExistsException;

	/**
	 * Returns the stations of the line.
	 * @param lineName
	 * @return the stations of the line.
	 * @throws LineDoesNotExistException
	 */
	Iterator<Station> lineStationsIterator(String lineName) throws LineDoesNotExistException;

	/**
	 * Returns an iterator of the lines of the station.
	 * @param stationName
	 * @return
	 * @throws StationDoesNotExistException
	 */
	Iterator<Line> stationLinesIterator(String stationName) throws StationDoesNotExistException;

	/**
	 * Removes a line from the railway.
	 * @param lineName
	 * @throws LineDoesNotExistException
	 */
	void removeLine(String lineName) throws LineDoesNotExistException;

	/**
	 * Removes a schedule from the line.
	 * @param lineName
	 * @param stationName
	 * @param hours
	 * @param minutes
	 * @throws LineDoesNotExistException
	 * @throws ScheduleDoesNotExistException
	 */
	void removeSchedule(String lineName, String stationName, int hours, int minutes) throws LineDoesNotExistException, ScheduleDoesNotExistException;

	/**
	 * Returns the schedules of the line.
	 * @param lineName
	 * @param stationName
	 * @return
	 * @throws LineDoesNotExistException
	 * @throws StationDoesNotExistException
	 */
	Iterator<Entry<Date, Schedule>> lineSchedulesIterator(String lineName, String stationName) throws LineDoesNotExistException, StationDoesNotExistException;

	/**
	 * Returns an Iterator of the schedules of the station.
	 * @param stationName
	 * @return an Iterator of the schedules of the station.
	 * @throws StationDoesNotExistException
	 */
	Iterator<Entry<Integer, Date>> stationSchedulesIterator(String stationName) throws StationDoesNotExistException;

	/**
	 * Returns the best schedule from the railway given a line, departure station, arrival station, hours and minutes.
	 * @param lineName
	 * @param departureStation
	 * @param arrivalStation
	 * @param hours
	 * @param minutes
	 * @return the best schedule
	 */
    Schedule bestSchedule(String lineName, String departureStation, String arrivalStation, int hours, int minutes) throws LineDoesNotExistException, StationDoesNotExistException, ImpossibleRouteException;

	/**
	 * Adds a schedule to a certain line.
	 * @param lineName
	 * @param trainNumber
	 * @param schedule
	 */
	void addSchedule(String lineName, int trainNumber, Queue<Entry<String, Entry<Integer, Integer>>> schedule) throws LineDoesNotExistException, InvalidScheduleException;
}
