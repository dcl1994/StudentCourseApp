package com.siyann.studentcourseapp.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.siyann.studentcourseapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import util.MyApplication;
import util.PhotoAdapter;
import widget.Photo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private DrawerLayout mDrawerLayout;
    private long exitTime = 0;

    private Photo[] photos={
            new Photo("图片1",R.drawable.a),
            new Photo("图片2",R.drawable.b),
            new Photo("图片3",R.drawable.c),
            new Photo("图片4",R.drawable.d),
            new Photo("图片5",R.drawable.e),
            new Photo("图片6",R.drawable.f),
    };

    private List<Photo> photoList=new ArrayList<>();
    private PhotoAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPhoto();
        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new PhotoAdapter(photoList);
        recyclerView.setAdapter(adapter);

        mContext = MyApplication.getContext();  //获取context

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }
        /**
         * 侧滑页面的各项子菜单的点击事件
         */
        navView.setCheckedItem(R.id.nav_call);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_call:
                        Toast.makeText(MainActivity.this, "打电话", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_friends:
                        Toast.makeText(MainActivity.this, "聊天", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_photo:
                        Toast.makeText(MainActivity.this, "拍照", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_select:
                        Toast.makeText(MainActivity.this, "查找", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        });


        /**
         * FloatingActionButton(悬浮按钮)的点击事件
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 使用Snackbar进行提示
                 */
                Snackbar.make(v, "Data Delete", Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "Date restored", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });


        /**
         * swipeRefreshLayout
         */

        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swip_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                refreshPhoto();
            }
        });

    }

    private void refreshPhoto() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initPhoto();
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

            }
        }).start();

    }

    private void initPhoto() {
        photoList.clear();
        for (int i=0;i<50;i++){
            Random random=new Random();
            int index=random.nextInt(photos.length);
            photoList.add(photos[index]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    /**
     * 重写返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                android.os.Process.killProcess(android.os.Process.myPid());
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.nav_call:
//                Toast.makeText(MainActivity.this, "打电话", Toast.LENGTH_SHORT).show();
//            break;
//
//            case R.id.nav_friends:
//                Toast.makeText(MainActivity.this, "聊天", Toast.LENGTH_SHORT).show();
//                break;
        }
    }
}
