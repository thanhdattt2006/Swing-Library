package entities;

public class Book {
	private int id;
	private String isbn;
	private String title;
	private String call_number;
	private String description;
	private double price;
	private int author_id;
	private int category_id;
	private int publication_year;
	private int stock;
	private String photo;

	public Book() {
		super();
	}

	public Book(int id, String isbn, String title, String call_number, String description, double price, int author_id,
			int category_id, int publication_year, int stock, String photo) {
		super();
		this.id = id;
		this.isbn = isbn;
		this.title = title;
		this.call_number = call_number;
		this.description = description;
		this.price = price;
		this.author_id = author_id;
		this.category_id = category_id;
		this.publication_year = publication_year;
		this.stock = stock;
		this.photo = photo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCall_number() {
		return call_number;
	}

	public void setCall_number(String call_number) {
		this.call_number = call_number;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(int author_id) {
		this.author_id = author_id;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public int getPublication_year() {
		return publication_year;
	}

	public void setPublication_year(int publication_year) {
		this.publication_year = publication_year;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", isbn=" + isbn + ", title=" + title + ", call_number=" + call_number
				+ ", description=" + description + ", price=" + price + ", author_id=" + author_id + ", category_id="
				+ category_id + ", publication_year=" + publication_year + ", stock=" + stock + ", photo=" + photo
				+ "]";
	}
}
