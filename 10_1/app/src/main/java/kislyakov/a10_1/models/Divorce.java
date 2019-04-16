
package kislyakov.a10_1.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class Divorce implements Serializable {

    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("start")
    @Expose
    private String start;

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public static String DivorceConverter (List<Divorce> divorces){
        String divorcesStr = "";

        SimpleDateFormat incomingTime = new SimpleDateFormat("HH:mm:ss");

        Date start = new Date();
        Date stop = new Date();

        for (int i = 0; i < divorces.size(); i++)
        {
            try {
                start = incomingTime.parse(divorces.get(i).getStart());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                stop = incomingTime.parse(divorces.get(i).getEnd());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat outputDateFormat = new SimpleDateFormat("H:mm");

            divorcesStr += outputDateFormat.format(start);
            divorcesStr += " - ";
            divorcesStr += outputDateFormat.format(stop);
            divorcesStr += "   ";
        }
        return divorcesStr;
    }

    public static int DivorceState(List<Divorce> divorces){
        int state = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date start = new Date();
        Date stop = new Date();
        Date now = Calendar.getInstance().getTime();

        for (int i = 0; i < divorces.size(); i++)
        {
            try {
                start = sdf.parse(divorces.get(i).getStart());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                stop = sdf.parse(divorces.get(i).getEnd());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(stop.getTime() > ((now.getTime()+10800000)%86400000) && start.getTime() - ((now.getTime()+10800000)%86400000) < 3600000)
            {
                state -= 1; // желтый мост
            }

            if(stop.getTime() > ((now.getTime()+10800000)%86400000) && ((now.getTime()+10800000)%86400000) > start.getTime())
            {
                state -= 2; // красный мост
            }
        }
        return state;
    }

    public Date TimestrToCalendar(String timeStr){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        Date result = new Date();
        try {
            result.setMinutes(sdf.parse(timeStr).getMinutes());
            result.setHours(sdf.parse(timeStr).getHours());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

}
