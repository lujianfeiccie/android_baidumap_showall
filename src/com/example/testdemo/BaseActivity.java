/**    �ļ���������
 *��Ȩ��Ϣ��
 *�ļ����ƣ�BaseActivity.java
 *ϵͳ��ţ�
 *ϵͳ���ƣ���ľ��
 *ģ�����ƣ�base���๦��ʵ��
 *��       �� ��֣ΰ��
 *������ڣ�
 *����ĵ�������Ҫ����xml
 *����ժҪ������Activity,ȫ�������ƺ����л�
 */

/**   �ļ��޸�����
 * �޸ļ�¼1��
 * �޸����ڣ�
 * ��  ��  �ˣ�
 * �޸����ݣ�
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
