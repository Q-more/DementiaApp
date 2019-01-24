package tki.fer.hr.dementia;

import android.content.Intent;
import android.os.Bundle;
import android.support.wear.widget.SwipeDismissFrameLayout;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Objects;

public class InformationActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        TextView mTextView = findViewById(R.id.location);

        // Enables Always-on
        setAmbientEnabled();
        byte[] infoBytes = Objects.requireNonNull(getIntent().getExtras()).getByteArray("INFO");

        try {
            InformationData informationData = getInfo(infoBytes);
            mTextView.setText(informationData.getLocation());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private InformationData getInfo(byte[] message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(message, InformationData.class);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


}
