package com.yousuf.shawon.NotificationDemo;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MyActivity extends Activity {
    private Button btn1;
    private Button btn2, btn3;
    private int mId = 1;
    private  int numMessages = 0;

    private String TAG = getClass().getSimpleName();

    NotificationCompat.Builder mBuilder;
    NotificationManager mNotificationManager;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        iniUI();
    }

    private void iniUI() {
        btn1 = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateNotification();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateNotification2();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationWithProgress();
            }
        });
    }

    private void generateNotification() {
        mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_action_mail)
                        .setContentTitle("My notification")
                        .setContentText("Hello World! \n Hello World! ")
                        .setAutoCancel(true);

// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, NotifyResultActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(NotifyResultActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );



        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        String[] events = new String[6];
// Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("Event tracker details:");

// Moves events into the expanded layout
        for (int i=0; i < events.length; i++) {
            events[i] = " " + i ;
            inboxStyle.addLine(events[i]);
        }


        // Moves the expanded layout object into the notification object.
  //      mBuilder.setStyle(inboxStyle);


        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setNumber(++numMessages);
         mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());
      //  mId++;
    }



    public void generateNotification2(){

        int localMId = 0;


        // Instantiate a Builder object.
         mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_action_mail)
                .setContentTitle("My notification")
                .setContentText("Hello World! \n Hello World! ")
                .setAutoCancel(true);
// Creates an Intent for the Activity
        Intent notifyIntent =
                new Intent(this, NotifyResultActivity.class);
// Sets the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
// Creates the PendingIntent
        PendingIntent pendingNotifyIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

// Puts the PendingIntent into the notification builder
        mBuilder.setContentIntent(pendingNotifyIntent);
// Notifications are issued by sending them to the
// NotificationManager system service.
         mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// Builds an anonymous Notification object from the builder, and
// passes it to the NotificationManager
        mNotificationManager.notify(localMId, mBuilder.build());

    }



    public  void notificationWithProgress(){

        final int localMId = 0;
        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
      final  NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Picture Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_action_mail);
// Start a lengthy operation in a background thread
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int incr;
                        // Do the "lengthy" operation 20 times
                        for (incr = 0; incr <= 100; incr+=5) {
                            // Sets the progress indicator to a max value, the
                            // current completion percentage, and "determinate"
                            // state
                            mBuilder.setProgress(100, incr, false);
                            // Displays the progress bar for the first time.
                            mNotificationManager.notify(0, mBuilder.build());
                            // Sleeps the thread, simulating an operation
                            // that takes time
                            try {
                                // Sleep for 5 seconds
                                Thread.sleep(5*1000);
                            } catch (InterruptedException e) {
                                Log.d(TAG, "sleep failure");
                            }
                        }
                        // When the loop is finished, updates the notification
                        mBuilder.setContentText("Download complete")
                                // Removes the progress bar
                                .setProgress(0,0,false);
                        mNotificationManager.notify(localMId, mBuilder.build());
                    }
                }
// Starts the thread by calling the run() method in its Runnable
        ).start();

    }


}
