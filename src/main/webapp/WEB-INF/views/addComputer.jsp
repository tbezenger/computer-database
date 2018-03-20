<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="css/font-awesome.css" rel="stylesheet" media="screen">
<link href="css/main.css" rel="stylesheet" media="screen">
<script src="js/jquery.min.js"></script>
<script src="js/validation.js"></script>

</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="dashboard"> Application - Computer
				Database </a>
		</div>
	</header>
	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<h1>Add Computer</h1>
					<form:form modelAttribute="addForm" action="addComputer"
						method="POST">
						<input name="action" type="hidden" value="add" />
						<fieldset>
								<div class="form-group">
									<label for="computerName">Computer name</label>
									<form:input path="name" type="text" class="form-control"
										id="computerName" name="computerName"
										placeholder="Computer name (only letters or numbers, max = 30)"
										pattern="[A-Za-z0-9]*" />
									<form:errors path="name" cssClass="error" />
								</div>
								<div class="form-group">
									<label for="introduced">Introduced date</label>
									<form:input path="introduced" type="date" class="form-control"
										id="introduced" name="introduced"
										placeholder="Introduced date" />
									<form:errors path="introduced" cssClass="error" />
								</div>

								<div class="form-group">
									<label for="discontinued">Discontinued date</label>
									<form:input path="discontinued" type="date"
										class="form-control" id="discontinued" name="discontinued"
										placeholder="Discontinued date" />
									<form:errors path="discontinued" cssClass="error" />
								</div>
								<div class="form-group">
									<label for="companyId">Company</label>
									<form:select path="company.id" class="form-control"
										id="companyId" name="companyId">
										<option value="0">--</option>
										<c:forEach items="${companies}" var="company">
											<option value="${company.id}">${company.name}</option>
										</c:forEach>
									</form:select>
									<form:errors path="company.id" cssClass="error" />
								</div>

						</fieldset>
						<div class="actions pull-right">
							<input id="addButton" type="submit" value="Add"
								class="btn btn-primary"> or <a href="dashboard"
								class="btn btn-default">Cancel</a>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</section>
</body>
</html>