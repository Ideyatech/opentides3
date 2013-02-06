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

package org.opentides.exception;

/**
 * This exception is called when unexpected return value is received from a
 * method. Usually due to logical error in program implementation.
 * 
 * @author allanctan
 */
public class CodeGenerationException extends RuntimeException {

    /**
     * Generated class UID
     */
    private static final long serialVersionUID = -5571782216124026964L;

    /**
     * Default constructor.
     */
    public CodeGenerationException() {
        super();
    }

    /**
     * Inherited constructor from RuntimeException.
     * 
     * @param message - error message
     * @param cause - root cause
     */
    public CodeGenerationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Inherited constructor from RuntimeException.
     * 
     * @param message - error message
     */
    public CodeGenerationException(final String message) {
        super(message);
    }

    /**
     * Inherited constructor from RuntimeException.
     * 
     * @param cause - root cause
     */
    public CodeGenerationException(final Throwable cause) {
        super(cause);
    }

}
