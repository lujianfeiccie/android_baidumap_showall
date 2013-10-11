package com.example.testdemo;

import android.app.Application;

import com.baidu.mapapi.BMapManager;

/*
版权所有：版权所有(C)2013，固派软件
文件名称：com.example.testdemo.MyApplication.java
系统编号：
系统名称：testdemo
模块编号：
模块名称：
设计文档：
创建日期：2013-10-10 上午3:42:08
作 者：陆键霏
内容摘要：
类中的代码包括三个区段：类变量区、类属性区、类方法区。
文件调用:
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


