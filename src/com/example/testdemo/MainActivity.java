package com.example.testdemo;

import java.util.ArrayList;
import java.util.List;

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
	
	MyMapView mMapView = null; //��ͼ  
	
	Drawable marker=null; //���ģʽ
	
	int zoomLevel = 17; //���ż���
	
	//�����������õ����ſؼ�  
	MapController mMapController = null;
	
	GeoPoint gleft_top = null; //���Ͻǵ�
	GeoPoint centerpoint = null; //���ĵ�
	List<GeoPoint> point_list = null; //�㼯
	GeoSize mLongestDistance = null; //�Լ��ͱ�ĵ����Զ����
	class GeoSize{
	  int width_half; //��γ�ȿ�
	  int height_half;//��γ�ȿ�
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mMapView=(MyMapView)findViewById(R.id.bmapsView);  
		mMapView.setBuiltInZoomControls(true);  
		mMapController=mMapView.getController();  
		mMapController.setZoom(zoomLevel);//���õ�ͼzoom����  
		
		mMapView.setIBMapOnClickListener(this);//���ü���
		
		if(marker==null)
		{
			marker= MainActivity.this.getResources().getDrawable(R.drawable.ic_launcher);
		}
		//����IteminizedOverlay   
		OverlayTest itemOverlay = new OverlayTest(marker,mMapView);
		//��IteminizedOverlay��ӵ�MapView��   

		mMapView.getOverlays().clear();   
		
		mMapView.getOverlays().add(itemOverlay);
		
		
		point_list = new ArrayList<GeoPoint>(); //���������
	    centerpoint =new GeoPoint((int)(22.61667* 1E6),(int)(114.06667 * 1E6));  //����- ����λ��
	    
	    mMapController.setCenter(centerpoint);//���õ�ͼ���ĵ�
	    
		GeoPoint dongguan =new GeoPoint((int)(23.02* 1E6),(int)(113.45 * 1E6));//��ݸ
		GeoPoint heyuan =new GeoPoint((int)( 23.43* 1E6),(int)(114.41 * 1E6));//��Դ 
		GeoPoint qingyuan =new GeoPoint((int)( 23.42* 1E6),(int)(113.01 * 1E6));//��Զ
		
		point_list.add(centerpoint);
		point_list.add(dongguan);
		point_list.add(heyuan);
		point_list.add(qingyuan);
		mLongestDistance = getLongestDistance(point_list); 
		
		for(GeoPoint point : point_list){ //����������뵽��ͼ
			itemOverlay.addItem(new OverlayItem(point,"item2","item2"));
		}
		mMapView.refresh();
	}
	//Ѱ����Զ�������
	GeoSize getLongestDistance(List<GeoPoint> another_point_list){
		int width;
		int height;
		GeoSize mGeoSizeLast = getGWidthGHeight(another_point_list.get(0),centerpoint); //��һ��������ĵ�ľ���
		width = mGeoSizeLast.width_half;
		height = mGeoSizeLast.height_half;
		
		for(int i=1;i<another_point_list.size();i++){
			GeoSize temp = getGWidthGHeight(another_point_list.get(i),centerpoint);
			if(temp.width_half > width){ //Ѱ����������
				width = temp.width_half;
			}
			if(temp.height_half > height){ //Ѱ����������
				height = temp.height_half;
			}
		}
		GeoSize mGeoSize = new GeoSize();
		mGeoSize.width_half = width;
		mGeoSize.height_half = height;
		log("getLongestDistance width:"+width+" height:"+height);
		return mGeoSize;
	}
    //�õ�����֮��ĺ��ݾ���
	GeoSize getGWidthGHeight(GeoPoint leftTop, GeoPoint center){
		GeoSize mGeoSize = new GeoSize();
		mGeoSize.height_half = Math.abs(leftTop.getLatitudeE6() - center.getLatitudeE6());
		mGeoSize.width_half = Math.abs(leftTop.getLongitudeE6() - center.getLongitudeE6()); 
		return mGeoSize;
	}
	//�Ƿ�����Ļ��ʾ����(��Ļ��С,��С)
	boolean inRange(GeoSize mScreenGeoSize,GeoSize mPointGeoSize){
		log(String.format("inRange(%s,%s)", mScreenGeoSize.width_half,mScreenGeoSize.height_half));
		log(String.format("inRange(%s,%s)", mPointGeoSize.width_half,mPointGeoSize.height_half));
		log("");
		if(mScreenGeoSize.width_half > mPointGeoSize.width_half &&
			mScreenGeoSize.height_half > mPointGeoSize.height_half){
			return true;
		}else{
			return false;
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
	
	//������
	class OverlayTest extends ItemizedOverlay<OverlayItem> {   
	    //��MapView����ItemizedOverlay   
	    public OverlayTest(Drawable mark,MapView mapView){   
	            super(mark,mapView);   
	    }   
	    protected boolean onTap(int index) {   
	        //�ڴ˴���item����¼�   
	    	showToast("item onTap: "+index);   
	        return true;   
	    }   
        public boolean onTap(GeoPoint pt, MapView mapView){   
                //�ڴ˴���MapView�ĵ���¼��������� trueʱ   
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
	//�õ���Ļ���Ͻǵľ�γ��
	@Override
	public void onMeasure() {
		// TODO Auto-generated method stub
		log("onMeasure");
		gleft_top = mMapView.getProjection().fromPixels((int)0, (int)0);//���ϽǾ�γ��
		if(gleft_top==null){
			return;
		}
		new Thread(){
			public void run() {
				log(""+runningThread);
				synchronized(this){
						if(runningThread){  //ȷ��ֻ��һ���̷߳���
							return;
						}
						runningThread = true;
						while(true){
							gleft_top = mMapView.getProjection().fromPixels((int)0, (int)0);//���ϽǾ�γ��
							GeoSize mScreenGeoSize =  getGWidthGHeight(gleft_top,centerpoint);//ȡ�����ĺ��������ҵľ���
							if (inRange(mScreenGeoSize, mLongestDistance)) { // ��Ļ��Χ��,ok
								log("inRange threadId:"+this.currentThread().getId());
								break;
							} else { // ��Χ��
								log("NotInRange threadId:"+this.currentThread().getId());
								--zoomLevel; //��С��ͼ:ֱ��λ����Ļ��Χ��
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
						//�ٴ���С,������ʾ
						/*if(zoomLevel-1>=0){
							--zoomLevel;
							mMapController.setZoom(zoomLevel);
							mMapView.refresh();
						}*/
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


