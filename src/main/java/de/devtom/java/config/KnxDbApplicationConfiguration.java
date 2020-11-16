package de.devtom.java.config;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class KnxDbApplicationConfiguration {
	public static final String BASE_PATH = "/knx-db";
	private static final String API_TITLE = "KNX DB API";
	private static final String API_DESCRIPTION = "API to access and modify a basic KNX database";
	private static final String API_VERSION = "1.0.0";
	private static final String API_TERMS_OF_SERVICE_URL = null;
	private static final Contact API_CONTACT = new Contact("KNX DB API Team", "http://devtom.de",
			"knx-db-api@devtom.de");
	private static final String API_LICENSE = null;
	private static final String API_LICENSE_URL = null;
	@SuppressWarnings("rawtypes")
	private static final Collection<VendorExtension> API_VENDOR_EXTENSIONS = new ArrayList<>();

	@Autowired
	private TypeResolver typeResolver;

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.basePackage("de.devtom.java.rest.controllers"))
				.paths(PathSelectors.any()).build()
				.directModelSubstitute(LocalDate.class, String.class).genericModelSubstitutes(ResponseEntity.class)
				.alternateTypeRules(newRule(
						typeResolver.resolve(DeferredResult.class,
								typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
						typeResolver.resolve(WildcardType.class)))
				.useDefaultResponseMessages(false)
				.enableUrlTemplating(false);
	}

	private ApiInfo apiInfo() {
		return new ApiInfo(API_TITLE, API_DESCRIPTION, API_VERSION, API_TERMS_OF_SERVICE_URL, API_CONTACT, API_LICENSE,
				API_LICENSE_URL, API_VENDOR_EXTENSIONS);
	}
}
