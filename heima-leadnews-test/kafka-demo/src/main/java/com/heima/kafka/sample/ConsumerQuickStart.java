package com.heima.kafka.sample;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

public class ConsumerQuickStart {

    //    public static void main(String[] args) {
//        //1.添加kafka的配置信息
//        Properties properties = new Properties();
//        //kafka的连接地址
//        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.13:9092");
//        //消费者组
//        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group2");
//        //消息的反序列化器
//        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
//        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
//
//        //2.消费者对象
//        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
//
//        //3.订阅主题
//        consumer.subscribe(Collections.singletonList("itheima-topic"));
//
//        //当前线程一直处于监听状态
//        while (true) {
//            //4.获取消息
//            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
//            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
//                System.out.println(consumerRecord.key());
//                System.out.println(consumerRecord.value());
//            }
//        }
//
//    }
    public static void main(String[] args) {
        // 配置Kafka消费者
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.1.13:9092"); // Kafka服务器的地址和端口
        props.put("group.id", "test-group"); // 消费者组ID
        props.put("enable.auto.commit", "true"); // 启用自动提交偏移量
        props.put("auto.commit.interval.ms", "1000"); // 自动提交偏移量的时间间隔
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());

        // 创建Kafka消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        // 订阅主题
        consumer.subscribe(Collections.singletonList("my_topic")); // 替换为你的主题名称

        // 循环监听消息
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭Kafka消费者
            consumer.close();
        }
    }
}
