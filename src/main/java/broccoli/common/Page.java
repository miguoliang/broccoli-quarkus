package broccoli.common;

import java.util.List;
import lombok.Getter;

/**
 * The {@link Page} class.
 *
 * @param <T> T
 */
@Getter
public class Page<T> {

  private final int pageNumber;
  private final int pageSize;
  private final long totalElements;
  private final int totalPages;
  private final List<T> content;

  /**
   * The {@link Page} constructor.
   *
   * @param content       content
   * @param pageNumber    page number
   * @param pageSize      page size
   * @param totalElements total elements
   */
  public Page(List<T> content, int pageNumber, int pageSize, long totalElements) {
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.totalElements = totalElements;
    this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
    this.content = content;
  }

}
