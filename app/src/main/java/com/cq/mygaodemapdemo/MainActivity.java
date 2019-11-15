package com.cq.mygaodemapdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.amap.poisearch.util.CityModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ///34343fdfdef
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_map:
                startActivity(new Intent( MainActivity.this, MapActivity.class));
                break;
            case R.id.btn_city:
//                startActivity(new Intent(MainActivity.this, CityActivity.class));
//                startActivity(new Intent(MainActivity.this, ChooseCityActivity.class));
                Intent intent = new Intent(MainActivity.this, ChooseCityActivity.class);
                intent.putExtra(ChooseCityActivity.CURR_CITY_KEY, "广州");
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.slide_in_up, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (1 == requestCode) {
                CityModel cityModel = data.getParcelableExtra(ChooseCityActivity.CURR_CITY_KEY);
//                mTripHostDelegate.setCurrCity(cityModel);
                Toast.makeText(MainActivity.this,cityModel.getCity(),Toast.LENGTH_SHORT).show();
            }
        }
    }
}
