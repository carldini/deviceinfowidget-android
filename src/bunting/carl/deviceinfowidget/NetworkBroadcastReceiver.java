package bunting.carl.deviceinfowidget;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.RemoteViews;
import bunting.carl.deviceinfowidget.R;

public class NetworkBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.deviceinfowidget);
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        
		NetworkInfo mobileNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		String mobileState = mobileNetworkInfo.getDetailedState().toString() + " (" + mobileNetworkInfo.getExtraInfo() + ")";
		updateViews.setTextViewText(R.id.deviceInfoWidget_mobile_ipAddressValue, mobileState);
		
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		if (wifiInfo != null) {
			int ipAddress = wifiInfo.getIpAddress();
			String ssid = wifiInfo.getSSID();
			updateViews.setTextViewText(R.id.deviceInfoWidget_wifi_ipAddressValue, "" + NetworkBroadcastReceiver.intToIp(ipAddress) + " (" + ssid + ")");
		} else {
			updateViews.setTextViewText(R.id.deviceInfoWidget_wifi_ipAddressValue, "---");
		}
		

        ComponentName myComponentName = new ComponentName(context, DeviceInfoWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myComponentName, updateViews);
		
	}
	
	public String getLocalIpAddress() {
	    try {
	    	Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
	        while (networkInterfaces.hasMoreElements()) {
	            NetworkInterface networkInterface = networkInterfaces.nextElement();
	            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
	            while (inetAddresses.hasMoreElements()) {
	                InetAddress inetAddress = inetAddresses.nextElement();
	                if (!inetAddress.isLoopbackAddress()) {
	                    return networkInterface.getDisplayName() + " " + inetAddress.getHostAddress().toString();
	                }
	            }
	        }
	    } catch (SocketException ex) {
	        Log.e(this.getClass().getName(), ex.toString());
	    }
	    return null;
	}
	
    /**
     * http://teneo.wordpress.com/2008/12/23/java-ip-address-to-integer-and-back/
     */
	public static String intToIp(int i) {
        return (i & 0xFF) + "." +
        	((i >>  8 ) & 0xFF) + "." + 
            ((i >> 16 ) & 0xFF) + "." +
            ((i >> 24 ) & 0xFF);
    }

}
