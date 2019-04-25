package com.hsy.student_app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hsy.student_app.R;
import com.hsy.student_app.adapter.Adapter_Rv_All;
import com.hsy.student_app.bean.RvItemBean;
import com.hsy.student_app.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentHome extends Fragment {

    @BindView(R.id.rv_common)
    RecyclerView rvCommon;
    @BindView(R.id.rv_all)
    RecyclerView rvAll;
    Unbinder unbinder;

    List<RvItemBean> listAll;
    List<RvItemBean> listCommon;
    @BindView(R.id.tb_toolbar)
    Toolbar tbToolbar;

    public static FragmentHome newInstance() {

        Bundle args = new Bundle();

        FragmentHome fragment = new FragmentHome();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        tbToolbar.setTitle("首页");
        try {
            String json = "[\n" +
                    "\n" +
                    "\t{\n" +
                    "\t\t\"name\": \"成绩\",\n" +
                    "\t\t\"imgurl\": \"\"\n" +
                    "\t},\n" +
                    "\t{\n" +
                    "\t\t\"name\": \"课表\",\n" +
                    "\t\t\"imgurl\": \"\"\n" +
                    "\t}, {\n" +
                    "\t\t\"name\": \"班级评分\",\n" +
                    "\t\t\"imgurl\": \"\"\n" +
                    "\t}, {\n" +
                    "\t\t\"name\": \"资产管理\",\n" +
                    "\t\t\"imgurl\": \"\"\n" +
                    "\t}, {\n" +
                    "\t\t\"name\": \"年级\",\n" +
                    "\t\t\"imgurl\": \"\"\n" +
                    "\t}, {\n" +
                    "\t\t\"name\": \"年纪评分\",\n" +
                    "\t\t\"imgurl\": \"\"\n" +
                    "\t}\n" +
                    "]";

            listAll = new ArrayList<>();
            listCommon = new ArrayList<>();
            //全部的
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                RvItemBean bean = new RvItemBean();
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                bean.setName(jsonObject.getString("name"));
                bean.setImgUrl(jsonObject.getString("imgurl"));
                listAll.add(bean);
            }

            GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
            rvAll.setLayoutManager(manager);
            Adapter_Rv_All adapterRvAll = new Adapter_Rv_All(getContext());
            adapterRvAll.setList(listAll);
            rvAll.setAdapter(adapterRvAll);
            adapterRvAll.notifyDataSetChanged();
            adapterRvAll.setOnItemClickListener(new Adapter_Rv_All.OnItemClickListener() {
                @Override
                public void onClick(int position) {
                    ToastUtils.showShort("" + position);
                }
            });

            //常用的
//            RvItemBean bean = new RvItemBean();
//            bean.setName("添加");
//            bean.setImgUrl("");
//            listCommon.add(bean);
//            GridLayoutManager manager2 = new GridLayoutManager(getContext(), 4);
//            rvCommon.setLayoutManager(manager2);
//            Adapter_Rv_Common adapterRvCommon = new Adapter_Rv_Common(getContext());
//            adapterRvCommon.setList(listCommon);
//            rvCommon.setAdapter(adapterRvCommon);
//            adapterRvCommon.notifyDataSetChanged();
//
//
//            adapterRvCommon.setOnItemClickListener(new Adapter_Rv_Common.OnItemClickListener() {
//                @Override
//                public void onClick(int position) {
//                    if (position == listCommon.size() - 1) {
//                        startActivity(new Intent(getContext(), ActivityAddCommon.class));
//                    }
//                }
//            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
