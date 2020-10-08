import com.alexkirillov.alitabot.models.appointment.AlitaAppointment;
import com.alexkirillov.alitabot.models.scheduling.Workday;
import com.alexkirillov.alitabot.services.scheduling.WeekManager;
import com.mongodb.*;


import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

@SuppressWarnings("deprecation")
public class WeekManagerTest {
    public static void main(String[] x) {
        MongoClient mongoClient = new MongoClient();
        DB database = mongoClient.getDB("AlitaDB");
        DBCollection collection = database.getCollection("schedule");

        WeekManager manager = new WeekManager();
        Workday test_day = new Workday();

        Workday[] week_1 = manager.retrieveWeekData(1);
        Workday[] week_2 = manager.retrieveWeekData(2);

        AlitaAppointment ap_1 = new AlitaAppointment(400, "Haircut(man)",
                Duration.of(45, ChronoUnit.MINUTES), (short) 3);

        test_day.addAppointment(ap_1);

        week_2[0] = test_day;

        System.out.println(Arrays.toString(week_1));
        System.out.println("-----------");
        System.out.println(Arrays.toString(week_2));
        System.out.println("vvv-----------vvv");

        manager.shiftDays(week_1, week_2);

        System.out.println(Arrays.toString(week_1));
        System.out.println("-----------");
        System.out.println(Arrays.toString(week_2));
        System.out.println("-----------");

        BasicDBObject query_1 = new BasicDBObject("weekNo", 1);
        BasicDBObject query_2 = new BasicDBObject("weekNo", 2);
        DBObject[] db_week_1 = new DBObject[7];
        DBObject[] db_week_2 = new DBObject[7];

        for(int i = 0; i < 6; i++){
            db_week_1[i] = week_1[i].toDBObject();
            db_week_2[i] = week_2[i].toDBObject();
            collection.findAndModify(query_1, db_week_1[i]);
            collection.findAndModify(query_2, db_week_2[i]);
        }


    }
}