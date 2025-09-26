/**
 * @author Dinis Raleiras (67819) d.raleiras@campus.fct.unl.pt
 * @author Filipe Nobre (67850) fm.nobre@campus.fct.unl.pt
 * @Fase 2
 */
package Line;

import java.io.Serializable;

import Exceptions.*;
import Date.*;
import Schedule.Schedule;
import dataStructures.*;
import Station.*;
public interface Line extends Comparable<Line>, Serializable{

	/**
	 * Returns the name of the line.
	 * @return the name of the line.
	 */
	String getName();

	/**
	 * Returns the stations of the line.
	 * @return the stations of the line.
	 */
	Iterator<Station> getStations();

	/**
	 * Returns the best schedule of the line given the departure station, arrival station and date.
	 * @param departureStation
	 * @param arrivalStation
	 * @param date
	 * @return the best schedule
	 */
    Schedule bestSchedule(String departureStation, String arrivalStation, Date date) throws StationDoesNotExistException, ImpossibleRouteException;
}
