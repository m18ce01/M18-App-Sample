package com.multiable.opcq.ejb.checker;

import com.multiable.core.ejb.checker.base.CheckRange;
import com.multiable.core.ejb.checker.base.CheckType;
import com.multiable.core.ejb.checker.base.EntityCheck;
import com.multiable.core.ejb.eao.curd.SeSaveParam;
import com.multiable.core.share.data.SqlTable;
import com.multiable.core.share.entity.SqlEntity;
import com.multiable.core.share.message.CawError;
import com.multiable.core.share.message.CheckMsg;
import com.multiable.core.share.message.CheckMsgLib;
import com.multiable.core.share.server.CawGlobal;

public class OpcqBookChecker {

	@EntityCheck(type = CheckType.SAVE, range = CheckRange.BEFORE, checkOrder = 200)
	public CheckMsg clear_subFooter(SeSaveParam param) {
		SqlEntity entity = param.getSqlEntity();
		SqlTable opcqbook = entity.getMainData();
		CheckMsg msg = null;
		if(!IsbnCheckerLib.validateIsbn13(opcqbook.getValueStr(1, "isbn"))){
			msg = CheckMsgLib.createMsg(CawError.FAILED_SAVE, CawGlobal.getMess("opcq.IsbnError"));
			msg.setInfo_desc(CawGlobal.getMess("opcq.IsbnError"));
		}	
		return msg;
	}
}
