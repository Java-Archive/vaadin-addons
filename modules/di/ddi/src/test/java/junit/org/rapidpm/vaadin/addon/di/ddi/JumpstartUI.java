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

package junit.org.rapidpm.vaadin.addon.di.ddi;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.vaadin.addon.di.ddi.JumpstartUIComponentFactory;

import javax.inject.Inject;

@PreserveOnRefresh
@Title("JumpstartServlet")
@Push
public class JumpstartUI extends UI implements HasLogger {


  @Inject private JumpstartUIComponentFactory jumpstartUIComponentFactory;

  @Override
  protected void init(VaadinRequest vaadinRequest) {
    logger().fine("init - request = " + vaadinRequest);
    logger().fine("init - request getWrappedSession id = " + vaadinRequest.getWrappedSession().getId());
    setContent(jumpstartUIComponentFactory.createComponentToSetAsContent(vaadinRequest));
    setSizeFull();
  }

}
