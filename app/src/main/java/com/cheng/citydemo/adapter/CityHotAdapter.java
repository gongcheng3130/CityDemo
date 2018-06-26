package com.cheng.citydemo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.cheng.citydemo.CarCityClickListener;
import com.cheng.citydemo.R;
import com.cheng.citydemo.bean.City;
import java.util.ArrayList;
import java.util.List;

public class CityHotAdapter extends BaseAdapter {

    private Context context;
    private CarCityClickListener listener;
    private LayoutInflater inflater;
    public boolean isShowHot;
    private List<City> lists = new ArrayList<>();

    public CityHotAdapter(Context context, CarCityClickListener listener, boolean isShowHot, List<City> lists){
        this.context = context;
        this.listener = listener;
        this.isShowHot = isShowHot;
        this.inflater = LayoutInflater.from(context);
        if(lists!=null){
            this.lists.addAll(lists);
        }
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gv_city_hot, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.city_hot_tv_name.setText(lists.get(position).name);
        if(position!=0 && position==lists.size()-1 && TextUtils.isEmpty(lists.get(position).name)){
            holder.city_hot_iv_arrow.setVisibility(View.VISIBLE);
            if(position==8){
                holder.city_hot_iv_arrow.setImageResource(R.mipmap.icon_arrow_down);
            }else{
                holder.city_hot_iv_arrow.setImageResource(R.mipmap.icon_arrow_up);
            }
        }else{
            holder.city_hot_iv_arrow.setVisibility(View.GONE);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==8 && TextUtils.isEmpty(lists.get(position).name)){
                    listener.showHot();
                }else if(position==lists.size()-1 && TextUtils.isEmpty(lists.get(position).name)){
                    listener.hideHot();
                }else{
                    listener.choice(lists.get(position));
                }
            }
        });
        return convertView;
    }

    public class ViewHolder{

        private TextView city_hot_tv_name;
        private ImageView city_hot_iv_arrow;

        public ViewHolder(View v){
            city_hot_iv_arrow = (ImageView) v.findViewById(R.id.city_hot_iv_arrow);
            city_hot_tv_name = (TextView) v.findViewById(R.id.city_hot_tv_name);
        }

    }

}
