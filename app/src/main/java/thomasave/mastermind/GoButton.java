package thomasave.mastermind;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Gravity;
import android.widget.Button;
import android.widget.RelativeLayout;

public class GoButton extends Button {

    private float m_padding;
    private int current_round = 0;
    private int m_height;

    public GoButton(Context context, int width, int height, float padding) {
        super(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.setMargins((int) padding,0,0,0);
        setLayoutParams(layoutParams);
        setColor(R.drawable.round_button_confirm);
        setText("GO!");
        setPadding((int) padding, (int) padding - 20, (int) padding,(int) padding - 20);
        setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        setTextColor(Color.WHITE);
        setTranslationY(-padding);
        setTextSize(height / 7);
        m_padding = padding;
        m_height = height;
    }

    public void reset() {
        setTranslationY(-m_padding);
        current_round = 0;
    }

    private void setColor(int drawable_id) {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), drawable_id, null);
        setBackgroundDrawable(drawable);
    }

    public void onScreenRotate(int width, int height) {
        m_height = height;
        setTextSize(height / 7);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.setMargins(30,0,0,0);
        setLayoutParams(layoutParams);
        setTranslationY(-(m_padding + (m_padding + m_height) * current_round));
    }

    public void nextRound() {
        current_round++;
        setTranslationY(-(m_padding + (m_padding + m_height) * current_round));
    }

}
