package com.yuri.tam.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * 开机广播:开机启动应用
 *
 * @author 谭忠扬-YuriTam
 * @date 2015年11月23日
 */
public class BootReceiver extends BroadcastReceiver {
	private static final String ACTION_AUDIO = android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY;

	private boolean mIsFirst = true;

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (TextUtils.isEmpty(action)) return;
		//开机广播，开机启动应用服务
		if(action.equals(Intent.ACTION_BOOT_COMPLETED) || action.equals(ACTION_AUDIO)){

		}
	}

}
