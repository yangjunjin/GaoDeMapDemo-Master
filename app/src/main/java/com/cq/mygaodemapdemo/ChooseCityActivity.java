package com.cq.mygaodemapdemo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amap.poisearch.searchmodule.CityChooseDelegate;
import com.amap.poisearch.searchmodule.CityChooseWidget;
import com.amap.poisearch.searchmodule.ICityChooseModule;
import com.amap.poisearch.util.CityModel;
import com.amap.poisearch.util.CityUtil;
import com.google.gson.Gson;

import java.util.ArrayList;


/**
 * Created by liangchao_suxun on 2017/5/9.
 */

public class ChooseCityActivity extends AppCompatActivity {
    private CityChooseWidget mCityChooseWidget;
    private MyCityChooseDelegate mCityChooseDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_choose);
        RelativeLayout contentView = (RelativeLayout)findViewById(R.id.content_view);

        mCityChooseDelegate = new MyCityChooseDelegate();
        mCityChooseDelegate.bindParentDelegate(mCityChooseParentDelegate);
        contentView.addView(mCityChooseDelegate.getWidget(this));

//
        ArrayList<CityModel> groupedCityList = CityUtil.getGroupCityList(ChooseCityActivity.this);
        Log.e("ChooseCityActivity=",new Gson().toJson(groupedCityList));
    }

    public static final String CURR_CITY_KEY = "curr_city_key";
    @Override
    protected void onResume() {
        super.onResume();
        String currCity = getIntent().getStringExtra(CURR_CITY_KEY);
        if (TextUtils.isEmpty(currCity)) {
            currCity = "北京市";
        }
        mCityChooseDelegate.setCurrCity(currCity);
    }

    private ICityChooseModule.IParentDelegate mCityChooseParentDelegate = new ICityChooseModule.IParentDelegate() {
        @Override
        public void onChooseCity(CityModel city) {
            Intent intent = new Intent();
            intent.putExtra(CURR_CITY_KEY, city);
            ChooseCityActivity.this.setResult(RESULT_OK, intent);
            ChooseCityActivity.this.finish();
            ChooseCityActivity.this.overridePendingTransition(0, R.anim.slide_out_down);
        }

        @Override
        public void onCancel() {
            ChooseCityActivity.this.finish();
            ChooseCityActivity.this.overridePendingTransition(0, R.anim.slide_out_down);
        }
    };

}
