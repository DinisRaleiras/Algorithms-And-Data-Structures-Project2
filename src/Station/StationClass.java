/**
 * @author Dinis Raleiras (67819) d.raleiras@campus.fct.unl.pt
 * @author Filipe Nobre (67850) fm.nobre@campus.fct.unl.pt
 * @Fase 2
 */
package Station;

import dataStructures.*;
import Line.*;

import java.io.Serializable;

import Date.*;

public class StationClass implements StationUpdate, Serializable{

	//Serial Version UID
	static final long serialVersionUID = 0L;

	//Name of the station
	private String name;

	//Lines of the station. This variable is transient because it is not serializable. And its more efficient to recreate it when needed.
	private transient Set<Line> lines;

	//Schedules of the station. This variable is transient because it is not serializable. And its more efficient to recreate it when needed.
	private transient ValueDictionary<Integer, Date> schedules;


	/**
	 * Constructor of the class StationClass.
	 * @param name
	 */
	public StationClass(String name) {
		this.name = name;
		lines = new TreeSet<>();
		schedules = new AVLTreeByValue<>();
	}


	@Override
	public void addLine(LineUpdate line) {
		lines.insert(line);
	}


	@Override
	public String getName() {
		return this.name;
	}


	@Override
	public Iterator<Line> getLines() {
		return lines.iterator();
	}


	@Override
	public Iterator<Entry<Integer, Date>> getShedules() {
		return schedules.iterator();
	}


	@Override
	public void removeSchedule(int number, Date date) {
		schedules.remove(number, date);
	}


	@Override
	public void removeLine(Line line) {
		lines.remove(line);
	}


	@Override
	public boolean hasNoLines() {
		return lines.isEmpty();
	}

	@Override
	public void addSchedule(int number, Date value) {
		schedules.insert(number, value);
	}

	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}else if(!(o instanceof StationUpdate)) {
			return false;
		}
		return this.getName().equalsIgnoreCase(((StationUpdate)o).getName());
	}
	/**
	 * Writes the object in the ObjectOutputStream. Used due to serialization problems.
	 * @param s
	 * @throws java.io.IOException
	 */
	private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
		s.writeObject(name);
	}

	/**
	 * Reads the object from the ObjectInputStream. Used due to serialization problems.
	 * @param s
	 * @throws java.io.IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
	    name = (String) s.readObject();// Deserialize all non-transient fields

	    // Initialize transient fields
	    this.schedules = new AVLTreeByValue<>(); // Initialize schedules to an empty dictionary
	    this.lines = new TreeSet<>(); // Initialize lines to an empty set
	}

}
