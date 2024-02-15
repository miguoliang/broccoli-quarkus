package broccoli.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * The {@link SetVertexPropertyRequest} class.
 */
public record SetVertexPropertyRequest(@NotBlank String scope, @NotBlank String key,
                                       @NotNull String value) {
}
