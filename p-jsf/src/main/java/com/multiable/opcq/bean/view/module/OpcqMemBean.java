package com.multiable.opcq.bean.view.module;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.naming.NamingException;

import com.multiable.bean.view.ModuleAction;
import com.multiable.bean.view.ModuleAction.ActionParam;
import com.multiable.bean.view.ModuleBean;
import com.multiable.core.share.data.SqlTable;
import com.multiable.core.share.data.SqlTableField;
import com.multiable.core.share.localinterface.modules.SearchLocal;
import com.multiable.core.share.meta.search.FormatCond;
import com.multiable.core.share.meta.search.SearchResult;
import com.multiable.core.share.meta.search.StParameter;
import com.multiable.core.share.util.JNDILocator;
import com.multiable.ui.application.FacesAssistant;
import com.multiable.web.LookupDecorateEvent;
import com.multiable.web.ValidateEvent;
import com.multiable.web.ValueChangeEvent;
import com.multiable.web.WebUtil;
import com.multiable.web.component.edittable.EditTable;
import com.multiable.web.component.edittable.entity.TableAction.ActionType;
import com.multiable.web.component.edittable.interfaces.TableActionListener;
import com.multiable.web.component.edittable.interfaces.TableModel;

@ManagedBean(name = "opcqMem")
@ViewScoped
public class OpcqMemBean extends ModuleBean {
	private static final long serialVersionUID = 1L;

	@Override
	protected void initialized() {
		super.initialized();
		getTableModel("opcqmemt").addListener(new ActionListener());
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
		prepareUIData(action);
		refreshDesc();
	}
	
	private void prepareUIData(ModuleAction action) {
		SqlTable memt = getEntity().getData("opcqmemt");
		memt.addField(new SqlTableField("bookIsbn", String.class));
		memt.addField(new SqlTableField("bookDesc", String.class));
	}

	@Override
	public void beforeSave(ModuleAction action) {
		super.beforeSave(action);
	}

	@Override
	public void lookupParameter(LookupDecorateEvent event) {
		super.lookupParameter(event);
		
		StParameter param = (StParameter) event.getSource();
		List<FormatCond> appendConds = param.getAppendConds();
		String compId = event.getComponentId();

		if (("opcqmemt").equals(compId)) {
			if ("opcqBook".equals(event.getLookupAction().getLookupType())) {
				FormatCond cond = new FormatCond();
				cond.setLeftField("avail");
				cond.setOperator("=");
				cond.setRightField("1");
				
				appendConds.add(cond);
			}
		}
	}

	@Override
	public void valueChange(ValueChangeEvent vce) {
		super.valueChange(vce);
		UIComponent component = vce.getComponent();
		String comId = component.getId();
		if (comId.equals("opcqmemt")) {
			EditTable eTable = (EditTable) vce.getComponent();
			String columnName = eTable.getCellColumnName();
			int rowIndex = getEditTableRowIndex(eTable);
			SqlTable myTable = eTable.getLookupResult();
			SqlTable memt = getEntity().getData("opcqmemt");
			if (columnName.equals("bookId")) {
				if (myTable != null && myTable.size() > 0) {
					load_book(eTable, myTable, rowIndex);
				} else {
					memt.setString(rowIndex, "bookDesc", "");
					memt.setString(rowIndex, "bookIsbn", "");
				}
			}
		}
	}

	@Override
	public boolean validateValue(ValidateEvent ve) {
		return super.validateValue(ve);
	}

	public void load_book(EditTable eTable, SqlTable myTable, int rowIndex) {
		String tableName = eTable.getTableName();
		SqlTable memt = getEntity().getData("opcqmemt");
		eTable.setCellRowid(getTableModel(tableName).getRowId(rowIndex));
		eTable.fireTableRowUpdate(eTable.getCellRowid());

		if (myTable != null && myTable.size() > 0) {
			for (int i = 1; i <= myTable.size(); i++) {
				if (i != 1) {
					boolean flag = getTableModel(tableName).triggerTableEvent(eTable, ActionType.addRow);
					if (flag) {
						rowIndex = rowIndex + 1;
					} else {
						continue;
					}
				}

				memt.setLong(rowIndex, "bookId", myTable.getLong(i, "id"));
				memt.setString(rowIndex, "bookDesc", myTable.getValueStr(i, "desc"));
				memt.setString(rowIndex, "bookIsbn", myTable.getValueStr(i, "isbn"));

				getTableModel(eTable.getTableName()).assignedLookupData(eTable.getTableName(), rowIndex, myTable,
						i);
			}
		}

		reloadEditTable("opcqmemt");
	}
	
	public void reloadEditTable(String eTableId) {
		FacesAssistant.getCurrentInstance().execute("EditTable.reloadGrid('" + WebUtil.getFacesId(eTableId) + "');");
	}
	
	public int getEditTableRowIndex(EditTable eTable) {
		TableModel tableModel = eTable.getModel();

		if (eTable.getModel() == null) {
			tableModel = getTableModel(eTable.getTableName());
		}

		if (tableModel == null) {
			return -1;
		}
		return tableModel.getRowIndex(eTable.getCellRowid());
	}
	
	public void refreshDesc() {
		SqlTable memt = getEntity().getData("opcqmemt");
		StringBuffer bookIds = new StringBuffer("-1"); 
		for(int i : memt){
			long bookId = memt.getLong(i, "bookId");
			 if (bookId != 0) {
				 bookIds.append("," + bookId);
			 }
		}
		
		
		SearchLocal search = null;
		try {
			search = JNDILocator.getInstance().lookupEJB("SearchEJB", SearchLocal.class);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		if (search == null) {
			return ;
		}

		StParameter stParam = new StParameter();
		stParam.setStSearch("opcqBook");

		stParam.addExtraField("desc");
		stParam.addExtraField("isbn");
		
		FormatCond fcond = new FormatCond();
		fcond.setLeftField("id");
		fcond.setOperator("in");
		fcond.setRightField(String.valueOf(bookIds.toString()));
		stParam.getAppendConds().add(fcond);

		SearchResult stResult = search.searchData(stParam);

		if (stResult == null || stResult.getResultTable() == null) {
			return;
		}
		SqlTable bookInfo = stResult.getResultTable();
		
		Map<Long, Integer> bookMap = new HashMap<>();
		for (int i = 1; i <= bookInfo.size(); i++) {
			bookMap.put(bookInfo.getLong(i, "id"), i);
		}
		
		for(int i : memt){
			long bookId = memt.getLong(i, "bookId");
			if (bookId != 0) {
				int rec = bookMap.get(bookId);
				if(rec > 0){
					memt.setValue(i, "bookDesc", bookInfo.getValueStr(rec, "desc"));
					memt.setValue(i, "bookIsbn", bookInfo.getValueStr(rec, "isbn"));
				}
			}
		}
	}
	
	class ActionListener implements TableActionListener, Serializable {
		private static final long serialVersionUID = 1L;
	}
}
