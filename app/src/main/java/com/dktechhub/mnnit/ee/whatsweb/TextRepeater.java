package com.dktechhub.mnnit.ee.whatsweb;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class TextRepeater extends AppCompatActivity {
    private Button share,generate,copy;
    private EditText toCopy,noOfTimes;
    private TextView generated;
    private CheckBox includeNewLine,includeAllChars;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_repeater);
        AdView mAdView = findViewById(R.id.adview);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        share=findViewById(R.id.share);
        generate=findViewById(R.id.generate);
        copy=findViewById(R.id.copy);

        toCopy=findViewById(R.id.textToCopy);
        noOfTimes=findViewById(R.id.noOfTimes);

        generated=findViewById(R.id.generatedText);

       // includeAllChars=findViewById(R.id.includeOtherCharecters);
        includeNewLine=findViewById(R.id.includeNewLine);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateText();
            }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareText();
            }
        });
        TextWatcher textWatcher1 =new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               // Toast.makeText(TextRepeater.this, "before text\n"+s+start+' '+after+' '+count, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(TextRepeater.this, "on text\n"+s+start+' '+before+' '+count, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void afterTextChanged(Editable s) {
                //Toast.makeText(TextRepeater.this, "before text\n"+s, Toast.LENGTH_SHORT).show();
                b1= s.toString().length() != 0;
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
                String e = s.toString();

                b2=(!e.equals("")) &&e.length() <4 && Integer.parseInt(e)>0;
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
        if(b1&&b2)
        {

            generate.setEnabled(true);
        }else
        {

            generate.setEnabled(false);
        }
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
}