package com.alobot.dummloginproposal.utils;

import android.content.Context;
import android.net.Uri;

public class File_Util {
    public Uri getRawUri(Context context, int rawId) {
        return Uri.parse("android.resource://" + context.getPackageName() + "/" + rawId);
    }
}
