package com.multiable.opcq.bean.view.module;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import com.multiable.bean.view.ModuleAction;
import com.multiable.bean.view.ModuleAction.ActionParam;
import com.multiable.bean.view.ModuleBean;
import com.multiable.web.LookupDecorateEvent;
import com.multiable.web.ValidateEvent;
import com.multiable.web.ValueChangeEvent;

@ManagedBean(name = "opcqBookCat")
@ViewScoped
public class OpcqBookCatBean extends ModuleBean {
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
		return super.validateValue(ve);
	}

}
