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

public class CityHistoryAdapter extends BaseAdapter {

    private Context context;
    private CarCityClickListener listener;
    private LayoutInflater inflater;
    private List<City> lists = new ArrayList<>();

    public CityHistoryAdapter(Context context, CarCityClickListener listener, List<City> lists){
        this.context = context;
        this.listener = listener;
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
            convertView = inflater.inflate(R.layout.item_gv_city_history, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.city_history_tv_name.setText(lists.get(position).name);
        if(position==0){
            holder.city_history_tv_name.setTextColor(context.getResources().getColor(R.color.text_blue));
            holder.city_history_iv_local.setVisibility(View.VISIBLE);
            holder.city_history_iv_clear.setVisibility(View.GONE);
        }else{
            holder.city_history_tv_name.setTextColor(context.getResources().getColor(R.color.text_main_dark_gray));
            holder.city_history_iv_local.setVisibility(View.GONE);
            if(position==lists.size()-1){
                holder.city_history_iv_clear.setVisibility(View.GONE);
                holder.city_history_tv_name.setTextColor(context.getResources().getColor(R.color.text_blue));
            }else{
                holder.city_history_iv_clear.setVisibility(View.VISIBLE);
                holder.city_history_tv_name.setTextColor(context.getResources().getColor(R.color.text_main_dark_gray));
            }
        }
        holder.city_history_iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.remove(lists.get(position));
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position>1 && position==lists.size()-1){
                    listener.clear();
                }else{
                    if(!TextUtils.isEmpty(lists.get(position).name) && !lists.get(position).name.contains("定位")){
                        listener.choice(lists.get(position));
                    }else{
                        listener.location();
                    }
                }
            }
        });
        return convertView;
    }

    public class ViewHolder{

        private ImageView city_history_iv_local;
        private TextView city_history_tv_name;
        private ImageView city_history_iv_clear;

        public ViewHolder(View v){
            city_history_iv_local = (ImageView) v.findViewById(R.id.city_history_iv_local);
            city_history_iv_clear = (ImageView) v.findViewById(R.id.city_history_iv_clear);
            city_history_tv_name = (TextView) v.findViewById(R.id.city_history_tv_name);
        }

    }

}
