/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.throwingcoder.annotated;

import java.nio.ByteBuffer;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.stringbean.StringBean;
import com.sun.ts.tests.websocket.ee.jakarta.websocket.throwingcoder.ThrowingTextDecoder;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 */
public class WSClient extends WebSocketCommonClient {
  private static final long serialVersionUID = -8094860820359975543L;

  protected static final String ECHO = "Echo message";

  public WSClient() {
    setContextRoot("ee_java_websocket_throwingcoder_annotated_web");
    logExceptionOnInvocation(false);
  }

  public static void main(String[] args) {
    new WSClient().run(args);
  }

  /* Run test */
  /*
   * @testName: binaryDecoderThrowAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:38;
   * 
   * @test_Strategy: test the binary decoder throws decoderException and it is
   * caught in @OnError
   */
  public void binaryDecoderThrowAndCatchOnServerTest() throws Exception {
    invoke("binarydecoder", ByteBuffer.wrap(ECHO.getBytes()),
        ThrowingTextDecoder.ERR_MSG);
    logMsg("The DecoderException has been propagated to @OnError");
  }

  /*
   * @testName: binaryDecoderThrowAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:38;
   * 
   * @test_Strategy: test the binary decoder throws decoderException and it is
   * caught in @OnError
   */
  public void binaryDecoderThrowAndCatchOnClientTest() throws Exception {
    WSCClientEndpointWithBinaryDecoder endpoint = new WSCClientEndpointWithBinaryDecoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeOnClient("simplebin", ECHO);
    logMsg("The DecoderException has been propagated to @OnError");
  }

  /*
   * @testName: binaryStreamDecoderThrowAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:41;
   * 
   * @test_Strategy: test the binary stream decoder throws decoderException and
   * it is caught in @OnError
   */
  public void binaryStreamDecoderThrowAndCatchOnServerTest() throws Exception {
    invoke("binarystreamdecoder", ByteBuffer.wrap(ECHO.getBytes()),
        ThrowingTextDecoder.ERR_MSG);
    logMsg("The DecoderException has been propagated to @OnError");
  }

  /*
   * @testName: binaryStreamDecoderThrowAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:41;
   * 
   * @test_Strategy: test the binary stream decoder throws decoderException and
   * it is caught in @OnError
   */
  public void binaryStreamDecoderThrowAndCatchOnClientTest() throws Exception {
    WSCClientEndpointWithBinaryStreamDecoder endpoint = new WSCClientEndpointWithBinaryStreamDecoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeOnClient("simplebin", ECHO);
    logMsg("The DecoderException has been propagated to @OnError");
  }

  /*
   * @testName: textDecoderThrowAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:44;
   * 
   * @test_Strategy: test the text decoder throws decoderException and it is
   * caught in @OnError
   */
  public void textDecoderThrowAndCatchOnServerTest() throws Exception {
    invoke("textdecoder", ECHO, ThrowingTextDecoder.ERR_MSG);
    logMsg("The DecoderException has been propagated to @OnError");
  }

  /*
   * @testName: textDecoderThrowAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:44;
   * 
   * @test_Strategy: test the text decoder throws decoderException and it is
   * caught in @OnError
   */
  public void textDecoderThrowAndCatchOnClientTest() throws Exception {
    WSCClientEndpointWithTextDecoder endpoint = new WSCClientEndpointWithTextDecoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeOnClient("simpleecho", ECHO);
    logMsg("The DecoderException has been propagated to @OnError");
  }

  /*
   * @testName: textStreamDecoderThrowAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:47;
   * 
   * @test_Strategy: test the text stream decoder throws decoderException and it
   * is caught in @OnError
   */
  public void textStreamDecoderThrowAndCatchOnServerTest() throws Exception {
    invoke("textstreamdecoder", ECHO, ThrowingTextDecoder.ERR_MSG);
    logMsg("The DecoderException has been propagated to @OnError");
  }

  /*
   * @testName: textStreamDecoderThrowAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:47;
   * 
   * @test_Strategy: test the text stream decoder throws decoderException and it
   * is caught in @OnError
   */
  public void textStreamDecoderThrowAndCatchOnClientTest() throws Exception {
    WSCClientEndpointWithTextStreamDecoder endpoint = new WSCClientEndpointWithTextStreamDecoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeOnClient("simpleecho", ECHO);
    logMsg("The DecoderException has been propagated to @OnError");
  }

  // ---------------------------------------------------------------------

