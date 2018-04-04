package fileStructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
	static FolderStructure root = new FolderStructure("root");

	public static void spaces(Integer lvl) {
		for (int i = 0; i < lvl; ++i) {
			System.out.print("\t");
		}
	}

	
	public static HashMap<String, FolderStructure> existingFolders = new HashMap<String, FolderStructure>();

	public static Boolean createFile(String path, Integer space) {
		if (!path.contains("/"))
			return false;
		String parentPath = path.substring(0, path.lastIndexOf('/'));
		if (existingFolders.containsKey(parentPath)) {
			FolderStructure parent = existingFolders.get(parentPath);
			if (parent.createFile(new FileStructure(path, space))) {
				return true;
			}
		}
		return false;
	}

	public static Boolean createFolder(String path) {
		if (!path.contains("/"))
			return false;
		String parentPath = path.substring(0, path.lastIndexOf('/'));
		if (existingFolders.containsKey(parentPath)) {
			FolderStructure parent = existingFolders.get(parentPath);
			if (parent.createFolder(new FolderStructure(path))) {
				existingFolders.put(path, parent.folders.get(parent.folders.size()-1));
				return true;
			}
		}
		return false;
	}

	public static Boolean deleteFolder(String path) {
		if (!path.contains("/"))
			return false;
		String parentPath = path.substring(0, path.lastIndexOf('/'));
		if (existingFolders.containsKey(parentPath)) {
			FolderStructure parent = existingFolders.get(parentPath);
			if (parent.deleteFolder(new FolderStructure(path))) {
				existingFolders.remove(path);
				return true;
			}
		}
		return false;
	}

	public static void printStructure(FolderStructure cur, Integer lvl) {
		spaces(lvl);
		System.out.println(cur.getName());
		for (int i = 0; i < cur.files.size(); ++i) {
			spaces(lvl + 1);
			System.out.println(cur.files.get(i).getName()+" ("+cur.files.get(i).fileSize+")KB");
		}
		for (int i = 0; i < cur.folders.size(); ++i) {
			printStructure(cur.folders.get(i), lvl + 1);
		}
	}

	public static void printStatus() {
		Integer freeSpace = 0, allocatedSpace = 0, freeBlocks = 0, allocatedBlocks = 0;
		ArrayList<MemoryBlock> memory = FolderStructure.strategy.getMemory();
		for (int i = 0; i < memory.size(); ++i) {
			if (memory.get(i).allocatedFile == null) {
				freeBlocks++;
			} else {
				allocatedBlocks++;
			}
		}
		freeSpace = freeBlocks * FolderStructure.strategy.getBlockSize();
		allocatedSpace = allocatedBlocks * FolderStructure.strategy.getBlockSize();
		System.out.println("Free Space =" + freeSpace);
		System.out.println("allocated Space=" + allocatedSpace);
		System.out.println("# of free blocks=" + freeBlocks);
		System.out.println("# of allocated Blocks=" + allocatedBlocks);
	}

	public static Boolean deleteFile(String path) {
		if (!path.contains("/"))
			return false;
		String parentPath = path.substring(0, path.lastIndexOf('/'));
		System.out.println(parentPath);
		if (existingFolders.containsKey(parentPath)) {
			FolderStructure parent = existingFolders.get(parentPath);
			System.out.println(parent);
			if (parent.deleteFile(new FileStructure(path, 0)))
				return true;
		}
		return false;
	}

	public static void main(String[] args) {
		FolderStructure.strategy = new ExtentAllocation(10, 3);
		existingFolders.put("root", root);
		Scanner sc = new Scanner(System.in);
		String cmd = "";
		while (!cmd.equals("exit")) {
			cmd = sc.nextLine();
			String arr[] = cmd.split(" ");
			if (arr[0].equals("CreateFile")) {
				if (createFile(arr[1], Integer.parseInt(arr[2]))) {
					System.out.println("The file has been created");
					// printStructure(root,0);
				} else {
					System.out.println("Error crating file");
				}
			} else if (arr[0].equals("CreateFolder")) {
				if (createFolder(arr[1])) {
					System.out.println("The folder has been created");
				} else {
					System.out.println("Error crating folder");

				}
			} else if (arr[0].equals("DeleteFile")) {
				if (deleteFile(arr[1])) {
					System.out.println("The file has been deleted");
				} else {
					System.out.println("Error deleting the file");
				}
			} else if (arr[0].equals("DeleteFolder")) {
				if (deleteFolder(arr[1])) {
					System.out.println("The folder has been deleted");
				} else {
					System.out.println("Error deleting the folder");
				}
			} else if (arr[0].equals("DisplayDiskStatus")) {
				printStatus();
			} else if (arr[0].equals("DisplayDiskStructure")) {
				printStructure(root, 0);
			} else {
				System.out.println("Unknown Command");
			}
		}
		sc.close();
	}
}
