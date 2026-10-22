package org.cybergarage.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/** Bounds bytes and elapsed time for an untrusted buffered message. */
public final class LimitedInputStream extends FilterInputStream {
  private long remaining;
  private final long deadline;

  public LimitedInputStream(InputStream in, long maxBytes, long timeoutMillis) {
    super(in);
    if (maxBytes < 0 || timeoutMillis <= 0) throw new IllegalArgumentException();
    remaining = maxBytes;
    deadline = System.nanoTime() + timeoutMillis * 1000000L;
  }

  private void checkDeadline() throws IOException {
    if (System.nanoTime() - deadline >= 0) throw new IOException("Message deadline exceeded");
  }

  public int read() throws IOException {
    checkDeadline();
    int value = in.read();
    checkDeadline();
    if (value >= 0 && remaining-- <= 0) throw new IOException("Message size exceeded");
    return value;
  }

  public int read(byte[] buffer, int offset, int length) throws IOException {
    java.util.Objects.checkFromIndexSize(offset, length, buffer.length);
    if (length == 0) return 0;
    if (remaining <= 0) return read() < 0 ? -1 : 0;
    checkDeadline();
    int count = in.read(buffer, offset, (int) Math.min(length, remaining));
    checkDeadline();
    if (count > 0) remaining -= count;
    return count;
  }

  public long skip(long count) throws IOException {
    if (count <= 0) return 0;
    checkDeadline();
    long skipped = in.skip(Math.min(count, remaining));
    checkDeadline();
    remaining -= skipped;
    return skipped;
  }

  public boolean markSupported() {
    return false;
  }

  public void reset() throws IOException {
    throw new IOException("Mark/reset not supported");
  }
}
