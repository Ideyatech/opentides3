package org.opentides.service.impl;

import java.util.List;

import org.opentides.bean.PhotoInfo;
import org.opentides.dao.PhotoInfoDao;
import org.opentides.service.PhotoInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author ajalbaniel
 *
 */
@Service(value="PhotoInfoService")
public class PhotoInfoServiceImpl extends BaseCrudServiceImpl<PhotoInfo>
                implements PhotoInfoService {
        
        @Transactional(readOnly=true)
        public List<PhotoInfo> getPhotoInfoByFullPath(String path) {
                List<PhotoInfo> list = ((PhotoInfoDao)getDao()).findPhotoInfoByFullPath(path);
                if (list == null || list.size() < 1)
                        return null;
                return ((PhotoInfoDao)getDao()).findPhotoInfoByFullPath(path);
        }

        
}
