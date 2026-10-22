package org.cybergarage.xml;

import static org.junit.Assert.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import org.cybergarage.xml.parser.JaxpParser;
import org.cybergarage.util.LimitedInputStream;
import org.junit.Test;

public class XMLSecurityTest {
  @Test
  public void rejectsExternalAndInternalEntities() throws Exception {
    Path secret = Files.createTempFile("upnp-xxe", ".txt");
    try {
      Files.write(secret, "PRIVATE".getBytes(StandardCharsets.UTF_8));
      for (String declaration :
          new String[] {
            "<!ENTITY x SYSTEM '" + secret.toUri() + "'>",
            "<!ENTITY x SYSTEM 'http://127.0.0.1:1/private'>",
            "<!ENTITY % p SYSTEM 'http://127.0.0.1:1/private'>%p;",
            "<!ENTITY x 'expansion'>"
          }) {
        assertThrows(
            ParserException.class,
            () -> new JaxpParser().parse("<!DOCTYPE root [" + declaration + "]><root>&x;</root>"));
      }
    } finally {
      Files.deleteIfExists(secret);
    }
  }

  @Test
  public void ordinaryXmlStillParses() throws Exception {
    Node root = new JaxpParser().parse("<root><value>A &amp; B</value></root>");
    assertEquals("A & B", root.getNode("value").getValue());
  }

  @Test
  public void byteLimitAllowsExactSizeAndEof() throws Exception {
    LimitedInputStream exact =
        new LimitedInputStream(new ByteArrayInputStream(new byte[] {1, 2}), 2, 1000);
    assertEquals(1, exact.read());
    assertEquals(2, exact.read());
    assertEquals(-1, exact.read());
    LimitedInputStream excess =
        new LimitedInputStream(new ByteArrayInputStream(new byte[] {1, 2, 3}), 2, 1000);
    assertEquals(2, excess.read(new byte[5]));
    assertThrows(IOException.class, () -> excess.read(new byte[5]));
  }

  @Test
  public void totalDeadlineStopsSlowButActiveInput() throws Exception {
    InputStream delayed =
        new InputStream() {
          public int read() {
            try {
              Thread.sleep(20);
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
            }
            return 'x';
          }
        };
    LimitedInputStream bounded = new LimitedInputStream(delayed, 100, 5);
    assertThrows(IOException.class, () -> bounded.read());
  }
}
