package thomasave.mastermind;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.Button;
import android.widget.GridView;

public class RoundButton extends Button {
    public static int size=0;
    public static int padding=0;

    public int current_color = 0;
    private boolean is_enabled = false;

    public RoundButton(Context context) {
        super(context);
        setColor(R.drawable.round_button_white);
        GridView.LayoutParams layoutParams = new GridView.LayoutParams(RoundButton.size, RoundButton.size);
        setLayoutParams(layoutParams);
        setPadding(padding, padding, padding, padding);
    }

    public void onScreenRotate() {
        GridView.LayoutParams layoutParams = new GridView.LayoutParams(RoundButton.size, RoundButton.size);
        setLayoutParams(layoutParams);
        setPadding(padding, padding, padding, padding);
    }

    public void click() {
        if(is_enabled) {
            int drawable_id;
            switch(current_color) {
                case 0:
                    drawable_id = R.drawable.round_button_blue;
                    break;
                case 1:
                    drawable_id = R.drawable.round_button_green;
                    break;
                case 2:
                    drawable_id = R.drawable.round_button_red;
                    break;
                case 3:
                    drawable_id = R.drawable.round_button_white;
                    break;
                case 4:
                    drawable_id = R.drawable.round_button_yellow;
                    break;
                case 5:
                    drawable_id = R.drawable.round_button_black;
                    break;
                default:
                    drawable_id = 0;
            }
            setColor(drawable_id);
        }
        current_color = (current_color + 1) % 6;
    }
    public void enable() {
        setColor(R.drawable.round_button_black);
        is_enabled = true;
    }
    public void disable() {
        is_enabled = false;
    }

    private void setColor(int drawable_id) {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), drawable_id, null);
        setBackgroundDrawable(drawable);
    }

    public void reset() {
        setColor(R.drawable.round_button_white);
        current_color = 0;
        is_enabled = false;
    }
}
