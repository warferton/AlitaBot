package com.alexkirillov.alitabot.models.scheduling;

import com.alexkirillov.alitabot.models.appointment.AlitaAppointment;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;

public class Week {
    private Workday day_1;
    private Workday day_2;
    private Workday day_3;
    private Workday day_4;
    private Workday day_5;
    private Workday day_6;
    private Workday day_7;

    public Week(){
        this.day_1 = new Workday();
        this.day_2 = new Workday();
        this.day_3 = new Workday();
        this.day_4 = new Workday();
        this.day_5 = new Workday();
        this.day_6 = new Workday();
        this.day_7 = new Workday();
    }

    public Week(Workday day_1, Workday day_2,
                Workday day_3, Workday day_4,
                Workday day_5, Workday day_6,
                Workday day_7) {
        this.day_1 = day_1;
        this.day_2 = day_2;
        this.day_3 = day_3;
        this.day_4 = day_4;
        this.day_5 = day_5;
        this.day_6 = day_6;
        this.day_7 = day_7;
    }

    //day changing controls

    /**
     * Shifts the <i>Week</i> one day forward switching
     * every day to the next one that follows
     * (e.g., day1 = day2, day2 = day3, etc.).
     * The last day of the week will be assigned
     * to a <b>new<b/> <i>Workday<i/> object.
     *
     * @return <b>1</b> - if successfully shifted.<br/>
     *         <b>0</b> - if some exception occurred
     */
    public int moveWeekForwardOneDay(){
        try {
            this.day_1 = this.day_2;
            this.day_2 = this.day_3;
            this.day_3 = this.day_4;
            this.day_4 = this.day_5;
            this.day_5 = this.day_6;
            this.day_6 = this.day_7;
            this.day_7 = new Workday();
            return 1;
        }
        catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Returns a certain day of the <i>Week</i>.
     * Accepts <b>int</b> indexes from 1 to 7, any other number will cause
     * an <i>IndexOutOfBoundsException</i>.
     * @param workday_index
     * @return one of seven <i>Workday</i>'s in a week,
     *         according to the index entered.
     */
    public Workday getWorkday(int workday_index){
        try{
            if(workday_index > 0 && workday_index < 8) {
                switch (workday_index) {
                    case 1: return this.day_1;

                    case 2: return this.day_2;

                    case 3: return this.day_3;

                    case 4: return this.day_4;

                    case 5: return this.day_5;

                    case 6: return this.day_6;

                    case 7: return this.day_7;
                }
            }
            else {
                throw new IndexOutOfBoundsException
                        ("Incorrect index caught. Index should be in range from 1 to 7");
            }
        }
        catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return new Workday(LocalDate.of(-1, -1, -1),
                Duration.of(-1, ChronoUnit.HOURS), new LinkedList<AlitaAppointment>());
    }

    public void setWorkDay(int workday_index, Workday new_workday){
        try{
            if(workday_index > 0 && workday_index < 8) {
                switch (workday_index) {
                    case 1: this.day_1 = new_workday;
                            break;

                    case 2: this.day_2 = new_workday;
                            break;

                    case 3: this.day_3 = new_workday;
                            break;

                    case 4: this.day_4 = new_workday;
                            break;

                    case 5: this.day_5 = new_workday;
                            break;

                    case 6: this.day_6 = new_workday;
                            break;

                    case 7: this.day_7 = new_workday;
                            break;
                }
            }
            else {
                throw new IndexOutOfBoundsException
                                ("Incorrect index caught. Index should be in range from 1 to 7");
                    }
                }
                catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
    }
//    public DBObject toDBObject(int week_num){
//        return new BasicDBObject("Day1",day_1.toDBObject())
//                                .append("Day2", day_2.toDBObject())
//                                .append("Day3", day_3.toDBObject())
//                                .append("Day4", day_4.toDBObject())
//                                .append("Day5", day_5.toDBObject())
//                                .append("Day6", day_6.toDBObject())
//                                .append("Day7", day_7.toDBObject())
//                                .append("weekNo", week_num);
//    }

    @Override
    public String toString() {
        return "Week{" +
                "day_1=" + day_1 +
                ", day_2=" + day_2 +
                ", day_3=" + day_3 +
                ", day_4=" + day_4 +
                ", day_5=" + day_5 +
                ", day_6=" + day_6 +
                ", day_7=" + day_7 +
                '}';
    }
}