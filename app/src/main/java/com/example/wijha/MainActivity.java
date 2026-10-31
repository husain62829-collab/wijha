package com.example.wijha;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton;

public class MainActivity extends Activity {

    EditText editName;
    RadioGroup radioGroupServices;
    CheckBox checkExtra1, checkExtra2;
    TextView txtSelection;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editName);
        radioGroupServices =
                findViewById(R.id.radioGroupServices);
        checkExtra1 = findViewById(R.id.checkExtra1);
        checkExtra2 = findViewById(R.id.checkExtra2);
        txtSelection = findViewById(R.id.txtSelection);
        btnNext = findViewById(R.id.btnNext);

        // الاستجابة لاختيار الإضافات
        CompoundButton.OnCheckedChangeListener listener =
                (buttonView, isChecked) -> {

                    String message =
                            buttonView.getText().toString();

                    if (isChecked) {
                        message += " تمت إضافتها";
                    } else {
                        message += " تمت إزالتها";
                    }

                    Toast.makeText(
                            MainActivity.this,
                            message,
                            Toast.LENGTH_SHORT
                    ).show();

                    updateExtras();
                };

        checkExtra1.setOnCheckedChangeListener(listener);
        checkExtra2.setOnCheckedChangeListener(listener);

        // الاستجابة لاختيار الخدمة
        radioGroupServices.setOnCheckedChangeListener(
                (group, checkedId) -> {

                    RadioButton selected =
                            findViewById(checkedId);

                    if (selected != null) {
                        Toast.makeText(
                                MainActivity.this,
                                "تم اختيار: " + selected.getText(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });

        // الانتقال إلى الصفحة الثانية
        btnNext.setOnClickListener(v -> {

            String name =
                    editName.getText().toString().trim();

            if (name.isEmpty()) {
                editName.setError("يرجى إدخال الاسم");
                editName.requestFocus();
                return;
            }

            int selectedId =
                    radioGroupServices.getCheckedRadioButtonId();

            if (selectedId == -1) {
                Toast.makeText(
                        MainActivity.this,
                        "يرجى اختيار الخدمة",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            RadioButton selected =
                    findViewById(selectedId);

            Intent intent = new Intent(
                    MainActivity.this,
                    DetailsActivity.class
            );

            intent.putExtra("name", name);
            intent.putExtra(
                    "service",
                    selected.getText().toString()
            );
            intent.putExtra("extras", getExtras());

            startActivity(intent);
        });
    }

    private String getExtras() {

        String extras = "";

        if (checkExtra1.isChecked()) {
            extras = checkExtra1.getText().toString();
        }

        if (checkExtra2.isChecked()) {
            if (!extras.isEmpty()) {
                extras += "، ";
            }
            extras += checkExtra2.getText().toString();
        }

        if (extras.isEmpty()) {
            return "لا توجد إضافات";
        }

        return extras;
    }

    private void updateExtras() {
        txtSelection.setText(
                "الإضافات: " + getExtras()
        );
    }
}