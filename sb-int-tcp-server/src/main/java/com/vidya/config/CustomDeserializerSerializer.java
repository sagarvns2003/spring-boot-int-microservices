package com.vidya.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.ip.tcp.serializer.AbstractPooledBufferByteArraySerializer;
import org.springframework.integration.ip.tcp.serializer.SoftEndOfStreamException;

/**
 * @author Vidya Sagar Gupta
 * @since v1.0.0
 */
@Slf4j
public class CustomDeserializerSerializer extends AbstractPooledBufferByteArraySerializer {

  private final int expectedMessageLength;

  public CustomDeserializerSerializer(int expectedMessageLength) {
    this.expectedMessageLength = expectedMessageLength;
  }

  @Override
  protected byte[] doDeserialize(InputStream inputStream, byte[] buffer) throws IOException {

    int availableByte = inputStream.available();
    log.info("Request payload available to read: {} bytes. Deserializing now...", availableByte);

    int n = 0;
    try {
      int maxMessageSize = 0;
      do {
        int bite = inputStream.read();
        if (bite < 0 && n == 0) {
          throw new SoftEndOfStreamException("Stream closed between payloads.");
        }

        this.checkClosure(bite);
        if (n == (this.expectedMessageLength - 1)) {
          return this.copyToSizedArray(buffer, n);
        }
        buffer[n++] = (byte) bite;
        maxMessageSize = this.getMaxMessageSize();
      } while (n < maxMessageSize);

      throw new IOException("Unable to deserialize payload.");

    } catch (SoftEndOfStreamException softEndOfStreamException) {
      throw softEndOfStreamException;
    } catch (RuntimeException | IOException exp) {
      throw exp;
    }
  }

  @Override
  public void serialize(byte[] messageBytes, OutputStream outputStream) throws IOException {
    log.info(
        "Writing response message byte array to output stream. Total written byte: {}",
        messageBytes.length);
    outputStream.write(messageBytes);
    outputStream.flush();
  }
}
