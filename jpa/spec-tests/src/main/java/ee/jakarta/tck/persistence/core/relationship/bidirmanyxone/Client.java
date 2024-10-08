/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.relationship.bidirmanyxone;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {



	public Client() {
	}
	public static void main(String[] args) {
		Client theTests = new Client();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {

			super.setup(args,p);
			removeTestData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: biDirMX1Test1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1094; PERSISTENCE:JAVADOC:135;
	 * PERSISTENCE:JAVADOC:91; PERSISTENCE:SPEC:561; PERSISTENCE:SPEC:562;
	 * PERSISTENCE:SPEC:567; PERSISTENCE:SPEC:570; PERSISTENCE:SPEC:571;
	 * PERSISTENCE:SPEC:573; PERSISTENCE:SPEC:961; PERSISTENCE:SPEC:1028;
	 * PERSISTENCE:SPEC:1037; PERSISTENCE:SPEC:1038; PERSISTENCE:SPEC:1039
	 *
	 * @test_Strategy: Bi-Directional RelationShip ManyToOne Mapping
	 *
	 */
	public void biDirMX1Test1() throws Exception {
		logTrace( "Begin biDirMX1Test1");
		boolean pass = false;
		try {

			getEntityTransaction().begin();

			BiDirMX1Project project1 = new BiDirMX1Project(1L, "JavaEE", 500.0F);

			BiDirMX1Person person1 = new BiDirMX1Person(1L, "Duke");
			BiDirMX1Person person2 = new BiDirMX1Person(2L, "Foo");

			getEntityManager().persist(project1);
			getEntityManager().persist(person1);
			getEntityManager().persist(person2);
			logTrace( "persisted Persons and Project");

			person1.setProject(project1);
			person2.setProject(project1);

			List<BiDirMX1Person> list = new ArrayList<BiDirMX1Person>();
			list.add(person1);
			list.add(person2);
			project1.setBiDirMX1Persons(list);

			getEntityManager().merge(person1);
			logTrace( "merged contents of Person1");
			getEntityManager().merge(person2);
			logTrace( "merged contents of Person2");

			getEntityManager().flush();
			getEntityTransaction().commit();

			boolean pass1 = false;
			boolean pass2 = false;

			getEntityTransaction().begin();
			BiDirMX1Project newProject = getEntityManager().find(BiDirMX1Project.class, 1L);
			if (newProject != null) {
				List<BiDirMX1Person> persons = newProject.getBiDirMX1Persons();
				for (BiDirMX1Person person : persons) {
					if (person.getName().equals("Duke")) {
						logTrace( "Found Searched Person");
						pass1 = true;
					} else if (person.getName().equals("Foo")) {
						logTrace( "Found Searched Person");
						pass2 = true;

					} else {
						logTrace( "searched Person not Found");
					}

				}

			} else {
				logTrace( "searched Project not Found");

			}

			if (pass1 && pass2) {
				logTrace( "biDirMX1Test1: Expected results received");
				pass = true;
			} else {
				logErr( "Unexpected results received");
				pass = false;
			}

			getEntityTransaction().commit();

		} catch (Exception e) {

			logErr( "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass) {
			throw new Exception("biDirMX1Test1 failed");
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

	private void removeTestData() {
		logTrace( "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM BIDIRMX1PERSON").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM BIDIRMX1PROJECT").executeUpdate();
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
