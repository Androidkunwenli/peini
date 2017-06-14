package com.jsz.peini.ui.fragment;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.jsz.peini.PeiNiApp;
import com.jsz.peini.R;
import com.jsz.peini.threadpool.Priority;
import com.jsz.peini.threadpool.PriorityExecutor;
import com.jsz.peini.threadpool.PriorityRunnable;
import com.jsz.peini.base.BaseFragment;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.eventbus.FilterReturnBean;
import com.jsz.peini.model.eventbus.LoodingData;
import com.jsz.peini.model.eventbus.TaskReleaseRefreshTaskAndMapList;
import com.jsz.peini.model.eventbus.UnreadHuanXinMsgCountBean;
import com.jsz.peini.model.eventbus.ReceivedMessageBean;
import com.jsz.peini.model.map.BaiduMapBean;
import com.jsz.peini.model.search.LatLngBean;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.model.setting.UserSmsCntBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.presenter.map.BaiduMapService;
import com.jsz.peini.presenter.setting.SettingService;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.presenter.task.TaskService;
import com.jsz.peini.ui.activity.login.CompleteUserInfoActivity;
import com.jsz.peini.ui.activity.news.MapNewsActivity;
import com.jsz.peini.ui.activity.square.OfficialActivity;
import com.jsz.peini.ui.activity.task.MoreTaskActivity;
import com.jsz.peini.ui.activity.task.TaskActivity;
import com.jsz.peini.ui.activity.task.TaskDetailActivity;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;
import com.jsz.peini.utils.UiUtils;
import com.jsz.peini.utils.time.TimeUtils;
import com.jsz.peini.widget.ImageHendDialogFragment;
import com.jsz.peini.widget.UseCameraActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


/**
 * Created by kunwe on 2016/11/25.
 * 首界面
 */

public class MapFragment extends BaseFragment implements MKOfflineMapListener, ImageHendDialogFragment.SelectPhotoMonitorDialogListener {
    private MapFragment mFragment = this;
    @InjectView(R.id.point_number)
    TextView mPointNumber; //消息的数量
    @InjectView(R.id.map_publish)
    Button map_Button; //发布按钮
    private TextureMapView mapView;
    private BaiduMap mBaiduMap;

    private LocationClient mLocationClient;
    private double latitude;//精度
    private double longitude;//维度
    //是否已经定位了
    boolean isLocation = true;

    /**
     * 初始化比例尺大小
     */
    private int mapLevel;
    /**
     * ==================
     */
    private String mTaskCity = "64";

    private String mSort = "1";
    private String mOtherSex = "";
    private String mOtherLowAge = "";
    private String mOtherHignAge = "";
    private String mOtherLowheight = "";
    private String mOtherHignheight = "";
    private String mIsVideo = "";
    private String mIsIdcard = "";
    private String mSellerType = "";
    private int mapLevelNext; //比例尺
    public Marker mMarker;
    public Bundle mBundle;
    private List<BaiduMapBean.TaskMapListBean> mTaskMapList = new ArrayList<>();
    private BaiduMapOptions mBaiduMapOptions = new BaiduMapOptions();
    private BDLocationListener myListener = new MyLocationListener();
    /**
     * 当前定位的模式
     */
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
    public Intent mIntent;
    private boolean isnewlatLng = false;
    private double latLngBeanLatitude;
    private double latLngBeanLongitude;
    /**
     * 网络请求借口
     */
    boolean isMaplistData = true;

    /*离线地图*/
    MKOfflineMap mMKOfflineMap;

    /**
     * 需要地图首页展示的数据
     */
    private BaiduMapBean.TaskMapListBean.TaskObjectBean mTaskObjectBean;

