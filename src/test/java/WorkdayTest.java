import com.alexkirillov.alitabot.models.appointment.AlitaAppointment;
import com.alexkirillov.alitabot.models.scheduling.Workday;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;

public class WorkdayTest {

    public static void main(String[] args) {
        Workday wd_1 = new Workday();
        Workday wd_2 = new Workday(LocalDate.of(2007, 4, 21),
                Duration.of(8, ChronoUnit.HOURS),
                new LinkedList<AlitaAppointment>());

        AlitaAppointment ap_1 = new AlitaAppointment(400, "Haircut(man)",
                Duration.of(45, ChronoUnit.MINUTES), (short) 3);

        AlitaAppointment ap_2 = new AlitaAppointment(600, "Haircut(woman)",
                Duration.of(120, ChronoUnit.MINUTES), (short) 5);

        AlitaAppointment ap_3 = new AlitaAppointment(4000, "Nails",
                Duration.of(4005, ChronoUnit.MINUTES), (short) 1);

        System.out.println(wd_1+"\n");
        System.out.println(wd_2+"\n");
        wd_1.addAppointment(ap_1);
        wd_2.addAppointment(ap_2);
        System.out.println(wd_1+"\n");
        System.out.println(wd_2+"\n");
        wd_1.deleteAppointment(ap_1);
        System.out.println(wd_1+"\n");

        System.out.println(wd_1.deleteAppointment(ap_1));
        System.out.println(wd_1+"-------1\n");

        //exception expected
        System.out.println(wd_2.addAppointment(ap_3));
        System.out.println(wd_2+"---------2-");

    }
}

