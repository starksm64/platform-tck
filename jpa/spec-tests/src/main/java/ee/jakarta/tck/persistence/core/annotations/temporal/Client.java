/*
 * Copyright (c) 2008, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.temporal;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;





import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {



	private static final long serialVersionUID = 21L;
	Date date = null;
	Date date2 = null;

	Calendar calendar = null;
	Calendar calendar2 = null;
	A_Field aFieldRef;
	A_Property aPropertyRef;
	A2_Field a2FieldRef;
	A2_Property a2PropertyRef;
	List<Date> expectedDates;
	List<Calendar> expectedCalendars;

	public Client() {
	}
	public static void main(String[] args) {
		Client theTests = new Client();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}



	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setupData");
		try {
			super.setup(args,p);
			

			removeTestData();
			createTestData();

		} catch (Exception e) {
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: basicFieldTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2114; PERSISTENCE:SPEC:2114.1;
	 * 
	 * @test_Strategy: Used with a Basic annotation for field access
	 */
		public void basicFieldTest() throws Exception {

		boolean pass = false;
		try {
			A_Field a = getEntityManager().find(A_Field.class, "1");
			if (a != null) {
				if (aFieldRef.equals(a)) {
					pass = true;
				} else {
					logErr( "Expected:" + aFieldRef.toString() + ", actual:" + a.toString());
				}
			} else {
				logErr( "Find returned null result");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("basicFieldTest failed");
		}

	}

	/*
	 * @testName: basicPropertyTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2114; PERSISTENCE:SPEC:2114.1;
	 *
	 * @test_Strategy: Used with a Basic annotation for property access
	 */
		public void basicPropertyTest() throws Exception {
		boolean pass = false;
		try {
			A_Property a = getEntityManager().find(A_Property.class, "2");
			if (a != null) {
				if (aPropertyRef.equals(a)) {
					logTrace( "Received expected entity:" + a.toString());
					pass = true;
				} else {
					logErr( "Expected:" + aPropertyRef.toString() + ", actual:" + a.toString());
				}
			} else {
				logErr( "Find returned null result");
			}

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("basicPropertyTest failed");
		}

	}

	/*
	 * @testName: fieldElementCollectionTemporalTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2114; PERSISTENCE:SPEC:2114.3;
	 * 
	 * @test_Strategy: ElementCollection of a basic type
	 */
		public void fieldElementCollectionTemporalTest() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			logTrace( "find the previously persisted Customer and Country and verify them");
			A_Field a = getEntityManager().find(A_Field.class, "1");
			if (a != null) {
				logTrace( "Found A: " + a.toString());
				if (a.getDates().containsAll(expectedDates) && expectedDates.containsAll(a.getDates())
						&& a.getDates().size() == expectedDates.size()) {
					logTrace( "Received expected Dates:");
					for (Date d : a.getDates()) {
						logTrace( "date:" + d);
					}
					pass = true;
				} else {
					logErr( "Did not get expected results.");
					for (Date d : expectedDates) {
						logErr( "expected:" + d);
					}
					logErr( "actual:");
					for (Date d : a.getDates()) {
						logErr( "actual:" + d);
					}
				}
			} else {
				logErr( "Find returned null A");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Unexpected exception occurred: ", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("fieldElementCollectionTemporalTest failed");
		}
	}

	/*
	 * @testName: propertyElementCollectionTemporalTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2114; PERSISTENCE:SPEC:2114.3;
	 * 
	 * @test_Strategy: ElementCollection of a basic type
	 */
		public void propertyElementCollectionTemporalTest() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			logTrace( "find the previously persisted Customer and Country and verify them");
			A_Property a = getEntityManager().find(A_Property.class, "2");
			if (a != null) {
				logTrace( "Found A2: " + a.toString());
				if (a.getDates().containsAll(expectedCalendars) && expectedCalendars.containsAll(a.getDates())
						&& a.getDates().size() == expectedCalendars.size()) {
					logTrace( "Received expected Dates:");
					for (Calendar d : a.getDates()) {
						logTrace( "date:" + d);
					}
					pass = true;
				} else {
					logErr( "Did not get expected results.");
					for (Date d : expectedDates) {
						logErr( "expected:" + d);
					}
					logErr( "actual:");
					for (Calendar d : a.getDates()) {
						logErr( "actual:" + d);
					}
				}
			} else {
				logErr( "Find returned null A2");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Unexpected exception occurred: ", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("propertyElementCollectionTemporalTest failed");
		}
	}

	/*
	 * @testName: idFieldTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2114; PERSISTENCE:SPEC:2114.2;
	 *
	 * @test_Strategy: Used with a id annotation for field access
	 */
		public void idFieldTest() throws Exception {

		boolean pass = false;
		try {
			A2_Field a = getEntityManager().find(A2_Field.class, date2);
			if (a != null) {
				if (a2FieldRef.equals(a)) {
					pass = true;
				} else {
					logErr( "Expected:" + a2FieldRef.toString() + ", actual:" + a.toString());
				}
			} else {
				logErr( "Find returned null result");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("idFieldTest failed");
		}

	}

	/*
	 * @testName: idPropertyTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2114; PERSISTENCE:SPEC:2114.2;
	 *
	 * @test_Strategy: Used with a id annotation for property access
	 */
		public void idPropertyTest() throws Exception {
		boolean pass = false;
		try {
			A2_Property a = getEntityManager().find(A2_Property.class, calendar2);
			if (a != null) {
				if (a2PropertyRef.equals(a)) {
					logTrace( "Received expected entity:" + a.toString());
					pass = true;
				} else {
					logErr( "Expected:" + a2PropertyRef.toString() + ", actual:" + a.toString());
				}
			} else {
				logErr( "Find returned null result");
			}

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("idPropertyTest failed");
		}

	}

	
	public void cleanup() throws Exception {
		try {
			logTrace( "cleanup");
			removeTestData();
			logTrace( "cleanup complete, calling super.cleanup");
			super.cleanup();
		} finally {

        }
	}

	public void createTestData() {
		logTrace( "createTestData");

		try {
			getEntityTransaction().begin();

			date = getSQLDate();
			LocalDate localDate = ((java.sql.Date) date).toLocalDate();
			date2 = getUtilDate("2000-02-14");
			String sDate2 = "2000-02-14";

			calendar = Calendar.getInstance();
			calendar.clear();
			calendar.set(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
			calendar2 = getCalDate(2001, 06, 27);
			String sCalendar2 = "2001-06-27";

			expectedDates = new ArrayList<Date>();
			expectedDates.add(getUtilDate("2000-02-14"));
			expectedDates.add(getUtilDate("2001-06-27"));
			expectedDates.add(getUtilDate("2002-07-07"));
			aFieldRef = new A_Field("1", date, calendar, expectedDates);

			expectedCalendars = new ArrayList<Calendar>();
			expectedCalendars.add(getCalDate(2000, 02, 14));
			expectedCalendars.add(getCalDate(2001, 06, 27));
			expectedCalendars.add(getCalDate(2002, 07, 07));
			aPropertyRef = new A_Property("2", date, calendar, expectedCalendars);
			getEntityManager().persist(aFieldRef);
			getEntityManager().persist(aPropertyRef);

			a2FieldRef = new A2_Field(date2, sDate2);
			a2PropertyRef = new A2_Property(calendar2, sCalendar2);

			getEntityManager().persist(a2FieldRef);
			getEntityManager().persist(a2PropertyRef);

			getEntityManager().flush();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Unexpected Exception in createTestData:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
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
			getEntityManager().createNativeQuery("DELETE FROM A_BASIC").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DATE_TABLE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DATES_TABLE").executeUpdate();
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
