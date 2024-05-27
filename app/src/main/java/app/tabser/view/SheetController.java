package app.tabser.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.Objects;

import app.tabser.view.input.EditorMenu;
import app.tabser.view.input.EditorMenuController;
import app.tabser.view.input.ViewerMenu;
import app.tabser.view.input.ViewerMenuController;

public final class SheetController implements View.OnTouchListener, View.OnLongClickListener {

    public final SharedPreferences preferences;// = .getSharedPreferences("Keyboard", Context.MODE_PRIVATE);
    public final SheetView sheetView;// = new SheetView(this, theme, preferences, sheetController);
    //    public final EditorMenu editorMenu;// = new EditorMenu(keyboardRect, sheetView, context, theme, preferences);
//    public final ViewerMenu viewerMenu;// = new ViewerMenu(sheetView, context, theme, this);
    private boolean longClick;
    //private final GestureDetector gestureDetector;
    private final Handler handler = new Handler();
    Runnable touchScheduler = new Runnable() {
        public void run() {
            longClick = true;
        }
    };
    private boolean dragging;
//    private final SheetView sheetView;
    private Point pointDown = new Point();

    public final EditorMenuController editorMenuController;
    public final ViewerMenuController viewerMenuController;

    public SheetController(Context context, SheetView sheetView) {
        this.preferences = context.getSharedPreferences("Keyboard", Context.MODE_PRIVATE);
        this.sheetView = sheetView;
        this.viewerMenuController = new ViewerMenuController();
        this.editorMenuController = new EditorMenuController();
//        public final sheetView =new SheetView(this, theme, preferences, sheetController);
//        public final editorMenu =
//        new EditorMenu(keyboardRect, sheetView, context, theme, preferences);
//        public final viewerMenu =new ViewerMenu(sheetView, context, theme, this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        String message = "";
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            pointDown.set((int) motionEvent.getX(), (int) motionEvent.getY());
            handler.postDelayed(touchScheduler, ViewConfiguration.getLongPressTimeout());
            sheetView.startMove();
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if (dragging) {
                dragging = false;
                float deltaDragX = pointDown.x - motionEvent.getX();
                float deltaDragY = pointDown.y - motionEvent.getY();
                float tolerance = 15.0f;
                if (Math.abs(deltaDragX) > tolerance || Math.abs(deltaDragY) > tolerance) {
                    //Log.d("app.tabser", "onTouch: DRAG: dX=" + deltaDragX + " & dY=" + deltaDragY);
                    log(message);
                    return false;
                } else {
                    //Log.d("app.tabser", "onTouch: NO DRAG: dX=" + deltaDragX + " & dY=" + deltaDragY);
                }
            }
//            if (sheetView.settings.getMode() == SheetView.Mode.EDIT
//                    && keyboardRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
//                message = keyboard.touch(view, motionEvent, longClick);
//                //Toast.makeText(context, keyboard.touch(view, motionEvent, longClick) + (longClick ? " -L" : ""), Toast.LENGTH_LONG).show();
//            } else if (sheetView.settings.getMode() == SheetView.Mode.VIEW
//                    && playerRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
//                message = viewControls.touch(motionEvent, longClick);
//                //Toast.makeText(context, viewControls.touch(motionEvent, longClick) + (longClick ? " -L" : ""), Toast.LENGTH_LONG).show();
//            } else {
            message = sheetView.touch(motionEvent, longClick);
//                //Toast.makeText(context, sheet.onTouch(motionEvent, longClick) + (longClick ? " -L" : ""), Toast.LENGTH_LONG).show();
//            }
            if (longClick) {
                longClick = false;
            }
        } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && !dragging) {
            dragging = true;
        }
        if ((motionEvent.getAction() == MotionEvent.ACTION_MOVE)
                || (motionEvent.getAction() == MotionEvent.ACTION_UP)) {
            handler.removeCallbacks(touchScheduler);
        }
        if ((motionEvent.getAction() == MotionEvent.ACTION_MOVE)) {
            sheetView.moveVertical(pointDown, new Point((int) motionEvent.getX(), (int) motionEvent.getY()));
        }
        log(message);
        return false;
    }

    private void log(String message) {
        if (Objects.nonNull(message) && message.length() > 0) {
            Log.d("app.tabser", message);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        this.longClick = true;
        return false;
    }
}
