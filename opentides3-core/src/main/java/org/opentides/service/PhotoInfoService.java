package org.opentides.service;

import java.util.List;

import org.opentides.bean.PhotoInfo;

/**
 * @author ajalbaniel
 *
 */
public interface PhotoInfoService extends BaseCrudService<PhotoInfo> {

        /**
         * get PhotoInfo by Full Path
         * @param path
         * @return
         */
        public List<PhotoInfo> getPhotoInfoByFullPath(String path) ;
}