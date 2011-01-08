package bunting.carl.deviceinfowidget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.widget.RemoteViews;

public class DeviceInfoUpdateService extends Service {
	
	private BatteryBroadcastReceiver batteryBroadcastReceiver = new BatteryBroadcastReceiver();
	private NetworkBroadcastReceiver networkBroadcastReceiver = new NetworkBroadcastReceiver();
	
	@Override
	public void onCreate() {
		super.onCreate();
		
	    IntentFilter batteryIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	    this.registerReceiver(this.batteryBroadcastReceiver, batteryIntentFilter);
	    
	    IntentFilter networkIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
	    this.registerReceiver(this.networkBroadcastReceiver, networkIntentFilter);
		
        // Build the widget update for today
        RemoteViews updateViews = updateView(this);

        // Push update for this widget to the home screen
        ComponentName thisWidget = new ComponentName(this, DeviceInfoWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(thisWidget, updateViews);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(this.batteryBroadcastReceiver);
		this.unregisterReceiver(this.networkBroadcastReceiver);
	}
	
	private RemoteViews updateView(Context context) {
        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.deviceinfowidget);
        return updateViews;
	}
	

	@Override
	public IBinder onBind(Intent intent) {
		// not used
		return null;
	}

}
