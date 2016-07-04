package thomasave.mastermind;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Gravity;
import android.widget.GridView;
import android.widget.TextView;

public class ScoreBox extends TextView {
    public static int size=0;
    public static int padding=0;
    public enum Type {BLACK, WHITE}
    private Type m_type;

    public ScoreBox(Context context, Type type) {
        super(context);
        if(type == Type.BLACK) {
            setTextColor(Color.WHITE);
            setColor(R.drawable.round_button_scoreblack);
        } else {
            setTextColor(Color.BLACK);
            setColor(R.drawable.round_button_scorewhite);
        }
        m_type = type;
        setVisibility(INVISIBLE);
        GridView.LayoutParams layoutParams = new GridView.LayoutParams((int) (size * 0.9), size);
        setLayoutParams(layoutParams);
        setPadding(padding, padding - 20, padding, padding - 20);
        setTextSize((int) (0.178571429 * size));
        setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
    }

    public void onScreenRotate() {
        GridView.LayoutParams layoutParams = new GridView.LayoutParams((int) (size * 0.9), size);
        setLayoutParams(layoutParams);
        setPadding(padding, padding - 20, padding, padding - 20);
        setTextSize((int) (0.178571429 * size));
    }

    public void setText(String text) {
        super.setText(text);
        setVisibility(VISIBLE);
    }

    public void reset() {
        setVisibility(INVISIBLE);
    }

    private void setColor(int drawable_id) {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), drawable_id, null);
        setBackgroundDrawable(drawable);
    }
}
