package kislyakov.a12_1.util;

import android.util.Log;

import java.text.SimpleDateFormat;
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

        final long MILLS_IN_DAY = 86400000;
        final long MILLS_IN_HOUR = 3600000;
        final long MILLS_IN_3HOURS = 10800000;

        Date now = Calendar.getInstance().getTime();


        Log.d("myTag", new Date((now.getTime() + MILLS_IN_3HOURS) % MILLS_IN_DAY).toString());
        for (Divorce divorce : divorcesList) {
            if (divorce.getEnd().getTime() > ((now.getTime() + MILLS_IN_3HOURS) % MILLS_IN_DAY)
                    && divorce.getStart().getTime() - (now.getTime() + MILLS_IN_3HOURS) < MILLS_IN_HOUR) {
                state -= 1; // желтый мост
            }

            if (divorce.getEnd().getTime() > ((now.getTime() + MILLS_IN_3HOURS) % MILLS_IN_DAY)
                    && (now.getTime() + MILLS_IN_3HOURS) > divorce.getStart().getTime()) {
                state -= 2; // красный мост
            }
        }
        return state;
    }
}
