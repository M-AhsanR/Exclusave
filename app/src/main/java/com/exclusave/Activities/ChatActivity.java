package com.exclusave.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.IntentFilter;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.exclusave.Adapters.Chat_Adapter;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.MyChatService;
import com.exclusave.models.ChatMsgsModel;
import com.exclusave.models.Chat_Data;
import com.exclusave.BuildConfig;
import com.exclusave.R;
import com.soundcloud.android.crop.Crop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<String> returnValue = new ArrayList<>();
    Uri myUri;
    Bitmap bitmap;
    ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();

    RecyclerView chatRecycler;
    TextView typing;
    Button sendmessage;
    ImageView attachment;
    EditText message;
    ArrayList<Chat_Data> chat_datalist = new ArrayList<>();
    Chat_Data data;
    String ChatID = "";

    Options options;
    int reqCode = 0;

    Chat_Adapter chatAdapter;

    Map<String, String> imagebody = new HashMap<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<ChatMsgsModel> chatMsgsModel = new ArrayList<>();

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sharedPreferences = BaseClass.sharedPreferances(ChatActivity.this);
        editor = BaseClass.sharedPreferancesEditor(ChatActivity.this);
        registerReceiver(myReceiver, new IntentFilter((MyChatService.INTENT_FILTER)));

        initilizeviews();

        startChatApiCall();
       // getChatMessages();

        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                typing.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void initilizeviews() {
        chatRecycler = findViewById(R.id.chatrecycler);
        sendmessage = findViewById(R.id.sendbutton);
        message = findViewById(R.id.message_text);
        typing = findViewById(R.id.typing);
        attachment = findViewById(R.id.attachment);

        sendmessage.setOnClickListener(this);
        attachment.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == sendmessage){
            if (message.getText().toString().isEmpty()){
                Toast.makeText(ChatActivity.this, getResources().getString(R.string.emptyMsg), Toast.LENGTH_SHORT);
            }else {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(this.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                typing.setVisibility(View.GONE);
                sendMessage();
               // sendmessagetoChat();
            }
        }else if (view == attachment){
            reqCode = 3333;
            options = Options.init()
                    .setRequestCode(100)
                    .setCount(1);
            Log.e("cameraClick", "yes");
            returnValue.clear();
            Pix.start(ChatActivity.this,                    //Activity or Fragment Instance
                    options);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);

            for (int a = 0; a < returnValue.size(); a++) {
                File imgFile = new File(returnValue.get(a));
//                Uri myUri = Uri.parse(returnValue.get(a));
                myUri = Uri.fromFile(new File(returnValue.get(a)));
                Log.e("getdata", "---->" + myUri);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    Log.e("getdata", "---->" + myBitmap);
//                    ProfilePic.setImageBitmap(myBitmap);
                }
                beginCrop(myUri);

            }
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(ChatActivity.this, options);
                } else {
                    Toast.makeText(ChatActivity.this, "Approve permissions to open Camera", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            if (reqCode == 3333) {
                Toast.makeText(ChatActivity.this, this.getResources().getString(R.string.uploaded), Toast.LENGTH_SHORT).show();
            }
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Crop.getOutput(result));
                bitmapArrayList.add(bitmap);
                sendImage();
                Log.e("chekingBit", bitmap.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void startChatApiCall() {

        String language = BaseClass.checkLanguageFunction(ChatActivity.this);
        Log.e("ReceiverID",sharedPreferences.getString("ReceiverID", ""));
        BaseClass.showCustomLoader(ChatActivity.this);
        Map<String, String> body = new HashMap<String, String>();
        body.put("UserID", sharedPreferences.getString("UserID", " "));
        body.put("ReceiverID", sharedPreferences.getString("ReceiverID", ""));
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
      //  body.put("Language", language);

        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        Log.e("ReceiverID",body.toString());
        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.STARTCHAT, ChatActivity.this, body, header, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                BaseClass.dialog.dismiss();
                if (ERROR.isEmpty()) {
                    Log.e("ChatResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            ChatID = jsonObject.getString("ChatID");

                            JSONArray messagesArray = jsonObject.getJSONArray("ChatMessages");
                            for (int i = 0; i < messagesArray.length(); i++) {
                                JSONObject messagesObject = messagesArray.getJSONObject(i);
                                String ChatMessageID = messagesObject.getString("ChatMessageID");
                                String CompressedMessageImage = messagesObject.getString("CompressedMessageImage");
                                String CreatedAt = messagesObject.getString("CreatedAt");
                                String IsReadByReceiver = messagesObject.getString("IsReadByReceiver");
                                String IsReadBySender = messagesObject.getString("IsReadBySender");
                                String Message = messagesObject.getString("Message");
                                String MessageImage = messagesObject.getString("MessageImage");
                                String ReceiverID = messagesObject.getString("ReceiverID");
                                String ReceiverImage = messagesObject.getString("ReceiverImage");
                                String ReceiverName = messagesObject.getString("ReceiverName");
                                String ReceiverUserID = messagesObject.getString("ReceiverUserID");
                                String SenderID = messagesObject.getString("SenderID");
                                String SenderImage = messagesObject.getString("SenderImage");
                                String SenderName = messagesObject.getString("SenderName");
                                String SenderUserID = messagesObject.getString("SenderUserID");


                                chatMsgsModel.add(new ChatMsgsModel(ChatMessageID,CompressedMessageImage, CreatedAt, IsReadByReceiver, IsReadBySender, Message,MessageImage, ReceiverID, ReceiverImage, ReceiverName,
                                        ReceiverUserID, SenderID, SenderImage, SenderName, SenderUserID));
                            }
                            chatRecycler.setLayoutManager(new LinearLayoutManager(ChatActivity.this));

                            chatAdapter = new Chat_Adapter(ChatActivity.this, chatMsgsModel, new Chat_Adapter.CustomItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {

                                }
                            });
                            chatRecycler.setAdapter(chatAdapter);
                            chatRecycler.scrollToPosition(chatMsgsModel.size()-1);

                        } else {
                            Toast.makeText(ChatActivity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(ChatActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendMessage() {

        String language = BaseClass.checkLanguageFunction(ChatActivity.this);
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        Map<String, String> body = new HashMap<>();
        BaseClass.showCustomLoader(ChatActivity.this);

        body.put("UserID", sharedPreferences.getString("UserID", ""));
        body.put("ReceiverID", sharedPreferences.getString("ReceiverID", ""));
        body.put("Message", message.getText().toString());
        body.put("CreatedAt", ts);
        body.put("ChatID",ChatID);
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
       // body.put("Language",language);

        Log.e("body", body.toString());

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.sendMessage, ChatActivity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("GetChatMessagesResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            message.setText("");
                            BaseClass.dialog.dismiss();
                            JSONObject messagesObject = jsonObject.getJSONObject("message");
                            String ChatMessageID = messagesObject.getString("ChatMessageID");
                            String CompressedMessageImage = messagesObject.getString("CompressedMessageImage");
                            String CreatedAt = messagesObject.getString("CreatedAt");
                            String IsReadByReceiver = messagesObject.getString("IsReadByReceiver");
                            String IsReadBySender = messagesObject.getString("IsReadBySender");
                            String Message = messagesObject.getString("Message");
                            String MessageImage = messagesObject.getString("MessageImage");
                            String ReceiverID = messagesObject.getString("ReceiverID");
                            String ReceiverImage = messagesObject.getString("ReceiverImage");
                            String ReceiverName = messagesObject.getString("ReceiverName");
                            String ReceiverUserID = messagesObject.getString("ReceiverUserID");
                            String SenderID = messagesObject.getString("SenderID");
                            String SenderImage = messagesObject.getString("SenderImage");
                            String SenderName = messagesObject.getString("SenderName");
                            String SenderUserID = messagesObject.getString("SenderUserID");


                            chatMsgsModel.add(new ChatMsgsModel(ChatMessageID,CompressedMessageImage, CreatedAt, IsReadByReceiver, IsReadBySender, Message,MessageImage, ReceiverID, ReceiverImage, ReceiverName,
                                    ReceiverUserID, SenderID, SenderImage, SenderName, SenderUserID));

                            chatAdapter.notifyItemInserted(chatMsgsModel.size()-1);
                            chatRecycler.scrollToPosition(chatMsgsModel.size()-1);

                        } else {
                            BaseClass.dialog.dismiss();
                            Toast.makeText(ChatActivity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(ChatActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendImage() {

        String language = BaseClass.checkLanguageFunction(ChatActivity.this);
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

//        BaseClass.showCustomLoader(ChatActivity.this);

        imgToStringFunction(bitmap);
        imagebody.put("UserID", sharedPreferences.getString("UserID", ""));
        imagebody.put("ReceiverID", sharedPreferences.getString("ReceiverID", ""));
        imagebody.put("CreatedAt", ts);
        imagebody.put("ChatID",ChatID);
        imagebody.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
       // imagebody.put("Language", language);

        Log.e("body", imagebody.toString());

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.sendMessage, ChatActivity.this, imagebody, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("GetChatImageResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            BaseClass.dialog.dismiss();
                            JSONObject messagesObject = jsonObject.getJSONObject("message");
                            String ChatMessageID = messagesObject.getString("ChatMessageID");
                            String CompressedMessageImage = messagesObject.getString("CompressedMessageImage");
                            String CreatedAt = messagesObject.getString("CreatedAt");
                            String IsReadByReceiver = messagesObject.getString("IsReadByReceiver");
                            String IsReadBySender = messagesObject.getString("IsReadBySender");
                            String Message = messagesObject.getString("Message");
                            String MessageImage = messagesObject.getString("MessageImage");
                            String ReceiverID = messagesObject.getString("ReceiverID");
                            String ReceiverImage = messagesObject.getString("ReceiverImage");
                            String ReceiverName = messagesObject.getString("ReceiverName");
                            String ReceiverUserID = messagesObject.getString("ReceiverUserID");
                            String SenderID = messagesObject.getString("SenderID");
                            String SenderImage = messagesObject.getString("SenderImage");
                            String SenderName = messagesObject.getString("SenderName");
                            String SenderUserID = messagesObject.getString("SenderUserID");


                            chatMsgsModel.add(new ChatMsgsModel(ChatMessageID,CompressedMessageImage, CreatedAt, IsReadByReceiver, IsReadBySender, Message,MessageImage, ReceiverID, ReceiverImage, ReceiverName,
                                    ReceiverUserID, SenderID, SenderImage, SenderName, SenderUserID));

                            chatAdapter.notifyItemInserted(chatMsgsModel.size()-1);
                            chatRecycler.scrollToPosition(chatMsgsModel.size()-1);

                        } else {
                            BaseClass.dialog.dismiss();
                            Toast.makeText(ChatActivity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(ChatActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void imgToStringFunction(Bitmap bitmap) {

        if (bitmapArrayList.size() > 0) {
            Log.e("sizee", bitmapArrayList.size() + "");
            for (int j = 0; j < bitmapArrayList.size(); j++) {
                bitmap = bitmapArrayList.get(j);
                Log.e("bitmap", bitmap + " ");
                ByteArrayOutputStream bos = new ByteArrayOutputStream(); // Compressed
                ByteArrayOutputStream original_image = new ByteArrayOutputStream(); // Original
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos); // Compressed
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, original_image); // Original
                byte[] data = bos.toByteArray(); // Compressed
                byte[] original_data = original_image.toByteArray(); // Original
                imagebody.put("MessageImage", Base64.encodeToString(data, Base64.DEFAULT));
            }
        }
    }


    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int a = 0;
            Log.e("intentchecking", intent.getExtras().getString("message"));
            try {
                JSONObject messagesObject = new JSONObject(intent.getExtras().getString("message"));
                String ChatMessageID = messagesObject.getString("ChatMessageID");
                String CompressedMessageImage = messagesObject.getString("CompressedMessageImage");
                String CreatedAt = messagesObject.getString("CreatedAt");
                String IsReadByReceiver = messagesObject.getString("IsReadByReceiver");
                String IsReadBySender = messagesObject.getString("IsReadBySender");
                String Message = messagesObject.getString("Message");
                String MessageImage = messagesObject.getString("MessageImage");
                String ReceiverID = messagesObject.getString("ReceiverID");
                String ReceiverImage = messagesObject.getString("ReceiverImage");
                String ReceiverName = messagesObject.getString("ReceiverName");
                String ReceiverUserID = messagesObject.getString("ReceiverUserID");
                String SenderID = messagesObject.getString("SenderID");
                String SenderImage = messagesObject.getString("SenderImage");
                String SenderName = messagesObject.getString("SenderName");
                String SenderUserID = messagesObject.getString("SenderUserID");

                Log.e("recieverName", ReceiverName + " ");
              //  if(ChatID.equals(ChatMessageID)) {
                    if (!SenderUserID.equals(sharedPreferences.getString("UserID", ""))) {
                        chatMsgsModel.add(new ChatMsgsModel(ChatMessageID,CompressedMessageImage, CreatedAt, IsReadByReceiver, IsReadBySender, Message,MessageImage, ReceiverID, ReceiverImage, ReceiverName,
                                ReceiverUserID, SenderID, SenderImage, SenderName, SenderUserID));
                        chatAdapter.notifyItemInserted(chatMsgsModel.size()-1);
                        chatRecycler.scrollToPosition(chatMsgsModel.size()-1);
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                              //  chatAdapter.notifyDataSetChanged();
//                                chatRecycler.scrollToPosition(chatDataArrayList.size()-1);
                                // Stuff that updates the UI

                            }
                        });
                    }
                //}
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("pusher", e.toString());

            }
//            System.out.println(data);
//            Log.e("pusher", "data" + data);

        }
    };


}
