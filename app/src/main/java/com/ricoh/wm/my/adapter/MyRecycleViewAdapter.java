package com.ricoh.wm.my.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ricoh.wm.my.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by 2017063001 on 2018/4/20.
 */
public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.ViewHolder> {

    //LineData数据集合
//    private List<Integer> _list;
    private List<String> _list;
    private Context context;
    private MyClickListener mListener;

    private LayoutInflater mInflater;

    private Bitmap mBitmap; //

    private LruCache<String, BitmapDrawable> mMemoryCache;//

    public MyRecycleViewAdapter(Context context, List<String> list, MyClickListener mListener) {
        _list = list;
        this.context = context;
        this.mListener = mListener;
    }

    public MyRecycleViewAdapter(Context context, List<String> list) {
        _list = list;
        this.context = context;

        mInflater = LayoutInflater.from(context);
        //默认显示的图片
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        //计算内存，并且给Lrucache 设置缓存大小
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 6;
        mMemoryCache = new LruCache<String, BitmapDrawable>(cacheSize) {
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                return value.getBitmap().getByteCount();
            }
        };

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        CardView cardView;

        public ViewHolder(View view) {
            super(view);
//            imageView = (ImageView) view.findViewById(R.id.recycleView_item);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycleview_item, null);
        ViewHolder holder = new ViewHolder(view);
        holder.imageView = (ImageView) view.findViewById(R.id.recycleView_item);
        holder.cardView = (CardView) view.findViewById(R.id.cardview);
        return holder;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.cardView.setRadius(50);//设置图片圆角的半径大小

        holder.cardView.setCardElevation(10);//设置阴影部分大小

        holder.cardView.setContentPadding(20, 20, 20, 20);//设置图片距离阴影大小

        holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorGray));


        //先设置图片占位符
//        holder.imageView.setImageDrawable(context.getDrawable(R.mipmap.ic_launcher));
//        holder.imageView.setImageResource(_list.get(position));
        Picasso.with(context).load(_list.get(position))
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imageView);


        Picasso.with(context).setIndicatorsEnabled(true);


        /*String imageUrl= _list.get(position)+"";
        BitmapDrawable drawable = getBitmapDrawableFromMemoryCache(imageUrl);
        if (drawable != null){
            holder.imageView.setImageDrawable(drawable);
        }else if (cancelPotentialTask(imageUrl,holder.imageView)){
            //执行下载操作
            DownLoadTask task = new DownLoadTask(holder.imageView);
            AsyncDrawable asyncDrawable = new AsyncDrawable(context.getResources(),mBitmap,task);
            holder.imageView.setImageDrawable(asyncDrawable);
            task.execute(imageUrl);
        }*/

    }


    @Override
    public int getItemCount() {
//        System.out.println("显示的条目数" + _list.size());
        return _list.size();
    }

    /**
     * 用于回调的抽象类
     * <p/>
     * 控制按钮点击不退出对话框
     */
    public static abstract class MyClickListener implements View.OnClickListener {
        /**
         * 基类的onClick方法
         */
        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }

        public abstract void myOnClick(int position, View v);
    }


    /**
     * 检查复用的ImageView中是否存在其他图片的下载任务，如果存在就取消并且返回ture 否则返回 false
     *
     * @param imageUrl
     * @param imageView
     * @return
     */
    private boolean cancelPotentialTask(String imageUrl, ImageView imageView) {
        DownLoadTask task = getDownLoadTask(imageView);
        if (task != null) {
            String url = task.url;
            if (url == null || !url.equals(imageUrl)) {
                task.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }


    /**
     * 從缓存中获取已存在的图片
     *
     * @param imageUrl
     * @return
     */
    private BitmapDrawable getBitmapDrawableFromMemoryCache(String imageUrl) {
        return mMemoryCache.get(imageUrl);
    }

    /**
     * 添加图片到缓存中
     *
     * @param imageUrl
     * @param drawable
     */
    private void addBitmapDrawableToMemoryCache(String imageUrl, BitmapDrawable drawable) {
        if (getBitmapDrawableFromMemoryCache(imageUrl) == null) {
            mMemoryCache.put(imageUrl, drawable);
        }
    }

    /**
     * 获取当前ImageView 的图片下载任务
     *
     * @param imageView
     * @return
     */
    private DownLoadTask getDownLoadTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                return ((AsyncDrawable) drawable).getDownLoadTaskFromAsyncDrawable();
            }
        }
        return null;
    }


    /**
     * 新建一个类 继承BitmapDrawable
     * 目的： BitmapDrawable 和DownLoadTask建立弱引用关联
     */
    class AsyncDrawable extends BitmapDrawable {
        private WeakReference<DownLoadTask> downLoadTaskWeakReference;

        public AsyncDrawable(Resources resources, Bitmap bitmap, DownLoadTask downLoadTask) {
            super(resources, bitmap);
            downLoadTaskWeakReference = new WeakReference<DownLoadTask>(downLoadTask);
        }

        private DownLoadTask getDownLoadTaskFromAsyncDrawable() {
            return downLoadTaskWeakReference.get();
        }
    }

    /**
     * 异步加载图片
     * DownLoadTash 和 ImagaeView建立弱引用关联。
     */
    class DownLoadTask extends AsyncTask<String, Void, BitmapDrawable> {
        String url;
        private WeakReference<ImageView> imageViewWeakReference;

        public DownLoadTask(ImageView imageView) {
            imageViewWeakReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected BitmapDrawable doInBackground(String... params) {
            url = params[0];
//            Bitmap bitmap = downLoadBitmap(url);
            Bitmap b = BitmapFactory.decodeResource(context.getResources(), Integer.parseInt(url));
            BitmapDrawable drawable = new BitmapDrawable(context.getResources(), b);
            addBitmapDrawableToMemoryCache(url, drawable);
            return drawable;
        }

        /**
         * 验证ImageView 中的下载任务是否相同 如果相同就返回
         *
         * @return
         */
        private ImageView getAttachedImageView() {
            ImageView imageView = imageViewWeakReference.get();
            if (imageView != null) {
                DownLoadTask task = getDownLoadTask(imageView);
                if (this == task) {
                    return imageView;
                }
            }
            return null;
        }

        /**
         * 下载图片 这里使用google 推荐使用的OkHttp
         *
         * @param
         * @return
         */
     /*   private Bitmap downLoadBitmap(String url) {
            Bitmap bitmap = null;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                bitmap = BitmapFactory.decodeStream(response.body().byteStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }*/
        @Override
        protected void onPostExecute(BitmapDrawable drawable) {
            super.onPostExecute(drawable);
            ImageView imageView = getAttachedImageView();
            if (imageView != null && drawable != null) {
                imageView.setImageDrawable(drawable);
            }
        }


    }
}
