package org.opentides.dao.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.opentides.bean.Sequence;
import org.opentides.dao.SequenceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

public class SequenceDaoIntegrationTest extends BaseDaoTest {
	
	@Autowired
	private SequenceDao sequenceDao;
	
	@Test
	public void testLoadSequenceByKey() {
		Sequence seq1 = sequenceDao.loadSequenceByKey("KEY_1");
		assertNotNull(seq1);
		Sequence expected = new Sequence();
		expected.setKey("KEY_1");
		expected.setValue(1l);
		assertEquals(expected, seq1);
		
		Sequence seq2 = sequenceDao.loadSequenceByKey("NOT_EXISTING");
		assertNull(seq2);
	}
	
	@Test
	public void testIncrementValue() {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				sequenceDao.incrementValue("KEY_1");
			}
		});
		Sequence seq = sequenceDao.loadSequenceByKey("KEY_1");
		assertEquals(new Long(2), seq.getValue());
		
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				sequenceDao.incrementValue("NEW_SEQUENCE");
			}
		});
		Sequence seq2 = sequenceDao.loadSequenceByKey("NEW_SEQUENCE");
		assertNotNull(seq2);
		assertEquals(new Long(1), seq2.getValue());
	}
	
	@Test
	public void testIncrementValueWithStep() {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				sequenceDao.incrementValue("KEY_1", 5);
			}
		});
		Sequence seq = sequenceDao.loadSequenceByKey("KEY_1");
		assertEquals(new Long(6), seq.getValue());
		
	}

}
