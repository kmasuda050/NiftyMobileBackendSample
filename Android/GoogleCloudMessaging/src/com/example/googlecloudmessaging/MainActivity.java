package com.example.googlecloudmessaging;

import com.nifty.cloud.mb.NCMB;
import com.nifty.cloud.mb.NCMBAnalytics;
import com.nifty.cloud.mb.NCMBException;
import com.nifty.cloud.mb.NCMBInstallation;
import com.nifty.cloud.mb.NCMBPush;
import com.nifty.cloud.mb.RegistrationCallback;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

/**
 * res/values/strings.xml の mb_application_key, mb_client_key, gcm_sender_id を適宜置き換えてください。
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NCMB.initialize(this, getString(R.string.mb_application_key), getString(R.string.mb_client_key));

        final NCMBInstallation instllation = NCMBInstallation.getCurrentInstallation();

        final Handler handler = new Handler();

        // senderIdの取得はGoogle Cloud MessagingのGetting Startedを参照してください
        instllation.getRegistrationIdInBackground(getString(R.string.gcm_sender_id), new RegistrationCallback() {
        	@Override
        	public void done(NCMBException e) {
        		if (e == null) {
        			// 成功
        			try {
        				instllation.save();
            			Log.d("mb_gcm", "端末登録成功");
        				MainActivity.this.onTextUpdate(handler, "端末登録成功");
        			} catch (NCMBException le) {
        				// サーバ側への保存エラー
            			Log.d("mb_gcm", "サーバ側への保存失敗:" + le.getMessage());
        				MainActivity.this.onTextUpdate(handler, "サーバ側への保存失敗:" + le.getMessage());
        			}
        		} else {
        			// エラー
        			Log.d("mb_gcm", "端末登録失敗:" + e.getMessage());
    				MainActivity.this.onTextUpdate(handler, "端末登録失敗:" + e.getMessage());
        		}
        	}
        });
        NCMBPush.setDefaultPushCallback(this, MainActivity.class);

        NCMBAnalytics.trackAppOpened(getIntent());
    }
    
    void onTextUpdate(Handler handler, final String text) {
    	// RegistrationCallback.done は UI スレッドとは別スレッドで呼ばれるため、post する必要がある
		handler.post(new Runnable(){
			@Override
			public void run() {
				TextView view = (TextView)findViewById(R.id.textViewLoginState);
				view.setText(text);
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }    
}
