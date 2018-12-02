package broker;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import broker.SubscriptionCallBack;

public class Subscriber {

    String clientId = MqttClient.generateClientId();
    MemoryPersistence persistence = new MemoryPersistence();
    private MqttClient mqttClient;
    private MqttConnectOptions connOpts;
    String topic;
//    String content;
    int qos;
    String broker;
    //
//    public static final String BROKER_URL = "tcp://broker.hivemq.com:1883";
//    //public static final String BROKER_URL = "tcp://test.mosquitto.org:1883";
//
//    //We have to generate a unique Client id.
//    String clientId = MqttClient.generateClientId();
//    private MqttClient mqttClient;
    //
    public Subscriber(String topic, int qos, String broker) {
        this.topic = topic;
//        this.content = content;
        this.qos = qos;
        this.broker = broker;

        try {
            mqttClient = new MqttClient(broker, clientId, persistence);
            connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setAutomaticReconnect(true);

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void start() {
        try {
            mqttClient.setCallback(new SubscriptionCallBack());
            mqttClient.connect(connOpts);
            mqttClient.subscribe(topic,qos);

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
