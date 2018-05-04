# GoodsInfoPage
##### 仿京东、天猫app的商品详情页的布局架构, 以及功能实现
##### 类似的架构可以自行修改, 代码中有注释
___
![Travis CI](https://travis-ci.org/DreaminginCodeZH/Douya.svg)
##### 有需要做电商类app的可以看看, 首先先看看效果实现

![效果实现](https://github.com/hexianqiao3755/GoodsInfoPage/blob/master/art/demo.gif)

也可以[点击这里下载](https://github.com/hexianqiao3755/GoodsInfoPage/blob/master/art/app-debug.apk)

## 配置
在项目`build.gradle`中添加依赖：
```
allprojects {
    repositories {
        jcenter()
    }
}

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.facebook.fresco:fresco:0.9.0'
    compile 'com.gxz.pagerslidingtabstrip:library:1.3'
    compile 'com.bigkoo:convenientbanner:2.0.5'
}
```

由于代码量过多, 就不一一讲解只介绍几个核心的自定义控件

##### 最外层的布局文件

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
     <!-- 顶部标题 -->
    <LinearLayout
        android:id="@+id/ll_title_root"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="#ec0f38"
        android:orientation="vertical">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="44dp"
            android:orientation="horizontal">

            <LinearLayout
                android:id="@+id/ll_back"
                android:layout_width="wrap_content"
                android:layout_height="match_parent"
                android:paddingLeft="15dp">

                <ImageView
                    android:id="@+id/iv_back"
                    android:layout_width="22dp"
                    android:layout_height="22dp"
                    android:layout_gravity="center_vertical"
                    android:src="@mipmap/address_come_back" />
            </LinearLayout>

            <LinearLayout
                android:layout_width="0dp"
                android:layout_height="match_parent"
                android:layout_weight="1"
                android:gravity="center">

                <!-- 商品、详情、评价切换的控件 -->
                <com.gxz.PagerSlidingTabStrip
                    android:id="@+id/psts_tabs"
                    android:layout_width="wrap_content"
                    android:layout_height="32dp"
                    android:layout_gravity="center"
                    android:textColor="#ffffff"
                    android:textSize="15sp"
                    app:pstsDividerColor="@android:color/transparent"
                    app:pstsDividerPaddingTopBottom="0dp"
                    app:pstsIndicatorColor="#ffffff"
                    app:pstsIndicatorHeight="2dp"
                    app:pstsScaleZoomMax="0.0"
                    app:pstsShouldExpand="false"
                    app:pstsSmoothScrollWhenClickTab="false"
                    app:pstsTabPaddingLeftRight="12dp"
                    app:pstsTextAllCaps="false"
                    app:pstsTextSelectedColor="#ffffff"
                    app:pstsUnderlineHeight="0dp" />

                <TextView
                    android:id="@+id/tv_title"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="图文详情"
                    android:textColor="#ffffff"
                    android:textSize="15sp"
                    android:visibility="gone" />
            </LinearLayout>
        </LinearLayout>
    </LinearLayout>

     <!-- 功能下面有介绍 -->
    <com.hq.hsmwan.widget.NoScrollViewPager
        android:id="@+id/vp_content"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1" />
</LinearLayout>

```

##### ItemWebView是`SlideDetailsLayout`的子View (SlideDetailsLayout代码太多, 放到了最后)

 * 功能为显示商品简介的`Webview`
 *  防止往上滑动时会直接滑动到第一个`View`
 * 实现滑动到`WebView`顶部时, 让父控件重新获得触摸事件

```
/**
 * 商品详情页底部的webview
 */
public class ItemWebView extends WebView {
    public float oldY;
    private int t;
    private float oldX;

    public ItemWebView(Context context) {
        super(context);
    }

    public ItemWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float Y = ev.getY();
                float Ys = Y - oldY;
                float X = ev.getX();

                //滑动到顶部让父控件重新获得触摸事件
                if (Ys > 0 && t == 0) {
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;

            case MotionEvent.ACTION_DOWN:
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                oldY = ev.getY();
                oldX = ev.getX();
                break;

            case MotionEvent.ACTION_UP:
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                break;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        this.t = t;
        super.onScrollChanged(l, t, oldl, oldt);
    }

}
```

##### ItemListView 也是`SlideDetailsLayout`的子View
 * 跟 `ItemWebView`功能大致一样
 * 当然这里不只是`ListView`可以, 其他的控件都可以写成这样来适配`SlideDetailsLayout`的滑动功能

```
/**
 * 商品详情页底部的ListView
 */
public class ItemListView extends ListView implements AbsListView.OnScrollListener {
    private float oldX, oldY;
    private int currentPosition;

    public ItemListView(Context context) {
        super(context);
        setOnScrollListener(this);
    }

    public ItemListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnScrollListener(this);
    }

    public ItemListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnScrollListener(this);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float Y = ev.getY();
                float Ys = Y - oldY;
                float X = ev.getX();
                int [] location = new int [2];
                getLocationInWindow(location);

                //滑动到顶部让父控件重新获得触摸事件
                if (Ys > 0 && currentPosition == 0) {
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;

            case MotionEvent.ACTION_DOWN:
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                oldY = ev.getY();
                oldX = ev.getX();
                break;

            case MotionEvent.ACTION_UP:
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                break;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        currentPosition = getFirstVisiblePosition();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
```

##### NoScrollViewPager为最外层的父布局
 * 当滑动到图文详情模块时, 能禁止掉`ViewPager`的滑动事件
 *  大概意思就是**滑动跟到了图文详情时不能直接向右滑动的方式切换到详情和评价模块**(参照京东淘宝功能)

```
/**
 * 提供禁止滑动功能的自定义ViewPager
 */
public class NoScrollViewPager extends ViewPager {
    private boolean noScroll = false;

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public NoScrollViewPager(Context context) {
        super(context);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

}
```

*  ###商品模块最外层的布局是一个自定义的ViewGroup名为`SlideDetailsLayout`
`SlideDetailsLayout`内容有两个`View`,  `mFrontView`(第一个`View`)和`mBehindView`(第二个`View`)
有两种状态, 状态设置为**close**就显示第一个商品数据`View`, **open**状态就显示第二个图文详情`View`

```
@SuppressWarnings("unused")
public class SlideDetailsLayout extends ViewGroup {

    /**
     * Callback for panel OPEN-CLOSE status changed.
     */
    public interface OnSlideDetailsListener {
        /**
         * Called after status changed.
         *
         * @param status {@link Status}
         */
        void onStatucChanged(Status status);
    }

    public enum Status {
        /** Panel is closed */
        CLOSE,
        /** Panel is opened */
        OPEN;

        public static Status valueOf(int stats) {
            if (0 == stats) {
                return CLOSE;
            } else if (1 == stats) {
                return OPEN;
            } else {
                return CLOSE;
            }
        }
    }

    private static final float DEFAULT_PERCENT = 0.2f;
    private static final int DEFAULT_DURATION = 300;

    private View mFrontView;
    private View mBehindView;

    private float mTouchSlop;
    private float mInitMotionY;
    private float mInitMotionX;


    private View mTarget;
    private float mSlideOffset;
    private Status mStatus = Status.CLOSE;
    private boolean isFirstShowBehindView = true;
    private float mPercent = DEFAULT_PERCENT;
    private long mDuration = DEFAULT_DURATION;
    private int mDefaultPanel = 0;

    private OnSlideDetailsListener mOnSlideDetailsListener;

    public SlideDetailsLayout(Context context) {
        this(context, null);
    }

    public SlideDetailsLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideDetailsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlideDetailsLayout, defStyleAttr, 0);
        mPercent = a.getFloat(R.styleable.SlideDetailsLayout_percent, DEFAULT_PERCENT);
        mDuration = a.getInt(R.styleable.SlideDetailsLayout_duration, DEFAULT_DURATION);
        mDefaultPanel = a.getInt(R.styleable.SlideDetailsLayout_default_panel, 0);
        a.recycle();

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    /**
     * Set the callback of panel OPEN-CLOSE status.
     *
     * @param listener {@link OnSlideDetailsListener}
     */
    public void setOnSlideDetailsListener(OnSlideDetailsListener listener) {
        this.mOnSlideDetailsListener = listener;
    }

    /**
     * Open pannel smoothly.
     *
     * @param smooth true, smoothly. false otherwise.
     */
    public void smoothOpen(boolean smooth) {
        if (mStatus != Status.OPEN) {
            mStatus = Status.OPEN;
            final float height = -getMeasuredHeight();
            animatorSwitch(0, height, true, smooth ? mDuration : 0);
        }
    }

    /**
     * Close pannel smoothly.
     *
     * @param smooth true, smoothly. false otherwise.
     */
    public void smoothClose(boolean smooth) {
        if (mStatus != Status.CLOSE) {
            mStatus = Status.CLOSE;
            final float height = -getMeasuredHeight();
            animatorSwitch(height, 0, true, smooth ? mDuration : 0);
        }
    }

    /**
     * Set the float value for indicate the moment of switch panel
     *
     * @param percent (0.0, 1.0)
     */
    public void setPercent(float percent) {
        this.mPercent = percent;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        final int childCount = getChildCount();
        if (1 >= childCount) {
            throw new RuntimeException("SlideDetailsLayout only accept childs more than 1!!");
        }

        mFrontView = getChildAt(0);
        mBehindView = getChildAt(1);

        // set behindview's visibility to GONE before show.
        //mBehindView.setVisibility(GONE);
        if(mDefaultPanel == 1){
            post(new Runnable() {
                @Override
                public void run() {
                    smoothOpen(false);
                }
            });
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int pWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int pHeight = MeasureSpec.getSize(heightMeasureSpec);

        final int childWidthMeasureSpec =
                MeasureSpec.makeMeasureSpec(pWidth, MeasureSpec.EXACTLY);
        final int childHeightMeasureSpec =
                MeasureSpec.makeMeasureSpec(pHeight, MeasureSpec.EXACTLY);

        View child;
        for (int i = 0; i < getChildCount(); i++) {
            child = getChildAt(i);
            // skip measure if gone
            if (child.getVisibility() == GONE) {
                continue;
            }

            measureChild(child, childWidthMeasureSpec, childHeightMeasureSpec);
        }

        setMeasuredDimension(pWidth, pHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int left = l;
        final int right = r;
        int top;
        int bottom;

        final int offset = (int) mSlideOffset;

        View child;
        for (int i = 0; i < getChildCount(); i++) {
            child = getChildAt(i);

            // skip layout
            if (child.getVisibility() == GONE) {
                continue;
            }

            if (child == mBehindView) {
                top = b + offset;
                bottom = top + b - t;
            } else {
                top = t + offset;
                bottom = b + offset;
            }

            child.layout(left, top, right, bottom);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTarget();
        if (null == mTarget) {
            return false;
        }

        if (!isEnabled()) {
            return false;
        }

        final int aciton = MotionEventCompat.getActionMasked(ev);

        boolean shouldIntercept = false;
        switch (aciton) {
            case MotionEvent.ACTION_DOWN: {
                mInitMotionX = ev.getX();
                mInitMotionY = ev.getY();
                shouldIntercept = false;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final float x = ev.getX();
                final float y = ev.getY();

                final float xDiff = x - mInitMotionX;
                final float yDiff = y - mInitMotionY;

                if (canChildScrollVertically((int) yDiff)) {
                    shouldIntercept = false;
                } else {
                    final float xDiffabs = Math.abs(xDiff);
                    final float yDiffabs = Math.abs(yDiff);

                    // intercept rules：
                    // 1. The vertical displacement is larger than the horizontal displacement;
                    // 2. Panel stauts is CLOSE：slide up
                    // 3. Panel status is OPEN：slide down
                    if (yDiffabs > mTouchSlop && yDiffabs >= xDiffabs
                        && !(mStatus == Status.CLOSE && yDiff > 0
                             || mStatus == Status.OPEN && yDiff < 0)) {
                        shouldIntercept = true;
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                shouldIntercept = false;
                break;
            }

        }

        return shouldIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        ensureTarget();
        if (null == mTarget) {
            return false;
        }

        if (!isEnabled()) {
            return false;
        }

        boolean wantTouch = true;
        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                // if target is a view, we want the DOWN action.
                if (mTarget instanceof View) {
                    wantTouch = true;
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final float y = ev.getY();
                final float yDiff = y - mInitMotionY;
                if (canChildScrollVertically(((int) yDiff))) {
                    wantTouch = false;
                } else {
                    processTouchEvent(yDiff);
                    wantTouch = true;
                }
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                finishTouchEvent();
                wantTouch = false;
                break;
            }
        }
        return wantTouch;
    }

    /**
     * @param offset Displacement in vertically.
     */
    private void processTouchEvent(final float offset) {
        if (Math.abs(offset) < mTouchSlop) {
            return;
        }

        final float oldOffset = mSlideOffset;
        // pull up to open
        if (mStatus == Status.CLOSE) {
            // reset if pull down
            if (offset >= 0) {
                mSlideOffset = 0;
            } else {
                mSlideOffset = offset;
            }

            if (mSlideOffset == oldOffset) {
                return;
            }

            // pull down to close
        } else if (mStatus == Status.OPEN) {
            final float pHeight = -getMeasuredHeight();
            // reset if pull up
            if (offset <= 0) {
                mSlideOffset = pHeight;
            } else {
                final float newOffset = pHeight + offset;
                mSlideOffset = newOffset;
            }

            if (mSlideOffset == oldOffset) {
                return;
            }
        }
        // relayout
        requestLayout();
    }

    /**
     * Called after gesture is ending.
     */
    private void finishTouchEvent() {
        final int pHeight = getMeasuredHeight();
        final int percent = (int) (pHeight * mPercent);
        final float offset = mSlideOffset;

        boolean changed = false;

        if (Status.CLOSE == mStatus) {
            if (offset <= -percent) {
                mSlideOffset = -pHeight;
                mStatus = Status.OPEN;
                changed = true;
            } else {
                // keep panel closed
                mSlideOffset = 0;
            }
        } else if (Status.OPEN == mStatus) {
            if ((offset + pHeight) >= percent) {
                mSlideOffset = 0;
                mStatus = Status.CLOSE;
                changed = true;
            } else {
                // keep panel opened
                mSlideOffset = -pHeight;
            }
        }

        animatorSwitch(offset, mSlideOffset, changed);
    }

    private void animatorSwitch(final float start, final float end) {
        animatorSwitch(start, end, true, mDuration);
    }

    private void animatorSwitch(final float start, final float end, final long duration) {
        animatorSwitch(start, end, true, duration);
    }

    private void animatorSwitch(final float start, final float end, final boolean changed) {
        animatorSwitch(start, end, changed, mDuration);
    }

    private void animatorSwitch(final float start,
                                final float end,
                                final boolean changed,
                                final long duration) {
        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSlideOffset = (float) animation.getAnimatedValue();
                requestLayout();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (changed) {
                    if (mStatus == Status.OPEN) {
                        checkAndFirstOpenPanel();
                    }

                    if (null != mOnSlideDetailsListener) {
                        mOnSlideDetailsListener.onStatucChanged(mStatus);
                    }
                }
            }
        });
        animator.setDuration(duration);
        animator.start();
    }

    /**
     * Whether the closed pannel is opened at first time.
     * If open first, we should set the behind view's visibility as VISIBLE.
     */
    private void checkAndFirstOpenPanel() {
        if (isFirstShowBehindView) {
            isFirstShowBehindView = false;
            mBehindView.setVisibility(VISIBLE);
        }
    }

    /**
     * When pulling, target view changed by the panel status. If panel opened, the target is behind view.
     * Front view is for otherwise.
     */
    private void ensureTarget() {
        if (mStatus == Status.CLOSE) {
            mTarget = mFrontView;
        } else {
            mTarget = mBehindView;
        }
    }

    /**
     * Check child view can srcollable in vertical direction.
     *
     * @param direction Negative to check scrolling up, positive to check scrolling down.
     *
     * @return true if this view can be scrolled in the specified direction, false otherwise.
     */
    protected boolean canChildScrollVertically(int direction) {
        if (mTarget instanceof AbsListView) {
            return canListViewSroll((AbsListView) mTarget);
        } else if (mTarget instanceof FrameLayout ||
                   mTarget instanceof RelativeLayout ||
                   mTarget instanceof LinearLayout) {
            View child;
            for (int i = 0; i < ((ViewGroup) mTarget).getChildCount(); i++) {
                child = ((ViewGroup) mTarget).getChildAt(i);
                if (child instanceof AbsListView) {
                    return canListViewSroll((AbsListView) child);
                }
            }
        }

        if (android.os.Build.VERSION.SDK_INT < 14) {
            return ViewCompat.canScrollVertically(mTarget, -direction) || mTarget.getScrollY() > 0;
        } else {
            return ViewCompat.canScrollVertically(mTarget, -direction);
        }
    }

    protected boolean canListViewSroll(AbsListView absListView) {
        if (mStatus == Status.OPEN) {
            return absListView.getChildCount() > 0
                   && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                                                                               .getTop() <
                                                                    absListView.getPaddingTop());
        } else {
            final int count = absListView.getChildCount();
            return count > 0
                   && (absListView.getLastVisiblePosition() < count - 1
                       || absListView.getChildAt(count - 1)
                                     .getBottom() > absListView.getMeasuredHeight());
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.offset = mSlideOffset;
        ss.status = mStatus.ordinal();
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mSlideOffset = ss.offset;
        mStatus = Status.valueOf(ss.status);

        if (mStatus == Status.OPEN) {
            mBehindView.setVisibility(VISIBLE);
        }

        requestLayout();
    }

    static class SavedState extends BaseSavedState {

        private float offset;
        private int status;

        /**
         * Constructor used when reading from a parcel. Reads the state of the superclass.
         *
         * @param source
         */
        public SavedState(Parcel source) {
            super(source);
            offset = source.readFloat();
            status = source.readInt();
        }

        /**
         * Constructor called by derived classes when creating their SavedState objects
         *
         * @param superState The state of the superclass of this view
         */
        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(offset);
            out.writeInt(status);
        }

        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
```


## 反馈
欢迎各位提issues和PRs

## 第三方库
- [Fresco](https://github.com/facebook/fresco)
- [ConvenientBanner](https://github.com/saiwu-bigkoo/Android-ConvenientBanner)
- [PagerSlidingTabStrip](https://github.com/astuetz/PagerSlidingTabStrip)

## 联系我
_hexianqiao3755@gmail.com_

## 许可证

    Copyright 2017 He Qiao

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.