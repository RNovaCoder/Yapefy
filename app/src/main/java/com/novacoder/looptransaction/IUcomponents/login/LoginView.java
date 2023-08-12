package com.novacoder.looptransaction.IUcomponents.login;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.novacoder.looptransaction.R;


public class LoginView extends ConstraintLayout {

    static LayoutInflater manager_xml;
    static String URL_LOGIN = "//";
    static String TOKEN_NAME = "..." + "=";
    private WebView webView;

    public LoginView(Context context, AttributeSet atr) {
        super(context, atr);
        inicializar();
    }

    public LoginView(Context context) {
        super(context);
        inicializar();
    }

    public void inicializar () {

        manager_xml = LayoutInflater.from(this.getContext());
        manager_xml.inflate(R.layout.login_layout, this, true);


        webView = this.findViewById(R.id.webView);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(URL_LOGIN);


    }


    private void tokenExist () {
        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(URL_LOGIN);

        if (cookies != null) {
            String[] cookieArray = cookies.split("; ");
            for (String cookie : cookieArray) {
                if (cookie.contains(TOKEN_NAME)) {

                    /*Se ha iniciado sesión y se ha guardado un token en
                    una cookie, toca guardarlo en los sharedprefernces y luego
                    borrar todas las cookies */

                    System.out.println("Cookie puntual encontrada: " + cookie);
                    break;
                }
            }
        }

    }



}