    private int mChatMessageCount = 0;
    private int mSystemMessageCount = 0;
    private ImageHendDialogFragment mDialogFragment;
    private Uri mCroppedImageUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(PeiNiApp.context);
        //注册事件
        EventBus.getDefault().register(this);
    }

    @Override
    public View initViews() {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        View view = UiUtils.inflate(mActivity, R.layout.fragment_map);
        // 地图初始化
        mapView = (TextureMapView) view.findViewById(R.id.bmapView);
        mBaiduMap = mapView.getMap();
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        /*离线地图*/
        mMKOfflineMap = new MKOfflineMap();
        mMKOfflineMap.init(this);
        //地图显示
        new Thread(new Runnable() {
            @Override
            public void run() {
                offlineMap();
                showCity();
            }
        }).start();

        //隐藏百度地图的logo
        View child = mapView.getChildAt(1);
        if (child != null &&
                (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.GONE);
        }
        UiSettings uiSettings = mBaiduMap.getUiSettings();
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setOverlookingGesturesEnabled(false);
        uiSettings.setCompassEnabled(false);
//        uiSettings.setZoomGesturesEnabled(false);
//        uiSettings.setScrollGesturesEnabled(false);
        // 设置是否显示缩放控件
        mapView.showZoomControls(false);
        mapView.showScaleControl(false);
        //地图模式
        mBaiduMapOptions.mapType(BaiduMap.MAP_TYPE_NORMAL);
//        //设置缩放层级
//        mBaiduMap.setMaxAndMinZoomLevel(21, 10);
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //定位初始化
        mLocationClient = new LocationClient(mActivity.getApplicationContext());
        //注册监听函数
        LocationClientOption option = new LocationClientOption();//打开GPS
        option.setOpenGps(true);
        //设置坐标类型
        option.setCoorType("bd09ll");
        //设置发起定位请求的间隔时间为5000ms
        option.setScanSpan(5000);
        //设置定位参数
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        //MyLocationData.Builder定位数据建造器
        //添加marker点击事件的监听
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //从marker中获取info信息
                mBundle = marker.getExtraInfo();
                String id = mBundle.getString("id");
                String sum = mBundle.getString("sum");
                String idStr = mBundle.getString("idStr");
                LogUtil.d("点击事件获取的信息---" + "id" + id + "sum" + sum + "--" + TimeUtils.getCurrentTime());
                if ("36524".equals(id)) {
                    return true;
                }
                if (!"1".equals(sum)) {
                    /*进入多级任务列表*/
                    mIntent = new Intent(mActivity, MoreTaskActivity.class);
                    mIntent.putExtra("idStr", idStr);
                    startActivity(mIntent);
                } else {
                    /*进入任务详情界面*/
                    mIntent = new Intent(mActivity, TaskDetailActivity.class);
                    mIntent.putExtra(Conversion.ID, id);
                    startActivity(mIntent);
                }
                return true;
            }
        });
        /**百度地图滑动的监听*/
        isMapMove();
    }

    //    isMaplistData = false;

    private void isMapMove() {
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                LogUtil.d("百度地图在移动在移动了2");
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                isMaplistData = false;
                LatLng target = mBaiduMap.getMapStatus().target;
                mapLevel = mapView.getMapLevel();
                DecimalFormat df = new DecimalFormat("######0.000000");
                String DecimalFormatLatitude = df.format(target.latitude);
                String DecimalFormatLongitude = df.format(target.longitude);
                double latitudeDoubleNext = Double.parseDouble(DecimalFormatLatitude);
                double longitudeDoubleNext = Double.parseDouble(DecimalFormatLongitude);
                LogUtil.i("截取后的字符串", latitudeDoubleNext + "===" + longitudeDoubleNext + "====" + mapLevel);
                latitude = latLngBeanLatitude;
                longitude = latLngBeanLongitude;
                initNetWork(latitudeDoubleNext + "", longitudeDoubleNext + "",
                        mapLevel, mSort, mOtherSex, mOtherLowAge, mOtherHignAge,
                        mOtherLowheight, mOtherHignheight, mIsVideo, mIsIdcard,
                        mSellerType, mTaskCity);
            }
        });
    }

    public void refreshLocation() {
        isLocation = true;
        if (mLocationClient != null) {
            mLocationClient.start();
        }
    }

    /*显示默认城市*/
    private void showCity() {
        double xPointDouble = Double.parseDouble(SpUtils.getXpoint(mActivity));
        double yPointDouble = Double.parseDouble(SpUtils.getYpoint(mActivity));
        LatLng p = new LatLng(xPointDouble, yPointDouble);
        MapStatus mMapStatus = new MapStatus.Builder().target(p).zoom(15)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                .newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    /**
     * 离线地图
     */
    private void offlineMap() {
        boolean start = mMKOfflineMap.start(150);
        LogUtil.d("石家庄地图下载---" + start);
    }

    private void initNetWork(
            final String xpoint,
            final String ypoint,
            final int mapLevelNext,
            final String sort,
            final String otherSex,
            final String otherLowAge,
            final String otherHignAge,
            final String otherLowheight,
            final String otherHignheight,
            final String isVideo,
            final String isIdcard,
            final String sellerType,
            final String taskCity) {
        ExecutorService executorService = new PriorityExecutor(5, false);
        PriorityRunnable priorityRunnable = new PriorityRunnable(Priority.NORMAL, new Runnable() {
            @Override
            public void run() {
                getMapList(xpoint, ypoint, mapLevelNext, sort, otherSex, otherLowAge, otherHignAge, otherLowheight, otherHignheight, isVideo, isIdcard, sellerType, taskCity);
            }
        });
        executorService.execute(priorityRunnable);
    }

    private void getMapList(final String xpoint, final String ypoint, int mapLevelNext, String sort, String otherSex, String otherLowAge, String otherHignAge, String otherLowheight, String otherHignheight, String isVideo, String isIdcard, String sellerType, String taskCity) {
        RetrofitUtil.createService(BaiduMapService.class).getTaskMapList(
                xpoint + "",
                ypoint + "",
                mapLevelNext,
                sort + "",
                otherSex + "",
                otherLowAge + "",
                otherHignAge + "",
                otherLowheight + "",
                otherHignheight + "",
                isVideo + "",
                isIdcard + "",
                sellerType + "",
                taskCity + "")
                .enqueue(new Callback<BaiduMapBean>() {
                    @Override
                    public void onResponse(Call<BaiduMapBean> call, Response<BaiduMapBean> response) {
                        if (response.isSuccessful()) {
                            BaiduMapBean body = response.body();
                            if (body.getResultCode() == 1) {
                                mTaskMapList.clear();
                                mTaskMapList.addAll(body.getTaskMapList());
                                if (isMaplistData) {
                                    setLocation(xpoint, ypoint, body);
                                }
                                addOverlay();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaiduMapBean> call, Throwable t) {
                    }
                });
    }

    private void setLocation(String xpoint, String ypoint, BaiduMapBean body) {
        LatLng p = new LatLng(Double.parseDouble(xpoint), Double.parseDouble(ypoint));
        int distance = body.getDistance();
        LogUtil.d("返回的比例尺=========" + distance);
        MapStatus mMapStatus = new MapStatus.Builder().target(p).zoom(getMapLevel(distance))
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                .newMapStatus(mMapStatus);
        if (null != mBaiduMap) {
            mBaiduMap.setMapStatus(mMapStatusUpdate);
        }
    }

    /**
     * 实时定位 监听位置信息 填充数据
     */
    private void addOverlay() {
        //清空地图
        mBaiduMap.clear();
        for (final BaiduMapBean.TaskMapListBean taskMapListBeen : mTaskMapList) {
            final List<BaiduMapBean.TaskMapListBean.TaskObjectBean> taskObjectBeen = taskMapListBeen.getTaskObject();
            String sum = taskMapListBeen.getSum();
            mTaskObjectBean = taskObjectBeen.get(0);
            if (taskObjectBeen.size() > 0 && mTaskObjectBean != null) {
                //创建布局
                final View linearLayout = UiUtils.inflate(mActivity, R.layout.point);
                //寻找布局的id 显示的数量
                TextView mMapMarkerViewViewById = (TextView) linearLayout.findViewById(R.id.point_number);
                //显示的头像
                final CircleImageView mPointIcronin = (CircleImageView) linearLayout.findViewById(R.id.point_icronin);
                final ImageView mPointSports = (ImageView) linearLayout.findViewById(R.id.point_sports);
                //展示的皇冠
                ImageView imageView = (ImageView) linearLayout.findViewById(R.id.point_grade);

                if ("1".equals(sum)) {
                    //填充数量
                    mMapMarkerViewViewById.setVisibility(View.GONE);
                } else {
                    //填充数量
                    mMapMarkerViewViewById.setText(sum);
                    mMapMarkerViewViewById.setVisibility(View.VISIBLE);
                }
                int sellerType = taskMapListBeen.getTaskObject().get(0).getSellerType();
                switch (sellerType) {
                    case 2:
                        mPointSports.setImageResource(R.drawable.ms);
                        break;
                    case 3:
                        mPointSports.setImageResource(R.drawable.cg);
                        break;
                    case 4:
                        mPointSports.setImageResource(R.drawable.dy);
                        break;
                    case 5:
                        mPointSports.setImageResource(R.drawable.yd);
                        break;
                    case 6:
                        mPointSports.setImageResource(R.drawable.xx);
                        break;
                    case 7:
                        mPointSports.setImageResource(R.drawable.jd);
                        break;
                    case 8:
                        mPointSports.setImageResource(R.drawable.lr);
                        break;
                    case 9:
                        mPointSports.setImageResource(R.drawable.sh);
                        break;
                    case 10:
                        mPointSports.setImageResource(R.drawable.qt);
                        break;
                    default:
                        mPointSports.setImageResource(R.drawable.ms);
                        break;
                }
                String rankType = taskMapListBeen.getRankType();
                switch (rankType) {
                    case "1":
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setImageResource(R.drawable.gold);
                        break;
                    case "2":
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setImageResource(R.drawable.buy);
                        break;
                    case "3":
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setImageResource(R.drawable.integrity);
                        break;
                    default:
                        imageView.setVisibility(View.GONE);
                        break;
                }
                //填充头像
                String url = IpConfig.HttpPic + mTaskObjectBean.getImageHead();
                final String sex = mTaskObjectBean.getSex();

                Glide.with(mActivity.getApplicationContext())
                        .load(url)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                mPointIcronin.setImageBitmap(bitmap);
                                setOnClickImage(taskMapListBeen, taskObjectBeen, linearLayout);
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                mPointIcronin.setImageResource("1".equals(sex) ? R.mipmap.ic_nan : R.mipmap.ic_nv);
                                setOnClickImage(taskMapListBeen, taskObjectBeen, linearLayout);
                            }
                        });
            }
        }

        /**设置搜索地图位置定位*/
        if (isnewlatLng) {
            // 定义marker坐标点
            LatLng point = new LatLng(latLngBeanLatitude, latLngBeanLongitude);
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.dingwei);
            // 构建markerOption，用于在地图上添加marker
            OverlayOptions options = new MarkerOptions()//
                    .position(point)// 设置marker的位置
                    .icon(bitmapDescriptor)// 设置marker的图标
                    .zIndex(9)// 設置marker的所在層級
                    .draggable(false);// 设置手势拖拽
            // 在地图上添加marker，并显示
            mMarker = (Marker) mBaiduMap.addOverlay(options);
            //info必须实现序列化接口
            mBundle = new Bundle();
            mBundle.putInt("id", 36524);
            mBundle.putInt("sum", 0);
            mBundle.putString("idStr", "");
            mMarker.setExtraInfo(mBundle);
        }
    }

    /*地图显示与点击事件*/
    private void setOnClickImage(BaiduMapBean.TaskMapListBean taskMapListBeen, List<BaiduMapBean.TaskMapListBean.TaskObjectBean> taskObjectBeen, View viewBitmap) {
        BaiduMapBean.TaskMapListBean.TaskObjectBean taskObjectBean = taskObjectBeen.get(0);
        String xpoint = taskObjectBean.getXpoint();
        String ypoint = taskObjectBean.getYpoint();
        if (TextUtils.isEmpty(xpoint) || TextUtils.isEmpty(ypoint)) {
            return;
        }
        LatLng latLng = new LatLng(Double.parseDouble(xpoint), Double.parseDouble(ypoint));
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(viewBitmap);
        // 构建markerOption，用于在地图上添加marker
        OverlayOptions options = new MarkerOptions()//
                .position(latLng)// 设置marker的位置
                .icon(bitmapDescriptor)// 设置marker的图标
                .draggable(false);// 设置手势拖拽
        //   .zIndex(9)// 設置marker的所在層級
        // 在地图上添加marker，并显示
        mMarker = (Marker) mBaiduMap.addOverlay(options);
        //info必须实现序列化接口
        mBundle = new Bundle();
        mBundle.putString("id", taskMapListBeen.getId());
        mBundle.putString("sum", taskMapListBeen.getSum());
        mBundle.putString("idStr", taskMapListBeen.getIdStr());
        mMarker.setExtraInfo(mBundle);
    }

    /*百度地图离线地图*/
    @Override
    public void onGetOfflineMapState(int i, int i1) {

    }

    /**
     * 百度定位的监听结果
     */
    class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapview 销毁后不在处理新接收的位置
            if (location == null || mBaiduMap == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(0)
                    .direction(0)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
//            // 设置自定义图标
            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                    .fromResource(R.drawable.map_icon);
            MyLocationConfiguration config = new MyLocationConfiguration(
                    mCurrentMode, true, mCurrentMarker);
            mBaiduMap.setMyLocationConfigeration(config);
            //设置定位数据
            mBaiduMap.setMyLocationData(locData);
            int locCode = location.getLocType();
            if (locCode == 161 || locCode == 61) {  //定位成功
                //获取经纬度
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                SpUtils.put(mActivity, "xpoint", latitude);
                SpUtils.put(mActivity, "ypoint", longitude);
            } else {
                latitude = Double.parseDouble(SpUtils.getXpoint(mActivity));
                longitude = Double.parseDouble(SpUtils.getYpoint(mActivity));
            }

            LogUtil.d("经度 = " + latitude + "  纬度" + longitude);

            if (isLocation) {
                initNetWork(String.valueOf(latitude), String.valueOf(longitude),
                        mapLevelNext, mSort, mOtherSex, mOtherLowAge, mOtherHignAge,
                        mOtherLowheight, mOtherHignheight, mIsVideo, mIsIdcard,
                        mSellerType, mTaskCity);
                isLocation = false;
            }
        }
    }

    private int easyGetMapLevel(int distance) {
        int[] distanceDivide = {
                5,
                10,
                20,
                50,
                100,
                200,
                500,
                1000,
                2000,
                5000,
                10000,
                20000,
                50000,
                100000,
                200000,
                500000,
                1000000,
                2000000,
                5000000,
                10000000};
        for (int i = 0, level = 21; i < distanceDivide.length; i++, level--) {
            if (distance <= distanceDivide[i]) {
                return level;
            }
        }
        return 8;
    }

    private int getMapLevel(int mapLevel) {
        if (mapLevel <= 5) {
            return 21;
        } else if (mapLevel > 5 && mapLevel <= 10) {
            return 20;
        } else if (mapLevel > 10 && mapLevel <= 20) {
            return 19;
        } else if (mapLevel > 20 && mapLevel <= 50) {
            return 18;
        } else if (mapLevel > 50 && mapLevel <= 100) {
            return 17;
        } else if (mapLevel > 100 && mapLevel <= 200) {
            return 16;
        } else if (mapLevel > 200 && mapLevel <= 500) {
            return 15;
        } else if (mapLevel > 500 && mapLevel <= 1000) {
            return 14;
        } else if (mapLevel > 1000 && mapLevel <= 2000) {
            return 13;
        } else if (mapLevel <= 5000) {
            return 12;
        } else {
            return 12;
        }
        /**
         *  else if (mapLevel > 5000 && mapLevel <= 10000) {
         return 12;
         } else if (mapLevel > 10000 && mapLevel <= 20000) {
         return 11;
         } else if (mapLevel > 25000 && mapLevel <= 50000) {
         return 10;
         } else if (mapLevel > 50000 && mapLevel <= 100000) {
         return 9;
         } else if (mapLevel > 100000 && mapLevel <= 200000) {
         return 8;
         } else if (mapLevel > 200000 && mapLevel <= 500000) {
         return 7;
         } else if (mapLevel > 500000 && mapLevel <= 1000000) {
         return 6;
         } else if (mapLevel > 1000000 && mapLevel <= 2000000) {
         return 5;
         } else if (mapLevel > 2000000 && mapLevel <= 5000000) {
         return 4;
         } else if (mapLevel > 5000000 && mapLevel <= 10000000) {
         return 3;
         }
         */
    }

    @OnClick({R.id.map_publish, R.id.map_news, R.id.map_refresh, R.id.mi_task})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.map_publish:
                if (SpUtils.isCompleteUserInfo(mActivity)) {
                    isHend();
                } else {
                    CompleteUserInfoActivity.actionShow(mActivity, SpUtils.getUserToken(mActivity));
                }
                break;
            case R.id.map_news:
                startActivity(new Intent(getActivity(), MapNewsActivity.class));
                break;
            case R.id.mi_task:
                startActivity(new Intent(getActivity(), OfficialActivity.class));
                break;
            case R.id.map_refresh:
                mapView.refreshDrawableState();
                isMaplistData = true;
                isnewlatLng = false;
                isLocation = true;
                mLocationClient.start();
