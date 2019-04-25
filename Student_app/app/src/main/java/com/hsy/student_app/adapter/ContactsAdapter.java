package com.hsy.student_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hsy.student_app.R;
import com.hsy.student_app.bean.ContactModel;

import java.util.List;

/**
 * Contact联系人适配器
 *
 * @fileName WaveSideBarView
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

    private List<ContactModel> contacts;
    Context context;
    private static final String TAG = "ContactsAdapter";

    public ContactsAdapter(List<ContactModel> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layaout_item_contacts, parent,false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContactsViewHolder holder, int position) {
        final ContactModel contact = contacts.get(position);
        Log.e(TAG, "onBindViewHolder: index:" + contact.getIndex());
        if (position == 0 || !contacts.get(position - 1).getIndex().equals(contact.getIndex())) {
            holder.tvIndex.setVisibility(View.VISIBLE);
            holder.tvIndex.setText(contact.getIndex());

        } else {
            holder.tvIndex.setVisibility(View.GONE);
        }

        holder.tvName.setText(contact.getName());



        Glide.with(context)
                .load(contacts.get(position).getImgUrl())
                .placeholder(R.drawable.dv)
                .error(R.drawable.dv)
                .into(holder.ivAvatar);

        holder.tvPhone.setText(contacts.get(position).getPhone());
        holder.ivPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri data = Uri.parse("tel:" + contact.getPhone());
                    Intent intent1 = new Intent(Intent.ACTION_DIAL);
                    intent1.setData(data);
                    context.startActivity(intent1);
                } catch (Exception e) {
                    Toast.makeText(context, "本机没有拨号功能", Toast.LENGTH_SHORT).show();
                }

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recyclerView.setLayoutManager(layoutManager);
        AdapterRvJob adapterRvJob = new AdapterRvJob();
        adapterRvJob.setList(contacts.get(position).getJobs());
        holder.recyclerView.setAdapter(adapterRvJob);
//        adapterRvJob.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder {
        TextView tvIndex;
        ImageView ivAvatar;
        ImageView ivPhone;
        TextView tvName;
        TextView tvPhone;
        RecyclerView recyclerView;

        ContactsViewHolder(View itemView) {
            super(itemView);
            tvIndex = (TextView) itemView.findViewById(R.id.tv_index);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
            ivPhone = (ImageView) itemView.findViewById(R.id.phone_icon);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview);
        }
    }

}
