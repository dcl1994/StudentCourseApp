package com.siyann.studentcourseapp.Activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.siyann.studentcourseapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import util.CustomDialog;
import util.HttpUrl;
import util.JellyInterpolator;
import util.MyApplication;
import util.UploadUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mBtnLogin;
    private View progress;
    private View mInputLayout;
    private View mView;
    private float mWidth, mHeight;
    private LinearLayout mName, mPsw;
    private Context mContext;
    private MyAsyncTask mytask;
    Map<String, String> map = new HashMap();
    Handler handler = new Handler();
    JSONArray jsonArray;
    /**
     * 登录界面
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        initView();
        mContext = MyApplication.getContext();  //获取context

        map.put("phone", "13148700419");
        map.put("password", "be7557be0c67b9abdaf91fab44b30717");
//        map.put("username", "");
//        map.put("password", "");
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
//        mName = (LinearLayout) findViewById(R.id.input_layout_name);
//        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);

        mName = (LinearLayout) findViewById(R.id.layout_username);
        mPsw = (LinearLayout) findViewById(R.id.layout_password);

//        mView = findViewById(R.id.LineView);
        mBtnLogin.setOnClickListener(this);
    }
    /**
     * login的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        /**
         * 登录的按钮点击后修改
         */
        mBtnLogin.setTextColor(getResources().getColor(R.color.bgcolor));
        mBtnLogin.setBackgroundColor(getResources().getColor(R.color.bgcolor));


        //计算出控件的宽与高
        mWidth = mBtnLogin.getMeasuredWidth();
        mHeight = mBtnLogin.getMeasuredHeight();
        //隐藏输入框
        mName.setVisibility(View.INVISIBLE);
        mPsw.setVisibility(View.INVISIBLE);
//        mView.setVisibility(View.INVISIBLE);
        inputAnimator(mInputLayout, mWidth, mHeight);
    }

    /**
     * 输入框的动画效果
     */
    private void inputAnimator(final View view, float w, float h) {
        AnimatorSet set = new AnimatorSet();
        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout, "scaleX", 1f, 0.5f);
        set.setDuration(600);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 * @param animation
                 */
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * 出现进度动画
     */
    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view, animator, animator2);
        animator3.setDuration(600);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();
        //实现登录
        handler.postDelayed(new Thread() {
            @Override
            public void run() {
                //开启一个异步任务
                mytask = new MyAsyncTask();
                mytask.execute(map);
            }
        }, 1000);
    }

    //使用异步加载的方式实现登录
    class MyAsyncTask extends AsyncTask<Map, Integer, JSONObject> {
        @Override
        protected JSONObject doInBackground(Map... params) {
            String result = UploadUtil.postGetJson(HttpUrl.login, params[0]);    //发起网络请求，返回json数据，对json进行解析
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }
        //进行UI操作的方法
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                jsonArray = jsonObject.getJSONArray("JsonArry");
                Log.e("jsonArray", jsonArray + "");
                if (jsonArray.getJSONObject(0).opt("message").equals("success")) {   //如果登录成功就跳转
                    Toast.makeText(mContext, "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                } else {
                    //登录失败给出提示
                    String hint = jsonArray.getJSONObject(0).opt("description").toString();
                    // Toast.makeText(mContext, hint, Toast.LENGTH_SHORT).show();
                    CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
                    builder.setTitle("提示");
                    builder.setMessage(hint);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    builder.create().show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(jsonObject);
        }
    }
}
