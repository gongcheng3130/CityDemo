package com.cheng.citydemo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.cheng.citydemo.adapter.CityListAdapter;
import com.cheng.citydemo.adapter.CitySearchAdapter;
import com.cheng.citydemo.bean.AllCity;
import com.cheng.citydemo.bean.City;
import com.cheng.citydemo.bean.HanziToPinyin3;
import com.cheng.citydemo.db.MyDBManage;
import com.cheng.citydemo.util.Convert;
import com.cheng.citydemo.util.SideBar;
import com.cheng.citydemo.util.StrUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChoiceCityActivity extends AppCompatActivity implements View.OnClickListener, CityClickListener {

    private ImageView city_iv_back;
    private EditText city_et_search;
    private ImageView city_iv_clean;
    private SideBar sideBar;
    private TextView choice_citv_tv_flag;
    private ListView city_lv_city_list;//one
    private ListView city_lv_search_result;//two
    private TextView city_tv_search_empty;//lastTextView
    private CityListAdapter list_adapter;
    private CitySearchAdapter search_adapter;
    private List<City> all_city = new ArrayList<>();
    private List<City> search = new ArrayList<>();
    private List<String> litter = new ArrayList<>();
    private MyDBManage dbManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_city);
        dbManage = MyDBManage.getInstance(this);
        initView();
        initListener();
        getHistory();
        getHotData();
        getLocationData();
