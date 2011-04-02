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
		
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.deviceinfowidget);
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        // Display Mobile Data connection details
		remoteViews.setTextViewText(R.id.deviceInfoWidget_mobile_ipAddressValue, this.getMobileDataInfo(context, connectivityManager));
		
		// Display Wifi connection details
		remoteViews.setTextViewText(R.id.deviceInfoWidget_wifi_ipAddressValue, this.getWifiInfo(context, connectivityManager));

        ComponentName thisComponentName = new ComponentName(context, DeviceInfoWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisComponentName, remoteViews);
		
	}
	
	private String getMobileDataInfo(Context context, ConnectivityManager connectivityManager) {
        NetworkInfo mobileNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		StringBuilder mobileState = new StringBuilder(mobileNetworkInfo.getDetailedState().name());
		if (mobileNetworkInfo.getExtraInfo() != null) {
			mobileState.append(" (")
				.append(mobileNetworkInfo.getExtraInfo())
				.append(")");
		}
		return mobileState.toString();
	}
	
	/**
	 * 
	 * @param context
	 * @param connectivityManager
	 * @return
	 */
	private String getWifiInfo(Context context, ConnectivityManager connectivityManager) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		StringBuilder wifiState = new StringBuilder(wifiNetworkInfo.getDetailedState().name());
		if (wifiInfo != null && wifiInfo.getSSID() != null) {
			int ipAddress = wifiInfo.getIpAddress();
			String ssid = wifiInfo.getSSID();
			wifiState.append(" ")
				.append(NetworkBroadcastReceiver.intToIp(ipAddress))
				.append(" (")
				.append(ssid)
				.append(")");
		}
		return wifiState.toString();
	}
	
	/**
	 * 
	 * @return
	 */
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
