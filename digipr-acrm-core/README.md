# DigiPR Spring Boot Example

This example illustrates how Spring Boot can be used to develop a microservice.

#### Contents:
- [Application Bootstrapping](#application-bootstrapping)
- [Microservice Application](#microservice-application)
- [Business Layer](#business-layer)
- [TestComponent](#testcomponent)
- [Spring Boot Testing](#spring-boot-testing)
- [Static HTML Files and Controllers](#static-html-files-and-controllers)

## Application Bootstrapping

This exemplary application is relying on [Spring Boot](https://projects.spring.io/spring-boot), which is the convention-over-configuration solution of the [Spring](https://spring.io) framework for creating stand-alone, production-grade Applications that you can "just run". In detail the application is based on the following:

- [Spring Boot](https://projects.spring.io/spring-boot)
- [Spring Web](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html)

Please use the Spring Initializr to bootstrap the application with [this shared configuration](https://start.spring.io/#!type=maven-project&language=java&platformVersion=2.2.0.RELEASE&packaging=jar&jvmVersion=1.8&groupId=rocks.process.acrm&artifactId=digipr-acrm-core&name=digipr-acrm-core&description=demo%20project%20for%20spring%20boot&packageName=rocks.process.acrm&dependencies=web).

Download the ZIP-file and extract it somewhere. Then import the project into your favourite Java/Maven IDE such as IntelliJ, NetBeans or Eclipse.

## Microservice Application

You may change the `PORT` of your microservice using the `src/main/java/resources/application.yml` (you may have to convert it) file by adding the following:

```yml
server:
  port: 8080
```

## Business Layer

Create the following three classes in the `rocks.process.acrm.example.task` package as an demonstrating example:

![](images/example.png)

- `Task` can be seen as an value object.
- `TaskRepository` should be implemented as an in-memory `@Repository` managing `tasks`.
- `TaskService` should be implemented as a business `@Service`. The `TaskRepository` should be `@Autowired`.

## Test Component

Create a testing `@Component` class called `TestComponent` as follows:

```Java
package rocks.process.acrm.example;
//...
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
```

## Spring Boot Testing

Write a `@SpringBootTest` as follows:

```Java
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
```

## Static HTML Files and Controllers

Create two basic HTML files under `src\main\resources\static`.

And finally, serve these two HTML files by implementing an `IndexController` as follows:
```Java
package rocks.process.acrm.controller;
//...
@Controller
@RequestMapping(path = "/")
public class IndexController {

    @GetMapping
    public String getIndex(){
        return "index.html";
    }

    @GetMapping(path = "/hello")
    public String getHello(){
        return "hello.html";
    }
}
```
