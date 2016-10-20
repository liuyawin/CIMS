package com.utils;

/**
 * 分页设置
 * 
 * @author zjn
 * @date 2016年9月13日
 */
public class Pager {
	private int page = 1; // 当前页
	private int pageSize = 10; // 每页多少行
	private int offset;// 当前页起始行
	private int limit = pageSize;// 往后取多少条
	private int totalRow; // 共多少行
	private int totalPage; // 共多少页

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		if (page < 1) {
			page = 1;
		} else {
			offset = pageSize * (page - 1);
		}
		this.page = page;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		this.limit = pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getOffset() {
		return offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;

		totalPage = (totalRow + pageSize - 1) / pageSize;
		if (totalPage < page) {
			page = totalPage;
			offset = totalPage == 0 ? 0 : pageSize * (page - 1);
		}

	}

	public int getTotalRow() {
		return totalRow;
	}

	public int getTotalPage() {
		return this.totalPage;
	}

	@Override
	public String toString() {
		return "Pager [page=" + page + ", pageSize=" + pageSize + ", offset=" + offset + ", limit=" + limit
				+ ", totalRow=" + totalRow + ", totalPage=" + totalPage + "]";
	}

}
