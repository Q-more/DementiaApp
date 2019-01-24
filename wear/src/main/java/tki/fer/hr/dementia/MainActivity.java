package tki.fer.hr.dementia;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * https://stackoverflow.com/questions/27911579/send-message-from-wearable-to-phone-and-then-immediately-reply
 * https://developers.google.com/android/reference/com/google/android/gms/wearable/WearableListenerService
 * https://developer.android.com/training/wearables/data-layer/migrate-to-googleapi.html
 * https://developer.android.com/training/wearables/data-layer/events.html#Listen
 * https://developer.android.com/training/wearables/data-layer/messages.html#SendMessage
 */
public class MainActivity extends WearableActivity implements MessageClient.OnMessageReceivedListener {

    private static final String MOBILE_PATH = "/mobile";
    private static Logger LOG = Logger.getLogger(MainActivity.class.getSimpleName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {
        LOG.info("Entering onMessageReceived...");
        if (messageEvent.getPath().equals("/wear")) {

            Intent intent = new Intent(this, InformationActivity.class);
            intent.putExtra("INFO", messageEvent.getData());
            TaskStackBuilder.create(this).addNextIntentWithParentStack(intent).startActivities();
        }
    }

    public void sendMessage() {
        LOG.info("Sanding message...");
        Wearable.getNodeClient(this).getConnectedNodes().addOnCompleteListener(new OnCompleteListener<List<Node>>() {
            @Override
            public void onComplete(@NonNull Task<List<Node>> task) {
                int counter = 0;
                for (Node node : task.getResult()) {
                    counter++;
                    Wearable.getMessageClient(MainActivity.this).sendMessage(node.getId(), MOBILE_PATH, null).addOnCompleteListener(new OnCompleteListener<Integer>() {
                        @Override
                        public void onComplete(@NonNull Task<Integer> task) {
                            LOG.info("Message send ");
                        }
                    });
                }

                if (counter == 0) {
                    Toast.makeText(MainActivity.this.getApplicationContext(), "Spojite sat s pametnim telefonom", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Wearable.getMessageClient(this).addListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();

        Wearable.getMessageClient(this).removeListener(this);
    }

    public void fetchAddressButtonHandler(View view) {
        sendMessage();
    }
}
