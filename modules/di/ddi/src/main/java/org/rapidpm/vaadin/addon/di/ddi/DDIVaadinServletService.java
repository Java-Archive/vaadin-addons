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
import com.vaadin.ui.UI;
import org.rapidpm.ddi.DI;
import org.rapidpm.dependencies.core.logger.HasLogger;

import javax.annotation.PostConstruct;
import java.util.stream.Stream;


public class DDIVaadinServletService extends VaadinServletService implements HasLogger {


  public DDIVaadinServletService(VaadinServlet servlet,
                                 DeploymentConfiguration deploymentConfiguration,
                                 Stream<String> topLevelPackagesToActivated)
      throws ServiceException {

    super(servlet, deploymentConfiguration);

    topLevelPackagesToActivated
        .filter(pkg -> !DI.isPkgPrefixActivated(pkg))
        .forEach(DI::activatePackages);

    addSessionInitListener(event -> event.getSession().addUIProvider(new DefaultUIProvider() {
      @Override
      public UI createInstance(final UICreateEvent event) {
        logger().fine("addSessionInitListener create UI instance event = " + event.getUiId());
        return DI.activateDI(event.getUIClass());
      }
    }));
    addSessionDestroyListener(event -> logger().fine("addSessionDestroyListener event = " + event.getSession()));

    addServiceDestroyListener(event -> logger().fine("addServiceDestroyListener event = " + event.getSource()));
  }


  @Override
  public void handleRequest(VaadinRequest request, VaadinResponse response) throws ServiceException {
    super.handleRequest(request, response);

    final VaadinSession current = VaadinSession.getCurrent();
    if (current != null) {
      final int size = current.getUIs().size();
      logger().fine(" handleRequest getUIs().size() size = " + size);
    }
  }

  @PostConstruct
  public void initialize() {
  }
}

