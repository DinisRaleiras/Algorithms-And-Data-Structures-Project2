/**
 * @author Dinis Raleiras (67819) d.raleiras@campus.fct.unl.pt
 * @author Filipe Nobre (67850) fm.nobre@campus.fct.unl.pt
 * @Fase 2
 */
package Station;

import java.io.Serializable;

import Line.*;
import dataStructures.*;
import Date.*;

public interface Station extends Serializable{
	/**
	 * Returns the name of the station.
	 * @return the name of the station.
	 */
	String getName();

	/**
	 * Returns the lines of the station.
	 * @return the lines of the station.
	 */
	Iterator<Line> getLines();

	/**
	 * Returns the schedules of the station.
	 * @return the schedules of the station.
	 */
	Iterator<Entry<Integer, Date>> getShedules();

	/**
	 * Compares the station with the object.
	 * @param o
	 * @return
	 */
	boolean equals(Object o);
}
