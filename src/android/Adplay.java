package com.vm5.plugins;

import com.vm5.adplay.AdplayCloseCode;
import com.vm5.adplay.AdplayErrorCode;
import com.vm5.adplay.AdplayListener;
import com.vm5.adplay.AdplayManager;
import com.vm5.adplay.AdplayToStoreCode;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andy on 7/6/16.
 */

public class Adplay extends CordovaPlugin implements AdplayListener{

    public static final String ACTION_INIT = "init";
    public static final String ACTION_SHOW = "show";
    private CallbackContext mCallbackContext = null;
    private PluginResult result = null;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        AdplayManager.getInstance().setListener(this);
        mCallbackContext = callbackContext;
        result = new PluginResult(PluginResult.Status.NO_RESULT);
        result.setKeepCallback(true);
        if(ACTION_INIT.equals(action)){
            init(args,callbackContext);
            return true;
        } else if(ACTION_SHOW.equals(action)){
            show(args,callbackContext);
            return true;
        }

        return false;
    }

    private void init(JSONArray args, CallbackContext callbackContext){
        String userId = null;
        String key = null;
        try {
            userId = "" + args.getJSONObject(0).getString("userId");
            key = "" + args.getJSONObject(0).getString("key");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(userId!=null && key!=null){
            AdplayManager.getInstance().init(cordova.getActivity(),userId,key);
        } else {
            callbackContext.error("unauthorized!!");
        }
    }

    private void show(JSONArray args, CallbackContext callbackContext){
        String ad_id = null;
        try {
            ad_id = "" + args.getJSONObject(0).getString("ad_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(ad_id!=null){
            AdplayManager.getInstance().show(ad_id);
        } else {
            callbackContext.error("ad_id not found!!");
        }
    }

    private void sendCallback(){
        result.setKeepCallback(false);
        if (mCallbackContext != null) {
            mCallbackContext.sendPluginResult(result);
            mCallbackContext = null;
        }
    }

    @Override
    public void onAdplayShown(String ad_id) {

    }

    @Override
    public void onAdplayError(String ad_id, AdplayErrorCode err) {
        JSONObject errorCode = new JSONObject();
        JSONObject jo = new JSONObject();
        try {
            errorCode.put("code",err.getCode());
            errorCode.put("description",err.getDescription());
            jo.put(ad_id,errorCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result = new PluginResult(PluginResult.Status.ERROR,
                jo);
        sendCallback();
    }

    @Override
    public void onAdplayClosed(String ad_id, AdplayCloseCode close) {

    }

    @Override
    public void onAdplayFinished(String ad_id) {

    }

    @Override
    public void onAdplayAdListReady(String[] ad_list) {
        JSONArray ja = new JSONArray();
        for (String node: ad_list) {
            ja.put(node);
        }
        result = new PluginResult(PluginResult.Status.OK, ja);
        sendCallback();
    }

    @Override
    public void onAdplayAdListComplete() {

    }

    @Override
    public void onAdplayToStore(String ad_id, AdplayToStoreCode code) {

    }
}
