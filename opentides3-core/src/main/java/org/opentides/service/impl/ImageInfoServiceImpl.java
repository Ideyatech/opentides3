package org.opentides.service.impl;

import org.opentides.bean.ImageInfo;
import org.opentides.service.ImageInfoService;
import org.springframework.stereotype.Service;


/**
 * @author AJ
 *
 */
@Service(value="imageInfoService")
public class ImageInfoServiceImpl extends BaseCrudServiceImpl<ImageInfo>
                implements ImageInfoService {
        
}
