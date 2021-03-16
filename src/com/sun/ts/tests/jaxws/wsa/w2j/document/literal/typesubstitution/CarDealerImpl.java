/*
 * Copyright (c) 2007, 2021 Oracle and/or its affiliates. All rights reserved.
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
 * $Id: CarDealerImpl.java 51063 2006-08-11 19:56:36Z lschwenk $
 */

package com.sun.ts.tests.jaxws.wsa.w2j.document.literal.typesubstitution;

import java.util.ArrayList;
import java.util.List;

@jakarta.jws.WebService(portName = "CarDealerPort", serviceName = "CarDealerService", targetNamespace = "http://typesubstitution/wsdl", wsdlLocation = "WEB-INF/wsdl/WSAW2JDLTypeSubstitutionTest.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.wsa.w2j.document.literal.typesubstitution.CarDealer")
public class CarDealerImpl {
  public List<Car> getSedans() {
    List<Car> cars = new ArrayList<Car>();
    Toyota camry = new Toyota();

    camry.setMake("Toyota");
    camry.setModel("Camry");
    camry.setYear("1998");
    camry.setColor("white");

    cars.add(camry);

    Ford mustang = new Ford();

    mustang.setMake("Ford");
    mustang.setModel("Mustang");
    mustang.setYear("1999");
    mustang.setColor("red");
    cars.add(mustang);
    return cars;
  }
}