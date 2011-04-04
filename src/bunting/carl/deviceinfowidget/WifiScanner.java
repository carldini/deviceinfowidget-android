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
				map.put("SSID", scanResult.SSID + ": " + scanResult.frequency + " " + scanResult.capabilities);
				accessPoints.add(map);
			}
		}

		ListAdapter adapter = new SimpleAdapter(this, accessPoints, R.layout.wifi_scanner, new String[] {"SSID"}, new int[] {R.id.text1}); 
		
		setListAdapter(adapter);
	}
	
}
