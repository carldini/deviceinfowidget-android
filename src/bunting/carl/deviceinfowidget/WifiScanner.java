package bunting.carl.deviceinfowidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class WifiScanner extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		List<ScanResult> scanResults = wifiManager.getScanResults();
		List<Map<String, String>> accessPoints = new ArrayList<Map<String, String>>();
		if (scanResults != null) {
			for (ScanResult scanResult: scanResults) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("SSID", scanResult.SSID + ": " + WifiScanner.getChannelFromFrequency(scanResult.frequency) + " " + scanResult.capabilities);
				accessPoints.add(map);
			}
		}

		ListAdapter adapter = new SimpleAdapter(this, accessPoints, R.layout.wifi_scanner, new String[] {"SSID"}, new int[] {R.id.text1}); 
		super.setListAdapter(adapter);
	}
	
	private static int getChannelFromFrequency(int frequency) {
		switch (frequency) {
		case 2412: return 1;
		case 2417: return 2;
		case 2422: return 3;
		case 2427: return 4;
		case 2432: return 5;
		case 2437: return 6;
		case 2442: return 7;
		case 2447: return 8;
		case 2452: return 9;
		case 2457: return 10;
		case 2462: return 11;
		case 2467: return 12;
		case 2472: return 13;
		case 2484: return 14;
		default:
			return frequency;
		}
	}
	
}
