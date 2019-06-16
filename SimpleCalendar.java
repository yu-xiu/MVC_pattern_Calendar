/**
 * This class contains the main method to test the calendar
 * The events files would be loaded when run the program
 * @author: Yu Xiu
 */

import java.io.File;
import java.time.LocalDate;
import java.util.Scanner;

public class SimpleCalendar {
    /**
     * main function to test calendar
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        LocalDate currentDate = LocalDate.now();
        CalendarDataModel mc = new CalendarDataModel(currentDate);
        CalendarView monthDayView = new CalendarView(mc);

        // Read txt files
        Scanner input = new Scanner(new File("src/events.txt"));

        // Load files and print out the event stored in the hashMap
        while (true) {
            if (! input.hasNextLine()) {
                break;
            }
            String eventLine1 = input.nextLine();
            String eventLine2 = input.nextLine();

            mc.addEvent(eventLine1,eventLine2);

        }
        // attach view
        mc.attach(monthDayView);


    }
}
