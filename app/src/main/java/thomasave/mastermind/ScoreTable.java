package thomasave.mastermind;

import android.content.Context;
import android.widget.TextView;

public class ScoreTable extends Table {

    public ScoreTable(int columns, int rows, Context context) {
        super(columns,rows,context);
        for(int i = 0;i < rows;i++) {
            m_buttons.add(new ScoreBox(m_context, ScoreBox.Type.WHITE));
            m_buttons.add(new ScoreBox(m_context, ScoreBox.Type.BLACK));
        }
        buttonAdapter = new ButtonAdapter(m_context,m_buttons);
    }

    public void nextRound(int[] scores) {
        for(int i=current_row*m_columns;i< current_row*m_columns + m_columns;i++) {
            ScoreBox scoreBox = (ScoreBox) m_buttons.get(i);
            scoreBox.setText(Integer.toString(scores[i - current_row*m_columns]));
        }
        current_row++;
    }

    public void reset() {
        current_row = 0;
        for(TextView button: m_buttons) {
            ((ScoreBox) button).reset();
        }
    }

    public void onScreenRotate() {
        for(TextView button: m_buttons) {
            ((ScoreBox) button).onScreenRotate();
        }
    }
}