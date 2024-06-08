package app.tabser.sheetview;

import android.animation.LayoutTransition;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import app.tabser.sheetview.menu.MenuView;

//import app.tabser.view.input.MenuView;

public class MenuAnimator implements LayoutTransition.TransitionListener {
    public void reset() {
        hideMenu();
    }

    public void setHidePhase() {
        phase = Phase.HIDE;
    }

    public void startMode(SheetView.Mode mode) {
        setMode(mode);
        setHidePhase();
    }

    private enum Phase {SHOW, HIDE}

    private final View mainView;
    private final View menuView;
    private final LinearLayout editorLayout;
    private SheetView.Mode viewMode;
    private Phase phase = Phase.SHOW;

    public MenuAnimator(LinearLayout editorLayout, View mainView, View menuView, SheetView.Mode viewMode) {
        this.mainView = mainView;
        this.menuView = menuView;
        this.editorLayout = editorLayout;
//        setMode(viewMode);
        LayoutTransition layoutTransition = editorLayout.getLayoutTransition();
        layoutTransition.addTransitionListener(this);
        layoutTransition.setDuration(333L);
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        Rect r = new Rect(0, 0, 0, 0);
    }

    public SheetView.Mode getMode() {
        return viewMode;
    }

    public void setMode(SheetView.Mode mode) {
        this.viewMode = mode;
        Log.d("app.tabser", "setMode(" + mode + ") phase=" + phase);
        if (phase == Phase.SHOW) {
            showMenu();
        } else {
            hideMenu();
        }
    }

    private void hideMenu() {
        ((LinearLayout.LayoutParams) mainView.getLayoutParams()).weight = 100f;
        ((LinearLayout.LayoutParams) menuView.getLayoutParams()).weight = 0f;
        mainView.requestLayout();
//        menuView.requestLayout();
    }

    private void showMenu() {
        float targetMenuWeight = viewMode == SheetView.Mode.VIEW ? 5f : 25f;
        float targetMainViewWeight = 100f - targetMenuWeight;
        ((LinearLayout.LayoutParams) mainView.getLayoutParams()).weight = targetMainViewWeight;
        ((LinearLayout.LayoutParams) menuView.getLayoutParams()).weight = targetMenuWeight;
        mainView.requestLayout();
//        menuView.requestLayout();
    }

    @Override
    public void startTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {

    }

    @Override
    public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
        if (view instanceof MenuView) {
            Log.d("app.tabser", "End... Phase=" + phase);
            if (phase == Phase.SHOW) {
                phase = Phase.HIDE;
            } else {
                phase = Phase.SHOW;
                showMenu();
            }
        }
    }
}
