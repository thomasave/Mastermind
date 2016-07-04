package thomasave.mastermind;

import android.view.View;

public class ButtonClickListener implements View.OnClickListener {
    @Override
    public void onClick(View view) {
        RoundButton roundButton = (RoundButton) view;
        roundButton.click();
    }
}
