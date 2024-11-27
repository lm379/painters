package cn.lm379.painters;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.AdapterView;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    int[] imageResources = {
            R.drawable.image1, // 请确保你已添加这些图片资源
            R.drawable.image2,
            R.drawable.image0,
            R.drawable.image3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridView);

        // 自定义GridView适配器来显示图片
        ImageAdapter adapter = new ImageAdapter(this, imageResources);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                // 点击图片时，进入涂鸦页面
                Intent intent = new Intent(MainActivity.this, DrawingActivity.class);
                intent.putExtra("imageResource", imageResources[position]);
                startActivity(intent);
            }
        });
    }
}