/**
 * Event class provide comparison between two events based on dates
 * It used to create event object
 */
import java.time.LocalDate;
import java.time.LocalTime;

public class Event implements Comparable<Event>{

    private String eventName;
    private String weekOfDays;
    private LocalDate currentDate;
    private LocalDate startDate;
    private LocalDate endDate;
    //private boolean isRegular;
    private TimeInterval timeInterval;

    /**
     * Constructor for one time event
     * @param eventName
     * @param startDate
     * @param startTime
     * @param endTime
     */
    public Event(String eventName, LocalDate startDate, LocalTime startTime, LocalTime endTime) {
        this.startDate = startDate;
        this.endDate = startDate;
        this.currentDate = startDate;
        this.timeInterval = new TimeInterval(startTime, endTime);
        this.eventName = eventName;
        //this.isRegular = false;
    }

    /**
     * Constructor for comparision
     * @param currentDate
     * @param startDate
     * @param endDate
     */
    public Event (LocalDate currentDate, LocalDate startDate, LocalDate endDate) {
        this.currentDate = currentDate;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Get the date of currentDate, which could be any day between start date and end date
     * @return current date
     */
    public LocalDate getCurrentDate() {
        return currentDate;
    }

    /**
     * Get the start date of a regular event
     * @return start date of a regular event
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Get the end date of a regular event
     * @return end date of a regular event
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Get the start time of an event
     * @return start time of an event
     */
    public LocalTime getStartTime() {
        return timeInterval.getStartTime();
    }

    /**
     * Get end time of an event
     * @return end time of an event
     */
    public LocalTime getEndTime() {
        return timeInterval.getEndTime();
    }

    /**
     * Get event name
     * @return a string of event name
     */
    public String getEventName() {
        return eventName;
    }

    /**
     *  Get the week days of an event
     * @return a string of the week days
     */
    public String getWeekOfDays() {
        return weekOfDays;
    }

    /**
     *  Set the start date
     * @param newStartDate
     */
    public void setStartDate(LocalDate newStartDate) {
        startDate = newStartDate;
    }

    /**
     * Set end time
     * @param newEndDate
     */
    public void setEndtDate(LocalDate newEndDate) {
        endDate = newEndDate;
    }

    /**
     * set week of days
     * @param newWeekOfDays
     */
    public void setWeekOfDays(String newWeekOfDays) {
        weekOfDays = newWeekOfDays;
    }

    /**
     * set start time
     * @param newStartTime
     */
    public void setStartTime(LocalTime newStartTime) {
        timeInterval.setStartTime(newStartTime);
    }

    /**
     * set end time
     * @param newEndTime
     */
    public void setEndTime(LocalTime newEndTime) {
        timeInterval.setEndTime(newEndTime);
    }

    public String getScheduledString () {
        return eventName + "\n" + startDate+ " " + timeInterval;

    }

    /**
     * Compare two events based on its current date and end date
     * @param i
     * @return
     */
    public int compareTo(Event i) {
        if (i == null) {
            return 1;
        }
        if (this.endDate.compareTo(currentDate) > 0) {
            return 1;
        } else if (this.endDate.compareTo(currentDate) < 0){
            return -1;
        } else {
            return timeInterval.compareTo(i.timeInterval);
        }
    }

    /**
     * A to String method
     * @return a string with whole information of an event
     */
    public String toString() {
        return eventName + " " + startDate + " " + timeInterval.getStartTime() + " " + timeInterval.getEndTime();
    }
}