  /*
   * @testName: binaryEncoderThrowAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:57;
   * 
   * @test_Strategy: test the binary encoder throws EncoderException and it is
   * caught in @OnError
   */
  public void binaryEncoderThrowAndCatchOnServerTest() throws Exception {
    invoke("binaryencoder", ECHO, ThrowingTextDecoder.ERR_MSG);
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: binaryEncoderThrowAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:57;
   * 
   * @test_Strategy: test the binary encoder throws EncoderException and it is
   * caught in @OnError
   */
  public void binaryEncoderThrowAndCatchOnClientTest() throws Exception {
    WSCClientEndpointWithBinaryEncoder endpoint = new WSCClientEndpointWithBinaryEncoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeOnClient("simpleecho", new StringBean(ECHO));
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: binaryStreamEncoderThrowAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:59;
   * 
   * @test_Strategy: test the binary stream encoder throws EncoderException and
   * it is caught in @OnError
   */
  public void binaryStreamEncoderThrowAndCatchOnServerTest() throws Exception {
    invoke("binarystreamencoder", ECHO, ThrowingTextDecoder.ERR_MSG);
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: binaryStreamEncoderThrowAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:59;
   * 
   * @test_Strategy: test the binary stream encoder throws EncoderException and
   * it is caught in @OnError
   */
  public void binaryStreamEncoderThrowAndCatchOnClientTest() throws Exception {
    WSCClientEndpointWithBinaryStreamEncoder endpoint = new WSCClientEndpointWithBinaryStreamEncoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeOnClient("simpleecho", new StringBean(ECHO));
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: textEncoderThrowAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:62;
   * 
   * @test_Strategy: test the text encoder throws EncoderException and it is
   * caught in @OnError
   */
  public void textEncoderThrowAndCatchOnServerTest() throws Exception {
    invoke("textencoder", ECHO, ThrowingTextDecoder.ERR_MSG);
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: textEncoderThrowAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:62;
   * 
   * @test_Strategy: test the text encoder throws EncoderException and it is
   * caught in @OnError
   */
  public void textEncoderThrowAndCatchOnClientTest() throws Exception {
    WSCClientEndpointWithTextEncoder endpoint = new WSCClientEndpointWithTextEncoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeOnClient("simpleecho", new StringBean(ECHO));
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: textStreamEncoderThrowAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:64;
   * 
   * @test_Strategy: test the text stream encoder throws EncoderException and it
   * is caught in @OnError
   */
  public void textStreamEncoderThrowAndCatchOnServerTest() throws Exception {
    invoke("textstreamencoder", ECHO, ThrowingTextDecoder.ERR_MSG);
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: textStreamEncoderThrowAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:64;
   * 
   * @test_Strategy: test the text stream encoder throws EncoderException and it
   * is caught in @OnError
   */
  public void textStreamEncoderThrowAndCatchOnClientTest() throws Exception {
    WSCClientEndpointWithTextStreamEncoder endpoint = new WSCClientEndpointWithTextStreamEncoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeOnClient("simpleecho", new StringBean(ECHO));
    logMsg("The EncoderException has been propagated to @OnError");
  }

  // -----------------------------------------------------------------------
  // returning encoders

  /*
   * @testName: returningBinaryEncoderThrowAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:57;
   * 
   * @test_Strategy: test the binary encoder throws EncoderException and it is
   * caught in @OnError
   */
  public void returningBinaryEncoderThrowAndCatchOnServerTest() throws Exception {
    invoke("returningbinaryencoder", ECHO, ThrowingTextDecoder.ERR_MSG);
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: returningBinaryEncoderThrowAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:57;
   * 
   * @test_Strategy: test the binary encoder throws EncoderException and it is
   * caught in @OnError
   */
  public void returningBinaryEncoderThrowAndCatchOnClientTest() throws Exception {
    WSCReturningClientEndpointWithBinaryEncoder endpoint = new WSCReturningClientEndpointWithBinaryEncoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeOnClient("simpleecho", ECHO);
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: returningBinaryStreamEncoderThrowAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:59;
   * 
   * @test_Strategy: test the binary stream encoder throws EncoderException and
   * it is caught in @OnError
   */
  public void returningBinaryStreamEncoderThrowAndCatchOnServerTest()
      throws Exception {
    invoke("returningbinarystreamencoder", ECHO, ThrowingTextDecoder.ERR_MSG);
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: returningBinaryStreamEncoderThrowAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:59;
   * 
   * @test_Strategy: test the binary stream encoder throws EncoderException and
   * it is caught in @OnError
   */
  public void returningBinaryStreamEncoderThrowAndCatchOnClientTest()
      throws Exception {
    WSCReturningClientEndpointWithBinaryStreamEncoder endpoint = new WSCReturningClientEndpointWithBinaryStreamEncoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeOnClient("simpleecho", ECHO);
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: returningTextEncoderThrowAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:62;
   * 
   * @test_Strategy: test the text encoder throws EncoderException and it is
   * caught in @OnError
   */
  public void returningTextEncoderThrowAndCatchOnServerTest() throws Exception {
    invoke("returningtextencoder", ECHO, ThrowingTextDecoder.ERR_MSG);
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: returningTextEncoderThrowAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:62;
   * 
   * @test_Strategy: test the text encoder throws EncoderException and it is
   * caught in @OnError
   */
  public void returningTextEncoderThrowAndCatchOnClientTest() throws Exception {
    WSCReturningClientEndpointWithTextEncoder endpoint = new WSCReturningClientEndpointWithTextEncoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeOnClient("simpleecho", ECHO);
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: returningTextStreamEncoderThrowAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:64;
   * 
   * @test_Strategy: test the text stream encoder throws EncoderException and it
   * is caught in @OnError
   */
  public void returningTextStreamEncoderThrowAndCatchOnServerTest()
      throws Exception {
    invoke("returningtextstreamencoder", ECHO, ThrowingTextDecoder.ERR_MSG);
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: returningTextStreamEncoderThrowAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:64;
   * 
   * @test_Strategy: test the text stream encoder throws EncoderException and it
   * is caught in @OnError
   */
  public void returningTextStreamEncoderThrowAndCatchOnClientTest()
      throws Exception {
    WSCReturningClientEndpointWithTextStreamEncoder endpoint = new WSCReturningClientEndpointWithTextStreamEncoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeOnClient("simpleecho", ECHO);
    logMsg("The EncoderException has been propagated to @OnError");
  }

  // ------------------------------------------------------------------------
  // IO Exceptions

  /*
   * @testName: binaryStreamDecoderThrowIOAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:42;
   * 
   * @test_Strategy: test the binary stream decoder throws IOException and it is
   * caught in @OnError
   */
  public void binaryStreamDecoderThrowIOAndCatchOnServerTest() throws Exception {
    invoke("iobinarystreamdecoder", ByteBuffer.wrap(ECHO.getBytes()),
        ThrowingTextDecoder.IO_ERR_MSG);
    logMsg("The IOException has been propagated to @OnError");
  }

  /*
   * @testName: binaryStreamDecoderThrowIOAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:42;
   * 
   * @test_Strategy: test the binary stream decoder throws IOException and it is
   * caught in @OnError
   */
  public void binaryStreamDecoderThrowIOAndCatchOnClientTest() throws Exception {
    WSCClientEndpointWithIOBinaryStreamDecoder endpoint = new WSCClientEndpointWithIOBinaryStreamDecoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeIOOnClient("simplebin", ECHO);
    logMsg("The IOException has been propagated to @OnError");
  }

  /*
   * @testName: textStreamDecoderThrowIOAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:48;
   * 
   * @test_Strategy: test the text stream decoder throws IOException and it is
   * caught in @OnError
   */
  public void textStreamDecoderThrowIOAndCatchOnServerTest() throws Exception {
    invoke("iotextstreamdecoder", ECHO, ThrowingTextDecoder.IO_ERR_MSG);
    logMsg("The IOException has been propagated to @OnError");
  }

  /*
   * @testName: textStreamDecoderThrowIOAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:48;
   * 
   * @test_Strategy: test the text stream decoder throws IOException and it is
   * caught in @OnError
   */
  public void textStreamDecoderThrowIOAndCatchOnClientTest() throws Exception {
    WSCClientEndpointWithIOTextStreamDecoder endpoint = new WSCClientEndpointWithIOTextStreamDecoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeIOOnClient("simpleecho", ECHO);
    logMsg("The IOException has been propagated to @OnError");
  }

  /*
   * @testName: binaryStreamEncoderThrowIOAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:60;
   * 
   * @test_Strategy: test the binary stream encoder throws IOException and it is
   * caught in @OnError
   */
  public void binaryStreamEncoderThrowIOAndCatchOnServerTest() throws Exception {
    invoke("iobinarystreamencoder", ECHO, ThrowingTextDecoder.IO_ERR_MSG);
    logMsg("The IOException has been propagated to @OnError");
  }

  /*
   * @testName: binaryStreamEncoderThrowIOAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:60;
   * 
   * @test_Strategy: test the binary stream encoder throws IOException and it is
   * caught in @OnError
   */
  public void binaryStreamEncoderThrowIOAndCatchOnClientTest() throws Exception {
    WSCClientEndpointWithIOBinaryStreamEncoder endpoint = new WSCClientEndpointWithIOBinaryStreamEncoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeIOOnClient("simpleecho", new StringBean(ECHO));
    logMsg("The IOException has been propagated to @OnError");
  }

  /*
   * @testName: textStreamEncoderThrowIOAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:65;
   * 
   * @test_Strategy: test the text stream encoder throws IOException and it is
   * caught in @OnError
   */
  public void textStreamEncoderThrowIOAndCatchOnServerTest() throws Exception {
    invoke("iotextstreamencoder", ECHO, ThrowingTextDecoder.IO_ERR_MSG);
    logMsg("The IOException has been propagated to @OnError");
  }

  /*
   * @testName: textStreamEncoderThrowIOAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:65;
   * 
   * @test_Strategy: test the text stream encoder throws IOException and it is
   * caught in @OnError
   */
  public void textStreamEncoderThrowIOAndCatchOnClientTest() throws Exception {
    WSCClientEndpointWithIOTextStreamEncoder endpoint = new WSCClientEndpointWithIOTextStreamEncoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeIOOnClient("simpleecho", new StringBean(ECHO));
    logMsg("The IOException has been propagated to @OnError");
  }

  /*
   * @testName: returningBinaryStreamEncoderThrowIOAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:60;
   * 
   * @test_Strategy: test the binary stream encoder throws IOException and it is
   * caught in @OnError
   */
  public void returningBinaryStreamEncoderThrowIOAndCatchOnServerTest()
      throws Exception {
    invoke("ioreturningbinarystreamencoder", ECHO,
        ThrowingTextDecoder.IO_ERR_MSG);
    logMsg("The IOException has been propagated to @OnError");
  }

  /*
   * @testName: returningBinaryStreamEncoderThrowIOAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:60;
   * 
   * @test_Strategy: test the binary stream encoder throws IOException and it is
   * caught in @OnError
   */
  public void returningBinaryStreamEncoderThrowIOAndCatchOnClientTest()
      throws Exception {
    WSCReturningClientEndpointWithIOBinaryStreamEncoder endpoint = new WSCReturningClientEndpointWithIOBinaryStreamEncoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeIOOnClient("simpleecho", ECHO);
    logMsg("The IOException has been propagated to @OnError");
  }

  /*
   * @testName: returningTextStreamEncoderThrowIOAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:65;
   * 
   * @test_Strategy: test the text stream encoder throws IOException and it is
   * caught in @OnError
   */
  public void returningTextStreamEncoderThrowIOAndCatchOnServerTest()
      throws Exception {
    invoke("ioreturningtextstreamencoder", ECHO,
        ThrowingTextDecoder.IO_ERR_MSG);
    logMsg("The IOException has been propagated to @OnError");
  }

  /*
   * @testName: returningTextStreamEncoderThrowIOAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:65;
   * 
   * @test_Strategy: test the text stream encoder throws IOException and it is
   * caught in @OnError
   */
  public void returningTextStreamEncoderThrowIOAndCatchOnClientTest()
      throws Exception {
    WSCReturningClientEndpointWithIOTextStreamEncoder endpoint = new WSCReturningClientEndpointWithIOTextStreamEncoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeIOOnClient("simpleecho", ECHO);
    logMsg("The IOException has been propagated to @OnError");
  }

  // ///////////////////////////////////////////////////////////////////////
  private void invokeOnClient(String endpoint, Object entity) throws Exception {
    setCountDownLatchCount(2);
    invoke(endpoint, entity, ThrowingTextDecoder.ERR_MSG,
        ThrowingTextDecoder.ERR_MSG);
  }

  private void invokeIOOnClient(String endpoint, Object entity) throws Exception {
    setCountDownLatchCount(2);
    invoke(endpoint, entity, ThrowingTextDecoder.IO_ERR_MSG,
        ThrowingTextDecoder.IO_ERR_MSG);
  }
}
