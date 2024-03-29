package com.example.nicolascarraggi.trgrd.rulesys.devices;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.nicolascarraggi.trgrd.MainActivity;
import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.logging.MyLogger;
import com.example.nicolascarraggi.trgrd.rulesys.Action;
import com.example.nicolascarraggi.trgrd.rulesys.Device;
import com.example.nicolascarraggi.trgrd.rulesys.DeviceManager;
import com.example.nicolascarraggi.trgrd.rulesys.Event;
import com.example.nicolascarraggi.trgrd.rulesys.NotificationAction;
import com.example.nicolascarraggi.trgrd.rulesys.RuleSystemService;
import com.example.nicolascarraggi.trgrd.rulesys.SendMessageAction;
import com.example.nicolascarraggi.trgrd.rulesys.SendMessageCallerAction;
import com.example.nicolascarraggi.trgrd.rulesys.State;

import java.util.concurrent.Callable;

/**
 * Created by nicolascarraggi on 5/04/17.
 */

public class AndroidPhone extends Device implements NotificationDevice, SendMessageDevice, SendMessageCallerDevice {

    // TO WAIT 1 SEC BEFORE EXECUTING A REJECT CALL

    private static class MyHandler extends Handler {}
    private final MyHandler mHandler = new MyHandler();

    public static class CallRejectRunnable implements Runnable {

        private AndroidPhone androidPhone;

        public CallRejectRunnable(AndroidPhone androidPhone) {
            this.androidPhone = androidPhone;
        }

        @Override
        public void run() {
            // SIMULATE by activating silent mode!
            androidPhone.acSoundModeSilent();
        }
    }

    private CallRejectRunnable mRunnable = new CallRejectRunnable(AndroidPhone.this);

    // Constants

    public static final String CALL_PHONE_STATE = "android.intent.action.PHONE_STATE";
    public static final String CALL_NEW_OUTGOING_CALL = "android.intent.action.NEW_OUTGOING_CALL";

    public static final String ALARM_SONY_ALERT_ACTION = "com.sonyericsson.alarm.ALARM_ALERT";
    public static final String ALARM_SONY_SNOOZE_ACTION = "com.sonyericsson.alarm.ALARM_SNOOZE";
    public static final String ALARM_SONY_DISMISS_ACTION = "com.sonyericsson.alarm.ALARM_DISMISS";
    public static final String ALARM_SONY_DONE_ACTION = "com.sonyericsson.alarm.ALARM_DONE";

    // com.samsung.sec.android.clockpackage.alarm.

    public static final String ALARM_SAMSUNG_ALERT_ACTION = "com.samsung.sec.android.clockpackage.alarm.ALARM_ALERT"; // WORKS
    public static final String ALARM_SAMSUNG_SNOOZE_ACTION = "com.samsung.sec.android.clockpackage.alarm.ALARM_SNOOZE"; // DOESN'T WORK ...
    public static final String ALARM_SAMSUNG_DISMISS_ACTION = "com.samsung.sec.android.clockpackage.alarm.ALARM_DISMISS"; // DOESN'T WORK ...
    public static final String ALARM_SAMSUNG_DONE_ACTION = "com.samsung.sec.android.clockpackage.alarm.ALARM_DONE"; // DOESN'T WORK ...
    public static final String ALARM_SAMSUNG_CLOCKPACKAGE = "com.sec.android.app.clockpackage.ClockPackage"; // DOESN'T WORK ...

    public static final String ALARM_ALERT_ACTION = "com.android.deskclock.ALARM_ALERT";
    public static final String ALARM_SNOOZE_ACTION = "com.android.deskclock.ALARM_SNOOZE";
    public static final String ALARM_DISMISS_ACTION = "com.android.deskclock.ALARM_DISMISS";
    public static final String ALARM_DONE_ACTION = "com.android.deskclock.ALARM_DONE";

    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED"; // WORKS on emulator & sony phone ( if permission in settings ) !!!

    private int state = 0;

    // Android Phone Broadcast Receiver

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("TRGRD","AndroidPhone mReceiver "+action);

