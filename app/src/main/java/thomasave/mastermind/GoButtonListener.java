package thomasave.mastermind;

import android.util.Log;
import android.view.View;
import android.widget.Button;

public class GoButtonListener implements Button.OnClickListener {

    private Game m_game;

    public GoButtonListener(Game game) {
        m_game = game;
    }

    @Override
    public void onClick(View view) {
        Log.v("Nextround","");
        m_game.nextRound();
    }
}
