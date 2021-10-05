import documents.Document;

public class Main {
	public static void main (String[] args){
		Document document = new Document("src\\main\\resources\\files\\file.java");
		document.readDocument();
		document.setContent(document.getContent() + "hola caracola");
		document.saveDocument();
		System.out.println(document.content);
	}
}
