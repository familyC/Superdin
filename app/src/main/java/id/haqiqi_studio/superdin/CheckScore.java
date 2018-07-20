package id.haqiqi_studio.superdin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheckScore extends AppCompatActivity {
    GameSetting setting = new GameSetting();
    TextView score;
    Button share;
    int bestScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_score);

        score = (TextView) findViewById(R.id.score);
        share = (Button) findViewById(R.id.share);

        bestScore = setting.getBestScore(getApplicationContext(), "score");
        score.setText(String.valueOf(bestScore));

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTo("Wiihh, Score aku " + score.getText() + " nih, saingin aku yok! \n Dengan download dan mainkan Game nya disini https://play.google.com/store/apps/details?id=id.haqiqi_studio.superdin");
            }
        });


    }

    void shareTo(String txt) {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_TEXT,txt);
        i.setType("text/plain");
        startActivity(Intent.createChooser(i, "Bagikan Ke:"));
    }

}
