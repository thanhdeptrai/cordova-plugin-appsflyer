package com.appsflyer.cordova.plugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;

import android.content.Context;
import android.util.Log;

public class AppsFlyerPlugin extends CordovaPlugin {
	
	@Override
	public boolean execute(final String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if("setCurrencyCode".equals(action))
		{
			setCurrencyCode(args);
			return true;
		}
		else if("setAppUserId".equals(action))
		{
			setAppUserId(args, callbackContext);
			return true;
		}
		else if("getAppsFlyerUID".equals(action))
		{
			getAppsFlyerUID(args, callbackContext);
			return true;
		}
		else if("sendTrackingWithEvent".equals(action))
		{
			sendTrackingWithEvent(args);
			return true;
		}
		else if("initSdk".equals(action))
		{
			initSdk(args,callbackContext);
			return true;
		}
		else if ("trackEvent".equals(action)) {
			trackEvent(args);
		}

		return false;
	}
	
	private void initSdk(JSONArray parameters, final CallbackContext callbackContext) {
		String devKey = null;
		try
		{
			devKey = parameters.getString(0);
			if(devKey != null){
				AppsFlyerLib.setAppsFlyerKey(devKey);
				initListener();
			}
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
			return;
		}
    	
		AppsFlyerLib.registerConversionListener(cordova.getActivity().getApplicationContext(), new AppsFlyerConversionListener(){

			@Override
			public void onAppOpenAttribution(Map<String, String> arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAttributionFailure(String errorMessage) {
				//Added this to avoid compilation failure
			}

			@Override
			public void onInstallConversionDataLoaded(Map<String, String> conversionData) {
				final String json = new JSONObject(conversionData).toString();
				webView.getView().post(new Runnable() {
					public void run() {
						webView.loadUrl("javascript:window.plugins.appsFlyer.onInstallConversionDataLoaded('"+json+"')");
					}
				});
			}

			@Override
			public void onInstallConversionFailure(String arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
            	
	}
	
	private void initListener() {
		Runnable task = new Runnable() {
		    public void run() {
		    	AppsFlyerLib.sendTracking(cordova.getActivity().getApplicationContext());
		    }
		};
		ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
		worker.schedule(task, 500, TimeUnit.MILLISECONDS);
	}

 	@Override
    public Object onMessage(String id, Object data) {
        return null;
    }
    
	private void sendTrackingWithEvent(JSONArray parameters) {
		String eventName = null;
		String eventValue = "";
		try
		{
			eventName = parameters.getString(0);
			eventValue = parameters.getString(1);
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
			return;
		}
		if(eventName == null || eventName.length()==0)
		{
			return;
		}
		Context c = this.cordova.getActivity().getApplicationContext();
		AppsFlyerLib.sendTrackingWithEvent(c,eventName,eventValue);
	}

	private void trackEvent(JSONArray parameters) {
		String eventName = null;
		Map<String, Object> eventValues = null;
		try
		{
			eventName = parameters.getString(0);
			JSONObject jsonEventValues = parameters.getJSONObject(1);
			eventValues = jsonTOMap(jsonEventValues.toString());

		}
		catch (JSONException e) 
		{
			e.printStackTrace();
			return;
		}
		if(eventName == null || eventName.length()==0)
		{
			return;
		}
		Context c = this.cordova.getActivity().getApplicationContext();
		AppsFlyerLib.trackEvent(c, eventName, eventValues);
	}

	private void setCurrencyCode(JSONArray parameters)
	{
		String currencyId=null;
		try
		{
			currencyId = parameters.getString(0);
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
			return;
		}
		if(currencyId == null || currencyId.length()==0)
		{
			return;
		}
		AppsFlyerLib.setCurrencyCode(currencyId);
	
	}
	
	private void setAppUserId(JSONArray parameters, CallbackContext callbackContext)
	{
		try
		{
			String customeUserId = parameters.getString(0);
			if(customeUserId == null || customeUserId.length()==0)
			{
				return;
			}
        	AppsFlyerLib.setAppUserId(customeUserId);
        	PluginResult r = new PluginResult(PluginResult.Status.OK);
        	r.setKeepCallback(false);
        	callbackContext.sendPluginResult(r);
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
			return;
		}	
	}
	
	private void getAppsFlyerUID(JSONArray parameters, CallbackContext callbackContext)
	{
    	String id = AppsFlyerLib.getAppsFlyerUID(cordova.getActivity().getApplicationContext());
    	PluginResult r = new PluginResult(PluginResult.Status.OK, id);
    	r.setKeepCallback(false);
    	callbackContext.sendPluginResult(r);
	}

	private static Map<String,Object> jsonTOMap(String inputString){
		Map<String,Object> newMap = new HashMap<String, Object>();

		try {
			JSONObject jsonObject = new JSONObject(inputString);
			Iterator iterator = jsonObject.keys();
			while (iterator.hasNext()){
				String key = (String) iterator.next();
				newMap.put(key,jsonObject.getString(key));

			}
		} catch(JSONException e) {
			return null;
		}

		return newMap;
	}
}
