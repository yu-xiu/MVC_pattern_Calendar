
/**
 * This class holds all the data in calender
 * It parse the event and add the event to event arrayList
 * It checks the overlapped time interval when creating a new event
 * It also attach actionListeners
 */

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CalendarDataModel {
    private LocalDate currentDate = LocalDate.now();
    private ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private TreeMap<LocalDate, ArrayList<Event>> eventMap = new TreeMap<>();

    /**
     * Construct a calendar data model
     * @param date
     */
    public CalendarDataModel(LocalDate date) {
        System.out.println("   " + date.getMonth() + " " + date.getYear());
        System.out.println(" Su Mo Tu We Th Fr Sa");

        // first day of month
        LocalDate ld = LocalDate.of(date.getYear(), date.getMonth(), 1);
        //System.out.println(ld.getDayOfWeek() + " is the day of " + ld.getMonth() + " 1.");

        // print body of the calender
        for (int i = 0; i < ld.getDayOfWeek().getValue(); i++) {
            System.out.print("   ");
        }
        for (int j = 1; j <= ld.lengthOfMonth(); j++) {
            LocalDate dt = LocalDate.of(date.getYear(), date.getMonth(), j);
            if (eventMap.containsKey(dt) && j == currentDate.getDayOfMonth()) {
                System.out.printf(" {[%d]}", j);
            } else if (eventMap.containsKey(dt) && j != currentDate.getDayOfMonth()) {
                System.out.printf(" {%d}", j);
            } else if (!eventMap.containsKey(dt) && j == currentDate.getDayOfMonth()) {
                System.out.printf(" [%d]", j);
            } else {
                System.out.printf("%3d", j);
            }

            int dow = (ld.getDayOfWeek().getValue() - 1 + j) % 7;
            if (dow == 6) {
                System.out.println();
            }
        }
        System.out.println("\n----------------------------------");
    }

    /**
     *  Parse event by splitting strings
     * @param s1
     * @param s2
     * @return
     */
    public String addEvent (String s1, String s2) {
        //System.out.println("-------------\nAdding event:");
        //System.out.println(s1);
        //System.out.println(s2);

        String s = "";
        int c = s1.length() - s1.replaceAll(" ", "").length();
        int c1 = s2.length() - s2.replaceAll(" ", "").length();

        // split event
        //String str = s1;
        //String[] arrayStr = str.split(" ", 2);
        String str2 = s2;
        String[] eventInfo = str2.split(" ",5);

        // Convert String to correct format for one time event
        // Parse start time of a one time event
        DateTimeFormatter formatterSt = DateTimeFormatter.ofPattern("H:m");
        LocalTime sTime = LocalTime.parse(eventInfo[1], formatterSt);
        //System.out.println("Hello #2");

        // Parse end time of a one time event
        DateTimeFormatter formatterEt = DateTimeFormatter.ofPattern("H:m");
        LocalTime eTime = LocalTime.parse(eventInfo[2], formatterEt);
        //System.out.println("Hello 3");

        // Parse date of a one time event
        DateTimeFormatter formatterDt = DateTimeFormatter.ofPattern("M/d/yy");
        LocalDate oneDate = LocalDate.parse(eventInfo[0], formatterDt);
        //System.out.println("\n"+ oneDate + "Hello 4");

        // Create a one time event object
        Event parseEvent = new Event(s1, oneDate, sTime, eTime);
        //System.out.println("\n^^^^^^This is a one time event^^^^^^ \n" + parseEvent);

        if (!eventMap.containsKey(oneDate)) {
            eventMap.put(oneDate, new ArrayList<Event>());
            //System.out.println("New ArrayList");
        }
        //System.out.println(oneDate);
        //System.out.println(parseEvent);
        eventMap.get(oneDate).add(parseEvent);
        for(Event e: eventMap.get(oneDate))
        {
            //System.out.println("w " + e);
        }
        Collections.sort(eventMap.get(oneDate));

        for (ChangeListener l : listeners) {
            l.stateChanged(new ChangeEvent(this));
        }
        //System.out.println("Finish adding");
        return s;
    }

    /**
     * Add a event into the list and check overlaps
     * @param d LocalDate date
     * @param s String name of an event
     * @param start LocalTime start time
     * @param end LocalTime end time
     */
    public boolean createEventToDate(LocalDate d, String s, LocalTime start, LocalTime end) {
        boolean added = false;
        if (!eventMap.containsKey(d)) {
            ArrayList<Event> el = new ArrayList<Event>();
            Event newEvent = new Event(s,d,start,end);
            el.add(newEvent);
            eventMap.put(d, el);
            System.out.println("New Event added.");
            added = true;
        } else {
            ArrayList<Event> exist = eventMap.get(d);
            boolean isFound = false;
            Event newEvent = new Event(s,d,start,end);
            for (Event e : exist) {
                // if new event start time is before previous start time and previous time's end time is before
                // new event end time
                //case 0
                if(newEvent.getStartTime().isBefore(e.getStartTime()) &&
                        e.getEndTime().isBefore(newEvent.getEndTime()))
                {
                    System.out.println("The event you entered has been scheduled.");
                    isFound = true;
                    added = false;
                    break;
                }
                // if new event's start time is before old event's end time and new event start's time
                // is after old event's start time
                // case 1
                else if(newEvent.getStartTime().isBefore(e.getEndTime()) &&
                        newEvent.getStartTime().isAfter(e.getStartTime()))
                {
                    System.out.println("The event you entered has been scheduled.");
                    isFound = true;
                    added = false;
                    break;
                }
                // if new event's start time is after old event's start time and new event's end time
                // is before old event's end time
                // case 2
                else if(newEvent.getStartTime().isAfter(e.getStartTime()) &&
                        newEvent.getEndTime().isBefore(e.getEndTime()))
                {
                    System.out.println("The event you entered has been scheduled.");
                    isFound = true;
                    added = false;
                    break;
                }
                // case 3
                else if(newEvent.getEndTime().isAfter(e.getStartTime()) &&
                        newEvent.getEndTime().isBefore(e.getEndTime()))
                {
                    System.out.println("The event you entered has been scheduled.");
                    isFound = true;
                    added = false;
                    break;
                }
                // case 4
                else if (newEvent.getStartTime().equals(e.getStartTime()) &&
                        newEvent.getEndTime().equals(e.getEndTime())) {
                    System.out.println("The event you entered has been scheduled.");
                    isFound = true;
                    added = false;
                    break;
                }
            }
            if (isFound == false) {
                //Event newEvent = new Event(s,d,start,end);
                exist.add(newEvent);
                System.out.println("New Event added.");
                added = true;
            }
        }

        for (ChangeListener l : listeners) {
            l.stateChanged(new ChangeEvent(this));
        }
        return added;
    }

    /**
     *  attach changeListeners
     * @param c
     */
    public void attach(ChangeListener c) {
        listeners.add(c);
    }

    /**
     * set current date
     * @param date date
     */
    public void setCurrentDate (LocalDate date) {
        this.currentDate = date;
        for (ChangeListener l : listeners) {
            l.stateChanged(new ChangeEvent(this));
        }
    }

    /**
     * get current date
     * @return current date
     */
    public LocalDate getCurrentDate () {
        return currentDate;
    }

    /**
     * get event string
     * @param date date
     * @return string
     */
    public String getEventString(LocalDate date) {

        System.out.println(date);
       // System.out.println(eventMap);

        String events = "<html>";
        if (!eventMap.containsKey(date)) {
            events += " No event today";
        } else {
            ArrayList<Event> eventArray = eventMap.get(date);
            if (eventArray != null) {
                for (Event e : eventArray) {
                    System.out.println(e);
                    events += e + "<br>";
                }
            }
            events += "</html>";
        }
        return events;
    }

    /**
     * get a date before current date
     * @param ld current date
     * @return  date of (current date - 1)
     */
    public LocalDate getPreviousDate(LocalDate ld) {
        return ld.minusDays(1);
    }

    /**
     * get a date after current date
     * @param ld current date
     * @return date of (current date + 1)
     */
    public LocalDate getNextDate(LocalDate ld) {
        return ld.plusDays(1);
    }

    /**
     * Remove duplicated events when print the list out
     */
    public String printFinalEventList() {
        String result ="";
        Set<String> eventSet = new HashSet<String>();
        for (LocalDate ld : eventMap.keySet()) {
            ArrayList<Event> lt = eventMap.get(ld);
            for (Event e : lt) {
                String s = e.getScheduledString();
                if (!eventSet.contains(s)) {
                    //System.out.println(s);
                    result += s + "\n";
                    eventSet.add(s);
                }
            }
        }
        return result;
    }
}