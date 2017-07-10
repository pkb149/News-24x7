package com.pkb149.news24x7;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import static com.pkb149.news24x7.MainActivityTab1.CUSTOM_INTENT;
import static com.pkb149.news24x7.MainActivityTab2.TRENDING_INTENT;

/**
 * Created by CoderGuru on 06-07-2017.
 */

public class MyReceiver extends BroadcastReceiver {
    InterfaceToUpdateUI interfaceToUpdateUI=null;
    InterfaceToUpdateUiOfTrending interfaceToUpdateUiOfTrending=null;
    private final Handler handler; // Handler used to execute code on the UI thread
    public MyReceiver(Handler handler) {
        this.handler = handler;
    }
    @Override
    public void onReceive(final Context context, final Intent intent) {
        // Post the UI updating code to our Handler
        String str = intent.getAction();
        if(str.equals(CUSTOM_INTENT)){
            interfaceToUpdateUI.updateUI(context);
        }
       else if(str.equals(TRENDING_INTENT)){
            interfaceToUpdateUiOfTrending.updateUI(context);
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
            }
        });
    }
}
