/*!
 * Opentides 3.0 - Requires jQuery - http://jquery.com/   
 *    
 * Copyright 2007-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// helper functions
var opentides3 = (function() {
     var defaultMessages = {
         'no-results-found': 'No results found.',
         'summary-message': 'Displaying #start to #end of #total records',
         'are-you-sure-to-delete': 'Are you sure to delete #primary-value?',
         'result-seconds': '#time seconds'
     };
     return {
    	 /**
    	  * Returns true if current browser is support window.history which 
    	  * is new HTML5 standard.
    	  * @see: https://developer.mozilla.org/en-US/docs/DOM/Manipulating_the_browser_history
    	  * 
    	  * @returns boolean
    	  */
    	supportsHistory: function() {
   		  	return !!(window.history && history.pushState);
    	},
    	/**
    	 * Returns the current window location excluding the parameters. 
    	 * This is needed on all REST request to avoid conflict with initial url 
    	 * request with parameters.
    	 */
    	getPath: function(param) {
    		var path = window.location.pathname.replace(/\/$/,'');
    		return window.location.protocol + '//' + window.location.host + path;    		
    	},
    	/**
    	 * Gets value from the json object based on given path 
    	 * and removes any html tags or scripts. Use this method when 
    	 * writing json values into the page to avoid html injection.
    	 * 
    	 * @param json - json object containing the value
    	 * @param path - dot notation path reference to the value.
    	 * 
    	 */
    	getSafeValue: function(json, path) {
		    var i = 0,
	        path = path.split('.');	        
		    for (; i < path.length; i++) {
		    	json = json[path[i]];
		    }
		    if (json == null || json.length==0)
		    	return '';
		    else
		    	return $('<div/>').text(json).html();
    	},
    	/**
    	 * Retrieves the message based on the given code. 
    	 * 
    	 * Message resolution is as follows:
    	 *   (1) Check if element contains data attribute for the message. 
    	 *       For example, save code='summary-message' will look for 
    	 *       data-summary-message attribute on the element. 
    	 *   (2) If no message found, get default message.
    	 */
        getMessage: function(code, elem) { 
        	if ($(elem).length) {
        		msg = $(elem).data(code);
        		if (typeof(msg) !== 'undefined')
        			return msg;
        	}
        	return defaultMessages[code]; 
        },
        /**
         * Displays the message response received from the server side.
         * 
         */
        displayMessage: function(json) {        	
			var addMessage = function(elementClass, alertClass, message) {
				container = $('.'+elementClass);
				panel = container.children('.'+alertClass);
				if (panel.length === 0) {
					container.prepend("<div class='ot3-alert alert "+alertClass+"'><ul></ul></div>");
					panel = container.children('.'+alertClass);
					if (alertClass === 'alert-success') {
						panel.prepend('<button type="button" class="close" data-dismiss="modal">&times;</button>');
					}
				}
    			panel.children('ul').append('<li>'+message+'</li>');
			};
    		// display the message
    		$.each(json['messages'], function(i, message) {
    			if (message.type == 'error') {
	    			// displays error message (red, fixed)
    				addMessage(message.elementClass, 'alert-error', message.message);
    				if (typeof(message.fieldName) !== 'undefined' &&
    					message.fieldName.length > 0)
    					$("."+message.elementClass+" [name='"+message.fieldName+"']")
    						.closest('.control-group').addClass('error');
    			} else if (message.type == 'warning') {
    				// displays warning message (yellow, fixed)
    				addMessage(message.elementClass, 'alert-warning', message.message);
    			} else if (message.type == 'info') {
	    			// displays warning message (fixed, closable)
    				addMessage(message.elementClass, 'alert-success', message.message);
    			} else {
	    			// assume its notification
	    			// pops-up for a few seconds    				
    				$('.'+message.elementClass).notify({
    					message: { text: message.message }
    				}).show();
    			}
    			//TODO: Insert scrolling to message.elementClass code 
    			//      here in case error message is not visible.
    		}); //.each			
        },
        /**
         * Animate slide element to the left. 
         * Note: This animation works only with the following styles on container:
         *   - from,to element has "width:50%"
         *   - parent element has "width:200%"
         *   - super parent element has "overflow:hidden"
         * 
         * @param from - old element to be removed
         * @param to - new element to be displayed
         * 
         */
        slideLeft: function(from, to) {
        	from.after(to);
        	from.removeClass('pull-right');
        	to.removeClass('pull-right');
        	from.addClass('pull-left');
        	to.addClass('pull-left');
        	from.parent().css("margin-left","0");
        	from.stop(false, true).animate({
        	      marginLeft: "-100%"
        	    }, 600, 'swing', function() {
        	    	$(this).remove();
        	    });
        },        
        /**
         * Animate slide element to the right. 
         * Note: This animation works only with the following styles on container:
         *   - from,to element has "width:50%"
         *   - parent element has "width:200%"
         *   - super parent element has "overflow:hidden"
         * 
         * @param from - old element to be removed
         * @param to - new element to be displayed
         * 
         */
        slideRight: function(from, to) {        	
        	from.after(to);
        	from.removeClass('pull-left');
        	to.removeClass('pull-left');
        	from.addClass('pull-right');
        	to.addClass('pull-right');
        	from.parent().css("margin-left","-100%");
        	from.stop(false, true).animate({
        	      marginRight: "-50%"
        	    }, 300, 'swing', function() {
        	    	$(this).remove();
        	    }
        	);
        }

    };
})();

