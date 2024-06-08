package app.tabser.sheetview.menu;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import app.tabser.sheetview.MenuAnimator;
import app.tabser.sheetview.SheetView;

public class MenuView extends View implements View.OnTouchListener {

    private MenuAnimator menuAnimator;

    public MenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.CYAN);
        setOnTouchListener(this);

    }

    public void setMenuAnimator(MenuAnimator menuAnimator) {
        this.menuAnimator = menuAnimator;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("app.tabser", event.getAction()+"");
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            menuAnimator.setMode(menuAnimator.getMode() == SheetView.Mode.VIEW ?
                    SheetView.Mode.EDIT : SheetView.Mode.VIEW);
            return true;
        }
        return false;
    }
}
