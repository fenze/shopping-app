package dev.fenze.school;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Main extends AppCompatActivity
{
    private static HashMap<Integer, Integer> views;
    static HashMap<Integer, Integer> prices;
    static int[][] order;
    StringBuilder ret = new StringBuilder();
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    OrderDatabase db;

    public Main()
    {
        views = new HashMap<>();
        views.put(R.id.store, R.layout.main);
        views.put(R.id.order, R.layout.order);
        views.put(R.id.sms, R.layout.sms);
        views.put(R.id.about, R.layout.about);

        prices = new HashMap<>();

        prices.put(R.id.pc, 23000);
        prices.put(R.id.mouse, 900);
        prices.put(R.id.keyboard, 1400);
        prices.put(R.id.webcam, 300);
    }

    private void addOrder()
    {
        int end_price = 0;

        int[] names = new int[]{
                R.string.mac_pro,
                R.string.magic_mouse,
                R.string.magic_keyboard,
                R.string.webcam
        };

        int i = 0;
        for (int key : prices.keySet()) {
            String s_amount = ((EditText) findViewById(key)).getText().toString();

            if (s_amount.length() != 0) {
                end_price += Integer.parseInt(s_amount) * prices.get(key);

                ret.append(
                                getResources().getString(names[i]))
                        .append(" = ")
                        .append(Integer.parseInt(s_amount))
                        .append(" * ")
                        .append(prices.get(key))
                        .append(" = ")
                        .append(Integer.parseInt(s_amount) * prices.get(key))
                        .append("\n");

                Log.i("tag", String.valueOf(ret));
            }
            i++;
        }

        Log.i("DATABASE", String.valueOf(ret));
        db.addData(String.valueOf(ret));

        Toast.makeText(this, "Order added to cart", Toast.LENGTH_LONG).show();
    }

    private void setToolbar()
    {
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    private void sendSms()
    {
        String phone = ((EditText)findViewById(R.id.phone_number)).getText().toString();
        if (phone.length() != 0) {
            this.getSystemService(SmsManager.class).sendTextMessage(phone, null,
                    String.valueOf(ret), null, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setToolbar();

        db = new OrderDatabase(getApplicationContext());

        requestPermissions(new String[]{"android.permission.SEND_SMS"}, MY_PERMISSIONS_REQUEST_SEND_SMS);

        findViewById(R.id.sumbit).setOnClickListener(e -> addOrder());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        for (int key : views.keySet())
            if (item.getItemId() == key) {
                setContentView(views.get(key));
                setToolbar();

                if (item.getItemId() == R.id.order) {
                    ArrayList<String> data = new ArrayList<>();

                    @SuppressLint("Recycle") Cursor res = db.getReadableDatabase().query("ORDERS",
                            OrderDatabase.cells, null, null, null, null, null);

                    StringBuilder lst = new StringBuilder();

                    while (res.moveToNext()) {
                        data.add(res.getString(3));
                    }

                    Log.i("DATABASE", String.valueOf(data));

                    for (String s : data) {
                        lst.append(s);
                        lst.append("------------------\n");
                    }

                    Log.i("DATABASE", String.valueOf(lst));
                    ((TextView) findViewById(R.id.order_inspector)).setText(lst);
                }

                if (item.getItemId() == R.id.sms) {
                    findViewById(R.id.send_sms).setOnClickListener(
                            v -> {
                                sendSms();
                            }
                    );
                }
                return true;
            }

        return false;
    }
}