package com.example.testdemo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class MainActivity extends BaseActivity implements IBMapOnClickListener{
	String tag = getClass().getSimpleName();
	
	MyApplication mApp = MyApplication.getInstance();
	
	MyMapView mMapView = null; //地图  
	
	Drawable marker=null; //标记模式
	
	int zoomLevel = 17; //缩放级别
	
	//设置启用内置的缩放控件  
	MapController mMapController = null;
	
	GeoPoint gleft_top = null; //左上角点
	GeoPoint centerpoint = null; //中心点
	GeoPoint another_point = null; //别的点
	GeoSize mPointGeoSize = null; //自己和别的点的距离
	
	class GeoSize{
	  int width_half; //经纬度宽
	  int height_half;//经纬度宽
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mMapView=(MyMapView)findViewById(R.id.bmapsView);  
		mMapView.setBuiltInZoomControls(true);  
		//设置启用内置的缩放控件  
		mMapController=mMapView.getController();  
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放  
		centerpoint =new GeoPoint((int)(22.61667* 1E6),(int)(114.06667 * 1E6));  //深圳
		another_point =new GeoPoint((int)(23.02* 1E6),(int)(113.45 * 1E6));//东莞
		//取得另外一个点和中心点的距离
		mPointGeoSize  = getGWidthGHeight(centerpoint,another_point);
		//用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)  
		mMapController.setCenter(centerpoint);//设置地图中心点  
		mMapController.setZoom(zoomLevel);//设置地图zoom级别  
		
		mMapView.setIBMapOnClickListener(this);//设置监听
		
		if(marker==null)
		{
			marker= MainActivity.this.getResources().getDrawable(R.drawable.ic_launcher);
		}
		//创建IteminizedOverlay   
		OverlayTest itemOverlay = new OverlayTest(marker,mMapView);
		//将IteminizedOverlay添加到MapView中   

		mMapView.getOverlays().clear();   
		
		mMapView.getOverlays().add(itemOverlay);
		
		//现在所有准备工作已准备好，使用以下方法管理overlay.   
		//添加overlay, 当批量添加Overlay时使用addItem(List<OverlayItem>)效率更高   
		itemOverlay.addItem(new OverlayItem(centerpoint,"item1","item1"));
		itemOverlay.addItem(new OverlayItem(another_point,"item2","item2"));
		mMapView.refresh();
	}
    //得到两点之间的横纵距离
	GeoSize getGWidthGHeight(GeoPoint leftTop, GeoPoint center){
		GeoSize mGeoSize = new GeoSize();
		mGeoSize.height_half = Math.abs(leftTop.getLatitudeE6() - center.getLatitudeE6());
		mGeoSize.width_half = Math.abs(leftTop.getLongitudeE6() - center.getLongitudeE6()); 
		return mGeoSize;
	}
	//是否在屏幕显示区域
	boolean inRange(GeoSize mScreenGeoSize,GeoSize mPointGeoSize){
		if(mPointGeoSize.width_half > mScreenGeoSize.width_half &&
		   mPointGeoSize.height_half > mScreenGeoSize.height_half){
			return false;
		}else{
			return true;
		}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mMapView.onPause();  
//        if(mBMapMan!=null){  
//               mBMapMan.stop();  
//        }  
		super.onPause();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		mMapView.onResume();  
//        if(mBMapMan!=null){  
//                mBMapMan.start();  
//        } 
		super.onResume();
	}

	
	@Override
	public void onMapClick(double x, double y) {
		// TODO Auto-generated method stub
		log(String.format("(%s,%s)",x,y));
	}
	
	//覆盖物
	class OverlayTest extends ItemizedOverlay<OverlayItem> {   
	    //用MapView构造ItemizedOverlay   
	    public OverlayTest(Drawable mark,MapView mapView){   
	            super(mark,mapView);   
	    }   
	    protected boolean onTap(int index) {   
	        //在此处理item点击事件   
	    	showToast("item onTap: "+index);   
	        return true;   
	    }   
        public boolean onTap(GeoPoint pt, MapView mapView){   
                //在此处理MapView的点击事件，当返回 true时   
                super.onTap(pt,mapView);   
                return false;   
        }   
	}   
	void log(String msg){
		Log.d(tag, msg);
	}
	void showToast(String msg){
		Toast.makeText(this, msg, 200).show();
	}
	
	
	boolean runningThread = false;
	//得到屏幕左上角的经纬度
	@Override
	public void onMeasure() {
		// TODO Auto-generated method stub
		log("onMeasure");
		gleft_top = mMapView.getProjection().fromPixels((int)0, (int)0);//左上角经纬度
		if(gleft_top==null){
			return;
		}
		new Thread(){
			public void run() {
				log(""+runningThread);
				synchronized(this){
						if(runningThread){  //确保只有一个线程访问
							return;
						}
						runningThread = true;
						while(true){
							gleft_top = mMapView.getProjection().fromPixels((int)0, (int)0);//左上角经纬度
							//取得中心和上下左右的距离
							GeoSize mScreenGeoSize =  getGWidthGHeight(gleft_top,centerpoint);
							if (inRange(mScreenGeoSize, mPointGeoSize)) { // 屏幕范围内
								log("inRange "+this.currentThread().getId());
								break;
							} else { // 范围外
								log("NotInRange "+this.currentThread().getId());
								--zoomLevel; //缩小地图
								if (zoomLevel < 0) {
									break;
								}
								mMapController.setZoom(zoomLevel);
								mMapView.refresh();
							}
							try {
								sleep(100);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						//再次缩小,才能显示
						if(zoomLevel-1>=0){
							--zoomLevel;
							mMapController.setZoom(zoomLevel);
							mMapView.refresh();
						}
						runningThread = false;
				};
			}
		}.start();
		gleft_top = null;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
	    mMapView.destroy();  
//        if(mApp.getMapManager()!=null){  
//                mBMapMan.destroy();  
//                mBMapMan=null;  
//        }  
		super.onDestroy();
	}
}


