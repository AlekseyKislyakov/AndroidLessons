package kislyakov.a11_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final CustomDiagramView view = findViewById(R.id.custom_diagram_view);
        view.setData(5);//количество столбцов, значения задаются случайно
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.showAnimation();
                Log.d("LES", "OnClick animation");
            }
        });

    }
}
