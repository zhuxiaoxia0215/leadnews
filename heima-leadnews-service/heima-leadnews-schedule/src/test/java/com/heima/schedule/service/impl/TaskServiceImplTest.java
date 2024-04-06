package com.heima.schedule.service.impl;

import com.heima.model.schedule.dtos.Task;
import com.heima.schedule.ScheduleApplication;
import com.heima.schedule.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ScheduleApplication.class)
@RunWith(SpringRunner.class)
class TaskServiceImplTest {

    @Autowired
    private TaskService taskService;

    @Test
    void addTask() {

        for (int i = 0; i < 5; i++) {
            Task task = new Task();
            task.setTaskType(100);
            task.setPriority(50);
            task.setParameters("task test".getBytes(StandardCharsets.UTF_8));
            task.setExecuteTime(new Date().getTime() + 500 * i);
            long l = taskService.addTask(task);
        }

    }

    @Test
    public void testCancelTask() {
        boolean b = taskService.cancelTask(1776442923214184450L);
        System.out.println(b);
    }

    @Test
    public void testPoll() {
        Task task = taskService.poll(100, 50);
        System.out.println(task);
    }

    @Test
    public void testKeys() {

    }
}