package caselabkafka.messaging.consumer;

public interface ApplicationStateConsumer {
    void handle(String applicationData);
}
