package kislyakov.a12_1.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kislyakov.a12_1.data.models.Divorce;

public class DivorceUtil {

    public static final int STATE_OPEN = 1;
    public static final int STATE_NEAR = 0;
    public static final int STATE_CLOSE = -1;

    public static String divorceToString(List<Divorce> divorceList) {

        StringBuilder result = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm");

        for (Divorce divorce : divorceList) {
            result.append(formatter.format(divorce.getStart()))
                    .append(" - ")
                    .append(formatter.format(divorce.getEnd()))
                    .append("   ");
        }

        return result.toString();
    }

    public static int divorceState(List<Divorce> divorcesList) {

        int state = 1;
        final long MILLS_IN_HOUR = 3600000;

        Calendar cal = Calendar.getInstance();
        cal.set(1970,0,1);
        Date now = cal.getTime();

        for (Divorce divorce : divorcesList) {
            Log.d("myTag", "NOW + " + now.toString());
            Log.d("myTag", "START + " +divorce.getStart().toString());
            Log.d("myTag", "END + " +divorce.getEnd().toString());

            if (divorce.getEnd().after(now)
                    && divorce.getStart().getTime() - now.getTime() < MILLS_IN_HOUR) {
                state -= 1; // желтый мост
            }

            if (divorce.getEnd().after(now)
                    && now.after(divorce.getStart())) {
                state -= 2; // красный мост
            }
        }
        return state;
    }
}
