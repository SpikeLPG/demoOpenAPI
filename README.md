# Demo API Specification

A demo Spring API that explores the 2 main methods of documenting an
API, [RestDoc vs SpringDoc](https://www.baeldung.com/spring-rest-docs-vs-openapi)

## SpringDoc OpenAPI

This uses the [OpenAPI specification](https://swagger.io/specification/) that was started by SmartBear and the
documentation it produces looks like the specifications on swagger.io.

### Implementation
The `Books` endpoints are used as examples of the implementation.

#### Configuration

The dependency is added to the `build.gradle` file and the main configuration is added to the `application.yml` file
under `SpringDoc`

```yaml
springdoc:
  paths-to-exclude: /**/internal/**
  api-docs:
    path: /specification/openapi
    version: openapi_3_0
  swagger-ui:
    path: /specification/openapi/index.html
```

You can exclude certain packages or paths from the documentation by adding in an exclusion, the above config excludes
any url that includes `internal`, e.g. any internally used endpoints that you don't want publicly visible.  
The remaining config sets the location for the raw specification (`json` and `yaml`) and the url to view the rendered
specification page.

With the configuration set you are able to view the basic specification when you start the service, e.g.   
`./gradlew bootRun`  
and navigating to  
`http://localhost:8080/specification/openapi/index.html` or  
`http://localhost:8080/specification/openapi`

The `Songs` part of the specification is what the default endpoints 
will look like without any further work.

#### API Metadata

With the configuration set the metadata will only include the name of the API, but it is best to add some additional information. This can be done in the application class, i.e. `DemoApplication.java`

### Endpoint Descriptions

The same applies to the endpoints where there are a number of annotations can be added to each endpoint in the
controllers to explain what each endpoint should do.
`BookController` has examples of the annotations that can be used to describe the behaviour of the endpoints.

### Thoughts
The creation of the documentation is easy and the maintaining it is 
relatively simple, however it relies on the developer updating the 
model examples and response status codes within the OpenApi 
annotations so that the documentation remains accurate to the 
behaviour of the API as features are added or changed.  
The documentation is easily readable online and users of the API 
are able to view the documentation if they have access to the link.

### Links
[SpringDoc OpenAPI](https://springdoc.org/v2/#getting-started)

## Spring RestDocs
RestDocs is embedded in the Spring Framework and is maintained as part 
of Spring and has implementations for Rest-Assured, MockMvc, and 
WebTestClient, with MockMvc being preferred.

### Implementation
The `Songs` endpoints are used as examples of the implementation.

#### Configuration 
RestDoc uses Asciidoctor to generate the documentation so the 
dependencies and plugin are needed for both RestDoc and AsciiDoctor. 
The AsciiDoctor configurations are included in the gradle build file and
specify where to store the source files and output files.

#### Endpoints
Documenting the endpoints is done during the mock tests where the mock
is set up to record the interactions and produce AsciiDoctor source 
files based on the test data and outcomes.  
The test verification can include checks for the shape of the requests 
and responses which are then added to the source files.

#### Documentation
Documenting the endpoints requires an additional step where 
specification templates(s) are created and designed using the source 
files created during the test run.  
AsciiDoctor formatting is used though MarkDown is also possible but not 
preferred. As the template is manually created using the AsciiDoctor 
DSL the final documentation can be as complicated are the developer 
wishes it to be.  
The documentation can be generated during development by running  
`./gradlew asciiDoctor`  
which will run the tests to generate the latest versions of the source 
files before creating the html documentation in the output directory 
specified in the configuration.

### Thoughts
The benefit of using RestDocs is that if the shape of the request and 
responses change then the tests will fail if the tests include the 
shape documenting code, reminding the developer to keep the 
documentation up to date.  However, adding new endpoints to the 
documentation is not an automatic step and requires the developer to 
update the AsciiDoctor template which can be time-consuming.

### Links

[Spring RestDocs](https://spring.io/projects/spring-restdocs)  
[RestDoc documentation](https://docs.spring.io/spring-restdocs/docs/current/reference/htmlsingle/#introduction)  
[Asciidoctor](http://asciidoctor.org/)  
[Gradle Plugin documentation](https://asciidoctor.github.io/asciidoctor-gradle-plugin/development-3.x/user-guide/)

## Conclusion