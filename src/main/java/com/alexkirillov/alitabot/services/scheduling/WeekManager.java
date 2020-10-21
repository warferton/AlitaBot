package com.alexkirillov.alitabot.services.scheduling;

import com.alexkirillov.alitabot.models.appointment.AlitaAppointment;
import com.alexkirillov.alitabot.models.scheduling.Workday;
import com.mongodb.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
@SuppressWarnings("deprecation")
public class WeekManager {

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final MongoClient mongoClient = new MongoClient();
    private final DB database = mongoClient.getDB("AlitaDB");
    public DBCollection collection = database.getCollection("schedule");

    /**
     * A self-running function with a time period
     * of 24 hours.  Tries to retrieve initial data
     * from the connected database with a use of <b>retrieveDayData()</b> function.
     * Refreshes the <i>day set</i>, moving
     * it one day forward and erasing the very first day.
     * Then puts the <i>day set</i> back into the database.
     */
    @SuppressWarnings("rawtypes")
    public void weekUpdate() {
        final Runnable updater = new Runnable() {
            @Override
            public void run() {
                /* updater code
                 shift every day of 2 weeks
                 and add a new Workday to the end of the
                 second week.
                 */
                BasicDBObject query = new BasicDBObject("dayNo", 1);
                try {
                    Workday[] week_1 = retrieveDayData(7, 0);
                    Workday[] week_2 = retrieveDayData(7,7);

                    shiftDays(week_1, week_2);

                    DBObject query_1 = new BasicDBObject("dayNo", 1);
                    DBObject query_2 = new BasicDBObject("dayNo", 8);

                    DBObject[] db_week_1 = {};
                    DBObject[] db_week_2 = {};

                    for(int i = 0; i < 6; i++){
                        db_week_1[i] = week_1[i].toDBObject(i);
                        collection.findAndModify(query_1, db_week_1[i]);
                        collection.findAndModify(query_2, db_week_2[i]);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        ScheduledFuture updaterHandlerAtFixedRate =
                scheduler.scheduleAtFixedRate(updater, 0, 24, TimeUnit.HOURS);
    }

    /**
     * Accesses the connected DB's <i>schedule</i> collection
     * and tries to retrieve the <i>day set</i> data.
     * If ant of <i>Workday</i>'s equals to <b>null</b>
     * then throws <i>NullPointerException</i> and returns
     * an array of empty <i>Workday</i>s
     * @param num_of_days - number of days to retrieve
     * @param offset - offset, indicating the starting point of day retrieval
     * @return Workday[] - a <i>day set</i> array of two weeks.
     */
    public Workday[] retrieveDayData(int num_of_days, int offset){
        DBCursor cursor;
        Workday[] day_set = new Workday[num_of_days];
        try {
            for(int i = 0; i < (num_of_days - 1); i++) {
                cursor = collection.find(new BasicDBObject("dayNo", i + 1 + offset));
                day_set[i] = new Workday((LocalDate) cursor.one().get("date"),
                                        (Duration) cursor.one().get("worktime"),
                                        (LinkedList<AlitaAppointment>) cursor.one().get("appointments"));
                if(day_set[i].equals(null)){
                    throw new NullPointerException();
            }

            }
            return Objects.requireNonNull(day_set);
        }
        catch (NullPointerException e){
            for(int i = 0; i < num_of_days; i++) {
                day_set[i] = new Workday(LocalDate.now().plusDays(i-1));
            }
            return day_set;
        }


    }

    /**
     * Shifts days in two weeks 1 day forward
     * The very first day of the <i>week_2</i> becomes
     * the very last day of <i>week_1</i>.  The last
     * day of <i>week_2</i> is assigned an empty <i>Workday</i>
     * @param week_1 - first week
     * @param week_2 - second week
     */
    public void shiftDays(Workday[] week_1, Workday[] week_2){
        //shift days
        System.arraycopy(week_1, 1, week_1, 0, 6);
        week_1[6] = week_2[0];
        System.arraycopy(week_2, 1, week_2, 0, 6);
        week_2[6] = new Workday(LocalDate.now().plusDays(13));
    }

    /**
     *  Shifts days in two weeks 1 day forward.
     *  The 14th day is assigned an empty <i>Workday</i>
     * @param day_set - a <i>Workday</i> array representing 2 weeks(14 days)
     */
    public void shiftDays(Workday[] day_set){
        //shift days
        System.arraycopy(day_set, 1, day_set, 0, 13);
        day_set[13] = new Workday(LocalDate.now().plusDays(13));
    }

}
