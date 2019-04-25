package com.hsy.student_app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.hsy.student_app.R;
import com.hsy.student_app.adapter.ContactsAdapter;
import com.hsy.student_app.bean.AdressAndFoundBean;
import com.hsy.student_app.bean.ContactModel;
import com.hsy.student_app.helper.PinnedHeaderDecoration;
import com.hsy.student_app.utils.phone.LetterComparator;
import com.nanchen.wavesidebar.FirstLetterUtil;
import com.nanchen.wavesidebar.SearchEditText;
import com.nanchen.wavesidebar.Trans2PinYinUtil;
import com.nanchen.wavesidebar.WaveSideBarView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.hsy.student_app.fragment.FragmentFound.getJson;


/**
 * Description:
 * Data：2018/9/20-14:13
 * Author: Summer
 */
public class FragmentAdress extends Fragment {

    @BindView(R.id.tb_toolbar)
    Toolbar tbToolbar;
    Unbinder unbinder;
    private List<ContactModel> mContactModels;
    private List<ContactModel> mShowModels;
    private List<String> listJob;
    private RecyclerView mRecyclerView;
    private WaveSideBarView mWaveSideBarView;
    private SearchEditText mSearchEditText;
    private ContactsAdapter mAdapter;

    public static FragmentAdress newInstance() {

        Bundle args = new Bundle();

        FragmentAdress fragment = new FragmentAdress();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_adress, null);
        unbinder = ButterKnife.bind(this, view);
        initData();
        bindView(view);

        tbToolbar.setTitle("通讯录");
        return view;
    }

    private void bindView(View view) {

        mAdapter = new ContactsAdapter(mShowModels, getContext());

        // RecyclerView设置相关
        mRecyclerView = (RecyclerView) view.findViewById(R.id.main_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();

        decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
            @Override
            public boolean create(RecyclerView parent, int adapterPosition) {
                return true;
            }
        });
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mAdapter);


        // 侧边设置相关
        mWaveSideBarView = (WaveSideBarView) view.findViewById(R.id.main_side_bar);
        mWaveSideBarView.setOnSelectIndexItemListener(new WaveSideBarView.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String letter) {
                for (int i = 0; i < mContactModels.size(); i++) {
                    if (mContactModels.get(i).getIndex().equals(letter)) {
                        ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });


        // 搜索按钮相关
        mSearchEditText = (SearchEditText) view.findViewById(R.id.main_search);
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mShowModels.clear();
                for (ContactModel model : mContactModels) {
                    String str = Trans2PinYinUtil.trans2PinYin(model.getName());
                    if (str.contains(s.toString()) || model.getName().contains(s.toString())) {
                        mShowModels.add(model);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }


    private void initData() {
        mContactModels = new ArrayList<>();
        mShowModels = new ArrayList<>();

        mContactModels.addAll(adressData());
        mShowModels.addAll(mContactModels);
    }



    /**
     * Description:json
     * Author: Summer Data: 2018/9/28 10:43
     */
    private List<ContactModel> adressData() {

        List<ContactModel> contacts = null;
        try {
            AdressAndFoundBean adressAndFoundBean = JSON.parseObject(getJson(), AdressAndFoundBean.class);
            List<AdressAndFoundBean.AdressAllBean> adressAll = adressAndFoundBean.getAdressAll();
            contacts = new ArrayList<>();
            for (int i = 0; i <adressAll.size() ; i++) {
                ContactModel contactModel = new ContactModel();
                contactModel.setId(Integer.valueOf(adressAll.get(i).getId()));
                contactModel.setName(adressAll.get(i).getName());
                contactModel.setImgUrl(adressAll.get(i).getImgUrl());
                contactModel.setJobs(adressAll.get(i).getJobs());
                contactModel.setPhone(adressAll.get(i).getPhone());
                contactModel.setIndex(FirstLetterUtil.getFirstLetter(adressAll.get(i).getName()));
                contacts.add(contactModel);
            }

//            //开始解析json
//            JSONObject jsonObject = new JSONObject(getJson());
//            JSONArray adressAllJson = jsonObject.getJSONArray("adressAll");
//            contacts = new ArrayList<>();
//
//            for (int i = 0; i < adressAllJson.length(); i++) {
//                JSONObject adressJson = adressAllJson.getJSONObject(i);
//                JSONArray jsonArray = (JSONArray) adressJson.get("jobs");
//                listJob=new ArrayList<>();
//                for (int j = 0; j <jsonArray.length(); j++) {
//                    listJob.add((String)jsonArray.get(j));
//                }
//                contacts.add(new ContactModel(Integer.valueOf((String) adressJson.get("id")), adressJson.getString("name"), adressJson.getString("phone"), listJob, (String) adressJson.get("imgUrl"),null));
//            }
//            Log.e("jobs",listJob.get(0).toString());
//            Collections.sort(contacts, new LetterComparator());
            return contacts;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(contacts, new LetterComparator());
        return contacts;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mContactModels != null) {
            mContactModels.clear();
            mContactModels = null;
        }
        unbinder.unbind();
    }
}
