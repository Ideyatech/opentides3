/*
   Licensed to the Apache Software Foundation (ASF) under one
   or more contributor license agreements.  See the NOTICE file
   distributed with this work for additional information
   regarding copyright ownership.  The ASF licenses this file
   to you under the Apache License, Version 2.0 (the
   "License"); you may not use this file except in compliance
   with the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing,
   software distributed under the License is distributed on an
   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
   KIND, either express or implied.  See the License for the
   specific language governing permissions and limitations
   under the License.    
 */
package org.opentides.service.impl;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.opentides.bean.FileInfo;
import org.opentides.dao.FileInfoDao;
import org.opentides.service.FileInfoService;
import org.opentides.util.FileUtil;
import org.opentides.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author allantan
 * 
 */
@Service(value = "fileInfoService")
public class FileInfoServiceImpl extends BaseCrudServiceImpl<FileInfo>
		implements FileInfoService {

	public List<FileInfo> getFileInfoByFullPath(String path) {
		List<FileInfo> list = ((FileInfoDao) getDao())
				.findFileInfoByFullPath(path);
		if (list == null || list.size() < 1)
			return null;
		return ((FileInfoDao) getDao()).findFileInfoByFullPath(path);
	}

	@Transactional
	@Override
	public boolean deletePhysicalFile(String path) {
		List<FileInfo> fileInfos = this.getFileInfoByFullPath(path);
		Iterator<FileInfo> fileIterator = fileInfos.iterator();
		while(fileIterator.hasNext()) {
			FileInfo fileInfo = fileIterator.next();
			delete(fileInfo.getId());
		}
		//No exception so delete the physical file
		File file = new File(path);
		boolean isDeleted = false;
		isDeleted = file.delete();
		if(!isDeleted) {
			throw new RuntimeException("File cannot be deleted.");
		}
		return isDeleted;
	}

	@Override
	public byte[] convertToByteArray(FileInfo fileInfo) {
		if(fileInfo!=null && !StringUtil.isEmpty(fileInfo.getFullPath())){
			File file = new File(fileInfo.getFullPath());
			
			return FileUtil.readFileAsBytes(file);
		}
		
		return null;
	}

}
