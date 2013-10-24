package org.opentides.dao.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.opentides.bean.FileInfo;
import org.opentides.dao.FileInfoDao;
import org.springframework.beans.factory.annotation.Autowired;

public class FileInfoDAOIntegrationTest extends BaseDaoTest {
	
	@Autowired
	private FileInfoDao fileInfoDao;

	@Test
	public void testFindFileInfoByFullPath() {
		List<FileInfo> files = fileInfoDao.findFileInfoByFullPath("/home/user/file1.png");
		assertNotNull(files);
		assertEquals(1, files.size());
		FileInfo actual = files.get(0);
		FileInfo expected = fileInfoDao.loadEntityModel(1l);
		assertEquals(expected, actual);
	}

}
