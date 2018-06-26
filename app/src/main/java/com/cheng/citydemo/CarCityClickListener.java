package com.cheng.citydemo;

import com.cheng.citydemo.bean.City;

/**
 * Created by rongyu on 2017/9/19.
 */
public interface CarCityClickListener {

    void location();

    void showHot();

    void hideHot();

    void choice(City base);

    void remove(City base);

    void clear();

}
