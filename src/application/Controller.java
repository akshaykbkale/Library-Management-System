package application;

import java.sql.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import application.dbConnection;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable{

	@FXML // fx:id="title"
	private TextField title; // Value injected by FXMLLoader

	@FXML // fx:id="author"
	private TextField author; // Value injected by FXMLLoader

	@FXML // fx:id="isbn"
	private TextField isbn; // Value injected by FXMLLoader

	@FXML
	private Button butt_update; // Value injected by FXMLLoader
    
	@FXML
    private Label showLabel;
 
	@FXML
	private TextArea text_description; // Value injected by FXMLLoader

	@FXML
	private TextField text_edition; // Value injected by FXMLLoader

	@FXML
	private TextField text_dep; // Value injected by FXMLLoader

	@FXML
	private TextField text_quantity; // Value injected by FXMLLoader

	@FXML
	private TextField text_pages; // Value injected by FXMLLoader


	@FXML // fx:id="butt_save"
	private Button butt_save; // Value injected by FXMLLoader

	@FXML // fx:id="table_view"
	private TableView<BookDetails> table_view; // Value injected by FXMLLoader

	@FXML // fx:id="col_title"
	private TableColumn<BookDetails, String> col_title; // Value injected by FXMLLoader

	@FXML // fx:id="col_isbn"
	private TableColumn<BookDetails, String> col_isbn; // Value injected by FXMLLoader

	@FXML // fx:id="col_authors"
	private TableColumn<BookDetails, String> col_authors; // Value injected by FXMLLoader

	@FXML // fx:id="publisher"
	private ComboBox<String> publisher; // Value injected by FXMLLoader

	@FXML // fx:id="book_type"
	private ComboBox<String> book_type; // Value injected by FXMLLoader
	
	

    @FXML
    private TableColumn<BookDetails, String> col_department;

    @FXML
    private TableColumn<BookDetails, String> col_type;


	@FXML // fx:id="butt_clear"
	private Button butt_clear; // Value injected by FXMLLoader

	@FXML // fx:id="butt_search"
	private Button butt_search; // Value injected by FXMLLoader
	
    @FXML
    private TextField text_dds;

	private ObservableList<String> bookType = FXCollections.observableArrayList("Book","Periodical","Audio","Video","E-Books","Other");
	
	private ObservableList<BookDetails> table=FXCollections.observableArrayList();
	
    @FXML
    Label status;


    
    @FXML // this is to clear values of UI type.
    public void clearText(ActionEvent event) {
    	title.clear();
    	author.clear();
    	isbn.clear();
    	text_edition.clear();
    	text_description.clear();
    	text_dep.clear();
    	text_pages.clear();
    	text_quantity.clear();
    	text_dds.clear();
    	table_view.getItems().clear();
    	book_type.getSelectionModel().clearSelection();
    	publisher.getSelectionModel().clearSelection();
    	status.setText("you can enter new details");  
    	}
	
    @FXML //this search values for non null given UI
    public void searchResource(ActionEvent event) {
    	dc = new dbConnection();
    	Connection conn;
    	int counter=0;
		try {
			conn = dc.Getconnection();
			Statement stmt=conn.createStatement();
			String bookType=book_type.getSelectionModel().getSelectedItem();
			if(bookType==null) {
				bookType="";
			}
			//generating query for different insert and update depending upon   
			String Query="select * from (select E.isbn as 'ISBN', E.book_title as 'TITLE',D.author as 'AUTHOR',E.department as 'DEPARTMENT',D.resource_type as 'RES_TYPE' from lib_book_author D join lib_book E on D.resource_id=E.isbn \r\n" + 
					"union\r\n" + 
					"select G.isbn as 'ISBN', G.non_book_title as 'TITLE',F.author as 'AUTHOR',G.department as 'DEPARTMENT',F.resource_type as 'RES_TYPE' from lib_non_book_author F join lib_non_book G on F.resource_id=G.isbn \r\n" + 
					"union\r\n" + 
					"select I.issn as 'ISBN', I.periodical_title as 'TITLE',H.author as 'AUTHOR',I.department as 'DEPARTMENT',H.resource_type as 'RES_TYPE' from lib_periodicals_author H join lib_periodicals I on H.resource_id=I.issn \r\n" + 
					") as tablw where ISBN like ? and ISBN like '%%' and TITLE like ? and TITLE like '%%' and AUTHOR like ? and AUTHOR like '%%' and DEPARTMENT like ? and DEPARTMENT like '%%' and RES_TYPE like ? and RES_TYPE like '%%'"  ;
			
			
			List<String>values = Arrays.asList(new String[]{"%"+isbn.getText(),"%"+title.getText()+"%","%"+author.getText()+"%","%"+text_dep.getText()+"%","%"+bookType});

			PreparedStatement stmt2 = conn.prepareStatement(Query);
			System.out.println(values.size());
			for( int i = 0 ; i < values.size(); i=i+1 ) {
				++counter;
				System.out.println(values.get(i));
				System.out.println(i);
				stmt2.setString(counter,values.get(i));

			}
			//ResultSet rs = getResultset(Query, conn);
				ResultSet rs=stmt2.executeQuery();
			while(rs.next())  {
				System.out.println("1."+rs.getNString(1));
				System.out.println("2."+rs.getNString(2));
				System.out.println("3."+rs.getNString(3));
				System.out.println("4."+rs.getNString(4));
				System.out.println("5."+rs.getNString(5));
				
				
				table.add(new BookDetails(rs.getNString(1),rs.getNString(2),rs.getNString(3),rs.getNString(4),rs.getNString(5)));

			}
			table_view.setItems(null);
			table_view.setItems(table);

			status.setText("Search was sucessfull"); 
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
	@FXML // this is to update resource details
	public void updateResource(ActionEvent event) {
		dc = new dbConnection();
		String val=null;
		Connection conn;
		int counter=0;
		int rowAffected;
		
		try {
			conn = dc.Getconnection();
			Statement stmt=conn.createStatement();
			String bookType=book_type.getSelectionModel().getSelectedItem();					
			//(periodical_title,issn,author,res_description,quantity,department)"
			if(bookType=="Periodical") {

				String updatePeriodical= "UPDATE library.lib_periodicals set periodical_title=CASE WHEN (? = '') THEN periodical_title ELSE ? END ,issn=CASE WHEN (? = '') THEN issn ELSE ? END,res_description=CASE WHEN (? = '') THEN res_description ELSE ? END,quantity=CASE WHEN (? = '') THEN quantity ELSE ? END,department=CASE WHEN (? = '') THEN department ELSE ? END WHERE issn=CASE WHEN (? = '') THEN issn ELSE ? END";

				PreparedStatement stmt1 = conn.prepareStatement(updatePeriodical);

				List<String>possibleValue = Arrays.asList(new String[]{title.getText(),isbn.getText(),text_description.getText(),text_quantity.getText(),text_dep.getText(),isbn.getText()});


				System.out.println(possibleValue.size());
				for( int i = 0 ; i < possibleValue.size(); i=i+1 ) {
					++counter;
					stmt1.setString(counter,possibleValue.get(i));
					++counter;
					stmt1.setString(counter,possibleValue.get(i));

				}
				 rowAffected = stmt1.executeUpdate();

				System.out.println(rowAffected);
			}
			else if(bookType=="Book") {


				//(book_title,isbn,author,res_description,edition,quantity,no_pages,department)
				String updateBook="UPDATE library.lib_book set book_title=CASE WHEN (? = '') THEN book_title ELSE ? END, isbn=CASE WHEN (? = '') THEN isbn ELSE ? END,"
						+ " res_description=CASE WHEN (? = '') THEN res_description ELSE ? END, edition=CASE WHEN (? = '') THEN edition ELSE ? END, quantity=CASE WHEN (? = '') THEN quantity ELSE ? END, no_pages=CASE WHEN (? = '') THEN no_pages ELSE ? END, department=CASE WHEN (? = '') THEN department ELSE ? END WHERE isbn=CASE WHEN (? = '') THEN isbn ELSE ? END";

				PreparedStatement stmt2 = conn.prepareStatement(updateBook);
				List<String>possibleValue = Arrays.asList(new String[]{title.getText(),isbn.getText(),text_description.getText(),text_edition.getText(),text_quantity.getText(),text_pages.getText(),text_dep.getText(),isbn.getText()});


				//System.out.println(possibleValue.size());
				for( int i = 0 ; i < possibleValue.size(); i=i+1 ) {
					++counter;
					stmt2.setString(counter,possibleValue.get(i));
					++counter;
					stmt2.setString(counter,possibleValue.get(i));

				}
				rowAffected = stmt2.executeUpdate();
				
			}


			else {
				//non_book_title,isbn,author,res_description,edition,quantity,type,department
				String updateNonBook="UPDATE library.lib_non_book SET non_book_title=CASE WHEN (? = '') THEN non_book_title ELSE ? END, isbn=CASE WHEN (? = '') THEN isbn ELSE ? END, "
						+ " res_description=CASE WHEN (? = '') THEN res_description ELSE ? END, edition=CASE WHEN (? = '') THEN edition ELSE ? END, quantity=CASE WHEN (? = '') THEN quantity ELSE ? END,type=CASE WHEN (? = '') THEN type ELSE ? END, department=CASE WHEN (? = '') THEN department ELSE ? END WHERE isbn=CASE WHEN (? = '') THEN isbn ELSE ? END";

				PreparedStatement stmt3 = conn.prepareStatement(updateNonBook);

				List<String>possibleValue = Arrays.asList(new String[]{title.getText(),isbn.getText(),text_description.getText(),text_quantity.getText(),text_edition.getText(),bookType,text_dep.getText(),isbn.getText()});

				for( int i = 0 ; i < possibleValue.size(); i=i+1 ) {
					++counter;
					stmt3.setString(counter,possibleValue.get(i));
					++counter;
					stmt3.setString(counter,possibleValue.get(i));

				}
			rowAffected = stmt3.executeUpdate();
			}
			
			butt_search.setOnAction(this::searchResource);
			System.out.println(rowAffected);
			searchResource(event);
			
			status.setText(rowAffected +" rows updated");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}


		//String bookType=book_type.getSelectionModel().getSelectedItem();

	}

	@FXML //this is to insert values tables 
	private void insertResourceDetail(ActionEvent event) {
		dc = new dbConnection();
		System.out.println(Integer.valueOf(isbn.getText()));

		if (isbn.getText().length() == 13 || isbn.getText().length() == 10) {    
		try {
			

			Connection conn=dc.Getconnection();
			Statement stmt=conn.createStatement();

			String bookType=book_type.getSelectionModel().getSelectedItem();					

			if(bookType=="Periodical") {
				stmt.executeUpdate("INSERT INTO library.lib_periodicals(periodical_title,issn,res_description,quantity,ddn,department) "
						+ "VALUES ('"+title.getText()+"','"+isbn.getText()+"','"+text_description.getText()+"','"+text_quantity.getText()+"','"+text_dds.getText()+"','"+text_dep.getText()+"')");
				for (String a : author.getText().split(",",-2)) {
				stmt.executeUpdate("insert into lib_periodicals_author(resource_type,resource_id,author) values ('"+bookType+"','"+isbn.getText()+"','"+a+"')");
				}
			}
			else if(bookType=="Book") {

				stmt.executeUpdate("INSERT INTO library.lib_book(book_title,isbn,res_description,edition,quantity,no_pages,ddn,department)"
						+ " VALUES ('"+title.getText()+"','"+isbn.getText()+"','"+text_description.getText()+"','"+text_edition.getText()+"','"+text_quantity.getText()+"','"+text_pages.getText()+"','"+text_dds.getText()+"','"+text_dep.getText()+"')");
				stmt.executeUpdate("commit");
				for (String a : author.getText().split(",",-2)) {
				stmt.executeUpdate("insert into lib_book_author(resource_type,resource_id,author) values ('"+bookType+"','"+isbn.getText()+"','"+a+"')");}
			}
			else {
				stmt.executeUpdate("INSERT INTO library.lib_non_book(non_book_title,isbn,res_description,edition,quantity,type,ddn,department)"
						+ " Values ('"+title.getText()+"','"+isbn.getText()+"','"+text_description.getText()+"','"+text_edition.getText()+"','"+text_quantity.getText()+"','"+bookType+"','"+text_dds.getText()+"','"+text_dep.getText()+"')");
				for (String a : author.getText().split(",",-2)) {
				stmt.executeUpdate("insert into lib_non_book_author(resource_type,resource_id,author) values ('"+bookType+"','"+isbn.getText()+"','"+a+"')");}
			}
			searchResource(event);
			status.setText("rows added successfully and you can update resource details now");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			status.setText("Error in inserting the Data");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		else {
			status.setText("incorrect isbn value");
		}

	}

	private dbConnection dc;


	public ResultSet getResultset(String Query,Connection con) {

		ResultSet rs = null;
		try {
			Statement stmt=con.createStatement(); 
			rs = stmt.executeQuery(Query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	private ObservableList<String> pub=FXCollections.observableArrayList();
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		
				try {			isbn.textProperty().addListener(new ChangeListener<String>() {
						@Override
						public void changed(ObservableValue<? extends String> observable, String oldValue, 
						String newValue) {
							
						if (!newValue.matches("\\d*")) {
							
							isbn.setText(newValue.replaceAll("[^\\d]", ""));
							
									}
						
					}
				});
		

						dc = new dbConnection();
						// connecting to database
						Connection conn=dc.Getconnection();
						Statement stmt=conn.createStatement();
						ResultSet rs=stmt.executeQuery("select distinct publisher_name from lib_resource_publisher;");
						//to update values of publishers
						while(rs.next())  {
							pub.add(rs.getNString(1));
						System.out.println(rs.getNString(1));
						}
						publisher.setItems(pub);

			//assigning values to GroupBox BookType
			book_type.setItems(bookType);

			//Initializing values of tableView 
			col_title.setCellValueFactory(new PropertyValueFactory<BookDetails, String>("title"));
			col_isbn.setCellValueFactory(new PropertyValueFactory<BookDetails, String>("ISBN"));
			col_authors.setCellValueFactory(new PropertyValueFactory<BookDetails, String>("authors"));
			col_department.setCellValueFactory(new PropertyValueFactory<BookDetails, String>("department"));
			col_type.setCellValueFactory(new PropertyValueFactory<BookDetails, String>("type"));
			//function for buttons of ui 
			butt_clear.setOnAction(this::clearText);
			butt_save.setOnAction(this::insertResourceDetail);
			butt_update.setOnAction(this::updateResource);
			butt_search.setOnAction(this::searchResource);
			publisher.setItems(pub);


		} catch (Exception e) {

			e.printStackTrace();
		}

	}



}
