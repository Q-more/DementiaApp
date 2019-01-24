package tki.fer.hr.dementia.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.IOException;
import java.util.logging.Logger;

public class ListenerService extends WearableListenerService {

    private static final String WATCH_PATH = "/wear";
    private static final String PHONE_PATH = "/phone";

    private final Logger LOG = Logger.getLogger(ListenerService.class.getSimpleName());


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        LOG.info("Message recived from " + messageEvent.getSourceNodeId());
        if (messageEvent.getPath().equals(PHONE_PATH)) {
            String nodeId = messageEvent.getSourceNodeId();


            try {
                Wearable.getMessageClient(this).sendMessage(nodeId, WATCH_PATH, getData());
                LOG.info("Message send to wear...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] getData() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsBytes(new InformationData("Jane", "Doe", "Unknown"));
    }

}
