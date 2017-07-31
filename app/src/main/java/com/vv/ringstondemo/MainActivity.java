package com.vv.ringstondemo;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    /* 3个按钮 */
    private Button mButtonRingtone;
    private Button mButtonAlarm;
    private Button mButtonNotification;

    /* 自定义的类型 */
    public static final int CODE_RINGSTONE = 0;
    public static final int CODE_ALARM = 1;
    public static final int CODE_NOTIFICATION = 2;
    /**
     *  来电铃声文件夹
     *  /system/media/audio/ringtones       系统来电铃声
     *  /sdcard/music/ringtones             用户来电铃声
     */
    private String strRingtoneFolder = "/system/media/audio/ringtones";
//  private String strRingtoneFolder = "/sdcard/music/ringtones";
    /**
     *  闹钟铃声文件夹
     *  /system/media/audio/alarms          系统闹钟铃声
     *  /sdcard/music/alarms                用户闹钟铃声
     */
    private String strAlarmFolder = "/system/media/audio/alarms";
//  private String strAlarmFolder = "/sdcard/music/alarms ";
    /**
     *  闹钟铃声文件夹
     *  /system/media/audio/notifications       系统通知铃声
     *  /sdcard/music/notifications             用户通知铃声
     */
    private String strNotificationFolder = "/system/media/audio/notifications";
//  private String strNotificationFolder = "/sdcard/music/notifications";


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButtonRingtone = (Button) findViewById(R.id.buttonRingtone);
        mButtonAlarm = (Button) findViewById(R.id.buttonAlarm);
        mButtonNotification = (Button) findViewById(R.id.buttonNotification);
        mButtonRingtone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasFolder(strRingtoneFolder)) {
                    // 打开系统铃声设置
                    Intent intent = new Intent(
                            RingtoneManager.ACTION_RINGTONE_PICKER);
                    // 类型为来电RINGTONE
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
                            RingtoneManager.TYPE_RINGTONE);
                    // 设置显示的title
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,
                            "设置来电铃声");
                    // 当设置完成之后返回到当前的Activity
                    startActivityForResult(intent, CODE_RINGSTONE);
                }
            }
        });
        mButtonAlarm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasFolder(strAlarmFolder)) {
                    // 打开系统铃声设置
                    Intent intent = new Intent(
                            RingtoneManager.ACTION_RINGTONE_PICKER);
                    // 设置铃声类型和title
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
                            RingtoneManager.TYPE_ALARM);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,
                            "设置闹钟铃声");
                    // 当设置完成之后返回到当前的Activity
                    startActivityForResult(intent, CODE_ALARM);
                }
            }
        });
        mButtonNotification.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasFolder(strNotificationFolder)) {
                    // 打开系统铃声设置
                    Intent intent = new Intent(
                            RingtoneManager.ACTION_RINGTONE_PICKER);
                    // 设置铃声类型和title
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
                            RingtoneManager.TYPE_NOTIFICATION);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,
                            " 设置通知铃声");
                    // 当设置完成之后返回到当前的Activity
                    startActivityForResult(intent, CODE_NOTIFICATION);
                }
            }
        });
    }
    /**
     * 当设置铃声之后的回调函数
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        // 得到我们选择的铃声
        Uri pickedUri = data
                .getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
        if (pickedUri != null) {
            switch (requestCode) {
                case CODE_RINGSTONE:
                    // 将我们选择的铃声设置成为默认来电铃声
                    RingtoneManager.setActualDefaultRingtoneUri(this,
                            RingtoneManager.TYPE_RINGTONE, pickedUri);
                    break;
                case CODE_ALARM:
                    // 将我们选择的铃声设置成为默认闹钟铃声
                    RingtoneManager.setActualDefaultRingtoneUri(this,
                            RingtoneManager.TYPE_ALARM, pickedUri);
                    break;
                case CODE_NOTIFICATION:
                    // 将我们选择的铃声设置成为默认通知铃声
                    /**
                     * 敲黑板:黑代码解决Android 7.0 调用系统通知无法播放声音的问题
                     */
                    grantUriPermission("com.android.systemui", pickedUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    RingtoneManager.setActualDefaultRingtoneUri(this,
                            RingtoneManager.TYPE_NOTIFICATION, pickedUri);
                    break;
            }
        }
    }

    /**
     * 检测是否存在指定的文件夹,如果不存在则创建
     *
     * @param strFolder
     *            文件夹路径
     */
    private boolean hasFolder(String strFolder) {
        boolean btmp = false;
        File f = new File(strFolder);
        if (!f.exists()) {
            if (f.mkdirs()) {
                btmp = true;
            } else {
                btmp = false;
            }
        } else {
            btmp = true;
        }
        return btmp;
    }
}