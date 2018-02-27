/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.rapidpm.vaadin.addon.di.ddi;

import com.vaadin.server.*;
import com.vaadin.shared.Registration;
import org.jsoup.nodes.Element;
import org.rapidpm.frp.model.serial.Pair;
import org.vaadin.leif.headertags.HeaderTagHandler;

import javax.servlet.ServletException;
import java.util.stream.Stream;

public abstract class DDIVaadinServlet extends VaadinServlet {

  private Registration tagRegistration;

  @Override
  protected void servletInitialized() throws ServletException {
    super.servletInitialized();
    HeaderTagHandler.init(getService());
    tagRegistration = getService().addSessionInitListener(tagInitListener());

  }

  @Override
  public void destroy() {
    super.destroy();
    tagRegistration.remove();
  }
  //add Metrics here

  @Override
  protected VaadinServletService createServletService(final DeploymentConfiguration deploymentConfiguration) throws ServiceException {
    final DDIVaadinServletService service = new DDIVaadinServletService(this, deploymentConfiguration, topLevelPackagesToActivate());
    service.init();
    return service;
  }

  /**
   * return a list of pkg names that are available for Injection
   *
   * @return return
   */
  public abstract Stream<String> topLevelPackagesToActivate();

  public abstract Stream<Pair<String, String>> attributesToAddToHTML();

  private SessionInitListener tagInitListener() {
    return e ->
        e.getSession()
         .addBootstrapListener(new BootstrapListener() {
           @Override
           public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
             // NOP, this is for portlets etc
           }

           @Override
           public void modifyBootstrapPage(BootstrapPageResponse response) {
             final Element child = response
                 .getDocument()
                 .child(0);
             attributesToAddToHTML()
                 .forEach(p -> child.attr(p.getT1(), p.getT2()));


           }
         });
  }
}
