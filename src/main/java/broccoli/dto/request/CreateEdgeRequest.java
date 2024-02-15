package broccoli.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * The {@link CreateEdgeRequest} class.
 *
 * @param inVertexId  In vertex id
 * @param outVertexId Out vertex id
 * @param name        Edge name
 * @param scope       Edge scope
 */
public record CreateEdgeRequest(@NotBlank String inVertexId, @NotBlank String outVertexId,
                                @NotBlank String name, @NotBlank String scope) {
}
