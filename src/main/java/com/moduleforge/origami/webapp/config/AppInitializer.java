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

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

@SuppressWarnings("nls")
public class AppInitializer implements WebApplicationInitializer {
   @Override
   public void onStartup(ServletContext container) throws ServletException {
      container.addListener( new RequestContextListener() );
      AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
      ctx.register(AppConfiguration.class);
      ctx.setServletContext(container);
      ServletRegistration.Dynamic servlet = container.addServlet("dispatcher", new DispatcherServlet(ctx));
      servlet.setLoadOnStartup(1);
      servlet.addMapping( "/");
      setFilters(container);
   }
   private void setFilters(ServletContext container) {
      FilterRegistration.Dynamic frDynamic = container.addFilter("webResourceOptimizer",
              new DelegatingFilterProxy());
      frDynamic.setInitParameter("targetBeanName", "wroFilter");
      frDynamic.setInitParameter("targetFilterLifecycle", "true");
      frDynamic.addMappingForUrlPatterns(null, true, "/wro/*");
   }
}
