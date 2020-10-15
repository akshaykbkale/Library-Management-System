package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BookDetails {
	
	private String ISBN;
	private String Title;
	private String Authors;
	private String department;
	private String  type;
	public BookDetails() {

	}
	public BookDetails(String ISBN, String Title, String Authors,String department,String type) {
		// TODO Auto-generated constructor stub
		
		this.ISBN =ISBN;
		this.Title = Title;
		this.Authors = Authors;
		this.department=department;
		this.type=type;
	}
	
	public String getDepartment() {
		return department;
	}
	public String getType() {
		return type;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getISBN() {
		return ISBN;
	}
	public String getTitle() {
		return Title;
	}
	public String getAuthors() {
		return Authors;
	}
	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public void setAuthors(String authors) {
		Authors = authors;
	}
	
	

}
