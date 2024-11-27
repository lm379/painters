package cn.lm379.painters;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import androidx.appcompat.app.AppCompatActivity;

public class DrawingActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "DrawingPrefs";
    private static final String PREF_CURRENT_TEMPLATE = "TemplateIndex";
    private ImageView imageView;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;
    private int currentColor = Color.BLACK;
    private int currentTemplate = 0;
    private Button saveButton, deleteButton, returnButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        // 加载保存的模板序号
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentTemplate = preferences.getInt(PREF_CURRENT_TEMPLATE, 0);

        imageView = findViewById(R.id.imageView);
        saveButton = findViewById(R.id.saveButton);
        saveButton.setText(R.string.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setText(R.string.deleteButton);
        returnButton = findViewById(R.id.returnButton);
        returnButton.setText(R.string.returnButton);
        setupButtonListeners();

        // 在布局渲染完成后再获取 ImageView 的宽高
        imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                int imageViewWidth = imageView.getWidth();
                int imageViewHeight = imageView.getHeight();

                if (imageViewWidth > 0 && imageViewHeight > 0) {
                    // 只初始化一次 Bitmap 和 Canvas
                    initializeBitmap(imageViewWidth, imageViewHeight);
                    // 移除监听器，避免重复初始化
                    imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                }

                return true;
            }
        });


        ImageButton redButton = findViewById(R.id.redButton);
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paint.setColor(Color.RED);
                currentColor = Color.RED;
            }
        });


        ImageButton greenButton = findViewById(R.id.greenButton);
        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paint.setColor(Color.GREEN);
                currentColor = Color.GREEN;
            }
        });


        ImageButton blueButton = findViewById(R.id.blueButton);
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paint.setColor(Color.BLUE);
                currentColor = Color.BLUE;
            }
        });


        Button switchButton = findViewById(R.id.switchButton);
        switchButton.setText(R.string.switchButton);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTemplate = (currentTemplate + 1) % 4;
                initializeBitmap(imageView.getWidth(), imageView.getHeight());
            }
        });
    }

    private void setupButtonListeners() {
        // 保存按钮
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理保存逻辑（此处为示例）
                saveDrawing();
            }
        });

        // 删除按钮
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理删除逻辑（此处为示例）
                deleteDrawing();
            }
        });

        // 返回按钮，跳转回图片选择页面
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToImageSelection();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 保存当前模板序号
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREF_CURRENT_TEMPLATE, currentTemplate);
        editor.apply();
    }


    private void saveDrawing() {
        // 获取存储路径，在外部存储的应用专属目录中保存
        File storageDir = getExternalFilesDir(null);
        if (storageDir != null) {
            File file = new File(storageDir, "drawing.png");

            try (FileOutputStream fos = new FileOutputStream(file)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);  // 将 Bitmap 压缩成 PNG 格式并保存
                Toast.makeText(DrawingActivity.this, R.string.drawingSaved, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(DrawingActivity.this, R.string.saveFailed+ ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(DrawingActivity.this, R.string.connotAccessStorage, Toast.LENGTH_SHORT).show();
        }
    }

    // 删除绘图的功能（此处为示例逻辑）
    private void deleteDrawing() {
        // 清空当前的绘图内容
        imageView.setImageDrawable(null);
        Toast.makeText(DrawingActivity.this, R.string.photoDeleted, Toast.LENGTH_SHORT).show();
    }

    // 跳转回图片选择页面（假设是 MainActivity）
    private void returnToImageSelection() {
        Intent intent = new Intent(DrawingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();  // 如果不希望回到绘图页面，可以调用 finish() 来销毁当前活动
    }






    private void initializeBitmap(int width, int height) {
        // 创建一个与 ImageView 大小相同的 Bitmap 和 Canvas
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);


        // 根据当前模板加载不同的图片
        BitmapDrawable drawable;
        switch (currentTemplate) {
            case 0:
                drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.image0);
                break;
            case 1:
                drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.image1);
                break;
            case 2:
                drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.image2);
                break;
            case 3:
                drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.image3);
                break;
            default:
                drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.image0);
                break;
        }
        Bitmap templateBitmap = drawable.getBitmap();

        // 绘制模板图像到 canvas
        canvas.drawBitmap(templateBitmap, 0, 0, null);


        paint = new Paint();
        paint.setColor(currentColor);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);

        // 设置 ImageView 显示图片模板
        imageView.setImageBitmap(bitmap);

        // 设置触摸监听器，处理涂鸦
        imageView.setOnTouchListener(new View.OnTouchListener() {
            float x = 0, y = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float newX = event.getX();
                float newY = event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = newX;
                        y = newY;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        canvas.drawLine(x, y, newX, newY, paint);
                        x = newX;
                        y = newY;

                        // 更新 ImageView 显示的 Bitmap
                        imageView.setImageBitmap(bitmap); // 这里重新设置图片来刷新界面
                        imageView.invalidate(); // 刷新 ImageView，确保显示更新


                        break;
                }
                return true;
            }
        });
    }
}