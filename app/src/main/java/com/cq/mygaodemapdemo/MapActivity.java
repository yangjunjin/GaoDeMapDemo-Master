package com.cq.mygaodemapdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;

public class MapActivity extends Activity implements AMap.OnMarkerDragListener, AMap.OnMapLoadedListener, AMap.OnMarkerClickListener {
    private LatLng latlng = new LatLng(39.926516, 116.389366);
    private AMapLocationClient locationClientSingle = null;
    private AMap aMap;
    private MapView mapView;
    private RelativeLayout root;

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.map);
        root = findViewById(R.id.root);
        // 此方法必须重写
        mapView.onCreate(savedInstanceState);
        init();
    }

    /**
     * 初始化地图
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    private void setUpMap() {
        aMap.setOnMarkerDragListener(this);
        //设置amap加载成功事件监听器
        aMap.setOnMapLoadedListener(this);
        //Marker点击响应
        aMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onMapLoaded() {
        //移动到具体的位置
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.925516,
                116.395366), 15));
        startSingleLocation();
    }

    /**
     * 启动单次客户端定位
     */
    public void startSingleLocation() {
        if (null == locationClientSingle) {
            locationClientSingle = new AMapLocationClient(this.getApplicationContext());
        }

        AMapLocationClientOption locationClientOption = new AMapLocationClientOption();
        //使用单次定位
        locationClientOption.setOnceLocation(true);
        // 地址信息
        locationClientOption.setNeedAddress(true);
        locationClientOption.setLocationCacheEnable(false);
        locationClientSingle.setLocationOption(locationClientOption);
        locationClientSingle.setLocationListener(locationSingleListener);
        locationClientSingle.startLocation();
    }

    /**
     * 单次客户端的定位监听
     */
    AMapLocationListener locationSingleListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null == location) {
                Toast.makeText(MapActivity.this, "定位失败!", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("locationSingleListener", "====" + location.getCity() + "," + location.getAddress());
                //获取经纬度对象
                latlng = new LatLng(location.getLatitude(),
                        location.getLongitude());
                //移动到具体的位置
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
                addGrowMarker(latlng);
            }
        }
    };

    /**
     * 添加带生长效果marker
     */
    private void addGrowMarker(LatLng latLng) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng).snippet("yangjunjin");
        if (true) {
            Bitmap bitmap = createBitmap(root);
            options.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        } else {
            options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.l_location)));
        }
        Marker marker = aMap.addMarker(options);
        Animation markerAnimation = new ScaleAnimation(0, 1, 0, 1); //初始化生长效果动画
        markerAnimation.setDuration(1000);  //设置动画时间 单位毫秒
        marker.setAnimation(markerAnimation);
        marker.startAnimation();
    }

    /**
     * view转成bitmap
     *
     * @param
     * @return
     */
    private Bitmap createBitmap(RelativeLayout view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        c.drawColor(Color.WHITE);
        view.draw(c);
        return bitmap;
    }

    /**
     * 在marker拖动过程中回调此方法, 这个marker的位置可以通过getPosition()方法返回。
     * 这个位置可能与拖动的之前的marker位置不一样。
     * marker 被拖动的marker对象。
     */
    @Override
    public void onMarkerDrag(Marker marker) {
    }

    /**
     * 在marker拖动完成后回调此方法, 这个marker的位置可以通过getPosition()方法返回。
     * 这个位置可能与拖动的之前的marker位置不一样。
     * marker 被拖动的marker对象。
     */
    @Override
    public void onMarkerDragEnd(Marker arg0) {

    }

    /**
     * 当marker开始被拖动时回调此方法, 这个marker的位置可以通过getPosition()方法返回。
     * 这个位置可能与拖动的之前的marker位置不一样。
     * marker 被拖动的marker对象。
     */
    @Override
    public void onMarkerDragStart(Marker arg0) {
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (aMap != null) {
            jumpPoint(marker);
        }
        Toast.makeText(MapActivity.this, "您点击了Marker" + marker.getSnippet(), Toast.LENGTH_LONG).show();
        return true;
    }

    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        final LatLng markerLatlng = marker.getPosition();
        Point markerPoint = proj.toScreenLocation(markerLatlng);
        markerPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * markerLatlng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * markerLatlng.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != locationClientSingle) {
            locationClientSingle.onDestroy();
            locationClientSingle = null;
        }
    }
}
