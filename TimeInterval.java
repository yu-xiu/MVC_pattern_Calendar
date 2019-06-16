/**
 * TimeInterval class provides function to compare starting time of events, and it suitable for events
 */

import java.time.LocalTime;

public class TimeInterval implements Comparable<TimeInterval>{

    private LocalTime startTime;
    private LocalTime endTime;

    /**
     * Constructor
     * @param startTime starting time of an event
     * @param endTime end time of an event
     */
    public TimeInterval(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Comparing starting time
     * @param i integer
     * @return
     */
    public int compareTo(TimeInterval i) {
        if (i == null) {
            return 1;
        }
        if (this.startTime.compareTo(startTime) > 0) {
            return 1;
        } else if (this.startTime.compareTo(startTime) < 0){
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Get end time
     * @return endTime
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Get start time
     * @return
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Set end time
     * @param endTime
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * set start time
     * @param startTime
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Convert time to string
     * @return Sting of time interval
     */
    public String toString() {
        return startTime + " " + endTime;
    }
}