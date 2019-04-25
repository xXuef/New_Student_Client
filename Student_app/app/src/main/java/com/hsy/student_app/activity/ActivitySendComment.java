package com.hsy.student_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hsy.student_app.R;
import com.hsy.student_app.adapter.AdapterIv;
import com.hsy.student_app.utils.ToastUtils;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hsy.student_app.helper.Contact.ISCONTAINS_IV;

public class ActivitySendComment extends TakePhotoActivity implements TakePhoto.TakeResultListener, InvokeListener {
    InvokeParam invokeParam;
    @BindView(R.id.tb_toolbar)
    Toolbar tbToolbar;
    @BindView(R.id.et)
    EditText et;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.bt_select_iv)
    Button btSelectIv;
    List<String> imgPathList;

    private CropOptions cropOptions;
    private AdapterIv adapterIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_comment);
        ButterKnife.bind(this);

        boolean isContainIv = getIntent().getBooleanExtra(ISCONTAINS_IV, false);
        if (!isContainIv){//如果点击的不带照片
            rv.setVisibility(View.GONE);
            btSelectIv.setVisibility(View.GONE);
        }

        tbToolbar.setTitle("动态");
        imgPathList = new ArrayList<>();
        GridLayoutManager manager1 = new GridLayoutManager(this,3);
        rv.setLayoutManager(manager1);
        adapterIv = new AdapterIv(ActivitySendComment.this);
        adapterIv.setAddIvIsShow(true);
        adapterIv.setList(imgPathList);
        Log.e("imgPathList",imgPathList.size()+"");
        rv.setAdapter(adapterIv);
        adapterIv.setOnItemClickListener(new AdapterIv.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (imgPathList.size()==3)return;

                if (position==imgPathList.size()-1){
                    ToastUtils.showShort("add");
                    onClickTake(getTakePhoto());
                }
            }
        });

    }
    @OnClick({ R.id.bt_select_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.bt_select_iv:
//                cropOptions = new CropOptions.Builder().setAspectX(50).setAspectY(50)
//                        .setWithOwnCrop(true)
//                        .create();
//                CompressConfig compressConfig = new CompressConfig.Builder()
//                        .setMaxSize(50).setMaxPixel(50)
//                        .enableReserveRaw(true)
//                       .create();
//                getTakePhoto().onEnableCompress(compressConfig,true);//压缩条件
//                getTakePhoto().onPickMultipleWithCrop(3, cropOptions);

                onClickTake(getTakePhoto());

                break;
        }
    }
    public void onClickTake(TakePhoto takePhoto) {
        configCompress(takePhoto);
        cropOptions = new CropOptions.Builder().setAspectX(50).setAspectY(50)//裁剪配置
                .setWithOwnCrop(true)
                .create();
        takePhoto.onPickMultipleWithCrop(3,cropOptions);//根据需求这里面放最大图片数 一张图片takePhoto.onPickFromGallery();
    }

    private void configCompress(TakePhoto takePhoto) {//压缩配置
        int maxSize = Integer.parseInt("409600");//最大 压缩
        int width = Integer.parseInt("800");//宽
        int height = Integer.parseInt("800");//高
        CompressConfig config;
        config = new CompressConfig.Builder().setMaxSize(maxSize)
                .setMaxPixel(width >= height ? width : height)

                .enableReserveRaw(false)//拍照压缩后是否显示原图
                .create();
        takePhoto.onEnableCompress(config, false);//是否显示进度条

    }
    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        imgPathList.clear();
        for (int i = 0; i <result.getImages().size() ; i++) {
            imgPathList.add(result.getImages().get(i).getCompressPath());
        }
        adapterIv.setList(imgPathList);
        if (imgPathList.size()==3) adapterIv.setAddIvIsShow(false);
        adapterIv.notifyDataSetChanged();
//        Glide.with(getApplicationContext()).load(result.getImages().get(0).getCompressPath()).into(ivTest);

    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }






}
