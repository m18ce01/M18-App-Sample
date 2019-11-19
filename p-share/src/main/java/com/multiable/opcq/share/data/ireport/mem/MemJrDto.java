package com.multiable.opcq.share.data.ireport.mem;

import com.multiable.core.share.server.CawGlobal;
import com.multiable.core.share.util.ireport.ModuleReportDto;

public class MemJrDto extends ModuleReportDto {

	private static final long serialVersionUID = 8142728286130044585L;

	private boolean prtBookDesc = true;


	public boolean isPrtBookDesc() {
		return prtBookDesc;
	}

	public void setPrtBookDesc(boolean prtBookDesc) {
		this.prtBookDesc = prtBookDesc;
	}

	@Override
	public String getDefaultTitle() {
		return CawGlobal.getMess(getLangCode(), "opcq.memRep");
	}

	public String getTitle() {
		return !getReportTitle().isEmpty() ? getReportTitle() : getDefaultTitle();
	}

}
