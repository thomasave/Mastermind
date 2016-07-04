package thomasave.mastermind;

import android.content.Context;
import android.widget.TextView;

public class ButtonTable extends Table{

    public ButtonTable(int columns, int rows, Context context) {
        super(columns,rows,context);
        for(int i = 0;i < rows * columns;i++) {
            m_buttons.add(new RoundButton(m_context));
            m_buttons.get(i).setOnClickListener(new ButtonClickListener());
            m_buttons.get(i).setId(i);
        }
        buttonAdapter = new ButtonAdapter(m_context,m_buttons);
        for(int i=0;i<columns;i++) {
            RoundButton roundButton = (RoundButton) m_buttons.get(i);
            roundButton.enable();
        }
    }

    public int[] nextRound() {
        int results[] = new int[m_columns];
        for(int i=current_row*m_columns;i< current_row*m_columns + m_columns;i++) {
            if(i + m_columns < m_buttons.size()) {
                RoundButton roundButton_next = (RoundButton) m_buttons.get(i+m_columns);
                roundButton_next.enable();
            }

            RoundButton roundButton = (RoundButton) m_buttons.get(i);
            results[i - current_row*m_columns] = roundButton.current_color;
            roundButton.disable();
        }
        current_row++;
        return results;
    }

    public void onScreenRotate() {
        for(TextView button: m_buttons) {
            ((RoundButton) button).onScreenRotate();
        }
    }

    public void reset() {
        current_row = 0;
        for(TextView button: m_buttons) {
            ((RoundButton) button).reset();
        }
        for(int i=0;i<m_columns;i++) {
            RoundButton roundButton = (RoundButton) m_buttons.get(i);
            roundButton.enable();
        }
    }
}