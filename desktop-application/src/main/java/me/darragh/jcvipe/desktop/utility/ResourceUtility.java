package me.darragh.jcvipe.desktop.utility;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Optional;

/**
 * A utility class for accessing embedded resources.
 *
 * @author darraghd493
 * @since 0.0.1
 */
@Slf4j
@UtilityClass
public final class ResourceUtility {
    public static @NotNull Optional<InputStream> getAsStream(@NotNull String path) {
        InputStream stream = ResourceUtility.class.getResourceAsStream(path);
        if (stream == null) {
            log.warn("Resource not found: {}", path);
            return Optional.empty();
        }
        return Optional.of(stream);
    }

    public static @NotNull Optional<byte[]> getAsBytes(@NotNull String path) {
        return getAsStream(path).flatMap(stream -> {
            try (stream) {
                return Optional.of(stream.readAllBytes());
            } catch (IOException e) {
                log.error("Failed to read bytes from resource: {}", path, e);
                return Optional.empty();
            }
        });
    }

    public static @NotNull Optional<String> getAsString(@NotNull String path) {
        return getAsStream(path).flatMap(stream -> {
            try (stream) {
                return Optional.of(new String(stream.readAllBytes()));
            } catch (IOException e) {
                log.error("Failed to read bytes from resource: {}", path, e);
                return Optional.empty();
            }
        });
    }

    public static @NotNull Optional<BufferedReader> getAsReader(@NotNull String path) { // TODO: Check for memory leaks
        return getAsStream(path).flatMap(stream -> Optional.of(new BufferedReader(new InputStreamReader(stream))));
    }

    public static @NotNull String getParentDirectory(@NotNull String path) {
        int lastSlash = path.lastIndexOf('/');
        return (lastSlash >= 0) ? path.substring(0, lastSlash + 1) : "";
    }

    public static @NotNull String normalise(@NotNull String path) {
        String cleaned = path.replace('\\', '/');
        if (!cleaned.startsWith("/")) {
            cleaned = "/" + cleaned;
        }
        try {
            URI uri = URI.create(cleaned).normalize();
            return uri.getPath();
        } catch (Exception e) {
            return cleaned;
        }
    }
}
