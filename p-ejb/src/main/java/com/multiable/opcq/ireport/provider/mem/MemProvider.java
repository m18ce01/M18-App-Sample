package com.multiable.opcq.ireport.provider.mem;


import com.multiable.core.ejb.ds.MacQuery;
import com.multiable.core.share.data.ireport.CawReportDataSet;
import com.multiable.ireport.provider.ModuleProvider;
import com.multiable.opcq.share.data.ireport.mem.MemJrDto;

public class MemProvider extends ModuleProvider {
	
	public int setAlias(CawReportDataSet dataSet, int i, String alias, String entityName) {
		dataSet.setAlias(++i, alias, entityName);
		return i;
	}
	
	@Override
	public void initReportStru(CawReportDataSet reData) {
		reData.setQuery(0);
		int i = -1;
		i = setAlias(reData, i, "opcqmem", "opcqmem");
		i = setAlias(reData, i, "opcqmemt", "opcqmemt");
	}

	@Override
	public void adjustData(CawReportDataSet reData) {
		reData.assignSubReport("opcqmem", "opcqmemt", new String[] {}, "memId");

		reData.setColumnDesc("opcqmemt",
				new String[] { "memId", "bookCode", "bookDesc" },
				new String[] { "opcq.mem", "opcq.bookIsbn", "opcq.bookTitle" });
	}

	@Override
	public void handleFieldRight(CawReportDataSet reData) {
		super.handleFieldRight(reData);
	}

	@Override
	public CawReportDataSet genIdsData() {
		MemJrDto jrDto = (MemJrDto) getReportDto();
		String sql = "{CALL opcq_prtMem('" + jrDto.getMainIdString() + "')}";
		MacQuery query = new MacQuery();
		query.setQuery(sql);

		return fillAndAdjustData(query);
	}
}