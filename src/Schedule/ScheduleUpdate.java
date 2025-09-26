/**
 * @author Dinis Raleiras (67819) d.raleiras@campus.fct.unl.pt
 * @author Filipe Nobre (67850) fm.nobre@campus.fct.unl.pt
 * @Fase 2
 */
package Schedule;

import Date.*;

public interface ScheduleUpdate extends Schedule{
	/**
	 * Removes the schedule from the station.
	 */
	void removeStops();

	/**
	 * Returns the time between the stations.
	 * @param departureStation
	 * @param arrivalStation
	 * @param date
	 * @return the time between the stations.
	 */
    int timeBetweenStations(String departureStation, String arrivalStation, Date date);

	/**
	 * Adds the schedule to the station.
	 */
	void addStops();
}
