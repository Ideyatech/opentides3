package org.opentides.web.json;

public interface DataView {
	boolean hasView();
	Class<? extends Views.BaseView> getView();
	Object getData();
}
