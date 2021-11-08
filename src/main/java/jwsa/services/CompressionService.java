package jwsa.services;

import jwsa.CompressionType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

// Java gzip compression and decompression code example
// https://developpaper.com/java-gzip-compression-and-decompression-code-example/

public class CompressionService implements ICompressionService  {
    public byte[] compress(String source, CompressionType compressionType){
        byte[] result;

        switch (compressionType)
        {
            case GZip:
            {
                result = gzipCompress(source);
                break;
            }
            case NONE:
            default:
            {
                result = source.getBytes(StandardCharsets.UTF_8);
                break;
            }
        }

        return result;
    }

    public byte[] decompress(byte[] bytes, CompressionType compressionType)
    {
        byte[] result;

        switch (compressionType)
        {
            case GZip:
            {
                result = gzipDecompress(bytes);
                break;
            }
            case NONE:
            default:
            {
                result = bytes;
                break;
            }
        }

        return result;
    }

    private byte[] gzipCompress(String source){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream;
        try {
            gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(source.getBytes(StandardCharsets.UTF_8));
            gzipOutputStream.close();
        } catch ( Exception e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    private byte[] gzipDecompress(byte[] bytes){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gzipInputStream.read(buffer)) >= 0) {
                byteArrayOutputStream.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }
}
