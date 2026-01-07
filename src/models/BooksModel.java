package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import entities.Author;
import entities.Book;

public class BooksModel {
	
	
	public List<Book> findAll() {
	    List<Book> books = new ArrayList<>();
	    try {
	        PreparedStatement ps = ConnectDB.connection().prepareStatement(
	            "SELECT b.*, " +
	            "a.name AS author_name, " +
	            "c.name AS category_name " +
	            "FROM book b " +
	            "JOIN author a ON b.author_id = a.id " +
	            "JOIN category c ON b.category_id = c.id " +
	            "ORDER BY b.id DESC"
	        );

	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            Book book = new Book();
	            book.setId(rs.getInt("id"));
	            book.setIsbn(rs.getString("isbn"));
	            book.setTitle(rs.getString("title"));
	            book.setCall_number(rs.getString("call_number"));
	            book.setDescription(rs.getString("description"));
	            book.setPrice(rs.getDouble("price"));

	            book.setAuthor_id(rs.getInt("author_id"));
	            book.setCategory_id(rs.getInt("category_id"));

	            book.setAuthor_name(rs.getString("author_name"));     
	            book.setCategory_name(rs.getString("category_name"));  

	            book.setPublication_year(rs.getInt("publication_year"));
	            book.setStock(rs.getInt("stock"));
	            book.setPhoto(rs.getBytes("photo"));

	            books.add(book);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        books = null;
	    } finally {
	        ConnectDB.disconnect();
	    }
	    return books;
	}
	
	//findById
	public Book findById(int id) {
		Book book = null;
		try {
			PreparedStatement preparedStatement = ConnectDB.connection()
					.prepareStatement("select * from book where id = ?");
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				 	book = new Book();
		            book.setId(rs.getInt("id"));
		            book.setIsbn(rs.getString("isbn"));
		            book.setTitle(rs.getString("title"));
		            book.setCall_number(rs.getString("call_number"));
		            book.setDescription(rs.getString("description"));
		            book.setPrice(rs.getDouble("price"));

		            book.setAuthor_id(rs.getInt("author_id"));
		            book.setCategory_id(rs.getInt("category_id"));

		            book.setPublication_year(rs.getInt("publication_year"));
		            book.setStock(rs.getInt("stock"));
		            book.setPhoto(rs.getBytes("photo"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			book = null;
		} 
		finally {
			ConnectDB.disconnect();
		}
		return book;
	}

}
