package thomasave.mastermind;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.view.Display;
import android.widget.GridView;
import android.widget.RelativeLayout;

import java.util.Arrays;
import java.util.Random;

public class Game {

    private int m_rows;
    private int m_columns;

    private ButtonTable m_button_table;
    private ScoreTable  m_score_table;
    private GoButton m_gobutton;

    private int m_current_round;

    private Activity m_activity;

    private int m_correct[];

    public Game(Context mContext, int rows, int columns) {
        m_rows = rows;
        m_columns = columns;

        m_activity = (Activity) mContext;
        Display display = m_activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        RoundButton.size = (int) (size.x / (2 * columns) + columns * 5 - 3 * 5);
        RoundButton.padding = 30;
        ScoreBox.size = (int) (size.x / (2 * columns) + columns * 5 - 3 * 5);
        ScoreBox.padding = 30;
        m_button_table = new ButtonTable(columns,rows,m_activity);

        m_score_table = new ScoreTable(2,rows,m_activity);
        m_gobutton = new GoButton(m_activity,(int) (ScoreBox.size * 2.1 * 0.93) ,ScoreBox.size,30);
        m_gobutton.setOnClickListener(new GoButtonListener(this));

        m_correct = generateAnswers();

        addViews();
    }

    public int[] checkAnswers(int[] input) {

        int black = 0;
        int white = 0;

        boolean code_used[] = new boolean[m_columns];
        Arrays.fill(code_used, false);

        boolean input_used[] = new boolean[m_columns];
        Arrays.fill(code_used, false);

        for (int i = 0; i < m_columns; i++) {
            if (m_correct[i] == input[i]) {
                black++;
                code_used[i] = true;
                input_used[i] = true;
            }
        }
        for (int i = 0; i < m_columns; i++) {
            for (int j = 0; j < m_columns; j++) {
                if (m_correct[i] == input[j] && !code_used[i] && !input_used[j]) {
                    white++;
                    code_used[i] = true;
                    input_used[j] = true;
                }
            }
        }
        return new int[]{white, black};
    }

    private int[] generateAnswers() {
        Random r = new Random();
        int result[] = new int[m_columns];
        for(int i=0;i<m_columns;i++) {
            result[i] = r.nextInt(6);
        }
        return result;
    }

    public void reset() {
        m_score_table.reset();
        m_button_table.reset();
        m_gobutton.reset();
        m_correct = generateAnswers();
        m_current_round = 0;
    }

    public void nextRound() {
        m_current_round++;
        int results[] = checkAnswers(m_button_table.nextRound());
        if(results[1] == m_columns) {
            winner();
        } else if(m_current_round == m_rows) {
            loser();
        } else {
            m_gobutton.nextRound();
        }
        m_score_table.nextRound(results);

    }

    public void winner() {
        SharedPreferences sharedPreferences = m_activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEdit1 = sharedPreferences.edit();
        mEdit1.putInt("wincount", sharedPreferences.getInt("wincount",0) + 1);
        mEdit1.apply();

        AlertDialog.Builder builder = new AlertDialog.Builder(m_activity);
        builder.setTitle("We have a winner")
                .setMessage("Congratulations, you won!")
                .setCancelable(false)
                .setNeutralButton("Restart",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        reset();
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void addViews() {

        RelativeLayout relativeLayout = (RelativeLayout) m_activity.findViewById(R.id.relativelayout);

        GridView buttons = m_button_table.generateLayout();
        RelativeLayout.LayoutParams layoutButtons = new RelativeLayout.LayoutParams((int) (RoundButton.size * m_columns * 1.2), m_rows * (RoundButton.size + RoundButton.padding));
        layoutButtons.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutButtons.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutButtons.setMargins(0,30,0,0);

        RelativeLayout.LayoutParams layoutScores = new RelativeLayout.LayoutParams((int) (ScoreBox.size * 2.1), m_rows * (ScoreBox.size + ScoreBox.padding));
        layoutScores.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutScores.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutScores.setMargins(30,10,0,0);

        buttons.setLayoutParams(layoutButtons);
        relativeLayout.addView(buttons);
        GridView scores = m_score_table.generateLayout();

        scores.setLayoutParams(layoutScores);
        relativeLayout.addView(scores);
        relativeLayout.addView(m_gobutton);

        ((MainActivity) m_activity).changeBackground(m_rows);
    }

    public void onScreenRotate() {
        Display display = m_activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        RoundButton.size = (int) (size.x / (2 * m_columns) + m_columns * 5 - 3 * 5);
        ScoreBox.size = (int) (size.x / (2 * m_columns) + m_columns * 5 - 3 * 5);
        m_button_table.onScreenRotate();
        m_score_table.onScreenRotate();
        m_gobutton.onScreenRotate((int) (ScoreBox.size * 2.1*0.93) ,ScoreBox.size);
        ((RelativeLayout) m_activity.findViewById(R.id.relativelayout)).removeAllViews();
        addViews();
    }

    public void loser() {
        SharedPreferences sharedPreferences = m_activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEdit1 = sharedPreferences.edit();
        mEdit1.putInt("lostcount", sharedPreferences.getInt("lostcount",0) + 1);
        mEdit1.apply();

        AlertDialog.Builder builder = new AlertDialog.Builder(m_activity);
        builder.setTitle("Oops")
                .setMessage("You lost... Better luck next time.")
                .setCancelable(false)
                .setPositiveButton("Restart",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        reset();
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
