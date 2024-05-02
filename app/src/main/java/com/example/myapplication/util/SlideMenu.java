/**
 * SlideMenu
 *
 * SlideMenu is a custom view that provides sliding menu functionality. It allows the user to slide a menu
 * view from the side of the screen to reveal additional options or content. This view can be incorporated
 * into applications to create a navigation or settings menu that slides in and out of view.
 *
 * Features:
 * - Sliding menu view that can be opened and closed through touch interaction.
 * - Smooth animation using Scroller to slide the menu view in and out.
 *
 * Usage:
 * - Include the SlideMenu view in your layout XML file.
 * - Add child views for the main content and the menu content.
 * - Customize the menu and main content views as needed.
 * - Use the SlideMenu in your activity or fragment to enable sliding menu functionality.
 * - Call switchMenu() method to open or close the menu programmatically.
 *
 */

package com.example.myapplication.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class SlideMenu extends FrameLayout {
    private View menuView, mainView;
    private int menuWidth;
    private Scroller scroller;

    public SlideMenu(Context context) {
        super(context);
        init();
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        scroller = new Scroller(getContext());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        menuView = getChildAt(0);
        mainView = getChildAt(1);
        menuWidth = menuView.getLayoutParams().width;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        menuView.layout(-menuWidth, 0, 0, b);
        mainView.layout(0, 0, r, b);
    }

    private int downX;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) (ev.getX() - downX);
                if (Math.abs(deltaX) > 8) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int deltaX = moveX - downX;
                int newScrollX = getScrollX() - deltaX;
                if (newScrollX < -menuWidth) newScrollX = -menuWidth;
                if (newScrollX > 0) newScrollX = 0;
                scrollTo(newScrollX, 0);
                downX = moveX;
                break;
            case MotionEvent.ACTION_UP:
                if (getScrollX() > -menuWidth / 2) {
                    closeMenu();
                } else {
                    openMenu();
                }
                break;
        }
        return true;
    }

    private void closeMenu() {
        scroller.startScroll(getScrollX(), 0, 0 - getScrollX(), 0, 400);
        invalidate();
    }

    private void openMenu() {
        scroller.startScroll(getScrollX(), 0, -menuWidth - getScrollX(), 0, 400);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), 0);
            invalidate();
        }
    }

    public void switchMenu() {
        if (getScrollX() == 0) {
            openMenu();
        } else {
            closeMenu();
        }
    }
}
