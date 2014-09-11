package org.opentides.dao;

import org.opentides.bean.Sequence;

/**
 * 
 * @author gbusok
 */
public interface SequenceDao extends BaseEntityDao<Sequence, Long> {
	
	/**
	 * Increment the value of the sequence.
	 * @return
	 */
	public Long incrementValue(String key);
	
	/**
	 * Increments the value of the sequence by the given size of step.
	 * @param key
	 * @param step
	 * @return
	 */
	public Long incrementValue(String key, int step);
	
	/**
	 * 
	 * @param key
	 * @param step
	 * @param threadSafe
	 * @return
	 */
	public Long incrementValue(String key, int step, boolean threadSafe);
	
	/**
	 * Get sequence by key
	 * @param key
	 * @return
	 */
	public Sequence loadSequenceByKey(String key);

}
