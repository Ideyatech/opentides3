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

package org.opentides.persistence.evolve;

import org.springframework.transaction.annotation.Transactional;


/**
 * @author allantan
 *
 */
public interface DBEvolve extends Comparable<DBEvolve>{
	/**
	 * Actual database evolve script operations.
	 */
	@Transactional
	public void execute();
	/**
	 * Returns the description of evolve script.
	 * @return
	 */
	public String getDescription();
	/**
	 * Returns the version of this evolve script.
	 * Ensures that execution of evolve script is in proper
	 * sequence.
	 * @return
	 */
	public int getVersion();
}
