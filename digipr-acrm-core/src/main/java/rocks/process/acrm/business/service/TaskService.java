/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.acrm.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rocks.process.acrm.data.domain.Task;
import rocks.process.acrm.data.repository.TaskRepository;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task task){
        return taskRepository.save(task);
    }

    public Task updateTask(Task task){
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks(){
        return taskRepository.getTasks();
    }
}
