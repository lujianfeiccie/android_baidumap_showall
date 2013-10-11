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
	
	MyMapView mMapView = null; //��ͼ  
	
	Drawable marker=null; //���ģʽ
	
	int zoomLevel = 17; //���ż���
	
	//�����������õ����ſؼ�  
	MapController mMapController = null;
	
	GeoPoint gleft_top = null; //���Ͻǵ�
	GeoPoint centerpoint = null; //���ĵ�
	GeoPoint another_point = null; //��ĵ�
	GeoSize mPointGeoSize = null; //�Լ��ͱ�ĵ�ľ���
	
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
		//�����������õ����ſؼ�  
		mMapController=mMapView.getController();  
		// �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����  
		centerpoint =new GeoPoint((int)(22.61667* 1E6),(int)(114.06667 * 1E6));  //����
		another_point =new GeoPoint((int)(23.02* 1E6),(int)(113.45 * 1E6));//��ݸ
		//ȡ������һ��������ĵ�ľ���
		mPointGeoSize  = getGWidthGHeight(centerpoint,another_point);
		//�ø����ľ�γ�ȹ���һ��GeoPoint����λ��΢�� (�� * 1E6)  
		mMapController.setCenter(centerpoint);//���õ�ͼ���ĵ�  
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
		
		//��������׼��������׼���ã�ʹ�����·�������overlay.   
		//���overlay, ���������Overlayʱʹ��addItem(List<OverlayItem>)Ч�ʸ���   
		itemOverlay.addItem(new OverlayItem(centerpoint,"item1","item1"));
		itemOverlay.addItem(new OverlayItem(another_point,"item2","item2"));
		mMapView.refresh();
	}
    //�õ�����֮��ĺ��ݾ���
	GeoSize getGWidthGHeight(GeoPoint leftTop, GeoPoint center){
		GeoSize mGeoSize = new GeoSize();
		mGeoSize.height_half = Math.abs(leftTop.getLatitudeE6() - center.getLatitudeE6());
		mGeoSize.width_half = Math.abs(leftTop.getLongitudeE6() - center.getLongitudeE6()); 
		return mGeoSize;
	}
	//�Ƿ�����Ļ��ʾ����
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
							//ȡ�����ĺ��������ҵľ���
							GeoSize mScreenGeoSize =  getGWidthGHeight(gleft_top,centerpoint);
							if (inRange(mScreenGeoSize, mPointGeoSize)) { // ��Ļ��Χ��
								log("inRange "+this.currentThread().getId());
								break;
							} else { // ��Χ��
								log("NotInRange "+this.currentThread().getId());
								--zoomLevel; //��С��ͼ
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


