package org.ithot.android.test;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.ithot.android.view.SeekView;
import org.ithot.android.view.listener.SVRangeMapCallback;

public class ExampleActivity extends Activity {

    SeekView seekView;
    TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekView = findViewById(R.id.seek_view);
        tvInfo = findViewById(R.id.tv_info);
        /* if setSVCallback onStep disabled
        seekView.setSVCallback(new SVRangeMapCallback(-20, 30) {
            @Override
            public void step(int progress) {
                tvInfo.setText("progress:" + progress);
            }
        });
        */
        seekView.setSVCallback(new SVRangeMapCallback(-20, 30) {
            @Override
            public void step(int progress) {
                tvInfo.setText("progress:" + progress);
            }

            @Override
            public void start() {
                Toast.makeText(ExampleActivity.this, "start", Toast.LENGTH_SHORT).show();
                // touch start
            }

            @Override
            public void end() {
                Toast.makeText(ExampleActivity.this, "end", Toast.LENGTH_SHORT).show();
                // touch end
            }
        });
    }

    public void SetIndicatorColor(View v) {
        seekView.setIndicatorColor(Color.parseColor("#f0ad4e"));
    }

    public void SetInitPos(View v) {
        seekView.init(80);
        tvInfo.setText("progress:" + 80);
    }

    public void SetInitPosRangeMap(View v) {
        seekView.init(10, -20, 30);
        tvInfo.setText("progress:" + 10);
    }

    public void SetForegroundColor(View v) {
        seekView.setSeekForegroundColor(Color.parseColor("#337ab7"));
    }

    public void SetBackgroundColor(View v) {
        seekView.setSeekBackgroundColor(Color.parseColor("#5bc0de"));
    }

    public void onStep(int progress) {
        tvInfo.setText("progress:" + progress);
    }
}