//        locationManager.startLocation(500, true);定位
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 500 && resultCode == RESULT_OK){
//            locationManager.startLocation(500, true);//请求定位权限后重定位
        }
    }

    private void initView() {
        city_iv_back = (ImageView) findViewById(R.id.city_iv_back);
        city_et_search = (EditText) findViewById(R.id.city_et_search);
        city_iv_clean = (ImageView) findViewById(R.id.city_iv_clean);
        sideBar = findViewById(R.id.sideBar);
        choice_citv_tv_flag = (TextView) findViewById(R.id.choice_citv_tv_flag);
        city_lv_city_list = (ListView) findViewById(R.id.city_lv_city_list);
        city_lv_search_result = (ListView) findViewById(R.id.city_lv_search_result);
        city_tv_search_empty = (TextView) findViewById(R.id.city_tv_search_empty);
        search_adapter = new CitySearchAdapter(this, this);
        city_lv_search_result.setAdapter(search_adapter);
        list_adapter = new CityListAdapter(this, this);
        city_lv_city_list.setAdapter(list_adapter);
        city_lv_city_list.setSelected(true);
    }

    private void initListener() {
        city_iv_back.setOnClickListener(this);
        city_iv_clean.setOnClickListener(this);
        city_et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString().trim())) {
                    intoSearch();
                    long now = System.currentTimeMillis();
                    if (now - msec > 150) {
                        msec = now;
                        getSearchData(s.toString());
                    }
                } else {
                    exitSearch();
                }
            }
        });
        sideBar.setOnLetterChangedListener(new SideBar.OnLetterChangedListener() {
            @Override
            public void onLetterChangedListener(int index, String s) {
                choice_citv_tv_flag.setVisibility(View.VISIBLE);
                choice_citv_tv_flag.setText(s);
                city_lv_city_list.setSelection(index);
            }
            @Override
            public void onLetterReleaseListener() {
                choice_citv_tv_flag.setVisibility(View.GONE);
            }
        });
    }

    public void setResult(City city) {
        List<City> all = dbManage.findAll(City.class);
        int index = -1;
        for (int i = 0; i < all.size(); i++) {
            if(all.get(i).name.equals(city.name)){
                index = i;
                break;
            }
        }
        city.save_time = System.currentTimeMillis();
        long insert = 0;
        if(index==-1){
            if(all.size()>=9) dbManage.deleteById(City.class, all.get(all.size()-1).id);
            insert = dbManage.addObject(city);
        }else{
            insert = dbManage.updateByArgs(City.class, city, "name=?", new String[]{city.name});
        }
        Intent intent = new Intent();
        intent.putExtra("City", city);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.city_iv_back:
                finish();
                break;
            case R.id.city_iv_clean:
                city_et_search.setText("");
                exitSearch();
                break;
            default:
                break;
        }
    }

    private long msec = 0;

    private void exitSearch() {
        city_lv_city_list.setVisibility(View.VISIBLE);
        city_iv_clean.setVisibility(View.GONE);
        city_tv_search_empty.setVisibility(View.GONE);
        city_lv_search_result.setVisibility(View.GONE);
    }

    private void intoSearch() {
        city_lv_city_list.setVisibility(View.GONE);
        city_iv_clean.setVisibility(View.VISIBLE);
        city_lv_search_result.setVisibility(View.VISIBLE);
    }

    private void getLocationData(){
        litter.add("历");
        litter.add("热");
        new Thread(){
            @Override
            public void run() {
                try {
                    InputStream is = getAssets().open("allcity.txt");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    String str = new String(buffer, "UTF-8");
                    ArrayList<AllCity> lists = new ArrayList<>();
                    AllCity[] data = Convert.fromJson(str, AllCity[].class);
                    if(data!=null && data.length>0){
                        for (int i = 0; i < data.length; i++) {
                            litter.add(data[i].name);
                            lists.add(data[i]);
                            if(data[i]!=null && data[i].cities!=null){
                                all_city.addAll(data[i].cities);
                            }
                        }
                    }
                    Message m = handler.obtainMessage();
                    m.what = 10;
                    m.obj = lists;
                    handler.sendMessage(m);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }.run();
    }

    private void getHistory() {
        List<City> all = dbManage.findByArgs(City.class, null, null, "save_time", true, 7, 0);
        list_adapter.setHistory(all);
    }

    private void getHotData() {
        new Thread(){
            @Override
            public void run() {
                try {
                    ArrayList<City> hots = new ArrayList<>();
                    InputStream is = getAssets().open("hotcity.txt");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    String str = new String(buffer, "UTF-8");
                    City[] data = Convert.fromJson(str, City[].class);
                    Collections.addAll(hots, data);
                    Message m = handler.obtainMessage();
                    m.what = 15;
                    m.obj = hots;
                    handler.sendMessage(m);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }.run();
    }

    private void getSearchData(final String str) {
        new Thread(){
            @Override
            public void run() {
                ArrayList<City> cur_search_station = new ArrayList<>();//当次搜索数据
                for (int i = 0; i < all_city.size(); i++) {
                    if(StrUtils.isChinese(str)){
                        if((StrUtils.isNotNull(all_city.get(i).name) && all_city.get(i).name.contains(str))){
                            cur_search_station.add(all_city.get(i));
                        }
                    }else{
                        String pinYin = HanziToPinyin3.getPinYin(str);
                        String py = HanziToPinyin3.getPinYin(all_city.get(i).name);
                        if(py.contains(pinYin)){
                            cur_search_station.add(all_city.get(i));
                        }
                    }
                }
                Message m = handler.obtainMessage();
                m.what = 20;
                m.obj = cur_search_station;
                handler.sendMessage(m);
            }
        }.run();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    sideBar.setLetter(litter);
                    ArrayList<AllCity> lists = (ArrayList<AllCity>) msg.obj;
                    list_adapter.setLetter(litter);
                    list_adapter.setAll(lists);
                    break;
                case 15:
                    ArrayList<City> hot = (ArrayList<City>) msg.obj;
                    list_adapter.setHot(hot, false);
                    break;
                case 20:
                    search.clear();
                    ArrayList<City> cur_search_base = (ArrayList<City>) msg.obj;
                    if (cur_search_base!=null && cur_search_base.size()>0) {
                        search.addAll(cur_search_base);
                    }
                    search_adapter.setList(search);
                    if (search == null || search.size() == 0) {
                        city_tv_search_empty.setVisibility(View.VISIBLE);
                    } else {
                        city_tv_search_empty.setVisibility(View.GONE);
                        intoSearch();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    //TODO 定位结果
//    @Override
//    public void result(Location location, int error) {
//          getCityId(location.name)
//    }

    //根据定位结果的城市名拿城市数据
    private City getCityId(String cityName) {
        if (all_city != null && all_city.size() > 0) {
            for (int i = 0; i < all_city.size(); i++) {
                if(all_city.get(i).name.equals(cityName) || all_city.get(i).name.contains(cityName)){
                    return all_city.get(i);
                }
            }
        }
        return new City("定位失败");
    }

    @Override
    public void location() {
//        locationManager.removeLocationUpdate();
//        locationManager.startLocation(500);
        list_adapter.setLocation(new City("定位中"));
    }

    @Override
    public void showHot() {
        list_adapter.setHot(true);
    }

    @Override
    public void hideHot() {
        list_adapter.setHot(false);
    }

    @Override
    public void choice(City base) {
        setResult(base);
    }

    @Override
    public void remove(City base) {
        dbManage.deleteById(City.class, base.id);
        getHistory();
    }

    @Override
    public void clear() {
        dbManage.deleteByClass(City.class, 0);
        list_adapter.setHistory(new ArrayList<City>());
    }

}
