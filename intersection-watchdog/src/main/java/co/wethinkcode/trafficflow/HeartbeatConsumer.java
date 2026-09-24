package co.wethinkcode.trafficflow;

import co.wethinkcode.trafficflow.mq.MqConfig;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class HeartbeatConsumer {

    private volatile long lastHeartbeatTime = System.currentTimeMillis();

    public void start() {
        ActiveMQConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory(MqConfig.BROKER_URL);

        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(
                    false,
                    Session.AUTO_ACKNOWLEDGE
            );

            Queue queue =
                    session.createQueue(MqConfig.HEARTBEAT_QUEUE);

            MessageConsumer consumer = session.createConsumer(queue);

            consumer.setMessageListener(message -> {
                if (message instanceof TextMessage) {
                    lastHeartbeatTime = System.currentTimeMillis();
                    System.out.println("Heartbeat received from intersection-service");
                }
            });

            connection.start();

        } catch (JMSException e) {
            throw new RuntimeException(
                    "Could not listen for intersection heartbeats",
                    e
            );
        }
    }

    public long getLastHeartbeatTime() {
        return lastHeartbeatTime;
    }
}
