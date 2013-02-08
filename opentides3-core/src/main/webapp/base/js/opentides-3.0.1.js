/*!
   Opentides 3.0 - Requires jQuery - http://jquery.com/   
  
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
var opentides3 = (function() {
     var defaultMessages = {
         'no-results-found': 'No results found.',
         'summary-message': 'Displaying #start to #end of #total records',
         'are-you-sure-to-delete': 'Are you sure to delete #primary-value?'
     };
     return {
    	getSafeValue: function(object, path) {
		    var i = 0,
	        path = path.split('.');	        
		    for (; i < path.length; i++) {
		    	object = object[path[i]];
		    }
		    return $('<div/>').text(object).html();
    	},
        getMessage: function(code, elem) { 
        	if ($(elem).length) {
        		msg = $(elem).data(code);
        		if (msg != undefined)
        			return msg;
        	}
        	return defaultMessages[code]; 
        },
        displayMessage: function(json) {        	
			var addMessage = function(elementClass, alertClass, message) {
				container = $('.'+elementClass);
				panel = container.children('.'+alertClass);
				if (panel.length === 0) {
					container.prepend("<div class='ot3-alert alert "+alertClass+"'><ul></ul></div>");
					panel = container.children('.'+alertClass);
					if (alertClass=='alert-success') {
						panel.prepend('<button type="button" class="close" data-dismiss="modal">&times;</button>');
					}
				}
    			panel.children('ul').append('<li>'+message+'</li>');
			};
			// remove all alert within the form
			$('.ot3-alert').remove();
    		// display the message
    		$.each(json['messages'], function(i, message) {
    			if (message.type == 'error') {
	    			// displays error message (red, fixed)
    				addMessage(message.elementClass, 'alert-error', message.message);
    				message.objectName
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
    		}); //.each			
        },
        populateForm: function(form,json) {
        	if(json.id > 0) {
        		form.attr("action",json.id);
        		form.attr("method","put");
        	} else {
        		form.attr("action","");
        		form.attr("method","post");
        	}
            $.each(json,function(key,value) {
                var elem = form.find("[name='"+key+"']");
                // do not bind password or file upload
                if (elem.attr('type') == 'text' || elem.attr('type') == 'hidden' || 
                		elem.is('textarea') || elem.is("select")) {
					// set the default value for the form
                	elem.val(value);
				} else if (elem.attr('type') == 'checkbox') {
					// check the field if the data value is true/1
					if ( value == 1 ) 
						elem.attr("checked", "checked");
					else
						elem.attr("checked","");
				} else  if (elem.attr('type') == 'radio') {
					// TODO: Add bind code here
				}
           });
        },
        clearForm: function(form) {
        	form.find('input:text, input:password, '+
    						'input:file, select, textarea').val('');
        	form.find('input:radio, input:checkbox')
		    				.removeAttr('checked').removeAttr('selected');        	
        },
        displayTableRow: function(tableElement, result, listedNames) {
        	if (typeof(listedNames) === 'undefined') {
        		listedNames = { };
        		// get the listed results column
				tableElement.find('th').each(function(i, item) {
					listedNames[i] = $(item).data('fieldName');
				});
        	}
        	var tableRow = tableElement.find('tr[data-id="'+result['id']+'"]');
        	if (tableRow.length > 0) {
        		// update the record
        		var row = "";
    			$.each(listedNames, function(j, fieldName) {
    				if (fieldName=='ot3-controls') {
    					row = row + "<td> <i class='icon-edit' data-id='"+result['id']+"'/> " +
    								"<i class='icon-remove' data-id='"+result['id']+"'/></td>";
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
    					row = row + "<td> <i class='icon-edit' data-id='"+result['id']+"'/> " +
    								"<i class='icon-remove' data-id='"+result['id']+"'/></td>";
    				} else {
        				row = row + '<td>'+opentides3.getSafeValue(result,fieldName)+'</td>';  	  		    					
    				}
    			});
    			row = row + "</tr>";
    			tableElement.append(row);
        	}
        }
    };
})();

(function( $ ){
	/**
	 * Converts a form into json based submission for opentides.
	 * Use this method in conjunction with BaseCrudController of opentides3.
	 * @param message-panel   - location where messages are displayed. Default is #message-panel.
	 * @param results-panel   - location where results are displayed. Default is #results-panel.
	 * @param form-panel      - panel containing the form. Default is #form-panel.
	 * 
	 */  
	$.fn.jsonForm = function(options) {  
		// extend the options with defaults
	    var settings = $.extend( {
	      'message-panel' : '#message-panel',
	      'results-panel' : '#results-panel',
	      'form-panel'	  : '#form-panel'
	    }, options);
	    return this.each(function() {
	    	var addEditForm = $(settings['form-panel']).find('form:first');
	    	var tableElement = $(settings['results-panel'] + ' table');
	    	// ensure form exist within the panel
	    	if (addEditForm.length == 0) {
	    		alert("No form found on "+settings['form-panel']);
	    		return;
	    	}
	    	
	    	addEditForm.find("[data-submit='save']").on("click", function() {
	    		var type="POST";
	    		if (addEditForm.attr('action').length > 0)
	    			type = "PUT";
	    		
  				$.ajax( {
  					type: type,
  					url:addEditForm.attr('action'), 	// url
  					data:addEditForm.serialize(),		// data
  					success: function(json) {			// callback
  		    			opentides3.displayMessage(json);
  		    			if (typeof(json.command) === 'object' &&
  		    					json.command.id > 0) {	  		    				
	  		  				$(settings['form-panel']).modal('hide');
	  		  				opentides3.displayTableRow(tableElement, json.command);
  		    			}
  	  		    	},
  	  		    	dataType:'json'
  				});	  		    		
	    	});
	    	
	    	addEditForm.find("[data-submit='save-and-new']").on("click", function(){
	    		var type="POST";
	    		if (addEditForm.attr('action').length > 0)
	    			type = "PUT";
  				$.ajax( {
  					type: type,
  					url:addEditForm.attr('action'), 		// url
  					data:addEditForm.serialize(),			// data
  					success: function(json) {				// callback
  		    			opentides3.displayMessage(json);
  		    			if (typeof(json.command) === 'object' &&
  		    					json.command.id > 0) {
	  		    			opentides3.clearForm(addEditForm);
	  		    			opentides3.displayTableRow(tableElement, json.command);
  		    			}
  	  		    	},
  	  		    	dataType:'json'
  				});		
	    	});

	    	$(settings['form-panel']).on('show', function (e) {
	  			$(this).find('.ot3-alert').remove();
	  		});
	  		
			// convert the search form to ajax search
	  		$(this).on("click", function() {
	  			// add new record
	  			$.getJSON(
	  		    		"0", 									// url - new record
	  		    		"",										// data
	  	  		    	function(json) {						// callback
	  		    			opentides3.populateForm(addEditForm, json);
	  		    			$(settings['form-panel']).find('[data-form-display="add"]').show();	    			
	  		    			$(settings['form-panel']).find('[data-form-display="update"]').hide();	    			
	  			  			$(settings['form-panel']).modal();
	  		    		});
	  			return false;
			});
	    });		
	};
	
	/**
	 * Converts the search panel into json search.
	 * Parameters include:
	 *   message-panel   - location where messages are displayed. Default is #message-panel.
	 *   results-panel   - location where results are displayed. Default is #results-panel.
	 *   edit-form-panel - panel to display when updating record. Default is #form-panel.
	 */
	$.fn.jsonSearch = function(options) {
		// extend the options with defaults
	    var settings = $.extend( {
	      'message-panel'  : '#message-panel',
	      'results-panel'  : '#results-panel',
	      'edit-form-panel': '#form-panel'
	    }, options);
	    
	    if ($(settings['results-panel'] + ' table tr').length <= 1) {
	    	$(settings['results-panel']).hide();
	    	$(settings['message-panel']).hide();
	    } else {
	    	$(settings['message-panel']).show();	    	
	    	$(settings['results-panel']).show();	    	
	    	
	    }
		
	    return this.each(function() { 
	    	var searchForm = $(this);
			var tableElement = $(settings['results-panel'] + ' table');
			var displayResults = function(json) {
				var listedNames = {};
				// get the listed results column
				tableElement.find('th').each(function(i, item) {
					listedNames[i] = $(item).data('fieldName');
				});
				// clear the table
				tableElement.find('tr').not('.table-header').remove();
	    		// add results as table row
	    		$.each(json['results'], function(i, result) {
	    			opentides3.displayTableRow(tableElement, result, listedNames);
	    		}); // each	    		
				
	    		$(settings['message-panel']).show();
				
	    		// look for paging 
	    		$('.ot3-paging').each(function(i,elem) {
	    			if ($(elem).data('display-summary')) {
		    			if (json['results'].length == 0) {
		    				html = "<div class='alert alert-warning'>" +
		    					   opentides3.getMessage('no-results-found', elem) +
		    					   "</div>";
		    				$(elem).html(html);
		    	    		$(settings['results-panel']).hide();
		    			} else {
		    	    		$(settings['results-panel']).show();
		    				html = opentides3.getMessage('summary-message', elem)
								 .replace('#start', json['startIndex']+1)
								 .replace('#end',   json['endIndex']+1)
								 .replace('#total', json['totalResults']);
		    				$(elem).html(html);  	  	  		    				
		    			}
		    		}
	    			if ($(elem).data('display-pagelinks')) {
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
		    				$('.ot3-firstPage a').on("click", function() {
		    					doSearch(searchForm, 1);
		    				})
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
		    					doSearch(searchForm, i);	    					
		    				})		    				
		    			} else {
		    				$(elem).html('');	    				
		    			} 	  		    				
	    			}  	  		    				 	  		    			
	    		});			
			}
						
			var doSearch = function(formElement, page) {
				var data = formElement.serialize() + "&p=" + page;
	  		    $.getJSON(
	  		    		formElement.attr('action'), 			// url
	  		    		data,									// data
	  	  		    	function(json) {						// callback
	  		    			//change the url
	  		    			history.pushState(null, null, '?' +
	  		    					data.replace(/[^&]+=\.?(?:&|$)/g, '') );
	  		    			// show the results
	  		    			displayResults(json);
	  		    		}
	  		    );			
			}	    	
			// convert the search form to ajax search
	    	searchForm.on("submit", function() {
				doSearch(searchForm, 1);
	  			return false;
			});

			// attach edit to all icon-edit
			tableElement.on("click", ".icon-edit", function() {
				var id = $(this).data('id');
				$.getJSON(
	  		    		id,	 									// url
	  		    		"",										// data
	  	  		    	function(json) {						// callback
	  		    			var addEditForm = $(settings['edit-form-panel']).find('form:first');
	  		    			opentides3.populateForm(addEditForm, json);
	  		    			$(settings['edit-form-panel']).find('[data-form-display="add"]').hide();	    			
	  		    			$(settings['edit-form-panel']).find('[data-form-display="update"]').show();	    			
	  			  			$(settings['edit-form-panel']).modal();
	  		    		});	    					
			});
			
			
			// attach delete to all icon-remove
			tableElement.on("click", ".icon-remove", function() {
				var ref = $(this).data('primary-value');
				var id = $(this).data('id');
				var tableRow = $(this).closest('tr');
				var message = opentides3.getMessage('are-you-sure-to-delete', this);
				if (ref != undefined && ref.length > 0) {
					message = message.replace('#primary-value', ref);
				} else {
					message = message.replace('#primary-value', 'record');
				}
				if (confirm(message)) {
					$.ajax({
					    url: id,
					    type: 'DELETE',
					    success: function(json) {
	  		    			opentides3.displayMessage(json);		    		  		    			
	  		    			tableRow.fadeOut(300, function(){ $(this).remove(); });	    		  		    			
					    },
					    dataType:'json'
					});
				}
			})
			// add sort fields as hidden on the form
			if (searchForm.find('[name="orderOption"]').length == 0) 
				searchForm.append("<input type='hidden' name='orderOption' value=''/>");
			if (searchForm.find('[name="orderFlow"]').length == 0) 
				searchForm.append("<input type='hidden' name='orderFlow' value=''/>");
			
			// attach clear button function
			searchForm.find('[data-submit="clear"]').on("click", function() {
				opentides3.clearForm(searchForm);
				return false;
			});			
			searchForm.find('[data-submit="search"]').on("click", function() {
				$(this).closest('form').submit();
				return false;
			});			
	    });
	}
})( jQuery );