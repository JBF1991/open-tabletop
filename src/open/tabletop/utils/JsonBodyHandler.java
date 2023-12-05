package open.tabletop.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.net.http.HttpResponse;

import com.google.gson.GsonBuilder;

public class JsonBodyHandler<W> implements HttpResponse.BodyHandler<W> {

    private final Class<W> clazz;

    public JsonBodyHandler(Class<W> clazz) {
        this.clazz = clazz;
    }

    public static <W> HttpResponse.BodySubscriber<W> asJSON(Class<W> targetType) {
        HttpResponse.BodySubscriber<InputStream> upstream = HttpResponse.BodySubscribers.ofInputStream();
        return HttpResponse.BodySubscribers.mapping(upstream, inputStream -> toSupplierOfType(inputStream, targetType));
    }

    public static <W> W toSupplierOfType(InputStream inputStream, Class<W> targetType) {
        try (Reader stream = new InputStreamReader(inputStream)) {
            return new GsonBuilder().create().fromJson(stream, targetType);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public HttpResponse.BodySubscriber<W> apply(HttpResponse.ResponseInfo responseInfo) {
        return asJSON(clazz);
    }
    
}