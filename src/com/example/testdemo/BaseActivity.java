/**    文件描述区域
 *版权信息：
 *文件名称：BaseActivity.java
 *系统编号：
 *系统名称：有木有
 *模块名称：base基类功能实现
 *作       者 ：郑伟坚
 *完成日期：
 *设计文档：不需要界面xml
 *内容摘要：基类Activity,全屏并控制横屏切换
 */

/**   文件修改区域
 * 修改记录1：
 * 修改日期：
 * 修  改  人：
 * 修改内容：
 * 
 */
package com.example.testdemo;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

public class BaseActivity extends Activity {
	//String tag = getClass().getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void onResume() {
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		//log(""+getRequestedOrientation()+" protrait="+ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onResume();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//mApp.startFloatService();
	}
}
