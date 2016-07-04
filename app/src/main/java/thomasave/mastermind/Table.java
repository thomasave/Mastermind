package thomasave.mastermind;

import android.content.Context;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Vector;

public class Table {

    public Table(int columns, int rows, Context context) {
        m_columns = columns;
        m_rows = rows;
        m_context = context;
        m_buttons = new Vector<>();
    }

    GridView generateLayout() {
        GridView gridView = new GridView(m_context);
        gridView.setNumColumns(m_columns);
        gridView.setAdapter(buttonAdapter);
        gridView.setVerticalSpacing(RoundButton.padding);
        return gridView;
    }

    protected int m_columns ;
    protected int m_rows;
    protected Context m_context;
    protected ButtonAdapter buttonAdapter;
    protected int current_row = 0;
    protected Vector<TextView> m_buttons;
}
