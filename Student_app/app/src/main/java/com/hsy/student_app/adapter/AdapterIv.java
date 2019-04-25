package com.hsy.student_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hsy.student_app.R;

import java.util.List;

public class AdapterIv extends RecyclerView.Adapter<AdapterIv.ViewHold> {

    List<String> list;
    String defultList = "file:///android_asset/add.png";
    Context context;
    boolean inAddIvIsShow;

    public AdapterIv(Context context) {
        this.context = context;
    }


    public void setAddIvIsShow(boolean aBoolean) {
        this.inAddIvIsShow = aBoolean;
    }


    public void setList(List<String> list) {
        if (inAddIvIsShow) {
            list.add(defultList);
            this.list = list;
        } else {
            this.list = list;
        }
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
    public ViewHold onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_commet_iv, viewGroup, false);
        return new ViewHold(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold hold, final int i) {
        hold.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onClick(i);
            }
        });
        Glide.with(context).load(list.get(i)).into(hold.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHold extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHold(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv);
        }
    }
}
