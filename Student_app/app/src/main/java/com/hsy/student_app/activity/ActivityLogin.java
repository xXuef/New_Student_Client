package com.hsy.student_app.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hsy.student_app.R;
import com.hsy.student_app.bean.User;
import com.hsy.student_app.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hsy.student_app.helper.Contact.USER;

public class ActivityLogin extends AppCompatActivity {

    @BindView(R.id.edit_admin)
    EditText editAdmin;
    @BindView(R.id.edit_pw)
    EditText editPw;
    @BindView(R.id.iv_show_pw)
    ImageView ivShowPw;
    @BindView(R.id.tv_pw_admin_erro)
    TextView tvPwAdminErro;
    @BindView(R.id.tv_forget)
    TextView tvForget;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.tv_phone_login)
    TextView tvPhoneLogin;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.iv_show_pw, R.id.tv_pw_admin_erro, R.id.tv_forget, R.id.bt_login, R.id.tv_phone_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_show_pw:
                break;
            case R.id.tv_pw_admin_erro:
                break;
            case R.id.tv_forget:
                break;
            case R.id.bt_login:

                requestData();
                if (isEmptyEt()) {
                    if (editAdmin.getText().toString().equals(user.getName())&&editPw.getText().toString().equals(user.getPassword())){
                        startActivity(new Intent(ActivityLogin.this, ActivityMain.class));
                        SharedPreferences sp = getSharedPreferences(USER, Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putBoolean("isLogin",true);
                        edit.commit();
                        finish();
                    }
                }
                break;
            case R.id.tv_phone_login:
                break;
        }
    }

    private void requestData() {
        {
            String json = "{\n" +
                    "\t\"id\": 1,\n" +
                    "\t\"token\": 1001001,\n" +
                    "\t\"state\": 0,\n" +
                    "\t\"name\": \"123\",\n" +
                    "\t\"password\": \"123\"\n" +
                    "}";

            JSONObject jsonObject;

            try {
                user = new User();
                jsonObject = new JSONObject(json);
                user.setId(jsonObject.getInt("id"));
                user.setState( jsonObject.getInt("state"));
                user.setToken(jsonObject.getInt("token"));
                user.setName(jsonObject.getString("name"));
                user.setPassword(jsonObject.getString("password"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  @Data 2019/4/20 11:07
     *  @deprecated  判断是否为空
     */
    private boolean isEmptyEt() {
        if (TextUtils.isEmpty(editAdmin.getText()) || TextUtils.isEmpty(editPw.getText())) {
            ToastUtils.showShort("请输入用户名或者密码");
            return false;
        }else {
            return true;
        }
    }




}
