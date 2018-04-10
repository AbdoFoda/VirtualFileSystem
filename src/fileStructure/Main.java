package fileStructure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class Main {

	public static void spaces(Integer lvl) {
		for (int i = 0; i < lvl; ++i) {
			System.out.print("\t");
		}
	}

	private static class folderStructure {
		public static HashMap<String, FolderStructure> existingFolders = new HashMap<String, FolderStructure>();
		public static FolderStructure root = new FolderStructure("root");
		private static String filePath = "folderStructure.txt";

		// private static
		public static void save() {
			File f = new File(filePath);
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(filePath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			for (Entry<String, FolderStructure> entry : existingFolders.entrySet()) {
				writer.println(
						entry.getKey() + " " + entry.getValue().folders.size() + " " + entry.getValue().files.size());
				for (int i = 0; i < entry.getValue().folders.size(); ++i) {
					writer.println(entry.getValue().folders.get(i).path);
				}
				for (int i = 0; i < entry.getValue().files.size(); ++i) {
					writer.println(entry.getValue().files.get(i).path + " " + entry.getValue().files.get(i).fileSize
							+ " " + entry.getValue().files.get(i).startingBlock.blockId);
				}
			}

		}

		public static void load() {
			File f = new File(filePath);
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Scanner sc = new Scanner(f);
				while (sc.hasNext()) {
					String folderName = sc.next();
					Integer numOfFolders = sc.nextInt();
					Integer numOfFiles = sc.nextInt();
					FolderStructure parent = null;
					if (!existingFolders.containsKey(folderName)) {
						parent = new FolderStructure(folderName);
						existingFolders.put(folderName, parent);
					} else {
						parent = existingFolders.get(folderName);
					}
					for (int i = 0; i < numOfFolders; ++i) {
						String childFolderPath = sc.next();
						if (!existingFolders.containsKey(childFolderPath)) {
							FolderStructure childFolder = new FolderStructure(childFolderPath);
							childFolder.parentFolder = parent;
							existingFolders.put(folderName, childFolder);
						}
					}
					for (int i = 0; i < numOfFiles; ++i) {
						FileStructure childFile = new FileStructure();
						childFile.path = sc.next();
						childFile.fileSize = sc.nextInt();
						childFile.startingBlock = new MemoryBlock(sc.nextInt());
						parent.files.add(childFile);
					}
				}
				sc.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if (!existingFolders.containsKey("root"))
				existingFolders.put("root", root);
		}

	}

	public static Boolean createFile(String path, Integer space) {
		if (!path.contains("/"))
			return false;
		String parentPath = path.substring(0, path.lastIndexOf('/'));
		if (folderStructure.existingFolders.containsKey(parentPath)) {
			FolderStructure parent = folderStructure.existingFolders.get(parentPath);
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
		if (folderStructure.existingFolders.containsKey(parentPath)) {
			FolderStructure parent = folderStructure.existingFolders.get(parentPath);
			if (parent.createFolder(new FolderStructure(path))) {
				folderStructure.existingFolders.put(path, parent.folders.get(parent.folders.size() - 1));
				return true;
			}
		}
		return false;
	}

	public static Boolean deleteFolder(String path) {
		if (!path.contains("/") || !folderStructure.existingFolders.containsKey(path))
			return false;
		String parentPath = path.substring(0, path.lastIndexOf('/'));
		if (folderStructure.existingFolders.containsKey(parentPath)) {
			FolderStructure parent = folderStructure.existingFolders.get(parentPath);
			if (parent.deleteFolder(folderStructure.existingFolders.get(path))) {
				folderStructure.existingFolders.remove(path);
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
			System.out.println(cur.files.get(i).getName() + " (" + cur.files.get(i).fileSize + ")KB");
		}
		for (int i = 0; i < cur.folders.size(); ++i) {
			printStructure(cur.folders.get(i), lvl + 1);
		}
	}

	public static void printStatus() {

		Integer freeSpace = 0, allocatedSpace = 0, freeBlocks = 0, allocatedBlocks = 0;
		ArrayList<MemoryBlock> memory = AllocationStrategy.singleTone.memory;
		for (int i = 0; i < memory.size(); ++i) {
			if (memory.get(i).allocatedFile == null) {
				System.out.println("Free :" + i);
				freeBlocks++;
			} else {
				System.out.println("Allocated :" + i);
				allocatedBlocks++;
			}
		}
		freeSpace = freeBlocks * AllocationStrategy.singleTone.blockSize;
		allocatedSpace = allocatedBlocks * AllocationStrategy.singleTone.blockSize;
		System.out.println("Free Space =" + freeSpace);
		System.out.println("allocated Space=" + allocatedSpace);
		System.out.println("# of free blocks=" + freeBlocks);
		System.out.println("# of allocated Blocks=" + allocatedBlocks);
	}

	public static Boolean deleteFile(String path) {
		if (!path.contains("/"))
			return false;
		String parentPath = path.substring(0, path.lastIndexOf('/'));
		if (folderStructure.existingFolders.containsKey(parentPath)) {
			FolderStructure parent = folderStructure.existingFolders.get(parentPath);
			if (parent.deleteFile(new FileStructure(path, 0)))
				return true;
		}
		return false;
	}

	public static void main(String[] args) {

		AllocationStrategy.setSingleTone(new ExtentAllocation(10, 1));
		folderStructure.load();
		AllocationStrategy.singleTone.loadMemoryState();
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
				printStructure(folderStructure.root, 0);
			} else {
				System.out.println("Unknown Command");
			}
		}
		sc.close();
		folderStructure.save();
		AllocationStrategy.singleTone.saveMemoryState();
	}
}
