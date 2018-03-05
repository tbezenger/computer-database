package com.excilys.formation.tbezenger;

public class Strings {
	// CONEXION BDD
	public static String url = "jdbc:mysql://127.0.0.1:3306/computer-database-db?useSSL=false";
	public static String dbName = "admincdb";
	public static String dbPassword = "qwerty1234";


	// JSP ATTRIBUTES/PARAMETERS
	public static final String NAME_FIELD = "computerName";
	public static final String INTRODUCED_FIELD = "introduced";
	public static final String DISCONTINUED_FIELD = "discontinued";
	public static final String COMPANY_FIELD = "companyId";
	public static final String COMPUTER = "computer";
	public static final String COMPANIES = "companies";
	public static final String ID = "id";
	public static final String DASHBOARD = "dashboard";
	public static final String ERRORS = "errors";
	public static final String COMPUTER_COUNT = "computerCount";
	public static final String ROWS = "rows";
	public static final String PAGE = "page";
	public static final String LINKS = "links";
	public static final String COMPUTER_PAGE = "computerPage";
	public static final String SELECTION = "selection";
	public static final String SELECTION_SEPARATOR = ",";


	// JSP VIEWS
	public static final String EDIT_COMPUTER_VIEW = "/WEB-INF/views/editComputer.jsp";
	public static final String ADD_COMPUTER_VIEW = "/WEB-INF/views/addComputer.jsp";
	public static final String DASHBOARD_VIEW = "/WEB-INF/views/dashboard.jsp";

}
