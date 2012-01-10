package com.systex.sop.cvs.helper;

import java.util.List;

import com.systex.sop.cvs.dto.Tbsoptcvsmap;
import com.systex.sop.cvs.dto.Tbsoptcvsver;

public class CVSParserHelper {
	
	public void parser(List<String> lineList) {
		if (lineList == null || lineList.size() < 1) return;
		
		Tbsoptcvsmap map = new Tbsoptcvsmap();
		Tbsoptcvsver ver = new Tbsoptcvsver();
	}

}
