package com.multiable.erp.opcq.eao.bean;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import com.multiable.opcq.share.interfaces.local.OpcqBookLocal;
import com.multiable.opcq.share.interfaces.restful.OpcqBookRestful;
import com.alibaba.fastjson.JSONObject;
import com.multiable.opcq.ejb.checker.IsbnCheckerLib;
import com.multiable.opcq.share.OpcqStaticVar.OpcqEJB;

@Stateless(name = OpcqEJB.BookEJB)
@Local({ OpcqBookLocal.class, OpcqBookRestful.class })
public class OpcqBookEJB implements OpcqBookLocal, OpcqBookRestful {

	@Override
	public Response wsCheckIsbn(String isbn) {
		boolean valid = checkIsbn(isbn);
		JSONObject obj = new JSONObject();
		obj.put("status", valid);
		return Response.ok(obj.toJSONString()).build();
	}

	@Override
	public boolean checkIsbn(String isbn) {
		return IsbnCheckerLib.validateIsbn13(isbn);
	}
}
