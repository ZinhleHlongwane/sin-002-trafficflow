package co.wethinkcode.trafficflow;

import co.wethinkcode.trafficflow.mq.MqConfig;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class HeartbeatPublisher {

    public void publish() {
        ActiveMQConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory(MqConfig.BROKER_URL);

        try (
                Connection connection = connectionFactory.createConnection();
                Session session = connection.createSession(
                        false,
                        Session.AUTO_ACKNOWLEDGE
                )
        ) {
            connection.start();

            Queue queue =
                    session.createQueue(MqConfig.HEARTBEAT_QUEUE);

            try (MessageProducer producer = session.createProducer(queue)) {
                TextMessage message =
                        session.createTextMessage("intersection-service-alive");

                producer.send(message);
            }

        } catch (JMSException e) {
            throw new RuntimeException(
                    "Could not publish heartbeat",
                    e
            );
        }
    }
}
