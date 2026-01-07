package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import entities.Author;
import entities.Category;

public class CategoriesModel {
	
	//getAll
	public List<Category> findAll() {
		List<Category> categories = new ArrayList<Category>();
		try {
			PreparedStatement preparedStatement = ConnectDB.connection().prepareStatement("select * from category order by id desc");
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Category category = new Category();
				category.setId(resultSet.getInt("id"));
				category.setName(resultSet.getString("name"));
				category.setDescription(resultSet.getString("description"));
				categories.add(category);
			}
		} catch (Exception e) {
			e.printStackTrace();
			categories = null;
			// TODO: handle exception
		} 
		finally {
			ConnectDB.disconnect();
		}
		
		return categories;
	}
	
	//create
	public boolean create(Category category) {
		boolean result = true;
		try {
			PreparedStatement preparedStatement = ConnectDB.connection().prepareStatement("insert into category(name, description) values(?,?)");
			preparedStatement.setString(1, category.getName());
			preparedStatement.setString(2, category.getDescription());
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
		public boolean update(Category cate) {
			boolean result = true;
			try {
				PreparedStatement preparedStatement = ConnectDB.connection()
						.prepareStatement("update category set name = ?, description = ? where id = ?");
				
				preparedStatement.setString(1, cate.getName());
				preparedStatement.setString(2, cate.getDescription());
				preparedStatement.setInt(3, cate.getId());
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
		public Category findById(int id) {
			Category cate = null;
			try {
				PreparedStatement preparedStatement = ConnectDB.connection()
						.prepareStatement("select * from category where id = ?");
				preparedStatement.setInt(1, id);
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					cate = new Category();
					cate.setId(resultSet.getInt("id"));
					cate.setName(resultSet.getString("name"));
					cate.setDescription(resultSet.getString("description"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				cate = null;
			} 
			finally {
				ConnectDB.disconnect();
			}
			return cate;
		}
		

		//delete
		public boolean delete(int id) {
			boolean result = true;
			try {
				PreparedStatement preparedStatement = ConnectDB.connection()
						.prepareStatement("delete from category where id = ?");	
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
