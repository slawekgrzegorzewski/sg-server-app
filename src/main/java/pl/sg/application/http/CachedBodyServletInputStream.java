package pl.sg.application.http;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CachedBodyServletInputStream extends ServletInputStream {

    private final InputStream cachedBodyInputStream;

    public CachedBodyServletInputStream(byte[] cachedBody) {
        this.cachedBodyInputStream = new ByteArrayInputStream(cachedBody);
    }

    @Override
    public boolean isFinished() {
        try {
            return cachedBodyInputStream.available() == 0;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int read() throws IOException {
        return this.cachedBodyInputStream.read();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return this.cachedBodyInputStream.read(b, off, len);
    }

    @Override
    public int read(byte[] b) throws IOException {
        return this.cachedBodyInputStream.read(b);
    }

    @Override
    public long skip(long n) throws IOException {
        return this.cachedBodyInputStream.skip(n);
    }

    @Override
    public int available() throws IOException {
        return this.cachedBodyInputStream.available();
    }

    @Override
    public void close() throws IOException {
        this.cachedBodyInputStream.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        this.cachedBodyInputStream.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        this.cachedBodyInputStream.reset();
    }

    @Override
    public boolean markSupported() {
        return this.cachedBodyInputStream.markSupported();
    }
}