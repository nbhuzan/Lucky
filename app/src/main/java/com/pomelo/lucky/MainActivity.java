package com.pomelo.lucky;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private LuckySurfaceView luckySurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        luckySurfaceView = (LuckySurfaceView) findViewById(R.id.lucky);

        final ImageView iv = (ImageView) findViewById(R.id.start);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!luckySurfaceView.isStart()) {
                    luckySurfaceView.start(1,getApplicationContext());
                    iv.setImageResource(R.drawable.stop);
                }else{
                    if(!luckySurfaceView.isEnd()){
                        luckySurfaceView.end();
                        iv.setImageResource(R.drawable.start);
                    }
                }

            }
        });
    }
}
