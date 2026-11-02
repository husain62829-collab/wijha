package com.example.wijha; // أو com.example.wijha حسب اسم الحزمة في مشروعك

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // 1. استقبال البيانات من صفحة الحجز
        Intent intent = getIntent();
        String destName = intent.getStringExtra("DEST_NAME");
        String depCity = intent.getStringExtra("DEP_CITY");
        String depDate = intent.getStringExtra("DEP_DATE");
        String retDate = intent.getStringExtra("RET_DATE");
        int finalPrice = intent.getIntExtra("FINAL_PRICE", 0);
        boolean hasHotel = intent.getBooleanExtra("HAS_HOTEL", false);
        boolean hasAirport = intent.getBooleanExtra("HAS_AIRPORT", false);

        String tripType = intent.getStringExtra("TRIP_TYPE");
        if (tripType == null || tripType.isEmpty()) {
            tripType = "غير محدد";
        }

        // ✅ استقبال اسم المستخدم
        String passengerName = intent.getStringExtra("USERNAME_KEY");
        if (passengerName == null || passengerName.isEmpty()) {
            passengerName = "مسافر";
        }

        if (depCity == null || depCity.isEmpty()) depCity = "الأحساء";
        if (destName == null || destName.isEmpty()) destName = "الوجهة";

        // 2. ربط عناصر الواجهة (تم الحفاظ على جميع الـ IDs)
        TextView tvDestName = findViewById(R.id.tvDestName);
        TextView tvDestIataOut = findViewById(R.id.tvDestIataOut);
        TextView tvOriginNameOut = findViewById(R.id.tvOriginNameOut);
        TextView tvOriginIataOut = findViewById(R.id.tvOriginIataOut);
        TextView tvDepDate = findViewById(R.id.tvDepDate);

        TextView tvDestNameReturn = findViewById(R.id.tvDestNameReturn);
        TextView tvDestIataRet = findViewById(R.id.tvDestIataRet);
        TextView tvOriginNameRet = findViewById(R.id.tvOriginNameRet);
        TextView tvOriginIataRet = findViewById(R.id.tvOriginIataRet);
        TextView tvRetDate = findViewById(R.id.tvRetDate);

        TextView tvTotalPrice = findViewById(R.id.tvTotalPrice);
        TextView tvHotelService = findViewById(R.id.tvHotelService);
        TextView tvAirportService = findViewById(R.id.tvAirportService);

        TextView tvAirline = findViewById(R.id.tvAirline);
        TextView tvFlightNum = findViewById(R.id.tvFlightNum);
        TextView tvPassengerName = findViewById(R.id.tvPassengerName);
        TextView tvSeatNumber = findViewById(R.id.tvSeatNumber);
        TextView tvWeight = findViewById(R.id.tvWeight);
        TextView tvDurationDep = findViewById(R.id.tvDurationDep);
        TextView tvDepTime = findViewById(R.id.tvDepTime);
        TextView tvDurationRet = findViewById(R.id.tvDurationRet);
        TextView tvRetTime = findViewById(R.id.tvRetTime);
        TextView btnReturnHome = findViewById(R.id.btnReturnHome);

        // 3. تعبئة البيانات الأساسية
        tvOriginNameOut.setText(depCity);
        tvOriginIataOut.setText("(" + getIataCode(depCity) + ")");
        tvDestName.setText(destName);
        tvDestIataOut.setText("(" + getIataCode(destName) + ")");
        tvDepDate.setText(depDate);

        tvDestNameReturn.setText(depCity);
        tvDestIataRet.setText("(" + getIataCode(depCity) + ")");
        tvOriginNameRet.setText(destName);
        tvOriginIataRet.setText("(" + getIataCode(destName) + ")");
        tvRetDate.setText(retDate);

        // ✅ تنسيق السعر ليكون أنيقاً (مثلاً: 8,750 ريال)
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        String formattedPrice = numberFormat.format(finalPrice);
        tvTotalPrice.setText("السعر الإجمالي: " + formattedPrice + " ريال");

        // 4. توليد بيانات متغيرة للرحلة
        Random random = new Random();

        // ✅ وضع اسم المستخدم هنا بدلاً من العشوائية
        tvPassengerName.setText(passengerName);

        // المنطق المحدث لاختيار الخطوط الجوية
        String airlineName = "طيران ناس";
        String flightCode = "XY";
        String duration = "1h 30m";

        if (destName.contains("صلالة")) {
            airlineName = "طيران عمان"; flightCode = "WY"; duration = "2h 15m";
        } else if (destName.contains("المالديف")) {
            airlineName = "طيران الإمارات"; flightCode = "EK"; duration = "4h 45m";
        } else if (destName.contains("سانتوريني")) {
            airlineName = "الخطوط التركية"; flightCode = "TK"; duration = "5h 20m";
        } else if (destName.contains("العلا") || destName.contains("أبها") || destName.contains("جدة")) {
            airlineName = "الخطوط الجوية السعودية"; flightCode = "SV";
        }

        // [تم التعديل] تصحيح عرض اسم الخطوط الجوية بدلاً من نوع الرحلة
        tvAirline.setText(airlineName);
        tvFlightNum.setText(flightCode + (100 + random.nextInt(899)));

        tvDurationDep.setText(duration);
        tvDurationRet.setText(duration);

        // توليد أوقات عشوائية
        tvDepTime.setText(String.format(Locale.getDefault(), "%02d:%02d %s", 1 + random.nextInt(11), random.nextInt(60), random.nextBoolean() ? "ص" : "م"));
        tvRetTime.setText(String.format(Locale.getDefault(), "%02d:%02d %s", 1 + random.nextInt(11), random.nextInt(60), random.nextBoolean() ? "ص" : "م"));

        // توليد المقعد
        char seatLetter = (char) ('A' + random.nextInt(6));
        int seatNum = 1 + random.nextInt(30);
        tvSeatNumber.setText(seatNum + String.valueOf(seatLetter));

        // الوزن
        tvWeight.setText(destName.contains("المالديف") || destName.contains("سانتوريني") ? "30Kg" : "20Kg");

        // 5. الخيارات الإضافية (تظهر فقط إذا اختارها العميل)
        if (hasHotel) {
            tvHotelService.setVisibility(View.VISIBLE);
        }
        if (hasAirport) {
            tvAirportService.setVisibility(View.VISIBLE);
        }

        // 6. زر العودة
        btnReturnHome.setOnClickListener(v -> finish());
    }

    // دالة مساعدة لجلب أكواد المطارات (IATA)
    private String getIataCode(String city) {
        switch (city) {
            case "الأحساء": return "HOF";
            case "الدمام":
            case "الخبر":
            case "القطيف":
            case "الجبيل": return "DMM";
            case "الرياض": return "RUH";
            case "جدة":
            case "مكة المكرمة": return "JED";
            case "المدينة المنورة": return "MED";
            case "أبها": return "AHB";
            case "تبوك": return "TUU";
            case "حائل": return "HAS";
            case "صلالة": return "SLL";
            case "سانتوريني": return "JTR";
            case "المالديف": return "MLE";
            case "العلا": return "ULH";
            default: return "APT";
        }
    }
}