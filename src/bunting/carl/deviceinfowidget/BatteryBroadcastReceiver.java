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

	        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.deviceinfowidget);
	        updateViews.setTextViewText(R.id.deviceInfoWidget_batteryChargeValue, batteryLevel + " " + this.getBatteryStatus(intent));
	        
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
	
	private String getBatteryStatus(Intent intent) {
		
		String batteryVoltage = " " + String.valueOf((float) intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0) / 1000) + "V";
		String batteryTemperature = " " + String.valueOf((float) intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10) + "\u2103";
		String batteryTechnology = " " + intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
		String batteryPlugged = this.getBatteryPlugged(intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0));
		int batteryStatus = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);

		switch (batteryStatus) {
			case BatteryManager.BATTERY_STATUS_CHARGING:
				return "Charging" + batteryPlugged + batteryTemperature;
			case BatteryManager.BATTERY_STATUS_DISCHARGING:
				return "Discharging" + batteryTechnology + batteryVoltage;
			case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
				return "Not charging";
			case BatteryManager.BATTERY_STATUS_FULL:
				return "Full" + batteryPlugged + batteryTemperature + batteryVoltage;
			default:
				return "Unknown";
		}
	}
	
	private String getBatteryHealth(Intent intent) {
		int batteryHealth = intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN);
		
		switch (batteryHealth) {
			case BatteryManager.BATTERY_HEALTH_GOOD:
				return "Good";
			case BatteryManager.BATTERY_HEALTH_OVERHEAT:
				return "Overheat";
			case BatteryManager.BATTERY_HEALTH_DEAD:
				return "Dead";
			case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
				return "Over Voltage";
			case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
				return "Unspecified Failure";
			default:
				return "Unknown";
		}
	}
}
