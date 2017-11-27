/*****************************************************************************
The Clear BSD License
Copyright (c) 2017 Mark Charlebois
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted (subject to the limitations in the disclaimer
below) provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of DBOpenSource nor the names of its contributors may be
  used to endorse or promote products derived from this software without
  specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY
THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*****************************************************************************/
package com.github.dbopensource.OnBootTestApp;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.os.IBinder;
import android.net.wifi.WifiManager;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import android.content.Context;
import android.util.Log;
import android.provider.Settings;
import android.os.Build;

public class OnBootService extends Service {

    private WifiManager mWifiManager;
    private WifiConfiguration mWifiConfig;
    private static final String TAG = "MC";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);

        mWifiConfig =  new WifiConfiguration();
        mWifiConfig.SSID = "android-1234";
        mWifiConfig.preSharedKey = "password1234";
        mWifiConfig.status = WifiConfiguration.Status.ENABLED;
        mWifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        mWifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        mWifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        mWifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        mWifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        mWifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        mWifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        mWifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

        Log.i(TAG, "OnBootService created");
        createConnection();
    }

    public void createConnection() {

        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }

        Log.i(TAG, "Setting up network, mWifiConfig.SSID: " + mWifiConfig.SSID + ", password: " + mWifiConfig.preSharedKey);

        Boolean success = false;

        if (mWifiManager.isWifiApEnabled()) {
            // AP is already set up
            success = true;
        } else {
            if (mWifiManager.setWifiApEnabled(null, false)) {
            	success = mWifiManager.setWifiApEnabled(mWifiConfig, true);
            }
        }

        if (success) {
            Log.i(TAG, "AP set up");
        }
    }

}
