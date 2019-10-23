/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.acrm.data.repository;

import org.springframework.stereotype.Repository;
import rocks.process.acrm.data.domain.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Repository
public class TaskRepository {
    private TreeMap<Long, Task> tasks = new TreeMap<>();

    public Task save(Task task) {
        long key;
        if (task.getId() == 0) {
            if (tasks.isEmpty()) {
                key = 1;
            } else {
                key = tasks.lastKey() + 1;
            }
            task.setId(key);
        } else {
            key = task.getId();
        }
        tasks.put(key, task);
        return task;
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void delete(Task task){
        tasks.remove(task.getId());
    }
}
