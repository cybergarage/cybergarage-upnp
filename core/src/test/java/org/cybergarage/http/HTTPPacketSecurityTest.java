package org.cybergarage.http;

import static org.junit.Assert.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import org.junit.Test;

public class HTTPPacketSecurityTest {
  private HTTPPacket packet = new HTTPPacket();

  private boolean parse(String message) {
    return packet.set(new ByteArrayInputStream(message.getBytes(StandardCharsets.US_ASCII)));
  }

  private String chunked(String body) {
    return "POST / HTTP/1.1\r\nTransfer-Encoding: chunked\r\n\r\n" + body;
  }

  @Test(timeout = 1000)
  public void rejectsEveryTruncatedChunkBoundary() {
    String valid = "2\r\nab\r\n1\r\nc\r\n0\r\n\r\n";
    for (int n = 0; n < valid.length(); n++)
      assertFalse("prefix " + n, parse(chunked(valid.substring(0, n))));
    assertTrue(parse(chunked(valid)));
    assertEquals("abc", packet.getContentString());
  }

  @Test
  public void supportsExtensionsTrailersAndContinue() {
    assertTrue(parse(chunked("2;x=y\r\nab\r\n1\r\nc\r\n0\r\nX-End: yes\r\n\r\n")));
    assertEquals("abc", packet.getContentString());
    assertTrue(
        parse("HTTP/1.1 100 Continue\r\n\r\nHTTP/1.1 200 OK\r\nContent-Length: 2\r\n\r\nOK"));
    assertEquals("OK", packet.getContentString());
  }

  @Test
  public void rejectsInvalidFraming() {
    for (String size : new String[] {"-1", "+1", "xyz", "ffffffffffffffff"})
      assertFalse(parse(chunked(size + "\r\n")));
    for (String length : new String[] {"-1", "+1", "9223372036854775808", "8388609"})
      assertFalse(parse("POST / HTTP/1.1\r\nContent-Length: " + length + "\r\n\r\n"));
    assertFalse(parse("POST / HTTP/1.1\r\nContent-Length: 1\r\nContent-Length: 1\r\n\r\nx"));
    assertFalse(
        parse(
            "POST / HTTP/1.1\r\nContent-Length: 1\r\nTransfer-Encoding: chunked\r\n\r\n0\r\n\r\n"));
    assertFalse(parse(chunked("1\r\naXX0\r\n\r\n")));
  }

  @Test
  public void enforcesLineHeaderAndAggregateBodyLimits() {
    assertFalse(parse("GET /" + "x".repeat(HTTPPacket.MAX_LINE_BYTES) + " HTTP/1.1\r\n\r\n"));
    assertFalse(parse("GET / HTTP/1.1\r\n" + "X: y\r\n".repeat(101) + "\r\n"));
    assertFalse(
        parse("GET / HTTP/1.1\r\n" + ("X: " + "x".repeat(8000) + "\r\n").repeat(9) + "\r\n"));
    String body = "x".repeat(HTTPPacket.MAX_CONTENT_BYTES);
    assertTrue(parse("POST / HTTP/1.1\r\nContent-Length: " + body.length() + "\r\n\r\n" + body));
    assertEquals(body.length(), packet.getContent().length);
    assertFalse(
        parse(
            chunked(
                Integer.toHexString(body.length()) + "\r\n" + body + "\r\n1\r\ny\r\n0\r\n\r\n")));
  }

  @Test
  public void permitsBoundedCloseDelimitedResponses() {
    HTTPResponse response = new HTTPResponse();
    assertTrue(
        response.set(
            new ByteArrayInputStream(
                "HTTP/1.0 200 OK\r\n\r\nXML".getBytes(StandardCharsets.US_ASCII))));
    assertEquals("XML", response.getContentString());
  }

  @Test
  public void headResponseDoesNotReadBody() {
    assertTrue(
        packet.set(
            new ByteArrayInputStream(
                "HTTP/1.1 200 OK\r\nContent-Length: 999999999\r\n\r\n"
                    .getBytes(StandardCharsets.US_ASCII)),
            true));
    assertEquals(0, packet.getContent().length);
  }

  @Test
  public void interruptedInputFailsClosed() {
    assertFalse(
        packet.set(
            new InputStream() {
              public int read() throws IOException {
                throw new InterruptedIOException();
              }
            }));
  }
}
