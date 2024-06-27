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

/*
 * $Id$
 */
package com.sun.ts.tests.jms.core.exceptionQueue;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.BytesMessage;
import jakarta.jms.Destination;
import jakarta.jms.InvalidDestinationException;
import jakarta.jms.InvalidSelectorException;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.MessageEOFException;
import jakarta.jms.MessageFormatException;
import jakarta.jms.MessageNotReadableException;
import jakarta.jms.MessageNotWriteableException;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.QueueBrowser;
import jakarta.jms.QueueReceiver;
import jakarta.jms.QueueSender;
import jakarta.jms.StreamMessage;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;


public class ExceptionQueueTestsIT {
	private static final String testName = "com.sun.ts.tests.jms.core.exceptionQueue.ExceptionQueueTestsIT";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(ExceptionQueueTestsIT.class.getName());

	// JMS objects
	private transient JmsTool tool = null;

	// Harness req's
	private Properties props = null;

	// properties read
	long timeout;

	String user;

	String password;

	String mode;

	ArrayList queues = null;

	ArrayList connections = null;

	/* Test setup: */

	/*
	 * setup() is called before each test
	 * 
	 * Creates Administrator object and deletes all previous Destinations.
	 * Individual tests create the JmsTool object with one default Queue and/or
	 * Topic Connection, as well as a default Queue and Topic. Tests that require
	 * multiple Destinations create the extras within the test
	 * 
	 * 
	 * @class.setup_props: jms_timeout; user; password; platform.mode;
	 * 
	 * @exception Fault
	 */

	@BeforeEach
	public void setup() throws Exception {
		try {

			// get props
			timeout = Long.parseLong(System.getProperty("jms_timeout"));
			user = System.getProperty("user");
			password = System.getProperty("password");
			mode = System.getProperty("platform.mode");

			// check props for errors
			if (timeout < 1) {
				throw new Exception("'timeout' (milliseconds) in must be > 0");
			}
			if (user == null) {
				throw new Exception("'user' is null");
			}
			if (password == null) {
				throw new Exception("'password' is null");
			}
			queues = new ArrayList(2);
			connections = new ArrayList(2);

			// get ready for new test
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("Setup failed!", e);
		}
	}

	/* cleanup */

	/*
	 * cleanup() is called after each test
	 * 
	 * Closes the default connections that are created by setup(). Any separate
	 * connections made by individual tests should be closed by that test.
	 * 
	 * @exception Fault
	 */

	@AfterEach
	public void cleanup() throws Exception {
		try {
			if (tool != null) {
				logger.log(Logger.Level.INFO, "Cleanup: Closing Queue and Topic Connections");
				tool.doClientQueueTestCleanup(connections, queues);
			}

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			logger.log(Logger.Level.ERROR, "An error occurred while cleaning");
			throw new Exception("Cleanup failed!", e);
		}
	}

	/* Tests */

