package com.uk.google.njtest;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class MyFlowLayout extends ViewGroup {

	private int mHorizontalSpacing = 10;
	private int mVerticalSpacing = 10;

	public MyFlowLayout(Context context) {
		super(context);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		l = 0;
		t = 0;

		for (int i = 0; i < lineList.size(); i++) {
			Line line = lineList.get(i);
			line.layoutView(l, t);
			t = t + line.mLineHeight + mVerticalSpacing;
		}
	}

	private void restoreState() {
		lineList.clear();
		mCurrentLine = null;
		mUsedWidth = 0;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		// 恢复状态 onMeasure有可能执行多次，在每一次执行的时候，都需要恢复一下状态
		restoreState();
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		mCurrentLine = new Line();

		int childCount = getChildCount();// 得到孩子的数量
		for (int i = 0; i < childCount; i++) {
			View childView = getChildAt(i);
			// 孩子的宽度最多不能超过父控件的宽度
			int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize,
					MeasureSpec.AT_MOST);
			int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
			childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);

			int childWidth = childView.getMeasuredWidth();// 得到子控件的宽度

			mUsedWidth = mUsedWidth + childWidth;
			if (mUsedWidth < widthSize) {
				// 代表剩余的空间能够装得下这个元素
				mCurrentLine.addView(childView);
				mUsedWidth = mUsedWidth + mHorizontalSpacing;// 将水平间距加到最后
			} else {
				// 代表剩余的空间已经装不下这个元素了
				if (mCurrentLine.getLineViewCount() == 0) {
					// 代表添加第一个元素的时候，空间就不够了
					mCurrentLine.addView(childView);
					if(!newLine()) {
						break;
					}
				} else {
					if(!newLine()) {
						break;
					}
					mCurrentLine.addView(childView);
					mUsedWidth = childWidth + mHorizontalSpacing;
				}
			}
		}
		if (mCurrentLine != null && !lineList.contains(mCurrentLine)) {
			lineList.add(mCurrentLine);
		}
		int totalHeight = 0;
		for (int i = 0; i < lineList.size(); i++) {
			Line line = lineList.get(i);
			totalHeight = totalHeight + line.mLineHeight + mVerticalSpacing;
		}
		totalHeight = totalHeight - mVerticalSpacing;
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(totalHeight,
				MeasureSpec.EXACTLY);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	private boolean newLine() {
		lineList.add(mCurrentLine);
		mCurrentLine = new Line();
		mUsedWidth = 0;
		return true;
	}

	private int mUsedWidth;

	private ArrayList<Line> lineList = new ArrayList<Line>();

	private Line mCurrentLine;// 当前正在计算的那一行

	class Line {

		private int mLineHeight;

		private ArrayList<View> lineViews = new ArrayList<View>();

		public void addView(View childView) {
			lineViews.add(childView);
			mLineHeight = childView.getMeasuredHeight();
		}

		public int getLineViewCount() {
			return lineViews.size();
		}

		public void layoutView(int left, int top) {
			for (int i = 0; i < getLineViewCount(); i++) {
				View childView = lineViews.get(i);
				childView.layout(left, top ,
						left + childView.getMeasuredWidth(), top
								+ childView.getMeasuredHeight());
				// 更新left的值
				left = left + childView.getMeasuredWidth() + mHorizontalSpacing;
			}
		}
	}

}
