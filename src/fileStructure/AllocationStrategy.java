package fileStructure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class AllocationStrategy {
	public Integer blockSize, numberOfBlocks;
	
	public ArrayList<MemoryBlock> memory;
	
	public static AllocationStrategy singleTone = null;
	
	public AllocationStrategy(Integer n, Integer sz) {
		this.numberOfBlocks = n;
		this.blockSize = sz;
		memory = new ArrayList<MemoryBlock>();
		for (int i = 0; i < n; ++i) {
			memory.add(i, new MemoryBlock(i, blockSize));
		}
	}
	
	public static void setSingleTone(AllocationStrategy strategy) {
		singleTone = strategy;
	}
	
	
	public abstract Boolean allocate(FileStructure f) ;

	public abstract Boolean deAllocate(FileStructure f);
//
//	public void saveMemoryState() {
//		File f = new File("memoryState.txt");
//		try {
//			f.createNewFile();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		try {
//			PrintWriter writer = new PrintWriter(f);
//			writer.println(numberOfBlocks + " " + blockSize);
//			for (int i = 0; i < numberOfBlocks; ++i) {
//				writer.println((memory.get(i).allocatedFile == null ? "null" : memory.get(i).allocatedFile.path) + " "
//						+ memory.get(i).nextBlock);
//			}
//			writer.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void loadMemoryState() {
//		File f = new File("memoryState.txt");
//		try {
//			if (!f.createNewFile()) {
//				return;
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		try {
//			Scanner sc = new Scanner(f);
//			numberOfBlocks = sc.nextInt();
//			blockSize = sc.nextInt();
//			if (memory == null)
//				memory = new ArrayList<MemoryBlock>();
//			for (int i = 0; i < numberOfBlocks; ++i) {
//				MemoryBlock block = new MemoryBlock(i);
//				block.allocatedFile = new FileStructure(sc.next());
//				if(block.allocatedFile.path == "null")
//					block.allocatedFile = null;
//				block.nextBlock = sc.nextInt();
//				memory.add(block);
//			}
//			sc.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

}
