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
		'no-results-found' : 'No results found.',
		'summary-message' : 'Displaying #start to #end of #total records',
		'are-you-sure-to-delete' : 'Are you sure to delete #primary-value?',
		'result-seconds' : '#time seconds'
	};
	
	var entityMap = {
	    "&": "&amp;",
	    "<": "&lt;",
	    ">": "&gt;",
	    '"': '&quot;',
	    "'": '&#39;',
	    "/": '&#x2F;'
	};

	return {
		/**
		 * Returns true if current browser is support window.history which is
		 * new HTML5 standard.
		 * 
		 * @see: https://developer.mozilla.org/en-US/docs/DOM/Manipulating_the_browser_history
		 * 
		 * @returns boolean
		 */
		supportsHistory : function() {
			return !!(window.history && history.pushState);
		},
		/**
		 * Returns the current window location excluding the parameters. This is
		 * needed on all REST request to avoid conflict with initial url request
		 * with parameters.
		 */
		getPath : function(param) {
			var path = window.location.pathname.replace(/\/$/, '');
			return window.location.protocol + '//' + window.location.host
					+ path;
		},
		/**
		 * Gets value from the json object based on given path and removes any
		 * html tags or scripts. Use this method when writing json values into
		 * the page to avoid html injection.
		 * 
		 * @param json -
		 *            json object containing the value
		 * @param path -
		 *            dot notation path reference to the value.
		 * @param escape - if true, value will be escaped
		 * 
		 */
		getValue : function(json, path, escape) {
			var i = 0, path = path.split('.');
			for (; i < path.length; i++) {
				if (json == null || json.length == 0)
					return '';
				json = json[path[i]];
			}
			if (json == null || json.length == 0)
				return '';
			else {
				if (typeof (escape) === 'undefined' || typeof (escape) === 'null')
					escape = true;
				if (escape == true && typeof (json) === 'string')
					return $('<div/>').text(json).html();
				else
					return json;
			}
		},
		
		/**
		 * Escapes string to be html safe.
		 * 
		 * @param string
		 * @returns
		 */
		escapeHtml : function(string) {
		    return String(string).replace(/[&<>"'\/]/g, function (s) {
		      return entityMap[s];
		    });
		},

		/**
		 * Retrieves the message based on the given code.
		 * 
		 * Message resolution is as follows: (1) Check if element contains data
		 * attribute for the message. For example, save code='summary-message'
		 * will look for data-summary-message attribute on the element. (2) If
		 * no message found, get default message.
		 */
		getMessage : function(code, elem) {
			if ($(elem).length) {
				msg = $(elem).data(code);
				if (typeof (msg) !== 'undefined')
					return msg;
			}
			return defaultMessages[code];
		},
		/**
		 * Displays the message response received from the server side.
		 * 
		 */
        displayMessage: function(json, container, noClean) {
        	if(container && !noClean) {
        		//remove messages already displayed in the container
    			container.find('.control-group').each(function(){
    				$(this).removeClass('warning info success error');
    				$(this).find('.help-inline').remove();
    			});
    			container.find('.alert').remove();
        	}
        	// display the message
    		$.each(json['messages'], function(i, message) {

    			if (message.fieldName) {
    				
    				var element = container.find('*[name="'+message.fieldName+'"]:last');
        			element.closest('.control-group').addClass(message.type);
        			element.closest('div').append("<span class='help-inline'><small>" + message.message + "</small></span>");
        			
        			//TODO: Insert scrolling to message.elementClass code 
        			//      here in case error message is not visible.
    			
    			} else {
    				if(message.type == 'notification') {
    					$('.notifications').notify({ message: message.message, type: 'success' }).show();
    				} else {
    					if(container) {
    						container.find('.message-container').prepend("<div class='alert alert-" + message.type + "'>"+message.message+"</div>");
    					} else {
    						$('.notifications').notify({ message: message.message, type: message.type }).show();
    					}
    				}
    			}
    		});
    				
        },
		/**
		 * Templating function extracted from underscore but converted as jquery
		 * plugin.
		 * 
		 * JavaScript micro-templating, similar to John Resig's implementation.
		 * Underscore templating handles arbitrary delimiters, preserves
		 * whitespace, and correctly escapes quotes within interpolated code.
		 */
		template : function(text, data) {
			// For this implementation, we are using mustache-style template
			// delimeters.
			
			var settings = {
				evaluate :    /\{\{\-([\s\S]+?)\}\}/g,
				interpolate : /\{\{\=([\s\S]+?)\}\}/g,
				escape :      /\{\{([\s\S]+?)\}\}/g
			};

			// When customizing `settings`, if you don't want to define an
			// interpolation, evaluation or escaping regex, we need one that is
			// guaranteed not to match.
			var noMatch = /(.)^/;

			// Certain characters need to be escaped so that they can be put
			// into a string literal.
			var escapes = {
				"'" : "'",
				'\\' : '\\',
				'\r' : 'r',
				'\n' : 'n',
				'\t' : 't',
				'\u2028' : 'u2028',
				'\u2029' : 'u2029'
			};

			var escaper = /\\|'|\r|\n|\t|\u2028|\u2029/g;
			
			var render;

			// Combine delimiters into one regular expression via
			// alternation.
			var matcher = new RegExp([ (settings.evaluate || noMatch).source,
					(settings.interpolate || noMatch).source,
					(settings.escape || noMatch).source ].join('|')
					+ '|$', 'g');

			// Compile the template source, escaping string literals
			// appropriately.
			var index = 0;
			var source = "__p+='";
			
			text.replace(matcher, function(match, evaluate, interpolate,
					escape, offset) {
				source += text.slice(index, offset).replace(escaper,
						function(match) {
							return '\\' + escapes[match];
						});

				if (evaluate) {
					source += "'+\n" + evaluate + ";\n__p+='";
				}
				if (interpolate) {
					source += "'+\n((__t=(" + interpolate
							+ "))==null?'':__t)+\n'";
				}
				if (escape) {
					var escapeHtml = true;
					if(escape.indexOf('&') == 0) {
						//to support unescaping of html tags using &
						escape = escape.substring(1);
						escapeHtml = false;
					}
					source += "'+\n((__t=(" + escape
							+ "))==null?'':" + (escapeHtml ? "opentides3.escapeHtml(__t))+\n'" : "__t)+\n'");					
				}
				index = offset + match.length;
				return match;
			});
			source += "';\n";

			// If a variable is not specified, place data values in local
			// scope.
			if (!settings.variable)
				source = 'with(obj||{}){\n' + source + '}\n';
			
			source = "var __t,__p='',__j=Array.prototype.join,"
					+ "print=function(){__p+=__j.call(arguments,'');};\n"
					+ source + "return __p;\n";

			try {
				render = new Function(settings.variable || 'obj', '_',
						source);
			} catch (e) {
				e.source = source;
				throw e;
			}

			if (data)
				return render(data);
			var template = function(data) {
				return render.call(this, data);
			};

			// Provide the compiled function source as a convenience for
			// precompilation.
			template.source = 'function(' + (settings.variable || 'obj')
					+ '){\n' + source + '}';
			// update the element with the given template
			return template;
		},
		/**
		 * Animate slide element to the left. Note: This animation works only
		 * with the following styles on container: - from,to element has
		 * "width:50%" - parent element has "width:200%" - super parent element
		 * has "overflow:hidden"
		 * 
		 * @param from -
		 *            old element to be removed
		 * @param to -
		 *            new element to be displayed
		 * @param remove -
		 *            removes the element if true, otherwise, hide only 
		 * 
		 */
		slideLeft : function(from, to, remove) {
			from.after(to);
			from.removeClass('pull-right');
			to.removeClass('pull-right');
			from.addClass('pull-left');
			to.addClass('pull-left');
			from.parent().css("margin-left", "0");
			from.css("margin-left","0");
			to.show();
			from.stop(false, true).animate({
				marginLeft : "-100%"
			}, 600, 'swing', function() {
				if (remove == true)
					$(this).remove();
				else
					$(this).hide();
			});
		},
		/**
		 * Animate slide element to the right. Note: This animation works only
		 * with the following styles on container: - from,to element has
		 * "width:50%" - parent element has "width:200%" - super parent element
		 * has "overflow:hidden"
		 * 
		 * @param from -
		 *            old element to be removed
		 * @param to -
		 *            new element to be displayed
		 * @param remove -
		 *            removes the element if true, otherwise, hide only
		 *            
		 */
		slideRight : function(from, to, remove) {
			from.after(to);
			from.removeClass('pull-left');
			to.removeClass('pull-left');
			from.addClass('pull-right');
			to.addClass('pull-right');
			from.parent().css("margin-left", "-100%");
			from.css("margin-right","0");
			to.show();			
			from.stop(false, true).animate({
				marginRight : "-50%"
			}, 300, 'swing', function() {
				if (remove == true)
					$(this).remove();
				else
					$(this).hide();

			});
		},
		
		/**
		 * Replaces the element without animation.
		 * 
		 * @param from -
		 *            old element to be removed
		 * @param to -
		 *            new element to be displayed
		 * @param remove -
		 *            removes the element if true, otherwise, hide only
		 *            
		 */
		replace : function(from, to, remove) {
			if (remove == true) {
				from.remove();
			} else {
				from.hide();
			}
			to.show();			
		},
		
		/**
		 * Converts the URL parameter into javascript object.
		 */
		getUrlVars : function (url) {
		    var hash;
		    var myJson = {};
		    var hashes = url.slice(url.indexOf('?') + 1).split('&');
		    for (var i = 0; i < hashes.length; i++) {
		        hash = hashes[i].split('=');
		        myJson[hash[0]] = hash[1];
		    }
		    return myJson;
		},
		
		/**
		 * Display Upload Photo form
		 * 
		 * @author AJ
		 */
		showUploadPhoto : function() {
			$('.adjust-photo-modal').modal('hide');
			
			var url = $(this).data('url');
			
			$('.upload-photo-modal').load(url, '', function(){
				$('.upload-photo-modal').modal();
			});
		},
		
		/**
		 * Display Adjust Photo form
		 * 
		 * @author AJ
		 */
		showAdjustPhoto : function() {
			$('.upload-photo-modal').modal('hide');
			
			var url = $(this).data('url');
			
			$('.adjust-photo-modal').load(url, '', function(){
				$('.adjust-photo-modal').modal().on('shown', function(){
					$('#original-image').Jcrop({
						setSelect: [0, 0, 200, 200],
						allowSelect: false,
						minSize: [ 200, 200 ],
						onChange: function(c){
							$('#x').val(c.x);
						    $('#y').val(c.y);
						    $('#x2').val(c.x2);
						    $('#y2').val(c.y2);
						}
					});
				});
			});
		},
		
		/**
		 * Display response for photo tasks
		 * 
		 * @author AJ
		 */
		jsonForm : function(form, successCallback, errorCallback) {
			
			$(form).ajaxForm(function(data) {
				$.each(data['messages'].reverse(), function(i, result) {
					if(result.type == 'error') {
						opentides3.displayMessage({ messages : [ {
								type : "error",
								message : result.message,
								fieldName : result.fieldName,
								code: result.code,
								objectName: result.objectName
							}]}, form, (i!=0));
						if(errorCallback && i==0) { errorCallback(data); };
	
					} else {
						opentides3.displayMessage({ messages : [ {
							type : "success",
							message : result.message,
						}]}, form);
	
						if(successCallback) { successCallback(data); };
					}
				});
			});
		}

	};
})();

