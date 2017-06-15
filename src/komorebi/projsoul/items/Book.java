package komorebi.projsoul.items;

//Represents a book item in the game, which can be read
public class Book extends CharacterItem
{
	int pages;
	String story;
	
	public Book(String name, int salePrice, int quantity, int pages, String words, String title) 
	{
		super(name, salePrice, quantity, title);
		this.pages = pages;
		story = words;
	}
	
	public void readBook(Book currentBook)
	{
		
	}

}
