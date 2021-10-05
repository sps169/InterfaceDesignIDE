package documents;

import lombok.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Data
public class Document{

	public String title;
	public Path file;
	public String content;

	public Document (String path){
		initFile(path);
		this.title = this.file.getFileName().toString();
	}

	public void initFile(String path) {
		if (Files.exists(Paths.get(path))) {
			this.file = Paths.get(path);
		}else{
			try{
				this.file = Files.createFile(Paths.get(path));
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}
	}
	public boolean readDocument(){
		boolean success = false;
		try {
			byte[] bytes = Files.readAllBytes(this.file);
			this.content = new String(bytes, StandardCharsets.UTF_8);
			success = true;
		}catch(IOException e){
			e.printStackTrace();
		}
		return success;
	}

	public void saveDocument(){
		boolean success = false;
		try{
			Files.write(Paths.get(this.file.toRealPath().toString()), this.content.getBytes(StandardCharsets.UTF_8));
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
}
