/**
 * @author Dinis Raleiras (67819) d.raleiras@campus.fct.unl.pt
 * @author Filipe Nobre (67850) fm.nobre@campus.fct.unl.pt
 * @Fase 2
 */
package Line;
import Station.*;
import Exceptions.*;
import Schedule.*;
import Date.*;
import dataStructures.*;

public interface LineUpdate extends Line{

	/**
	 * Adds a station to the line.
	 * @param station
	 */
	void addStation(Station station);

	/**
	 * Removes all schedules of the line.
	 */
	void removeAllSchedules();

	/**
	 * Removes a schedule of the line.
	 * @param stationName
	 * @param date
	 * @throws ScheduleDoesNotExistException
	 */
	void removeSchedule(String stationName, Date date) throws ScheduleDoesNotExistException;

	/**
	 * Returns the schedules of the line.
	 * @param stationName
	 * @return
	 * @throws StationDoesNotExistException
	 */
	Iterator<Entry<Date, Schedule>> getSchedules(String stationName) throws StationDoesNotExistException;

	/**
	 * Adds a schedule to the line.
	 * @param trainNumber
	 * @param schedule
	 */
    void addSchedule(int trainNumber, Queue<Entry<String, Entry<Integer, Integer>>> schedule)throws InvalidScheduleException;
}