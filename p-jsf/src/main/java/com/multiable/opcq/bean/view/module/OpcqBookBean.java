package com.multiable.opcq.bean.view.module;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;

import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSONObject;
import com.multiable.bean.view.ModuleAction;
import com.multiable.bean.view.ModuleAction.ActionParam;
import com.multiable.bean.view.ModuleBean;
import com.multiable.core.share.lib.ClassLib;
import com.multiable.core.share.lib.ConvertLib;
import com.multiable.core.share.message.CheckMsg;
import com.multiable.core.share.message.MsgLocator;
import com.multiable.core.share.message.MsgType;
import com.multiable.core.share.server.CawGlobal;
import com.multiable.opcq.share.OpcqStaticVar.OpcqRESTFUL;
import com.multiable.web.LookupDecorateEvent;
import com.multiable.web.ValidateEvent;
import com.multiable.web.ValueChangeEvent;
import com.multiable.web.WebMessage;
import com.multiable.web.WebMessage.MessageType;
import com.multiable.web.component.edittable.EditTable;
import com.multiable.web.rfws.RestfulWs;
import com.multiable.web.rfws.WsFactory;
import com.multiable.web.rfws.WsType;
import com.multiable.web.util.MessageUtil;

@ManagedBean(name = "opcqBook")
@ViewScoped
public class OpcqBookBean extends ModuleBean {
	private static final long serialVersionUID = 1L;


	@Override
	protected void initialized() {
		super.initialized();
	}

	@Override
	public void afterCreated(ModuleAction action) {
		super.afterCreated(action);
		prepareData(action);
	}

	@Override
	public void afterRead(ModuleAction action) {
		super.afterRead(action);
		if ((boolean) action.getDataOrDefault(ActionParam.status, false)) {
			prepareData(action);
		}
	}

	@Override
	public void afterRefresh(ModuleAction action) {
		super.afterRefresh(action);
		prepareData(action);
	}

	private void prepareData(ModuleAction action) {
		getMainData().setValue(1, "avail", 1);
	}

	@Override
	public void beforeSave(ModuleAction action) {
		super.beforeSave(action);
	}

	@Override
	public void lookupParameter(LookupDecorateEvent event) {
		super.lookupParameter(event);
	}

	@Override
	public void valueChange(ValueChangeEvent vce) {
		super.valueChange(vce);
		UIComponent component = vce.getComponent();
		String comId = component.getId();

	}

	@Override
	public boolean validateValue(ValidateEvent ve) {
		UIComponent component = ve.getComponent();
		Object value = ve.getNewValue();
		String id = component.getId();
		if (("opcqbook_isbn").equals(id)) {
			String isbn = ConvertLib.toString(value);
			StringBuffer path = new StringBuffer();
			path.append(OpcqRESTFUL.book).append("/checkIsbn/");
			RestfulWs ws = WsFactory.createWS(path.toString(), WsType.get);
			ws.addHeaderParam("isbn", isbn);
			HttpResponse response = ws.sendRequest();
			if (WsFactory.isResponseOK(response)) {
				String info = WsFactory.resolveResponse(response);
				JSONObject obj = JSONObject.parseObject(info);
				if(obj != null && obj.getBooleanValue("status")){
					return true;
				}
			}
			addValidateMessage(component, "opcq.IsbnError");
			return false;
		}
		return super.validateValue(ve);
	}
	
	public void addValidateMessage(UIComponent component, String... param) {
		String contentMess = "";
		String replace = null;
		String replaceMess = null;
		switch (param.length) {
		case 1:
			contentMess = param[0];
			break;
		case 3:
			contentMess = param[0];
			replace = param[1];
			replaceMess = param[2];
			break;
		default:
			return;
		}
		String content = CawGlobal.getMess(contentMess);
		if (replace != null && replaceMess != null) {
			content = content.replace(replace, CawGlobal.getMess(replaceMess));
		}

		if (component instanceof EditTable) {
			EditTable eTable = (EditTable) component;
			CheckMsg msg = new CheckMsg();
			msg.setType(MsgType.Error);
			msg.setInfo(content);
			msg.setInfo_desc(content);
			msg.setTrace(ClassLib.getCallerClass());

			MsgLocator loc = new MsgLocator();
			loc.setTableName(eTable.getTableName());
			loc.setColName(eTable.getCellColumnName());
			loc.setRow(eTable.getCellRow());
			msg.getLocators().add(loc);

			WebMessage message = MessageUtil.createMessage(msg);
			message.setAutoClose(true);
			message.setClosable(true);
			message.setDelayClose(6000);
			MessageUtil.postMessage(component, message);
		} else {
			MessageUtil.postMessage(component, MessageType.ERROR, content);
		}
	}

}
