package com.example.zach.vollydemo;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    Button jsonRequestb;
    Button imageRequest;
    Button can;
    ImageView imageView;
    JsonObjectRequest jsonRequest;
    RequestQueue queue;
    Request.Priority priority;
    public void setPriority(Request.Priority priority) {

        this.priority = priority;

    }

    public Request.Priority getPriority() {

        if (this.priority != null) {

            return priority;

        } else {

            return Request.Priority.NORMAL;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);
        jsonRequestb = (Button) findViewById(R.id.json);
        imageRequest = (Button) findViewById(R.id.img);
        can = (Button) findViewById(R.id.cancle);
        jsonRequestb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fetchJson();
            }
        });
        imageRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchImage();
            }
        });
        can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancle();
            }
        });
    }
    void fetchJson() {
        String url = "http://httpbin.org/get?site=code&network=tutsplus";

        jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {


                            response = response.getJSONObject("args");
                            String site = response.getString("site"),
                                    network = response.getString("network");
                            Toast.makeText(MainActivity.this, site, Toast.LENGTH_SHORT).show();
                            System.out.println("Site: "+site+"\nNetwork: "+network);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        queue.add(jsonRequest);
    }
    void fetchImage() {

                String url = "http://i.imgur.com/Nwk25LA.jpg";
        imageView = (ImageView) findViewById(R.id.image);

        test imgRequest = new test(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imageView.setBackgroundColor(Color.parseColor("#ff0000"));
                error.printStackTrace();
            }
        });
        imgRequest.setPriority(Request.Priority.HIGH);
        Toast.makeText(MainActivity.this, "Priority:" + imgRequest.getPriority(), Toast.LENGTH_LONG).show();
       queue.add(imgRequest);
    }
    void cancle() {
        Toast.makeText(MainActivity.this, "All jobs are cancelled!", Toast.LENGTH_LONG).show();
        queue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return false;
            }
        });
    }
}
