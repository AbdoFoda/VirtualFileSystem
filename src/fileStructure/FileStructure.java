package fileStructure;

public class FileStructure {
	public String path;
	public Integer fileSize;
	public FolderStructure parentFolder;

	public Boolean deleteFile() {
		if (parentFolder != null) {
			parentFolder.deleteFile(this);
			return true;
		}
		return false;
	}

	String getName() {
		return path.substring(path.lastIndexOf('/') + 1);
	}
	
	FileStructure(){
		this.path = "";
		this.fileSize = 0;
		parentFolder = null;
	}
	
	FileStructure(String path, Integer fileSize) {
		this.path = path;
		this.fileSize = fileSize;
	}
	
	FileStructure(String path){
		this.path = path;
		this.fileSize = 0;
		parentFolder = null;
	}
}
