package org.wsa.sjwsa.services;

import org.wsa.sjwsa.CompressionType;

public interface ICompressionService {
    byte[] compress(String source, CompressionType compressionType);
    byte[] decompress(byte[] bytes, CompressionType compressionType);
}
