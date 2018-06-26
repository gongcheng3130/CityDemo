package com.cheng.citydemo.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.cheng.citydemo.CarCityClickListener;
import com.cheng.citydemo.util.MyGridView;
import com.cheng.citydemo.R;
import com.cheng.citydemo.bean.AllCity;
import com.cheng.citydemo.bean.City;
import java.util.ArrayList;
import java.util.List;

public class CityListAdapter extends BaseAdapter {

    private static final int VIEW_TYPE_COUNT = 3;
    private Context mContext;
    private CarCityClickListener listener;
    private LayoutInflater inflater;
    private boolean isShowHot;
    public City local = new City("定位中");
    private List<City> hot_show = new ArrayList<>();
    private List<City> hot_all = new ArrayList<>();
    private List<City> his_all = new ArrayList<>();
    private ArrayList<AllCity> all = new ArrayList<>();
    private List<String> litter = new ArrayList<>();

    public CityListAdapter(Context mContext, CarCityClickListener listener){
        this.mContext = mContext;
        this.listener = listener;
        setHot(new ArrayList<City>(), false);
        this.inflater = LayoutInflater.from(mContext);
    }

    public void setLocation(City base){
        local = base;
        if(his_all.size()==0){
            his_all.add(new City());
        }
        his_all.set(0, local);
        notifyDataSetChanged();
    }

    public void setHot(List<City> hot, boolean isShow){
        this.hot_all.clear();
        if(hot!=null && hot.size()>0){
            hot_all.addAll(hot);
        }
        setHot(isShow);
    }

    public void setHot(boolean isShow){
        this.isShowHot = isShow;
        this.hot_show.clear();
        if(isShow){
            hot_show.addAll(hot_all);
        }else{
            if(hot_all.size()>8){
                for (int i = 0; i < 8; i++) {
                    hot_show.add(hot_all.get(i));
                }
            }else{
                hot_show.addAll(hot_all);
            }
        }
        if(hot_show.size()>=8){
            hot_show.add(new City(""));
        }
        notifyDataSetChanged();
    }

    public void setHistory(List<City> history){
        this.his_all.clear();
        this.his_all.add(local);
        if(history!=null && history.size()>0){
            this.his_all.addAll(history);
            this.his_all.add(new City("清除全部"));
        }
        notifyDataSetChanged();
    }

    public void setAll(ArrayList<AllCity> all){
        this.all.clear();
        if(all!=null && all.size()>0){
            this.all.addAll(all);
        }
        notifyDataSetChanged();
    }

    public void setLetter(List<String> litter){
        this.litter.clear();
        if(litter!=null && litter.size()>0){
            this.litter.addAll(litter);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return litter.size();
    }

    @Override
    public Object getItem(int position) {
        return litter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return position > VIEW_TYPE_COUNT-2 ? VIEW_TYPE_COUNT-1 : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        if(viewType==0 || viewType==1){
            ViewHolder1 holder;
            if (convertView == null) {
                if(viewType==0){
                    convertView = inflater.inflate(R.layout.item_lv_city_history, parent, false);
                }else{
                    convertView = inflater.inflate(R.layout.item_lv_city_hot, parent, false);
                }
                holder = new ViewHolder1(convertView);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder1) convertView.getTag();
            }
            if(viewType==0){
                CityHistoryAdapter hotAdapter = new CityHistoryAdapter(mContext, listener, his_all);
                holder.city_gv.setAdapter(hotAdapter);
            }else{
                CityHotAdapter hotAdapter = new CityHotAdapter(mContext, listener, isShowHot, hot_show);
                holder.city_gv.setAdapter(hotAdapter);
            }
            holder.city_gv.setSelector(new ColorDrawable(mContext.getResources().getColor(R.color.transparent_white_alpha_00)));//去除默认的条目点击效果
        }else{
            ViewHolder2 holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_lv_city_all, parent, false);
                holder = new ViewHolder2(convertView);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder2) convertView.getTag();
            }
            if(all.size()>0 && position>=2){
                CityAllAdapter allAdapter = new CityAllAdapter(mContext, listener, litter.get(position), all.get(position-2).cities);
                holder.city_all_lv.setAdapter(allAdapter);
            }
        }
        return convertView;
    }

    public class ViewHolder1 {

        private MyGridView city_gv;

        public ViewHolder1(View v){
            city_gv = (MyGridView) v.findViewById(R.id.city_gv);
        }

    }

    public class ViewHolder2 {

        public ListView city_all_lv;

        public ViewHolder2(View v){
            city_all_lv = (ListView) v.findViewById(R.id.city_all_lv);
        }

    }

}
