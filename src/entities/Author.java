package entities;

public class Author {
	private int id;
	private String name;
	private String bio;
	private byte[] photo;

	public Author() {
		super();
	}

	public Author(int id, String name, String bio, byte[] photo) {
		super();
		this.id = id;
		this.name = name;
		this.bio = bio;
		this.photo = photo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	@Override
	public String toString() {
		return "Author [id=" + id + ", name=" + name + ", bio=" + bio + ", photo=" + photo + "]";
	}
}
