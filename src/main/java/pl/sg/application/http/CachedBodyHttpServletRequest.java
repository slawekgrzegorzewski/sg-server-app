package pl.sg.application.http;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.Part;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.util.Collection;

import static java.util.Optional.ofNullable;

public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {

    private static final Logger LOG = LoggerFactory.getLogger(CachedBodyHttpServletRequest.class);
    private Collection<Part> parts = null;
    private byte[] cachedBody;

    public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        Boolean isMultipartRequest = ofNullable(request.getContentType())
                .map(contentType -> contentType.startsWith(ContentType.MULTIPART_FORM_DATA.getMimeType()))
                .orElse(false);
        if (isMultipartRequest) {
            try {
                parts = request.getParts();
            } catch (ServletException e) {
                LOG.warn("Trying to extract body parts on non multipart form", e);
            }
        }
        InputStream requestInputStream = request.getInputStream();
        this.cachedBody = StreamUtils.copyToByteArray(requestInputStream);
    }

    @Override
    public ServletInputStream getInputStream() {
        return new CachedBodyServletInputStream(this.cachedBody);
    }

    @Override
    public BufferedReader getReader() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);
        return new BufferedReader(new InputStreamReader(byteArrayInputStream));
    }

    @Override
    public Collection<Part> getParts() {
        return parts;
    }

    @Override
    public Part getPart(String name) {
        return ofNullable(parts)
                .stream()
                .flatMap(Collection::stream)
                .filter(part -> name.equals(part.getName()))
                .findFirst()
                .orElse(null);
    }
}