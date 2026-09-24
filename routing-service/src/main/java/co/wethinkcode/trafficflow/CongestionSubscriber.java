package co.wethinkcode.trafficflow;

import co.wethinkcode.trafficflow.mq.MqConfig;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class CongestionSubscriber {

    private volatile int latestCongestionLevel = 0;

    public void start() {
        ActiveMQConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory(MqConfig.BROKER_URL);

        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(
                    false,
                    Session.AUTO_ACKNOWLEDGE
            );

            Topic topic = session.createTopic(MqConfig.TOPIC);
            MessageConsumer consumer = session.createConsumer(topic);

            consumer.setMessageListener(message -> {
                if (message instanceof TextMessage textMessage) {
                    try {
                        latestCongestionLevel =
                                Integer.parseInt(textMessage.getText());

                        System.out.println(
                                "Received congestion level: "
                                + latestCongestionLevel
                        );
                    } catch (JMSException | NumberFormatException e) {
                        System.err.println(
                                "Could not read congestion message"
                        );
                    }
                }
            });

            connection.start();

        } catch (JMSException e) {
            throw new RuntimeException(
                    "Could not subscribe to congestion topic",
                    e
            );
        }
    }

    public int getLatestCongestionLevel() {
        return latestCongestionLevel;
    }
}
