package com.example.facebook;

import com.nifty.cloud.mb.LogInCallback;
import com.nifty.cloud.mb.NCMB;
import com.nifty.cloud.mb.NCMBException;
import com.nifty.cloud.mb.NCMBFacebookUtils;
import com.nifty.cloud.mb.NCMBUser;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

/**
 * res/values/strings.xml �� mb_application_key, mb_client_key, app_id ��K�X�u�������Ă��������B
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NCMB.initialize(this, getString(R.string.mb_application_key), getString(R.string.mb_client_key));

        NCMBFacebookUtils.initialize(getString(R.string.app_id));

        NCMBFacebookUtils.logIn(this, new LogInCallback() {
        	@Override
        	public void done(NCMBUser user, NCMBException err) {
        		TextView text = (TextView)findViewById(R.id.textViewLoginState);
        		if (err == null) {
        			// ����
        			if (user.isNew()) {
            			Log.d("mb_fb", "Facebook�Ń��O�C�� �V�K�o�^");
        				text.setText("Facebook�Ń��O�C�� �V�K�o�^");
        			} else {
            			Log.d("mb_fb", "Facebook�Ń��O�C��");
        				text.setText("Facebook�Ń��O�C��");
        			}
        		} else {
        			// ���s
        			Log.d("mb_fb", "Facebook�Ń��O�C�����s: " + err.getMessage());
    				text.setText("Facebook�Ń��O�C�����s: " + err.getMessage());
        		}
        	}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	NCMBFacebookUtils.getSession().onActivityResult(this, requestCode, resultCode, data);
    }
}
