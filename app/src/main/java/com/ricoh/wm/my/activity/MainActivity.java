package com.ricoh.wm.my.activity;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.ricoh.wm.my.R;
import com.ricoh.wm.my.adapter.MyFragmentAdapter;
import com.ricoh.wm.my.fragment.Fragment_four;
import com.ricoh.wm.my.fragment.Fragment_one;
import com.ricoh.wm.my.fragment.Fragment_three;
import com.ricoh.wm.my.fragment.Fragment_two;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.textView_home)
    TextView textMain;
    @Bind(R.id.imageView_open)
    ImageView imageViewOpen;
    @Bind(R.id.nav)
    NavigationView navigationView;
    @Bind(R.id.activity_na)
    DrawerLayout drawerLayout;
    @Bind(R.id.viewPager_index)
    ViewPager viewPagerIndex;
    @Bind(R.id.radioGroup_index)
    RadioGroup radioGroupIndex;

    private List<Fragment> fragmentList;


    private SystemBarTintManager tintManager;
    private int id;



    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initWindow();
        initData();
        initAdapter();
        initAction();

        initBroadCast();

        System.out.println("当前手机的android版本号" + Build.VERSION.RELEASE);

    }

    private void initBroadCast() {
        //构建意图过滤器
        intentFilter = new IntentFilter();
        //添加要接收的广播
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        //注册广播传入一个BroadcastReceiver子类实现onReceive方法，还有意图过滤器
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    private void initAdapter() {
        viewPagerIndex.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(), fragmentList));
    }

    private void initData() {
        fragmentList = new ArrayList<>();
        Fragment fragment_one = new Fragment_one();
        Fragment fragment_two = new Fragment_two();
        Fragment fragment_three = new Fragment_three();
        Fragment fragment_four = new Fragment_four();


        fragmentList.add(fragment_one);
        fragmentList.add(fragment_two);
        fragmentList.add(fragment_three);
        fragmentList.add(fragment_four);
    }

    private void initAction() {

        radioGroupIndex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        viewPagerIndex.setCurrentItem(0);//此方法用于将切换到viewpager的第几个页面
                        break;
                    case R.id.rb_category:
                        viewPagerIndex.setCurrentItem(1);//这4个id顺序和不能乱，要和布局中的顺序一样
                        break;
                    case R.id.rb_new:
                        viewPagerIndex.setCurrentItem(2);
                        break;
                    case R.id.rb_mine:
                        viewPagerIndex.setCurrentItem(3);
                        break;
                }
            }
        });
        viewPagerIndex.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * 滑动监听器OnPageChangeListener
             *  OnPageChangeListener这个接口需要实现三个方法：onPageScrollStateChanged，onPageScrolled ，onPageSelected
             *      1、onPageScrollStateChanged(int arg0) 此方法是在状态改变的时候调用。
             *          其中arg0这个参数有三种状态（0，1，2）
             *              arg0 ==1的时表示正在滑动，arg0==2的时表示滑动完毕了，arg0==0的时表示什么都没做
             *              当页面开始滑动的时候，三种状态的变化顺序为1-->2-->0
             *      2、onPageScrolled(int arg0,float arg1,int arg2) 当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直被调用。
             *          其中三个参数的含义分别为：
             *              arg0 :当前页面，及你点击滑动的页面
             *              arg1:当前页面偏移的百分比
             *              arg2:当前页面偏移的像素位置
             *      3、onPageSelected(int arg0) 此方法是页面跳转完后被调用，arg0是你当前选中的页面的Position（位置编号）
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //当Viewpager页面改变时回调，返回值当前被选择的页面位置
            @Override
            public void onPageSelected(int position) {
                //获取传入参数（index 序号） 来获取子控件
                // ViewPager的position和RadioBtton的index是一一对应的。
                RadioButton radioButton = (RadioButton) radioGroupIndex.getChildAt(position);//RadioGroup通过序号找到RaidoBtton
                //将从索引得到的radioButton设置选中状态，与viewPager滑动监听绑定
                radioGroupIndex.check(radioButton.getId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * intent 启动MainActivity
     *
     * @param context
     * @param userName
     */
    public static void actionStart(Context context, String userName) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("userName", userName);
        context.startActivity(intent);

    }

    private void initWindow() {//初始化窗口属性，让状态栏和导航栏透明

        this.textMain.setText(this.getIntent().getStringExtra("userName"));

        View headerView = navigationView.getHeaderView(0);//获取头布局
        imageViewOpen.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                id = item.getItemId();
               /* switch (id) {
                    case R.id.favorite:
                        Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                        navigationView.getMenu().findItem(id).setChecked(false);
                        System.out.println(item.isChecked());
                        break;
                    case R.id.wallet:
                        Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                        navigationView.getMenu().findItem(id).setChecked(false);
                        System.out.println(item.isChecked());
                        break;
                    case R.id.photo:
                        Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                        navigationView.getMenu().findItem(id).setChecked(false);
                        System.out.println(item.isChecked());
                        break;
                    case R.id.file:
                        Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                        navigationView.getMenu().findItem(id).setChecked(false);
                        System.out.println(item.isChecked());
                        break;
                    case R.id.setting:
                        Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                        navigationView.getMenu().findItem(id).setChecked(false);
                        System.out.println(item.isChecked());
                        break;
                    case R.id.file1:
                        Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                        navigationView.getMenu().findItem(id).setChecked(false);
                        System.out.println(item.isChecked());
                        break;
                    case R.id.setting1:
                        Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                        navigationView.getMenu().findItem(id).setChecked(false);
                        System.out.println(item.isChecked());
                        break;
                    case R.id.file2:
                        Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                        navigationView.getMenu().findItem(id).setChecked(false);
                        System.out.println(item.isChecked());
                        break;
                    case R.id.setting2:
                        Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                        navigationView.getMenu().findItem(id).setChecked(false);
                        System.out.println(item.isChecked());
                        break;
                }*/


//                System.out.println(item.isChecked());
                Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(GravityCompat.START);
//                navigationView.getMenu().findItem(item.getItemId()).setChecked(false);

//                item.setCheckable(false);
                return true;


            }
        });

        drawerLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    System.out.println("获取焦点");
                    drawerLayout.closeDrawer(navigationView);
                    imageViewOpen.setBackgroundResource(R.drawable.ic_action_database);
                    RotateAnimation rotateAnimation = new RotateAnimation(270, //初始角度
                            360,//结束角度
                            Animation.RELATIVE_TO_SELF, //指定在x轴方向相对于自身对象
                            0.5f, //指定X轴偏移的百分比
                            Animation.RELATIVE_TO_SELF,//指定在x轴方向相对于自身对象
                            0.5f//指定y轴偏移的百分比
                    );
                    rotateAnimation.setDuration(50);

                    //想让动画执行完毕后固定在动画结束的那一帧上可以设置
                    rotateAnimation.setFillAfter(true);
                    imageViewOpen.startAnimation(rotateAnimation);

                    if (id != 0) {
                        navigationView.getMenu().findItem(id).setChecked(false);
                    }

                } else {
                    System.out.println("失去焦点");
                    drawerLayout.openDrawer(navigationView);
                    imageViewOpen.setBackgroundResource(R.mipmap.down);
                    RotateAnimation rotateAnimation = new RotateAnimation(0, //初始角度
                            90,//结束角度
                            Animation.RELATIVE_TO_SELF, //指定在x轴方向相对于自身对象
                            0.5f, //指定X轴偏移的百分比
                            Animation.RELATIVE_TO_SELF,//指定在x轴方向相对于自身对象
                            0.5f//指定y轴偏移的百分比
                    );
                    rotateAnimation.setDuration(50);
                    rotateAnimation.setFillAfter(true);
                    imageViewOpen.startAnimation(rotateAnimation);

                }
            }
        });


       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//透明导航栏
            tintManager = new SystemBarTintManager(this);
            int statusColor = Color.parseColor("#1976d2");
            tintManager.setStatusBarTintColor(statusColor);
            tintManager.setStatusBarTintEnabled(true);
        }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_open://点击菜单，跳出侧滑菜单
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
//                    imageViewOpen.setBackgroundResource(R.drawable.ic_action_database);
                   /* RotateAnimation rotateAnimation=new RotateAnimation(270, //初始角度
                            360,//结束角度
                            Animation.RELATIVE_TO_SELF, //指定在x轴方向相对于自身对象
                            0.5f, //指定X轴偏移的百分比
                            Animation.RELATIVE_TO_SELF,//指定在x轴方向相对于自身对象
                            0.5f//指定y轴偏移的百分比
                    );
                    rotateAnimation.setDuration(100);

                    //想让动画执行完毕后固定在动画结束的那一帧上可以设置
                    rotateAnimation.setFillAfter(true);
                    imageViewOpen.startAnimation(rotateAnimation);*/



                } else {
                    drawerLayout.openDrawer(navigationView);
//                    imageViewOpen.setBackgroundResource(R.mipmap.down);
                  /*  RotateAnimation rotateAnimation=new RotateAnimation(0, //初始角度
                            90,//结束角度
                            Animation.RELATIVE_TO_SELF, //指定在x轴方向相对于自身对象
                            0.5f, //指定X轴偏移的百分比
                            Animation.RELATIVE_TO_SELF,//指定在x轴方向相对于自身对象
                            0.5f//指定y轴偏移的百分比
                    );
                    rotateAnimation.setDuration(100);
                    //想让动画执行完毕后固定在动画结束的那一帧上可以设置
                    rotateAnimation.setFillAfter(true);
                    imageViewOpen.startAnimation(rotateAnimation);*/

                }
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销广播接收者
        unregisterReceiver(networkChangeReceiver);
    }

    /**
     * 网络切换的广播接收者
     */
    class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

            if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) {

                Toast.makeText(context, "Network is available", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Network is unavailable", Toast.LENGTH_SHORT).show();
            }

        }
    }
}

