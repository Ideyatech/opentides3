package org.opentides.web.json.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

public class HibernateAwareObjectMapper extends ObjectMapper {
	
	private static final long serialVersionUID = -103471301066332061L;

	public HibernateAwareObjectMapper() {
		registerModule(new Hibernate4Module());
	}

}
