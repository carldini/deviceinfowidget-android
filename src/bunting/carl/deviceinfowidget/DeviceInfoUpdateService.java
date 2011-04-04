package bunting.carl.deviceinfowidget;

import android.app.PendingIntent;
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
	private RemoteViews updateViews;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
	    IntentFilter batteryIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	    this.registerReceiver(this.batteryBroadcastReceiver, batteryIntentFilter);
	    
	    IntentFilter networkIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
	    this.registerReceiver(this.networkBroadcastReceiver, networkIntentFilter);
		
        if (this.updateViews == null) {
        	this.updateView(this);
        }
        Intent wifiScannerIntent = new Intent(this, WifiScanner.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, wifiScannerIntent, 0);
        this.updateViews.setOnClickPendingIntent(R.id.deviceInfoWidget_wifi_ipAddressValue, pendingIntent);

        // Push update for this widget to the home screen
        ComponentName thisWidget = new ComponentName(this, DeviceInfoWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(thisWidget, this.updateViews);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(this.batteryBroadcastReceiver);
		this.unregisterReceiver(this.networkBroadcastReceiver);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (this.updateViews == null) {
        	this.updateView(this);
        }
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	private void updateView(Context context) {
		this.updateViews = new RemoteViews(context.getPackageName(), R.layout.deviceinfowidget);
	}
	

	@Override
	public IBinder onBind(Intent intent) {
		// not used
		return null;
	}

}
