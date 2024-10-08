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

package ee.jakarta.tck.persistence.core.inheritance.nonentity;


import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/*
 * PartTimeEmployee
 */

@Entity
@DiscriminatorValue("NONEXEMPT")
public class PartTimeEmployee extends Employee {

	

	private float wage;

	public PartTimeEmployee() {
	}

	public PartTimeEmployee(int id, String firstName, String lastName, Date hireDate, float wage) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.hireDate = hireDate;
		this.wage = wage;
	}

	public PartTimeEmployee(int id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	// ===========================================================
	// getters and setters for the state fields

	@Column(name = "SALARY")
	public float getWage() {
		return wage;
	}

	public void setWage(float wage) {
		this.wage = wage;
	}

}
