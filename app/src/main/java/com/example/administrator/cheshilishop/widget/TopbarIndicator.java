package com.example.administrator.cheshilishop.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 20160728 on 2016/7/28.
 */
public class TopbarIndicator extends LinearLayout {

    private Paint mPaint;

    private Path mPath;

    private int LineWidth;

    private int mInitTranslationX;//画布初始化偏移位置

    private int mTranslationX;//随手指移动的偏移位置

    private int mTabVisibleCount = 1;//可见Tab的数量

    private static final int DEFAULT_VISIBLETAB_COUNT = 4;//默认可见Tab的数量

    private static final int DEFAULT_TEXT_COLOR = 0x99000000;
    private static final int LIGHT_TEXT_COLOR = 0xFFF7564C;

    private ViewPager mViewPager;

    private List<String> titles;


    public TopbarIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        //获取可见tab数量的属性
       /* TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyViewPagerIndicator);

        mTabVisibleCount = a.getInt(R.styleable.MyViewPagerIndicator_visible_tab_count,DEFAULT_VISIBLETAB_COUNT);
        if(mTabVisibleCount<=0){
            mTabVisibleCount = DEFAULT_VISIBLETAB_COUNT;
        }

        a.recycle();*/


        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(20);
        mPaint.setColor(Color.parseColor("#3F51B5"));
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        canvas.save();
        canvas.translate(mInitTranslationX+mTranslationX,getHeight());
        canvas.drawPath(mPath,mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    //在这里设置布局可见tab的数量(当布局文件加载完毕调用该方法)
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int cCount = getChildCount();
        if(cCount==0) return;
        for(int i = 0;i < cCount; i++){
            View view = getChildAt(i);
            LayoutParams lp = (LayoutParams) view.getLayoutParams();
            lp.weight = 0;
            lp.width = getScreenWidth()/mTabVisibleCount;
            view.setLayoutParams(lp);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //mInitTranslationX = 0;//初始化偏移量
        LineWidth = w / mTabVisibleCount;
        initLine();
    }

    //初始化下划线
    private void initLine() {
        mPath = new Path();
        mPath.moveTo(0,0);
        mPath.lineTo(LineWidth,0);
        mPath.close();//路径闭合
    }

    public void scroll(int position,float offset){
        int tabWidth  = getWidth() / mTabVisibleCount;
        mTranslationX = (int)(tabWidth*(offset+position));

        //容器移动，在tab处于移动至最后一个时
        if(position >= (mTabVisibleCount-2)&&offset > 0&& getChildCount()>mTabVisibleCount){
            if(mTabVisibleCount!=1){
                this.scrollTo((position-(mTabVisibleCount-2))*tabWidth +(int)(tabWidth*offset),0);
            }else {
                this.scrollTo(position*tabWidth +(int)(tabWidth*offset),0);
            }

        }

        //偏移量发生改变,重绘三角形
        invalidate();
    }

    /**
     * 获取手机屏幕宽度
     * @return
     */
    public int getScreenWidth(){
        WindowManager mg = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics  = new DisplayMetrics();
        mg.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    //设置可见tab的数量
    public void setVisibleTabCount(int visibleCount){
        mTabVisibleCount = visibleCount;
    }

    //设置标题
    public void setTitles(List<String> titleList){

        if(titleList!=null&&titleList.size()>0){
            this.removeAllViews();
            this.titles = titleList;
            for(String title:titles){
                this.addView(generaterTextView(title));
            }
        }
        setLightTextColor(0);//设置第一个默认选中
        seTabclickListener();//给Tab添加点击事件
    }

    //创建并添加子view
    private TextView generaterTextView(String title) {
        TextView tv = new TextView(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.width = getScreenWidth()/mTabVisibleCount;
        tv.setText(title);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(DEFAULT_TEXT_COLOR);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        tv.setLayoutParams(lp);
        return tv;
    }

    //设置Viewpager
    public void setViewpager(ViewPager viewpager){
        mViewPager = viewpager;

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position,positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                setLightTextColor(position);
                if(viewpagerChangeListener!=null){
                    viewpagerChangeListener.onPageSelected(position);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //设置选中字体的颜色
    public void setLightTextColor(int posiotion){
        View view = this.getChildAt(posiotion);
        initTextColor();
        if(view instanceof TextView){
            ((TextView) view).setTextColor(LIGHT_TEXT_COLOR);
        }
    }

    //初始化所有的字体颜色
    public void initTextColor(){
       int cCount = this.getChildCount();
        for(int i = 0; i < cCount; i ++){
            View view = this.getChildAt(i);
            if(view instanceof TextView){
                ((TextView) view).setTextColor(DEFAULT_TEXT_COLOR);
            }
        }
    }

    public void seTabclickListener(){
        int cCount = this.getChildCount();
        for(int i = 0; i < cCount; i ++){
            final int j = i;
            View view = this.getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }

    public interface MyViewpagerChangeListener{
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
        void onPageSelected(int position);
        void onPageScrollStateChanged(int state);
    }

    private MyViewpagerChangeListener viewpagerChangeListener;

    public void setMyViewPagerChangeListener(MyViewpagerChangeListener listener){
        viewpagerChangeListener = listener;
    }

}
