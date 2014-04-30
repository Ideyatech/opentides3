package org.opentides.bean;

public class SortField {
	
	public static enum OrderFlow {
		ASC, DESC
	}
	
	public SortField() {
		
	}
	
	public SortField(String fieldName, OrderFlow orderFlow) {
		this.fieldName = fieldName;
		this.orderFlow = orderFlow;
	}


	private String fieldName;
	
	private OrderFlow orderFlow;

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the order
	 */
	public OrderFlow getOrderFlow() {
		return orderFlow;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrderFlow(OrderFlow order) {
		this.orderFlow = order;
	}

}
