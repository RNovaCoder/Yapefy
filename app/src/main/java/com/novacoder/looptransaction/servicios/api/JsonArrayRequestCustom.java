package com.novacoder.looptransaction.servicios.api;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class JsonArrayRequestCustom<T> extends Request {

    private  Response.Listener<T> listener;
    private final Map<String, String> headers = new HashMap<>();
    private final String requestBody;

    public JsonArrayRequestCustom(int method, String url, String requestBody, Response.Listener listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = listener;
        this.requestBody = requestBody;
        setRetryPolicy(new DefaultRetryPolicy(15000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success((T) jsonString, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(Object response) {
        if (listener != null) {
            listener.onResponse((T) response);
        }
    }

    @Override
    public byte[] getBody() {
        try {
            return requestBody.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getBodyContentType() {
        // Devolver el tipo de contenido del cuerpo de la solicitud
        return "application/json; charset=utf-8";
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    // Métodos para agregar o modificar encabezados
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }
    public void addHeader(Map<String, String> header) {
        header.putAll(headers);
    }
    public void removeHeader(String key) {
        headers.remove(key);
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        return super.getRetryPolicy();
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
