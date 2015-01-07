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

package org.opentides.bean;

import java.util.ArrayList;
import java.util.List;

import org.opentides.web.json.Views;

import com.fasterxml.jackson.annotation.JsonView;

public class SearchResults<T> {

    /** 
     * Number of matching results 
     */
	@JsonView(Views.SearchView.class)	
    private long totalResults;
    
    /** 
     * Current page for display 
     */
	@JsonView(Views.SearchView.class)
    private int currPage;
    
    /** 
     * Number of records per page 
     */
	@JsonView(Views.SearchView.class)
    private int pageSize;
    
    /** 
     * Number of links to display 
     */
	@JsonView(Views.SearchView.class)
    private int numLinks;
    
    /** 
     * Duration of search time in seconds 
     */
	@JsonView(Views.SearchView.class)
    private long searchTime;
	
	/** list containing matching results */
	@JsonView(Views.SearchView.class)
	private List<T> results;
	
	/** Messages to be displayed */
	@JsonView(Views.SearchView.class)	
	private List<MessageResponse> messages;
		
	public SearchResults() {
		results = new ArrayList<T>();
		messages = new ArrayList<MessageResponse>();
	}

	public SearchResults(int pageSize, int numLinks) {
        this.pageSize = pageSize;
        this.numLinks = numLinks;
		results = new ArrayList<T>();
		messages = new ArrayList<MessageResponse>();
	}

    /**
     * Computes for starting record number based on current page and page size.
     * 
     * @return int - start record index
     */
	@JsonView(Views.SearchView.class)	
    public final int getStartIndex() {
        int start = (this.currPage - 1) * this.pageSize;
        if (start < 0) {
            // ensure minimum
            start = 0;
        }
        if ((start + this.pageSize) > this.totalResults) {
            // ensure maximum
            start = (int) (this.totalResults / this.pageSize) * this.pageSize;
        }
        return start;
    }

    /**
     * Computes for ending record number based on current page contents
     * 
     * @return int - end record index
     */
	@JsonView(Views.SearchView.class)	
    public final int getEndIndex() {
        int endIndex = this.getStartIndex() + this.getDisplayedResultsCount() - 1;
        if (endIndex < 0) {
            endIndex = 0;
        }
        return endIndex;
    }

    /**
     * Total number of pages
     * 
     * @return int - total number of pages
     */
	@JsonView(Views.SearchView.class)	
    public final int getTotalPages() {
		if (this.pageSize == 0) 
			return 1;
        int totalPages = (int) (this.totalResults / this.pageSize);
        if ((this.totalResults % this.pageSize) != 0) {
            totalPages += 1;
        }
        return totalPages;
    }

    /**
     * Get starting page number
     * 
     * @return int - start page
     */
	@JsonView(Views.SearchView.class)	
    public final int getStartPage() {
        int startPage = this.currPage - (this.numLinks / 2);
        // check for empty results
        if (this.getTotalResults() == 0) {
            return 0;
        }
        // check for maximum
        final int totalPages = this.getTotalPages();
        if ((startPage + (this.numLinks - 1)) > totalPages) {
            // we will exceed maximum....
            startPage = totalPages - (this.numLinks - 1);
        }
        // check for minimum
        if (startPage < 1) {
            startPage = 1;
        }
        return startPage;
    }

    /**
     * Get starting page number
     * 
     * @return int - start page
     */
	@JsonView(Views.SearchView.class)	
    public final int getEndPage() {
        int endPage = this.getStartPage() + (this.numLinks - 1);
        final int totalPages = this.getTotalPages();
        if (endPage > totalPages) {
            endPage = totalPages;
        }
        return endPage;
    }

    /**
     * Getter method for totalResults.
     *
     * @return the totalResults
     */
    public final long getTotalResults() {
        return this.totalResults;
    }

    /**
     * Setter method for totalResults.
     *
     * @param totalResults the totalResults to set
     */
    public final void setTotalResults(final long totalResults) {
        this.totalResults = totalResults;
    }

    /**
     * Getter method for currPage.
     *
     * @return the currPage
     */
    public final int getCurrPage() {
        return this.currPage;
    }

    /**
     * Setter method for currPage.
     *
     * @param currPage the currPage to set
     */
    public final void setCurrPage(final int currPage) {
        this.currPage = currPage;
    }

    /**
     * Getter method for pageSize.
     *
     * @return the pageSize
     */
    public final int getPageSize() {
        return this.pageSize;
    }

    /**
     * Setter method for pageSize.
     *
     * @param pageSize the pageSize to set
     */
    public final void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Getter method for numLinks.
     *
     * @return the numLinks
     */
    public final int getNumLinks() {
        return this.numLinks;
    }

    /**
     * Setter method for numLinks.
     *
     * @param numLinks the numLinks to set
     */
    public final void setNumLinks(final int numLinks) {
        this.numLinks = numLinks;
    }

    /**
     * Getter method for searchTime.
     *
     * @return the searchTime
     */
    public final long getSearchTime() {
        return this.searchTime;
    }

    /**
     * Setter method for searchTime.
     *
     * @param searchTime the searchTime to set
     */
    public final void setSearchTime(final long searchTime) {
        this.searchTime = searchTime;
    }
    
	/**
	 * Counts the total number of results returned.
	 * @return
	 */
    @JsonView(Views.SearchView.class)	
	public final int getDisplayedResultsCount() {
		return results.size();
	}	
    
    /**
     * Adds record to result.
     * @param row
     */
	public final void addResults(T row) {
		results.add(row);		
	}
	
	/**
	 * Adds multiple records to result.
	 * @param rows
	 */
	public final void addResults(List<T> rows) {
		results.addAll(rows);
	}
	
	/**
	 * Returns the result.
	 * @return
	 */
	public final List<T> getResults() {
		return results;
	}

	/**
	 * @return the messages
	 */
	public final List<MessageResponse> getMessages() {
		return messages;
	}
	
	/**
	 * Adds a message to be displayed.
	 * @param message
	 */
	public final void addMessage(MessageResponse message) {
		messages.add(message);
	}

}
