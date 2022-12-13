package com.vrp.demo.configuration;

//import com.vrp.demo.configuration.multitenancy.RequestInterceptor;
import com.vrp.demo.configuration.multitenancy.RequestInterceptor;
import com.vrp.demo.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

@Configuration
@EnableWebMvc
@EnableAsync
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private RequestInterceptor requestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*","sessionID", "X-TenantID", "content-type,x-tenantid")
                .allowedMethods("*");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.indentOutput(true).dateFormat(CommonUtils.getSimpleDateFormat());
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }

//    @Bean
//    public LocaleResolver localeResolver() {
//        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
//        localeResolver.setSupportedLocales(CommonUtils.getSupportedLocale());
//        localeResolver.setDefaultLocale(Locale.US);
//        return localeResolver;
//    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n.messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
