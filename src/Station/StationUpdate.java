/**
 * @author Dinis Raleiras (67819) d.raleiras@campus.fct.unl.pt
 * @author Filipe Nobre (67850) fm.nobre@campus.fct.unl.pt
 * @Fase 2
 */
package Station;

import Date.Date;
import Line.*;

public interface StationUpdate extends Station{
	/**
	 * Adds a Line to the station.
	 * @param line
	 */
	void addLine(LineUpdate line);

	/**
	 * Removes a certain schedule from the station based on the train number.
	 * @param number - the number of the train.
	 */
	void removeSchedule(int number, Date date);

	/**
	 * Removes the line from the station.
	 * @param line
	 */
	void removeLine(Line line);

	/**
	 * Checks if the station has no lines.
	 * @return true if the station has no lines, false otherwise.
	 */
	boolean hasNoLines();

	/**
	 * Adds a schedule to the station.
	 * @param number - the number of the train.
	 * @param value - the date of the schedule.
	 */
	void addSchedule(int number, Date value);
}
