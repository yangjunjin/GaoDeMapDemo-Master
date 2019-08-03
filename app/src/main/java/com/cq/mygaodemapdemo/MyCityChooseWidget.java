package com.cq.mygaodemapdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.amap.poisearch.searchmodule.CityInputWidget;
import com.amap.poisearch.searchmodule.CityListWidget;
import com.amap.poisearch.searchmodule.CurrCityWidget;
import com.amap.poisearch.searchmodule.ICityChooseModule;
import com.amap.poisearch.searchmodule.ICityChooseModule.IDelegate;
import com.amap.poisearch.util.CityModel;

import java.util.ArrayList;

/**
 * Created by liangchao_suxun on 2017/4/28.
 */

public class MyCityChooseWidget extends RelativeLayout implements ICityChooseModule.IWidget, CityListWidget.IParentWidget, CityInputWidget.IParentWidget {

    private CityInputWidget mCityInputWidget;
    private CurrCityWidget mCurrCityWidget;
    private CityListWidget mCityListWidget;

    public MyCityChooseWidget(Context context) {
        super(context);
        init();
    }

    public MyCityChooseWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyCityChooseWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.my_widget_city_choose, this);

        mCurrCityWidget = (CurrCityWidget)findViewById(R.id.curr_city_widget);
        mCityListWidget = (CityListWidget)findViewById(R.id.city_list);
        mCityInputWidget = (CityInputWidget)findViewById(R.id.city_input_widget);

        mCityListWidget.setParentWidget(this);
        mCityInputWidget.setParentWidget(this);
    }

    private IDelegate mDelegate;
    @Override
    public void bindDelegate(IDelegate delegate) {
        mDelegate = delegate;
    }

    @Override
    public void setCurrCity(String cityName) {
        mCurrCityWidget.setCurrCity(cityName);
    }

    @Override
    public void loadCityList(ArrayList<CityModel> data){
        mCityListWidget.loadCityList(data);
    }

    @Override
    public void onSelCity(CityModel cityModel) {
        if (mDelegate != null) {
            mDelegate.onChooseCity(cityModel);
        }
    }

    @Override
    public void onCityInput(String cityInput) {
        if (mDelegate != null) {
            mDelegate.onCityInput(cityInput);
        }
    }

    @Override
    public void onCancel() {
        if (mDelegate != null) {
            mDelegate.onCancel();
        }
    }
}
