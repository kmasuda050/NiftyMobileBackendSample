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
 * res/values/strings.xml �� mb_application_key, mb_client_key, gcm_sender_id ��K�X�u�������Ă��������B
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NCMB.initialize(this, getString(R.string.mb_application_key), getString(R.string.mb_client_key));

        final NCMBInstallation instllation = NCMBInstallation.getCurrentInstallation();

        final Handler handler = new Handler();

        // senderId�̎擾��Google Cloud Messaging��Getting Started���Q�Ƃ��Ă�������
        instllation.getRegistrationIdInBackground(getString(R.string.gcm_sender_id), new RegistrationCallback() {
        	@Override
        	public void done(NCMBException e) {
        		if (e == null) {
        			// ����
        			try {
        				instllation.save();
            			Log.d("mb_gcm", "�[���o�^����");
        				MainActivity.this.onTextUpdate(handler, "�[���o�^����");
        			} catch (NCMBException le) {
        				// �T�[�o���ւ̕ۑ��G���[
            			Log.d("mb_gcm", "�T�[�o���ւ̕ۑ����s:" + le.getMessage());
        				MainActivity.this.onTextUpdate(handler, "�T�[�o���ւ̕ۑ����s:" + le.getMessage());
        			}
        		} else {
        			// �G���[
        			Log.d("mb_gcm", "�[���o�^���s:" + e.getMessage());
    				MainActivity.this.onTextUpdate(handler, "�[���o�^���s:" + e.getMessage());
        		}
        	}
        });
        NCMBPush.setDefaultPushCallback(this, MainActivity.class);

        NCMBAnalytics.trackAppOpened(getIntent());
    }
    
    void onTextUpdate(Handler handler, final String text) {
    	// RegistrationCallback.done �� UI �X���b�h�Ƃ͕ʃX���b�h�ŌĂ΂�邽�߁Apost ����K�v������
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
