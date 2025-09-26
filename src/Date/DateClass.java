/**
 * @author Dinis Raleiras (67819) d.raleiras@campus.fct.unl.pt
 * @author Filipe Nobre (67850) fm.nobre@campus.fct.unl.pt
 * @Fase 2
 */
package Date;
import java.io.Serializable;
public class DateClass implements Date, Serializable {

	// Serial Version UID
	private static final long serialVersionUID = 0L;

	// Hours of the date
	private int hour;

	// Minutes of the date
	private int minutes;

	/**
	 * Constructor of the class DateClass.
	 * @param hour the hour of the date.
	 * @param minutes the minutes of the date.
	 */
	public DateClass(int hour, int minutes) {
		this.hour = hour;
		this.minutes = minutes;
	}

	@Override
	public int getHour() {
		return hour;
	}

	@Override
	public int getMinutes() {
		return minutes;
	}

	@Override
	public int difOfTime(Date date) {
		int time1 = date.getHour()*60 + date.getMinutes();
		int time2 = this.getHour()*60 + this.getMinutes();
		return time1 - time2;
	}

	@Override
	public int compareTo(Date o) {
		if(this.getHour() > o.getHour()){
			return 1;
		}else if(this.getHour() < o.getHour()){
			return -1;
		} else if(this.getMinutes() > o.getMinutes()){
			return 1;
		}
		else if(this.getMinutes() < o.getMinutes()){
			return -1;
		}else {
			return 0;
		}
	}
}
