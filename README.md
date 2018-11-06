# DigiPR Spring Boot Examples

[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Deploy to Heroku](https://img.shields.io/badge/deploy%20to-Heroku-6762a6.svg?longCache=true)](https://heroku.com/deploy)

## Examples

| Example | 
|--- | 
| **Core Spring Boot Bootstrapping**<br><br>[digipr-acrm-core](digipr-acrm-core) example illustrates how Spring Boot can be used to develop a microservice.<br><br>✔ Application Bootstrapping<br>✔ Microservice Application<br>✔ Spring Boot Testing | 
| **Spring Boot Data Example**<br><br>[digipr-acrm-data](digipr-acrm-data) example illustrates how JPA can be used with the help of Spring Boot Data JPA including H2.<br><br>✔ Data Access / Persistence Layer<br>✔ Domain Objects / Entities<br>✔ Repositories |


#### Sub-Module Heroku Deployment

Since this repository consists of several sub-modules and a Maven parent, the Heroku GitHub deployment would be slightly different:
1. Fork this repository
2. Connect your Heroku-Account to your GitHub-Account
3. Create a new app and choose under *Deploy* the forked repository
4. You may change the following two **Config Vars** under *Settings* if you wil like to change the to be deployed module:
   1. `MAVEN_CUSTOM_OPTS` = `-pl xyz-submodule-path`
   2. `PATH_TO_PROJECT` = `xyz-submodule-path`
5. Re-deploy the app

#### Maintainer
- [Andreas Martin](https://andreasmartin.ch)

#### License

- [Apache License, Version 2.0](blob/master/LICENSE)