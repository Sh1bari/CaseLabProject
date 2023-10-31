package com.example.caselabproject.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@OpenAPIDefinition(
        info = @Info(
                description = "hello"
        )
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

    /*@Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**") // Укажите путь к вашим API-контроллерам
                .addOperationCustomizer(addAuthorizationHeader())
                .build();
    }
    @Bean
    public OperationCustomizer addAuthorizationHeader() {
        return (operation, handlerMethod) -> {
            if (operation.getParameters() == null) {
                operation.setParameters(new ArrayList<>());
            }
            Parameter parameter = new Parameter();
            parameter.name("Authorization");
            parameter.description("Заголовок авторизации Bearer");
            parameter.required(true);
            parameter.in("Header");
            parameter.schema(new StringSchema().type("string").format("string"));
            parameter.example("Bearer YOUR_ACCESS_TOKEN");
            operation.addParametersItem(parameter);
            return operation;
        };
    }*/

}
