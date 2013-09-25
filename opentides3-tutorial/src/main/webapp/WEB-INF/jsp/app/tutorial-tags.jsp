<%--
	- tutorial-tags.jsp
	- Displays a tutorial for using the tags of opentides 3
	-
	- @author - ONZ
--%>

<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="label.tutorial" active="tags"/>

<ul class="breadcrumb">
  <li><a href="${home}"><spring:message code="label.home"/></a> <span class="divider">/</span></li>
  <li><spring:message code="label.tutorial"/></li>
</ul>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span2">
		<div data-spy="affix" data-offset-top="60" class="affix-top" style="top: 55px;">
			<ul class="nav nav-list side-nav">
				<li class="nav-header"><spring:message code="label.tutorial.formTags"/></li>
				<li class="active"><a id="scroll-textBox"><spring:message code="label.tutorial.textBox"/></a></li>
				<li><a id="scroll-datePicker"><spring:message code="label.tutorial.datePicker"/></a></li>
				<li><a id="scroll-select"><spring:message code="label.tutorial.select"/></a></li>
				<li><a id="scroll-multipleSelect"><spring:message code="label.tutorial.multipleSelect"/></a></li>
				<li><a id="scroll-comboBox"><spring:message code="label.tutorial.comboBox"/></a></li>
				<li><a id="scroll-checkBox"><spring:message code="label.tutorial.checkBox"/></a></li>
				<li><a id="scroll-radioButton"><spring:message code="label.tutorial.radioButton"/></a></li>
				<li><a id="scroll-tokenizer"><spring:message code="label.tutorial.tokenizer"/></a></li>
				<li class="nav-header"><spring:message code="label.tutorial.pageTags"/></li>
				<li><a id="scroll-header"><spring:message code="label.tutorial.header"/></a></li>
				<li><a id="scroll-footer"><spring:message code="label.tutorial.footer"/></a></li>
				<li class="nav-header"><spring:message code="label.tutorial.otherTags"/></li>
				<li><a id="scroll-pagination"><spring:message code="label.tutorial.pagination"/></a></li>
				<li><a id="scroll-status"><spring:message code="label.tutorial.status"/></a></li>
				<li><a id="scroll-comment"><spring:message code="label.tutorial.comments"/></a></li>
			</ul>
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<h1><i class="icon-book"></i> Guide to using Opentides 3 tags</h1>
		
		<h3 id="textBox"><spring:message code="label.tutorial.textBox"/></h3>
		<p>Displays a combination of a text field and a label. Used inside a form</p>
		
		<div class="tutorial-body">
			<form:form modelAttribute="formCommand" id="ninja-form" cssClass="form-horizontal">
				<div class="example">
					<span class="code code-black"></span>
				</div>
				<div class="highlight">
					<h4>Usage</h4>
					<span class="code code-red">&lt;app:input path="attribute" label="label.tutorial.firstName"/&gt;</span>
					<h4>Common parameters</h4>
					<table>
						<tr>
							<td class="wider"><span class="code code-teal">path</span></td>
							<td>Specify the attribute from the model the text field will be linked into</td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">label</span></td>
							<td>Label that will be displayed. Should be inside an *.properties file</td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">required</span></td>
							<td><span class="code code-red">true</span> or <span class="code code-red">false</span>. Adds a required text into the label and the html 5 <span class="code code-red">required</span> tag into the element</td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">placeholder</span></td>
							<td>Adds a placeholder message into the text field</td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">cssClass</span></td>
							<td>Used to add user's custom styling</td>
						</tr>
					</table>
				</div>
			</form:form>
		</div>
		
		<h3>Adding additional texts and icons</h3>
		<div class="tutorial-body">
			<form:form modelAttribute="formCommand" id="tutorial-form" cssClass="form-horizontal">
				<div class="example">
					<app:input path="attribute" label="label.tutorial.hashtag" prependText="#"/>
					<app:input path="attribute" label="label.tutorial.homeAddress" appendIcon="icon-home"/>
				</div>
				<div class="highlight">
					<h4>Usage</h4>
					<span class="code code-red">&lt;app:input path="attribute" label="label.tutorial.hashtag" prependText="#"/&gt;</span>
					<br/><br/>
					<span class="code code-red">&lt;app:input path="attribute" label="label.tutorial.homeAddress" appendIcon="icon-home"/&gt;</span>
					<h4>Additional parameters</h4>
					<table>
						<tr>
							<td class="wider"><span class="code code-teal">appendText</span></td>
							<td>Adds a text at the end of the text field</td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">prependText</span></td>
							<td>Adds a text at the beginning of the text field</td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">appendIcon</span></td>
							<td>Adds an icon at the end of the text field</td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">prependIcon</span></td>
							<td>Adds an icon at the beginning of the text field</td>
						</tr>
					</table>
				</div>
			</form:form>
		</div>
		
		<hr/>
		
		<h3 id="datePicker"><spring:message code="label.tutorial.datePicker"/></h3>
		<p>Displays a date picker interface into a text field.</p>
		<div class="tutorial-body">
			<form:form modelAttribute="formCommand" id="ninja-form" cssClass="form-horizontal">
				<div class="example">
					<app:input path="expirationDate" label="label.tutorial.expirationDate" datepicker="true" appendIcon="icon-calendar"/>
				</div>
				<div class="highlight">
					<h4>Usage</h4>
					<span class="code code-red">&lt;app:input path="expirationDate" label="label.tutorial.expirationDate" datepicker="true" appendIcon="icon-calendar"/&gt;</span>
					<h4>Parameters</h4>
					<table>
						<tr>
							<td class="wider"><span class="code code-teal">datepicker</span></td>
							<td><span class="code code-red">true</span></td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">appendIcon</span></td>
							<td><span class="code code-red">icon-calendar</span></td>
						</tr>
					</table>
				</div>
				
			</form:form>
		</div>
		
		<hr/>
		
		<h3 id="select"><spring:message code="label.tutorial.select"/></h3>
		<p>Displays a list of items for selection. Items to be displayed are passed in the form of a list.</p>
		<div class="tutorial-body">
			<form:form modelAttribute="formCommand" id="ninja-form" cssClass="form-horizontal">
				<div class="example">
					<app:select label="label.tutorial.brand" path="attribute" items="${brandList}" itemLabel="value" itemValue="key" select2="true"/>
				</div>
				<div class="highlight">
					<h4>Usage</h4>
					<span class="code code-red">&lt;app:select label="label.tutorial.brand" path="attribute" items="&#36;{brandList}" itemLabel="value" itemValue="key" select2="true" /&gt;</span>
					<h4>Parameters</h4>
					<table>
						<tr>
							<td class="wider"><span class="code code-teal">label</span></td>
							<td>Label that will be displayed. Should be inside an *.properties file</td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">path</span></td>
							<td>Specify the attribute from the model the text field will be linked into</td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">required</span></td>
							<td><span class="code code-red">true</span> or <span class="code code-red">false</span>. Adds a required text into the label and the html 5 <span class="code code-red">required</span> tag into the element</td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">cssClass</span></td>
							<td>Used to add user's custom styling</td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">items</span></td>
							<td>List of objects that will be displayed as options</td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">itemLabel</span></td>
							<td><span class="code code-red">value</span></td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">itemValue</span></td>
							<td><span class="code code-red">key</span></td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">select2</span></td>
							<td>Provides a search/filter for the items in the drop down if set into <span class="code code-red">true</span></td>
						</tr>
					</table>
				</div>	
			</form:form>
		</div>
		
		<hr/>
		
		<h3 id="multipleSelect"><spring:message code="label.tutorial.multipleSelect"/></h3>
		<p>Allows the selection of multiple items from the list.</p>
		<div class="tutorial-body">
			<form:form modelAttribute="formCommand" id="ninja-form" cssClass="form-horizontal">
				<div class="example">
					<app:select label="label.tutorial.brand" path="attribute2" items="${brandList}" itemLabel="value" itemValue="key" select2= "true" multiple="true"/>
				</div>
				<div class="highlight">
					<h4>Usage</h4>
					<span class="code code-red">&lt;app:select label="label.tutorial.brand" path="attribute" items="&#36;{brandList}" itemLabel="value" itemValue="key" select2= "true" multiple="true"/&gt;</span>
					<h4>Parameters</h4>
					<table>
						<tr>
							<td class="wider"><span class="code code-teal">multiple</span></td>
							<td>Allows multiple selection of items in the list if set into <span class="code code-red">true</span></td>
						</tr>
					</table>
				</div>	
			</form:form>
		</div>
		
		<hr/>
		
		<h3 id="comboBox"><spring:message code="label.tutorial.comboBox"/></h3>
		<p>Displays a drop down menu similar to select tag but user can add new items in the list.</p>
		<div class="tutorial-body">
			<form:form modelAttribute="formCommand" id="ninja-form" cssClass="form-horizontal">
				<div class="example">
					<app:combobox path="element" label="label.tutorial.element" items="${elementList}"/>
				</div>
				<div class="highlight">
					<h4>Usage</h4>
					<span class="code code-red">&lt;app:combobox path="element" label="label.tutorial.element" items="${elementList}" /&gt;</span>
					<h4>Parameters</h4>
					<table>
						<tr>
							<td class="wider"><span class="code code-teal">label</span></td>
							<td>Label that will be displayed. Should be inside an *.properties file</td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">path</span></td>
							<td>Specify the attribute from the model the text field will be linked into</td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">required</span></td>
							<td><span class="code code-red">true</span> or <span class="code code-red">false</span>. Adds a required text into the label and the html 5 <span class="code code-red">required</span> tag into the element</td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">cssClass</span></td>
							<td>Used to add user's custom styling</td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">items</span></td>
							<td>List of objects that will be displayed as options</td>
						</tr>
					</table>
				</div>	
			</form:form>
		</div>
		
		<hr/>
		
		<h3 id="checkBox"><spring:message code="label.tutorial.checkBox"/></h3>
		<p>Displays a check box that can be toggled.</p>
		<div class="tutorial-body">
			<form:form modelAttribute="formCommand" id="ninja-form" cssClass="form-horizontal">
				<div class="example">
					<app:checkbox label="label.tutorial.active" path="active"/>
				</div>
				<div class="highlight">
					<h4>Usage</h4>
					<span class="code code-red">&lt;app:checkbox label="label.tutorial.active" path="active"/&gt;</span>
					<h4>Parameters</h4>
					<table>
						<tr>
							<td class="wider"><span class="code code-teal">path</span></td>
							<td>Specify the attribute from the model the text field will be linked into</td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">label</span></td>
							<td>Label that will be displayed. Should be inside an *.properties file</td>
						</tr>
					</table>
				</div>
			</form:form>
		</div>
		
		<hr/>
		
		<h3 id="radioButton"><spring:message code="label.tutorial.radioButton"/></h3>
		<p>Displays multiple options for the user.</p>
		<div class="tutorial-body">
			<form:form modelAttribute="formCommand" id="ninja-form" cssClass="form-horizontal">
				<div class="example">
					<app:radio label="label.tutorial.gender" path="gender" items="${genderList}" itemLabel="value" itemValue="key"/>
				</div>
				<div class="highlight">
					<h4>Usage</h4>
					<span class="code code-red">&lt;app:radio label="label.tutorial.gender" path="gender" items="${genderList}" itemLabel="value" itemValue="key"/&gt;</span>
					<h4>Parameters</h4>
					<table>
						<tr>
							<td class="wider"><span class="code code-teal">path</span></td>
							<td>Specify the attribute from the model the text field will be linked into</td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">label</span></td>
							<td>Label that will be displayed. Should be inside an *.properties file</td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">required</span></td>
							<td><span class="code code-red">true</span> or <span class="code code-red">false</span>. Adds a required text into the label and the html 5 <span class="code code-red">required</span> tag into the element</td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">items</span></td>
							<td>List of objects that will be displayed as options</td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">itemLabel</span></td>
							<td><span class="code code-red">value</span></td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">itemValue</span></td>
							<td><span class="code code-red">key</span></td>
						</tr>
					</table>
				</div>
			</form:form>
		</div>
		
		<hr/>
		
		<h3 id="tokenizer"><spring:message code="label.tutorial.tokenizer"/></h3>
		<p>Displays a text area to be used for multiple string value.</p>
		<div class="tutorial-body">
			<form:form modelAttribute="formCommand" id="ninja-form" cssClass="form-horizontal">
				<div class="example">
					<app:tokenizer label="label.tutorial.tags" path="tags" />
				</div>
				<div class="highlight">
					<h4>Usage</h4>
					<span class="code code-red">&lt;app:tokenizer label="label.tutorial.tags" path="tags" /&gt;</span>
					<h4>Parameters</h4>
					<table>
						<tr>
							<td class="wider"><span class="code code-teal">path</span></td>
							<td>Specify the attribute from the model the text field will be linked into</td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">label</span></td>
							<td>Label that will be displayed. Should be inside an *.properties file</td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">required</span></td>
							<td><span class="code code-red">true</span> or <span class="code code-red">false</span>. Adds a required text into the label and the html 5 <span class="code code-red">required</span> tag into the element</td>
						</tr>
						<tr>
							<td class="wider"><span class="code code-teal">cssClass</span></td>
							<td>Used to add user's custom styling</td>
						</tr>
					</table>
				</div>
			</form:form>
		</div>
		
		<hr/>
		
		<h1><i class="icon-tags"></i> <spring:message code="label.tutorial.pageTags"/></h1>

		<h3 id="header"><spring:message code="label.tutorial.header"/></h3>
		<p>Adds the default Open Tides 3 header to the page. </p><br/><br/>
		<div class="tutorial-body">
			<div class="highlight">
				<h4>Usage</h4>
				<span class="code code-red">&lt;app:header pageTitle="label.tutorial" active="tutorial"/&gt;</span>
				<h4>Parameters</h4>
				<table>
					<tr>
						<td class="wider"><span class="code code-teal">pageTitle</span></td>
						<td>The title of the page that would be displayed. Content must be in the *.message.properties file</td>
					<tr/>
					<tr>
						<td class="wider"><span class="code code-teal">active</span></td>
						<td>Sets the specified tab to be seen as selected.</td>
					</tr>
					<tr>
						<td class="wider"><span class="code code-teal">pageType</span></td>
						<td>
							Open Tides 3 currently supports 3 page types. 
							The default page, <span class="code code-red">modal-page</span> and <span class="code code-red">anonymous-page</span>
						</td>
					</tr>
				</table>
			</div>
		</div>
		
		<hr/>
		
		<h3 id="footer"><spring:message code="label.tutorial.footer"/></h3>
		<p>Adds the default Open Tides 3 footer to the page. All javascript codes are written inside this tag.</p><br/><br/>
		<div class="tutorial-body">
			<div class="highlight">
				<h4>Usage</h4>
				<span class="code code-red">
					&lt;app:footer&gt;<br/>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;//javascript code here<br/>
					&lt;/app:footer&gt;
				</span>
			</div>
		</div>
		
		<hr/>
		
		<h1><i class="icon-tags"></i> Other tags</h1>

		<h3 id="pagination">Pagination</h3>
		<p>Displays a text area to be used for multiple string value.</p>
		<div class="tutorial-body">
			<div class="example">
				<app:paging results="${results}"/>
			</div>
			<div class="highlight">
				<h4>Usage</h4>
				<span class="code cod-red">&lt;app:paging results="&#36;{results}" /&gt;</span>
				<h4>Parameters</h4>
				<table>
					<tr>
						<td class="wider"><span class="code code-teal">results</span></td>
						<td>Specify the result set of the list to have pagination.</td>
					</tr>
				</table>
			</div>
		</div>
		
		<hr/>
		
		<h3 id="status"><spring:message code="label.tutorial.status"/></h3>
		<p>Displays a block of text that contains a quick overview of the query executed in the page.</p>
		<div class="tutorial-body">
			<div class="example">
				<app:status results="${results}"/>
			</div>
			<div class="highlight">
				<h4>Usage</h4>
				<span class="code code-red">&lt;app:paging results="&#36;{results}" /&gt;</span>
				<h4>Parameters</h4>
				<table>
					<tr>
						<td class="wider"><span class="code code-teal">results</span></td>
						<td>Specify the result set of the list to have information given.</td>
					</tr>
					<tr>
						<td class="wider"><span class="code code-teal">resultLabel</span></td>
						<td>Name referring to the results. Used as "Displaying X of Y <strong>Cars</strong>" <small>(not yet implemented)</small></td>
					</tr>
				</table>
			</div>
		</div>
		
		<hr/>
		
		<h3 id="comment"><spring:message code="label.tutorial.comments"/></h3>
		<p>Displays a block of text that contains a quick overview of the query executed in the page.</p>
		<div class="tutorial-body">
			<div class="example">
				<app:comments commentList="${tutorialModel.comments}" action="/" commentableId="${tutorialModel.id}"/>
			</div>
			<div class="highlight">
				<h4>Usage</h4>
				<span class="code code-red">&lt;app:comments commentList="&#36;{tutorialModel.comments}" action="/" commentableId="&#36;{tutorialModel.id}" /&gt;</span>
				<h4>Parameters</h4>
				<table>
					<tr>
						<td class="wider"><span class="code code-teal">action</span></td>
						<td>The url for the comment controller of the specified bean</td>
					</tr>
					<tr>
						<td class="wider"><span class="code code-teal">commentList</span></td>
						<td>A list of comments from the current bean</td>
					</tr>
					<tr>
						<td class="wider"><span class="code code-teal">commentableId</span></td>
						<td>ID of the bean that would have a comment</td>
					</tr>
				</table>
			</div>
		</div>


	</div>
</div>

<app:footer>

	<script type="text/javascript">
		$(document).ready(function(){
			$('ul.nav li a').click(function(){
				$('ul.nav li').removeClass('active');
				$(this).parent('li').addClass('active');
				var id = $(this).attr("id").split("-")[1];
				scrollToView(id); 
			});
		});
		
		function scrollToView(destinationID){
			$('html, body').animate({
				scrollTop: $("#"+destinationID).offset().top-45
			}, 1500);
		}
	</script>
</app:footer>