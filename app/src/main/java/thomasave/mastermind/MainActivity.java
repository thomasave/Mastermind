package thomasave.mastermind;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    private Game m_game;
    public static boolean m_size_changed = false;
    public static boolean m_ads_changed = false;
    public static boolean m_background_changed = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int rows = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("rows", "7"));
        int columns = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("columns", "4"));
        m_game = new Game(this, rows, columns);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_about) {
            Intent intentabout = new Intent(this, about.class);
            startActivity(intentabout);
            return true;
        }
        if (id == R.id.action_settings) {
            Intent intentsettings = new Intent(this, settings.class);
            startActivity(intentsettings);
            return true;
        }
        if (id == R.id.restart) {
            m_game.reset();
            return true;
        }
        if (id == R.id.stats) {
            Intent intentsettings = new Intent(this, Stats.class);
            startActivity(intentsettings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        m_game.onScreenRotate();
    }

    public void changeBackground(int m_rows) {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout);

        RelativeLayout relativeparent = (RelativeLayout) findViewById(R.id.parent);

        if(PreferenceManager.getDefaultSharedPreferences(this).getString("backgroundpreference", "None").equals("Wood")) {
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.background_image);
            Rect rect = new Rect();
            Window win = this.getWindow();
            win.getDecorView().getWindowVisibleDisplayFrame(rect);
            int statusBarHeight = rect.top;
            int contentViewTop = win.findViewById(Window.ID_ANDROID_CONTENT).getTop();
            int titleBarHeight = contentViewTop - statusBarHeight;
            DisplayMetrics metrics = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int screenHeight = metrics.heightPixels;
            int screenWidth = metrics.widthPixels;
            int layoutHeight = Math.max(screenHeight,screenWidth) - (titleBarHeight + statusBarHeight);

            if(layoutHeight <  m_rows * (RoundButton.size + RoundButton.padding)) {
                relativeparent.setBackgroundResource(0);
                BitmapDrawable ob = new BitmapDrawable(getResources(), Bitmap.createBitmap(bitmap, 0,0, Math.min(screenWidth,bitmap.getWidth()), Math.min(m_rows * (RoundButton.size + RoundButton.padding),bitmap.getHeight())));
                relativeLayout.setBackgroundDrawable(ob);
            } else {
                relativeLayout.setBackgroundResource(0);
                relativeparent.setBackgroundResource(R.drawable.background_image);

            }
        } else {
            relativeparent.setBackgroundResource(0);
            relativeLayout.setBackgroundResource(0);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(m_size_changed) {
            m_size_changed = false;
            ((RelativeLayout) findViewById(R.id.relativelayout)).removeAllViews();
            int rows = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("rows", "7"));
            int columns = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("columns", "4"));
            m_game = new Game(this,rows,columns);
        } else if(m_background_changed) {
            m_background_changed = false;
            changeBackground(Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("rows", "7")));
        }
    }
}
