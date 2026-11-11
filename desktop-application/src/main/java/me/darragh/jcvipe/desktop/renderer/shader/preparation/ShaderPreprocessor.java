package me.darragh.jcvipe.desktop.renderer.shader.preparation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.darragh.jcvipe.desktop.utility.ResourceUtility;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * "Prepares" a shader by automatically handling:
 * <ul>
 *     <li>file inclusion</li>
 * </ul>
 *
 * @author darraghd493
 * @since 0.0.1
 */
@Slf4j
@RequiredArgsConstructor
public final class ShaderPreprocessor {
    public static @NotNull String process(@NotNull String path) {
        return process(path, new HashSet<>(), new Stack<>());
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public static @NotNull String process(@NotNull String path, @NotNull Set<String> processedFiles, @NotNull Stack<String> includedFiles) {
        String normalisedPath = ResourceUtility.normalise(path);

        if (includedFiles.contains(normalisedPath)) {
            throw new IllegalStateException("Circular #include dependency detected: " + normalisedPath + " (Stack trace: " + includedFiles + ")");
        }

        if (processedFiles.contains(normalisedPath)) {
            return "";
        }

        processedFiles.add(normalisedPath);
        includedFiles.push(normalisedPath);

        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = ResourceUtility.getAsReader(normalisedPath).orElseThrow(
                () -> new IllegalArgumentException("Shader resource not found: " + normalisedPath)
        )) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String trimmed = line.trim();

                if (trimmed.startsWith("#include") && !trimmed.startsWith("//")) {
                    String includeRelativePath = extractIncludePath(trimmed, lineNumber, normalisedPath),
                            parentDirectory = ResourceUtility.getParentDirectory(normalisedPath),
                            resolvedIncludePath = ResourceUtility.normalise(parentDirectory + includeRelativePath);

                    String includedContent = process(resolvedIncludePath, processedFiles, includedFiles);
                    builder.append("// --- begin #include \"").append(includeRelativePath).append("\" ---\n");
                    builder.append(includedContent);
                    if (!includedContent.endsWith("\n") && !includedContent.isEmpty()) {
                        builder.append("\n");
                    }
                    builder.append("// --- end #include \"").append(includeRelativePath).append("\" ---\n");
                } else if (trimmed.equals("#pragma once")) {
                    // TODO
                } else {
                    builder.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading shader resource at " + normalisedPath, e);
        } finally {
            includedFiles.pop();
        }

        return builder.toString();
    }

    //region Helpers
    private static String extractIncludePath(String line, int lineNumber, String currentFile) {
        int firstQuote = line.indexOf('"'),
                lastQuote = line.lastIndexOf('"');
        if (firstQuote == -1 || lastQuote <= firstQuote) {
            throw new IllegalArgumentException("Malformed #include directive at " + currentFile + ":" + lineNumber + " -> " + line);
        }
        return line.substring(firstQuote + 1, lastQuote);
    }
    //endregion
}
