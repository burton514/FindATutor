package com.example.goodsmanagementapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private ArrayList<CheckBox> checkBoxList = new ArrayList<>();
    private Button btnEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        LinearLayout layout = findViewById(R.id.layout_checkboxes);
        btnEnter = findViewById(R.id.btn_enter);

        // Tạo danh sách Checkbox từ Lớp 1 đến Lớp 12
        for (int i = 1; i <= 12; i++) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText("Lớp " + i);
            checkBox.setId(View.generateViewId());
            layout.addView(checkBox);
            checkBoxList.add(checkBox);
        }
        CheckBox checkBox_Math = new CheckBox(this);
        checkBox_Math.setText("Môn Toán");
        checkBox_Math.setId(View.generateViewId());
        layout.addView(checkBox_Math);
        checkBoxList.add(checkBox_Math);

        CheckBox checkBox_Lit = new CheckBox(this);
        checkBox_Lit.setText("Môn Văn");
        checkBox_Lit.setId(View.generateViewId());
        layout.addView(checkBox_Lit);
        checkBoxList.add(checkBox_Lit);

        CheckBox checkBox_Eng = new CheckBox(this);
        checkBox_Eng.setText("Môn Tiếng Anh");
        checkBox_Eng.setId(View.generateViewId());
        layout.addView(checkBox_Eng);
        checkBoxList.add(checkBox_Eng);


        // Xử lý sự kiện khi nhấn nút Enter
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> selectedClasses = new ArrayList<>();
                for (CheckBox checkBox : checkBoxList) {
                    if (checkBox.isChecked()) {
                        selectedClasses.add(checkBox.getText().toString());
                    }
                }

                // Trả kết quả về HomeFragment (dưới dạng ArrayList<String>)
                Intent resultIntent = new Intent();
                resultIntent.putStringArrayListExtra("search_result", selectedClasses);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
