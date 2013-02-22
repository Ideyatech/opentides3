<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


	<!--Header-->
	<h2>Welcome to Opentides 3.0!</h2>

	<!--Content/Login-->

	<div id="login-body" class="container span12 pagination-centered">
		<div class="span6 offset3">
		<form class="form-horizontal">
			<div class="control-group">
				<label class="control-label" for="inputEmail">Email</label>
				<div class="controls input-prepend">
					<span class="add-on"><i class="icon-user"></i></span> 
					<input type="text" id="inputEmail" placeholder="Username">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="inputPassword">Password</label>
				<div class="controls input-prepend">
					<span class="add-on"><i class="icon-lock"></i></span>
					<input type="password" id="inputPassword" placeholder="Password">
				</div>
			</div>
			<div class="control-group">
				<div class="controls input-prepend">
					<label class="checkbox"> <input type="checkbox">
						Remember me
					</label>
					<span class="add-on"><i class="icon-ok"></i></span>
					<button type="submit" class="btn"> Sign in</button>
				</div>
			</div>		
		</form>
		</div>
		<div class="forgot-password">
			<a href="#">Forgot your password?</a>
		</div>
	</div>

<app:footer-anonymous />