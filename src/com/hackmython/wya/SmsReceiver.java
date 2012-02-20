package com.hackmython.wya;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
//import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
	public ArrayList<String> locationRequestQueue;
	public LocationManager lm;
	public LocationListener locationListener;
	public Context mcontext; 

    @Override
    public void onReceive(Context context, Intent intent) 
    {
    	Log.d("WHEREYOUAPP", "OnReceive called");
    	
    	if(locationRequestQueue == null){
    		locationRequestQueue = new ArrayList<String>();
    	}
    	mcontext = context;
    	//Get Password
    	SharedPreferences passwdfile = context.getSharedPreferences(
				WhereYouApp.PREF_KEY, 0);
		
		String correctMd5 = passwdfile.getString(WhereYouApp.PASSWORD_PREF_KEY,
				null);
		
    	Log.d("WHEREYOUAPP", "Got Password");

        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();        
        SmsMessage[] msgs = null;
        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];      
            
            Log.d("WHEREYOUAPP", "Got message bundle");
            
            if(locationListener == null){
				// Define a listener that responds to location updates
				locationListener = new LocationListener() {
				    public void onLocationChanged(Location location) {
				    	sendMessage(location);
	
				    }
	
				    public void onStatusChanged(String provider, int status, Bundle extras) {
//				    	if(locationRequestQueue!= null && locationRequestQueue.size() > 0){
//					    	SmsManager sm = SmsManager.getDefault();
//					    	
//					    	//TODO: Reverse Geocode
//					    	String text = (new Date(lm.getLastKnownLocation(provider).getTime())).toString().substring(0, 16) + " Lon: " + lm.getLastKnownLocation(provider).getLongitude() + " Lat: " + lm.getLastKnownLocation(provider).getLatitude() + " Acc: " + lm.getLastKnownLocation(provider).getAccuracy() + "m";
//				    		for(String number : locationRequestQueue){
//				    			//TODO: OR USE TWILIO
//				    		    sendSMS(number,text);
//
//	    						Toast.makeText(mcontext, "Where You App Location Sent To" + number + " - \"" + text + "\"", 
//	    								Toast.LENGTH_LONG).show();
//				    		}
//				    		locationRequestQueue.clear();
//
//				    	}
//				    	lm.removeUpdates(locationListener);
				    }
	
				    public void onProviderEnabled(String provider) {}
	
				    public void onProviderDisabled(String provider) {
//				    	if(locationRequestQueue!= null && locationRequestQueue.size() > 0){
//					    	SmsManager sm = SmsManager.getDefault();
//	
//					    	//TODO: Reverse Geocode
//					    	String text = (new Date(lm.getLastKnownLocation(provider).getTime())).toString().substring(0, 16) + " Lon: " + lm.getLastKnownLocation(provider).getLongitude() + " Lat: " + lm.getLastKnownLocation(provider).getLatitude() + " Acc: " + lm.getLastKnownLocation(provider).getAccuracy() + "m";;
//				    		for(String number : locationRequestQueue){
//				    			//TODO: OR USE TWILIO
//				    		    sendSMS(number,text);
//
//	    						Toast.makeText(mcontext, "Where You App Location Sent To" + number + " - \"" + text + "\"", 
//	    								Toast.LENGTH_SHORT).show();
//				    		}
//				    		locationRequestQueue.clear();
//				    	}
//				    	lm.removeUpdates(locationListener);
				    }
				  };

            }
            for (int i=0; i<msgs.length; i++){
                String str = "";            

                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
                str += "SMS from " + msgs[i].getOriginatingAddress();                     
                str += " :";
                str += msgs[i].getMessageBody().toString();
                str += "\n";        
            
	            //---display the new SMS message---
	            Toast.makeText(context, str, Toast.LENGTH_LONG).show();
	            
	            Log.d("WHEREYOUAPP", "Parsing Message " + i);
	            String msg = str;
	            
	          	SharedPreferences triggersFile = WhereYouApp.mcontext.getSharedPreferences(
	        			WhereYouApp.TRIGGERS_KEY, 0);
	            for(Object key : triggersFile.getAll().values()){
	            	if(msg.contains((String)key)){
	            		Log.d("WHEREYOUAPP", "Approved: adding address" + msgs[i].getOriginatingAddress());
    		            findLocation(msgs[i].getOriginatingAddress());

	            	}
	            }
	            
	    		if (correctMd5 != null) {
	    			String[] tokens = msg.split("(\\s+|\\:)");
	    			for(String word : tokens){
	    				if (tokens.length >= 2) {
	    					String md5hash = WhereYouApp.getMd5Hash(word);
	    		            Log.d("WHEREYOUAPP", "Parsing Message Word: \"" + word + "\"");

	    					if (md5hash.equals(correctMd5)) {
		    		            Log.d("WHEREYOUAPP", "Approved: adding address" + msgs[i].getOriginatingAddress());
		    		            findLocation(msgs[i].getOriginatingAddress());

	    					}
	    				}
	    			}
	    		}
            }
        } 

		
		
		
	}
    
    private void sendMessage(Location location){
    	//TODO: add to preferences
    	if(location.getAccuracy()<100){
	      	// Called when a new location is found by the network location provider.
	    	if(locationRequestQueue!= null && locationRequestQueue.size() > 0){
		    	SmsManager sm = SmsManager.getDefault();
	
		    	//TODO: Reverse Geocode
		    	String text = (new Date(location.getTime())).toString().substring(0, 16) + " Lon: " + location.getLongitude() + " Lat: " + location.getLatitude() + " Acc: " + location.getAccuracy() + "m";
	    		for(String number : locationRequestQueue){
	    			//TODO: OR USE TWILIO
	    		    sendSMS(number,text);
	
					Toast.makeText(mcontext, "Where You App Location Sent To" + number + " - \"" + text + "\"", 
							Toast.LENGTH_SHORT).show();
	    		}
	    		
	    	}
			lm.removeUpdates(locationListener);
    	}
    }
    
    private void findLocation(String number){
    	//TODO: add preference for update interval tolerance
    	int toleranceInMS = 60000;
		// Acquire a reference to the system Location Manager
		lm = 
			(LocationManager) mcontext.getSystemService(Context.LOCATION_SERVICE);

    	if((new Date()).after(new Date(lm.getLastKnownLocation(lm.getBestProvider(new Criteria(), false)).getTime()+toleranceInMS))
        	|| lm.getLastKnownLocation(lm.getBestProvider(new Criteria(), false)).getAccuracy() > 100){

    		locationRequestQueue.add(number);

            Log.d("WHEREYOUAPP", "Starting locationManagerUpdates");

    		// Register the listener with the Location Manager to receive location updates
    		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    	}
    	else{
    		sendMessage(lm.getLastKnownLocation(lm.getBestProvider(new Criteria(), false)));
    	}

    }
    
    //---sends an SMS message to another device---
    private void sendSMS(String phoneNumber, String message)
    {        
    	if(false){
        Log.d("WHEREYOUAPP", "Sending: " + message + " TO " + phoneNumber);
        String sent = "android.telephony.SmsManager.STATUS_ON_ICC_SENT";
        PendingIntent piSent = PendingIntent.getBroadcast(WhereYouApp.mcontext, 0,new Intent(sent), 0);                
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, piSent, null); 
    	}
    	else{
         
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
 
        PendingIntent sentPI = PendingIntent.getBroadcast(mcontext, 0,
            new Intent(SENT), 0);
 
        PendingIntent deliveredPI = PendingIntent.getBroadcast(mcontext, 0,
            new Intent(DELIVERED), 0);
 
        //---when the SMS has been sent---
        WhereYouApp.mcontext.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(mcontext, "SMS sent", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(mcontext, "Generic failure", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(mcontext, "No service", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(mcontext, "Null PDU", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(mcontext, "Radio off", 
                                Toast.LENGTH_SHORT).show();
                        break;
                }
                //TODO: add number back to queue if sending unsuccessful
	    		//locationRequestQueue.add(phoneNumber);
            }
        }, new IntentFilter(SENT));
 
        //---when the SMS has been delivered---
        WhereYouApp.mcontext.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(mcontext, "SMS delivered", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(mcontext, "SMS not delivered", 
                                Toast.LENGTH_SHORT).show();
                        break;                        
                }
            }
        }, new IntentFilter(DELIVERED));        
 
        SmsManager sms = SmsManager.getDefault();
        Log.d("WHEREYOUAPP", "Sending: " + phoneNumber + message);
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI); 
    	}
    }
}
