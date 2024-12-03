package cn.lm379.painters;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    int[] imageResources = {
            R.drawable.image0, // 请确保你已添加这些图片资源
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridView);
        // 从 SharedPreferences 中获取当前模板
        SharedPreferences preferences = getSharedPreferences(DrawingActivity.PREFS_NAME, 0);
        int currentTemplate = preferences.getInt(DrawingActivity.PREF_CURRENT_TEMPLATE, 0);
        boolean firstRun = preferences.getBoolean("firstRun", true);

        ImageAdapter adapter = new ImageAdapter(this, imageResources);
        Intent intent = new Intent(MainActivity.this, DrawingActivity.class);
        gridView.setAdapter(adapter);
        if (firstRun && currentTemplate != -1) {
            // 如果当前模板不是 -1，且是第一次运行，则直接进入涂鸦页面
            intent.putExtra("currentTemplate", currentTemplate);
            intent.putExtra("imageResource", imageResources[currentTemplate]);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();
            startActivity(intent);
            finish();
        } else {
            // 自定义GridView适配器来显示图片
            gridView.setOnItemClickListener((parent, view, position, id) -> {
                // 点击图片时，进入涂鸦页面
                intent.putExtra("currentTemplate", position);
                intent.putExtra("imageResource", imageResources[position]);
                startActivity(intent);
            });
        }
    }
}