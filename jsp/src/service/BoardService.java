package service;

import java.util.HashMap;
import java.util.List;

public interface BoardService {

	List<HashMap> selectBoardList();
	
	HashMap selectBoard();
	
	int insertBoard();
	
	int deleteBoard();
	
	int updateBoard();
}
