package com.technumopus.moneylisa.databaseHelper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;

public class DailyRecTransExecuter {

    public Context context;

    public DailyRecTransExecuter(Context context) {

        this.context = context;

    }

    public void setSchedule() {
        // -------------------------------------------------------------------------------
        // setting alarm
        // -------------------------------------------------------------------------------

        Calendar calendar = Calendar.getInstance();
        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                17,
                32,
                0
        );
        setAlarm(calendar.getTimeInMillis());
    }

    // -------------------------------------------------------------------------------
    // set alarm function
    // -------------------------------------------------------------------------------

    private void setAlarm(long timeInMillis) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyRecTransBroadcastReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent);
        //Toast.makeText(this,"Images Move Start Time set", Toast.LENGTH_SHORT).show();
    }

}
