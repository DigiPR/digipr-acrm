package rocks.process.acrm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rocks.process.acrm.example.task.Task;
import rocks.process.acrm.example.task.TaskService;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DigiprAcrmCoreApplicationTests {

	@Autowired
	TaskService taskService;

	@Test
	public void taskServiceTest() {
		Task task = new Task();
		task.setDescription("third task");
		task = taskService.createTask(task);
		assertThat(task.getId()).isEqualTo(3);

		task.setDescription("updated third task");
		task = taskService.updateTask(task);
		assertThat(task.getDescription()).isEqualTo("updated third task");

		task = new Task();
		task.setDescription("forth task");
		task = taskService.createTask(task);
		assertThat(task.getId()).isEqualTo(4);
	}
}