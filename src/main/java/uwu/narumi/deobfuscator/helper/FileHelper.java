package uwu.narumi.deobfuscator.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class FileHelper {

    private static final Logger LOGGER = LogManager.getLogger(FileHelper.class);

    private FileHelper() {
    }

    public static Map<String, byte[]> loadFilesFromZip(String file) {
        Map<String, byte[]> files = new HashMap<>();

        try (ZipFile zipFile = new ZipFile(file)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while(entries.hasMoreElements()){
                ZipEntry zipEntry = entries.nextElement();
                try {
                    files.put(zipEntry.getName(), zipFile.getInputStream(zipEntry).readAllBytes());
                } catch (Exception e) {
                    LOGGER.error("Could not load ZipEntry: {}", zipEntry.getName());
                    LOGGER.debug("Error", e);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Could not load file: {}", file);
            LOGGER.debug("Error", e);
        }

        return files;
    }
    private static final int MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8;
    private static final int BUFFER_SIZE = 8192;

    public static byte[] read(InputStream source) throws IOException {
        int capacity = 2048;
        byte[] buf = new byte[capacity];
        int nread = 0;
        int n;
        for (;;) {
            // read to EOF which may read more or less than initialSize (eg: file
            // is truncated while we are reading)
            while ((n = source.read(buf, nread, capacity - nread)) > 0)
                nread += n;

            // if last call to source.read() returned -1, we are done
            // otherwise, try to read one more byte; if that failed we're done too
            if (n < 0 || (n = source.read()) < 0)
                break;

            // one more byte was read; need to allocate a larger buffer
            if (capacity <= MAX_BUFFER_SIZE - capacity) {
                capacity = Math.max(capacity << 1, BUFFER_SIZE);
            } else {
                if (capacity == MAX_BUFFER_SIZE)
                    throw new OutOfMemoryError("Required array size too large");
                capacity = MAX_BUFFER_SIZE;
            }
            buf = Arrays.copyOf(buf, capacity);
            buf[nread++] = (byte)n;
        }
        return (capacity == nread) ? buf : Arrays.copyOf(buf, nread);
    }

}
