package com.ricoh.wm.my.welcome;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ricoh.wm.my.R;
import com.ricoh.wm.my.activity.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {

    //引导页的viewpager
    private ViewPager viewPager_in;
    //放icon切换小点的布局
    private LinearLayout linerLayout_icon;

    private Button to_login;
    private PagerAdapter mAdapter;//需要PagerAdapter适配器
    /**
     * 声明存储图标的ImageView数组
     */
    private ImageView[] imageView_icons;

    private List<View> mViews=new ArrayList<>();//准备数据源
    //放icon图标的数组 之前的索引位
    private int prePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();

        initData();
    }

    private void initData() {


        mAdapter=new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view=mViews.get(position);//初始化适配器，将view加到container中
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                View view=mViews.get(position);
                container.removeView(view);//将view从container中移除
            }

            @Override
            public int getCount() {
                return mViews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;//判断当前的view是我们需要的对象
            }
        };

        viewPager_in.setAdapter(mAdapter);




        //linearLayout_icons.getChildCount():获取线性布局中的子元素个数i
        int count=this.linerLayout_icon.getChildCount();
        this.imageView_icons=new ImageView[count];
        for(int i=0;i<count;i++){
            ImageView imageView_icon=(ImageView) this.linerLayout_icon.getChildAt(i);
            //给ImageView设置一个标志值
            //imageView_icon.setTag(i);

            imageView_icon.setId(i);
            imageView_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // int currentItem=Integer.parseInt(v.getTag().toString());
                    //设置ViewPager切换到指定页面
                    //viewPager.setCurrentItem(currentItem);

                    viewPager_in.setCurrentItem(v.getId());
                }
            });

            this.imageView_icons[i]=imageView_icon ;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        this.viewPager_in = (ViewPager) this.findViewById(R.id.viewPager_in);

        this.linerLayout_icon = (LinearLayout) this.findViewById(R.id.linerLayout_icon);



        //给ViewPager注册滑动监听器
        this.viewPager_in.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * 当ViewPager滚动过程中自动调用的方法
             * @param position 当前页面的索引位
             * @param positionOffset 滚动的偏移量,[0-1) 一旦这个值大于0.5后才可能滑动目标页面,否则会回退到之前的页面
             * @param positionOffsetPixels 以像素为单位指定滚动的距离
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                System.out.println("===onPageScrolled(int position="+position+", float positionOffset="+positionOffset+", int positionOffsetPixels="+positionOffsetPixels+")===");
            }

            /**
             * 当滚动到目标页面后自动调用的方法
             * @param position 新页面的索引位
             */
            @Override
            public void onPageSelected(int position) {
                //将目标页面的图片设置为彩色图标
                imageView_icons[position].setImageResource(R.drawable.radio_on);
                //将之前页面的图标还原成灰色图标
                imageView_icons[prePosition].setImageResource(R.drawable.radio_off);
                //将当前位置作为之前页面的位置
                prePosition=position;
                System.out.println("===onPageSelected(int position="+position+")==");
            }

            /**
             * 当页面的滚动状态发生变化时自动调用的方法
             * @param state 滚动状态
             *               ViewPager.SCROLL_STATE_IDLE=0:当前ViewPager处于空闲状态
             *              ViewPager.SCROLL_STATE_DRAGGING=1:当前ViewPager正在被用户拖拽
             *              ViewPager.SCROLL_STATE_SETTLING=2:表示ViewPager 准备滑动到最终位置的过程中
             *
             */
            @Override
            public void onPageScrollStateChanged(int state) {

                System.out.println("===onPageScrollStateChanged(int state="+state+")==");
            }
        });



        LayoutInflater inflater=LayoutInflater.from(this);//将每个xml文件转化为View
        View guideOne=inflater.inflate(R.layout.guidance01, null);//每个xml中就放置一个imageView
        View guideTwo=inflater.inflate(R.layout.guidance02,null);
        View guideThree=inflater.inflate(R.layout.guidance03,null);

        mViews.add(guideOne);//将view加入到list中
        mViews.add(guideTwo);
        mViews.add(guideThree);

        this.to_login= (Button) guideThree.findViewById(R.id.to_Login);
        this.to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GuideActivity.this,LoginActivity.class);
                startActivity(i);
                GuideActivity.this.finish();
            }
        });
    }
}
