package org.opentides.service.impl;

import org.opentides.bean.PhotoInfo;
import org.opentides.service.PhotoInfoService;
import org.springframework.stereotype.Service;


/**
 * @author ajalbaniel
 *
 */
@Service(value="PhotoInfoService")
public class PhotoInfoServiceImpl extends BaseCrudServiceImpl<PhotoInfo>
                implements PhotoInfoService {
        
}
