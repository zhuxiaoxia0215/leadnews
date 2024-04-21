package com.heima.kafka.sample;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class ProducerQuickStart {
    public static void main(String[] args) {
        // 配置Kafka生产者
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.1.10:9092"); // Kafka服务器的地址和端口
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // 创建Kafka生产者
        Producer<String, String> producer = new KafkaProducer<>(props);

        // 定义要发送的消息
        String topicName = "my_topic"; // Kafka的主题名称
        String key = "my_key"; // 消息的键
        String value = "Hello, Kafka!"; // 消息的值

        // 创建ProducerRecord对象
        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, key, value);

        // 发送消息
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if(e != null){
                    System.out.println("记录异常信息到日志表中");
                }
                System.out.println(recordMetadata.offset());
            }
        });
    }
}
