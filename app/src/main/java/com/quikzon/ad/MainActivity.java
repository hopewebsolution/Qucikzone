package com.quikzon.ad;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.lbl_title)
    TextView title;
    @BindView(R.id.input_name)
    EditText subtitle;
    @BindView(R.id.btn_enter)
    Button footer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        title.setText("title");
        subtitle.setText("subtitle");
        footer.setText("footer");
    }
    @OnClick(R.id.btn_enter)
    public void onButtonClick(View view) {
        Toast.makeText(getApplicationContext(), "You have entered: ",
                Toast.LENGTH_SHORT).show();
    }
}
