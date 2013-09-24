package com.ideyatech.dao.impl;

import org.opentides.dao.impl.BaseEntityDaoJpaImpl;
import org.springframework.stereotype.Repository;

import com.ideyatech.bean.Ninja;
import com.ideyatech.dao.NinjaDAO;

@Repository
public class NinjaDAOJpaImpl extends BaseEntityDaoJpaImpl<Ninja, Long> implements
		NinjaDAO {

}
