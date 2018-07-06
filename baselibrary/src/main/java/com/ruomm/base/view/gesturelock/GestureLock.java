package com.ruomm.base.view.gesturelock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.ruomm.R;

import java.util.ArrayList;
import java.util.List;

public class GestureLock extends ViewGroup {

    public static final int MODE_NORMAL = 0;
    public static final int MODE_EDIT = 1;

    private int mode = MODE_NORMAL;

    private int depth = 3;

    private int mWidth, mHeight;
    private int mCenterX, mCenterY;

    private int[] defaultGestures = new int[]{0};
    private int[] negativeGestures;

    private List<Integer> gesturesContainer;
//    private int gestureCursor = 0;

    private Path gesturePath;

    private int lastX;
    private int lastY;
    private int lastPathX;
    private int lastPathY;

    private static final int MAX_BLOCK_SIZE = 200;

    private int mContentSize;
    private int mHalfContentSize;

    private Paint paint;

    private int unmatchedCount;
    private int unmatchedBoundary = 5;

    private boolean touchable;
    private int Color_Transparent=0x00000000;
    private int DEFAULT_ERROR_COLOR = 0x66FF0000;
    private int DEFAULT_COLOR = 0x66DDFFFF;
    private int mCustomColor;
    private int mCustomErrorColor;

    private OnGestureEventListener onGestureEventListener;
    private GestureLockAdapter mAdapter;
    private boolean canRepeat=true;
    private int maxGestureCount=9;
    private boolean showNormalArrow=false;
    private boolean showErrorArrow=true;
    private boolean showNormalPath=true;
    private boolean showErrorPath=true;

    public interface OnGestureEventListener{
        void onBlockSelected(int position);
        void onGestureResult(boolean matched,List<Integer> gesturesResultLst);
        void onUnmatchedExceedBoundary();
    }

    /**
     * GestureLockAdapter provide a way to customize the depth, correct gestures and gesture locker style
     */
    public interface GestureLockAdapter{
        int getDepth();
        int[] getCorrectGestures();
        int getUnmatchedBoundary();
        int getBlockGapSize();
        GestureLockView getGestureLockViewInstance(Context context, int position);
    }

    public GestureLock(Context context){
        this(context, null);
    }

