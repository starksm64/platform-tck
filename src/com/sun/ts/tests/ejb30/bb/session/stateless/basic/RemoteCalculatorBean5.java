/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * $Id$
 */

package com.sun.ts.tests.ejb30.bb.session.stateless.basic;

import com.sun.ts.tests.ejb30.common.calc.BaseRemoteCalculator;
import com.sun.ts.tests.ejb30.common.calc.RemoteCalculator;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.EJBException;
import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;
import jakarta.ejb.SessionContext;
import jakarta.annotation.Resource;

//@Stateless(name="RemoteCalculatorBean5",
//        description="a simple stateless session bean without component-defining annotations")
//@Remote({RemoteCalculator.class})
public class RemoteCalculatorBean5 extends BaseRemoteCalculator {
  private boolean postConstructCalled;

  @Resource
  private SessionContext sessionContext;

  public RemoteCalculatorBean5() {
  }

  @PostConstruct
  public void postConstruct() {
    postConstructCalled = true;
  }

  @Override
  public int remoteAdd(int a, int b) {
    if (sessionContext == null) {
      throw new EJBException(
          "SessionContext is null in business method remoteAdd: " + this);
    }
    if (!postConstructCalled) {
      throw new EJBException(
          "PostConstruct method has not been called when the business method is invoked.");
    }
    return super.remoteAdd(a, b);
  }

}