            if(action.equals(ALARM_SONY_ALERT_ACTION)){
                evAlarmStart();
            } else if(action.equals(ALARM_SONY_SNOOZE_ACTION)){
                evAlarmSnooze();
            } else if(action.equals(ALARM_SONY_DISMISS_ACTION)){
                evAlarmDismiss();
            } else if(action.equals(ALARM_SONY_DONE_ACTION)){ // || action.equals(ALARM_SAMSUNG_DONE_ACTION) || action.equals(ALARM_DONE_ACTION)
                evAlarmDone();
            } else if(action.equals(CALL_PHONE_STATE)){
                String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
                String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                if(stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                    state = TelephonyManager.CALL_STATE_IDLE;
                    evCallIncStop();
                }
                else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                    state = TelephonyManager.CALL_STATE_OFFHOOK;
                    // event on call incoming answered?
                }
                else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                    state = TelephonyManager.CALL_STATE_RINGING;
                    callerPhoneNumber = number;
                    evCallIncStart();
                }
                MyLogger.debugLog("TRGRD","AndroidPhone INCOMING CALL from: "+number+", state = "+state);
            }
        }
    };

    // Events, states & actions

    private Event mEvAlarmStart, mEvAlarmSnooze, mEvAlarmDismiss, mEvAlarmDone, mEvCallIncStart,
                    mEvCallIncStop, mEvCallIncFavStart, mEvCallIncFavStop, mEvCallIncNoFavStart,
                    mEvCallIncNoFavStop;
    private State mStAlarmGoing, mStCallIncGoing, mStCallIncFavGoing, mStCallIncNoFavGoing;
    private Action mAcAlarmDismiss, mAcAlarmSnooze, mAcCallReject, mAcAudioNormal, mAcAudioSilent,
                    mAcAudioVibrate;
    private NotificationAction mAcNotify;
    private SendMessageAction mAcSendMessage;
    private SendMessageCallerAction mAcSendMessageCaller;

    private String callerPhoneNumber = "0486371224";

    public AndroidPhone(RuleSystemService ruleSystemService, DeviceManager deviceManager) {
        super(ruleSystemService.getNewId(), "Phone", "Google", "Android", R.drawable.ic_phone_android_black_24dp, ruleSystemService,deviceManager);
        // Types
        this.eventTypes.put(deviceManager.getEvAlarmAlert().getId(),deviceManager.getEvAlarmAlert());
        this.eventTypes.put(deviceManager.getEvAlarmSnooze().getId(),deviceManager.getEvAlarmSnooze());
        this.eventTypes.put(deviceManager.getEvAlarmDismiss().getId(),deviceManager.getEvAlarmDismiss());
        this.eventTypes.put(deviceManager.getEvAlarmDone().getId(),deviceManager.getEvAlarmDone());
        this.eventTypes.put(deviceManager.getEvCallInc().getId(),deviceManager.getEvCallInc());
        this.stateTypes.put(deviceManager.getStAlarmGoing().getId(),deviceManager.getStAlarmGoing());
        this.stateTypes.put(deviceManager.getStCallIncGoing().getId(),deviceManager.getStCallIncGoing());
        this.actionTypes.put(deviceManager.getAcAlarmSnooze().getId(),deviceManager.getAcAlarmSnooze());
        this.actionTypes.put(deviceManager.getAcAlarmDismiss().getId(),deviceManager.getAcAlarmDismiss());
        this.actionTypes.put(deviceManager.acSendMessage.getId(),deviceManager.acSendMessage);
        // Events
        this.mEvAlarmStart = new Event(deviceManager.getNewId(),"Phone alarm starts ringing", R.drawable.ic_alarm_black_24dp, this, deviceManager.getEvAlarmAlert(), ruleSystemService.getRuleManager().getRuleEngine());
        this.mEvAlarmSnooze = new Event(deviceManager.getNewId(),"Phone alarm snoozed", R.drawable.ic_alarm_off_black_24dp, this, deviceManager.getEvAlarmSnooze(), ruleSystemService.getRuleManager().getRuleEngine());
        this.mEvAlarmDismiss = new Event(deviceManager.getNewId(),"Phone alarm dismissed", R.drawable.ic_alarm_off_black_24dp, this, deviceManager.getEvAlarmDismiss(), ruleSystemService.getRuleManager().getRuleEngine());
        this.mEvAlarmDone = new Event(deviceManager.getNewId(),"Phone alarm done", R.drawable.ic_alarm_off_black_24dp, this, deviceManager.getEvAlarmDone(), ruleSystemService.getRuleManager().getRuleEngine());
        this.mEvCallIncStart = new Event(deviceManager.getNewId(),"Phone call incoming starts", R.drawable.ic_call_black_24dp, this, deviceManager.getEvCallInc(), ruleSystemService.getRuleManager().getRuleEngine());
        this.mEvCallIncStop = new Event(deviceManager.getNewId(),"Phone call incoming stops", R.drawable.ic_call_end_black_24dp, this, deviceManager.getEvCallInc(), ruleSystemService.getRuleManager().getRuleEngine());
        this.mEvCallIncFavStart = new Event(deviceManager.getNewId(),"Phone call incoming from FAVORITES starts", R.drawable.ic_call_black_24dp, this, deviceManager.getEvCallInc(), ruleSystemService.getRuleManager().getRuleEngine());
        this.mEvCallIncFavStop = new Event(deviceManager.getNewId(),"Phone call incoming from FAVORITES stops", R.drawable.ic_call_end_black_24dp, this, deviceManager.getEvCallInc(), ruleSystemService.getRuleManager().getRuleEngine());
        this.mEvCallIncNoFavStart = new Event(deviceManager.getNewId(),"Phone call incoming from NON-FAVORITES starts", R.drawable.ic_call_black_24dp, this, deviceManager.getEvCallInc(), ruleSystemService.getRuleManager().getRuleEngine());
        this.mEvCallIncNoFavStop = new Event(deviceManager.getNewId(),"Phone call incoming from NON-FAVORITES stops", R.drawable.ic_call_end_black_24dp, this, deviceManager.getEvCallInc(), ruleSystemService.getRuleManager().getRuleEngine());
        // States
        this.mStAlarmGoing = new State(deviceManager.getNewId(),"Phone alarm is ringing", R.drawable.ic_alarm_black_24dp, this, deviceManager.getStAlarmGoing(), false, ruleSystemService.getRuleManager().getRuleEngine());
        this.mStCallIncGoing = new State(deviceManager.getNewId(),"Phone call incoming going", R.drawable.ic_call_black_24dp, this, deviceManager.getStCallIncGoing(), false, ruleSystemService.getRuleManager().getRuleEngine());
        this.mStCallIncFavGoing = new State(deviceManager.getNewId(),"Phone call incoming from FAVORITES going", R.drawable.ic_call_black_24dp, this, deviceManager.getStCallIncGoing(), false, ruleSystemService.getRuleManager().getRuleEngine());
        this.mStCallIncNoFavGoing = new State(deviceManager.getNewId(),"Phone call incoming from NON-FAVORITES going", R.drawable.ic_call_black_24dp, this, deviceManager.getStCallIncGoing(), false, ruleSystemService.getRuleManager().getRuleEngine());
        // Actions
        this.mAcAlarmDismiss = new Action(deviceManager.getNewId(),"Phone alarm dismiss", R.drawable.ic_alarm_off_black_24dp, this, deviceManager.getAcAlarmDismiss(), new Callable<String>() {
            @Override
            public String call() throws Exception {
                acAlarmDismiss();
                return null;
            }
        });
        this.mAcAlarmSnooze = new Action(deviceManager.getNewId(),"Phone alarm snooze", R.drawable.ic_alarm_off_black_24dp, this, deviceManager.getAcAlarmSnooze(), new Callable<String>(){
            @Override
            public String call() throws Exception {
                acAlarmSnooze();
                return null;
            }
        });
        // Callable is null, will be overridden in instance with the correct parameters using getNotifyCallable(...)
        this.mAcNotify = new NotificationAction(deviceManager.getNewId(),"Phone notification",R.drawable.ic_notifications_active_black_24dp,this,deviceManager.getAcNotify());
        this.mAcSendMessage = new SendMessageAction(deviceManager.getNewId(),"Phone send message to ...", R.drawable.ic_message_black_24dp, this, deviceManager.acSendMessage);
        this.mAcSendMessageCaller = new SendMessageCallerAction(deviceManager.getNewId(),"Phone send message to caller", R.drawable.ic_message_black_24dp, this, deviceManager.acSendMessage);
        this.mAcCallReject = new Action(deviceManager.getNewId(), "Phone reject incoming call", R.drawable.ic_call_end_black_24dp, this, deviceManager.acCallIncReject, new Callable() {
            @Override
            public Object call() throws Exception {
                // Execute the Runnable in 4 seconds
                mHandler.postDelayed(mRunnable, 4000);
                return null;
            }
        });
        this.mAcAudioNormal = new Action(deviceManager.getNewId(), "Phone set audio mode NORMAL", R.drawable.ic_volume_up_black_24dp, this, deviceManager.acAudioMode, new Callable() {
            @Override
            public Object call() throws Exception {
                acSoundModeNormal();
                return null;
            }
        });
        this.mAcAudioVibrate = new Action(deviceManager.getNewId(), "Phone set audio mode VIBRATE", R.drawable.ic_vibration_black_24dp, this, deviceManager.acAudioMode, new Callable() {
            @Override
            public Object call() throws Exception {
                acSoundModeVibrate();
                return null;
            }
        });
        this.mAcAudioSilent = new Action(deviceManager.getNewId(), "Phone set audio mode SILENT", R.drawable.ic_volume_mute_black_24dp, this, deviceManager.acAudioMode, new Callable() {
            @Override
            public Object call() throws Exception {
                acSoundModeSilent();
                return null;
            }
        });
        // Put in hashsets
        this.events.put(mEvAlarmStart.getId(),mEvAlarmStart);
        this.events.put(mEvAlarmSnooze.getId(),mEvAlarmSnooze);
        this.events.put(mEvAlarmDismiss.getId(),mEvAlarmDismiss);
        this.events.put(mEvAlarmDone.getId(),mEvAlarmDone);
        this.events.put(mEvCallIncStart.getId(),mEvCallIncStart);
        this.events.put(mEvCallIncStop.getId(),mEvCallIncStop);
        this.events.put(mEvCallIncFavStart.getId(),mEvCallIncFavStart);
        this.events.put(mEvCallIncFavStop.getId(),mEvCallIncFavStop);
        this.events.put(mEvCallIncNoFavStart.getId(),mEvCallIncNoFavStart);
        this.events.put(mEvCallIncNoFavStop.getId(),mEvCallIncNoFavStop);
        this.states.put(mStAlarmGoing.getId(),mStAlarmGoing);
        this.states.put(mStCallIncGoing.getId(),mStCallIncGoing);
        this.states.put(mStCallIncFavGoing.getId(),mStCallIncFavGoing);
        this.states.put(mStCallIncNoFavGoing.getId(),mStCallIncNoFavGoing);
        this.actions.put(mAcAlarmDismiss.getId(),mAcAlarmDismiss);
        this.actions.put(mAcAlarmSnooze.getId(),mAcAlarmSnooze);
        this.actions.put(mAcNotify.getId(),mAcNotify);
        this.actions.put(mAcSendMessage.getId(),mAcSendMessage);
        this.actions.put(mAcSendMessageCaller.getId(),mAcSendMessageCaller);
        this.actions.put(mAcCallReject.getId(),mAcCallReject);
        this.actions.put(mAcAudioNormal.getId(),mAcAudioNormal);
        this.actions.put(mAcAudioVibrate.getId(),mAcAudioVibrate);
        this.actions.put(mAcAudioSilent.getId(),mAcAudioSilent);
    }

    // These getters only needed for testing!

    public Event getAlarmStart() {
        return mEvAlarmStart;
    }

    public Event getAlarmSnooze() {
        return mEvAlarmSnooze;
    }

    public Event getAlarmDismiss() {
        return mEvAlarmDismiss;
    }

    public Event getAlarmDone() {
        return mEvAlarmDone;
    }

    public Event getCallIncStart() {
        return mEvCallIncStart;
    }

    public Event getCallIncStop() {
        return mEvCallIncStop;
    }

    public State getAlarmGoing() {
        return mStAlarmGoing;
    }

    public State getCallIncGoing() {
        return mStCallIncGoing;
    }

    public Event getCallIncFavStart() {
        return mEvCallIncFavStart;
    }

    public Event getCallIncFavStop() {
        return mEvCallIncFavStop;
    }

    public Event getCallIncNoFavStart() {
        return mEvCallIncNoFavStart;
    }

    public Event getCallIncNoFavStop() {
        return mEvCallIncNoFavStop;
    }

    public State getCallIncFavGoing() {
        return mStCallIncFavGoing;
    }

    public State getCallIncNoFavGoing() {
        return mStCallIncNoFavGoing;
    }

    public Action getAcAlarmDismiss() {
        return mAcAlarmDismiss;
    }

    public Action getAcAlarmSnooze() {
        return mAcAlarmSnooze;
    }

    public NotificationAction getNotify() {
        return mAcNotify;
    }

    public SendMessageAction getSendMessage() {
        return mAcSendMessage;
    }

    public SendMessageCallerAction getSendMessageCaller() {
        return mAcSendMessageCaller;
    }

    public Action getCallReject() {
        return mAcCallReject;
    }

    // FOR SIMULATION TESTS:

    public Action getSimAcAlarmDismiss(){
        Action alarmDismiss = new Action(0,"Phone Alarm Dismiss", 0, this, null, new Callable<String>() {
            @Override
            public String call() throws Exception {
                evAlarmDismiss();
                return null;
            }
        });
        return alarmDismiss;
    }

    public Action getSimAcAlarmSnooze(){
        Action alarmSnooze = new Action(0,"Phone Alarm Snooze", 0, this, null, new Callable<String>() {
            @Override
            public String call() throws Exception {
                evAlarmSnooze();
                return null;
            }
        });
        return alarmSnooze;
    }

    // Real Events and Actions

    public void evCallIncStart(){
        System.out.println("[Phone Event] Call incoming start!");
        mEvCallIncStart.trigger();
        mStCallIncGoing.setState(true);
        // Simulation!
        mEvCallIncFavStart.trigger();
        mStCallIncFavGoing.setState(true);
        mEvCallIncNoFavStart.trigger();
        mStCallIncNoFavGoing.setState(true);
    }

    public void evCallIncStop(){
        System.out.println("[Phone Event] Call incoming stop!");
        mEvCallIncStop.trigger();
        mStCallIncGoing.setState(false);
        // Simulation!
        mEvCallIncFavStop.trigger();
        mStCallIncFavGoing.setState(false);
        mEvCallIncNoFavStop.trigger();
        mStCallIncNoFavGoing.setState(false);
    }

    public void evAlarmStart(){
        System.out.println("[Phone Event] Alarm starts going off!");
        mEvAlarmStart.trigger();
        mStAlarmGoing.setState(true);
    }

    public void evAlarmSnooze(){
        System.out.println("[Phone Event] Alarm snoozed!");
        mStAlarmGoing.setState(false);
        mEvAlarmSnooze.trigger();
    }

    public void evAlarmDismiss(){
        System.out.println("[Phone Event] Alarm dismissed!");
        mStAlarmGoing.setState(false);
        mEvAlarmDismiss.trigger();
    }

    public void evAlarmDone(){
        System.out.println("[Phone Event] Alarm done!");
        mStAlarmGoing.setState(false);
        mEvAlarmDone.trigger();
    }

    public void acAlarmSnooze(){
        Intent sonyIntent = new Intent(ALARM_SONY_SNOOZE_ACTION);
        ruleSystemService.getApplicationContext().sendBroadcast(sonyIntent);
        //Intent samsungIntent = new Intent(ALARM_SAMSUNG_SNOOZE_ACTION);
        //ruleSystemService.getApplicationContext().sendBroadcast(samsungIntent);
        //Intent stockIntent = new Intent(ALARM_SNOOZE_ACTION);
        //ruleSystemService.getApplicationContext().sendBroadcast(stockIntent);
        Toast.makeText(ruleSystemService, "Alarm snoozed by TRGRD", Toast.LENGTH_SHORT).show();
        //evAlarmSnooze(); THIS IS DONE in mReceiver already!
    }

    public void acAlarmDismiss(){
        Intent sonyIntent = new Intent(ALARM_SONY_DISMISS_ACTION);
        ruleSystemService.getApplicationContext().sendBroadcast(sonyIntent);
        //Intent samsungIntent = new Intent(ALARM_SAMSUNG_DISMISS_ACTION);
        //ruleSystemService.getApplicationContext().sendBroadcast(samsungIntent);
        //Intent stockIntent = new Intent(ALARM_DISMISS_ACTION);
        //ruleSystemService.getApplicationContext().sendBroadcast(stockIntent);
        Toast.makeText(ruleSystemService, "Alarm dismissed by TRGRD", Toast.LENGTH_SHORT).show();
        //evAlarmDismiss(); THIS IS DONE in mReceiver already!
    }

    public void acSoundModeNormal(){
        AudioManager am;
        am= (AudioManager) ruleSystemService.getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }

    public void acSoundModeVibrate(){
        AudioManager am;
        am= (AudioManager) ruleSystemService.getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
    }

    public void acSoundModeSilent(){
        AudioManager am;
        am= (AudioManager) ruleSystemService.getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }



    // SPECIAL ACTIONS

    public NotificationAction getNotifyAction(String title, String text, NotificationAction.NotificationActionType type){
        NotificationAction instance = new NotificationAction(deviceManager.getNewId(),mAcNotify,title,text,getNotifyCallable(title,text,type));
        actionInstances.put(instance.getId(),instance);
        return instance;
    }

    public void editNotifyAction(NotificationAction notificationAction, String title, String text){
        notificationAction.setTitle(title);
        notificationAction.setText(text);
        notificationAction.setCallable(getNotifyCallable(title,text,notificationAction.getNotificationActionType()));
    }

    public Callable getNotifyCallable(final String title, final String text, final NotificationAction.NotificationActionType type){
        return new Callable<String>(){
            @Override
            public String call() throws Exception {
                acNotify(title, text, type);
                return null;
            }
        };
    }

    public void acNotify(String title, String text, NotificationAction.NotificationActionType type) {

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ruleSystemService);

        // Define the notification settings.
        builder.setSmallIcon(R.drawable.ic_triggered_notification)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(ruleSystemService.getResources(),
                        R.mipmap.ic_launcher_blue))
                .setColor(Color.rgb(104,159,56))
                .setContentTitle(title)
                .setContentText(text);

        if (type == NotificationAction.NotificationActionType.MAIN){
            // Create an explicit content Intent that starts the main Activity.
            Intent notificationIntent = new Intent(ruleSystemService, MainActivity.class);
            // Construct a task stack.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(ruleSystemService);
            // Add the main Activity to the task stack as the parent.
            stackBuilder.addParentStack(MainActivity.class);
            // Push the content Intent onto the stack.
            stackBuilder.addNextIntent(notificationIntent);
            // Get a PendingIntent containing the entire back stack.
            PendingIntent notificationPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(notificationPendingIntent)
                    .setContentText(ruleSystemService.getString(R.string.geofence_transition_notification_text));
        }

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) ruleSystemService.getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }


    @Override
    public SendMessageCallerAction getSendMessageCallerAction(String message) {
        SendMessageCallerAction instance = new SendMessageCallerAction(deviceManager.getNewId(),mAcSendMessageCaller,message,getSendMessageCallerCallable(message));
        actionInstances.put(instance.getId(),instance);
        return instance;
    }

    @Override
    public void editSendMessageCallerAction(SendMessageCallerAction sendMessageCallerAction, String message) {
        sendMessageCallerAction.setMessage(message);
        sendMessageCallerAction.setCallable(getSendMessageCallerCallable(message));
    }

    @Override
    public Callable getSendMessageCallerCallable(final String message) {
        return new Callable<String>(){
            @Override
            public String call() throws Exception {
                acSendMessageCaller(message);
                return null;
            }
        };
    }

    @Override
    public void acSendMessageCaller(String message) {
        MyLogger.debugLog("TRGRD","AndroidPhone acSendMessage to ... : "+message);
        SmsManager smsManager =     SmsManager.getDefault();
        // caller phone number simulation !!
        smsManager.sendTextMessage(callerPhoneNumber, null, message, null, null);
        acNotify("Phone","Your message has been sent to the caller!", NotificationAction.NotificationActionType.NONE);
    }

    @Override
    public SendMessageAction getSendMessageAction(String phonenumber, String message) {
        SendMessageAction instance = new SendMessageAction(deviceManager.getNewId(),mAcSendMessage,phonenumber,message,getSendMessageCallable(phonenumber,message));
        actionInstances.put(instance.getId(),instance);
        return instance;
    }

    @Override
    public void editSendMessageAction(SendMessageAction sendMessageAction, String phonenumber, String message) {
        sendMessageAction.setPhonenumber(phonenumber);
        sendMessageAction.setMessage(message);
        sendMessageAction.setCallable(getSendMessageCallable(phonenumber, message));
    }

    @Override
    public Callable getSendMessageCallable(final String phonenumber, final String message) {
        return new Callable<String>(){
            @Override
            public String call() throws Exception {
                acSendMessage(phonenumber, message);
                return null;
            }
        };
    }

    @Override
    public void acSendMessage(String phonenumber, String message) {
        MyLogger.debugLog("TRGRD","AndroidPhone acSendMessage to "+phonenumber+": "+message);
        SmsManager smsManager =     SmsManager.getDefault();
        smsManager.sendTextMessage(phonenumber, null, message, null, null);
        acNotify("Phone","Your message has been sent to "+phonenumber+"!", NotificationAction.NotificationActionType.NONE);
    }
    
    // Register & UnRegister Android Phone Broadcast Receiver

    public void registerAndroidPhoneReceiver(){
        // SONY
        IntentFilter filter = new IntentFilter(ALARM_SONY_ALERT_ACTION);
        filter.addAction(ALARM_SONY_SNOOZE_ACTION);
        filter.addAction(ALARM_SONY_DISMISS_ACTION);
        filter.addAction(ALARM_SONY_DONE_ACTION);
//        // SAMSUNG
//        filter.addAction(ALARM_SAMSUNG_ALERT_ACTION);
//        filter.addAction(ALARM_SAMSUNG_SNOOZE_ACTION);
//        filter.addAction(ALARM_SAMSUNG_DISMISS_ACTION);
//        filter.addAction(ALARM_SAMSUNG_DONE_ACTION);
//        filter.addAction(ALARM_SAMSUNG_CLOCKPACKAGE);
//        // STOCK
//        filter.addAction(ALARM_ALERT_ACTION);
//        filter.addAction(ALARM_SNOOZE_ACTION);
//        filter.addAction(ALARM_DISMISS_ACTION);
//        filter.addAction(ALARM_DONE_ACTION);
        filter.addAction(CALL_PHONE_STATE);
        filter.addAction(CALL_NEW_OUTGOING_CALL);
        ruleSystemService.registerReceiver(mReceiver, filter);
        Log.d("TRGRD","AndroidPhone mReceiver registered!");
    }

    public void unRegisterAndroidPhoneReceiver(){
        ruleSystemService.unregisterReceiver(mReceiver);
        Log.d("TRGRD","AndroidPhone mReceiver unRegistered!");
    }

    @Override
    public void start(){
        this.registerAndroidPhoneReceiver();
        this.started = true;
        //deviceManager.sendRefreshBroadcast();
    }

    @Override
    public void stop(){
        this.unRegisterAndroidPhoneReceiver();
        this.started = false;
        //deviceManager.sendRefreshBroadcast();
    }
}
