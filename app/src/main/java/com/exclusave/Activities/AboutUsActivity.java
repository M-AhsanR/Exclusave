package com.exclusave.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.exclusave.R;

public class AboutUsActivity extends YouTubeBaseActivity {

    ImageView play;
    TextView faqs, terms;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
//ajbdc
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        faqs = findViewById(R.id.faqs);
        terms = findViewById(R.id.terms);
        play = findViewById(R.id.playVideo);
        youTubePlayerView = findViewById(R.id.player);

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo("q9ukzzqNu6Y");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        play.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                youTubePlayerView.initialize("AIzaSyC53WKjXnzbdhCSVTrA64MB_Oq9NNx730s", onInitializedListener);
                play.setVisibility(View.GONE);
            }
        });

        faqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutUsActivity.this, WebViewActivity.class);
                intent.putExtra("WEBURL", "http://www.exclusavecard.com/faq.html");
/*                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://www.exclusavecard.com/faq.html"));*/
                startActivity(intent);
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutUsActivity.this, WebViewActivity.class);
                intent.putExtra("WEBURL", "http://www.exclusavecard.com/faq.html");
/*                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://www.exclusavecard.com/faq.html"));*/
                startActivity(intent);
            }
        });
    }
}
