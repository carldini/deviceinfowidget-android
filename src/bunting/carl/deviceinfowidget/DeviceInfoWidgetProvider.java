package bunting.carl.deviceinfowidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class DeviceInfoWidgetProvider extends AppWidgetProvider {
	
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}
	
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // To prevent any ANR timeouts, we perform the update in a service
        context.startService(new Intent(context, DeviceInfoUpdateService.class));
        //context.startService(new Intent(context, BatteryBroadcastReceiver.class));
        //context.startService(new Intent(context, NetworkBroadcastReceiver.class));
    }
}
