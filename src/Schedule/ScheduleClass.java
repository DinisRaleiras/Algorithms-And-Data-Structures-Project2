/**
 * @author Dinis Raleiras (67819) d.raleiras@campus.fct.unl.pt
 * @author Filipe Nobre (67850) fm.nobre@campus.fct.unl.pt
 * @Fase 2
 */
package Schedule;

import dataStructures.*;
import Station.*;

import java.io.Serializable;

import Date.*;

public class ScheduleClass implements ScheduleUpdate, Serializable{

	// Serial Version UID
	static final long serialVersionUID = 0L;

	// Number of the train
	private int number;

	// Schedule of the train
	private List<Entry<Station, Date>> schedule;

	/**
	 * Constructor of the class ScheduleClass.
	 * @param number
	 * @param schedule
	 */
	public ScheduleClass(int number, List<Entry<Station, Date>> schedule) {
		this.number = number;
		this.schedule = schedule;
	}

	@Override
	public int getNumber() {
		return number;
	}

	@Override
	public void removeStops() {
		Iterator<Entry<Station, Date>> it = schedule.iterator();
		while(it.hasNext()) {
			Entry<Station, Date> stop = it.next();
			((StationUpdate) stop.getKey()).removeSchedule(this.getNumber(), stop.getValue());
		}
	}

	@Override
	public int timeBetweenStations(String departureStation, String arrivalStation,Date date) {
		Iterator<Entry<Station, Date>> it = schedule.iterator();
		boolean foundDeparture = false;
		while (it.hasNext()) {
			Entry<Station, Date> stop = it.next();
			if (stop.getKey().getName().equalsIgnoreCase(departureStation.toUpperCase())) {
				foundDeparture = true;
			}else if(foundDeparture && stop.getKey().getName().equalsIgnoreCase(arrivalStation.toUpperCase())){
				return stop.getValue().difOfTime(date);
			}
		}
		return -1;
	}

	@Override
	public void addStops() {
		Iterator<Entry<Station, Date>> it = schedule.iterator();
		while(it.hasNext()) {
			Entry<Station, Date> entry = it.next();
			((StationUpdate) entry.getKey()).addSchedule(this.getNumber(), entry.getValue());
		}
	}

	@Override
	public Iterator<Entry<Station, Date>> scheduleIterator() {
		return schedule.iterator();
	}

	/**
	 * Writes the object in the ObjectOutputStream. Used due to serialization problems.
	 * @param s
	 * @throws java.io.IOException
	 */
	private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
		s.writeInt(number);
		s.writeInt(schedule.size());
		Iterator<Entry<Station, Date>> it = schedule.iterator();
		while(it.hasNext()) {
			Entry<Station, Date> stop = it.next();
			s.writeObject(stop.getKey());
			s.writeObject(stop.getValue());
		}
	}

	/**
	 * Reads the object in the ObjectOutputStream. Used due to serialization problems.
	 * @param s
	 * @throws java.io.IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
		number = s.readInt();
		int size = s.readInt();
		schedule = new DoubleList<>();
		for(int i = 0; i < size; i++) {
			schedule.addLast(new EntryClass<>((Station)s.readObject(), (Date)s.readObject()));
		}
	}
}
