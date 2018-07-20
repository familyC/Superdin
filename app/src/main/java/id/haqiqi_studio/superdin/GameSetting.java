package id.haqiqi_studio.superdin;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class GameSetting extends AppCompatActivity {
    public final String SCORING = "score";
    int score, highScore;

    protected void setBestScore(Context context, String key, int score) {
        SharedPreferences pref = context.getSharedPreferences(SCORING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        highScore = getBestScore(context, key);
        if (score > highScore) {
            editor.putInt(key, score).apply();
        }

    }

    protected int getBestScore(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(SCORING, Context.MODE_PRIVATE);

        return pref.getInt(key, 0);
    }
}
