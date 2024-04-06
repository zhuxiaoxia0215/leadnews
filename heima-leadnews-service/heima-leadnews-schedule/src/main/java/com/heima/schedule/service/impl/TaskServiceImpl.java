package com.heima.schedule.service.impl;


import com.alibaba.fastjson.JSON;
import com.heima.common.constants.ScheduleConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.schedule.dtos.Task;
import com.heima.model.schedule.pojos.Taskinfo;
import com.heima.model.schedule.pojos.TaskinfoLogs;
import com.heima.schedule.mapper.TaskinfoLogsMapper;
import com.heima.schedule.mapper.TaskinfoMapper;
import com.heima.schedule.service.TaskService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    @Override
    public long addTask(Task task) {
        //添加任务到数据库中
        boolean success = addTaskTODb(task);
        //添加任务到redis
        if (success) {
            addTaskToCache(task);
        }
        return task.getTaskId();
    }

    @Override
    public boolean cancelTask(Long taskId) {
        boolean flag = false;
        Task task = updateDB(taskId, ScheduleConstants.CANCELLED);
        if (task != null) {
            removeTaskFromCache(task);
            flag = true;
        }
        return flag;
    }

    @Override
    public Task poll(int type, int priority) {
        Task task = new Task();
        String key = type + "_" + priority;
        String task_json = cacheService.lRightPop(ScheduleConstants.TOPIC + key);
        if (StringUtils.isNotBlank(task_json)) {
            task = JSON.parseObject(task_json, Task.class);
            updateDB(task.getTaskId(), ScheduleConstants.EXECUTED);
        }
        return task;
    }

    private void removeTaskFromCache(Task task) {
        String key = task.getTaskType() + "_" + task.getPriority();
        if (task.getExecuteTime() <= System.currentTimeMillis()) {
            cacheService.lRemove(ScheduleConstants.TOPIC + key, 0, JSON.toJSONString(task));
        } else {
            cacheService.zRemove(ScheduleConstants.FUTURE + key, JSON.toJSONString(task));
        }
    }

    private Task updateDB(Long taskId, int status) {
        taskinfoMapper.deleteById(taskId);
        TaskinfoLogs taskinfoLogs = taskinfoLogsMapper.selectById(taskId);
        taskinfoLogs.setStatus(status);
        taskinfoLogsMapper.updateById(taskinfoLogs);

        Task task = new Task();
        BeanUtils.copyProperties(taskinfoLogs, task);
        task.setExecuteTime(taskinfoLogs.getExecuteTime().getTime());
        return task;
    }

    @Autowired
    private TaskinfoMapper taskinfoMapper;

    @Autowired
    private TaskinfoLogsMapper taskinfoLogsMapper;

    private boolean addTaskTODb(Task task) {

        Taskinfo taskInfo = new Taskinfo();
        BeanUtils.copyProperties(task, taskInfo);
        taskInfo.setExecuteTime(new Date(task.getExecuteTime()));
        taskinfoMapper.insert(taskInfo);

        task.setTaskId(taskInfo.getTaskId());

        TaskinfoLogs taskinfoLogs = new TaskinfoLogs();
        BeanUtils.copyProperties(taskInfo, taskinfoLogs);
        taskinfoLogs.setVersion(1);
        taskinfoLogs.setStatus(ScheduleConstants.SCHEDULED);
        taskinfoLogsMapper.insert(taskinfoLogs);
        return true;
    }

    @Autowired
    private CacheService cacheService;

    private void addTaskToCache(Task task) {
        String key = task.getTaskType() + "_" + task.getPriority();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        long nextScheduleTime = calendar.getTimeInMillis();

        if (task.getExecuteTime() <= System.currentTimeMillis()) {
            cacheService.lLeftPush(ScheduleConstants.TOPIC + key, JSON.toJSONString(task));
        } else if (task.getExecuteTime() <= nextScheduleTime) {
            cacheService.zAdd(ScheduleConstants.FUTURE + key, JSON.toJSONString(task), task.getExecuteTime());
        }
    }


    @Scheduled(cron = "0 */1 * * * ?")
    public void refresh() {

        String token = cacheService.tryLock("FUTURE_TASK_SYNC", 1000 * 30);

        if(StringUtils.isNotBlank(token)){
            log.info("定时任务刷新");

            Set<String> futureKeys = cacheService.scan(ScheduleConstants.FUTURE + "*");
            for (String futureKey : futureKeys) {

                String topicKey = ScheduleConstants.TOPIC + futureKey.split(ScheduleConstants.FUTURE)[1];

                Set<String> tasks = cacheService.zRangeByScore(futureKey, 0, System.currentTimeMillis());
                if (!tasks.isEmpty()) {
                    cacheService.refreshWithPipeline(futureKey, topicKey, tasks);
                    log.info("刷新一条数据");
                }
            }
        }
    }
}
