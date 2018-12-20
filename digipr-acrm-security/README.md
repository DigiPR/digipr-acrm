# DigiPR Spring Security and JWT Example

This example illustrates how Spring Security can be used to implement a JWT-based authentication.

#### Contents:
- [Prerequisite and Use Case](#prerequisite-and-use-case)
- [Application Bootstrapping](#application-bootstrapping)
- [Microservice Application](#microservice-application)
- [Password Encoding and Demo User](#password-encoding-and-demo-user)
- [Initial Web Security Configuration](#initial-web-security-configuration)
- [Service Layer / API](#service-layer--api)
- [User Registration](#user-registration)
- [User Authentication Phases](#user-authentication-phases)
  - [User Login](#user-login)
  - [User Authentication](#user-authentication)
  - [User Logout](#user-logout)
- [CSRF](#csrf)
- [XSS, HTTPS and Disable Session](#xss-https-and-disable-session)

## Prerequisite and Use Case

> Please note, this example is extension to the [digipr-acrm-data](./digipr-acrm-data) and the [digipr-acrm-api](./digipr-acrm-api) examples.

## Application Bootstrapping

The project can be bootstrapped using the [Spring Initializr](https://start.spring.io) using the following group and artefact ids:

```XML
<groupId>rocks.process.acrm</groupId>
<artifactId>digipr-acrm-security</artifactId>
```

Besides, in the [Spring Initializr](https://start.spring.io) select `Spring Security`, `Spring Web`, `JPA` and the `H2` support. Then generate and import the project into your favourite IDE.

Finally, add the following JWT specific Maven dependencies to your `pom.xml`:

```XML
<dependency>
	<groupId>io.jsonwebtoken</groupId>
	<artifactId>jjwt</artifactId>
	<version>0.9.1</version>
</dependency>
```

## Microservice Application

You should set a `spring.profiles.active` property using the `src/main/java/resources/application.yml` (you may have to convert it) as follows:

```yml
spring:
  profiles:
    active: dev
```

By making the `dev` profile active, you are now able to place development specific properties, such as token secrets, to an additional property file called `src/main/java/resources/application-dev.yml`. Make sure that you add property files containing secrets to `.gitignore` **before** adding them to git as follows:

```git
*-dev.yml
```

Finally, you can set the token secret to the `src/main/java/resources/application-dev.yml` file as follows:

```yml
token:
  secret: secret
```

## Password Encoding and Demo User

Storing passwords in plain text is one of the worst things you can do! Therefore define a `PasswordEncoder` as follows (such as BCrypt or SCrypt):

```Java
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

For testing purposes we need a demo user at the beginning as follows:

```Java
@Service
@Validated
public class AgentService {

    @Autowired
    private AgentRepository agentRepository;
    // ...
    @Autowired
    private PasswordEncoder passwordEncoder;

    //...

    public Agent getCurrentAgent() {
        String userEmail = "demo@demo.ch";
        return agentRepository.findByEmail(userEmail);
    }

    @PostConstruct
    private void init() throws Exception {
        Agent agent = new Agent();
        agent.setName("Demo");
        agent.setEmail("demo@demo.ch");
        agent.setPassword(passwordEncoder.encode("password"));
        this.saveAgent(agent);
    }
}
```
## Initial Web Security Configuration

To start testing the API, you should permit all request by defining an initial `WebSecurityConfig` as follows:

```Java
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/**").permitAll();
    }
}
```

## Service Layer / API

As mentioned at the beginning, this example is extension to the  [digipr-acrm-api](./digipr-acrm-api) example. Therefore the API definition needs to be imported into this example.

Once the application has been started, you can test the API by using the following Postman collection:

[![Import in Postman](https://img.shields.io/badge/Import%20in-Postman-F47023.svg?longCache=true)](https://app.getpostman.com/run-collection/b4e64019eea8d78d60cc)

## User Registration

The user registration has been realized in a corresponding `UserController` as part of Spring MVC using REST methods: 

```Java
@Controller
public class UserController {

    @Autowired
    private AgentService agentService;

    @PostMapping("/user/register")
    public ResponseEntity<Void> postRegister(@RequestBody Agent agent) {
        try {
            agentService.saveAgent(agent);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/validate", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ResponseEntity<Void> init() {
        return ResponseEntity.ok().build();
    }
}
``` 

Next we need to enable the path to the user registration that it can be used:

```Java
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/", "/assets/**", "/user/**", "/login/**").permitAll();
    }
}
```

You can try out the registration by using the following `curl` (in e.g. Postman):

```bash
curl --request POST \
  --url http://localhost:8080/user/register \
  --header 'Content-Type: application/json' \
  --data '{\n    "name": "Admin",\n    "email": "admin@example.com",\n    "password": "password"\n}'
```

## User Authentication Phases

The use authentication has been implemented using JSON Web Tokens (JWT). It has been implemented using the [Java JWT (jjwt)](https://github.com/jwtk/jjwt) library and its corresponding guidelines.

The verification and issuing procedures are implemented in the `rocks.process.acrm.security-token` package based on [Java JWT (jjwt)](https://github.com/jwtk/jjwt). Besides, the package contains a JPA-based repository for making tokens invalid.

### User Login

```Java
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/", "/assets/**", "/user/**", "/login/**").permitAll()
                .anyRequest().authenticated().and()
                    .addFilter(new TokenLoginFilter(authenticationManager(),this.tokenService));
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
```

```bash
curl --request POST \
  --url http://localhost:8080/login \
  --header 'Content-Type: application/json' \
  --data '{\n    "email": "admin@example.com",\n    "password": "password",\n    "remember": "false"\n}'
```

### User Authentication

```Java
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // ...
    @Autowired
    private TokenService tokenService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/", "/assets/**", "/user/**", "/login/**").permitAll()
                .anyRequest().authenticated().and()
                    .addFilter(new TokenLoginFilter(authenticationManager(),this.tokenService))
                    .addFilter(new TokenAuthenticationFilter(authenticationManager(), this.tokenService));
    }

    // ...
}
```

```Java
@Service
@Validated
public class AgentService {

    @Autowired
    private AgentRepository agentRepository;
    // ...
    @Autowired
    private PasswordEncoder passwordEncoder;

    // ...

    public Agent getCurrentAgent() {
        String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return agentRepository.findByEmail(userEmail);
    }
}
```

```bash
curl --request HEAD \
  --url http://localhost:8080/validate \
  --header 'Authorization: Bearer <<your token>>'
```

### User Logout

```Java
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // ...
    @Autowired
    private TokenService tokenService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/", "/assets/**", "/user/**", "/login/**").permitAll()
                .anyRequest().authenticated().and()
                    .addFilter(new TokenLoginFilter(authenticationManager(),this.tokenService))
                    .addFilter(new TokenAuthenticationFilter(authenticationManager(), this.tokenService))
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/")
                    .addLogoutHandler(new TokenLogoutHandler(this.tokenService));
    }

    // ...
}
```

## CSRF

```Java
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // ...
    @Autowired
    private TokenService tokenService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
             .csrf()
                .requireCsrfProtectionMatcher(new CSRFRequestMatcher())
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
            .authorizeRequests()
                .antMatchers("/", "/assets/**", "/user/**", "/login/**").permitAll()
                .anyRequest().authenticated().and()
                    .addFilter(new TokenLoginFilter(authenticationManager(),this.tokenService))
                    .addFilter(new TokenAuthenticationFilter(authenticationManager(), this.tokenService))
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/")
                    .addLogoutHandler(new TokenLogoutHandler(this.tokenService));
    }

    // ...
}
```

## XSS, HTTPS and Disable Session

```Java
@Entity
public class Customer {

	@Id
	@GeneratedValue
	private Long id;
	@SafeHtml
	private String name;
	@SafeHtml
	private String email;
	@SafeHtml
	private String mobile;
	// ...
}
```

```Java
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // ...
    @Autowired
    private TokenService tokenService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and()
            .requiresChannel().requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null).requiresSecure().and()
            .csrf()
                .requireCsrfProtectionMatcher(new CSRFRequestMatcher())
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
            .authorizeRequests()
                .antMatchers("/", "/assets/**", "/user/**", "/login/**").permitAll()
                .anyRequest().authenticated().and()
                    .addFilter(new TokenLoginFilter(authenticationManager(),this.tokenService))
                    .addFilter(new TokenAuthenticationFilter(authenticationManager(), this.tokenService))
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/")
                    .addLogoutHandler(new TokenLogoutHandler(this.tokenService));
    }

    // ...
}
```