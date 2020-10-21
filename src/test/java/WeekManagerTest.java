import com.alexkirillov.alitabot.models.appointment.AlitaAppointment;
import com.alexkirillov.alitabot.models.scheduling.Workday;
import com.alexkirillov.alitabot.services.scheduling.WeekManager;
import com.mongodb.*;


import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("deprecation")
public class WeekManagerTest {
    public static void main(String[] x) {
        MongoClient mongoClient = new MongoClient();
        DB database = mongoClient.getDB("AlitaDB");
        DBCollection collection = database.getCollection("schedule");

        WeekManager manager = new WeekManager();

        Workday[] day_set = manager.retrieveDayData(14,0);

        AlitaAppointment ap_1 = new AlitaAppointment(400, "Haircut(man)",
                Duration.of(45, ChronoUnit.MINUTES), (short) 3, new ArrayList<>());

        day_set[7].addAppointment(ap_1);
        System.out.println("-----------");
        System.out.println(Arrays.toString(day_set));
        System.out.println("vvv-----------vvv");

        manager.shiftDays(day_set);

        System.out.println("----------");
        System.out.println(Arrays.toString(day_set));
        System.out.println("-----------");

        BasicDBObject query;
        DBObject[] db_day_set = new DBObject[14];//2 weeeks


        for(int i = 0; i < 14; i++){
            query = new BasicDBObject("dayNo", i+1);
            db_day_set[i] = day_set[i].toDBObject(i+1);
            //collection.insert(db_day_set[i]);//initial insert
            collection.findAndModify(query, db_day_set[i]); //further modifying
            }


    }
}