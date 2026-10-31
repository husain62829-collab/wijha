package com.example.wijha;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends Activity {

    TextView txtCustomerName, txtService, txtExtras;
    Button btnConfirm, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        txtCustomerName =
                findViewById(R.id.txtCustomerName);
        txtService =
                findViewById(R.id.txtService);
        txtExtras =
                findViewById(R.id.txtExtras);
        btnConfirm =
                findViewById(R.id.btnConfirm);
        btnBack =
                findViewById(R.id.btnBack);

        // استقبال البيانات
        String name =
                getIntent().getStringExtra("name");
        String service =
                getIntent().getStringExtra("service");
        String extras =
                getIntent().getStringExtra("extras");

        // عرض البيانات
        txtCustomerName.setText(
                name == null ? "" : name
        );
        txtService.setText(
                service == null ? "" : service
        );
        txtExtras.setText(
                extras == null ? "لا توجد إضافات" : extras
        );

        // تأكيد الطلب
        btnConfirm.setOnClickListener(v -> {

            Toast.makeText(
                    DetailsActivity.this,
                    "تم تأكيد طلب " +
                            txtCustomerName.getText() +
                            " بنجاح",
                    Toast.LENGTH_LONG
            ).show();

            btnConfirm.setText("تم التأكيد ✓");
            btnConfirm.setEnabled(false);
        });

        // العودة للتعديل
        btnBack.setOnClickListener(v -> finish());
    }
}