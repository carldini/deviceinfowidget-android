package bunting.carl.deviceinfowidget;

import bunting.carl.deviceinfowidget.R;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.RemoteViews;

/**
 * Based on http://android-er.blogspot.com/2010/10/home-screen-battery-widget.html
 */
public class BatteryBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {

			String batteryLevel = String.valueOf(intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)) + "%";
			String batteryVoltage = " " + String.valueOf((float) intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0) / 1000) + "V";
			String batteryTemperature = " " + String.valueOf((float) intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10) + "c";
			String batteryTechnology = " " + intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
			String batteryPlugged = this.getBatteryPlugged(intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0));

			int status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
			String strStatus;
			if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
				strStatus = "Charging" + batteryPlugged + batteryTemperature;
			} else if (status == BatteryManager.BATTERY_STATUS_DISCHARGING) {
				strStatus = "Discharging" + batteryTechnology + batteryVoltage;
			} else if (status == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
				strStatus = "Not charging";
			} else if (status == BatteryManager.BATTERY_STATUS_FULL) {
				strStatus = "Full" + batteryPlugged + batteryTemperature + batteryVoltage;
			} else {
				strStatus = "Unknown";
			}
			
			int health = intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN);
			String strHealth;
			if (health == BatteryManager.BATTERY_HEALTH_GOOD) {
				strHealth = "Good";
			} else if (health == BatteryManager.BATTERY_HEALTH_OVERHEAT) {
				strHealth = "Over Heat";
			} else if (health == BatteryManager.BATTERY_HEALTH_DEAD) {
				strHealth = "Dead";
			} else if (health == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) {
				strHealth = "Over Voltage";
			} else if (health == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE) {
				strHealth = "Unspecified Failure";
			} else {
				strHealth = "Unknown";
			}
			String batteryHealth = "Health: " + strHealth;
			
	        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.deviceinfowidget);
	        updateViews.setTextViewText(R.id.deviceInfoWidget_batteryChargeValue, batteryLevel + " " + strStatus);
	        
	        ComponentName myComponentName = new ComponentName(context, DeviceInfoWidgetProvider.class);
	        AppWidgetManager manager = AppWidgetManager.getInstance(context);
	        manager.updateAppWidget(myComponentName, updateViews);
		}
	}
	
	private String getBatteryPlugged(int pluggedType) {
		switch (pluggedType) {
		case BatteryManager.BATTERY_PLUGGED_AC:
			return " (AC)";
		case BatteryManager.BATTERY_PLUGGED_USB:
			return " (USB)";
		default:
			return " Battery";
		}
	}
}