    public GestureLock(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public GestureLock(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);

        negativeGestures = new int[depth * depth];
        for(int i = 0; i < negativeGestures.length; i++) negativeGestures[i] = -1;
        gesturesContainer=new ArrayList<>();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GestureLock);
        int lineWidth = ta.getDimensionPixelSize(R.styleable.GestureLock_line_width, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics()));
        mCustomColor = ta.getColor(R.styleable.GestureLock_line_normal_color, DEFAULT_COLOR);
        mCustomErrorColor = ta.getColor(R.styleable.GestureLock_line_error_color, DEFAULT_ERROR_COLOR);
        canRepeat=ta.getBoolean(R.styleable.GestureLock_gesture_can_repeat,false);
        maxGestureCount=ta.getInteger(R.styleable.GestureLock_gesture_max_count,9);
        showNormalArrow=ta.getBoolean(R.styleable.GestureLock_gesture_normal_show_arrow,false);
        showErrorArrow=ta.getBoolean(R.styleable.GestureLock_gesture_error_show_arrow,true);
        showNormalPath=ta.getBoolean(R.styleable.GestureLock_gesture_normal_show_path,true);
        showErrorPath=ta.getBoolean(R.styleable.GestureLock_gesture_error_show_path,true);
        ta.recycle();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);

        unmatchedCount = 0;

        touchable = true;
    }

    public void setAdapter(GestureLockAdapter adapter){
        if(mAdapter == adapter) return;

        mAdapter = adapter;

        if(mAdapter != null){
            updateParametersForAdapter();
            updateChildForAdapter();
        }
    }

    private void updateChildForAdapter(){
        final int totalBlockCount = depth * depth;
        removeAllViewsInLayout();

        for(int i = 0; i < totalBlockCount; i++){
            GestureLockView child = mAdapter.getGestureLockViewInstance(getContext(), i);
            child.setLockerState(GestureLockView.LockerState.LOCKER_STATE_NORMAL);
            child.setId(i + 1);

            addViewInLayout(child, i, generateDefaultLayoutParams());
        }

        requestLayout();
    }

    private void updateParametersForAdapter(){
        this.depth = mAdapter.getDepth();
        if(maxGestureCount<3)
        {
            maxGestureCount=9;
        }
        negativeGestures = new int[depth * depth];
        for(int i = 0; i < negativeGestures.length; i++) negativeGestures[i] = -1;
        gesturesContainer = new ArrayList<>();
        defaultGestures = mAdapter.getCorrectGestures();

//        if(defaultGestures.length > negativeGestures.length) throw new IllegalArgumentException("defaultGestures length must be less than or equal to " + negativeGestures.length);

        unmatchedBoundary = mAdapter.getUnmatchedBoundary();
    }

    public void notifyDataChanged(){
        updateParametersForAdapter();
        updateChildForAdapter();
    }

    public void setTouchable(boolean touchable){
        this.touchable = touchable;
    }

    public void resetUnmatchedCount(){
        unmatchedCount = 0;
    }

    public void setOnGestureEventListener(OnGestureEventListener onGestureEventListener){
        this.onGestureEventListener = onGestureEventListener;
    }

    @Override
    public void addView(View child, int width, int height){
        if(!(child instanceof GestureLockView)){
            throw new IllegalArgumentException();
        }

        super.addView(child, width, height);
    }

    @Override
    public void addView(View child, int index, LayoutParams layoutParams){
        if(!(child instanceof GestureLockView)){
            throw new IllegalArgumentException();
        }

        super.addView(child, index, layoutParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(mAdapter != null){
            final int childCount = getChildCount();
            if(childCount == depth * depth){

                int childWidthMode, childHeightMode;
                int childWidthSize, childHeightSize;

                int totalGap = mAdapter.getBlockGapSize() * (depth - 1);

                if(widthMode == MeasureSpec.EXACTLY){
                    childWidthMode = MeasureSpec.EXACTLY;
                    childWidthSize = (widthSize - totalGap) / depth;
                }else{
                    childWidthMode = MeasureSpec.AT_MOST;
                    childWidthSize = MAX_BLOCK_SIZE;
                }

                if(heightMode == MeasureSpec.EXACTLY){
                    childHeightMode = MeasureSpec.EXACTLY;
                    childHeightSize = (heightSize - totalGap) / depth;
                }else{
                    childHeightMode = MeasureSpec.AT_MOST;
                    childHeightSize = MAX_BLOCK_SIZE;
                }

                int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, childWidthMode);
                int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, childHeightMode);

                for(int i = 0; i < childCount; i++){
                    View child = getChildAt(i);

                    child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                }
            }
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        mWidth = w;
        mHeight = h;

        mCenterX = w / 2;
        mCenterY = h / 2;

        mContentSize = mWidth > mHeight ? mHeight : mWidth;
        mHalfContentSize = mContentSize / 2;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b){

        int totalGap = mAdapter.getBlockGapSize() * (depth - 1);

        final int xStart = mCenterX - mHalfContentSize;
        final int yStart = mCenterY - mHalfContentSize;

        int xStep = xStart;
        int yStep = yStart;

        int childSize = (mContentSize - totalGap) / depth;

        final int childCount = getChildCount();

        for(int i = 0; i < childCount; i++){

            View child = getChildAt(i);

            child.layout(xStep, yStep, xStep + childSize, yStep + childSize);

            if(i % depth == depth - 1){
                xStep = xStart;
                yStep += childSize + mAdapter.getBlockGapSize();
            }else{
                xStep += childSize + mAdapter.getBlockGapSize();
            }
        }
    }

    public void clear(){
        for(int i = 0; i < getChildCount(); i++) {
            View c = getChildAt(i);
            if(c instanceof GestureLockView){
                ((GestureLockView) c).setLockerState(GestureLockView.LockerState.LOCKER_STATE_NORMAL);
                ((GestureLockView) c).clearArrow();
            }
        }

        gesturePath = null;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (touchable) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    for (int i = 0; i < getChildCount(); i++) {
                        View c = getChildAt(i);
                        if (c instanceof GestureLockView) {
                            ((GestureLockView) c).setLockerState(GestureLockView.LockerState.LOCKER_STATE_NORMAL);
                            ((GestureLockView) c).clearArrow();
                        }
                    }

                    gesturePath = null;

                    lastX = (int) event.getX();
                    lastY = (int) event.getY();
                    lastPathX = lastX;
                    lastPathY = lastY;
                    if(showNormalPath){
                        paint.setColor(mCustomColor);
                    }
                   else {
                        paint.setColor(Color_Transparent);
                    }

                    break;
                case MotionEvent.ACTION_MOVE:

                    lastX = (int) event.getX();
                    lastY = (int) event.getY();

                    int cId = calculateChildIdByCoords(lastX, lastY);

                    View child = findViewById(cId + 1);
                    boolean checked = false;
                    if(gesturesContainer.size()>=maxGestureCount)
                    {
                        checked=true;
                    }
                    else if(canRepeat){
                        int size=gesturesContainer.size();
                        if(size<=0)
                        {
                            checked=false;
                        }
                        else if(gesturesContainer.get(size-1)==cId)
                        {
                            checked=true;
                        }
                     }
                    else{
                        for (int id : gesturesContainer) {
                            if (id == cId) {
                                checked = true;
                                break;
                            }
                        }
                    }


                    if (child != null && child instanceof GestureLockView && checkChildInCoords(lastX, lastY, child)) {


                        if (!checked) {
                            if(!showNormalPath)
                            {
                                for(int i=0;i<gesturesContainer.size();i++)
                                {
                                    View hisChild = findViewById(gesturesContainer.get(i) + 1);
                                    if(null!=hisChild&&hisChild instanceof  GestureLockView)
                                    {
                                        ((GestureLockView) hisChild).setLockerState(GestureLockView.LockerState.LOCKER_STATE_NORMAL);
                                    }
                                }

                            }
                            ((GestureLockView) child).setLockerState(GestureLockView.LockerState.LOCKER_STATE_SELECTED);
                            int checkedX = child.getLeft() + child.getWidth() / 2;
                            int checkedY = child.getTop() + child.getHeight() / 2;
                            if (gesturePath == null) {
                                gesturePath = new Path();
                                gesturePath.moveTo(checkedX, checkedY);
                            } else {
                                gesturePath.lineTo(checkedX, checkedY);
                            }
//                            gesturesContainer[gestureCursor] = cId;
//                            gestureCursor++;
                            gesturesContainer.add(cId);

                            lastPathX = checkedX;
                            lastPathY = checkedY;
                            if(showNormalPath&&showNormalArrow&&gesturesContainer.size()>1)
                            {
                                int k=gesturesContainer.size()-2;
                                View previewChild = findViewById(gesturesContainer.get(k) + 1);
                                if(previewChild!=null&&previewChild instanceof GestureLockView){
                                    int dx = child.getLeft() - previewChild.getLeft();
                                    int dy = child.getTop() - previewChild.getTop();

                                    int angle = (int) Math.toDegrees(Math.atan2(dy, dx)) + 90;
                                    ((GestureLockView) previewChild).addArrow(angle);
                                }
                            }
                            if (onGestureEventListener != null)
                                onGestureEventListener.onBlockSelected(cId);
                        }
                    }

                    invalidate();
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:

                    if (gesturesContainer.size()>0) {
                        boolean matched = false;

                        if (null!=defaultGestures&&gesturesContainer.size() == defaultGestures.length) {
                            for (int j = 0; j < defaultGestures.length; j++) {
                                if (gesturesContainer.get(j) == defaultGestures[j]) {
                                    matched = true;
                                } else {
                                    matched = false;
                                    break;
                                }
                            }
                        }

                        if (!matched && mode != MODE_EDIT) {
                            unmatchedCount++;
                            if(showErrorPath){
                                paint.setColor(mCustomErrorColor);
                            }
                            else{
                                paint.setColor(Color_Transparent);
                            }
                            for (int k = 0; k < gesturesContainer.size(); k++) {
                                View selectedChild = findViewById(gesturesContainer.get(k) + 1);
                                if (selectedChild != null && selectedChild instanceof GestureLockView) {
                                    if(showErrorPath){
                                        ((GestureLockView) selectedChild).setLockerState(GestureLockView.LockerState.LOCKER_STATE_ERROR);
                                    }
                                    else
                                    {
                                        ((GestureLockView) selectedChild).setLockerState(GestureLockView.LockerState.LOCKER_STATE_NORMAL);
                                    }
                                    if (showErrorPath&&showErrorArrow&&k < gesturesContainer.size()- 1) {
                                        View nextChild = findViewById(gesturesContainer.get(k+1) + 1);
                                        if (nextChild != null) {
                                            int dx = nextChild.getLeft() - selectedChild.getLeft();
                                            int dy = nextChild.getTop() - selectedChild.getTop();

                                            int angle = (int) Math.toDegrees(Math.atan2(dy, dx)) + 90;
                                            ((GestureLockView) selectedChild).addArrow(angle);
                                        }
                                    }
                                    else {
                                        ((GestureLockView) selectedChild).clearArrow();
                                    }
                                }
                            }
                        } else {
                            if(!showNormalPath)
                            {
                                int index=gesturesContainer.size()-1;
                                if(index>=0)
                                {
                                    View lastChild = findViewById(gesturesContainer.get(index) + 1);
                                    if (lastChild != null && lastChild instanceof GestureLockView) {
                                        ((GestureLockView) lastChild).setLockerState(GestureLockView.LockerState.LOCKER_STATE_NORMAL);
                                    }
                                }
                            }
                            unmatchedCount = 0;
                        }


                        if (onGestureEventListener != null) {
                            List<Integer> gesturesResultLst=new ArrayList<Integer>();
                            if(gesturesContainer.size()>0)
                            {
                                gesturesResultLst.addAll(gesturesResultLst);

                            }
                            onGestureEventListener.onGestureResult(matched,gesturesResultLst);
                            if (unmatchedCount >= unmatchedBoundary) {
                                onGestureEventListener.onUnmatchedExceedBoundary();
                                unmatchedCount = 0;
                            }
                        }
                    }

//                    gestureCursor = 0;
                    gesturesContainer.clear();

                    lastX = lastPathX;
                    lastY = lastPathY;

                    invalidate();

                    break;
            }
        }

        return true;
    }

    public void setMode(int mode){
        this.mode = mode;
    }

    private int calculateChildIdByCoords(int x, int y){
        if(x >= mCenterX - mHalfContentSize && x <= mCenterX + mHalfContentSize && y >= mCenterY - mHalfContentSize && y <= mCenterY + mHalfContentSize){
            x -= mCenterX - mHalfContentSize;
            y -= mCenterY - mHalfContentSize;

            int rowX = (int) (((float) x / (float) mContentSize) * depth);
            int rowY = (int) (((float) y / (float) mContentSize) * depth);

            return rowX + (rowY * depth);
        }

        return -1;
    }

    private boolean checkChildInCoords(int x, int y, View child){
        if(child != null){

            int centerX = child.getLeft() + child.getWidth() / 2;
            int centerY = child.getTop() + child.getHeight() / 2;

            int dx = centerX - x;
            int dy = centerY - y;

            int radius = child.getWidth() > child.getHeight() ? child.getHeight() : child.getWidth();
            radius /= 2;
            if(dx * dx + dy * dy < radius * radius) return true;
        }

        return false;
    }

    @Override
    public void dispatchDraw(Canvas canvas){

        if(gesturePath != null){
            canvas.drawPath(gesturePath, paint);
        }

        if(gesturesContainer.size()>0) canvas.drawLine(lastPathX, lastPathY, lastX, lastY, paint);

        super.dispatchDraw(canvas);
    }
}
