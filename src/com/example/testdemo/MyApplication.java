package com.example.testdemo;

import android.app.Application;

import com.baidu.mapapi.BMapManager;

/*
��Ȩ���У���Ȩ����(C)2013���������
�ļ����ƣ�com.example.testdemo.MyApplication.java
ϵͳ��ţ�
ϵͳ���ƣ�testdemo
ģ���ţ�
ģ�����ƣ�
����ĵ���
�������ڣ�2013-10-10 ����3:42:08
�� �ߣ�½����
����ժҪ��
���еĴ�������������Σ���������������������෽������
�ļ�����:
 */
public class MyApplication extends Application{
	private String tag = getClass().getSimpleName();
	private static MyApplication mInstance = null;
	BMapManager mBMapMan = null;
	
	public static MyApplication getInstance(){
		return mInstance;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mInstance = this;
		mBMapMan=new BMapManager(this);  
		mBMapMan.init("4197CC17278DCFB93DB2BA0D95F304D11372F91F", null);  
	}
	public BMapManager getMapManager(){
		return mBMapMan;
	}
}


