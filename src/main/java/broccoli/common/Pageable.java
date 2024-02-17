package broccoli.common;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.QueryParam;
import lombok.Getter;
import lombok.Setter;

/**
 * Pageable.
 */
@Getter
@Setter
public class Pageable {

  @QueryParam("page")
  @Min(0)
  private int page = 0;

  @QueryParam("size")
  @Min(1)
  @Max(100)
  private int size = 20;
}
