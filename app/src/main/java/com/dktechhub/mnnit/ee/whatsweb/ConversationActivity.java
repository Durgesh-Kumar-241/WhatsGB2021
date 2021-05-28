package com.dktechhub.mnnit.ee.whatsweb;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class ConversationActivity extends AppCompatActivity {
    ImageButton sendButton;
    EditText inputText;
    chatService chatService;
    String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        String name=getIntent().getStringExtra("name");
        number=getIntent().getStringExtra("number");
        String photo_uri=getIntent().getStringExtra("photo_uri");
       ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if(name!=null)
                actionBar.setTitle(name);
            else
                if(number!=null)
                    actionBar.setTitle(number);
            //actionBar.hide();
        }

        inputText=findViewById(R.id.conversationInput);
        sendButton=findViewById(R.id.sendButton);
        sendButton.setEnabled(false);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                sendButton.setEnabled(s.toString().length() > 0);

            }
        });
        new DatabaseManager(this).updateRecentChatList();
       chatService= com.dktechhub.mnnit.ee.whatsweb.chatService.getInstance();




    }

    public void sendMessage()
    {
        if(chatService!=null) {
            chatService.message=inputText.getText().toString();
            chatService.sendMessage(inputText.getText().toString(), number);
            inputText.setText("");
        }

    }
}