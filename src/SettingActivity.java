package com.example.nitesh.housingmanagmentapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
import com.turkialkhateeb.materialcolorpicker.ColorListener;

public class SettingActivity extends AppCompatActivity {

    private Switch mySwitch;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button button;
    Methods methods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        } else
            setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setTheme(Constant.theme);
        setContentView(R.layout.activity_setting);


        methods=new Methods();
        button=(Button)findViewById(R.id.changecolor);
        sharedPreferences=getSharedPreferences("setting",MODE_PRIVATE);
        editor=sharedPreferences.edit();

        colorize();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorChooserDialog dialog=new ColorChooserDialog(SettingActivity.this);
                dialog.setTitle("Select Color");
                dialog.setColorListener(new ColorListener() {
                    @Override
                    public void OnColorClick(View v, int color) {
                        colorize();
                        Constant.color=color;
                        methods.setColorTheme();
                        editor.putInt("color",color);
                        editor.putInt("theme",Constant.theme);
                        editor.apply();
                        Intent intent=new Intent(SettingActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
                dialog.show();
            }
        });
        mySwitch = (Switch) findViewById(R.id.switchchanger);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            mySwitch.setChecked(true);
        }
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    if (mySwitch.isChecked()) {
                        LinearLayout li = (LinearLayout) findViewById(R.id.linear);
                        li.setBackgroundColor(getResources().getColor(R.color.bblack));
                        restart();
                    }
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    restart();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void colorize(){
        ShapeDrawable d=new ShapeDrawable(new OvalShape());
        d.setBounds(58,58,58,58);
        d.getPaint().setStyle(Paint.Style.FILL);
        d.getPaint().setColor(Constant.color);
        button.setBackground(d);
    }

    private void restart() {
        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
        finish();
    }
}
