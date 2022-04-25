package com.pedro.calculomental;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {


        registry.addViewController("/").setViewName("login");
        registry.addViewController("/login").setViewName("login");
        //registry.addViewController("/user").setViewName("user");
        registry.addViewController("/signup").setViewName("signup");

        //Se pueden quitar estos una vez que se hayan incluido en el controller
        registry.addViewController("/mainpanel").setViewName("mainpanel");
        registry.addViewController("/activitylist").setViewName("activitylist");
        registry.addViewController("/activity").setViewName("activity");


    }

}
	