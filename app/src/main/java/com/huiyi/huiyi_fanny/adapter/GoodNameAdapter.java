package com.huiyi.huiyi_fanny.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.huiyi.huiyi_fanny.R;
import com.huiyi.huiyi_fanny.model.GoodsListEntry;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 产品名称的适配器
 * */
public class GoodNameAdapter extends RecyclerView.Adapter<GoodNameAdapter.MyHolder>{

  List<GoodsListEntry.GoodEntry> list;
  private Context mContext;


  public GoodNameAdapter(List<GoodsListEntry.GoodEntry> list , Context mContext) {
    this.list = list;
    this.mContext = mContext;

  }

  @Override
  public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_good_all_item,parent,false);
    return new MyHolder(view);

  }

  @Override
  public void onBindViewHolder(MyHolder holder, final int position) {
    final GoodsListEntry.GoodEntry itemsBean = list.get(position);
     holder.btnText.setText(itemsBean.getP_name());

    holder.btnText.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (onItemClickListener != null){
          onItemClickListener.onItemClickListener("",position);

        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return list.size();
  }


  class MyHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.btn_color)
    Button btnText;

    public MyHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

  }

  private OnItemClickListener onItemClickListener;

  public interface OnItemClickListener {
    void onItemClickListener(String id, int position);
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

}
