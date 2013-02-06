package org.opentides.web.json;


public class PojoView implements DataView {

	private final Object data;
	private final Class<? extends Views.BaseView> view;
	
	public PojoView(Object data, Class<? extends Views.BaseView> view) {
		super();
		this.data = data;
		this.view = view;
	}
	@Override
	public boolean hasView() {
		return true;
	}
	/**
	 * @return the data
	 */
	public final Object getData() {
		return data;
	}
	/**
	 * @return the view
	 */
	public final Class<? extends Views.BaseView> getView() {
		return view;
	}
}
