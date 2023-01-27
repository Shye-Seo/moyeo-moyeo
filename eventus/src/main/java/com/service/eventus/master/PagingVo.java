package com.service.eventus.master;

import lombok.Data;

@Data
public class PagingVo {
	
	private int pageSize = 10; //페이지 당 보여지는 게시글의 최대 개수
	private int blockSize = 5; //페이징된 버튼의 블럭당 최대 개수
	private int page = 1; //현재 페이지
	private int block = 1; //현재 블럭
	private int totalListCnt; //총 게시글 수
	private int totalPageCnt; //총 페이지 수
	private int totalBlockCnt; //총 블럭 수
	private int startPage = 1; //블럭 시작 페이지
	private int endPage = 1; //블럭 마지막 페이지
	private int startIndex = 0; //DB 접근시작 index
	private int prevBlock; //이전 블럭의 마지막 페이지
	private int nextBlock; //다음 블럭의 시작 페이지
	
	private String searchKeyword;
	
	public PagingVo(int totalListCnt, int page) {

		// 총 게시물 수와 현재 페이지를 Controller로부터 받아온다.

		// 총 게시물 수 - totalListCnt
		// 현재 페이지	- page
		
		setPage(page); //현재 페이지
		setTotalListCnt(totalListCnt); //총 게시글 수

		// 한 페이지의 최대 개수를 총 게시물 수 * 1.0로 나누어주고 올림 해준다.
		// 총 페이지 수 계산
		setTotalPageCnt((int) Math.ceil(totalListCnt * 1.0 / pageSize));

		// 한 블럭의 최대 개수를 총  페이지의 수 * 1.0로 나누어주고 올림 해준다.
		// 총 블럭 수 계산
		setTotalBlockCnt((int) Math.ceil(totalPageCnt * 1.0 / blockSize));
        
		// 현재 페이지 * 1.0을 블럭의 최대 개수로 나누어주고 올림한다.
		// 현재 블럭 계산
		setBlock((int) Math.ceil((page * 1.0)/blockSize)); 

		setStartPage((block - 1) * blockSize + 1); //블럭 시작 페이지
		setEndPage(startPage + blockSize - 1); //블럭 마지막 페이지
        
		/* === 블럭 마지막 페이지에 대한 validation ===*/
		if(endPage > totalPageCnt){this.endPage = totalPageCnt;}
		setPrevBlock((block * blockSize) - blockSize); //이전 블럭(클릭 시, 이전 블럭 마지막 페이지)

		/* === 이전 블럭에 대한 validation === */
		if(prevBlock < 1) {this.prevBlock = 1;}
		setNextBlock((block * blockSize) + 1); //다음 블럭(클릭 시, 다음 블럭 첫번째 페이지)
        
		/* === 다음 블럭에 대한 validation ===*/
		if(nextBlock > totalPageCnt) {nextBlock = totalPageCnt;}
		setStartIndex((page-1) * pageSize); //DB 접근 시작 index
		
		if(totalListCnt == 0) { //결과와 일치하는 게시물이 없을 때
			setTotalPageCnt(1);
			setEndPage(1);
			setStartPage(1);
		}
	}
}
