package kislyakov.a10_1.classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        Log.d("myTag", "ALARM");
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(intent.getStringExtra("bridge_name"),
                intent.getStringExtra("divorce_time"));
        notificationHelper.getManager().notify(intent.getIntExtra("item_position",0), nb.build());
    }
}
