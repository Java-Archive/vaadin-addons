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

import com.vaadin.annotations.VaadinServletConfiguration;
import org.rapidpm.frp.model.serial.Pair;
import org.rapidpm.vaadin.addon.di.ddi.DDIVaadinServlet;

import javax.servlet.annotation.WebServlet;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static java.util.stream.Stream.of;
import static org.rapidpm.frp.model.serial.Pair.next;

@WebServlet(
    urlPatterns = "/*",
    name = "JumpstartServlet",
    displayName = "JumpstartServlet",
    asyncSupported = true)
@VaadinServletConfiguration(ui = JumpstartUI.class, productionMode = false)
public class JumpstartServlet extends DDIVaadinServlet {

  @Override
  public Stream<String> topLevelPackagesToActivate() {
    return of("org.rapidpm");
  }

  @Override
  public Stream<Pair<String, String>> attributesToAddToHTML() {
    return of(next("lang", "en_US"));
  }
}
