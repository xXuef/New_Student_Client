package com.hsy.student_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hsy.student_app.R;
import com.hsy.student_app.bean.Comment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterRvFound extends RecyclerView.Adapter<AdapterRvFound.ViewHolde> {


    List<Comment> list;
    Context context;


    OnItemClickListener mOnItemStarClickListener;
    public interface OnItemClickListener {
        void onClickStar(int position,ImageView view);
    }
    public void setOnItemClickListener_Star(OnItemClickListener onItemClickListener) {
        this.mOnItemStarClickListener = onItemClickListener;
    }
    public void setList(List<Comment> list) {
        this.list = list;
    }

    public AdapterRvFound(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolde onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_found_rv, viewGroup, false);

        return new ViewHolde(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolde holde, final int i) {

        if( mOnItemStarClickListener!= null) {
            holde.ivStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemStarClickListener.onClickStar(i,  holde.ivStar);
                }
            });
        }
        if (i==list.size()-1){
            holde.fakeDervie.setVisibility(View.GONE);
        }
        Glide.with(context).load(list.get(i).getImgUrl()).into(holde.iv);
        holde.tvName.setText(list.get(i).getName());
        AdapterRvJob adapterRvJob = new AdapterRvJob();

        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holde.rvJob.setLayoutManager(manager);
        holde.rvJob.setAdapter(adapterRvJob);
        adapterRvJob.setList(list.get(i).getJobs());
        adapterRvJob.notifyDataSetChanged();
        holde.tvContent.setText(list.get(i).getContent());
        holde.tvData.setText(list.get(i).getData());
        holde.tvTime.setText(list.get(i).getTime());
        GridLayoutManager manager1 = new GridLayoutManager(context, 3);
        holde.rvIv.setLayoutManager(manager1);
        AdapterIv aci = new AdapterIv(context);
        aci.setList(list.get(i).getImageViewList());
        holde.rvIv.setAdapter(aci);
        holde.tvStar.setText(list.get(i).getStar()==0?"点赞":list.get(i).getStar()+"");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolde extends RecyclerView.ViewHolder {
        @BindView(R.id.iv)
        CircleImageView iv;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.rv_job)
        RecyclerView rvJob;
        @BindView(R.id.rv_iv)
        RecyclerView rvIv;
        @BindView(R.id.tv_data)
        TextView tvData;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_star)
        TextView tvStar;
        @BindView(R.id.iv_star)
        ImageView ivStar;
        @BindView(R.id.fake_dervie)
        View fakeDervie;

        public ViewHolde(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
