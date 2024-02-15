package broccoli.dto.response;

import java.util.List;

public class Page<T> {

  private int pageNumber;
  private int pageSize;
  private long totalElements;
  private int totalPages;
  private List<T> content;

  public Page(List<T> content, int pageNumber, int pageSize, long totalElements) {
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.totalElements = totalElements;
    this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
    this.content = content;
  }

  public int getPageNumber() {
    return pageNumber;
  }

  public int getPageSize() {
    return pageSize;
  }

  public long getTotalElements() {
    return totalElements;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public List<T> getContent() {
    return content;
  }
}
