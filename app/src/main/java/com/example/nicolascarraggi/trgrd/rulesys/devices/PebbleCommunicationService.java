package com.example.nicolascarraggi.trgrd.rulesys.devices;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.nicolascarraggi.trgrd.logging.MyLogger;
import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.util.UUID;

public class PebbleCommunicationService extends Service {

    // Constants

    public static final String PEBBLE_BUTTON_UP_EVENT = "com.example.nicolascarraggi.trgrd.rulesys.devices.PEBBLE_BUTTON_UP";
    public static final String PEBBLE_BUTTON_SELECT_EVENT = "com.example.nicolascarraggi.trgrd.rulesys.devices.PEBBLE_BUTTON_SELECT";
    public static final String PEBBLE_BUTTON_DOWN_EVENT = "com.example.nicolascarraggi.trgrd.rulesys.devices.PEBBLE_BUTTON_DOWN";
    public static final String PEBBLE_SHAKE_EVENT = "com.example.nicolascarraggi.trgrd.rulesys.devices.PEBBLE_SHAKE";

    // PebbleCommunicationService Broadcast Receiver

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("TRGRD","PebbleCommunicationService mReceiver "+action);

            if(action.equals(Pebble.PEBBLE_VIBRATE_ACTION)){
                vibrate();
            } else if(action.equals(Pebble.PEBBLE_NOTIFICATION_ACTION)){
                notifyPebble(intent.getStringExtra("title"),intent.getStringExtra("text"));
            } else if(action.equals(Pebble.PEBBLE_SCREEN_TIME_ACTION)){
                screenTime();
            } else if(action.equals(Pebble.PEBBLE_SCREEN_SPORT_ACTION)){
                screenSport(intent.getStringExtra("score"));
            } else if(action.equals(Pebble.PEBBLE_SCREEN_ALARM_ACTION)){
                //screenAlarm();
                screenText("Phone alarm going off!");
            } else if(action.equals(Pebble.PEBBLE_SCREEN_CLEAN_ACTION)){
                screenText(" ");
            }
        }
    };

    private static final UUID WATCHAPP_UUID = UUID.fromString("57dad58f-9c43-4731-96b2-b741bf7231be");
    private static final String WATCHAPP_FILENAME = "TemplateTest.pbw";

    private static final int
            KEY_BUTTON = 0,
            KEY_VIBRATE = 1,
            KEY_TEXT = 2,
            KEY_NOTIF = 3,
            KEY_TAP = 4,
            KEY_TIME = 5,
            KEY_SPORT = 6,
            BUTTON_UP = 0,
            BUTTON_SELECT = 1,
            BUTTON_DOWN = 2;

    private PebbleKit.PebbleDataReceiver mAppMessageReceiver;

    final PebbleDictionary mVibratedict;
    final PebbleDictionary mTextdict;

    public PebbleCommunicationService() {
        mVibratedict = new PebbleDictionary();
        mTextdict = new PebbleDictionary();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        int cmd = super.onStartCommand(intent, flags, startId);
        if (intent == null) return cmd;

        // get Intent parameters?
        //String[] extras= (String[]) intent.getSerializableExtra("data");
        //final String string1 = extras[0];
        //final String string1 = extras[0];

        //Log.d("PebbleCommunication", "onStartCommand 1");

        // Define AppMessage behavior
        if (mAppMessageReceiver == null) {

            //Log.d("PebbleCommunication", "onStartCommand 2");

            mAppMessageReceiver = new PebbleKit.PebbleDataReceiver(WATCHAPP_UUID) {

                @Override
                public void receiveData(Context context, int transactionId, PebbleDictionary data) {
                    // Always ACK
                    PebbleKit.sendAckToPebble(context, transactionId);

                    //Log.d("PebbleCommunication", "onStartCommand 3");

                    // What message was received?
                    if(data.getInteger(KEY_BUTTON) != null) {

                        // KEY_BUTTON was received, determine which button
                        final int button = data.getInteger(KEY_BUTTON).intValue();

                        Intent newIntent;

                        // WITHOUT HANDLER
                        switch (button) {
                            case BUTTON_UP:
                                Log.d("TRGRD", "PebbleCommunication Button Up");
                                Toast.makeText(getApplicationContext(), "Pebble up", Toast.LENGTH_SHORT).show();
                                newIntent = new Intent(PEBBLE_BUTTON_UP_EVENT);
                                LocalBroadcastManager.getInstance(PebbleCommunicationService.this).sendBroadcast(newIntent);
                                break;
                            case BUTTON_SELECT:
                                Log.d("TRGRD", "PebbleCommunication Button Select");
                                Toast.makeText(getApplicationContext(), "Pebble select", Toast.LENGTH_SHORT).show();
                                newIntent = new Intent(PEBBLE_BUTTON_SELECT_EVENT);
                                LocalBroadcastManager.getInstance(PebbleCommunicationService.this).sendBroadcast(newIntent);
                                break;
                            case BUTTON_DOWN:
                                Log.d("TRGRD", "PebbleCommunication Button Down");
                                Toast.makeText(getApplicationContext(), "Pebble down", Toast.LENGTH_SHORT).show();
                                newIntent = new Intent(PEBBLE_BUTTON_DOWN_EVENT);
                                LocalBroadcastManager.getInstance(PebbleCommunicationService.this).sendBroadcast(newIntent);
                                break;
                            default:
                                //Toast.makeText(getApplicationContext(), "Unknown button: " + button, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                    if(data.getInteger(KEY_TAP) != null){
                        Log.d("TRGRD", "PebbleCommunication TAP/SHAKE");
                        Toast.makeText(getApplicationContext(), "Pebble tap/shake", Toast.LENGTH_SHORT).show();
                        Intent newIntent = new Intent(PEBBLE_SHAKE_EVENT);
                        LocalBroadcastManager.getInstance(PebbleCommunicationService.this).sendBroadcast(newIntent);
                    }
                }
            };

            // Add AppMessage capabilities
            PebbleKit.registerReceivedDataHandler(this, mAppMessageReceiver);
        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerPebbleCommunicationReceiver();
    }

    @Override
    public void onDestroy() {
        Log.d("PebbleCommunication","PebbleCommunicationService onDestroy()");
        // Unregister AppMessage reception
        if(mAppMessageReceiver != null) {
            unregisterReceiver(mAppMessageReceiver);
            mAppMessageReceiver = null;
        }
        unRegisterPebbleCommunicationReceiver();
    }

    // Register & UnRegister Pebble Communication Broadcast Receiver

    public void registerPebbleCommunicationReceiver(){
        IntentFilter filter = new IntentFilter(Pebble.PEBBLE_VIBRATE_ACTION);
        filter.addAction(Pebble.PEBBLE_NOTIFICATION_ACTION);
        filter.addAction(Pebble.PEBBLE_SCREEN_TIME_ACTION);
        filter.addAction(Pebble.PEBBLE_SCREEN_SPORT_ACTION);
        filter.addAction(Pebble.PEBBLE_SCREEN_ALARM_ACTION);
        filter.addAction(Pebble.PEBBLE_SCREEN_CLEAN_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);
        Log.d("TRGRD","PebbleCommunicationService mReceiver registered!");
    }

    public void unRegisterPebbleCommunicationReceiver(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        Log.d("TRGRD","PebbleCommunicationService mReceiver unRegistered!");
    }

    // Pebble actions

    private void vibrate(){
        // Send KEY_VIBRATE to Pebble
        mVibratedict.addInt32(KEY_VIBRATE, 0);
        PebbleKit.sendDataToPebble(getApplicationContext(), WATCHAPP_UUID, mVibratedict);
    }

    private void screenTime(){
        //mTextdict.addString(KEY_TIME," ");
        //PebbleKit.sendDataToPebble(getApplicationContext(), WATCHAPP_UUID, mTextdict);
        mVibratedict.addInt32(KEY_TIME, 0);
        PebbleKit.sendDataToPebble(getApplicationContext(), WATCHAPP_UUID, mVibratedict);
        MyLogger.debugLog("TRGRD","Pebble Communiation Service Screen Time");
    }

    private void screenSport(String score){
        mTextdict.addString(KEY_SPORT,score);
        PebbleKit.sendDataToPebble(getApplicationContext(), WATCHAPP_UUID, mTextdict);
        MyLogger.debugLog("TRGRD","Pebble Communiation Service Screen Sport");
    }

    private void screenText(String text){
        mTextdict.addString(KEY_TEXT,text);
        PebbleKit.sendDataToPebble(getApplicationContext(), WATCHAPP_UUID, mTextdict);
    }

    private void notifyPebble(String title, String text){
        Log.d("TRGRD","Notify Pebble");
        mTextdict.addString(KEY_NOTIF,title+"|"+text);
        PebbleKit.sendDataToPebble(getApplicationContext(), WATCHAPP_UUID, mTextdict);
    }

}
