<%--
	- @author - ONZ
--%>

<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="title.form-tags" active="advanced">
	<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
</app:header>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div id="sideBar" class="span2" style="top: 55px;">
		<ul class="nav nav-list side-nav affix">
			<li><a href="#textBox">Text Box</a></li>
			<li><a href="#datePicker">Date Picker</a></li>
			<li><a href="#select">Select</a></li>
			<li><a href="#multipleSelect">Multiple Select</a></li>
			<li><a href="#comboBox">Combo Box</a></li>
			<li><a href="#checkBox">Check Box</a></li>
			<li><a href="#radioButton">Radio Button</a></li>
		</ul>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<ul class="breadcrumb">
			<li>Advanced <span class="divider">/</span></li>
			<li>Form Tags</li>
		</ul>
		
		<h1><i class="icon-book"></i> Form Tags</h1>
		<p class="lead">HTML form elements are used to select different kinds of user input.</p>
		
		<section id="textBox">
			<h3><spring:message code="label.tutorial.textBox"/></h3>
			<p>Displays a combination of a text field and a label. Used inside a form</p>
			<div class="tutorial-body">
				<form:form modelAttribute="formCommand" id="ninja-form" cssClass="form-horizontal">
					<div class="example">
						<app:input path="attribute" label="label.tutorial.firstName"/>
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
		</section>
		
		<section id="datePicker">
			<h3><spring:message code="label.tutorial.datePicker"/></h3>
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
		</section>
		
		<section id="select">
			<h3><spring:message code="label.tutorial.select"/></h3>
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
		</section>
		
		<section id="multipleSelect">
			<h3><spring:message code="label.tutorial.multipleSelect"/></h3>
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
		</section>
		
		<section id="comboBox">
			<h3><spring:message code="label.tutorial.comboBox"/></h3>
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
		</section>
		
		<section id="checkBox">
			<h3><spring:message code="label.tutorial.checkBox"/></h3>
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
		</section>
		
		<section id="radioButton">
			<h3><spring:message code="label.tutorial.radioButton"/></h3>
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
		</section>
		
		<hr/>
		
		<h2><i class="icon-dashboard"></i> Information Overload!</h2>
		<p>Wait are you tired? You shouldn't be. You still have an exercise to do!</p>
		<p>Base from what you have learned right now, implement the following into our patient form</p>
		<ul>
			<li>Radiobutton tag for gender</li>
			<li>Datepicker tag for birthday</li>
			<li>Select tag for City</li>
		</ul>
		<p>Meanwhile, feel free to explore the tags for yourself. Ciao!</p>
		
		<ul class="pager">
			<li class="previous">
				<a href="${home}/start">&larr; Previous (Getting Started)</a>
			</li>
			<li class="next">
				<a href="${home}/java-classes">Next (Basic classes) &rarr;</a>
			</li>
		</ul>
	</div>
</div>

<app:footer>
</app:footer>