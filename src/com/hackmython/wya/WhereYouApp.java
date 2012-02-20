package com.hackmython.wya;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import java.security.MessageDigest;
import java.util.Date;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WhereYouApp extends Activity {

	public static final String PREF_KEY = "prefs";

	public static final String PASSWORD_PREF_KEY = "password";
	public static final String PASSWORD_LAST_UPDATED_PREF_KEY = "password_last_updated";

	
	public static final String APPROVED_KEY = "approved_users_pref";
	
	public static final String RESPONSE_SETTING_KEY = "response_setting_pref";

	public static final String TRIGGERS_KEY = "triggers_pref";

	public static final String[] default_triggers = {
		"where are you?",
		"where are you\n",
		"where you at",
		"where you be",
		"where are you coming from"
	};



	private TextView messages;
	private EditText pass1;
	private EditText pass2;
	private EditText oldpass;
	private TextView oldpasslabel;


	
	public static Context mcontext;
	public static Activity mactivity;


	@Override
	public void onCreate(Bundle icicle) {
		mcontext = this.getBaseContext();
		mactivity = this;
		super.onCreate(icicle);
		setContentView(R.layout.main);
		
		mcontext = this.getBaseContext();
    	SharedPreferences passwdfile = mcontext.getSharedPreferences(
				WhereYouApp.PREF_KEY, 0);
		String lastUpdated = passwdfile.getString(WhereYouApp.PASSWORD_LAST_UPDATED_PREF_KEY,
				"No Password Set");
		
		if(lastUpdated.equals("No Password Set")){
			
			messages = (TextView) findViewById(R.id.text1);
			oldpass = (EditText) findViewById(R.id.old_password);
			oldpasslabel = (TextView) findViewById(R.id.old_password_labelid);
			oldpass.setVisibility(View.GONE);
			oldpasslabel.setVisibility(View.GONE);

			pass1 = (EditText) findViewById(R.id.password);
			pass2 = (EditText) findViewById(R.id.password_confirm);
	
			Button button = (Button) findViewById(R.id.ok);
			button.setOnClickListener(clickListener);
			
			messages.setText(lastUpdated);
			
			//Set triggers
	    	SharedPreferences triggersFile = mcontext.getSharedPreferences(
					WhereYouApp.TRIGGERS_KEY, 0);
	    	Editor triggerEditor = triggersFile.edit();
	    	if(triggersFile.getAll() == null || triggersFile.getAll().size() == 0){
	    		Log.d("WHEREYOUAPP", "Adding Default Triggers");
	    		for(int i = 0; i < default_triggers.length; i++){
		    		Log.d("WHEREYOUAPP", "Adding Default Triggers: " + default_triggers[i]);
		    		triggerEditor.putString(""+i, default_triggers[i]);
	    		}
	    		triggerEditor.commit();
	    	}
		}
		else{
			//if(this.getIntent().getAction().equals("CHANGEPASS")){
				messages = (TextView) findViewById(R.id.text1);
				oldpass = (EditText) findViewById(R.id.old_password);
				oldpasslabel = (TextView) findViewById(R.id.old_password_labelid);
				pass1 = (EditText) findViewById(R.id.password);
				pass2 = (EditText) findViewById(R.id.password_confirm);
		
				Button button = (Button) findViewById(R.id.ok);
				button.setOnClickListener(clickListener);
				
				messages.setText(lastUpdated);
				
		    	SharedPreferences triggersFile = mcontext.getSharedPreferences(
						WhereYouApp.TRIGGERS_KEY, 0);
		    	Editor triggerEditor = triggersFile.edit();
		    	if(triggersFile.getAll() == null || triggersFile.getAll().size() == 0){
		    		Log.d("WHEREYOUAPP", "Adding Default Triggers");
		    		for(int i = 0; i < default_triggers.length; i++){
			    		Log.d("WHEREYOUAPP", "Adding Default Triggers: " + default_triggers[i]);
			    		triggerEditor.putString(""+i, default_triggers[i]);
		    		}
		    		triggerEditor.commit();
		    	}
//			}else{
//		        // When the button is clicked, launch an activity through this intent
//		        Intent launchPreferencesIntent = new Intent().setClass(this, Preferences.class);
//		        this.startActivity(launchPreferencesIntent);
//	        }
		}

	}

	private OnClickListener clickListener = new OnClickListener() {

		public void onClick(View v) {
			String p1 = pass1.getText().toString();
			String p2 = pass2.getText().toString();
			String old = oldpass.getText().toString();
	    	SharedPreferences passfile = mcontext.getSharedPreferences(
					WhereYouApp.PREF_KEY, 0);
	    	String oldcheck = passfile.getString(PASSWORD_PREF_KEY, "");
			if(!getMd5Hash(old).equals(oldcheck)){
				Toast.makeText(mcontext, "Incorrect Password. Try Again.", 
						Toast.LENGTH_SHORT).show();
				return;
			}
			
			
			if (p1.equals(p2)) {

				if (p1.length() >= 6 || p2.length() >= 6) {

					Editor passwdfile = getSharedPreferences(WhereYouApp.PREF_KEY, 0).edit();
					String md5hash = getMd5Hash(p1);
					passwdfile.putString(WhereYouApp.PASSWORD_PREF_KEY,
							md5hash);
					passwdfile.putString(WhereYouApp.PASSWORD_LAST_UPDATED_PREF_KEY,
							"Last Updated: " + (new Date()).toString());
					passwdfile.commit();
					Toast.makeText(mcontext, "Password updated! " + (new Date()).toString(), 
							Toast.LENGTH_SHORT).show();
					messages.setText("Password updated! " + (new Date()).toString());
					mactivity.finish();

				} else
					Toast.makeText(mcontext, "Passwords must be at least 6 characters", 
							Toast.LENGTH_SHORT).show();
					messages.setText("Passwords must be at least 6 characters");

			} else {
				pass1.setText("");
				pass2.setText("");
				Toast.makeText(mcontext, "Passwords do not match", 
						Toast.LENGTH_SHORT).show();
				messages.setText("Passwords do not match");
			}

		}

	};

	public static String getMd5Hash(String input) {
		try	{
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1,messageDigest);
			String md5 = number.toString(16);
	    	
			while (md5.length() < 32)
				md5 = "0" + md5;
	    	
			return md5;
		} catch(NoSuchAlgorithmException e) {
			Log.e("MD5", e.getMessage());
			return null;
		}
	}
	
}