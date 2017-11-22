package com.huiyi.huiyi_fanny.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.huiyi.huiyi_fanny.R;
import com.huiyi.huiyi_fanny.model.GoodsListEntry;

import java.util.List;
import butterknife.ButterKnife;

/**
 * Created by lw on 2017/11/1.
 * 搜索的适配器
 *
 */

public class GoodsListAdapter extends BaseAdapter implements Filterable {

    List<GoodsListEntry> list;
    private Context mContext;

    public GoodsListAdapter(Context mContext,List<GoodsListEntry> list) {
        this.mContext = mContext;
        this.list= list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        //  UserSSResult.QBean.CikasBean.CikaBean cikaBean = carList.get(position);
        GoodsListEntry goodsListEntry = list.get(position);
        HolderView holder = null;
        if(view == null){
            holder = new HolderView();
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item,null);
            holder.tvName = (TextView) view.findViewById(R.id.tv_strName);
            ButterKnife.bind(holder,view);
            view.setTag(holder);
        }else{
            holder = (HolderView) view.getTag();
        }
        List<GoodsListEntry.GoodEntry> entry = goodsListEntry.getGoodlist();
//        "交货单号：" + str3 + "\n客户编号：" + str7 + "\n客户名称：" + str8+ "\n下单数量：" + str10 + "\n已发数量：" + str11);
        //这里是合拼数量
        int xquantity = 0;
        int yquantity = 0;
        for(int i = 0;i < entry.size();i++){
            xquantity += Integer.parseInt(entry.get(i).getX_xquantity());
            yquantity += Integer.parseInt(entry.get(i).getX_yquantity());
        }
        holder.tvName.setText("交货单号：" + goodsListEntry.getX_number() + "\n客户编码："+entry.get(0).getC_number()
                +"\n下单数量：" +xquantity +"\n已发数量："+yquantity);
        return view;
    }

    @Override
    public Filter getFilter() {
        return null;
    }


    class HolderView {
        TextView tvName;
    }

}
