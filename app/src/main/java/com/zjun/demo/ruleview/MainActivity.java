package com.zjun.demo.ruleview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjun.widget.MoneySelectRulerView;
import com.zjun.widget.RulerView;
import com.zjun.widget.TimeRulerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView tvValue;
    private RulerView gvRule;
    private MoneySelectRulerView msrvMoney;
    private TextView tvMoney;
    private EditText etMoney;
    private TimeRulerView trvTime;
    private TextView tvTime;

    private float moneyBalance;
    private boolean isMoneySloped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTime = findViewById(R.id.tv_time);
        trvTime = findViewById(R.id.trv_time);
        tvMoney = findViewById(R.id.tv_money);
        etMoney = findViewById(R.id.et_new_money);
        msrvMoney = findViewById(R.id.msrv_money);
        tvValue = findViewById(R.id.tv_value);
        gvRule = findViewById(R.id.gv_1);
        gvRule.setValue(0, 500, 0, 1, 10);


        tvValue.setText(Float.toString(gvRule.getCurrentValue()));
        gvRule.setOnValueChangedListener(new RulerView.OnValueChangedListener() {
            @Override
            public void onValueChanged(float value) {
                tvValue.setText(Float.toString(value));
                if (value > 0 && value <= 50) {
                    gvRule.setBgColor(Color.parseColor("#00966E"));
                } else if (value > 50 && value <= 100) {
                    gvRule.setBgColor(Color.parseColor("#FFE08D"));
                } else if (value > 100 && value <= 150) {
                    gvRule.setBgColor(Color.parseColor("#FDA577"));
                } else if (value > 150 && value <= 200) {
                    gvRule.setBgColor(Color.parseColor("#F16966"));
                } else if (value > 200 && value <= 300) {
                    gvRule.setBgColor(Color.parseColor("#C463EF"));
                } else if (value > 300 && value <= 500) {
                    gvRule.setBgColor(Color.parseColor("#A0596D"));
                }
            }
        });

        tvMoney.setText(Integer.toString(msrvMoney.getValue()));
        moneyBalance = msrvMoney.getBalance();
        msrvMoney.setOnValueChangedListener(new MoneySelectRulerView.OnValueChangedListener() {
            @Override
            public void onValueChanged(int newValue) {
                tvMoney.setText(Integer.toString(newValue));
                if (newValue > moneyBalance) {
                    if (!isMoneySloped) {
                        isMoneySloped = true;
                        Snackbar.make(msrvMoney, "超出额度", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    if (isMoneySloped) {
                        isMoneySloped = false;
                    }
                }
            }
        });

        trvTime.setOnTimeChangedListener(new TimeRulerView.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(int newTimeValue) {
                tvTime.setText(TimeRulerView.formatTimeHHmmss(newTimeValue));
            }
        });
        // 模拟时间段数据
        List<TimeRulerView.TimePart> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TimeRulerView.TimePart part = new TimeRulerView.TimePart();
            part.startTime = i * 1000;
            part.endTime = part.startTime + new Random().nextInt(1000);
            list.add(part);
        }
        trvTime.setTimePartList(list);

    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_rule_indicator:
                toggleSettingsShow(R.id.ll_rule_settings);
                break;
            case R.id.btn_50:
                gvRule.setCurrentValue(gvRule.getMinValue() == 11 ? 15 : 50);
                break;
            case R.id.btn_change:
                toggleValue();
                break;
            case R.id.tv_money_indicator:
                toggleSettingsShow(R.id.ll_money_settings);
                break;
            case R.id.btn_set_money:
                float money = getMoney();
                msrvMoney.setValue(money);
                break;
            case R.id.btn_set_balance:
                moneyBalance = getMoney();
                msrvMoney.setBalance(moneyBalance);
                isMoneySloped = false;
                break;
            default:
                break;
        }
    }

    private void toggleSettingsShow(@IdRes int layoutId) {
        LinearLayout llSettings = findViewById(layoutId);
        llSettings.setVisibility(llSettings.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
    }

    private void toggleValue() {
        if (gvRule.getMinValue() == 11) {
            gvRule.setValue(0, 100, 50, 0.1f, 10);
        } else {
            gvRule.setValue(11, 20, 15, 0.2f, 5);
        }
    }

    private float getMoney() {
        String moneyStr = etMoney.getText().toString();
        if (moneyStr.isEmpty()) {
            moneyStr = "0";
        }
        return Float.parseFloat(moneyStr);
    }

}