	/*
	 * @testName: xInvalidDestinationExceptionQTest
	 * 
	 * @assertion_ids: JMS:SPEC:174; JMS:JAVADOC:190; JMS:JAVADOC:192;
	 * JMS:JAVADOC:184; JMS:JAVADOC:186; JMS:JAVADOC:188; JMS:JAVADOC:622;
	 * JMS:JAVADOC:623; JMS:JAVADOC:618; JMS:JAVADOC:619; JMS:JAVADOC:621;
	 *
	 * @test_Strategy: pass an invalid Queue object to createBrowser(null)
	 * createBrowser(null,selector) createReceiver(null)
	 * createReceiver(null,selector) createSender(null)This null is valid
	 * 
	 */
	@Test
	public void xInvalidDestinationExceptionQTest() throws Exception {
		boolean pass = true;
		QueueBrowser qBrowser = null;
		QueueSender qSender = null;
		QueueReceiver qReceiver = null;

		try {

			// create Queue Connection and QueueBrowser
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			logger.log(Logger.Level.TRACE, "** Close default QueueReceiver **");
			tool.getDefaultQueueReceiver().close();
			tool.getDefaultQueueConnection().start();

			// create QueueBrowser
			logger.log(Logger.Level.INFO, "Test createBrowser(null)");
			Queue dummy = null;

			try {
				qBrowser = tool.getDefaultQueueSession().createBrowser(dummy);
				if (qBrowser != null)
					logger.log(Logger.Level.TRACE, "qBrowser=" + qBrowser);
				logger.log(Logger.Level.TRACE, "FAIL: expected InvalidDestinationException!");
				pass = false;
			} catch (Exception ee) {
				if (ee instanceof jakarta.jms.InvalidDestinationException) {
					logger.log(Logger.Level.TRACE, "Pass: InvalidDestinationException thrown as expected");
				} else {
					logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.INFO, "Test createBrowser(null),selector");
			try {
				qBrowser = tool.getDefaultQueueSession().createBrowser(dummy, "TEST");
				if (qBrowser != null)
					logger.log(Logger.Level.TRACE, "qBrowser=" + qBrowser);
				logger.log(Logger.Level.TRACE, "FAIL: expected InvalidDestinationException!");
				pass = false;
			} catch (Exception ee) {
				if (ee instanceof jakarta.jms.InvalidDestinationException) {
					logger.log(Logger.Level.TRACE, "Pass: InvalidDestinationException thrown as expected");
				} else {
					logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.INFO, "Test createReceiver(null)");
			try {
				qReceiver = tool.getDefaultQueueSession().createReceiver(dummy);
				if (qReceiver != null)
					logger.log(Logger.Level.TRACE, "qReceiver=" + qReceiver);
				logger.log(Logger.Level.TRACE, "FAIL: expected InvalidDestinationException!");
				pass = false;
			} catch (Exception ee) {
				if (ee instanceof jakarta.jms.InvalidDestinationException) {
					logger.log(Logger.Level.TRACE, "Pass: InvalidDestinationException thrown as expected");
				} else {
					logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.INFO, "Test createReceiver(null,selector)");
			try {
				qReceiver = tool.getDefaultQueueSession().createReceiver(dummy, "TEST");
				if (qReceiver != null)
					logger.log(Logger.Level.TRACE, "qReceiver=" + qReceiver);
				logger.log(Logger.Level.TRACE, "FAIL: expected InvalidDestinationException!");
				pass = false;
			} catch (Exception ee) {
				if (ee instanceof jakarta.jms.InvalidDestinationException) {
					logger.log(Logger.Level.TRACE, "Pass: InvalidDestinationException thrown as expected");
				} else {
					logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.INFO, "Test createSender(null) - null is valid here ");
			try {
				qSender = tool.getDefaultQueueSession().createSender(dummy);
				if (qSender != null)
					logger.log(Logger.Level.TRACE, "qSender=" + qSender);
				logger.log(Logger.Level.TRACE, "PASS: ");
			} catch (Exception ee) {
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during xInvalidDestinationExceptionQTest tests");
			}
		} catch (Exception e) {
			throw new Exception("xInvalidDestinationExceptionQTest", e);
		}
	}

	/*
	 * @testName: xMessageNotReadableExceptionQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:178; JMS:JAVADOC:680;
	 * 
	 * @test_Strategy: create a BytesMessage, read it.
	 */
	@Test
	public void xMessageNotReadableExceptionQueueTest() throws Exception {
		try {
			BytesMessage msg = null;

			// create Queue Connection
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();

			// Create a BytesMessage
			msg = tool.getDefaultQueueSession().createBytesMessage();
			msg.setStringProperty("COM_SUN_JMS_TESTNAME", "xMessageNotReadableExceptionQueueTest");
			try {
				msg.readByte();
				logger.log(Logger.Level.TRACE, "FAIL --- should not have gotten this far!");
				throw new Exception("Fail: Did not throw expected error!!!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "Passed.\n");
				// TestUtil.printStackTrace(nr);
				logger.log(Logger.Level.TRACE, "MessageNotReadableException occurred!");
				logger.log(Logger.Level.TRACE, " " + nr.getMessage());
			}
		} catch (Exception e) {
			logger.log(Logger.Level.TRACE, " " + e.getMessage());
			logger.log(Logger.Level.TRACE, "Expected MessageNotReadableException did not occur!");
			throw new Exception("xMessageNotReadableExceptionQueueTest test failed", e);
		}
	}

	/*
	 * @testName: xMessageNotWriteableExceptionQTestforTextMessage
	 * 
	 * @assertion_ids: JMS:SPEC:179; JMS:SPEC:70; JMS:JAVADOC:766;
	 * 
	 * @test_Strategy: When a client receives a text message it is in read-only
	 * mode. Send a message and have the client attempt to write to it.
	 */
	@Test
	public void xMessageNotWriteableExceptionQTestforTextMessage() throws Exception {
		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			byte bValue = 127;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createTextMessage();
			messageSent.setText("just a test");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xMessageNotWriteableExceptionQTestforTextMessage");
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (TextMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceived == null) {
				throw new Exception("getMessage returned null!! - test did not run!");
			} else {
				logger.log(Logger.Level.TRACE, "Got message - OK");
			}

			// Received message should be read-only - verify proper exception is
			// thrown
			try {
				messageReceived.setText("testing...");
			} catch (MessageNotWriteableException nr) {
				logger.log(Logger.Level.TRACE, "Passed.\n");
				// TestUtil.printStackTrace(nr);
				logger.log(Logger.Level.TRACE, "MessageNotWriteableException occurred!");
				logger.log(Logger.Level.TRACE, " " + nr.getMessage());
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("xMessageNotWriteableExceptionQTestforTextMessage");
		}
	}

	/*
	 * @testName: xMessageNotWriteableExceptionQTestforBytesMessage
	 * 
	 * @assertion_ids: JMS:SPEC:73; JMS:SPEC:179; JMS:SPEC:70; JMS:JAVADOC:702;
	 * 
	 * @test_Strategy: When a client receives a Bytes message it is in read-only
	 * mode. Send a message and have the client attempt to write to it.
	 */
	@Test
	public void xMessageNotWriteableExceptionQTestforBytesMessage() throws Exception {
		try {
			BytesMessage messageSent = null;
			BytesMessage messageReceived = null;
			byte bValue = 127;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createBytesMessage();
			messageSent.writeByte(bValue);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xMessageNotWriteableExceptionQTestforBytesMessage");
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (BytesMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceived == null) {
				throw new Exception("getMessage returned null!! - test did not run!");
			} else {
				logger.log(Logger.Level.TRACE, "Got message - OK");
			}

			// Received message should be read-only - verify proper exception is
			// thrown
			try {
				messageReceived.writeByte(bValue);
			} catch (MessageNotWriteableException nr) {
				logger.log(Logger.Level.TRACE, "Passed.\n");
				// TestUtil.printStackTrace(nr);
				logger.log(Logger.Level.TRACE, "MessageNotWriteableException occurred!");
				logger.log(Logger.Level.TRACE, " " + nr.getMessage());
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("xMessageNotWriteableExceptionQTestforBytesMessage", e);
		}
	}

	/*
	 * @testName: xMessageNotWriteableExceptionQTestforStreamMessage
	 * 
	 * @assertion_ids: JMS:SPEC:73; JMS:SPEC:179; JMS:JAVADOC:760;
	 * 
	 * @test_Strategy: When a client receives a Stream message it is in read-only
	 * mode. Send a message and have the client attempt to write to it.
	 */
	@Test
	public void xMessageNotWriteableExceptionQTestforStreamMessage() throws Exception {
		try {
			StreamMessage messageSent = null;
			StreamMessage messageReceived = null;
			byte bValue = 127;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createStreamMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xMessageNotWriteableExceptionQTestforStreamMessage");

			// Irene - what's this?
			// messageSent.writeByte(bValue);
			messageSent.writeString("Testing...");
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceived == null) {
				throw new Exception("getMessage returned null!! - test did not run!");
			} else {
				logger.log(Logger.Level.TRACE, "Got message - OK");
			}

			// Received message should be read-only - verify proper exception is
			// thrown
			try {
				messageReceived.writeString("Testing...");
			} catch (MessageNotWriteableException nr) {
				logger.log(Logger.Level.TRACE, "Passed.\n");
				// TestUtil.printStackTrace(nr);
				logger.log(Logger.Level.TRACE, "MessageNotWriteableException occurred!");
				logger.log(Logger.Level.TRACE, " " + nr.getMessage());
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("xMessageNotWriteableExceptionQTestforStreamMessage", e);
		}
	}

	/*
	 * @testName: xMessageNotWriteableExceptionQTestforMapMessage
	 * 
	 * @assertion_ids: JMS:SPEC:73; JMS:SPEC:179; JMS:JAVADOC:822;
	 * 
	 * @test_Strategy: When a client receives a Map message it is in read-only mode.
	 * Send a message and have the client attempt to write to it.
	 */
	@Test
	public void xMessageNotWriteableExceptionQTestforMapMessage() throws Exception {
		try {
			MapMessage messageSent = null;
			MapMessage messageReceived = null;
			byte bValue = 127;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createMapMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xMessageNotWriteableExceptionQTestforMapMessage");

			// messageSent.setByte("aByte",bValue);
			messageSent.setString("aString", "value");
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceived == null) {
				throw new Exception("getMessage returned null!! - test did not run!");
			} else {
				logger.log(Logger.Level.TRACE, "Got message - OK");
			}

			// Received message should be read-only - verify proper exception is
			// thrown
			try {

				// messageReceived.setByte("aByte",bValue);
				messageReceived.setString("aString", "value");
			} catch (MessageNotWriteableException nr) {
				logger.log(Logger.Level.TRACE, "Passed.\n");
				// TestUtil.printStackTrace(nr);
				logger.log(Logger.Level.TRACE, "MessageNotWriteableException occurred!");
				logger.log(Logger.Level.TRACE, " " + nr.getMessage());
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("xMessageNotWriteableExceptionQTestforMapMessage", e);
		}
	}

	/*
	 * @testName: xNullPointerExceptionQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:86.1;
	 * 
	 * @test_Strategy: Create a bytes message. Attempt to write null to it. Verify
	 * that a NullPointerException is thrown.
	 * 
	 */
	@Test
	public void xNullPointerExceptionQueueTest() throws Exception {
		try {
			BytesMessage msg = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			msg = tool.getDefaultQueueSession().createBytesMessage();
			msg.setStringProperty("COM_SUN_JMS_TESTNAME", "xNullPointerExceptionQueueTest");
			msg.writeBytes(null);
		} catch (java.lang.NullPointerException nullp) {
			logger.log(Logger.Level.TRACE, "Passed.\n");
			// TestUtil.printStackTrace(nullp);
			logger.log(Logger.Level.TRACE, "NullPointerException occurred!");
			logger.log(Logger.Level.TRACE, " " + nullp.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Logger.Level.TRACE, " " + e.getMessage());
			logger.log(Logger.Level.TRACE, "Expected NullPointerException did not occur!");
			throw new Exception("xNullPointerExceptionQueueTest");
		}
	}

	/*
	 * @testName: xMessageEOFExceptionQTestforBytesMessage
	 * 
	 * @assertion_ids: JMS:SPEC:176; JMS:JAVADOC:679;
	 * 
	 * @test_Strategy: Send a message to the queue with one byte. Retreive message
	 * from queue and read two bytes.
	 * 
	 * 
	 */
	@Test
	public void xMessageEOFExceptionQTestforBytesMessage() throws Exception {
		try {
			BytesMessage messageSent = null;
			BytesMessage messageReceived = null;
			byte bValue = 127;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createBytesMessage();
			messageSent.writeByte(bValue);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xMessageEOFExceptionQTestforBytesMessage");
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (BytesMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceived == null) {
				throw new Exception("getMessage returned null!! - test did not run!");
			} else {
				logger.log(Logger.Level.TRACE, "Got message - OK");
			}

			// Received message should contain one byte
			// reading 2 bytes should throw the excpected exception
			messageReceived.readByte();
			try {
				messageReceived.readByte();

				// Should not reach here !!
				logger.log(Logger.Level.TRACE, "Failed:  expected MessageEOFException not thrown");
				throw new Exception("Fail: Did not throw expected error!!!");
			} catch (MessageEOFException end) {
				logger.log(Logger.Level.TRACE, "Passed.\n");
				// TestUtil.printStackTrace(end);
				logger.log(Logger.Level.TRACE, "MessageEOFException occurred!");
				logger.log(Logger.Level.TRACE, " " + end.getMessage());
			}
		} catch (Exception e) {
			throw new Exception("xMessageEOFExceptionQTestforBytesMessage", e);
		}
	}

	/*
	 * @testName: xMessageEOFExceptionQTestforStreamMessage
	 * 
	 * @assertion_ids: JMS:SPEC:176; JMS:JAVADOC:722;
	 * 
	 * @test_Strategy: Send a message to the queue with one byte. Retreive message
	 * from queue and read two bytes.
	 * 
	 * 
	 */
	@Test
	public void xMessageEOFExceptionQTestforStreamMessage() throws Exception {
		try {
			StreamMessage messageSent = null;
			StreamMessage messageReceived = null;
			byte bValue = 127;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createStreamMessage();
			messageSent.writeByte(bValue);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xMessageEOFExceptionQTestforStreamMessage");
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceived == null) {
				throw new Exception("getMessage returned null!! - test did not run!");
			} else {
				logger.log(Logger.Level.TRACE, "Got message - OK");
			}

			// Received message should contain one byte
			// reading 2 bytes should throw the excpected exception
			messageReceived.readByte();
			try {
				messageReceived.readByte();

				// Should not reach here !!
				logger.log(Logger.Level.TRACE, "Failed:  expected MessageEOFException not thrown");
				throw new Exception("Fail: Did not throw expected error!!!");
			} catch (MessageEOFException end) {
				logger.log(Logger.Level.TRACE, "Passed.\n");
				// TestUtil.printStackTrace(end);
				logger.log(Logger.Level.TRACE, "MessageEOFException occurred!");
				logger.log(Logger.Level.TRACE, " " + end.getMessage());
			}
		} catch (Exception e) {
			throw new Exception("xMessageEOFExceptionQTestforStreamMessage", e);
		}
	}

	/*
	 * @testName: xMessageFormatExceptionQTestforBytesMessage
	 * 
	 * @assertion_ids: JMS:SPEC:177;
	 * 
	 * @test_Strategy: Call writeObject with a q session object
	 */
	@Test
	public void xMessageFormatExceptionQTestforBytesMessage() throws Exception {
		try {
			BytesMessage messageSent = null;
			BytesMessage messageReceived = null;
			byte bValue = 127;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createBytesMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xMessageFormatExceptionQTestforBytesMessage");
			logger.log(Logger.Level.TRACE, "try to write an invalid object");
			try {
				messageSent.writeObject(tool.getDefaultQueueSession());

				// Should not reach here !!
				logger.log(Logger.Level.TRACE, "Failed:  expected MessageEOFException not thrown");
				throw new Exception("Fail: Did not throw expected error!!!");
			} catch (MessageFormatException fe) {
				logger.log(Logger.Level.TRACE, "Passed.\n");
				// TestUtil.printStackTrace(fe);
				logger.log(Logger.Level.TRACE, "MessageFormatException occurred!");
				logger.log(Logger.Level.TRACE, " " + fe.getMessage());
			}
		} catch (Exception e) {
			throw new Exception("xMessageFormatExceptionQTestforBytesMessage", e);
		}
	}

	/*
	 * @testName: xMessageFormatExceptionQTestforStreamMessage
	 * 
	 * @assertion_ids: JMS:SPEC:177; JMS:JAVADOC:744;
	 * 
	 * @test_Strategy: Write a byte array, read it as a string.
	 */
	@Test
	public void xMessageFormatExceptionQTestforStreamMessage() throws Exception {
		try {
			StreamMessage messageSent = null;
			StreamMessage messageReceived = null;
			byte[] bValues = { 127, 0, 3 };

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createStreamMessage();
			messageSent.writeBytes(bValues);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xMessageFormatExceptionQTestforStreamMessage");
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceived == null) {
				throw new Exception("getMessage returned null!! - test did not run!");
			} else {
				logger.log(Logger.Level.TRACE, "Got message - OK");
			}

			// Received message should contain a byte, read a string
			try {
				messageReceived.readString();

				// Should not reach here !!
				logger.log(Logger.Level.TRACE, "Failed:  expected MessageFormatException not thrown");
				throw new Exception("Fail: Did not throw expected error!!!");
			} catch (MessageFormatException fe) {
				logger.log(Logger.Level.INFO, "Passed.\n");
				logger.log(Logger.Level.TRACE, "MessageFormatException occurred!");
				logger.log(Logger.Level.TRACE, " " + fe.getMessage());
			}
		} catch (Exception e) {
			throw new Exception("xMessageFormatExceptionQTestforStreamMessage", e);
		}
	}

	/*
	 * @testName: xInvalidSelectorExceptionQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:69; JMS:SPEC:175; JMS:JAVADOC:624; JMS:JAVADOC:620;
	 * 
	 * @test_Strategy: call createBrowser with an invalid selector string call
	 * createReceiver with an invalid selector string
	 */
	@Test
	public void xInvalidSelectorExceptionQueueTest() throws Exception {
		try {
			QueueBrowser qBrowser = null;
			QueueReceiver qReceiver = null;
			boolean pass = true;

			// close default QueueReceiver
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			logger.log(Logger.Level.TRACE, "** Close default QueueReceiver **");
			tool.getDefaultQueueReceiver().close();
			tool.getDefaultQueueConnection().start();

			// send message to Queue
			logger.log(Logger.Level.TRACE, "Send message to Queue. Text = \"message 1\"");
			TextMessage msg1 = tool.getDefaultQueueSession().createTextMessage();

			msg1.setText("message 1");
			msg1.setStringProperty("COM_SUN_JMS_TESTNAME", "xInvalidSelectorExceptionQueueTest");
			tool.getDefaultQueueSender().send(msg1);

			try {
				// create QueueBrowser
				logger.log(Logger.Level.TRACE, "call createBrowser with incorrect selector.");
				qBrowser = tool.getDefaultQueueSession().createBrowser(tool.getDefaultQueue(), "=TEST 'test'");
				logger.log(Logger.Level.ERROR,
						"Error --- createBrowser didn't throw expected InvalidSelectorException!");
				pass = false;
			} catch (InvalidSelectorException es) {
				logger.log(Logger.Level.TRACE, "createBrowser threw expected InvalidSelectorException!");
			} catch (Exception e) {
				pass = false;
				logger.log(Logger.Level.ERROR, "Error -- Incorrect Exception thrown by createBrowser.", e);
			} finally {
				if (qBrowser != null) {
					try {
						qBrowser.close();
					} catch (Exception ee) {
						logger.log(Logger.Level.ERROR, "Error -- failed to close qBrowser.", ee);
					}
				}
			}

			try {
				// create QueueReceiver
				logger.log(Logger.Level.TRACE, "call createReceiver with incorrect selector.");
				qReceiver = tool.getDefaultQueueSession().createReceiver(tool.getDefaultQueue(), "=TEST 'test'");
				if (qReceiver != null)
					logger.log(Logger.Level.TRACE, "qReceiver=" + qReceiver);
				logger.log(Logger.Level.ERROR,
						"Error --- createReceiver didn't throw expected InvalidSelectorException!");
				pass = false;
			} catch (InvalidSelectorException es) {
				logger.log(Logger.Level.INFO, "createReceiver threw expected InvalidSelectorException!");
			} catch (Exception e) {
				pass = false;
				logger.log(Logger.Level.ERROR, "Error -- Incorrect Exception thrown by createReceiver.", e);
			} finally {
				if (qReceiver != null) {
					try {
						qReceiver.close();
					} catch (Exception ee) {
						logger.log(Logger.Level.ERROR, "Error -- failed to close qReceiver.", ee);
					}
				}
			}

			if (pass != true)
				throw new Exception("xInvalidSelectorExceptionQueueTest failed!!");

		} catch (Exception e) {
			throw new Exception("xInvalidSelectorExceptionQueueTest: ", e);
		}

	}

	/*
	 * @testName: xIllegalStateExceptionQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:171; JMS:JAVADOC:634;
	 * 
	 * @test_Strategy: Call session.commit() when there is no transaction to be
	 * committed. Verify that the proper exception is thrown.
	 */
	@Test
	public void xIllegalStateExceptionQueueTest() throws Exception {
		boolean passed = false;

		try {

			// create Queue Connection
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.INFO, "Calling session.commit(), an illegal operation.");
			try {
				tool.getDefaultQueueSession().commit();
			} catch (jakarta.jms.IllegalStateException iStateE) {
				passed = true;
				logger.log(Logger.Level.INFO, "Received jakarta.jms.IllegalStateException -- GOOD");
				logger.log(Logger.Level.TRACE, "Exception message: " + iStateE.getMessage());
			}
			if (passed == false) { // need case for no exception being thrown
				throw new Exception("Did not receive IllegalStateException");
			}
		} catch (Exception e) { // handles case of other exception being thrown
			TestUtil.printStackTrace(e);
			throw new Exception("xIllegalStateExceptionQueueTest");
		}
	}

	/*
	 * @testName: xUnsupportedOperationExceptionQTest1
	 * 
	 * @assertion_ids: JMS:JAVADOC:668; JMS:JAVADOC:671;
	 * 
	 * @test_Strategy: Create a QueueSender with a null Queue. Verify that
	 * UnsupportedOperationException is thrown when send is called without a valid
	 * Queue.
	 */
	@Test
	public void xUnsupportedOperationExceptionQTest1() throws Exception {
		try {
			TextMessage messageSent = null;
			boolean pass = true;
			Queue myQueue = null;
			QueueSender qSender = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);

			// Close default QueueSender and create a new one with null Queue
			tool.getDefaultQueueSender().close();
			qSender = tool.getDefaultQueueSession().createSender(myQueue);

			tool.getDefaultQueueConnection().start();

			try {
				messageSent = tool.getDefaultQueueSession().createTextMessage();
				messageSent.setText("sending a Text message");
				messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xUnsupportedOperationExceptionQTest1");

				logger.log(Logger.Level.TRACE, "sending a Text message");
				qSender.send(messageSent);

				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: QueueSender.send(Message) didn't throw expected UnsupportedOperationException.");
			} catch (UnsupportedOperationException ex) {
				logger.log(Logger.Level.INFO,
						"Got expected UnsupportedOperationException from QueueSender.send(Message)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: QueueSender.send(Message) throw incorrect Exception: ", e);
				pass = false;
			}

			try {
				messageSent = tool.getDefaultQueueSession().createTextMessage();
				messageSent.setText("sending a Text message");
				messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xUnsupportedOperationExceptionQTest1");

				logger.log(Logger.Level.TRACE, "sending a Text message");
				qSender.send(messageSent, Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY,
						Message.DEFAULT_TIME_TO_LIVE);

				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: QueueSender.send(Message, int, int, long) didn't throw expected UnsupportedOperationException.");
			} catch (UnsupportedOperationException ex) {
				logger.log(Logger.Level.INFO,
						"Got expected UnsupportedOperationException from QueueSender.send(Message, int, int, long)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR,
						"Error: QueueSender.send(Message, int, int, long) throw incorrect Exception: ", e);
				pass = false;
			}

			if (pass != true)
				throw new Exception("xUnsupportedOperationExceptionQTest1 Failed!");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "xUnsupportedOperationExceptionQTest1 Failed!", e);
			throw new Exception("xUnsupportedOperationExceptionQTest1 Failed!", e);
		} finally {
			try {
				if (tool.getDefaultQueueConnection() != null) {
					tool.flushQueue();
					tool.getDefaultQueueConnection().close();
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Exception closing QueueConnection and cleanup", e);
			}
		}
	}

	/*
	 * @testName: xUnsupportedOperationExceptionQTest2
	 * 
	 * @assertion_ids: JMS:JAVADOC:599; JMS:JAVADOC:602;
	 *
	 * @test_Strategy: Create a MessageProducer with a null Destination. Verify that
	 * UnsupportedOperationException is thrown when send is called without a valid
	 * Destination.
	 */
	@Test
	public void xUnsupportedOperationExceptionQTest2() throws Exception {
		try {
			TextMessage messageSent = null;
			boolean pass = true;
			Destination myDest = null;
			MessageProducer mSender = null;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);

			// Close default MessageProducer and create a new one with null
			// Destination
			tool.getDefaultProducer().close();
			mSender = tool.getDefaultSession().createProducer(myDest);

			tool.getDefaultConnection().start();

			try {
				messageSent = tool.getDefaultSession().createTextMessage();
				messageSent.setText("sending a Text message");
				messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xUnsupportedOperationExceptionQTest2");

				logger.log(Logger.Level.TRACE, "sending a Text message");
				mSender.send(messageSent);

				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: MessageProducer.send(Message) didn't throw expected UnsupportedOperationException.");
			} catch (UnsupportedOperationException ex) {
				logger.log(Logger.Level.INFO,
						"Got expected UnsupportedOperationException from MessageProducer.send(Message)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: MessageProducer.send(Message) throw incorrect Exception: ", e);
				pass = false;
			}

			try {
				messageSent = tool.getDefaultSession().createTextMessage();
				messageSent.setText("sending a Text message");
				messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xUnsupportedOperationExceptionQTest2");

				logger.log(Logger.Level.TRACE, "sending a Text message");
				mSender.send(messageSent, Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY,
						Message.DEFAULT_TIME_TO_LIVE);

				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: MessageProducer.send(Message, int, int, long) didn't throw expected UnsupportedOperationException.");
			} catch (UnsupportedOperationException ex) {
				logger.log(Logger.Level.INFO,
						"Got expected UnsupportedOperationException from MessageProducer.send(Message, int, int, long)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR,
						"Error: MessageProducer.send(Message, int, int, long) throw incorrect Exception: ", e);
				pass = false;
			}

			if (pass != true)
				throw new Exception("xUnsupportedOperationExceptionQTest2 Failed!");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "xUnsupportedOperationExceptionQTest2 Failed!", e);
			throw new Exception("xUnsupportedOperationExceptionQTest2 Failed!", e);
		} finally {
			try {
				if (tool.getDefaultConnection() != null) {
					tool.flushDestination();
					tool.getDefaultConnection().close();
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Exception closing Connection and cleanup", e);
			}
		}
	}

	/*
	 * @testName: xUnsupportedOperationExceptionQTest3
	 * 
	 * @assertion_ids: JMS:JAVADOC:605;
	 *
	 * @test_Strategy: Create a MessageProducer with a valid Destination. Verify
	 * that UnsupportedOperationException is thrown when send is called with another
	 * valid Destination.
	 */
	@Test
	public void xUnsupportedOperationExceptionQTest3() throws Exception {
		try {
			TextMessage messageSent = null;
			boolean pass = true;
			Destination myDest = null;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			tool.getDefaultConnection().start();

			try {
				messageSent = tool.getDefaultSession().createTextMessage();
				messageSent.setText("sending a Text message");
				messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xUnsupportedOperationExceptionQTest3");

				logger.log(Logger.Level.TRACE, "get the second Destination");
				myDest = (Destination) tool.getQueueDestination("MY_QUEUE2");

				logger.log(Logger.Level.TRACE, "sending a Text message");
				tool.getDefaultProducer().send(myDest, messageSent);

				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: MessageProducer.send(Destination, Message) didn't throw expected UnsupportedOperationException.");
			} catch (UnsupportedOperationException ex) {
				logger.log(Logger.Level.INFO,
						"Got expected UnsupportedOperationException from MessageProducer.send(Destination, Message)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR,
						"Error: MessageProducer.send(Destination, Message) throw incorrect Exception: ", e);
				pass = false;
			}

			if (pass != true)
				throw new Exception("xUnsupportedOperationExceptionQTest3 Failed!");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "xUnsupportedOperationExceptionQTest3 Failed!", e);
			throw new Exception("xUnsupportedOperationExceptionQTest3 Failed!", e);
		} finally {
			try {
				if (tool.getDefaultConnection() != null) {
					tool.flushDestination();
					tool.getDefaultConnection().close();
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Exception closing Connection and cleanup", e);
			}
		}
	}

	/*
	 * @testName: xInvalidDestinationExceptionQTests
	 *
	 * @assertion_ids: JMS:JAVADOC:502; JMS:JAVADOC:504; JMS:JAVADOC:510;
	 * JMS:JAVADOC:638; JMS:JAVADOC:639; JMS:JAVADOC:641; JMS:JAVADOC:643;
	 * JMS:JAVADOC:644; JMS:JAVADOC:646; JMS:JAVADOC:647; JMS:JAVADOC:649;
	 *
	 * @test_Strategy: Create a Session with Queue Configuration, using a null
	 * Destination/Queue to verify InvalidDestinationException is thrown with
	 * various methods
	 */
	@Test
	public void xInvalidDestinationExceptionQTests() throws Exception {

		try {
			boolean pass = true;
			Destination dummy = null;
			Queue dummyQ = null;

			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			tool.getDefaultProducer().close();
			tool.getDefaultConsumer().close();

			try {
				tool.getDefaultSession().createConsumer(dummy);
				logger.log(Logger.Level.ERROR,
						"Error: createConsumer(null) didn't throw expected InvalidDestinationException");
				pass = false;
			} catch (InvalidDestinationException ex) {
				logger.log(Logger.Level.INFO, "Got expected InvalidDestinationException from createConsumer(null)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: createConsumer(null) throw incorrect Exception: ", e);
				pass = false;
			}

			try {
				tool.getDefaultSession().createConsumer(dummy, "TEST = 'test'");
				logger.log(Logger.Level.ERROR,
						"Error: createConsumer(null, String) didn't throw expected InvalidDestinationException");
				pass = false;
			} catch (InvalidDestinationException ex) {
				logger.log(Logger.Level.INFO,
						"Got expected InvalidDestinationException from createConsumer(null, String)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: createConsumer(null, String) throw incorrect Exception: ", e);
				pass = false;
			}

			try {
				tool.getDefaultSession().createConsumer(dummy, "TEST = 'test'", true);
				logger.log(Logger.Level.ERROR,
						"Error: createConsumer(null, String, boolean) didn't throw expected InvalidDestinationException");
				pass = false;
			} catch (InvalidDestinationException ex) {
				logger.log(Logger.Level.INFO,
						"Got expected InvalidDestinationException from createConsumer(null, String, true)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: createConsumer(null, String, true) throw incorrect Exception: ",
						e);
				pass = false;
			}

			try {
				tool.getDefaultSession().createBrowser(dummyQ);
				logger.log(Logger.Level.ERROR,
						"Error: createBrowser(null) didn't throw expected InvalidDestinationException");
				pass = false;
			} catch (InvalidDestinationException ex) {
				logger.log(Logger.Level.INFO, "Got expected InvalidDestinationException from createBrowser(null)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: createBrowser(null) throw incorrect Exception: ", e);
				pass = false;
			}

			try {
				tool.getDefaultSession().createBrowser(dummyQ, "TEST = 'test'");
				logger.log(Logger.Level.ERROR,
						"Error: createBrowser(null, String) didn't throw expected InvalidDestinationException");
				pass = false;
			} catch (InvalidDestinationException ex) {
				logger.log(Logger.Level.INFO,
						"Got expected InvalidDestinationException from createBrowser(null, String)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: createBrowser(null, String) throw incorrect Exception: ", e);
				pass = false;
			}

			try {
				tool.closeDefaultConnections();
			} catch (Exception ex) {
				logger.log(Logger.Level.ERROR, "Error closing the default Connection", ex);
			}

			if (pass != true)
				throw new Exception("xInvalidDestinationExceptionQTests");

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("xInvalidDestinationExceptionQTests");
		} finally {
			try {
				tool.closeDefaultConnections();
			} catch (Exception ex) {
				logger.log(Logger.Level.ERROR, "Error closing Connection", ex);
			}
		}
	}

	/*
	 * @testName: xMessageNotReadableExceptionQBytesMsgTest
	 *
	 * @assertion_ids: JMS:JAVADOC:676; JMS:JAVADOC:678; JMS:JAVADOC:682;
	 * JMS:JAVADOC:684; JMS:JAVADOC:686; JMS:JAVADOC:688; JMS:JAVADOC:690;
	 * JMS:JAVADOC:692; JMS:JAVADOC:694; JMS:JAVADOC:696; JMS:JAVADOC:698;
	 * JMS:JAVADOC:699; JMS:JAVADOC:700;
	 *
	 * @test_Strategy: Create a BytesMessage, call various read methods on it before
	 * sending. Verify that jakarta.jms.MessageNotReadableException is thrown.
	 */
	@Test
	public void xMessageNotReadableExceptionQBytesMsgTest() throws Exception {
		try {
			BytesMessage messageSent = null;
			boolean pass = true;
			boolean booleanValue = false;
			byte byteValue = 127;
			byte[] bytesValue = { 127, -127, 1, 0 };
			byte[] bytesValueRecvd = { 0, 0, 0, 0 };
			char charValue = 'Z';
			double doubleValue = 6.02e23;
			float floatValue = 6.02e23f;
			int intValue = 2147483647;
			long longValue = 9223372036854775807L;
			Integer nInteger = new Integer(-2147483648);
			short shortValue = -32768;
			String utfValue = "what";

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			tool.getDefaultProducer().close();
			tool.getDefaultConsumer().close();

			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultSession().createBytesMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xMessageNotReadableExceptionQBytesMsgTest");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.INFO, "Writing one of each primitive type to the message");

			// -----------------------------------------------------------------------------
			messageSent.writeBoolean(booleanValue);
			messageSent.writeByte(byteValue);
			messageSent.writeChar(charValue);
			messageSent.writeDouble(doubleValue);
			messageSent.writeFloat(floatValue);
			messageSent.writeInt(intValue);
			messageSent.writeLong(longValue);
			messageSent.writeObject(nInteger);
			messageSent.writeShort(shortValue);
			messageSent.writeUTF(utfValue);
			messageSent.writeBytes(bytesValue);
			messageSent.writeBytes(bytesValue, 0, 1);

			try {
				messageSent.getBodyLength();
				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: getBodyLength didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "getBodyLength threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "getBodyLength threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readBoolean();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readBoolean didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readBoolean threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readBoolean threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readByte();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readByte didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readByte threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readByte threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readUnsignedByte();
				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: readUnsignedByte didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readUnsignedByte threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readUnsignedByte threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readShort();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readShort didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readShort threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readShort threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readUnsignedShort();
				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: readUnsignedShort didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readUnsignedShort threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readUnsignedShort threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readChar();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readChar didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readChar threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readChar threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readInt();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readInt didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readInt threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readInt threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readLong();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readLong didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readLong threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readLong threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readFloat();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readFloat didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readFloat threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readFloat threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readDouble();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readDouble didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readDouble threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readDouble threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readUTF();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readUTF didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readUTF threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readUTF threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readBytes(bytesValueRecvd);
				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: readBytes(byte[]) didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readBytes(byte[]) threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readBytes(byte[]) threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readBytes(bytesValueRecvd, 1);
				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: readBytes(byte[], int) didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readBytes(byte[], int) threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readBytes(byte[],int) threw Wrong Exception!", e);
				pass = false;
			}

			if (pass != true)
				throw new Exception("xMessageNotReadableExceptionQBytesMsgTest Failed!");

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("xMessageNotReadableExceptionQBytesMsgTest:");
		} finally {
			try {
				tool.closeDefaultConnections();
			} catch (Exception ex) {
				logger.log(Logger.Level.ERROR, "Error closing Connection", ex);
			}
		}
	}

	/*
	 * @testName: xMessageNotReadableExceptionQStreamMsgTest
	 *
	 * @assertion_ids: JMS:SPEC:73.1; JMS:JAVADOC:431; JMS:JAVADOC:721;
	 * JMS:JAVADOC:724; JMS:JAVADOC:727; JMS:JAVADOC:730; JMS:JAVADOC:733;
	 * JMS:JAVADOC:736; JMS:JAVADOC:739; JMS:JAVADOC:742; JMS:JAVADOC:745;
	 * JMS:JAVADOC:748; JMS:JAVADOC:751;
	 *
	 * @test_Strategy: Create a StreamMessage, send and receive via a Topic; Call
	 * clearBoldy right after receiving the message; Call various read methods on
	 * received message; Verify jakarta.jms.MessageNotReadableException is thrown.
	 */
	@Test
	public void xMessageNotReadableExceptionQStreamMsgTest() throws Exception {
		try {
			StreamMessage messageSent = null;
			StreamMessage messageReceived = null;
			boolean pass = true;
			byte bValue = 127;
			boolean abool = false;
			byte[] bValues = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
			byte[] bValues2 = { 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
			byte[] bValuesReturned = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			char charValue = 'Z';
			short sValue = 32767;
			long lValue = 9223372036854775807L;
			double dValue = 6.02e23;
			float fValue = 6.02e23f;
			int iValue = 6;
			String myString = "text";
			String sTesting = "Testing StreamMessages";

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			tool.getDefaultConnection().start();

			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultSession().createStreamMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xMessageNotReadableExceptionQStreamMsgTest");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "");
			logger.log(Logger.Level.INFO, "Writing one of each primitive type to the message");
			// -----------------------------------------------------------------------------
			messageSent.writeBytes(bValues2, 0, bValues.length);
			messageSent.writeBoolean(abool);
			messageSent.writeByte(bValue);
			messageSent.writeBytes(bValues);
			messageSent.writeChar(charValue);
			messageSent.writeDouble(dValue);
			messageSent.writeFloat(fValue);
			messageSent.writeInt(iValue);
			messageSent.writeLong(lValue);
			messageSent.writeObject(sTesting);
			messageSent.writeShort(sValue);
			messageSent.writeString(myString);
			messageSent.writeObject(null);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultProducer().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (StreamMessage) tool.getDefaultConsumer().receive(timeout);

			logger.log(Logger.Level.TRACE, "call ClearBody()");
			messageReceived.clearBody();

			try {
				messageReceived.readBoolean();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readBoolean didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readBoolean threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readBoolean threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageReceived.readByte();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readByte didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readByte threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readByte threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageReceived.readShort();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readShort didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readShort threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readShort threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageReceived.readChar();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readChar didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readChar threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readChar threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageReceived.readInt();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readInt didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readInt threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readInt threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageReceived.readLong();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readLong didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readLong threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readLong threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageReceived.readFloat();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readFloat didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readFloat threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readFloat threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageReceived.readDouble();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readDouble didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readDouble threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readDouble threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageReceived.readString();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readString didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readString threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readString threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageReceived.readBytes(bValuesReturned);
				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: readBytes(byte[]) didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readBytes(byte[]) threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readBytes(byte[]) threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageReceived.readObject();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readObject didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readObject threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readObject threw Wrong Exception!", e);
				pass = false;
			}

			if (pass != true)
				throw new Exception("xMessageNotReadableExceptionQStreamMsgTest Failed!");

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("xMessageNotReadableExceptionQStreamMsgTest:");
		} finally {
			try {
				tool.closeDefaultConnections();
			} catch (Exception ex) {
				logger.log(Logger.Level.ERROR, "Error closing Connection", ex);
			}
		}
	}

	/*
	 * @testName: xIllegalStateExceptionTestTopicMethodsQ
	 * 
	 * @assertion_ids: JMS:SPEC:185.2; JMS:SPEC:185.3; JMS:SPEC:185.4;
	 * JMS:SPEC:185.5; JMS:SPEC:185;
	 *
	 * @test_Strategy: Create a QueueSession and call Topic specific methods
	 * inherited from Session, and verify that jakarta.jms.IllegalStateException is
	 * thrown.
	 */
	@Test
	public void xIllegalStateExceptionTestTopicMethodsQ() throws Exception {

		try {
			boolean pass = true;
			Topic myTopic = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			myTopic = tool.createNewTopic("MY_TOPIC");

			try {
				tool.getDefaultQueueSession().createDurableSubscriber(myTopic, "cts");
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: QueueSession.createDurableSubscriber(Topic, String) "
						+ "didn't throw expected IllegalStateException.");
			} catch (jakarta.jms.IllegalStateException ex) {
				logger.log(Logger.Level.INFO,
						"Got expected IllegalStateException from QueueSession.createDurableSubscriber(Topic, String)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR,
						"Error: QueueSession.createDurableSubscriber(Topic, String) throw incorrect Exception: ", e);
				pass = false;
			}

			try {
				tool.getDefaultQueueSession().createDurableSubscriber(myTopic, "cts", "TEST = 'test'", false);
				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: QueueSession.createDurableSubscriber(Topic, String, String, boolean) "
								+ "didn't throw expected IllegalStateException.");
			} catch (jakarta.jms.IllegalStateException ex) {
				logger.log(Logger.Level.INFO, "Got expected IllegalStateException from "
						+ "QueueSession.createDurableSubscriber(Topic, String, String, boolean)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR,
						"Error: QueueSession.createDurableSubscriber(Topic, String, String, boolean) "
								+ "throw incorrect Exception: ",
						e);
				pass = false;
			}

			try {
				tool.getDefaultQueueSession().createTemporaryTopic();
				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: QueueSession.createTemporayTopic() didn't throw expected IllegalStateException.");
			} catch (jakarta.jms.IllegalStateException ex) {
				logger.log(Logger.Level.INFO,
						"Got expected IllegalStateException from QueueSession.createTemporayTopic()");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: QueueSession.createTemporayTopic() throw incorrect Exception: ",
						e);
				pass = false;
			}

			try {
				tool.getDefaultQueueSession().createTopic("foo");
				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: QueueSession.createTopic(String) didn't throw expected IllegalStateException.");
			} catch (jakarta.jms.IllegalStateException ex) {
				logger.log(Logger.Level.INFO,
						"Got expected IllegalStateException from QueueSession.createTopic(String)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: QueueSession.createTopic(String) throw incorrect Exception: ",
						e);
				pass = false;
			}

			try {
				tool.getDefaultQueueSession().unsubscribe("foo");
				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: QueueSession.unsubscribe(String) didn't throw expected IllegalStateException.");
			} catch (jakarta.jms.IllegalStateException ex) {
				logger.log(Logger.Level.INFO,
						"Got expected IllegalStateException from QueueSession.unsubscribe(String)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: QueueSession.unsubscribe(String) throw incorrect Exception: ",
						e);
				pass = false;
			}

			if (pass != true)
				throw new Exception("xIllegalStateExceptionTestTopicMethodsQ Failed!");

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("xIllegalStateExceptionTestTopicMethodsQ");
		} finally {
			try {
				tool.closeDefaultConnections();
			} catch (Exception ex) {
				logger.log(Logger.Level.ERROR, "Error closing Connection", ex);
			}
		}
	}

	/*
	 * @testName: xIllegalStateExceptionTestRollbackQ
	 *
	 * @assertion_ids: JMS:JAVADOC:502; JMS:JAVADOC:504; JMS:JAVADOC:510;
	 * JMS:JAVADOC:242; JMS:JAVADOC:635; JMS:JAVADOC:317;
	 *
	 * @test_Strategy: 1. Create a TextMessages, send use a MessageProducer 2. Then
	 * rollback on the non-transacted session Verify that IllegalStateException is
	 * thrown
	 */
	@Test
	public void xIllegalStateExceptionTestRollbackQ() throws Exception {

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			boolean pass = true;

			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			tool.getDefaultConnection().start();

			messageSent = tool.getDefaultSession().createTextMessage();
			messageSent.setText("just a test");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xIllegalStateExceptionTestRollbackQ");

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message to a Queue");
			tool.getDefaultProducer().send(messageSent);

			try {
				logger.log(Logger.Level.TRACE,
						"Rolling back a non-transacted session must throw IllegalStateException");
				tool.getDefaultSession().rollback();
				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: QueueSession.rollback() didn't throw expected IllegalStateException");
			} catch (jakarta.jms.IllegalStateException en) {
				logger.log(Logger.Level.INFO, "Got expected IllegalStateException from QueueSession.rollback()");
			}

			if (pass != true)
				throw new Exception("xIllegalStateExceptionTestRollbackQ");

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("xIllegalStateExceptionTestRollbackQ");
		} finally {
			try {
				tool.closeDefaultConnections();
				tool.flushDestination();
			} catch (Exception ex) {
				logger.log(Logger.Level.ERROR, "Error closing Connection", ex);
			}
		}
	}
}
