package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import entities.Author;



public class AuthorsModel {
	
	//getAll
	public List<Author> findAll (){
		List<Author> authors = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = ConnectDB.connection().prepareStatement("select * from author order by id desc"); 
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				var author = new Author();
				author.setId(resultSet.getInt("id"));
				author.setName(resultSet.getString("name"));
				author.setBio(resultSet.getString("bio"));
				author.setPhoto(resultSet.getBytes("photo"));
				authors.add(author);
			}
		} catch(Exception e) {
			e.printStackTrace();
			authors = null;
		}
		
		finally {
			ConnectDB.disconnect();
		}
		
		return authors;
	}
	
	//create
	public boolean create(Author author) {
		boolean result = true;
		try {
			PreparedStatement preparedStatement = ConnectDB.connection().prepareStatement("insert into author(name, bio, photo) values(?,?,?)");
			preparedStatement.setString(1, author.getName());
			preparedStatement.setString(2, author.getBio());
			if(author.getPhoto() != null){
				preparedStatement.setBytes(3, author.getPhoto());
			} else{
				preparedStatement.setNull(3, java.sql.Types.BLOB);
		}
		
			result = preparedStatement.executeUpdate() > 0;
		}catch(Exception e) {
			e.printStackTrace();
			result = false;
		}
		
		finally {
			ConnectDB.disconnect();
		}
		return result;
	}
	
	//update
	public boolean update(Author author) {
		boolean result = true;
		try {
			PreparedStatement preparedStatement = ConnectDB.connection()
					.prepareStatement("update author set name = ?, bio = ?, photo = ? where id = ?");
			
			preparedStatement.setString(1, author.getName());
			preparedStatement.setString(2, author.getBio());
			preparedStatement.setBytes(3, author.getPhoto());
			preparedStatement.setInt(4, author.getId());
			result = preparedStatement.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			ConnectDB.disconnect();
		}
		return result;
	}
	
	//findById
	public Author findById(int id) {
		Author aut = null;
		try {
			PreparedStatement preparedStatement = ConnectDB.connection()
					.prepareStatement("select * from author where id = ?");
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				aut = new Author();
				aut.setId(resultSet.getInt("id"));
				aut.setName(resultSet.getString("name"));
				aut.setBio(resultSet.getString("bio"));
				aut.setPhoto(resultSet.getBytes("photo"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			aut = null;
		} 
		finally {
			ConnectDB.disconnect();
		}
		return aut;
	}
	
	//delete
	public boolean delete(int id) {
		boolean result = true;
		try {
			PreparedStatement preparedStatement = ConnectDB.connection()
					.prepareStatement("delete from author where id = ?");	
			preparedStatement.setInt(1, id);
			result = preparedStatement.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			// TODO: handle exception
		} 
		finally {
			ConnectDB.disconnect();
		}
		
		return result;
	}
}
