package com.hsy.student_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hsy.student_app.R;
import com.hsy.student_app.bean.RvItemBean;

import java.util.List;

public class Adapter_Rv_Common extends RecyclerView.Adapter<Adapter_Rv_Common.ViewHolder> {

    List<RvItemBean> list;
    Context context;

    public Adapter_Rv_Common(Context context) {
        this.context = context;
    }

    public void setList(List<RvItemBean> list) {
        this.list = list;
    }

    public List<RvItemBean> getList() {
        return list;
    }


    OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener {
        void onClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_rv_item, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        if( mOnItemClickListener!= null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(i);
                }
            });
        }
        holder.imageView.setImageResource(R.drawable.dv);
        holder.textView.setText(list.get(i).getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

           imageView =  itemView.findViewById(R.id.iv);
           textView = itemView.findViewById(R.id.tv);
        }
    }
}
