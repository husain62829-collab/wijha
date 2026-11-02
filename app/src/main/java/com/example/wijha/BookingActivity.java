package com.example.wijha;

import android.animation.LayoutTransition;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity {

    // عناصر الصفحة
    private TextView domesticButton;
    private TextView internationalButton;
    private Spinner departureSpinner;
    private LinearLayout destinationContainer;

    // نوع الرحلة
    private boolean isInternational = true;

    // التواريخ
    private String departureDate = "";
    private String returnDate = "";

    private String currentUsername = "";

    // قائمة الوجهات
    private final ArrayList<Destination> destinations = new ArrayList<>();

    // بيانات الوجهة
    static class Destination {
        String name;
        String country;
        int image;
        int price;

        Destination(String name, String country, int image, int price) {
            this.name = name;
            this.country = country;
            this.image = image;
            this.price = price;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // استقبال اسم المستخدم من صفحة تسجيل الدخول
        currentUsername = getIntent().getStringExtra("USERNAME_KEY");
        if (currentUsername == null || currentUsername.isEmpty()) {
            currentUsername = "مسافر";
        }

        domesticButton = findViewById(R.id.domesticButton);
        internationalButton = findViewById(R.id.internationalButton);
        departureSpinner = findViewById(R.id.departureSpinner);
        destinationContainer = findViewById(R.id.destinationContainer);

        setupDeparture();
        setupTripButtons();
        showInternationalDestinations();
    }

    // نقطة المغادرة
    private void setupDeparture() {
        String[] regions = {
                "الأحساء", "الدمام", "الخبر", "الرياض", "جدة",
                "مكة المكرمة", "المدينة المنورة", "القطيف",
                "الجبيل", "أبها", "تبوك", "حائل"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                regions
        ) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                TextView text = (TextView) super.getView(position, convertView, parent);
                // [تم التعديل] استخدام لون الهوية الجديد
                text.setTextColor(ContextCompat.getColor(BookingActivity.this, R.color.text_primary));
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                text.setTypeface(null, Typeface.BOLD);
                text.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                return text;
            }

            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                TextView text = (TextView) super.getDropDownView(position, convertView, parent);
                // [تم التعديل] إصلاح الخطأ واستخدام لون الهوية
                text.setTextColor(ContextCompat.getColor(BookingActivity.this, R.color.text_secondary));
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                text.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                text.setPadding(20, 15, 20, 15);
                return text;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departureSpinner.setAdapter(adapter);
        departureSpinner.setSelection(0);
    }

    // اختيار نوع الرحلة
    private void setupTripButtons() {
        domesticButton.setOnClickListener(v -> {
            isInternational = false;
            updateTripButtons();
            showDomesticDestinations();
        });

        internationalButton.setOnClickListener(v -> {
            isInternational = true;
            updateTripButtons();
            showInternationalDestinations();
        });

        updateTripButtons();
    }

    // ألوان أزرار نوع الرحلة
    private void updateTripButtons() {
        if (isInternational) {
            // [تم التعديل] استخدام الـ Drawables والـ Colors الجديدة من الهوية
            internationalButton.setBackgroundResource(R.drawable.bg_toggle_selected);
            internationalButton.setTextColor(ContextCompat.getColor(this, R.color.text_on_primary));
            domesticButton.setBackgroundResource(R.drawable.bg_toggle_unselected);
            domesticButton.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        } else {
            domesticButton.setBackgroundResource(R.drawable.bg_toggle_selected);
            domesticButton.setTextColor(ContextCompat.getColor(this, R.color.text_on_primary));
            internationalButton.setBackgroundResource(R.drawable.bg_toggle_unselected);
            internationalButton.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        }
    }

    // الوجهات الدولية
    private void showInternationalDestinations() {
        destinations.clear();
        destinations.add(new Destination("صلالة", "سلطنة عمان", R.drawable.salalah, 8770));
        destinations.add(new Destination("سانتوريني", "اليونان", R.drawable.santorini, 9500));
        destinations.add(new Destination("المالديف", "جزر المالديف", R.drawable.maldives, 12000));
        displayDestinations();
    }

    // الوجهات الداخلية
    private void showDomesticDestinations() {
        destinations.clear();
        destinations.add(new Destination("العلا", "المملكة العربية السعودية", R.drawable.alula, 2500));
        destinations.add(new Destination("أبها", "المملكة العربية السعودية", R.drawable.abha, 1800));
        destinations.add(new Destination("جدة", "المملكة العربية السعودية", R.drawable.jeddah, 1500));
        displayDestinations();
    }

    // عرض بطاقات الوجهات
    private void displayDestinations() {
        destinationContainer.removeAllViews();

        for (Destination destination : destinations) {
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            // [تم التعديل] استخدام بطاقة الهوية البيضاء بدلاً من الخلفية القديمة
            card.setBackgroundResource(R.drawable.bg_card_white);
            card.setClipToOutline(true);
            card.setElevation(8f); // إضافة ظل للبطاقة
            card.setLayoutTransition(new LayoutTransition());

            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.space_large));
            card.setLayoutParams(cardParams);

            ImageView image = new ImageView(this);
            image.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    (int) getResources().getDimension(R.dimen.height_destination_image)
            ));
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image.setImageResource(destination.image);
            card.addView(image);

            LinearLayout titleRow = new LinearLayout(this);
            titleRow.setOrientation(LinearLayout.HORIZONTAL);
            titleRow.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            titleRow.setGravity(Gravity.CENTER_VERTICAL);
            titleRow.setPadding(18, 14, 18, 14);

            TextView name = new TextView(this);
            name.setText(destination.name + "\n" + destination.country);
            name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            // [تم التعديل] استخدام لون النص الأساسي للهوية
            name.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
            name.setGravity(Gravity.RIGHT);
            name.setTypeface(null, Typeface.BOLD);

            LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
            );

            TextView arrow = new TextView(this);
            arrow.setText("⌄");
            arrow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            arrow.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
            arrow.setGravity(Gravity.CENTER);
            arrow.setPadding(12, 5, 12, 5);
            arrow.setClickable(true);
            arrow.setFocusable(true);

            titleRow.addView(name, nameParams);
            titleRow.addView(arrow);
            card.addView(titleRow);

            LinearLayout details = new LinearLayout(this);
            details.setOrientation(LinearLayout.VERTICAL);
            details.setPadding(20, 10, 20, 20);
            details.setVisibility(View.GONE);
            details.setLayoutTransition(new LayoutTransition());

            // حقل التاريخ
            LinearLayout dateSelector = createDateSelectors();
            details.addView(dateSelector);

            TextView servicesTitle = new TextView(this);
            servicesTitle.setText("خدمات إضافية");
            servicesTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            servicesTitle.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
            servicesTitle.setTypeface(null, Typeface.BOLD);
            servicesTitle.setPadding(0, 12, 0, 5);
            details.addView(servicesTitle);

            CheckBox hotel = new CheckBox(this);
            hotel.setText("حجز فندق");
            hotel.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
            details.addView(hotel);

            CheckBox airport = new CheckBox(this);
            airport.setText("استقبال من المطار");
            airport.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
            details.addView(airport);

            TextView price = new TextView(this);
            price.setText("السعر المقدر: " + destination.price + " ريال");
            price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            price.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
            price.setTypeface(null, Typeface.BOLD);
            price.setPadding(0, 12, 0, 12);
            details.addView(price);

            hotel.setOnCheckedChangeListener((buttonView, checked) ->
                    updatePrice(price, destination.price, hotel, airport));

            airport.setOnCheckedChangeListener((buttonView, checked) ->
                    updatePrice(price, destination.price, hotel, airport));

            TextView confirm = new TextView(this);
            confirm.setText("تأكيد الحجز");
            confirm.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            confirm.setTextColor(ContextCompat.getColor(this, R.color.text_on_primary));
            confirm.setGravity(Gravity.CENTER);
            confirm.setPadding(12, 14, 12, 14);
            // [تم التعديل] استخدام زر التدرج اللوني من الهوية
            confirm.setBackgroundResource(R.drawable.bg_btn_gradient);

            confirm.setOnClickListener(v -> {
                if (departureDate.isEmpty() || returnDate.isEmpty()) {
                    Toast.makeText(this, "يرجى اختيار تواريخ الرحلة", Toast.LENGTH_SHORT).show();
                    return;
                }
                // حساب السعر النهائي
                int finalPrice = destination.price;
                if (hotel.isChecked()) finalPrice += 1200;
                if (airport.isChecked()) finalPrice += 300;

                // الانتقال إلى شاشة التذكرة مع إرسال البيانات
                android.content.Intent intent = new android.content.Intent(BookingActivity.this, DetailsActivity.class);
                intent.putExtra("DEST_NAME", destination.name);
                intent.putExtra("DEST_COUNTRY", destination.country);
                intent.putExtra("DEP_DATE", departureDate);
                intent.putExtra("RET_DATE", returnDate);
                intent.putExtra("FINAL_PRICE", finalPrice);
                intent.putExtra("HAS_HOTEL", hotel.isChecked());
                intent.putExtra("HAS_AIRPORT", airport.isChecked());
                intent.putExtra("USERNAME_KEY", currentUsername);
                intent.putExtra("TRIP_TYPE", isInternational ? "دولي" : "داخلي");
                startActivity(intent);
            });

            details.addView(confirm);
            card.addView(details);

            View.OnClickListener expandListener = v -> {
                if (details.getVisibility() == View.GONE) {
                    details.setVisibility(View.VISIBLE);
                    arrow.setText("⌃");
                } else {
                    details.setVisibility(View.GONE);
                    arrow.setText("⌄");
                }
            };

            arrow.setOnClickListener(expandListener);
            titleRow.setOnClickListener(expandListener);

            destinationContainer.addView(card);
        }
    }

    // دالة إنشاء حقل التاريخ
    private LinearLayout createDateSelectors() {
        LinearLayout wrapper = new LinearLayout(this);
        wrapper.setOrientation(LinearLayout.VERTICAL);
        wrapper.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        TextView dateField = new TextView(this);
        dateField.setText("📅  اختر تواريخ الرحلة");
        dateField.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        dateField.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        dateField.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        dateField.setPadding(30, 25, 30, 25);
        // [تم التعديل] استخدام حقل التاريخ المخطط من الهوية
        dateField.setBackgroundResource(R.drawable.bg_date_field_outlined);
        dateField.setTypeface(null, Typeface.BOLD);
        dateField.setLineSpacing(10f, 1f);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 10, 0, 10);
        dateField.setLayoutParams(params);

        dateField.setOnClickListener(v -> {
            MaterialDatePicker.Builder<Pair<Long, Long>> builder =
                    MaterialDatePicker.Builder.dateRangePicker();
            builder.setTitleText("اختر تاريخ الانطلاق والعودة");

            MaterialDatePicker<Pair<Long, Long>> datePicker = builder.build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                String start = sdf.format(new Date(selection.first));
                String end = sdf.format(new Date(selection.second));

                departureDate = start;
                returnDate = end;

                dateField.setText("📅  تاريخ الرحلة\nمن " + start + "   ←   إلى " + end);
            });

            datePicker.show(getSupportFragmentManager(), "DATE_RANGE_PICKER");
        });

        wrapper.addView(dateField);
        return wrapper;
    }

    // حساب السعر
    private void updatePrice(TextView price, int base, CheckBox hotel, CheckBox airport) {
        int total = base;
        if (hotel.isChecked()) total += 1200;
        if (airport.isChecked()) total += 300;
        price.setText("السعر المقدر: " + total + " ريال");
    }
}