package com.alog.rxdemo;


import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alog.netlibrary.RxManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
                boolean notificationEnable = isNotificationEnable(MainActivity.this);
                checkPermission();
                if (notificationEnable) {
                    show();
                } else {

                }

            }
        });
    }

    private void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = getInstance().getNotificationChannel(id);
            if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
                startActivity(intent);
                Toast.makeText(this, "请手动将通知打开1", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "请手动将通知打开2", Toast.LENGTH_SHORT).show();
            }
        }else{

        }
    }

    private boolean isNotificationEnable(Context context) {
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();

        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        Class appOpsClass = null;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField("OP_POST_NOTIFICATION");
            int value = (int) opPostNotificationValue.get(Integer.class);
            return ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    //https://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650242841&idx=1&sn=6fd0a578a8ff35902d409ae01fbabc9f&scene=19#wechat_redirect
    private NotificationManager getInstance() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
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
            mChannel.enableVibration(false);
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