// *************************************
// Opentides3 JQuery extensions
// *************************************

(function($) {

	/**
	 * Shows the element on the given container.
	 */	
	$.fn.page = function(mode, data, path, options) {
		var settings = $.extend({
			'container'   : '',
			'from'        : '',
			'to'	      : '#'+$(this).attr('id'),
			'animate'     : 'none',
			'trackHistory': false,
			'removeFrom'  : true
		}, options);

		// if no container, default to use the closest parent container of element
		var container = (settings.container === '') ? 
						$(this).closest('.container') : 
						$(settings.container);
						
		// if no from, default to use the first visible page
		var from = (settings.from === '') ?
					container.find('.page:visible:first') :
					$(settings.from);
					
		var to = $(settings.to);		
					
		// if track history, then pushstate
		if (settings.trackHistory && opentides3.supportsHistory()) {
			history.pushState({
				mode : mode,
				data : data,
				from : settings.from,
				to   : settings.to,
				settings : settings
			}, null, path);
		}
		
		// do animation if page is different
		if (from.attr('id') != to.attr('id')) {
			if (settings.animate === 'forward') {
				opentides3.slideLeft(from, to, settings.removeFrom);
			} else if (settings.animate === 'back') {
				opentides3.slideRight(from, to, settings.removeFrom);
			} else {
				opentides3.replace(from, to, settings.removeFrom);
			}			
		}
	},
		
	/**
	 * Binds the json object to the form. Binding uses attribute 'name' for
	 * matching.
	 * 
	 * @param json -
	 *            json object containing the values
	 */

	$.fn.bindForm = function(json) {
		// if param is not json, assume its url param format.
		if (typeof json == 'string' || json instanceof String)
			json = opentides3.getUrlVars(json);
		
		return this.each(function() {
			var form = $(this);
			form.clearOTForm();

			form.find('textarea, input[type="text"], input[type="hidden"], input[type="password"], input[type="datetime"], input[type="datetime-local"], input[type="date"],input[type="month"], input[type="time"], input[type="week"], input[type="number"], input[type="email"], input[type="url"], input[type="search"], input[type="tel"], input[type="color"]')
					.each(function() {
				var $this = $(this),
					name = $this.attr('name');
				// special handler for file upload
				if(name && $this.is(":hidden") &&  $this.hasClass("ot-files")) {
					// clean up first - remove the rows as well as the hidden ids
					$("table.ot-attachment-list tbody").empty();
					$(".ot-files").filter(function() {
						return this.id;
					}).remove();
					// handler for photo or file upload
					var fileIds = opentides3.getValue(json, name.replace("__",""));
					if(fileIds.length > 0) {
						for(var i = 0; i < fileIds.length; i++) {
							if(fileIds[i].id !== undefined) {
								var filename = fileIds[i].filename != null ? fileIds[i].filename : fileIds[i].originalFileName; 
								var data = {"attachmentId" : fileIds[i].id, "attachmentName" : filename}, 
									tableRow = opentides3.template($('script#filesForDownload').html(), data);								
								$("table.ot-attachment-list tbody").append(tableRow);
							} 
						}						
					}
					
					if ($(".ot-upload-file").data("multiple") == false) {
						var len = $("#images-ot-attachment-list .files tr").length;
						if (len > 0) {
							// there is image, disable the button
							$('.browse-button-dummy').prop('disabled', true);
						} else {
							$('.browse-button-dummy').prop('disabled', false);
							
						}
					}
				} else if(name){
					var prime = toPrimitive(opentides3.getValue(json, name));
					$(this).val(prime).trigger("change");
				}
			});

			form.find("select").each(function() {
				var name = $(this).attr('name');
				var normValue = normalizeValue(opentides3.getValue(json, name));
				$(this).val(normValue).trigger("change");
			});

			form.find("input[type='checkbox']").each(function() {
				var name = $(this).attr('name');
				var value = opentides3.getValue(json, name);
				if (typeof (value) === 'boolean') {
					if (value == true)
						$(this).prop('checked', true).trigger("change");
					else
						$(this).prop('checked', false).trigger("change");
				} else {
					var normValue = normalizeValue(value, true);
					if (jQuery.inArray($(this).attr('value'), normValue) >= 0)
						$(this).prop('checked', true).trigger("change");
					else
						$(this).prop('checked', false).trigger("change");
				}
			});

			form.find("input[type='radio']").each(function() {
				var name = $(this).attr('name');
				var prime = toPrimitive(opentides3.getValue(json, name));
				if ($(this).attr('value') == prime)
					$(this).prop('checked', true).trigger('change');
				else
					$(this).prop('checked', false).trigger("change");

			});

		});
	};

	/**
	 * Clears all the input of the form.
	 */
	$.fn.clearOTForm = function() {
		return this.each(function() {
			$('.ot-select2').select2('data', null);
			$('.date').datepicker();
			
			//removes dynamically binded entities
			$('.removeable-pill').remove();
			
			//clears uploaded files
			$('#ot-attachment-list tbody').html('');
			
			$(':checkbox, :radio').prop('checked', false);
			$(':input', this).not(':checkbox, :radio, :button, :submit')
							 .val('')
							 .prop('selected', false);
		});
	};

	/**
	 * 
	 * Converts the elements into RESTful CRUD element. This jquery plugin
	 * should be used alongside standard opentides3 RESTful CRUD pages. While
	 * the functionalities conform to standard REST protocol, a corresponding
	 * BaseCrudController is expected receive the form submission.
	 * 
	 * (1) look for <add>, on click, display retrieve default values and display <form>. 
	 *     (1.1) when displaying check if <form> is modal, inline, on page. 
	 * (2) look for <search>, convert inner forms to json based search.
	 *     (2.1) display search results to <results> (must contain table) 
	 *     (2.2) within the results, look for <edit> and <remove> and attach events. 
	 * see steps 1 and 1.1.
	 * 
	 * Parameters include: 
	 *    search - element containing search criteria form. Default is '#search-panel'. 
	 *    results - element containing the results table. Default is '#results-panel'. 
	 *    form - element containing the form to display when adding/updating record. Default is '#form-body'. 
	 *    status - element where messages are displayed. Default is '.status'. 
	 *    paging - element where paging is displayed. Default is '.paging'. 
	 *    add - elements where add new record action is triggered on click. Default is '.add-action'.
	 *    edit - elements where edit record action is triggered on click. Default is '.edit-action' 
	 *    remove - elements where delete record action is triggered on click. Default is '.remove-action'
	 * 
	 * In addition, elements within the form with the following attributes are processed: 
	 * [data-submit='save'] - saves and close the panel
	 * [data-submit='save-and-new'] - saves and clear the form
	 * [data-display-form='add'] - displayed only when adding new record
	 * [data-display-form='update'] - displayed only when updating record
	 * 
	 */

	$.fn.RESTful = function(options) {
		// extend the options with defaults
		var settings = $.extend({
			'search'     : '#search-body',
			'form'       : '#form-body',
			'view'       : '#view-body',
			'results'    : '#results-panel',
			'status'     : '.status',
			'pagination' : '.pagination',
			'add'        : '.add-action',
			'edit'       : '.edit-action',
			'display'  	 : '.view-action',
			'remove'     : '.remove-action'
		}, options);

		return this
				.each(function() {

					// list of search forms
					var searchForms = $(this)
							.find(settings['search'] + ' form');

					// resultsPanel
					var results = $(this).find(settings['results']);

					// panel containing the form
					var form = $(this).find(settings['form']);
					
					// panel containing the view
					var view = $(this).find(settings['view']);

					// add/edit form
					var firstForm = $(this).find(settings['form'] + ' form:first');

					// status bar
					var status = $(this).find(settings['status']);

					// pagination bar
					var pagination = $(this).find(settings['pagination']);

					// body
					var body = $(this);

					// submit button
					var formSubmitButton = $(this).find("input[data-submit='save']");

					if ($(this).find(settings['results'] + ' table tr').length <= 1) {
						// no search results, hide the table and message
						//results.find('table').hide();
					}
					
					// Perform validations here.
					// page elements should have id (to.attr('id')
					// container should be present
					// ....

					/*****************************************************************************
					 * (1) Attach history pop event, when user clicks on back button of browser.
					 * There are 6 events that should be handled on a RESTful CRUD page
					 *   (1.1) On initial page load, display default search page.
					 *   (1.2) On search with criteria, bind the criteria and display results
					 *   (1.3) On page 2 of search results, adjust the pagination and display results
					 *   (1.4) On add form page, bind the empty and display form.
					 *   (1.5) On update form page, bind the form and display form.
					 *   (1.6) On view form page, bind the form and display view page.
					 *   (1.7) If initial load, just refresh the window
					 *****************************************************************************/
					if (opentides3.supportsHistory()) {
						// attach event on back 
						window.addEventListener("popstate", function(e) {							
							var state = e.state;
							if (state != null) {
								if (state.mode == 'search') {
									// 1.1, 1.2, 1.3
									var json = state.data;
									var searchForm = $(settings['search']).find('form').filter(':first');
									var search = searchForm.closest('.page');
									searchForm.bindForm(window.location.search);
									opentides3.displayMessage(json, searchForm.closest('div'));
									displayResults(searchForm, results, status, pagination, json);
									// slide page to search
									search.page('search', json, '', $.extend(state.settings,{'trackHistory':false}));
								} else if (state.mode == 'add'
										|| state.mode == 'update'
										|| state.mode == 'view') {
									// 1.4, 1.5
									displayForm(state.mode, 
											body.find(state.settings.to), 
											state.action,
											state.data);
									form.page(state.mode, '','', $.extend(state.settings,{'trackHistory':false}));
								}							
							} else {
								// initial page load, just refresh the screen
								// window.location.reload();
							}
						});
					}

					/*****************************************************************************
					 * (2) Attach error event handlers.
					 *****************************************************************************/
					// handle all ajax errors
					$(document).ajaxError(function(event, xhr, settings, exception) {
						var message = 'Oopps... An unexpected error occurred. ';
						if (xhr.status === 0) {
							message = "Cannot connect to server. Please verify your network connection.";
						} else if (xhr.status == 302) {
							// redirect to another page.
							message = "Redirecting to another page...";							
						} else if (xhr.status == 404) {
							message = 'Requested page not found. [Error: 404]';
						} else if (xhr.status == 500) {
							message = 'Internal Server Error. [Error: 500]';
						} else if (exception === 'parsererror') {
							message = 'Oopps... Unable to parse your JSON request.';
						} else if (exception === 'timeout') {
							message = 'Oopps... Server time-out error.';
						} else if (exception === 'abort') {
							message = 'Your request has been cancelled.';
						} else 	if (xhr.responseText.indexOf('login-panel') > 0 ) {
							showLoginModal(xhr.responseText);
							return;
						}				
						opentides3.displayMessage({
							messages : [ {
								type : "error",
								message : message,
							} ]
						});
					});
					
					/*****************************************************************************
					 * (3) Attach events for search operations.
					 *     (3.1) On search form submit
					 *     (3.2) On pagination
					 *     (3.3) Links to view details page
					 *     (3.4) Links to add new record detail
					 *     (3.5) links to edit existing record detail
					 *     (3.5) Links to delete existing record
					 *****************************************************************************/
					// 3.1, on search form submit
					searchForms.on('submit', function() {
						// search starts on page 1
						status.show();
						doSearch($(this), results, status, pagination, opentides3.getPath() + "?" + $(this).serialize() + "&p=1");
						pagination.show();
						return false;
					});
					
					// 3.2 Attach events on pagination links.
					$(this).find('.pagination').each(function() {
						// paging is already displayed, let's attach
						// events to it.
						$(this).find('li a').on("click",function() {
							// assume form used is the first
							// visible form in the search panel.
							var searchForm = $(settings['search']).find('form').filter(':visible:first');
							var i = $(this).data('page');
							doSearch(searchForm, results,
									status, pagination, opentides3.getPath() + "?" + searchForm.serialize() + "&p=" + i);
							return false;
						});
					});
					
					// 3.3 Links to view details
					// Attach view to all action-view
					$(this).on("click", settings['display'], function() {
						var id = $(this).data('id');
						var path = opentides3.getPath() + '/' + id;

						$.getJSON(path, // url
						"", // no data, id is passed in url
						function(json) { // callback
							displayForm('view', view, '', json);
							if (view.hasClass('modal'))
								view.modal();
							else if (view.hasClass('page')) {								
								view.page('view', 
										json, 
										opentides3.getPath() + '/view/' + id + window.location.search, 
										{'animate':'forward', 
									     trackHistory:true, 
									     removeFrom:false });
							}
						});
						// stop event propagation.
						return false;
					});		
					
					// 3.4 Links to add new record detail
					// attach events to add buttons
					// convert the search form to ajax search
					$(this).find(settings['add']).on('click', function() {
						// add new record
						var path = opentides3.getPath();
						$.getJSON(path + '/0', // url - new record has id=0
						'', // empty data
						function(json) { // callback
							displayForm('add', form, path, json);
							if (form.hasClass('modal'))
								form.modal();
							else if (form.hasClass('page')) {
								form.page('add', json, opentides3.getPath() + '/0' + window.location.search, 
									{'animate':'forward', 
								     trackHistory:true, 
								     removeFrom:false });
							}
						});
						// stop event propagation.
						return false;
					});
					
					// 3.5 Links to edit record detail
					// attach edit to all action-edit
					$(this).on("click", settings['edit'], function() {
						var id = $(this).data('id');
						var path = opentides3.getPath() + '/' + id;
						$.getJSON(path, // url
						"", // data
						function(json) { // callback
							displayForm('update', form, path, json)
							if (form.hasClass('modal'))
								form.modal();
							else if (form.hasClass('page')) {
								form.page('update', json, opentides3.getPath() + '/' + id + window.location.search, 
									{'animate':'forward', 
								     trackHistory:true, 
								     removeFrom:false });
							}
						});
					});

					// 3.6 Links to remove record detail
					// attach remove to all remove-action
					$(this).on("click",settings['remove'],
						function() {
							var ref = $(this).data('primary-value');
							var id = $(this).data('id');
							var tableRow = $(this).closest('tr');
							var message = opentides3.getMessage(
									'are-you-sure-to-delete', this);
							if (typeof (ref) != 'undefined'
									&& ref.length > 0) {
								message = message.replace('#primary-value', ref);
							} else {
								message = message.replace('#primary-value', 'record');
							}
							bootbox.dialog(message,
								[{"label" : "Remove",
								  "class" : "btn-danger",
								  "callback" : function() {
								 		$.ajax({url : opentides3.getPath() + '/' + id,
												type : 'DELETE',
												success : function(json) {
													opentides3.displayMessage(json);
													tableRow.fadeOut(300,
														function() {
															$(this).remove();
														});
												},
												dataType : 'json'
										});
									}
								},
								{"label" : "Cancel",
								 "class" : "btn"
								}]);
					});

					firstForm.find("[data-submit='save'], [data-submit='save-and-new']").on("click", function() {
						formSubmitButton = $(this);
					});

					firstForm.on("submit", function(e) {
						e.preventDefault();
						var firstForm = $(this);
						var button = formSubmitButton;
						button.prop('disabled', true);
						$.ajax({type : firstForm.attr('method'), // method
							url : firstForm.attr('action'), 	 // url
							data : firstForm.serialize(), 		 // data
							success : function(json) { 			 // callback
									opentides3.displayMessage(json, form);
									button.prop('disabled', false);
									if (typeof (json.command) === 'object'
											&& json.command.id > 0) {
										// successfully saved
										firstForm.clearOTForm();
										if (button.data('submit') !== 'save-and-new') {
											// hide modal
											if (form.hasClass('modal'))
												form.modal('hide');
											else if (form.hasClass('page')) {
												var url = opentides3.getPath().replace(
														/\/[0-9]+$/, '') + window.location.search;
												var search = $(settings['search']);
												var searchForm = search.find('form').filter(':first');
												doSearch(searchForm, results, status, pagination, url);
											}
										}
										displayTableRow(results.find('table'), json.command);
									}
								},
								dataType : 'json'
							}
						);
					});

					// hide all alerts when form is displayed.
					form.on('show', function(e) {						
						$(this).find('.control-group').each(function(){
							$(this).removeClass('warning info success error');
							$(this).find('.help-inline').remove();
						});
						$(this).find('.alert').remove();
					});
						
					// attach back or close buttons on view
					$(this).find("[data-dismiss='page']").on("click",function() {
						var url = opentides3.getPath().replace(
								/(\/view)?\/[0-9]+$/, '') + window.location.search;
						var search = $(settings['search']);
						var searchForm = search.find('form').filter(':first');
						doSearch(searchForm, results, status,
								pagination, url);
					});	

					// add sort fields as hidden on the form
					if (searchForms.find('[name="orderOption"]').length == 0)
						searchForms.append("<input type='hidden' name='orderOption' value=''/>");

					if (searchForms.find('[name="orderFlow"]').length == 0)
						searchForms.append("<input type='hidden' name='orderFlow' value=''/>");

					// attach clear button functions
					searchForms.find('[data-submit="clear"]').on("click",
						function() {
							
							$(this).closest('form').clearOTForm();
							return false;
						});
				});
	}
	
	// private method to display login as modal
	var showLoginModal = function(data) {
		if (data.indexOf('login-panel') > 0 ) {
			// display login panel as modal
			var loginModal = $('#login-modal');
			if (loginModal.length == 0) {
				loginModal = $('<div id="login-modal"/>').attr("class","modal fade");
				$('body').append(loginModal);
			}
			var loginPanel = $('<div/>').html(data).find('#login-panel').attr("class","");
			loginModal.html(loginPanel);
			loginModal.find('.header').addClass("modal-header").removeClass("header");
			loginModal.find('.footer').addClass("modal-footer").removeClass("footer");
			loginModal.find('.body').addClass("modal-body").removeClass("body");
			loginModal.find('.hide-modal').remove();								
			loginModal.find('form:first').on("submit",function() {
				// submit the form as ajax and close modal on success
		        $.ajax({
		            type: $(this).attr('method'),
		            url: $(this).attr('action'),
		            data: $(this).serialize(),
		            success: function (data) {
		            	if (typeof data === 'object' ||
		            		data.indexOf('login-panel') < 0) {
		            		$('#login-modal').modal('hide');
		            	} else {
		            		showLoginModal(data);
		            	}
		            }
		        });
		        return false;
			});

			if (typeof $('#login-modal').data('modal') == 'undefined' || 
				$('#login-modal').data('modal').isShown == false)
				$('#login-modal').removeData("modal").modal({backdrop: 'static', keyboard: false});
			return;
		}		
	}

	// Private method to perform search
	// parameters include:
	//   - searchForm
	//   - results
	//   - status
	//   - pagination
	//   - page
	var doSearch = function(searchForm, results, status, pagination, url) {		
		var search = searchForm.closest('.page');
		$.getJSON(
			url, // url
			'', // data
			function(json) { // callback
				opentides3.displayMessage(json, searchForm.closest('div'));
				displayResults(searchForm, results, status, pagination, json);
				// slide page to search
				search.page('search', json, url, {
					'animate'		: 'back',
					'removeFrom'	: false,
					'trackHistory'	: true
				});
			}
		);
	};

	/***************************************************************************
	 * (2.1) display search results to <results> (must contain table)
	 **************************************************************************/
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
			results.find('table').show();

			// old table
			var oldTable = results.find('table:first');
			var oldPage = oldTable.data('page');

			// new table
			var newTable = oldTable.clone();
			newTable.find('tr').not('.table-header').remove();
			newTable.attr('data-page', json.currPage);

			// listed results column
			var template = opentides3.template(results.find('script.template').html());

			// clear the table
			// add results as table row
			$.each(json['results'], function(i, result) {
				displayTableRow(newTable, result, template);
			}); // each

			// now let's animate the table
			if (typeof (oldPage) === 'undefined' || oldPage.length == 0) {
				// first page, no need to animate
				oldTable.after(newTable);
				oldTable.remove();
			} else if (oldPage < json.currPage)
				opentides3.slideLeft(oldTable, newTable, true);
			else if (oldPage > json.currPage)
				opentides3.slideRight(oldTable, newTable, true);
			else {
				// can't classify (or equal), no need to animate
				oldTable.after(newTable);
				oldTable.remove();
			}

		} else {
			results.find('table tbody tr').remove();
		}

		// look for status bar
		status.each(function(i, elem) {
			if (json['results'].length == 0) {
				html = opentides3.getMessage('no-results-found', elem);
				$(elem).html(html);
				//results.find('table').hide();
			} else {
				results.find('table').show();
				html = "<span class='records'>"
						+ opentides3.getMessage('summary-message', elem)
								.replace('#start', json['startIndex'] + 1)
								.replace('#end', json['endIndex'] + 1).replace(
										'#total', json['totalResults'])
						+ "</span>";
				html = html
						+ "<span class='searchTime'> ("
						+ opentides3.getMessage('result-seconds', elem)
								.replace('#time', json['searchTime'] / 1000)
						+ ")</span>";
				$(elem).html(html);
			}
		});
		// look for paging
		pagination.each(function(i, elem) {
			if (json['results'].length > 0
					&& (json['totalResults'] / json['pageSize'] > 1)) {
				html = 	"<div class='pagination pagination-centered'>" +
						"<ul><li class='ot3-firstPage'><a href='javascript:void(0)' data-page='1'>&laquo;</a></li>" + // first page
						"<li class='ot3-prevPage'><a href='javascript:void(0)' data-page='" + (json['currPage'] - 1) + "'>&lsaquo;</a></li>"; // prev page
				for ( var i = json['startPage']; i <= json['endPage']; i++) {
					html = html + "<li class='ot3-page-" + i + "'><a href='javascript:void(0)' data-page='" + i + "'>" + i + "</a></li>"; // pages
				}
				html = html +
						"<li class='ot3-nextPage'><a href='javascript:void(0)' data-page='" + (json['currPage'] + 1) + "'>&rsaquo;</a></li>" + // next page
						"<li class='ot3-lastPage'><a href='javascript:void(0)' data-page='" + json['endPage'] + "'>&raquo;</a></li>" + // last page
						"</ul></div>";
				$(elem).html(html);
				// set active page
				$('.ot3-page-' + json['currPage']).addClass('active');
				$('.ot3-page-' + json['currPage']).html(
						'<span>'+ $('.ot3-page-' + json['currPage'] + ' a').html() + '</span>');

				// if first page, disable first and prev page
				if (json['currPage'] == 1) {
					$('.ot3-firstPage').addClass('disabled');
					$('.ot3-prevPage').addClass('disabled');
					$('.ot3-firstPage').html('<span>&laquo;</span>');
					$('.ot3-prevPage').html('<span>&lsaquo;</span>');
				}
				// if last page, disable last and next page
				if (json['currPage'] == json['endPage']) {
					$('.ot3-lastPage').addClass('disabled');
					$('.ot3-nextPage').addClass('disabled');
					$('.ot3-lastPage').html('<span>&raquo;</span>');
					$('.ot3-nextPage').html('<span>&rsaquo;</span>');
				}
				$(elem).find('li a').on(
						"click",
						function() {
							var i = $(this).data('page');
							doSearch(searchForm, results, status,
									pagination, opentides3.getPath() + "?" + searchForm.serialize() + "&p=" + i);
						})
			} else {
				$(elem).html('');
			}
		});
	};

	/**
	 * Private helper that displays one row of search results based on defined template.
	 */
	var displayTableRow = function(resultsTable, result, template) {		
		if (typeof template === 'undefined' ||
			typeof template === 'null')
			template = opentides3.template(resultsTable.find('script.template').html());

		var tableRow = resultsTable.find('tr[data-id="' + result['id'] + '"]');

		if (tableRow.length > 0) {
			// update the record
			var row = template(result);
			tableRow.replaceWith(row);
		} else {
			// add new record
			var row = template(result);
			resultsTable.find('tbody').append(row);
		}

	};

	/**
	 * Private helper that displays the add/edit form in the page
	 */
	var displayForm = function(mode, form, action, json) {
		var firstForm = form.find('form:first');
		if (mode === 'update') {
			firstForm.attr('action', action);
			firstForm.attr('method', 'put');
			firstForm.bindForm(json);
			form.find('.ot3-add').hide();
			form.find('.ot3-update').show();
		} else if (mode === 'add') {
			firstForm.attr('action', action);
			firstForm.attr('method', 'post');
			firstForm.bindForm(json);
			form.find('.ot3-update').hide();
			form.find('.ot3-add').show();
		} else if (mode === 'view') {
			if( typeof json == "object" ) {
		        $.each(json, function(k,v) {
		            // k is either an array index or object key
		        	form.find("[name='"+k+"']").html(v);
		        });
		    }
		}
		
		// Refresh javascript/jquery form plugins here (if necessary)
		$('input[type="hidden"].tokenizer').select2({
            tags: [],
            tokenSeparators: [",", " "]
		});
		
		$('input[type="hidden"].combobox').each(function(){
			$(this).select2({
	            tags: comboBoxTags[$(this).attr('id')],
				maximumSelectionSize: 1
			});
		});
		// End of refreshing of javascript/jquery plugins
		
	};

	/**
	 * Private helper method to convert an object to primitive value. Used when
	 * converting json objects to single value string.
	 */
	var toPrimitive = function(value) {
		var type = jQuery.type(value);
		if (type === 'object') {
			if (value.key != null)
				return value.key;
			else if (value.id != null)
				return value.id;
			else {
				alert('Unable to convert object [' + value + '] to its primitive form.');
				return '';
			}
		} else
			return value;
	}

	/**
	 * Private helper that ensures value is always a single dimension array. If
	 * value is an object, key is used, otherwise id is used. Otherwise, error
	 * occurs.
	 */
	var normalizeValue = function(value, toArray) {
		if (jQuery.isEmptyObject(value))
			return '';
		var prime = toPrimitive(value);
		var type = jQuery.type(prime);
		if ((type === 'string') || (type === 'boolean') || (type === 'number')) {
			// value is primitive, return as is.
			if (typeof (toArray) != 'undefined' && toArray == true)
				return [ prime ];
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
	};

})(jQuery);

/**
 * Universal variable for holding comboBox tags.
 * 
 * @author AJ
 *
 */
var comboBoxTags = {};

/**
 * Override jQuery's addClass, toggleClass and removeClass
 * methods to fire an event.
 * 
 * @author AJ
 * 
 * Note: Nevermind this, speed and performance harshly fell.
 * 
 */
/*(function($){
    var methods = ["addClass", "toggleClass", "removeClass"];
    $.map(methods, function(method){
        var originalMethod = $.fn[ method ];
        $.fn[ method ] = function(){
            var result = originalMethod.apply( this, arguments );
            this.trigger(method);
            return result;
        };
    });
    
    $(document).ready()
    
    // bind hacks on document ready
    .on("removeClass", '.btn-group', function(e){
		$(this).closest('.table-wrapper-2').toggleClass('overflow-hidden');
	});
    
}(jQuery));*/
