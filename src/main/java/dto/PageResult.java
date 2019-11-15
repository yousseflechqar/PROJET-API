package dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class PageResult {

	public Collection<ProjetDto> content = new ArrayList<ProjetDto>();
	public Long totalElements;
	
	public PageResult() {}
	
	public PageResult(List<ProjetDto> content, Long totalElements) {
		this.content=content;
		this.totalElements=totalElements;
	}
//	public Integer number;
//	public Long totalPages;
//	public Integer currentPage;	
//	public String order;
//	public String sort;
//	public Integer size;

}
