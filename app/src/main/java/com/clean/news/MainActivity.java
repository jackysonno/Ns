package com.clean.news;

import android.app.Activity;
import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        WebSettings settings = webView.getSettings();

        // ۱. تنظیمات پایه‌ای
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        // ۲. تنظیمات کش (Cache) برای ذخیره ابدی در گوشی
        // این دستور می‌گوید: اگر آفلاین بودی یا فایل در کش موجود بود، از کش بخوان
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        
        webView.setWebViewClient(new WebViewClient());

        // ۳. تنظیمات دانلود منیجر سیستم
        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setMimeType(mimetype);
            String cookies = CookieManager.getInstance().getCookie(url);
            request.addRequestHeader("cookie", cookies);
            request.addRequestHeader("User-Agent", userAgent);
            request.setDescription("Downloading file...");
            request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));
            
            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            dm.enqueue(request);
            Toast.makeText(getApplicationContext(), "در حال دانلود...", Toast.LENGTH_LONG).show();
        });

        // ⚠️ بسیار مهم: در خط زیر، لینک اختصاصی گیت‌هاب پیجز خودت را که در مرحله قبل ساختی، جایگزین کن
        webView.loadUrl("https://Jackysonno.github.io/AM/"); 
        
        setContentView(webView);
    }

    // هندل کردن دکمه بازگشتِ گوشی
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
