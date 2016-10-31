package com.hq.hsmwan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hq.hsmwan.R;
import com.hq.hsmwan.bean.GoodsConfigBean;

import java.util.List;

/**
 * 商品详情里的规格参数数据适配器
 */
public class GoodsConfigAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<GoodsConfigBean> data;

    public GoodsConfigAdapter(Context context, List<GoodsConfigBean> data) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
    }

    public void setData(List<GoodsConfigBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public Object getItem(int position) {
        return this.data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.config_listview_item, null);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }

        GoodsConfigBean config = data.get(position);
        holder.tv_config_key.setText(config.getKeyProp());
        holder.tv_config_value.setText(config.getValue());
        return convertView;
    }

    class MyViewHolder {
        TextView tv_config_key;
        TextView tv_config_value;

        public MyViewHolder(View rootview) {
            tv_config_key = (TextView) rootview.findViewById(R.id.tv_config_key);
            tv_config_value = (TextView) rootview.findViewById(R.id.tv_config_value);
        }
    }
}
