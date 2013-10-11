package com.example.testdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.baidu.mapapi.map.MapView;

public class MyMapView extends MapView{
	
	IBMapOnClickListener  mIBMapOnClickListener = null;
	
	public void setIBMapOnClickListener(IBMapOnClickListener mIBMapOnClickListener) {
		if(mIBMapOnClickListener!=null){
			this.mIBMapOnClickListener = mIBMapOnClickListener;
		}
	}
	public MyMapView(Context arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
	}
	public MyMapView(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	public MyMapView(Context arg0, AttributeSet arg1, int arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if(mIBMapOnClickListener!=null){
			mIBMapOnClickListener.onMapClick(arg0.getX(),arg0.getY());				
		}
		//Log.d("MyMapView", String.format("gp.getLatitudeE6()=%s gp.getLongitudeE6()=%s", ,));
		return super.onTouchEvent(arg0);
	}
	@Override
	protected void onMeasure(int arg0, int arg1) {
		// TODO Auto-generated method stub
		super.onMeasure(arg0, arg1);
		if(mIBMapOnClickListener!=null){
			mIBMapOnClickListener.onMeasure();			
		}
	}
}
