/**
 *
 */
package broker;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import database.Database;

import javax.xml.crypto.Data;
import java.util.Arrays;


/**
 * @author jorg_
 *
 */
public class SubscriptionCallBack implements MqttCallback {


    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection Lost... Reconnecting");

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
//        System.out.println(topic + ": " + Arrays.toString(message.getPayload()));
        byte[] payload = message.getPayload();
        System.out.println(decrypt(payload));
        Database.insertStatement("logging", topic,decrypt(payload));
//        insertStatement("logging", topic,decrypt(payload));

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
    public static String decrypt(byte[] bytes) {
        String s = new String(bytes);
        return s;
    }
}

