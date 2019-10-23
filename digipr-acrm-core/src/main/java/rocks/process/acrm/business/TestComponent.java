/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.acrm.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rocks.process.acrm.data.domain.Task;
import rocks.process.acrm.business.service.TaskService;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class TestComponent {

    private Logger logger = LoggerFactory.getLogger(TestComponent.class);

    @Autowired
    TaskService taskService;

    @PostConstruct
    public void init(){
        logger.info("=> Create a first task");
        Task task = new Task();
        task.setDescription("first task");
        task = taskService.createTask(task);
        logger.info("=> First task with ID " + task.getId() + " created.");
        logger.info("=> Update the first task");
        task.setDescription("updated first task");
        task = taskService.updateTask(task);
        logger.info("=> First task with ID " + task.getId() + " updated.");
        logger.info("=> Create a second task");
        task = new Task();
        task.setDescription("second task");
        task = taskService.createTask(task);
        logger.info("=> Second task with ID " + task.getId() + " created.");
        logger.info("=> Get a list of all tasks");
        List<Task> tasks = taskService.getAllTasks();
        for(Task taskEntry : tasks){
            logger.info("=> We have \"" + taskEntry.getDescription() + "\" with ID " + taskEntry.getId() + " in the list.");
        }
    }
}