(function( $ ) {
	
	//*************************************
	// Opentides3 JQuery extensions
	//*************************************

	/**
	 * Shows the element on the given container.
	 */
	$.fn.page = function(options) {
		if ($.type(options) === 'string') {
			options = {'action':options};
		}
	    var settings = $.extend( {
		  'container'   : '',
	      'action'		: 'show'
	    }, options);

		return this.each(function() {
			var container = (settings.container === '') ? $(this).closest('.container') : $(settings.container);
			if (settings.action === 'show') {
				container.children().hide();
				$(this).show();
			} else if (settings.action === 'hide') {
				container.children().hide();
				container.children().not(this).show();
			}			
		});
	}
    /**
     * Binds the json object to the form.
     * Binding uses attribute 'name' for matching.
     * 
     * @param json - json object containing the values
     */
	
	$.fn.otbind = function(json) {  		
		return this.each(function() {
			var form = $(this);
			form.clearForm();
	        $.each(json,function(key,value) {
	            form.find("[name='"+key+"']").each(function() {
	            	var elem = $(this);
	            	var type = elem.attr('type');
		            if (value == null) value = '';
		            // do not bind password or file upload
		            if (type == 'text' || 
		            	type == 'hidden' || 
		            	elem.is('textarea') ) {
						// set the default value for the form
		            	var prime = toPrimitive(value);
		            	elem.val(prime);
					} else if (elem.is("select")) {
						var normValue = normalizeValue(value);
						elem.val(normValue);
					} else if (elem.attr('type') === 'checkbox') {
						// check the field if the data value is true/1
						if (jQuery.type(value) === 'boolean') {
							if (value==true)
								elem.prop('checked',true);
							else
								elem.prop('checked',false);
						} else {							
							var normValue = normalizeValue(value,true);
							if (jQuery.inArray(elem.attr('value'), normValue) >= 0)
								elem.prop('checked',true);
							else
								elem.prop('checked',false);
						} 
					} else  if (elem.attr('type') === 'radio') {
		            	var prime = toPrimitive(value);
						if (elem.attr('value')==prime)
							elem.prop('checked',true);
						else
							elem.prop('checked', false);
					}
	            })
	        });			
		});
	};
	
    /**
     * Clears all the input of the form.
     */
	$.fn.clearForm = function() {  
		return this.each(function() {
	    	$(this).find('input:text, input:password, input:file, select, textarea').val('');
	    	$(this).find('input:radio, input:checkbox').prop('checked',false).prop('selected',false);			
		});
    };
	
	/**
	 * 
	 * Converts the elements into RESTful CRUD element. This jquery plugin should 
	 * be used alongside standard opentides3 RESTful CRUD pages. While the 
	 * functionalities conform to standard REST protocol, a corresponding 
	 * BaseCrudController is expected receive the form submission.
	 * 
	 *     (1) look for <add>, on click, display retrieve default values 
	 *         and display <form>.  
     *     (1.1) when displaying check if <form> is modal, inline, on newpage.
     *     (2) look for <search>, convert inner forms to json based search.
   	 *     (2.1) display search results to <results> (must contain table)
   	 *     (2.2) within the results, look for <edit> and <remove> and attach events. see steps 1 and 1.1.
     *
	 * Parameters include:
	 *   search  	- element containing search criteria form. Default is '#search-panel'.
	 *   results    - element containing the results table. Default is '#results-panel'.
	 *   form	 	- element containing the form to display when adding/updating record. Default is '#form-panel'.
	 *   status     - element where messages are displayed. Default is '.status'.
	 *   paging     - element where paging is displayed. Default is '.paging'.
	 *   add		- elements where add new record action is triggered on click. Default is '.add-action'
	 *   edit		- elements where edit record action is triggered on click. Default is '.edit-action'
	 *   remove		- elements where delete record action is triggered on click. Default is '.remove-action'
	 *   
	 * In addition, elements within the form with the following attributes are processed:
	 *    - [data-submit='save']         - saves and close the panel 
	 *    - [data-submit='save-and-new'] - saves and clear the form
	 *    - [data-display-form='add']    - displayed only when adding new record
	 *    - [data-display-form='update'] - displayed only when updating record
	 *    
	 */
	
	$.fn.RESTful = function(options) {
		// extend the options with defaults
	    var settings = $.extend( {
		  'search'   	: '#search-body',
	      'form' 		: '#form-body',
	      'results'		: '#results-panel',
	      'status'		: '.status',
	      'pagination'	: '.pagination',
		  'add' 		: '.add-action',	      
		  'edit' 		: '.edit-action',	      
		  'remove'		: '.remove-action'	      
	    }, options);
		
		return this.each(function() {
			
  	    	// list of search forms
  	    	var searchForms = $(this).find(settings['search'] + ' form');
  	    	
  	    	// resultsPanel
  			var results = $(this).find(settings['results']);
  			
  			// panel containing the form
  			var form = $(this).find(settings['form']);

  			// add/edit form
  			var firstForm = $(this).find(settings['form'] + ' form:first');
  			
  			// status bar
  			var status = $(this).find(settings['status']);
  			
  			// pagination bar
  			var pagination = $(this).find(settings['pagination']);
  			
  			// body
  			var body = $(this);
  			
  			//submit button
  			var formSubmitButton = $(this).find("input[data-submit='save']");

  			if ($(this).find(settings['results'] + ' table tr').length <= 1) {
  		    	// no search results, hide the table and message
  		    	$(this).find(settings['results']).hide();
  		    	$(this).find(settings['status']).hide();
  		    	$(this).find(settings['pagination']).hide();
  		    } else {
  		    	// with search results, display the table and message
  		    	$(this).find(settings['results']).show();
  		    	$(this).find(settings['status']).show();
  		    	$(this).find(settings['pagination']).show();
  		    }	    

  		    // bind to popstate event for history tracking
  			if (opentides3.supportsHistory()) {
  				window.addEventListener("popstate", function(e) {
  					var state = e.state;
		    	    if (state != null ) {
		    	    	if (state.mode=='search') {
			    	    	var formElement = $('#'+state.formPath);
			    	    	formElement.deserialize(state.formData);
			    	    	displayResults(formElement, results, status, pagination, state.data);
			    	    } else if (state.mode == 'add' || state.mode == 'update') {
			    	    	displayForm(state.mode, body.find(state.form), state.action, state.data);
			    	    	form.page();
			    	    } else if (state.mode=='hide') {
			    	    	body.find(state.form).page('hide');
			    	    }
		    	    }
  				});
  			}  			
  			
  			// attach events to pagination (if displayed)
  			$(this).find('.pagination').each(function(){
  				// paging is already displayed, let's attach events to it.
  				// assume form used is the first visible form in the search panel.
  				var searchForm = $(settings['search']).find('form').filter(':visible:first');
  				$(this).find('li a').on("click", function() {
					var i = $(this).data('page');
					doSearch(searchForm, results, status, pagination, i);
					return false;
				});  				
  			});
  			
  			// handle all ajax errors
		    $(document).ajaxError(function(event, jqXHR, settings, exception) {
	        	$('.system-error .ot3-alert').remove();
	        	var message = 'Oopps... An unexpected error occurred. ';
	            if (jqXHR.status === 0) {
	            	message = "Cannot connect to server. Please verify your network connection.";
	            } else if (jqXHR.status == 404) {
	                message = 'Requested page not found. [Error: 404]';
	            } else if (jqXHR.status == 500) {
	                message = 'Internal Server Error. [Error: 500]';
	            } else if (exception === 'parsererror') {
	                message = 'Oopps... Unable to parse your JSON request.';
	            } else if (exception === 'timeout') {
	                message = 'Oopps... Server time-out error.';
	            } else if (exception === 'abort') {
	                message = 'Your request has been cancelled.';
	            }
            	opentides3.displayMessage({messages:[{
            		type: "error",
            		elementClass: "system-error",
            		message: message,
            	}]});
		    });

			/***********************************
			 * (1) look for <add>, on click, display retrieve default values and display <form>. 
			 ***********************************/  		   	 
	    	// attache events to add buttons
			// convert the search form to ajax search
  			$(this).find(settings['add']).on('click', function() {
	  			// add new record
  				var path = opentides3.getPath();
	  			$.getJSON(
	  					path+'/0', 				// url - new record
	  		    		'',						// empty data
	  	  		    	function(json) {		// callback
  		    				displayForm('add', form, path, json);
	  		    			if (form.hasClass('modal')) 
	  		    				form.modal();
	  		    			else if (form.hasClass('page')) {
	  		    				if (opentides3.supportsHistory()) {
		  		    	  			history.pushState({mode:'add', data:json, form:settings['form'], action:path}, 
		  		    	  				null, opentides3.getPath()+'/0');
	  		    				}
	  		    				form.page();
	  		    			}
	  		    		});
	  			return false;
			});
  			
  			firstForm.find("[data-submit='save'], [data-submit='save-and-new']").on("click", function() {
  				formSubmitButton = $(this);
  			});
	    	
  			firstForm.on("submit", function(e) {
	    		
  				e.preventDefault();
  				
	    		var firstForm = $(this);
	    		var button = formSubmitButton;
	    		
  				$.ajax( {
  					type:firstForm.attr('method'),		// method
  					url:firstForm.attr('action'), 		// url
  					data:firstForm.serialize(),			// data
  					success: function(json) {			// callback
  						firstForm.find('.ot3-alert').remove();
  						firstForm.find('.control-group').removeClass('error');
  		    			opentides3.displayMessage(json);
  		    			if (typeof(json.command) === 'object' &&
  		    					json.command.id > 0) {
  		    				// successfully saved
  		    				firstForm.clearForm();
  		    				if (button.data('submit')=='save') {
  		    					// hide modal
  		    					if (form.hasClass('modal')) 
  		  		    				form.modal('hide');
  		  		    			else if (form.hasClass('page')) {
	  		  		    			if (opentides3.supportsHistory()) {
	  		  		    				var url = opentides3.getPath().replace(/\/[0-9]+$/,'');
			  		    	  			history.pushState({mode:'hide', form:settings['form']}, 
			  		    	  				null, url);
		  		    				}
	  		  		    			// display the search page
	  		  		    			$(settings['search']).page();
  		  		    			}
  		    				}
	  		  				displayTableRow(results.find('table'), json.command);
  		    			}
  	  		    	},
  	  		    	dataType:'json'
  				});
	    	});
	    	
	    	firstForm.find("[data-dismiss='page']").on("click", function() {
	    		if (form.hasClass('page')) {
	    			if (opentides3.supportsHistory()) {
	    				var url = opentides3.getPath().replace(/\/[0-9]+$/,'');
	    	  			history.pushState({mode:'hide', form:settings['form']}, 
    	  				null, url);
	    			}
	    			$(settings['search']).page();
	    		}
	    	});
	    	
	    	// hide all alerts when form is displayed.
	    	form.on('show', function (e) {
	  			$(this).find('.ot3-alert').remove();
	  		});
	    	
 			/***********************************
 		     *  (1.1) when displaying check if <form> is modal, inline, on newpage.
 		     *  NOT YET IMPLEMENTED !!
 			 ***********************************/  		   	 
	    	
 			/***********************************
 		     *  (2) look for <search>, convert inner forms to json based search.
 			 ***********************************/  		   	 
			// convert the search form to ajax search
	    	searchForms.on('submit', function() {
	    		// search starts on page 1
	    		status.show();
				doSearch($(this), results, status, pagination, 1); 
	    		pagination.show();
	  			return false;
			});
	    	

 			/***********************************
	      	 * (2.2) within the results, look for <edit> and <remove> and attach events. see steps 1 and 1.1.
 			 ***********************************/
			// attach edit to all action-edit
	    	$(this).on("click", settings['edit'], function() {
				var id = $(this).data('id');
				var path = opentides3.getPath()+'/'+id;
				$.getJSON(
						path,	 								// url
	  		    		"",										// data
	  	  		    	function(json) {						// callback
							displayForm('update', form, path, json)
	  		    			if (form.hasClass('modal')) 
	  		    				form.modal();
	  		    			else if (form.hasClass('page')) {
	  		    				if (opentides3.supportsHistory()) {
		  		    	  			history.pushState({mode:'update', data:json, form:settings['form'], action:path}, 
		  		    	  				null, opentides3.getPath()+'/'+id);
	  		    				}
	  		    				form.page();
	  		    			}
	  		    		});
			});
			
			
			// attach remove to all remove-action
	    	$(this).on("click", settings['remove'], function() {
				var ref = $(this).data('primary-value');
				var id = $(this).data('id');
				var tableRow = $(this).closest('tr');
				var message = opentides3.getMessage('are-you-sure-to-delete', this);
				if (typeof(ref) != 'undefined' && ref.length > 0) {
					message = message.replace('#primary-value', ref);
				} else {
					message = message.replace('#primary-value', 'record');
				}
				bootbox.dialog(message, [{
				    "label" : "Remove",
				    "class" : "btn-danger",
				    "callback": function() {
				    	$.ajax({
						    url: opentides3.getPath()+'/'+id,
						    type: 'DELETE',
						    success: function(json) {
		  		    			opentides3.displayMessage(json);		    		  		    			
		  		    			tableRow.fadeOut(300, function(){ $(this).remove(); });	    		  		    			
						    },
						    dataType:'json'
						});
				    }
				}, {
				    "label" : "Cancel",
					 "class" : "btn"
				}]);
			})
			
			// add sort fields as hidden on the form
			if (searchForms.find('[name="orderOption"]').length == 0) 
				searchForms.append("<input type='hidden' name='orderOption' value=''/>");
			
			if (searchForms.find('[name="orderFlow"]').length == 0) 
				searchForms.append("<input type='hidden' name='orderFlow' value=''/>");
			
			// attach clear button functions
			searchForms.find('[data-submit="clear"]').on("click", function() {
				$(this).closest('form').clearForm();
				return false;
			});	
			
	    });
	}
	
	// private method to perform search 
	var doSearch = function(searchForm, results, status, pagination, page) {
		var data = searchForm.serialize() + "&p=" + page;
		    $.getJSON(
		    		opentides3.getPath(), 		// url
		    		data,						// data
	  		    	function(json) {			// callback
		    			if (opentides3.supportsHistory()) {
	  		    			//change the url
	  		    			var cleanUrl = '?' + data.replace(/[^&]+=\.?(?:&|$)/g, '');
	  		    			if (searchForm.attr('id').length === 0) {
	  		    				alert('id attribute is required for search form. Please check your html.');
	  		    			}
	  		    			history.pushState({mode:'search',data:json, formPath:searchForm.attr('id'),
	  		    				formData:searchForm.serialize()}, null, cleanUrl);	  		    				
		    			}
		    			displayResults(searchForm, results, status, pagination, json);  		    			
		    		}
		    );
	};
	
	/*****************************
	 * (2.1) display search results to <results> (must contain table)
	 *****************************/
	var displayResults = function(searchForm, results, status, pagination, json) {
		// ensure past animations are stopped
		if (results.find('table:animated').length > 0) {
			results.find('table:animated').stop().remove();
		}
		
		// show the search result status bar
		status.show();
		pagination.show();
		
		// show the results
		if (json['results'].length > 0) {
			results.show();
			
			// old table
			var oldTable = results.find('table:first');
			var oldPage = oldTable.data('page');
			
			// new table
			var newTable = oldTable.clone();
			newTable.find('tr').not('.table-header').remove();
			newTable.attr('data-page',json.currPage);
			
			// listed results column
			var listedNames = {};
			newTable.find('th').each(function(i, item) {
				listedNames[i] = $(item).data('fieldName');
			});
			
			// clear the table
			// add results as table row
			$.each(json['results'], function(i, result) {
				displayTableRow(newTable, result, listedNames);
			}); // each
			
			// now let's animate
			if (typeof(oldPage) === 'undefined' || 
					oldPage.length == 0) {
				// first page, no need to animate
				oldTable.after(newTable);
				oldTable.remove();				
			} else if (oldPage < json.currPage)
				opentides3.slideLeft(oldTable, newTable);
			else if (oldPage > json.currPage)
				opentides3.slideRight(oldTable, newTable);
			else {
				//can't classify (or equal), no need to animate
				oldTable.after(newTable);
				oldTable.remove();
			}
				
		} else {
			results.hide();  				
		}
		
		// look for status bar 
		status.each(function(i,elem) {
			if (json['results'].length == 0) {
				html = "<div class='alert alert-warning'>" +
					   opentides3.getMessage('no-results-found', elem) +
					   "</div>";
				$(elem).html(html);
				results.hide();
			} else {
				results.show();    				
				html = "<span class='records'>" +
						opentides3.getMessage('summary-message', elem)
					 	.replace('#start', json['startIndex']+1)
					 	.replace('#end',   json['endIndex']+1)
					 	.replace('#total', json['totalResults']) +
					 	"</span>";    					
				html = html + "<span class='searchTime'> (" + opentides3.getMessage('result-seconds', elem)
						.replace('#time',json['searchTime']/1000) + ")</span>";
				$(elem).html(html);  	  	  		    				
			}
		});
		// look for paging
		pagination.each(function(i,elem) {
			if (json['results'].length > 0 && (json['totalResults']/json['pageSize'] > 1)) {
				html = 	"<div class='pagination pagination-centered'>" +
						"<ul><li class='ot3-firstPage'><a href='javascript:void(0)' data-page='1'>&lt;&lt;</a></li>" + 						// first page
						"<li class='ot3-prevPage'><a href='javascript:void(0)' data-page='"+(json['currPage']-1)+"'>&lt;</a></li>";		 	// prev page
				for (var i=json['startPage'];i<=json['endPage'];i++) {
					html = html +"<li class='ot3-page-"+i+"'><a href='javascript:void(0)' data-page='"+i+"'>"+i+"</a></li>";				// pages				
				}
				html = html +
						"<li class='ot3-nextPage'><a href='javascript:void(0)' data-page='"+(json['currPage']+1)+"'>&gt;</a></li>" +		// next page
						"<li class='ot3-lastPage'><a href='javascript:void(0)' data-page='"+json['endPage']+"'>&gt;&gt;</a></li>" +			// last page
						"</ul></div>";	
				$(elem).html(html);
				// set active page
				$('.ot3-page-'+json['currPage']).addClass('active');
				$('.ot3-page-'+json['currPage']).html('<span>'+$('.ot3-page-'+json['currPage']+' a').html()+'</span>');
				
				// if first page, disable first and prev page
				if (json['currPage']==1) {
					$('.ot3-firstPage').addClass('disabled');
					$('.ot3-prevPage').addClass('disabled');
					$('.ot3-firstPage').html('<span>&lt;&lt;</span>');
					$('.ot3-prevPage').html('<span>&lt;</span>');
				}
				// if last page, disable last and next page
				if (json['currPage']==json['endPage']) {
					$('.ot3-lastPage').addClass('disabled');
					$('.ot3-nextPage').addClass('disabled'); 
					$('.ot3-lastPage').html('<span>&gt;&gt;</span>');
					$('.ot3-nextPage').html('<span>&gt;</span>');
				} 
				$(elem).find('li a').on("click", function() {
					var i = $(this).data('page');
					doSearch(searchForm, results, status, pagination, i);
				})
			} else {
				$(elem).html('');
			}
		});
	};
	
	var displayTableRow = function(resultsTable, result, listedNames) {
    	if (typeof(listedNames) === 'undefined') {
    		listedNames = { };
    		// get the listed results column
			resultsTable.find('th').each(function(i, item) {
				listedNames[i] = $(item).data('fieldName');
			});
    	}
    	var tableRow = resultsTable.find('tr[data-id="'+result['id']+'"]');
    	if (tableRow.length > 0) {
    		// update the record
    		var row = "";
			$.each(listedNames, function(j, fieldName) {
				if (fieldName=='ot3-controls') {
					row = row + "<td> <i class='icon-pencil edit-action' data-id='"+result['id']+"'/> " +
								"<i class='icon-trash remove-action' data-id='"+result['id']+"'/></td>";
				} else {
    				row = row + '<td>'+opentides3.getSafeValue(result,fieldName)+'</td>';  	  		    					
				}
			});
			tableRow.html(row);
    	} else {
    		// add new record
    		var row = "<tr data-id='"+result['id']+"'>";
			$.each(listedNames, function(j, fieldName) {
				if (fieldName=='ot3-controls') {
					row = row + "<td> <i class='icon-pencil edit-action' data-id='"+result['id']+"'/> " +
								"<i class='icon-trash remove-action' data-id='"+result['id']+"'/></td>";
				} else {
    				row = row + '<td>'+opentides3.getSafeValue(result,fieldName)+'</td>';  	  		    					
				}
			});
			row = row + "</tr>";
			resultsTable.append(row);
    	}
    };
    
    /**
     * Private helper that displays the add/edit form in the page
     */
	var displayForm = function(mode, form, action, json) {
		var firstForm = form.find('form:first');
		if (mode==='update') {
			firstForm.attr('action',action);
	  		firstForm.attr('method','put');
			firstForm.otbind(json);   					
			form.find('.ot3-add').hide();	    			
			form.find('.ot3-update').show();			
		} else if (mode === 'add') {
			firstForm.attr('action',action);
	  		firstForm.attr('method','post');
			firstForm.otbind(json);   			
			form.find('.ot3-update').hide();
			form.find('.ot3-add').show();
		}
	};
	
	/**
	 * Private helper method to convert an object to primitive value.
	 * Used when converting json objects to single value string.
	 */
	var toPrimitive = function(value) {
		var type = jQuery.type(value);
		if (type === 'object') {
			if (value.key != null)
				return value.key;
			else if (value.id != null)
				return value.id;
			else {
				alert('Unable to convert object ['+value+'] to its primitive form.');				
				return '';				
			}
		} else
			return value;
	}
	
	/**
	 * Private helper that ensures value is always a single dimension array.
	 * If value is an object, key is used, otherwise id is used.
	 * Otherwise, error occurs.
	 */ 
	var normalizeValue = function(value, toArray) {
		if (jQuery.isEmptyObject(value))
			return '';
		var prime = toPrimitive(value);
		var type = jQuery.type(prime);
		if ( (type === 'string') || 
			 (type === 'boolean')||
			 (type === 'number') ) {
			// value is primitive, return as is.
			if (typeof(toArray) != 'undefined' && toArray==true)
				return [prime];
			else
				return prime;
		}
		if (type === 'array') {
			var arr = [];
			$(prime).each(function(i, val) {				
				arr.push(toPrimitive(val));
			});					
			return arr;
		}
		return value;
	}
})( jQuery );