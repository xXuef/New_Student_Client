package com.hsy.student_app.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hsy.student_app.R;

import java.util.List;


/**
 * Description:职位横向adapter
 * Data：2018/9/25-14:11
 * Author: Summer
 */
public class AdapterRvJob extends RecyclerView.Adapter<AdapterRvJob.ViewHolder> {
    List<String> list;

    public void setList(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horlist, viewGroup, false);
        //将创建的View注册点击事件
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int postion) {
            holder.jobName.setText(list.get(postion));
    }

    @Override
    public int getItemCount() {
            return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView jobName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             jobName= itemView.findViewById(R.id.jobName);
        }
    }

}
