package com.alexkirillov.alitabot.services.scheduling;

import com.alexkirillov.alitabot.models.scheduling.Workday;
import com.mongodb.*;

import java.util.Objects;
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
     * TODO j-comment the code
     *
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
                BasicDBObject query_1 = new BasicDBObject("weekNo", 1);
                BasicDBObject query_2 = new BasicDBObject("weekNo", 2);
                try {
                    Workday[] week_1 = retrieveWeekData(1);
                    Workday[] week_2 = retrieveWeekData(2);

                    shiftDays(week_1, week_2);

                    DBObject[] db_week_1 = {};
                    DBObject[] db_week_2 = {};

                    for(int i = 0; i < 6; i++){
                        db_week_1[i] = week_1[i].toDBObject();
                        db_week_2[i] = week_2[i].toDBObject();
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
     * TODO j-comment the code
     * @param num - week number (1, 2, 3, etc.)
     * @return Workday[]
     */
    public Workday[] retrieveWeekData(int num){
        DBCursor cursor = collection.find(new BasicDBObject("weekNo", num));
        Workday[] dummy_week = {new Workday(), new Workday(), new Workday(), new Workday()
                                , new Workday(), new Workday(), new Workday()};
        try {
            Workday[] week = {
                      (Workday) cursor.one().get("Day1")
                    , (Workday) cursor.one().get("Day2")
                    , (Workday) cursor.one().get("Day3")
                    , (Workday) cursor.one().get("Day4")
                    , (Workday) cursor.one().get("Day5")
                    , (Workday) cursor.one().get("Day6")
                    , (Workday) cursor.one().get("Day7")};
            if(week[0].equals(null)){
                throw new NullPointerException();
            }
            return Objects.requireNonNull(week);
        }
        catch (NullPointerException e){
            return dummy_week;
        }


    }

    /**
     * TODO j-comment the code
     * @param week_1
     * @param week_2
     */
    public void shiftDays(Workday[] week_1, Workday[] week_2){
        //shift days
        System.arraycopy(week_1, 1, week_1, 0, 6);
        week_1[6] = week_2[0];
        System.arraycopy(week_2, 1, week_2, 0, 6);
        week_2[6] = new Workday();
    }

}
