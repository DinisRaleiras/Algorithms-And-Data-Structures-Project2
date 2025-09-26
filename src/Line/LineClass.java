/**
 * @author Dinis Raleiras (67819) d.raleiras@campus.fct.unl.pt
 * @author Filipe Nobre (67850) fm.nobre@campus.fct.unl.pt
 * @Fase 2
 */
package Line;

import Exceptions.*;
import Station.*;
import dataStructures.*;
import Schedule.*;

import java.io.Serializable;

import Date.*;

public class LineClass implements LineUpdate, Serializable {

	//Serial Version UID
	static final long serialVersionUID = 0L;

	//Name of the line
	private String name;

	//Stations of the line
	private List<Station> stations;

	//Schedules of the main Itinerary
	private Dictionary<Date, Schedule> headSchedules;

	//Schedules of the reverse Itinerary
	private Dictionary<Date, Schedule> tailSchedules;

	/**
	 * Constructor of the class LineClass.
	 *
	 * @param name
	 */
	public LineClass(String name) {
		this.name = name;
		this.stations = new DoubleList<>();
		headSchedules = new AVLTree<>();
		tailSchedules = new AVLTree<>();
	}

	@Override
	public int compareTo(Line o) {
		return this.getName().compareTo(o.getName());
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Iterator<Station> getStations() {
		return stations.iterator();
	}

	@Override
	public Schedule bestSchedule(String departureStation, String arrivalStation, Date date) throws StationDoesNotExistException, ImpossibleRouteException {
		if (!hasStation(departureStation)) {
			throw new StationDoesNotExistException();
		} else if (!hasStation(arrivalStation)) {
			throw new ImpossibleRouteException();
		}
		Schedule bestSchedule = findBestSchedule(headSchedules.iterator(), departureStation, arrivalStation, date);
		if (bestSchedule == null) {
			bestSchedule = findBestSchedule(tailSchedules.iterator(), departureStation, arrivalStation, date);
		}
		if (bestSchedule == null) {
			throw new ImpossibleRouteException();
		} else {
			return bestSchedule;
		}
	}

	@Override
	public void addStation(Station station) {
		stations.addLast(station);
	}

	@Override
	public void removeAllSchedules() {
		Iterator<Entry<Date, Schedule>> it = headSchedules.iterator();
		while (it.hasNext()) {
			((ScheduleUpdate) it.next().getValue()).removeStops();
		}
		it = tailSchedules.iterator();
		while (it.hasNext()) {
			((ScheduleUpdate) it.next().getValue()).removeStops();
		}
	}

	@Override
	public void removeSchedule(String stationName, Date date) throws ScheduleDoesNotExistException {
		ScheduleUpdate schedule;
		if (stationName.equalsIgnoreCase(stations.getFirst().getName())) {
			schedule = (ScheduleUpdate) headSchedules.remove(date);
			if (schedule == null)
				throw new ScheduleDoesNotExistException();
		} else if (stationName.equalsIgnoreCase(stations.getLast().getName())) {
			schedule = (ScheduleUpdate) tailSchedules.remove(date);
			if (schedule == null)
				throw new ScheduleDoesNotExistException();
		} else {
			throw new ScheduleDoesNotExistException();
		}
		schedule.removeStops();
	}

	@Override
	public Iterator<Entry<Date, Schedule>> getSchedules(String stationName) throws StationDoesNotExistException {
		if (stationName.equalsIgnoreCase(stations.getFirst().getName())) {
			return headSchedules.iterator();
		} else if (stationName.equalsIgnoreCase(stations.getLast().getName())) {
			return tailSchedules.iterator();
		} else {
			throw new StationDoesNotExistException();
		}
	}

	@Override
	public void addSchedule(int trainNumber, Queue<Entry<String, Entry<Integer, Integer>>> schedule)
			throws InvalidScheduleException {
		if (schedule.front().getKey().equalsIgnoreCase(stations.getFirst().getName())) {
			addScheduleHead(trainNumber, schedule);
		} else if (schedule.front().getKey().equalsIgnoreCase(stations.getLast().getName())) {
			addScheduleTail(trainNumber, schedule);
		} else {
			throw new InvalidScheduleException();
		}
	}

	/**
	 * Adds the schedule thats starts in the head of the line
	 *
	 * @param trainNumber
	 * @param schedule
	 * @throws InvalidScheduleException
	 */
	private void addScheduleHead(int trainNumber, Queue<Entry<String, Entry<Integer, Integer>>> schedule)
			throws InvalidScheduleException {
		List<Entry<Station, Date>> scheduleList = new DoubleList<>();
		Date departureDate = new DateClass(schedule.front().getValue().getKey(), schedule.front().getValue().getValue());
		Iterator<Station> it = stations.iterator();
		Date lastDate = null;
		while (it.hasNext()) {
			Station station = it.next();
			if (!schedule.isEmpty() && station.getName().equalsIgnoreCase(schedule.front().getKey())) {
				Entry<String, Entry<Integer, Integer>> stop = schedule.dequeue();
				Date date = new DateClass(stop.getValue().getKey(), stop.getValue().getValue());
				if (lastDate != null && lastDate.compareTo(date) >= 0) {
					throw new InvalidScheduleException();
				} else {
					lastDate = date;
				}
				scheduleList.addLast(new EntryClass<>(station, date));
			}
		}
		if (schedule.isEmpty()) {
			ScheduleUpdate s = new ScheduleClass(trainNumber, scheduleList);
			if (hasOvertake(s, departureDate, headSchedules)){
				throw new InvalidScheduleException();
			}
			headSchedules.insert(departureDate, s);
			s.addStops();
		} else {
			throw new InvalidScheduleException();
		}
	}

	/**
	 * Adds the schedule that starts in the tail of the line
	 *
	 * @param trainNumber
	 * @param schedule
	 * @throws InvalidScheduleException
	 */
	private void addScheduleTail(int trainNumber, Queue<Entry<String, Entry<Integer, Integer>>> schedule) throws InvalidScheduleException {
		List<Entry<Station, Date>> scheduleList = new DoubleList<>();
		Date departureDate = new DateClass(schedule.front().getValue().getKey(), schedule.front().getValue().getValue());
		TwoWayIterator<Station> it = stations.twoWayIterator();
		it.fullForward();
		Date lastDate = null;
		while (it.hasPrevious()) {
			Station station = it.previous();
			if (!schedule.isEmpty() && station.getName().equalsIgnoreCase(schedule.front().getKey())) {
				Entry<String, Entry<Integer, Integer>> stop = schedule.dequeue();
				Date date = new DateClass(stop.getValue().getKey(), stop.getValue().getValue());
				if (lastDate != null && lastDate.compareTo(date) >= 0) {
					throw new InvalidScheduleException();
				} else {
					lastDate = date;
				}
				scheduleList.addLast(new EntryClass<>(station, date));
			}
		}
		if (schedule.isEmpty()) {
			ScheduleUpdate s = new ScheduleClass(trainNumber, scheduleList);
			if (hasOvertake(s, departureDate, tailSchedules)){
				throw new InvalidScheduleException();
			}
			tailSchedules.insert(departureDate, s);
			s.addStops();
		} else {
			throw new InvalidScheduleException();
		}
	}

	/**
	 * Returns true if the line has the station with the name stationName.
	 *
	 * @param stationName
	 * @return true if the line has the station with the name stationName.
	 */
	private boolean hasStation(String stationName) {
		Iterator<Station> it = stations.iterator();
		while (it.hasNext()) {
			if (it.next().getName().equalsIgnoreCase(stationName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the best schedule between the departureStation and the arrivalStation
	 * @param it
	 * @param departureStation
	 * @param arrivalStation
	 * @param date
	 * @return
	 */
	private Schedule findBestSchedule(Iterator<Entry<Date, Schedule>> it, String departureStation, String arrivalStation, Date date) {
		Schedule bestSchedule = null;
		int bestTime = Integer.MAX_VALUE;
		while (it.hasNext()) {
			ScheduleUpdate schedule = (ScheduleUpdate) it.next().getValue();
			int time = schedule.timeBetweenStations(departureStation, arrivalStation, date);
			if (time >= 0 && time < bestTime) {
				bestTime = time;
				bestSchedule = schedule;
			}

		}
		return bestSchedule;
	}


	/**
	 * Returns true if the schedule s has an overtake with the schedules in exitsSchedules
	 * @param s
	 * @param d1
	 * @param exitsSchedules
	 * @return
	 */
	private boolean hasOvertake(Schedule s, Date d1, Dictionary<Date, Schedule> exitsSchedules) {
		Iterator<Entry<Date, Schedule>> it = exitsSchedules.iterator();
		if(exitsSchedules == headSchedules) {
			while(it.hasNext()){
				Entry<Date,Schedule> entry = it.next();
				if(overtakesHead(s, entry.getValue(), d1, entry.getKey())){
					return true;
				}
			}
		}else {
			while(it.hasNext()){
				Entry<Date,Schedule> entry = it.next();
				if(overtakesTail(s, entry.getValue(), d1, entry.getKey())){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns true if the schedule s1 overtakes the schedule s2
	 * @param s1
	 * @param s2
	 * @param d1
	 * @param d2
	 * @return
	 */
	private boolean overtakesTail(Schedule s1, Schedule s2, Date d1, Date d2) {
		boolean overtake = false;
		Iterator<Entry<Station, Date>> it1 = s1.scheduleIterator();
		it1.next();
		Iterator<Entry<Station, Date>> it2 = s2.scheduleIterator();
		it2.next();
		
		if(d1.compareTo(d2) > 0) { // s2 esta a frente
			Entry<Station, Date> stop1 = it1.next();
			Entry<Station, Date> stop2 = it2.next();
			while(!overtake) {
				int index1 = stations.size() - 1 - stations.find(stop1.getKey());
				int index2 = stations.size() - 1 - stations.find(stop2.getKey());
				if(index1 == index2 && stop1.getValue().compareTo(stop2.getValue()) <= 0) {
					overtake = true;
				}else if(index1 > index2) {
					if(!it2.hasNext()) {
						break;
					}
					stop2 = it2.next();
				}else {
					if(!it1.hasNext()) {
						break;
					}
					stop1 = it1.next();
				}
			}
		}else if(d1.compareTo(d2) < 0) { // s1 esta a frente
			Entry<Station, Date> stop1 = it1.next();
			Entry<Station, Date> stop2 = it2.next();
			while(!overtake) {
				int index1 = stations.size() - 1 - stations.find(stop1.getKey());
				int index2 = stations.size() - 1 - stations.find(stop2.getKey());
				if(index1 == index2 && stop1.getValue().compareTo(stop2.getValue()) >= 0) {
					overtake = true;
				}else if(index1 > index2) {
					if(!it2.hasNext()) {
						break;
					}
					stop2 = it2.next();
				}else {
					if(!it1.hasNext()) {
						break;
					}
					stop1 = it1.next();
				}
			}
		}else {
			return true;
		}
		return overtake;
	}

	/**
	 * Returns true if the schedule s1 overtakes the schedule s2
	 * @param s1
	 * @param s2
	 * @param d1
	 * @param d2
	 * @return
	 */
	private boolean overtakesHead(Schedule s1, Schedule s2, Date d1, Date d2) {
		boolean overtake = false;
		Iterator<Entry<Station, Date>> it1 = s1.scheduleIterator();
		it1.next();
		Iterator<Entry<Station, Date>> it2 = s2.scheduleIterator();
		it2.next();
		
		if(d1.compareTo(d2) > 0) { // s2 esta a frente
			Entry<Station, Date> stop1 = it1.next();
			Entry<Station, Date> stop2 = it2.next();
			while(!overtake) {
				int index1 = stations.find(stop1.getKey());
				int index2 = stations.find(stop2.getKey());
				if(index1 == index2 && stop1.getValue().compareTo(stop2.getValue()) <= 0) {
					overtake = true;
				}else if(index1 > index2) {
					if(!it2.hasNext()) {
						break;
					}
					stop2 = it2.next();
				}else {
					if(!it1.hasNext()) {
						break;
					}
					stop1 = it1.next();
				}
			}
		}else if(d1.compareTo(d2) < 0) { // s1 esta a frente
			Entry<Station, Date> stop1 = it1.next();
			Entry<Station, Date> stop2 = it2.next();
			while(!overtake) {
				int index1 = stations.find(stop1.getKey());
				int index2 = stations.find(stop2.getKey());
				if(index1 == index2 && stop1.getValue().compareTo(stop2.getValue()) >= 0) {
					overtake = true;
				}else if(index1 > index2) {
					if(!it2.hasNext()) {
						break;
					}
					stop2 = it2.next();
				}else {
					if(!it1.hasNext()) {
						break;
					}
					stop1 = it1.next();
				}
			}
		}else {
			return true;
		}
		return overtake;
	}

	/**
	 * Writes the object in the ObjectOutputStream. Used due to serialization problems.
	 * @param s
	 * @throws java.io.IOException
	 */
	private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
		s.writeObject(name);
		
		s.writeInt(stations.size());
		Iterator<Station> it1 = stations.iterator();
		while(it1.hasNext()) {
			s.writeObject(it1.next());
		}
		
		s.writeInt(headSchedules.size());
		Iterator<Entry<Date, Schedule>> it2 = headSchedules.iterator();
		while(it2.hasNext()) {
			Entry<Date, Schedule> entry = it2.next();
			s.writeObject(entry.getKey());
			s.writeObject(entry.getValue());
		}
		
		s.writeInt(tailSchedules.size());
		it2 = tailSchedules.iterator();
		while(it2.hasNext()) {
			Entry<Date, Schedule> entry = it2.next();
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
		name = (String) s.readObject();
		this.stations = new DoubleList<>();
		int size = s.readInt();
		for(int i = 0; i < size; i++) {
			stations.addLast((Station)s.readObject());
		}
		this.headSchedules = new AVLTree<>();
		size = s.readInt();
		for(int i = 0; i < size; i++) {
			headSchedules.insert((Date)s.readObject(), (Schedule)s.readObject());
		}
		this.tailSchedules = new AVLTree<>();
		size = s.readInt();
		for(int i = 0; i < size; i++) {
			tailSchedules.insert((Date)s.readObject(), (Schedule)s.readObject());
		}
		
		Iterator<Entry<Date, Schedule>> it = headSchedules.iterator();
		while(it.hasNext()) {
			((ScheduleUpdate) it.next().getValue()).addStops();
		}
		it = tailSchedules.iterator();
		while(it.hasNext()) {
			((ScheduleUpdate) it.next().getValue()).addStops();
		}
	}

}