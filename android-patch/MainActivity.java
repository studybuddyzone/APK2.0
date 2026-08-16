package com.xevrontech.studybuddyzonemanager;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import java.net.URISyntaxException;
import com.getcapacitor.BridgeActivity;
import com.getcapacitor.BridgeWebViewClient;

public class MainActivity extends BridgeActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webView = this.bridge.getWebView();

        // Android WebView sends "; wv)" in its User-Agent, which some sites
        // use to detect it's an embedded WebView and serve a limited UI.
        // Removing it makes the WebView present itself like normal Chrome.
        WebSettings settings = webView.getSettings();
        String originalUA = settings.getDefaultUserAgent(this);
        String chromeLikeUA = originalUA.replace("; wv", "");
        settings.setUserAgentString(chromeLikeUA);

        webView.setWebViewClient(new BridgeWebViewClient(this.bridge) {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (isPaymentAppScheme(url)) {
                    launchExternalPaymentApp(url);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    private boolean isPaymentAppScheme(String url) {
        if (url == null) return false;
        String lower = url.toLowerCase();
        return lower.startsWith("intent://")
            || lower.startsWith("upi://")
            || lower.startsWith("gpay://")
            || lower.startsWith("tez://")
            || lower.startsWith("phonepe://")
            || lower.startsWith("paytmmp://")
            || lower.startsWith("bhim://")
            || lower.startsWith("credpay://");
    }

    private void launchExternalPaymentApp(String url) {
        try {
            Intent intent;
            if (url.startsWith("intent://")) {
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            } else {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            }

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                if (fallbackUrl != null) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl)));
                }
            }
        } catch (URISyntaxException | ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
