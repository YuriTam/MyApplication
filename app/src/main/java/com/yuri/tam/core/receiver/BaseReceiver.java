package com.yuri.tam.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * 应用级广播监听器
 *
 * @author 谭忠扬-YuriTam
 * @date 2018年6月5日
 */
public class BaseReceiver extends BroadcastReceiver {
	private static final String TAG = BaseReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		//获取广播常量
		String action = intent.getAction();
		if (!TextUtils.isEmpty(action)){

		}
	}

}
