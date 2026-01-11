package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import entities.Author;
import entities.Book;
import entities.DetailStatus;
import entities.Loan_Details;


public class BooksModel {

	public List<Book> findAll() {
		List<Book> books = new ArrayList<>();
		try {
			PreparedStatement ps = ConnectDB.connection()
					.prepareStatement("SELECT b.*, " + "a.name AS author_name, " + "c.name AS category_name "
							+ "FROM book b " + "JOIN author a ON b.author_id = a.id "
							+ "JOIN category c ON b.category_id = c.id " + "ORDER BY b.id DESC");

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
				book.setAvailable_quantity(rs.getInt("available_quantity")); // ✅ NEW
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
	
	
	
	//create
	public boolean create(Book book) {
		boolean result = true;
		try {
			PreparedStatement preparedStatement = ConnectDB.connection()
					.prepareStatement("insert into book(isbn, title, call_number, description, price, author_id, category_id, publication_year, stock, available_quantity, photo) values(?,?,?,?,?,?,?,?,?,?,?)");	
			preparedStatement.setString(1, book.getIsbn());
			preparedStatement.setString(2, book.getTitle());
			preparedStatement.setString(3, book.getCall_number());
			preparedStatement.setString(4, book.getDescription());
			preparedStatement.setDouble(5, book.getPrice());
			preparedStatement.setInt(6, book.getAuthor_id());
			preparedStatement.setInt(7, book.getCategory_id());
			preparedStatement.setInt(8, book.getPublication_year());
			preparedStatement.setInt(9, book.getStock());
			preparedStatement.setInt(10, book.getAvailable_quantity());
			preparedStatement.setBytes(11, book.getPhoto());
			result = preparedStatement.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} 
		finally {
			ConnectDB.disconnect();
		}
		
		return result;
	}
	
//	//update
//		public boolean update(Book book) {
//			boolean result = true;
//			try {
//				PreparedStatement preparedStatement = ConnectDB.connection()
//						.prepareStatement("update book set isbn = ?, title = ?, call_number = ?, description = ?, price = ?, author_id = ?, category_id = ?, publication_year = ?, stock = ?, available_quantity = ?, photo = ? where id = ?");
//				
//				preparedStatement.setString(1, book.getIsbn());
//				preparedStatement.setString(2, book.getTitle());
//				preparedStatement.setString(3, book.getCall_number());
//				preparedStatement.setString(4, book.getDescription());
//				preparedStatement.setDouble(5, book.getPrice());
//				preparedStatement.setInt(6, book.getAuthor_id());
//				preparedStatement.setInt(7, book.getCategory_id());
//				preparedStatement.setInt(8, book.getPublication_year());
//				preparedStatement.setInt(9, book.getStock());
//				preparedStatement.setInt(10, book.getAvailable_quantity());
//				preparedStatement.setBytes(11, book.getPhoto());
//				preparedStatement.setInt(12, book.getId());
//				result = preparedStatement.executeUpdate() > 0;
//			} catch (Exception e) {
//				e.printStackTrace();
//				result = false;
//			} finally {
//				ConnectDB.disconnect();
//			}
//			return result;
//		}

	// findById
	public Book findById(int id) {
		Book book = null;
		try {
			PreparedStatement preparedStatement = ConnectDB.connection()
					.prepareStatement( "SELECT b.*, " +
				            "       a.name AS author_name, " +
				            "       c.name AS category_name " +
				            "FROM book b " +
				            "JOIN author a ON b.author_id = a.id " +
				            "JOIN category c ON b.category_id = c.id " +
				            "WHERE b.id = ?");
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

				book.setAuthor_name(rs.getString("author_name"));
				book.setCategory_name(rs.getString("category_name"));
				
				book.setPublication_year(rs.getInt("publication_year"));
				book.setStock(rs.getInt("stock"));
				book.setAvailable_quantity(rs.getInt("available_quantity")); // ✅ NEW
				book.setPhoto(rs.getBytes("photo"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			book = null;
		} finally {
			ConnectDB.disconnect();
		}
		return book;
	}
	
	
	//update
	public boolean update(Book book) {
		boolean result = true;
		try {
			PreparedStatement preparedStatement = ConnectDB.connection()
					.prepareStatement("update book set description = ?, price = ?, stock = ?, available_quantity = ?, publication_year = ?, photo = COALESCE(?, photo) where id = ?");
			
			preparedStatement.setString(1, book.getDescription());
			preparedStatement.setDouble(2, book.getPrice());
			preparedStatement.setInt(3, book.getStock());
			preparedStatement.setInt(4, book.getAvailable_quantity());
			preparedStatement.setInt(5, book.getPublication_year());
			preparedStatement.setBytes(6, book.getPhoto());
			preparedStatement.setInt(7, book.getId());
			result = preparedStatement.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			ConnectDB.disconnect();
		}
		return result;
	}
	
	//delete
	public boolean delete(int id) {
		boolean result = true;
		try {
			PreparedStatement preparedStatement = ConnectDB.connection()
					.prepareStatement("delete from book where id = ?");	
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
	
	//findAllRepair
	public List<Loan_Details> findAllRepair() {
    List<Loan_Details> list = new ArrayList<>();

    try {
        String sql = """
            SELECT
                ld.id AS loan_detail_id,
                ld.loan_master_id,
                ld.late_fee,
                ld.compensation_fee,
                ld.return_date,
                ld.status,

                b.id AS book_id,
                b.photo,
                b.isbn,
                b.title,
                b.call_number,

                a.name AS author_name,
                c.name AS category_name
            FROM loan_details ld
            JOIN book b       ON ld.book_id = b.id
            JOIN author a     ON b.author_id = a.id
            JOIN category c   ON b.category_id = c.id
        """;

        PreparedStatement ps = ConnectDB.connection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            var ld = new Loan_Details();

            ld.setId(rs.getInt("loan_detail_id"));
            ld.setLoan_master_id(rs.getInt("loan_master_id"));
            ld.setBook_id(rs.getInt("book_id"));

            ld.setLate_fee(rs.getDouble("late_fee"));
            ld.setCompensation_fee(rs.getDouble("compensation_fee"));
            ld.setReturn_date(rs.getDate("return_date"));
//            ld.setStatus(rs.getString("status"));
            ld.setStatus(DetailStatus.valueOf(rs.getString("status")));

            ld.setPhoto(rs.getBytes("photo"));
            ld.setIsbn(rs.getString("isbn"));
            ld.setTitle(rs.getString("title"));
            ld.setCall_number(rs.getString("call_number"));
            ld.setAuthor_name(rs.getString("author_name"));
            ld.setCategory_name(rs.getString("category_name"));

            list.add(ld);
        }

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        ConnectDB.disconnect();
    }

    return list;
}



}
