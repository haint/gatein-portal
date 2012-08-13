/**
 * Copyright (C) 2012 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.exoplatform.portal.gadget.core;

import com.google.inject.Injector;

import org.apache.shindig.common.servlet.GuiceServletContextListener;
import org.apache.shindig.config.ContainerConfig;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author <a href="nguyenanhien2a@gmail.com">Kien Nguyen</a>
 * @version $Revision$
 */

public class ContainerFilter implements Filter
{
   ServletContext context;
   
   /**
    * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
    */
   public void init(FilterConfig filterConfig) throws ServletException
   {
      context = filterConfig.getServletContext();
   }

   /**
    * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
    */
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
      ServletException
   {
      //Lazy loading for gadget container
      String container = request.getParameter("container");
      if (container != null)
      {
         Injector injector = (Injector) context.getAttribute(GuiceServletContextListener.INJECTOR_ATTRIBUTE);
         ExoContainerConfig config = injector.getInstance(ExoContainerConfig.class);
         if (!config.getContainers().contains(container))
         {
            //TODO should define how/where to load configuration of this container
            //Temporarily we use configuration of "default" container to set for this container
            Map<String, Object> props = config.getProperties(ContainerConfig.DEFAULT_CONTAINER);
            if (props != null)
            {
               config.addContainer(container, props);
            }
         }
      }
      
      chain.doFilter(request, response);
   }

   /**
    * @see javax.servlet.Filter#destroy()
    */
   public void destroy()
   {

   }

}
