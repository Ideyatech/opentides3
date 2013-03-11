package org.opentides.dao;

import java.util.List;

import org.opentides.bean.PhotoInfo;

public interface PhotoInfoDao extends BaseEntityDao<PhotoInfo, Long> {

    /**
     * Finds for PhotoInfo reference for the given path.
     * @param fullPath
     * @return
     */
    public List<PhotoInfo> findPhotoInfoByFullPath(String fullPath);

}