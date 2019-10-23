package rocks.process.acrm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rocks.process.acrm.data.domain.Task;
import rocks.process.acrm.business.service.TaskService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DigiprAcrmCoreApplicationTests {

	@Autowired
	TaskService taskService;

	@Test
	void taskServiceTest() {
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