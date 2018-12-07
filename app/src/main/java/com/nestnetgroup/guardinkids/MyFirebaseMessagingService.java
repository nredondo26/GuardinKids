package com.nestnetgroup.guardinkids;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0) {

          String mensaje = remoteMessage.getData().get("Tipo");

          if(mensaje.equals("imagen")){
              showNotification();
          }

        }


    }





    private void showNotification() {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_notificacion)
                .setLargeIcon((((BitmapDrawable)getResources().getDrawable(R.drawable.logo)).getBitmap()))
                .setContentTitle("GuardinKids")
                .setContentText("Su hijo(a) puede estar en riesgo")
                .setAutoCancel(true);

        Intent i = new Intent(getApplicationContext(), Principal.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_ONE_SHOT);
        notificationBuilder.setContentIntent(pendingIntent);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSound(alarmSound);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(1, notificationBuilder.build());
    }


}
