package com.alog.rxdemo;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alog.netlibrary.RxManager;

import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {

    public static final String id = "my_channel_01";
    private NotificationManager notificationManager;
    private int anInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RxManager.getInstance().getNet();
        createChannel();
        View viewById = findViewById(R.id.test);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test", "onclick");
                show();
            }
        });
    }



    private NotificationManager getInstance() {
        if (notificationManager == null)
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        return notificationManager;
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 用户可以看到的通知渠道的名字.
            CharSequence name = getString(R.string.channel);
            // 用户可以看到的通知渠道的描述
            String description = getString(R.string.channel_des);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            //注意Name和description不能为null或者""
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // 配置通知渠道的属性
            mChannel.setDescription(description);
            getInstance().createNotificationChannel(mChannel);
        }
    }

    private void show() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext(), id);
        builder.setContentText("通知标题");
        builder.setContentTitle("通知内容" + anInt);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setFullScreenIntent(pendingIntent, true);
        }
        Notification notification = builder.build();
        getInstance().notify(UUID.randomUUID().hashCode(), notification);
        anInt++;
    }
}
