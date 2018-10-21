package tw.alex.servicetest1;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    private Timer timer;
    private MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(mediaPlayer != null) return;
        mediaPlayer = MediaPlayer.create(this, R.raw.waterfall);
        int len = mediaPlayer.getDuration();
        Intent intent = new Intent("alex");
        intent.putExtra("len", len);
        sendBroadcast(intent);


        timer = new Timer();
        timer.schedule(new MyTask(), 0,100);
    }

    private class MyTask extends TimerTask{
        @Override
        public void run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                Intent intent = new Intent("alex");
                intent.putExtra("now", mediaPlayer.getCurrentPosition());
                sendBroadcast(intent);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean isStart = intent.getBooleanExtra("start", false);
        if(isStart){
            if (!mediaPlayer.isPlaying())
            mediaPlayer.start();
        }else{
            if(mediaPlayer !=null && mediaPlayer.isPlaying())
            mediaPlayer.pause();
        }
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if(timer != null){
            timer.cancel();
            timer.purge();
            timer = null;

        }
    }
}
