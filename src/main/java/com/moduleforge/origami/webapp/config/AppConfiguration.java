/*
 *    This file is part of "Origami".
 *
 *     Origami is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Origami is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Origami.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.moduleforge.origami.webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

//import ro.isdc.wro.http.ConfigurableWroFilter;

@SuppressWarnings("nls")
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.moduleforge.origami.webapp.controller")
public class AppConfiguration extends WebMvcConfigurerAdapter {
   @SuppressWarnings("static-method")
   @Bean
   public ViewResolver viewResolver() {
      InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
      viewResolver.setViewClass(JstlView.class);
      viewResolver.setPrefix("/WEB-INF/views/");
      viewResolver.setSuffix(".jsp");
      return viewResolver;
   }
//   @Bean
//   public Properties wroProperties() {
//      PropertiesFactoryBean pfb = new PropertiesFactoryBean();
//      pfb.setLocation(new ClassPathResource("WEB-INF/wro.properties"));
//      Properties prop= null;
//      try {
//         prop = pfb.getObject();
//      } catch (IOException e) {
//         e.printStackTrace();
//      }
//      return prop;
//   }
//   @Bean(name="wroFilter")
//   public ConfigurableWroFilter configurableWroFilter() {
//      ConfigurableWroFilter cwfilter = new ConfigurableWroFilter();
//      cwfilter.setProperties(wroProperties());
//      return cwfilter;
//   }
   @Override
   public void addResourceHandlers(final ResourceHandlerRegistry registry) {
      registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
      registry.addResourceHandler("/img/**").addResourceLocations("/img/");
      registry.addResourceHandler("/js/**").addResourceLocations("/js/");
      registry.addResourceHandler("/css/**").addResourceLocations("/css/");
      registry.addResourceHandler("/font/**").addResourceLocations("/font/");
   }
   @Override
   public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
      configurer.enable();
   }

}
