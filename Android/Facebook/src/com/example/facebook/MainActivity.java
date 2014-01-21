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
 * res/values/strings.xml の mb_application_key, mb_client_key, app_id を適宜置き換えてください。
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
        			// 成功
        			if (user.isNew()) {
            			Log.d("mb_fb", "Facebookでログイン 新規登録");
        				text.setText("Facebookでログイン 新規登録");
        			} else {
            			Log.d("mb_fb", "Facebookでログイン");
        				text.setText("Facebookでログイン");
        			}
        		} else {
        			// 失敗
        			Log.d("mb_fb", "Facebookでログイン失敗: " + err.getMessage());
    				text.setText("Facebookでログイン失敗: " + err.getMessage());
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
