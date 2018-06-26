package com.cheng.citydemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cheng.citydemo.CarCityClickListener;
import com.cheng.citydemo.R;
import com.cheng.citydemo.bean.City;
import java.util.ArrayList;
import java.util.List;

public class CitySearchAdapter extends BaseAdapter {

    private Context context;
    private CarCityClickListener listener;
    private LayoutInflater inflater;
    private List<City> lists = new ArrayList<>();

    public CitySearchAdapter(Context context, CarCityClickListener listener){
        this.context = context;
        this.listener = listener;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<City> station){
        this.lists.clear();
        if(station!=null){
            this.lists.addAll(station);
        }
        notifyDataSetChanged();
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
            convertView = inflater.inflate(R.layout.item_lv_city_search, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.city_search_tv_name.setText(lists.get(position).name);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.choice(lists.get(position));
            }
        });
        return convertView;
    }

    public class ViewHolder{

        public TextView city_search_tv_name;

        public ViewHolder(View v){
            city_search_tv_name = (TextView) v.findViewById(R.id.city_search_tv_name);
        }

    }

}
