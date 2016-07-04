package thomasave.mastermind;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Vector;

public class ButtonAdapter extends BaseAdapter {

    private Context mContext;
    private Vector<TextView> m_buttons;

    // Gets the context so it can be used later
    public ButtonAdapter(Context c, Vector<TextView> buttons) {
        mContext = c;
        m_buttons = buttons;
    }

    // Total number of things contained within the adapter
    public int getCount() {
        return m_buttons.size();
    }

    // Require for structure, not really used in my code.
    public Object getItem(int position) {
        return m_buttons.get(m_buttons.size() - position);
    }

    // Require for structure, not really used in my code. Can
    // be used to get the id of an item in the adapter for
    // manual control.
    public long getItemId(int position) {
        return m_buttons.size() - position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return m_buttons.get(m_buttons.size() - position - 1);
    }
}
