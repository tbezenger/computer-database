<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="custom" uri="/WEB-INF/taglib.tld"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="css/font-awesome.css" rel="stylesheet" media="screen">
<link href="css/main.css" rel="stylesheet" media="screen">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="dashboard"> Application - Computer
				Database </a>
			<div class="pull-right" role="group">
                <a href="dashboard?lang=fr">fr</a>
                <a href="dashboard?lang=en">en</a>
            </div>
		</div>
	</header>
	<section id="main">
		<div class="container">
			<h1 id="homeTitle">${computerPage.totalResults} <spring:message code="computer.found" /></h1>
			<div id="actions" class="form-horizontal">
				<div class="pull-left">
					<form id="searchForm" action="#" method="GET" class="form-inline">
						<input type="search" id="searchbox" name="search"
							class="form-control" placeholder="Search name" /> <input
							type="submit" id="searchsubmit" value="Filter by name"
							class="btn btn-primary" />
					</form>
				</div>
				<div class="pull-right">
					<a class="btn btn-success" id="addComputer" href="addComputer">Add
						Computer</a> <a class="btn btn-default" id="editComputer" href="#"
						onclick="$.fn.toggleEditMode();">Edit</a>
				</div>
			</div>
		</div>

		<form id="deleteForm" action="#" method="POST">
			<input type="hidden" name="selection" value="">
		</form>

		<div class="container" style="margin-top: 10px;">
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<!-- Variable declarations for passing labels as parameters -->
						<!-- Table header for Computer Name -->

						<th class="editMode" style="width: 60px; height: 22px;"><input
							type="checkbox" id="selectall" /> <span
							style="vertical-align: top;"> <a href="#"
								id="deleteSelected" onclick="$.fn.deleteSelected();"> <i
									class="fa fa-trash-o fa-lg"></i>
							</a>
						</span></th>
						<th><a
							href="dashboard?page=${computerPage.numPage}&rows=${computerPage.rows}&search=${computerPage.search}&orderBy=name&isAscending=${not computerPage.isAscending}">Computer
								name</a></th>
						<th><a
							href="dashboard?page=${computerPage.numPage}&rows=${computerPage.rows}&search=${computerPage.search}&orderBy=introduced&isAscending=${not computerPage.isAscending}">Introduced
							date</a></th>
						<th><a
							href="dashboard?page=${computerPage.numPage}&rows=${computerPage.rows}&search=${computerPage.search}&orderBy=discontinued&isAscending=${not computerPage.isAscending}">Discontinued
							date</a></th>
						<th><a
							href="dashboard?page=${computerPage.numPage}&rows=${computerPage.rows}&search=${computerPage.search}&orderBy=company&isAscending=${not computerPage.isAscending}">Company</a></th>

					</tr>
				</thead>
				<!-- Browse attribute computers -->
				<tbody id="results">
					<c:forEach items="${computerPage.computers}" var="computer">
						<tr>
							<td class="editMode"><input type="checkbox" name="cb"
								value="${computer.id}" class="cb"></td>
							<td><a href="editComputer?id=${computer.id}" onclick="">${computer.name}</a></td>
							<td>${computer.introduced}</td>
							<td>${computer.discontinued}</td>
							<td>${computer.company.name}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</section>

	<footer class="navbar-fixed-bottom">
		<div class="container text-center">
			<ul class="pagination">

				<c:if test="${computerPage.numPage > 1}">
					<li><a
						href="dashboard?page=${computerPage.numPage-1}&rows=${computerPage.rows}&search=${computerPage.search}&orderBy=${computerPage.orderBy}&isAscending=${computerPage.isAscending}"
						aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
					</a></li>
				</c:if>
				<custom:Pagination page="${computerPage}" />
				<c:if test="${computerPage.numPage < computerPage.maxPage}">
					<li><a
						href="dashboard?page=${computerPage.numPage+1}&rows=${computerPage.rows}&search=${computerPage.search}&orderBy=${computerPage.orderBy}&isAscending=${computerPage.isAscending}"
						aria-label="Previous"> <span aria-hidden="true">&raquo;</span>
					</a></li>
				</c:if>
			</ul>
			<div class="btn-group btn-group-sm pull-right" role="group">
				<a
					href="dashboard?page=${computerPage.numPage}&rows=10&search=${computerPage.search}&orderBy=${computerPage.orderBy}&isAscending=${computerPage.isAscending}"><button
						type="button" class="btn btn-default">10</button></a> <a
					href="dashboard?page=${computerPage.numPage}&rows=50&search=${computerPage.search}&orderBy=${computerPage.orderBy}&isAscending=${computerPage.isAscending}"><button
						type="button" class="btn btn-default">50</button></a> <a
					href="dashboard?page=${computerPage.numPage}&rows=100&search=${computerPage.search}&orderBy=${computerPage.orderBy}&isAscending=${computerPage.isAscending}"><button
						type="button" class="btn btn-default">100</button></a>
			</div>
		</div>

	</footer>
	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/dashboard.js"></script>

</body>
</html>