package mgl.laboratorio09;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mSensor;
    float gravity[] = {0,0,0};
    float linear_acceleration[] = {0,0,0};
    float linear_acceleration_maxima[] = {0,0,0};
    TextView tvMaxX;
    TextView tvMaxY;
    TextView tvMaxZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor= mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        tvMaxX = (TextView) findViewById(R.id.tvMaxX);
        tvMaxY = (TextView) findViewById(R.id.tvMaxY);
        tvMaxZ = (TextView) findViewById(R.id.tvMaxZ);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.8f;
        String fechaActual;
        // Isolatetheforceof gravitywiththelow-passfilter.

        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
        // Removethegravitycontributionwiththehigh-passfilter.
        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];

        if (linear_acceleration_maxima[0] < linear_acceleration[0]) {
            linear_acceleration_maxima[0] = linear_acceleration[0];
            fechaActual = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
            tvMaxX.setText("Hora Máximo X: " + fechaActual + "\nMagnitud: " + linear_acceleration_maxima[0]);
        }
        if (linear_acceleration_maxima[1] < linear_acceleration[1]) {
            linear_acceleration_maxima[1] = linear_acceleration[1];
            fechaActual = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
            tvMaxY.setText("Hora Máximo Y: " + fechaActual + "\nMagnitud: " + linear_acceleration_maxima[1]);
        }
        if (linear_acceleration_maxima[2] < linear_acceleration[2]) {
            linear_acceleration_maxima[2] = linear_acceleration[2];
            fechaActual = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
            tvMaxZ.setText("Hora Máximo Z: " + fechaActual + "\nMagnitud: " + linear_acceleration_maxima[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        // This prevents a sensor from continually sensing data and draining the battery
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

}
