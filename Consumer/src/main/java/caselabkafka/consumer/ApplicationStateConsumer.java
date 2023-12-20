package caselabkafka.consumer;


public interface ApplicationStateConsumer {
    
    void handle(String applicationData);
}
