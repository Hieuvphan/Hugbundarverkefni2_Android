package hi.hugbo.verywowchat.controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class Dah38DebugActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("dh", "Dah38DebugActivity.onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dah_38_debug);

        // mTextView = findViewById(R.id.textView2);


        // mTextView.setText("Go to hell!");
    }
}
