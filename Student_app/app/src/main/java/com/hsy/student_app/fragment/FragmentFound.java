package com.hsy.student_app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.hsy.student_app.R;
import com.hsy.student_app.activity.ActivitySendComment;
import com.hsy.student_app.adapter.AdapterRvFound;
import com.hsy.student_app.bean.AdressAndFoundBean;
import com.hsy.student_app.bean.Comment;
import com.hsy.student_app.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.hsy.student_app.helper.Contact.ISCONTAINS_IV;

public class FragmentFound extends Fragment {

    @BindView(R.id.tb_toolbar)
    Toolbar tbToolbar;
    Unbinder unbinder;
    @BindView(R.id.rv)
    RecyclerView rv;
    List<Comment> commentList;
    List<String> jobs;


    @BindView(R.id.swiprefreslayout)
    XRefreshView xRefreshView;
    @BindView(R.id.fab_exp1)
    FloatingActionButton fabExp1;
    @BindView(R.id.fab_exp2)
    FloatingActionButton fabExp2;
    @BindView(R.id.fab_menu)
    FloatingActionMenu fabMenu;

    private int refresh = 0;
    private int page = 0;
    private AdapterRvFound adapterRvFound;

    Intent toActivitySendCommentIntent;

    public static FragmentFound newInstance() {

        Bundle args = new Bundle();
        FragmentFound fragment = new FragmentFound();
        fragment.setArguments(args);
        return fragment;
    }

    private String getPath() {
        String path = Environment.getExternalStorageState() + "/corpPhoto";
        return path;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_found, container, false);
        unbinder = ButterKnife.bind(this, view);
        tbToolbar.setTitle("发现");

