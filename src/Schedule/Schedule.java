/**
 * @author Dinis Raleiras (67819) d.raleiras@campus.fct.unl.pt
 * @author Filipe Nobre (67850) fm.nobre@campus.fct.unl.pt
 * @Fase 2
 */
package Schedule;

import java.io.Serializable;

import Date.Date;
import Station.*;
import dataStructures.*;

public interface Schedule extends Serializable{
	/**
	 * Returns the number of the train.
	 */
	int getNumber();

	/**
	 * Returns the schedules.
	 * @return the schedules.
	 */
	Iterator<Entry<Station, Date>> scheduleIterator();


}
