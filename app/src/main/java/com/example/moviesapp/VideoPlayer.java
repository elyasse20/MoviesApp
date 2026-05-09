package com.example.moviesapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VideoPlayer extends AppCompatActivity {

    private WebView webView;
    private String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video_player);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        videoUrl = getIntent().getStringExtra("videoUrl");
        webView = findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false); // Mohima bach ykhdem direct

        // Kan-forciw WebView yban bhal Google Chrome dyal PC (Bach n-bypassiow Error 152 w 153)
        webSettings.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

        // Pour charger la vidéo dans l'application
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        if (videoUrl != null && !videoUrl.isEmpty()) {
            // Kat-s7e7 l'URL bo7dha wakha tkoun fih watch?v= awla m.youtube
            if (videoUrl.contains("watch?v=")) {
                videoUrl = videoUrl.replace("watch?v=", "embed/");
                videoUrl = videoUrl.replace("m.youtube.com", "www.youtube.com");
            }

            // L'astuce dyal 'allow' mohima l'YouTube daba
            String htmlData = "<html style='margin:0;padding:0;background:black;'><body style='margin:0;padding:0;'>" +
                    "<iframe width='100%' height='100%' src='" + videoUrl + "?autoplay=1' " +
                    "frameborder='0' allow='autoplay; encrypted-media' allowfullscreen></iframe>" +
                    "</body></html>";

            // Base URL hya youtube.com bach yts7ablih l'iframe f site s7i7
            webView.loadDataWithBaseURL("https://www.youtube.com", htmlData, "text/html", "utf-8", null);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}