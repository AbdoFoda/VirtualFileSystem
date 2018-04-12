package fileStructure;

import java.util.ArrayList;

public class FolderStructure {
	public String path;
	public ArrayList<FolderStructure> folders;
	public ArrayList<FileStructure> files;
	public FolderStructure parentFolder;

	FolderStructure(String path) {
		this.path = path;
		folders = new ArrayList<FolderStructure>();
		files = new ArrayList<FileStructure>();
		parentFolder = null;
	}

	String getName() {
		if (path.equals("root"))
			return path;
		return path.substring(path.lastIndexOf('/') + 1);
	}

	public Boolean createFile(FileStructure file) {
		for (int i = 0; i < files.size(); ++i) {
			if (files.get(i).path.equals(file.path))
				return false;
		}
		if (!AllocationStrategy.singleTone.allocate(file)) {
			return false;
		}
		files.add(file);
		file.parentFolder = this;
		return true;
	}

	public Boolean createFolder(FolderStructure folder) {
		for (int i = 0; i < folders.size(); ++i) {
			if (folders.get(i).path.equals(folder.path)) {
				return false;
			}
		}
		folders.add(folder);
		folder.parentFolder = this;
		return true;
	}

	public Boolean deleteFile(FileStructure file) {
		for (int i = 0; i < files.size(); ++i) {
			if (files.get(i).path.equals(file.path)) {
				if (AllocationStrategy.singleTone.deAllocate(files.get(i))) {
					files.remove(i);
					return true;
				}
			}
		}
		return false;
	}

	public Boolean deleteFolder(FolderStructure folder) {
		for (int i = 0; i < folders.size(); ++i) {
			if (folders.get(i).path.equals(folder.path)) {
				for (int j = 0; j < folder.folders.size(); ++j) {
					folder.deleteFolder(folder.folders.get(j));
				}
				for (int j = 0; j < folder.files.size(); ++j) {
					folder.deleteFile(folder.files.get(j));
				}
				folders.remove(i);
				return true;
			}
		}
		return false;
	}
}
