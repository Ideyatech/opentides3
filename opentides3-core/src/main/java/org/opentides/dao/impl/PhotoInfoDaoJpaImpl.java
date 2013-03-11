/*
   Licensed to the Apache Software Foundation (ASF) under one
   or more contributor license agreements.  See the NOTICE Photo
   distributed with this work for additional information
   regarding copyright ownership.  The ASF licenses this Photo
   to you under the Apache License, Version 2.0 (the
   "License"); you may not use this Photo except in compliance
   with the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing,
   software distributed under the License is distributed on an
   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
   KIND, either express or implied.  See the License for the
   specific language governing permissions and limitations
   under the License.    
 */
package org.opentides.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opentides.bean.PhotoInfo;
import org.opentides.dao.PhotoInfoDao;
import org.springframework.stereotype.Repository;

@Repository(value="photoInfoDao")
public class PhotoInfoDaoJpaImpl extends BaseEntityDaoJpaImpl<PhotoInfo, Long> implements
                PhotoInfoDao {
        
        public List<PhotoInfo> findPhotoInfoByFullPath(String fullPath) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("path", fullPath.trim());
                List<PhotoInfo> result = findByNamedQuery("jpql.photoInfo.findPhotoInfoByFullPath", params);
                return result;
        }

}