        initAll();
        initSwipRefresh();
        fabMenu.setClosedOnTouchOutside(true);
        return view;


    }

    /**
     * @Data 2019/4/23 15:38
     * @deprecated
     */
    private void initSwipRefresh() {
        // 设置是否可以下拉刷新
        xRefreshView.setPullRefreshEnable(true);
        // 设置是否可以上拉加载
        xRefreshView.setPullLoadEnable(true);
        // 设置是否可以自动刷新
        xRefreshView.setAutoRefresh(false);
        //设置是否自动加载更多
        xRefreshView.setAutoLoadMore(true);
        xRefreshView.setXRefreshViewListener(new XRefreshListener());

    }


    @OnClick({R.id.fab_exp1, R.id.fab_exp2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab_exp1:
                ToastUtils.showShort("写文字");
                toActivitySendCommentIntent = new Intent(getContext(), ActivitySendComment.class);
                toActivitySendCommentIntent.putExtra(ISCONTAINS_IV,false);
                startActivity(toActivitySendCommentIntent);
                fabMenu.close(true);
                break;
            case R.id.fab_exp2:
                ToastUtils.showShort("带照片");
                toActivitySendCommentIntent = new Intent(getContext(), ActivitySendComment.class);
                toActivitySendCommentIntent.putExtra(ISCONTAINS_IV,true);
                startActivity(toActivitySendCommentIntent);
                //申请相关权限
                fabMenu.close(true);
                break;

        }
    }



    private class XRefreshListener extends XRefreshView.SimpleXRefreshListener {
        @Override
        public void onRefresh(boolean isPullDown) {
            super.onRefresh(isPullDown);
            refresh = 0;
            page = 1;
            initAll();
            xRefreshView.stopRefresh();
        }

        @Override
        public void onLoadMore(boolean isSilence) {
            super.onLoadMore(isSilence);
            refresh = 1;
            page++;
            LodeMore();
            xRefreshView.stopLoadMore();
        }
    }

    private void initAll() {

        try {

            commentList = new ArrayList<>();
            AdressAndFoundBean adressAndFoundBean = JSON.parseObject(getJson(), AdressAndFoundBean.class);
            List<AdressAndFoundBean.AdressAllBean> adressAll = adressAndFoundBean.getAdressAll();
            for (int i = 0; i < adressAll.size(); i++) {
                List<AdressAndFoundBean.AdressAllBean.CommentBean> commentBeans = adressAll.get(i).getComment();
                for (int j = 0; j < commentBeans.size(); j++) {
                    Comment comment = new Comment();
                    comment.setName(adressAll.get(i).getName());
                    comment.setImgUrl(adressAll.get(i).getImgUrl());
                    comment.setJobs(adressAll.get(i).getJobs());
                    comment.setData(commentBeans.get(j).getData());
                    comment.setTime(commentBeans.get(j).getTime());
                    comment.setStar(commentBeans.get(j).getStar());
                    comment.setImageViewList(commentBeans.get(j).getImgUrl());
                    comment.setContent(commentBeans.get(j).getContent());
                    commentList.add(comment);
                }
            }

            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            rv.setLayoutManager(manager);
            adapterRvFound = new AdapterRvFound(getContext());
            rv.setAdapter(adapterRvFound);
            adapterRvFound.setList(commentList);
            adapterRvFound.notifyDataSetChanged();
            adapterRvFound.setOnItemClickListener_Star(new AdapterRvFound.OnItemClickListener() {
                boolean startIsClick = false;

                @Override
                public void onClickStar(int position, ImageView view) {
                    if (commentList.get(position).isStarIsClick()) {
                        commentList.get(position).setStar(commentList.get(position).getStar() - 1);
                        commentList.get(position).setStarIsClick(false);
                        view.setImageResource(R.drawable.star_normal);
                        adapterRvFound.notifyDataSetChanged();

                    } else {
                        commentList.get(position).setStar(commentList.get(position).getStar() + 1);
                        commentList.get(position).setStarIsClick(true);

                        view.setImageResource(R.drawable.star_click);
                        adapterRvFound.notifyDataSetChanged();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void LodeMore() {
        try {
            List<Comment> gsonCommentList = new ArrayList<>();


            AdressAndFoundBean adressAndFoundBean = JSON.parseObject(getMoreJson(), AdressAndFoundBean.class);
            List<AdressAndFoundBean.AdressAllBean> adressAll = adressAndFoundBean.getAdressAll();
            for (int i = 0; i < adressAll.size(); i++) {
                List<AdressAndFoundBean.AdressAllBean.CommentBean> commentBeans = adressAll.get(i).getComment();
                for (int j = 0; j < commentBeans.size(); j++) {
                    Comment comment = new Comment();
                    comment.setName(adressAll.get(i).getName());
                    comment.setImgUrl(adressAll.get(i).getImgUrl());
                    comment.setJobs(adressAll.get(i).getJobs());
                    comment.setData(commentBeans.get(j).getData());
                    comment.setTime(commentBeans.get(j).getTime());
                    comment.setStar(commentBeans.get(j).getStar());
                    comment.setImageViewList(commentBeans.get(j).getImgUrl());
                    comment.setContent(commentBeans.get(j).getContent());
                    gsonCommentList.add(comment);
                }
            }
            commentList.addAll(gsonCommentList);
            adapterRvFound.setList(commentList);
            adapterRvFound.notifyDataSetChanged();
            adapterRvFound.setOnItemClickListener_Star(new AdapterRvFound.OnItemClickListener() {
                @Override
                public void onClickStar(int position, ImageView view) {
                    if (commentList.get(position).isStarIsClick()) {
                        commentList.get(position).setStar(commentList.get(position).getStar() - 1);
                        commentList.get(position).setStarIsClick(false);
                        view.setImageResource(R.drawable.star_normal);
                        adapterRvFound.notifyDataSetChanged();

                    } else {
                        commentList.get(position).setStar(commentList.get(position).getStar() + 1);
                        commentList.get(position).setStarIsClick(true);
                        view.setImageResource(R.drawable.star_click);
                        adapterRvFound.notifyDataSetChanged();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public static String getMoreJson() {
        String json = "{\n" +
                "\t\"adressAll\": [{\n" +
                "\t\t\t\"id\": \"09\",\n" +
                "\t\t\t\"name\": \"阮子洋\",\n" +
                "\t\t\t\"phone\": \"531231513\",\n" +
                "\t\t\t\"imgUrl\": \"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\"jobs\": [\"学生\"],\n" +
                "\t\t\t\"comment\": [{\n" +
                "\t\t\t\t\"data\": \"2019-02-03\",\n" +
                "\t\t\t\t\"time\": \"12:21\",\n" +
                "\t\t\t\t\"star\": 3,\n" +
                "\t\t\t\t\"content\": \"More内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"data\": \"2018-01-03\",\n" +
                "\t\t\t\t\"time\": \"11:11\",\n" +
                "\t\t\t\t\"star\": 1,\n" +
                "\t\t\t\t\"content\": \"More内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"id\": \"010\",\n" +
                "\t\t\t\"name\": \"李嘉浩\",\n" +
                "\t\t\t\"phone\": \"162662255\",\n" +
                "\t\t\t\"imgUrl\": \"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\"jobs\": [\"学生\"],\n" +
                "\t\t\t\"comment\": [{\n" +
                "\t\t\t\t\"data\": \"2019-02-03\",\n" +
                "\t\t\t\t\"time\": \"12:21\",\n" +
                "\t\t\t\t\"star\": 3,\n" +
                "\t\t\t\t\"content\": \"More内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"data\": \"2018-01-03\",\n" +
                "\t\t\t\t\"time\": \"11:11\",\n" +
                "\t\t\t\t\"star\": 1,\n" +
                "\t\t\t\t\"content\": \"More内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"id\": \"011\",\n" +
                "\t\t\t\"name\": \"刘禹辰\",\n" +
                "\t\t\t\"phone\": \"156889463\",\n" +
                "\t\t\t\"imgUrl\": \"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\"jobs\": [\"学生\"],\n" +
                "\t\t\t\"comment\": [{\n" +
                "\t\t\t\t\"data\": \"2019-02-03\",\n" +
                "\t\t\t\t\"time\": \"12:21\",\n" +
                "\t\t\t\t\"star\": 3,\n" +
                "\t\t\t\t\"content\": \"More内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"data\": \"2018-01-03\",\n" +
                "\t\t\t\t\"time\": \"11:11\",\n" +
                "\t\t\t\t\"star\": 1,\n" +
                "\t\t\t\t\"content\": \"More内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"id\": \"012\",\n" +
                "\t\t\t\"name\": \"赵晨希\",\n" +
                "\t\t\t\"phone\": \"156549887\",\n" +
                "\t\t\t\"imgUrl\": \"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\"jobs\": [\"学生\"],\n" +
                "\t\t\t\"comment\": [{\n" +
                "\t\t\t\t\"data\": \"2019-02-03\",\n" +
                "\t\t\t\t\"time\": \"12:21\",\n" +
                "\t\t\t\t\"star\": 3,\n" +
                "\t\t\t\t\"content\": \"More内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"data\": \"2018-01-03\",\n" +
                "\t\t\t\t\"time\": \"11:11\",\n" +
                "\t\t\t\t\"star\": 1,\n" +
                "\t\t\t\t\"content\": \"More内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}]\n" +
                "\t\t}\n" +
                "\t]\n" +
                "\n" +
                "}";
        return json;
    }

    public static String getJson() {
        String json = "{\n" +
                "\t\"adressAll\": [{\n" +
                "\t\t\t\"id\": \"01\",\n" +
                "\t\t\t\"name\": \"赵海洋\",\n" +
                "\t\t\t\"phone\": \"1234564845\",\n" +
                "\t\t\t\"imgUrl\": \"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\"jobs\": [\"班长\", \"语文代表\"],\n" +
                "\t\t\t\"comment\": [{\n" +
                "\t\t\t\t\"data\": \"2013-02-03\",\n" +
                "\t\t\t\t\"time\": \"12:21\",\n" +
                "\t\t\t\t\"star\": 3,\n" +
                "\t\t\t\t\"content\": \"内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"data\": \"2018-01-03\",\n" +
                "\t\t\t\t\"time\": \"11:11\",\n" +
                "\t\t\t\t\"star\": 2,\n" +
                "\t\t\t\t\"content\": \"内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"id\": \"02\",\n" +
                "\t\t\t\"name\": \"刘湖南\",\n" +
                "\t\t\t\"phone\": \"1235645642\",\n" +
                "\t\t\t\"imgUrl\": \"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\"jobs\": [\"数学代表\"],\n" +
                "\t\t\t\"comment\": [{\n" +
                "\t\t\t\t\"data\": \"2019-02-03\",\n" +
                "\t\t\t\t\"time\": \"12:21\",\n" +
                "\t\t\t\t\"star\": 3,\n" +
                "\t\t\t\t\"content\": \"内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"data\": \"2018-01-03\",\n" +
                "\t\t\t\t\"time\": \"11:11\",\n" +
                "\t\t\t\t\"star\": 0,\n" +
                "\t\t\t\t\"content\": \"内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"id\": \"03\",\n" +
                "\t\t\t\"name\": \"刘雨桐\",\n" +
                "\t\t\t\"phone\": \"123456456\",\n" +
                "\t\t\t\"imgUrl\": \"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\"jobs\": [\"数学老师\", \"语文老师\"],\n" +
                "\t\t\t\"comment\": [{\n" +
                "\t\t\t\t\"data\": \"2019-02-03\",\n" +
                "\t\t\t\t\"time\": \"12:21\",\n" +
                "\t\t\t\t\"star\": 3,\n" +
                "\t\t\t\t\"content\": \"内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"data\": \"2016-01-03\",\n" +
                "\t\t\t\t\"time\": \"11:11\",\n" +
                "\t\t\t\t\"star\": 1,\n" +
                "\t\t\t\t\"content\": \"内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"id\": \"04\",\n" +
                "\t\t\t\"name\": \"赵艺博\",\n" +
                "\t\t\t\"phone\": \"1212315432\",\n" +
                "\t\t\t\"imgUrl\": \"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\"jobs\": [\"副班长\", \"语文代表\"],\n" +
                "\t\t\t\"comment\": [{\n" +
                "\t\t\t\t\"data\": \"2019-02-03\",\n" +
                "\t\t\t\t\"time\": \"12:21\",\n" +
                "\t\t\t\t\"star\": 3,\n" +
                "\t\t\t\t\"content\": \"内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"data\": \"2018-01-03\",\n" +
                "\t\t\t\t\"time\": \"11:11\",\n" +
                "\t\t\t\t\"star\": 1,\n" +
                "\t\t\t\t\"content\": \"内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"id\": \"05\",\n" +
                "\t\t\t\"name\": \"刘炳德\",\n" +
                "\t\t\t\"phone\": \"1112356423\",\n" +
                "\t\t\t\"imgUrl\": \"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\"jobs\": [\"英语代表\"],\n" +
                "\t\t\t\"comment\": [{\n" +
                "\t\t\t\t\"data\": \"2019-02-03\",\n" +
                "\t\t\t\t\"time\": \"12:21\",\n" +
                "\t\t\t\t\"star\": 3,\n" +
                "\t\t\t\t\"content\": \"内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"data\": \"2018-01-03\",\n" +
                "\t\t\t\t\"time\": \"11:11\",\n" +
                "\t\t\t\t\"star\": 1,\n" +
                "\t\t\t\t\"content\": \"内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"id\": \"06\",\n" +
                "\t\t\t\"name\": \"戴晓晓\",\n" +
                "\t\t\t\"phone\": \"124123152\",\n" +
                "\t\t\t\"imgUrl\": \"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\"jobs\": [\"学生\"],\n" +
                "\t\t\t\"comment\": [{\n" +
                "\t\t\t\t\"data\": \"2019-02-03\",\n" +
                "\t\t\t\t\"time\": \"12:21\",\n" +
                "\t\t\t\t\"star\": 3,\n" +
                "\t\t\t\t\"content\": \"内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"data\": \"2018-01-03\",\n" +
                "\t\t\t\t\"time\": \"11:11\",\n" +
                "\t\t\t\t\"star\": 1,\n" +
                "\t\t\t\t\"content\": \"内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"id\": \"07\",\n" +
                "\t\t\t\"name\": \"刘子铭\",\n" +
                "\t\t\t\"phone\": \"551564564\",\n" +
                "\t\t\t\"imgUrl\": \"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\"jobs\": [\"学生\"],\n" +
                "\t\t\t\"comment\": [{\n" +
                "\t\t\t\t\"data\": \"2019-02-03\",\n" +
                "\t\t\t\t\"time\": \"12:21\",\n" +
                "\t\t\t\t\"star\": 3,\n" +
                "\t\t\t\t\"content\": \"内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"data\": \"2018-01-03\",\n" +
                "\t\t\t\t\"time\": \"11:11\",\n" +
                "\t\t\t\t\"star\": 1,\n" +
                "\t\t\t\t\"content\": \"内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"id\": \"08\",\n" +
                "\t\t\t\"name\": \"孙亚菲\",\n" +
                "\t\t\t\"phone\": \"212544213\",\n" +
                "\t\t\t\"imgUrl\": \"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\"jobs\": [\"学生\"],\n" +
                "\t\t\t\"comment\": [{\n" +
                "\t\t\t\t\"data\": \"2019-02-03\",\n" +
                "\t\t\t\t\"time\": \"12:21\",\n" +
                "\t\t\t\t\"star\": 3,\n" +
                "\t\t\t\t\"content\": \"内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"data\": \"2018-01-03\",\n" +
                "\t\t\t\t\"time\": \"11:11\",\n" +
                "\t\t\t\t\"star\": 1,\n" +
                "\t\t\t\t\"content\": \"内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"id\": \"09\",\n" +
                "\t\t\t\"name\": \"阮子洋\",\n" +
                "\t\t\t\"phone\": \"531231513\",\n" +
                "\t\t\t\"imgUrl\": \"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\"jobs\": [\"学生\"],\n" +
                "\t\t\t\"comment\": [{\n" +
                "\t\t\t\t\"data\": \"2019-02-03\",\n" +
                "\t\t\t\t\"time\": \"12:21\",\n" +
                "\t\t\t\t\"star\": 3,\n" +
                "\t\t\t\t\"content\": \"内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"data\": \"2018-01-03\",\n" +
                "\t\t\t\t\"time\": \"11:11\",\n" +
                "\t\t\t\t\"star\": 1,\n" +
                "\t\t\t\t\"content\": \"内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"id\": \"010\",\n" +
                "\t\t\t\"name\": \"李嘉浩\",\n" +
                "\t\t\t\"phone\": \"162662255\",\n" +
                "\t\t\t\"imgUrl\": \"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\"jobs\": [\"学生\"],\n" +
                "\t\t\t\"comment\": [{\n" +
                "\t\t\t\t\"data\": \"2019-02-03\",\n" +
                "\t\t\t\t\"time\": \"12:21\",\n" +
                "\t\t\t\t\"star\": 3,\n" +
                "\t\t\t\t\"content\": \"内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"data\": \"2018-01-03\",\n" +
                "\t\t\t\t\"time\": \"11:11\",\n" +
                "\t\t\t\t\"star\": 1,\n" +
                "\t\t\t\t\"content\": \"内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"id\": \"011\",\n" +
                "\t\t\t\"name\": \"刘禹辰\",\n" +
                "\t\t\t\"phone\": \"156889463\",\n" +
                "\t\t\t\"imgUrl\": \"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\"jobs\": [\"学生\"],\n" +
                "\t\t\t\"comment\": [{\n" +
                "\t\t\t\t\"data\": \"2019-02-03\",\n" +
                "\t\t\t\t\"time\": \"12:21\",\n" +
                "\t\t\t\t\"star\": 3,\n" +
                "\t\t\t\t\"content\": \"内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"data\": \"2018-01-03\",\n" +
                "\t\t\t\t\"time\": \"11:11\",\n" +
                "\t\t\t\t\"star\": 1,\n" +
                "\t\t\t\t\"content\": \"内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"id\": \"012\",\n" +
                "\t\t\t\"name\": \"赵晨希\",\n" +
                "\t\t\t\"phone\": \"156549887\",\n" +
                "\t\t\t\"imgUrl\": \"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\"jobs\": [\"学生\"],\n" +
                "\t\t\t\"comment\": [{\n" +
                "\t\t\t\t\"data\": \"2019-02-03\",\n" +
                "\t\t\t\t\"time\": \"12:21\",\n" +
                "\t\t\t\t\"star\": 3,\n" +
                "\t\t\t\t\"content\": \"内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\",\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"data\": \"2018-01-03\",\n" +
                "\t\t\t\t\"time\": \"11:11\",\n" +
                "\t\t\t\t\"star\": 1,\n" +
                "\t\t\t\t\"content\": \"内容内容内容内容内容内容内容内容内容内容\",\n" +
                "\t\t\t\t\"imgUrl\": [\n" +
                "\t\t\t\t\t\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg\"\n" +
                "\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}]\n" +
                "\t\t}\n" +
                "\t]\n" +
                "\n" +
                "}";
        return json;
    }
//            //开始解析json
//            JSONObject jsonObject1 = new JSONObject(getJson());
//            JSONArray jsonArray = jsonObject1.getJSONArray("adressAll");
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//                jobs = new ArrayList<>();
//                JSONArray jsonJobs = (JSONArray) jsonObject.get("jobs");
//                for (int j = 0; j < jsonJobs.length(); j++) {
//                    jobs.add((String) jsonJobs.get(j));
//                }
//
//                JSONArray jsonCommet = (JSONArray) jsonObject.get("comment");
//                for (int j = 0; j < jsonCommet.length(); j++) {
//                    JSONObject jsonCommentItem = (JSONObject) jsonCommet.get(j);
//
//                    Comment comment = new Comment();
//                    comment.setName(jsonObject.getString("name"));
//                    comment.setImgUrl(jsonObject.getString("imgUrl"));
//                    comment.setJobs(jobs);
//                    comment.setData(jsonCommentItem.getString("data"));
//                    comment.setTime(jsonCommentItem.getString("time"));
//                    comment.setStar(jsonCommentItem.getInt("star"));
//                    comment.setContent(jsonCommentItem.getString("content"));
//                    JSONArray jsonImgArray = (JSONArray) jsonCommentItem.get("imgUrl");
//                    List<String> imgUrls = new ArrayList<>();
//                    for (int k = 0; k < jsonImgArray.length(); k++) {
//                        String img = (String) jsonImgArray.get(k);
//                        imgUrls.add(img);
//                    }
//                    comment.setImageViewList(imgUrls);
//                    commentList.add(comment);
//                }
//            }
}
