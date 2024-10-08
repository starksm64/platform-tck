/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.entityManager;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

public class Client3 extends PMClientBase {
	
	List<Employee> empRef = new ArrayList<Employee>();

	Employee emp0 = null;

	Order[] orders = new Order[5];

	Properties props = null;

	Map map = new HashMap<String, Object>();

	String dataBaseName = null;

	final static String ORACLE = "oracle";

	final static String POSTGRESQL = "postgresql";

	public Client3() {
	}
	public static void main(String[] args) {
		Client3 theTests = new Client3();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}


	/*
	 * setup() is called before each test
	 *
	 * @class.setup_props: jdbc.db;
	 */
	
	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args,p);
			removeTestData();
			createEmployeeData();
			map.putAll(getEntityManager().getProperties());
			map.put("foo", "bar");
			displayMap(map);
			dataBaseName = System.getProperty("jdbc.db");
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}
	
	public void cleanup() throws Exception {
		try {
			logTrace( "Cleanup data");
			removeTestData();
			cleanup();
		} finally {

        }
	}

	public List<List> getResultSetsFromStoredProcedure(StoredProcedureQuery spq) {
		logTrace( "in getResultSetsFromStoredProcedure");
		boolean results = true;
		List<List> listOfList = new ArrayList<List>();
		int rsnum = 1;
		int rowsAffected = 0;

		do {
			if (results) {
				logTrace( "Processing set:" + rsnum);
				List<Employee> empList = new ArrayList<Employee>();
				List list = spq.getResultList();
				if (list != null) {
					logTrace( "Getting result set: " + (rsnum) + ", size:" + list.size());
					for (Object o : list) {
						if (o instanceof Employee) {
							Employee e = (Employee) o;
							logTrace( "Saving:" + e);
							empList.add(e);
						} else {
							logErr(
									"Did not get instance of Employee, instead got:" + o.getClass().getName());
						}
					}
					if (empList.size() > 0) {
						listOfList.add(empList);
					}
				} else {
					logErr( "Result set[" + rsnum + "] returned was null");
				}
				rsnum++;
			} else {
				rowsAffected = spq.getUpdateCount();
				if (rowsAffected >= 0)
					logTrace( "rowsAffected:" + rowsAffected);
			}
			results = spq.hasMoreResults();
			logTrace( "Results:" + results);

		} while (results || rowsAffected != -1);
		return listOfList;
	}

	public boolean verifyListOfListEmployeeIds(List<Integer> expected, List<List> listOfList) {
		boolean result = false;
		int count = 0;
		for (List<Employee> lEmp : listOfList) {

			if (lEmp.size() > 0) {
				List<Integer> actual = new ArrayList<Integer>();
				for (Employee e : lEmp) {
					actual.add(e.getId());
				}

				if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {
					logTrace( "Received expected result:");
					for (Integer a : actual) {
						logTrace( "id:" + a);
					}
					count++;
				} else {
					logErr( "Did not receive expected result:");
					for (Integer e : expected) {
						logErr( " Expected id:" + e);
					}
					for (Integer a : actual) {
						logErr( "Actual id:" + a);
					}
				}

			} else {
				logErr( "Result set that was returned had 0 length");
			}

		}
		if (count == listOfList.size()) {
			result = true;
		}
		return result;
	}

	public boolean verifyListOfListEmployees(List<Employee> expected, List<List> listOfList) {
		boolean result = false;
		int count = 0;
		for (List<Employee> lEmp : listOfList) {

			if (lEmp.size() > 0) {
				List<Employee> actual = new ArrayList<Employee>();
				for (Employee e : lEmp) {
					actual.add(e);
				}
				if (verifyListEmployees(expected, actual)) {
					count++;
				}
			} else {
				logErr( "Result set that was returned had 0 length");
			}
		}
		if (count == listOfList.size()) {
			result = true;
		}
		return result;
	}

	public boolean verifyListEmployees(List<Employee> expected, List<Employee> actual) {
		boolean result = false;
		if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {
			for (Employee e : expected) {
				logTrace( "Received expected result:" + e);
			}
			result = true;
		} else {
			logErr( "Did not receive expected result:");
			for (Employee e : expected) {
				logErr( "expected employee:" + e);
			}
			for (Employee e : actual) {
				logErr( "actual employee :" + e);
			}
		}
		return result;
	}

	/*
	 * @testName: createStoredProcedureQueryStringTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:1520
	 *
	 * @test_Strategy:
	 *
	 */
	
	public void createStoredProcedureQueryStringTest() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		try {
			StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpOneFirstNameFromOut");
			spq.registerStoredProcedureParameter(1, String.class, ParameterMode.OUT);
			spq.execute();

			Object oActual = spq.getOutputParameterValue(1);
			if (oActual instanceof String) {
				String actual = (String) oActual;
				if (actual.equals(emp0.getFirstName())) {
					logTrace( "Received expected result:" + actual);
					pass = true;
				} else {
					logErr( "Expected result: " + emp0.getFirstName() + ", actual:" + actual);
				}
			} else {
				logErr( "Expected String to be returned, actual:" + oActual.getClass());
			}
		} catch (Exception ex) {
			logErr( "Received unexpected exception:", ex);
		}
		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("createStoredProcedureQueryStringTest failed");
		}

	}

	/*
	 * @testName: createStoredProcedureQueryStringClassArrayTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:1522
	 *
	 * @test_Strategy:
	 *
	 */
	
	public void createStoredProcedureQueryStringClassArrayTest() throws Exception {
		boolean pass = false;
		getEntityTransaction().begin();
		try {
			Class[] cArray = { Employee.class };
			StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpASCFromRS", cArray);
			if (dataBaseName.equalsIgnoreCase(ORACLE) || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
				logTrace( "register refcursor parameter");
				spq.registerStoredProcedureParameter(1, void.class, ParameterMode.REF_CURSOR);
			}
			if (spq.execute()) {
				List<List> listOfList = getResultSetsFromStoredProcedure(spq);
				if (listOfList.size() == 1) {
					List<Integer> expected = new ArrayList<Integer>();
					for (Employee e : empRef) {
						expected.add(e.getId());
					}
					pass = verifyListOfListEmployeeIds(expected, listOfList);
				} else {
					logErr(
							"Did not get the correct number of result sets returned, expected: 1, actual:"
									+ listOfList.size());
				}
			} else {
				logErr( "Expected execute() to return true, actual: false");
			}

		} catch (Exception ex) {
			logErr( "Received unexpected exception:", ex);
		}
		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("createStoredProcedureQueryStringClassArrayTest failed");
		}

	}

	/*
	 * @testName: createStoredProcedureQueryStringStringArrayTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:1524; PERSISTENCE:SPEC:1571;
	 *
	 * @test_Strategy:
	 *
	 */
	
	public void createStoredProcedureQueryStringStringArrayTest() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		clearCache();
		try {
			String[] sArray = { "id-firstname-lastname" };
			StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpIdFNameLNameFromRS",
					sArray);
			spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			if (dataBaseName.equalsIgnoreCase(ORACLE) || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
				logTrace( "register refcursor parameter");
				spq.registerStoredProcedureParameter(2, void.class, ParameterMode.REF_CURSOR);
			}
			spq.setParameter(1, 1);

			if (spq.execute()) {

				List<List> listOfList = getResultSetsFromStoredProcedure(spq);
				if (listOfList.size() == 1) {
					List<Employee> expected = new ArrayList<Employee>();
					expected.add(new Employee(emp0.getId(), emp0.getFirstName(), emp0.getLastName()));
					pass = verifyListOfListEmployees(expected, listOfList);
				} else {
					logErr(
							"Did not get the correct number of result sets returned, expected: 1, actual:"
									+ listOfList.size());
				}
			} else {
				logErr( "Expected execute() to return true, actual: false");
			}
		} catch (Exception ex) {
			logErr( "Received unexpected exception:", ex);
		}
		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("createStoredProcedureQueryStringStringArrayTest failed");
		}

	}

	/*
	 * @testName: createNamedStoredProcedureQueryStringTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:1514; PERSISTENCE:JAVADOC:1530;
	 * PERSISTENCE:JAVADOC:1532; PERSISTENCE:JAVADOC:1533; PERSISTENCE:JAVADOC:1534;
	 * PERSISTENCE:JAVADOC:1541; PERSISTENCE:JAVADOC:1543;
	 *
	 * @test_Strategy:
	 *
	 */
	
	public void createNamedStoredProcedureQueryStringTest() throws Exception {
		boolean pass = false;
		getEntityTransaction().begin();
		clearCache();
		try {
			StoredProcedureQuery spq = null;
			if (dataBaseName.equalsIgnoreCase(ORACLE) || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
				logTrace( "Calling refcursor specific named stored procedure query");
				spq = getEntityManager().createNamedStoredProcedureQuery("get-id-firstname-lastname-refcursor");
			} else {
				spq = getEntityManager().createNamedStoredProcedureQuery("get-id-firstname-lastname");
			}
			spq.setParameter(1, 1);
			if (spq.execute()) {
				List<List> listOfList = getResultSetsFromStoredProcedure(spq);
				if (listOfList.size() == 1) {
					List<Employee> expected = new ArrayList<Employee>();
					expected.add(new Employee(emp0.getId(), emp0.getFirstName(), emp0.getLastName()));
					pass = verifyListOfListEmployees(expected, listOfList);
				} else {
					logErr(
							"Did not get the correct number of result sets returned, expected: 1, actual:"
									+ listOfList.size());
				}
			} else {
				logErr( "Expected execute() to return true, actual: false");
			}

		} catch (Exception ex) {
			logErr( "Received unexpected exception:", ex);
		}
		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("createNamedStoredProcedureQueryStringTest failed");
		}

	}

	private void createEmployeeData() {

		try {
			getEntityTransaction().begin();
			logMsg( "Creating Employees");

			final Date d1 = getUtilDate("2000-02-14");
			final Date d2 = getUtilDate("2001-06-27");
			final Date d3 = getUtilDate("2002-07-07");
			final Date d4 = getUtilDate("2003-03-03");
			final Date d5 = getUtilDate();

			emp0 = new Employee(1, "Alan", "Frechette", d1, (float) 35000.0);
			empRef.add(emp0);
			empRef.add(new Employee(2, "Arthur", "Frechette", d2, (float) 35000.0));
			empRef.add(new Employee(3, "Shelly", "McGowan", d3, (float) 50000.0));
			empRef.add(new Employee(4, "Robert", "Bissett", d4, (float) 55000.0));
			empRef.add(new Employee(5, "Stephen", "DMilla", d5, (float) 25000.0));
			for (Employee e : empRef) {
				if (e != null) {
					getEntityManager().persist(e);
					logTrace( "persisted employee:" + e);
				}
			}
			getEntityManager().flush();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logErr( "Unexpected exception rolling back TX:", fe);
			}
		}
	}

	private void removeTestData() {
		logTrace( "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER").executeUpdate();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Exception encountered while removing entities:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in removeTestData:", re);
			}
		}
	}
}
