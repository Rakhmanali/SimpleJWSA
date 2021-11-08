package jwsa.services;

import jwsa.CompressionType;

public interface ICompressionService {
    byte[] compress(String source, CompressionType compressionType);
    byte[] decompress(byte[] bytes, CompressionType compressionType);
}
