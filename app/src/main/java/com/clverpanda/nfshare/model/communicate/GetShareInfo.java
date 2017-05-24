package com.clverpanda.nfshare.model.communicate;

import android.content.Context;

import com.clverpanda.nfshare.util.DeviceInfoGetter;

/**
 * Created by clverpanda on 2017/5/24 0024.
 * It's the file for NFShare.
 */

public class GetShareInfo
{
    private int pin_code;
    private String installation;

    public GetShareInfo() {
    }

    public GetShareInfo(int pin_code, Context context)
    {
        this.installation = DeviceInfoGetter.getInstance(context).getTencentInstallationId();
        this.pin_code = pin_code;
    }

    public String getInstallation() {
        return installation;
    }

    public void setInstallation(String installation) {
        this.installation = installation;
    }

    public int getPin_code() {
        return pin_code;
    }

    public void setPin_code(int pin_code) {
        this.pin_code = pin_code;
    }
}
