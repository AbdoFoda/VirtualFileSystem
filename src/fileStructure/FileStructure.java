package fileStructure;

public class FileStructure {
	public String path;
	public Integer fileSize;
	public MemoryBlock startingBlock;
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

	FileStructure(String path, Integer fileSize) {
		this.path = path;
		this.fileSize = fileSize;
	}
}
