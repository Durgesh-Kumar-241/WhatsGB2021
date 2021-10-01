package com.dktechhub.mnnit.ee.whatsweb;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;


public class trpr extends AppCompatActivity {
    private Button share,generate,copy;
    private EditText toCopy,noOfTimes;
    private TextView generated;
    private CheckBox includeNewLine;

    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_repeater);
        loadAd();
        share=findViewById(R.id.share);
        generate=findViewById(R.id.generate);
        copy=findViewById(R.id.copy);

        toCopy=findViewById(R.id.textToCopy);
        noOfTimes=findViewById(R.id.noOfTimes);

        generated=findViewById(R.id.generatedText);


        includeNewLine=findViewById(R.id.includeNewLine);
        generate.setOnClickListener(v -> generateText());
        copy.setOnClickListener(v -> copyToClipboard());
        share.setOnClickListener(v -> shareText());
        TextWatcher textWatcher1 =new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    b1= s.toString().length() != 0;
                }catch(Exception e)
                {
                    b1=false;
                }

                if(!b1)
                    toCopy.setError(getString(R.string.invalidinputtext));
                updateUi();
            }


        };


        TextWatcher textWatcher2 =new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                String e = s.toString();
                b2=(!e.equals("")) &&e.length() <4 && Integer.parseInt(e)>0;
                }
                catch(Exception e)
                {
                    b2=false;
                }
                if(!b2)
                    noOfTimes.setError(getString(R.string.invalidtextinput));
                    updateUi();
            }


        };

        TextWatcher textWatcher3 =new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                b3= s.toString().length() != 0;

                updateUi();

            }


        };
        toCopy.addTextChangedListener(textWatcher1);
        noOfTimes.addTextChangedListener(textWatcher2);
        generated.addTextChangedListener(textWatcher3);



    }
    boolean b1=false,b2=false,b3=false;
    public void updateUi()
    {
        generate.setEnabled(b1 && b2);
        if(b3)
        {
            share.setEnabled(true);
            copy.setEnabled(true);
        }else
        {
            share.setEnabled(false);
            copy.setEnabled(false);
        }
    }
    public void generateText()
    {
        String toCopyText=toCopy.getText().toString();
        int no=Integer.parseInt(noOfTimes.getText().toString());
        StringBuilder sb = new StringBuilder();
        if(includeNewLine.isChecked())
            toCopyText+='\n';
        for(int i=0;i<no;i++)
        {
            sb.append(toCopyText);
        }

        generated.setText(sb.toString());

    }

    public void copyToClipboard()
    {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(getString(R.string.generatedtext),generated.getText().toString());
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(this, getString(R.string.textcopied), Toast.LENGTH_SHORT).show();
    }

    public void shareText()
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,generated.getText().toString());
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent,getString(R.string.sendindtextto)));
    }

    public void loadAd()
    {
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.ban_text_rep));
        LinearLayout linearLayout = findViewById(R.id.banner_container);
        TextView tv = findViewById(R.id.ad_cont_test);
        linearLayout.addView(adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                tv.setVisibility(View.GONE);
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }


    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(adView!=null)
            adView.destroy();
    }
}