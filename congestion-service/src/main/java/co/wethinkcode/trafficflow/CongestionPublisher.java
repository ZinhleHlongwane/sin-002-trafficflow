package co.wethinkcode.trafficflow;

import co.wethinkcode.trafficflow.mq.MqConfig;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class CongestionPublisher {

    public void publish(int congestionLevel) {

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

            Topic topic = session.createTopic(MqConfig.TOPIC);

            try (MessageProducer producer = session.createProducer(topic)) {
                TextMessage message =
                        session.createTextMessage(String.valueOf(congestionLevel));

                producer.send(message);
            }

        } catch (JMSException e) {
            throw new RuntimeException(
                    "Could not publish congestion level",
                    e
            );
        }
    }
}
