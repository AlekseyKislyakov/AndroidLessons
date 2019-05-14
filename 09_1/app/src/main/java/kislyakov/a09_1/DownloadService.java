package kislyakov.a09_1;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import static kislyakov.a09_1.App.CHANNEL_ID;


public class DownloadService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("myTag", "Download onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return
        Log.d("myTag", "Download onStart");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Download service")
                .setContentText("Downloading and unzipping file")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        downloadFile();

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        //do something you want
        //stop service
        stopSelf();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void downloadFile() {
        new Thread(new Runnable() {
            public void run() {
                String fileName;
                String folder;
                int count;
                try {
                    URL url = new URL("https://getfile.dokpub.com/yandex/get/https://yadi.sk/d/cqDsUR2UUU-KOQ");
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    // getting file length
                    int lengthOfFile = connection.getContentLength();


                    // input stream to read file - with 8k buffer
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);

                    String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                    //Extract file name from URL
                    //fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                    //Append timestamp to file name
                    //fileName = timestamp + "_" + fileName;
                    fileName = "LOL";

                    //External directory path to save file
                    folder = Environment.getExternalStorageDirectory() + File.separator + "androiddeft" + File.separator;

                    //Create androiddeft folder if it does not exist
                    File directory = new File(folder);
                    File zipFile = new File(folder + fileName);
                    Log.d("myTag", directory.getAbsolutePath());

                    if (!directory.exists()) {
                        Log.d("myTag", Boolean.toString(directory.mkdirs()));
                    }

                    if (!zipFile.exists()) {
                        Log.d("myTag", Boolean.toString(zipFile.createNewFile()));
                    }

                    // Output stream to write file
                    OutputStream output = new FileOutputStream(folder + fileName);

                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // publishing the progress....
                        // After this onProgressUpdate will be called
                        //publishProgress("" + (int) ((total * 100) / lengthOfFile));
                        Log.d("myTag", "Progress: " + (int) ((total * 100) / lengthOfFile));
                        Intent intent = new Intent("LOL");
                        intent.putExtra("test", (int) ((total * 100) / lengthOfFile));
                        sendBroadcast(intent);
                        // writing data to file
                        output.write(data, 0, count);
                    }

                    // flushing output
                    output.flush();

                    // closing streams
                    output.close();
                    input.close();

                    String zippedFile = folder + fileName;
                    String unzipLocation = folder + "/unzipped/";

                    Intent finishMessage = new Intent("LOL");
                    finishMessage.putExtra("zip", "Unzipping");
                    sendBroadcast(finishMessage);

                    Decompress d = new Decompress(zippedFile, unzipLocation);
                    String res = d.unzip();

                    finishMessage = new Intent("LOL");
                    finishMessage.putExtra("finish", res);
                    sendBroadcast(finishMessage);
                    Log.d("myTag", "STOPPED");

                    stopSelf();
                    stopForeground(true);


                    //return "Downloaded at: " + folder + fileName;

                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }

                //return "Something went wrong";


                //рассказать почему так
                //LocalBroadcastManager.getInstance(StartService.this).sendBroadcast(intent);
                stopForeground(true);
                stopSelf();
            }
        }).start();

    }
}