package com.ideyatech.service.impl;

import org.opentides.service.impl.BaseCrudServiceImpl;
import org.springframework.stereotype.Service;

import com.ideyatech.bean.Ninja;
import com.ideyatech.service.NinjaService;

@Service(value = "ninjaService")
public class NinjaServiceImpl extends BaseCrudServiceImpl<Ninja> implements
		NinjaService {

}
