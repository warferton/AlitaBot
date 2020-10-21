package com.alexkirillov.alitabot.models.scheduling;

import com.alexkirillov.alitabot.models.appointment.AlitaAppointment;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;

public class Workday {
    private LocalDate date;
    private Duration workday_time;
    private LinkedList<AlitaAppointment> appointment_list;

    public Workday(){
        date = LocalDate.now();
        workday_time = Duration.of(12, ChronoUnit.HOURS);
        appointment_list = new LinkedList<AlitaAppointment>();
    }

    public Workday(LocalDate date){
        this.date = date;
        workday_time = Duration.of(12, ChronoUnit.HOURS);
        appointment_list = new LinkedList<AlitaAppointment>();
    }

    public Workday(LocalDate date, Duration workday_time, LinkedList<AlitaAppointment> appointment_list) {
        this.date = date;
        this.workday_time = workday_time;
        this.appointment_list = appointment_list;
    }

    //workday method controls

    /**
     * Adds an <i>AlitaAppointment</i> to a current <i>Workday<i/>.
     * First check if the appointment fits the current existing schedule.
     * If yes - returns 1 and adds the appointment to the
     * appointment list.
     * If not - returns 0.
     * @param appointment - <i>AlitaAppointment</i> that is to be added.
     * @return <b>1<b/> - if appointment is successfully added to the schedule.<br/>
     *         <b>0</b> - if appointment does not fit the schedule or exception occurred.
     *
     */
    public int addAppointment(AlitaAppointment appointment){
        //check if an appointment fits in the workday schedule

        try {
            if(fitsSchedule(appointment)){
                workday_time = workday_time.minusNanos(appointment.getEstimatedTimeInLong());
                appointment_list.add(appointment);
                return 1;
            } else {
                return 0;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Deletes an existing appointment for current <i>Workday<i/>.
     * First check if the appointment exists in the current existing schedule.
     * If yes - returns 1 and removes the appointment from the
     * appointment list.
     * If not - returns 0.
     * @param appointment - <i>AlitaAppointment</i> that is to be added.
     * @return <b>1<b/> - if appointment is successfully deleted from the schedule.<br/>
     *         <b>0</b> - if appointment is not found or exception occurred.
     */
    public int deleteAppointment(AlitaAppointment appointment) {
        try{
            if (appointment_list.contains(appointment)) {
                appointment_list.remove(appointment);
                    workday_time = workday_time.plusNanos(appointment.getEstimatedTimeInLong());
                return 1;
            } else {
                return 0;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Checks if an incoming appointment fits
     * the current days schedule.
     * @param appointment
     * @return true - if appointment fits the schedule<br/>
     *         false - if appointment does not fit the schedule
     */
    private boolean fitsSchedule(AlitaAppointment appointment) {
        if(appointment.equals(null)){
            return false;
        }
        else if(workday_time.minusNanos(appointment.getEstimatedTimeInLong()).isNegative()){
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Workday{" +
                "date=" + date +
                ", workday_time=" + workday_time.toMinutes() +
                ", appointment_list=" + appointment_list +
                '}';
    }

    public DBObject toDBObject(int day_num){
        return new BasicDBObject("date", date.toString()).append("worktime", workday_time.toString())
                                .append("appointments",appointment_list.toString()).append("dayNo", day_num);
    }
}
