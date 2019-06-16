/**
 * Calendar view and controller class
 * This class contains the constructor of the view
 * It displays a month view calendar and day view panel to display events in that day
 * It contains the actionListener button control
 * It state the changes when button clicked and mutated data model
 * It contains a go back button to see previous day and event, and a go next button to see next day and event
 * It contains a create button to create a new event
 * It contains a quit button to save all the scheduled events
 */

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarView extends JPanel implements ChangeListener{
    private LocalDate currentDate = LocalDate.now();
    private LocalDate selectedDate;
    private CalendarDataModel modelData;// = new CalendarDataModel(currentDate);
    private ArrayList<JButton> buttons = new ArrayList<JButton>();
    private Color pink = Color.PINK;
    private Color white = Color.WHITE;
    private JLabel dayLabel;
    private JPanel daysButtonPanel;
    private JLabel monthYearLabel;
    private JFrame frame;

    /**
     * Construct a calendar view
     * @param m
     */
    public CalendarView(CalendarDataModel m) {
        redraw(m);
    }

    /**
     * draw calendar
     * @param m
     */
    public void redraw(CalendarDataModel m) {
        modelData = m;
        //reference : https://stackoverflow.com/questions/1065691/how-to-set-the-background-color-of-a-jbutton-on-the-mac-os
        try{
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }catch(Exception e){
            e.printStackTrace();
        }
        // 2 panels on the same frame
        // day view panel
        JPanel dayPanel = new JPanel();
        dayPanel.setLayout(new GridLayout());
        dayPanel.setPreferredSize(new Dimension(600,200));
        dayPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        /*JLabel*/dayLabel = new JLabel();
        dayPanel.add(dayLabel);

        // a button to create events
        JButton create = new JButton("CREATE");
        create.setBackground(pink);
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame =  new JFrame();
                frame.setLayout(new FlowLayout());
                JTextField info = new JTextField(15);
                info.setText("Please enter information: ");
                JTextField addTitle = new JTextField(10);
                addTitle.setText("Enter the title");
                JTextField startTime = new JTextField(10);
                startTime.setText("Start: HH:MM");
                JTextField endTime = new JTextField(10);
                endTime.setText("End: HH:MM");
                JButton save = new JButton("save");
                save.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String eventName = addTitle.getText();
                        DateTimeFormatter formatterStime = DateTimeFormatter.ofPattern("H:m");
                        LocalTime stime = LocalTime.parse(startTime.getText(), formatterStime);
                        LocalTime etime = LocalTime.parse(endTime.getText(), formatterStime);
                        if(modelData.createEventToDate(modelData.getCurrentDate(),eventName, stime, etime)) {
                            frame.dispose();
                        }
                        else {
                            info.setText("Error making event, re-enter");
                            info.setForeground(Color.RED);
                        }
                    }
                });

                // popped up frame
                frame.add(info);
                frame.add(addTitle);
                frame.add(startTime);
                frame.add(endTime);
                frame.add(save);
                frame.pack();
                frame.setVisible(true);
            }
        });

        LocalDate ld = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1);

        // create blank buttons before the first date of the week
        for (int i = 0; i < ld.getDayOfWeek().getValue(); i++) {
            JButton blankButton = new JButton();
            blankButton.setBackground(white);
            blankButton.setBorder(new LineBorder(pink));
            buttons.add(blankButton);
        }

        // create buttons with the dates of the month on it
        for (int i = 1; i <= currentDate.lengthOfMonth(); i++) {
            JButton dateButton = new JButton(""+ i);

            dateButton.setBackground(white);
            dateButton.setBorder(new LineBorder(pink));
            buttons.add(dateButton);
            int day = i;
            // highlight current date at the beginning
            if(day == modelData.getCurrentDate().getDayOfMonth())
            {
                dateButton.setBackground(pink);
                dateButton.setOpaque(true);
                LocalDate temp = LocalDate.of(currentDate.getYear(), currentDate.getMonth().getValue(), day);
                selectedDate = temp;
                modelData.setCurrentDate(temp);
            }
            // when click on the date button, the event would be displayed in the dayViewPanel
            dateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dateButton.setBackground(Color.GRAY);
                    dateButton.setOpaque(true);

                    for (JButton b : buttons ) {
                        if (dateButton != b) {
                            b.setBackground(white);
                            b.setOpaque(true);
                        }
                    }
                    //System.out.println("hello *0000*****");
                    LocalDate temp = LocalDate.of(currentDate.getYear(), currentDate.getMonth().getValue(), day);
                    selectedDate = temp;
                    modelData.setCurrentDate(temp);
                    //Integer.parseInt(dateButton.getText()));
                    //System.out.println("hello ******" + temp);//modelData.printEventOfDay(temp));
                    //System.out.println(modelData.getEventString(temp));
                    dayLabel.setText(modelData.getEventString(temp));

                }
            });

        }
        for(int i = buttons.size(); i < 42; i++)
        {
            JButton tempButton = new JButton();
            tempButton.setBackground(white);
            tempButton.setBorder(new LineBorder(pink));
            buttons.add(tempButton);
        }

        // monthPanel to hold header panel and dates buttons
        JPanel monthPanel = new JPanel();
        monthPanel.setLayout(new BorderLayout());
        monthPanel.setPreferredSize(new Dimension(500, 500));
        monthPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        JPanel headerPanel = new JPanel();
        headerPanel.setFont(new Font(headerPanel.getName(), Font.PLAIN, Math.min(20, 20)));
        headerPanel.setPreferredSize(new Dimension(30,30));
        headerPanel.setLayout(new GridLayout(1,7));

        JLabel sun = new JLabel("Sun", SwingConstants.CENTER);
        JLabel sat = new JLabel("Sat", SwingConstants.CENTER);
        JLabel fri = new JLabel("Fri", SwingConstants.CENTER);
        JLabel thu = new JLabel("Thu", SwingConstants.CENTER);
        JLabel wed = new JLabel("Wed", SwingConstants.CENTER);
        JLabel tue = new JLabel("Tue", SwingConstants.CENTER);
        JLabel mon = new JLabel("Mon", SwingConstants.CENTER);

        headerPanel.add(sun);
        headerPanel.add(mon);
        headerPanel.add(tue);
        headerPanel.add(wed);
        headerPanel.add(thu);
        headerPanel.add(fri);
        headerPanel.add(sat);


        // a daysButtonPanel to holds all the dates button
        daysButtonPanel = new JPanel();
        daysButtonPanel.setLayout(new GridLayout(6,7));

        // add all the dates buttons to daysButtonPanel
        for (int i = 0; i < buttons.size(); i++) {
            daysButtonPanel.add(buttons.get(i));
        }

        // add buttons with dates and header to the center and create button to the top
        monthPanel.add(daysButtonPanel, BorderLayout.CENTER);
        monthPanel.add(create, BorderLayout.SOUTH);
        monthPanel.add(headerPanel, BorderLayout.NORTH);

        // a view panel to hold both dayPanel and monthPanel
        JPanel viewPanel = new JPanel();
        //viewPanel.setPreferredSize(new Dimension(500,600));
        viewPanel.setLayout(new FlowLayout());
        viewPanel.add(monthPanel);
        viewPanel.add(dayPanel);

        // a buttonPanel to hold goBack and goNext buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setPreferredSize(new Dimension(100,50));

        //TODO: add actionListener to buttons
        // go back button
        JButton goBack = new JButton("< go back");

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //LocalDate previousOfselectedDate = modelData.getPreviousDate(selectedDate);
                modelData.setCurrentDate(modelData.getPreviousDate(selectedDate));
                selectedDate = modelData.getCurrentDate();
                //System.out.println("see prevoius date and event" + modelData.getEventString(modelData.getCurrentDate()));
                //dayLabel.setText(modelData.getEventString(modelData.getCurrentDate()));
            }
        });
        // go forward button
        JButton goNext = new JButton("go next >");
        goNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelData.setCurrentDate(modelData.getNextDate(selectedDate));
                selectedDate = modelData.getCurrentDate();
                //System.out.println("see next date and event" + modelData.getEventString(modelData.getCurrentDate()));
            }
        });
        // a quit button terminates the Calendar program and saves the events in a file called events.txt
        JButton quit = new JButton("Quit");
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
                    String s = modelData.printFinalEventList();
                    writer.write(s);
                    writer.close();
                }
                catch (Exception e1) {
                    e1.printStackTrace();
                }
                frame.dispose();

            }
        });
        monthYearLabel = new JLabel("   " + currentDate.getMonth() + " " + currentDate.getYear(),
                SwingConstants.CENTER);
        // display month
        monthYearLabel.setFont(new Font(monthYearLabel.getName(), Font.PLAIN, Math.min(30, 30)));


        buttonPanel.add(monthYearLabel);
        buttonPanel.add(goBack);
        buttonPanel.add(goNext);
        buttonPanel.add(quit);


        // a base panel to hold viewPanel in center and goBack and goForward buttons on North
        JPanel basePanel = new JPanel();
        //basePanel.setPreferredSize(new Dimension(600,800));
        basePanel.setLayout(new BorderLayout());
        basePanel.add(buttonPanel, BorderLayout.NORTH);
        basePanel.add(viewPanel, BorderLayout.CENTER);


        // frame to hold panels
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(1200,600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(basePanel);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * state changed when click date buttons
     * @param e
     */
    public void stateChanged(ChangeEvent e) {

        dayLabel.setText(modelData.getEventString(modelData.getCurrentDate()));
        monthYearLabel.setText(modelData.getCurrentDate().getMonth() + " " + modelData.getCurrentDate().getYear());
        int currentDay = modelData.getCurrentDate().getDayOfMonth();

        for(int i = 0; i < buttons.size(); i++)
        {
            if(buttons.get(i).getActionListeners().length > 0) {
                buttons.get(i).removeActionListener(buttons.get(i).getActionListeners()[0]);
            }
        }

        LocalDate currDate = modelData.getCurrentDate();
        LocalDate firstDayOfMonth = LocalDate.of(currDate.getYear(), currDate.getMonth(), 1);
        int firstDayVal = firstDayOfMonth.getDayOfWeek().getValue();

        for(int i = 0; i < firstDayVal; i++){
            buttons.get(i).setText("");
            if(buttons.get(i).getActionListeners().length > 0)
            {
                buttons.get(i).removeActionListener(buttons.get(i).getActionListeners()[0]);
            }
        }

        int position = firstDayVal; //position in buttons ArrayList
        for(int j = 1; j <= modelData.getCurrentDate().lengthOfMonth(); j++){
            buttons.get(position).setText(j + "");

           if(buttons.get(position).getActionListeners().length > 0)
            {
                buttons.get(position).removeActionListener(buttons.get(position).getActionListeners()[0]);
            }
            JButton dateButton = buttons.get(position);
            int day = j; //!!! was i
            dateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // set background of button to gray when clicked
                    dateButton.setBackground(Color.GRAY);
                    dateButton.setOpaque(true);

                    for (JButton b : buttons ) {
                        if (dateButton != b) {
                            b.setBackground(white);
                            b.setOpaque(true);
                        }
                    }
                    //System.out.println("hello *0000*****");
                    LocalDate currDate = modelData.getCurrentDate();
                    LocalDate temp = LocalDate.of(currDate.getYear(), currDate.getMonth().getValue(), day);
                    selectedDate = temp;
                    modelData.setCurrentDate(temp);
                    //Integer.parseInt(dateButton.getText()));
                    //System.out.println("hello ******" + temp);//modelData.printEventOfDay(temp));
                    //System.out.println(modelData.getEventString(temp));
                    dayLabel.setText(modelData.getEventString(temp));
                }
            });
            position++;
        }

        int offset = firstDayOfMonth.getDayOfWeek().getValue();

        for(int j = modelData.getCurrentDate().lengthOfMonth() + offset; j < 42; j++)
        {
            buttons.get(j).setText("");
            // remove previous action listeners
            if(buttons.get(j).getActionListeners().length > 0)
            {
                buttons.get(j).removeActionListener(buttons.get(j).getActionListeners()[0]);
            }
        }
        // color current day in gray and set all other buttons in white
        for(JButton b: buttons)
        {
            if(b.getText().equals(currentDay + ""))
            {
                b.setBackground(Color.GRAY);
                b.setOpaque(true);
            }
            else{
                b.setBackground(white);
                b.setOpaque(true);
            }
        }
    }
}