////                mapLevelNext
//                initNetWork(SpUtils.getXpoint(mActivity), SpUtils.getYpoint(mActivity),
//                        mapLevelNext, mSort, mOtherSex, mOtherLowAge, mOtherHignAge,
//                        mOtherLowheight, mOtherHignheight, mIsVideo, mIsIdcard,
//                        mSellerType, mTaskCity);
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d("百度地图,当界面不可见的时候关闭定位");
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationEnabled(false);
        }
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d(" //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理");
        if (mapView != null) {
            mapView.onResume();
        }
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationEnabled(true);
        }
        isLocation = true;/* 取消重新定位*/
        try {
            Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
            Collection<EMConversation> conversationValues = conversations.values();
            if (conversationValues.size() > 0) {
                int unreadCountSum = 0;
                for (EMConversation emConversation : conversationValues) {
                    if (emConversation != null) {
                        unreadCountSum += emConversation.getUnreadMsgCount();
                    }
                }
                mChatMessageCount = unreadCountSum;
            }
            refreshMessageCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSystemMessageNumber();
    }

    @Override
    public void onPause() {
        super.onPause();
        isLocation = false;
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationEnabled(false);
        }
        if (mapView != null) {
            mapView.onPause();
        }
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
        LogUtil.d("//在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理

        isLocation = true;
        // 退出时销毁定位
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
        /**
         * 退出时，销毁离线地图模块
         */
        if (mMKOfflineMap != null) {
            mMKOfflineMap.destroy();

        }
        if (mapView != null) {
            mapView.onDestroy();
        }
        //取消事件注册
        EventBus.getDefault().unregister(this);
    }

    /*筛选返回的数据*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(FilterReturnBean messageEvent) {
        mSort = messageEvent.getSort();
        mOtherSex = messageEvent.getOtherSex();
        mOtherLowAge = messageEvent.getOtherLowAge();
        mOtherHignAge = messageEvent.getOtherHignAge();
        mOtherLowheight = messageEvent.getOtherLowheight();
        mOtherHignheight = messageEvent.getOtherHignheight();
        mIsVideo = messageEvent.getIsVideo();
        mIsIdcard = messageEvent.getIsIdcard();
        mSellerType = ",".equals(messageEvent.getSellerType()) ? "" : messageEvent.getSellerType();
        mTaskCity = messageEvent.getTaskCity();
        LogUtil.d(mActivity.getPackageName(), messageEvent.toString());
        LogUtil.d("这个是百度第一次访问数据=====================筛选数据=========================");
        initNetWork(latitude + "", longitude + "",
                mapLevelNext, mSort, mOtherSex, mOtherLowAge, mOtherHignAge,
                mOtherLowheight, mOtherHignheight, mIsVideo, mIsIdcard,
                mSellerType, mTaskCity);
    }

    /**
     * 任务定位返回的数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(LatLngBean latLngBean) {
        if (latLngBean != null) {
            isnewlatLng = true;
            isMaplistData = true;
        }
        latLngBeanLatitude = latLngBean.getLatitude();
        latLngBeanLongitude = latLngBean.getLongitude();
        LatLng loc = new LatLng(latLngBeanLatitude, latLngBeanLongitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(loc);
        mBaiduMap.animateMapStatus(msu);
        DecimalFormat df = new DecimalFormat("######0.000000");
        String DecimalFormatLatitude = df.format(latLngBeanLatitude);
        String DecimalFormatLongitude = df.format(latLngBeanLongitude);
        double latitudeDoubleNext = Double.parseDouble(DecimalFormatLatitude);
        double longitudeDoubleNext = Double.parseDouble(DecimalFormatLongitude);
        LogUtil.d("这个是百度第一次访问数据=====================定位数据=========================");
        initNetWork(latitudeDoubleNext + "", longitudeDoubleNext + "",
                mapLevelNext, mSort, mOtherSex, mOtherLowAge, mOtherHignAge,
                mOtherLowheight, mOtherHignheight, mIsVideo, mIsIdcard,
                mSellerType, mTaskCity);
    }

    /**
     * 任务发布成功通知百度地图界面刷新
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(TaskReleaseRefreshTaskAndMapList taskAndMapList) {
        LogUtil.d("任务发布成功通知百度地图界面刷新" + taskAndMapList.isRefresh() + "");
        LogUtil.d("这个是百度第一次访问数据=====================任务发布成功通知百度地图界面刷新=========================");
        if (taskAndMapList.isRefresh()) {
            initNetWork(SpUtils.getXpoint(mActivity), SpUtils.getYpoint(mActivity),
                    0, mSort, mOtherSex, mOtherLowAge, mOtherHignAge,
                    mOtherLowheight, mOtherHignheight, mIsVideo, mIsIdcard,
                    mSellerType, mTaskCity);
        }
    }


    /**
     * 环信消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(UnreadHuanXinMsgCountBean msgCountBean) {
        mChatMessageCount = msgCountBean.getUnreadMsgCount();
        refreshMessageCount();
    }

    /**
     * 系统消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(ReceivedMessageBean ReceivedMessageBean) {
        getSystemMessageNumber();
    }


    /*侧边栏界面关闭了*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(LoodingData loodingData) {
        if (loodingData.isLooding() && loodingData.getPage() == 0) {
            mSort = "1";
            mOtherSex = "";
            mOtherLowAge = "";
            mOtherHignAge = "";
            mOtherLowheight = "";
            mOtherHignheight = "";
            mIsVideo = "";
            mIsIdcard = "";
            mSellerType = "";
        }
    }

    private void getSystemMessageNumber() {
        RetrofitUtil.createService(SettingService.class)
                .getUserSmsCnt(SpUtils.getUserToken(getActivity()))
                .enqueue(new RetrofitCallback<UserSmsCntBean>() {
                    @Override
                    public void onSuccess(Call<UserSmsCntBean> call, Response<UserSmsCntBean> response) {
                        if (response.isSuccessful()) {
                            UserSmsCntBean body = response.body();
                            if (body.getCode() == 1) {
                                mSystemMessageCount = body.getNewMsg();
                                refreshMessageCount();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserSmsCntBean> call, Throwable t) {
                    }
                });
    }

    private void refreshMessageCount() {
        int totalCount = mChatMessageCount + mSystemMessageCount;
        if (totalCount > 99) {
            totalCount = 99;
        }
        mPointNumber.setVisibility(totalCount == 0 ? View.GONE : View.VISIBLE);
        mPointNumber.setText(String.valueOf(totalCount));
    }

    /**
     * 头像审核
     */
    private void isHend() {
        RetrofitUtil.createService(TaskService.class)
                .userIsHead(SpUtils.getUserToken(mActivity))
                .enqueue(new RetrofitCallback<SuccessfulBean>() {
                    @Override
                    public void onSuccess(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                        if (response.isSuccessful()) {
                            SuccessfulBean successfulBean = response.body();
                            if (successfulBean.getResultCode() == 1) {
                                if (!TextUtils.isEmpty(successfulBean.getIsHead())) { //不为空
                                    if ("1".equals(successfulBean.getIsHead())) { //不通过
                                        mDialogFragment = new ImageHendDialogFragment(mFragment.getContext(), mFragment, 1);
                                        mDialogFragment.show(getFragmentManager(), "1");
                                    } else if ("0".equals(successfulBean.getIsHead())) { //审核中
                                        mDialogFragment = new ImageHendDialogFragment(mFragment.getContext(), mFragment, 2);
                                        mDialogFragment.show(getFragmentManager(), "2");
                                    } else if ("2".equals(successfulBean.getIsHead())) { //通过
                                        startActivity(new Intent(getActivity(), TaskActivity.class));
                                    }
                                } else {
                                    startActivity(new Intent(getActivity(), TaskActivity.class));
                                }
                            } else if (successfulBean.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (successfulBean.getResultCode() == 0) {
                                Toasty.normal(mActivity, successfulBean.getResultDesc()).show();
                            } else {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SuccessfulBean> call, Throwable t) {

                    }
                });
    }

    @Override
    public void SelectPhoto() { //选择照片
        mIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mIntent.setType("image/*");
        getActivity().startActivityForResult(mIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    public void PhotoGraph() { // 拍照
        String state = Environment.getExternalStorageState(); //拿到sdcard是否可用的状态码
        if (state.equals(Environment.MEDIA_MOUNTED)) {   //如果可用
            mIntent = new Intent(mActivity, UseCameraActivity.class);
            getActivity().startActivityForResult(mIntent, RESULT_LOAD_IMAGE2);
        } else {
            Toasty.normal(mActivity, "sdcard不可用").show();
        }
    }

    private int PHOTO_REQUEST_CUT = 100;//截取图片
    private int RESULT_LOAD_IMAGE2 = 200; //拍照
    private int RESULT_LOAD_IMAGE = 300; //选择图片

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            crop(selectedImage, false);
        }
        if (requestCode == RESULT_LOAD_IMAGE2 && null != data) {
            String extra = data.getStringExtra(UseCameraActivity.IMAGE_PATH);
            String correction = Conversion.correctImage(extra);
            startPhotoZoom(new File(correction));
        }
        if (requestCode == PHOTO_REQUEST_CUT && resultCode == RESULT_OK && data != null) {
            final String path = mCroppedImageUri.getPath();
            if (mDialogFragment != null && mCroppedImageUri != null && StringUtils.isNoNull(path)) {
                mDialogFragment.setImageUrl(path);
                ExecutorService executorService = new PriorityExecutor(5, false);
                PriorityRunnable priorityRunnable = new PriorityRunnable(Priority.NORMAL, new Runnable() {
                    @Override
                    public void run() {
                        initUpdateUserImageHead(path);
                    }
                });
                executorService.execute(priorityRunnable);
            }

        }
    }

    private void initUpdateUserImageHead(final String bitmap) {
        final File file = new File(bitmap);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RetrofitUtil.createService(SquareService.class)
                .updateUserImageHead(part, mUserToken)
                .enqueue(new Callback<SuccessfulBean>() {
                    @Override
                    public void onResponse(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                        if (response.isSuccessful()) {
                            SuccessfulBean body = response.body();
                            if (body.getResultCode() == 1) {
                                if (!TextUtils.isEmpty(body.getImageUrl())) {
                                    SpUtils.setImageHead(mActivity, body.getImageUrl());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SuccessfulBean> call, Throwable t) {
                    }
                });
    }

    /*
  * 剪切图片 相册选择
  */
    private void crop(Uri uri, boolean isAfterCapture) {
        if (uri == null) {
            LogUtil.i("alanjet", "The uri is not exist.");
        }
        LogUtil.d("tempUri" + uri);

        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        Uri imageUri;
        if (isAfterCapture && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && uri != null) {
            File file = new File(uri.getPath());
            imageUri = FileProvider.getUriForFile(mActivity, "com.jsz.peini.fileprovider", file);//通过FileProvider创建一个content类型的Uri
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            imageUri = uri;
        }
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 800);
        intent.putExtra("aspectY", 800);
        intent.putExtra("outputX", 800);
        intent.putExtra("outputY", 800);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", false);
        mCroppedImageUri = Uri.fromFile(new File(Conversion.LOCAL_IMAGE_CACHE_PATH, "crop_" + System.currentTimeMillis() + ".jpg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCroppedImageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    /**
     * 剪裁图片
     */
    private void startPhotoZoom(File file) {
        LogUtil.i("TAG", getImageContentUri(mActivity, file) + "裁剪照片的真实地址");
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(getImageContentUri(mActivity, file), "image/*");//自己使用Content Uri替换File Uri
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 800);
            intent.putExtra("aspectY", 800);
            intent.putExtra("outputX", 800);
            intent.putExtra("outputY", 800);
            intent.putExtra("scale", true);
            intent.putExtra("scaleUpIfNeeded", true);
            intent.putExtra("return-data", false);
            mCroppedImageUri = Uri.fromFile(new File(Conversion.LOCAL_IMAGE_CACHE_PATH, "crop_" + System.currentTimeMillis() + ".jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCroppedImageUri);//定义输出的File Uri
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            startActivityForResult(intent, PHOTO_REQUEST_CUT);
        } catch (ActivityNotFoundException e) {
            String errorMessage = "Your device doesn't support the crop action!";
            Toast.makeText(mActivity, errorMessage, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
}