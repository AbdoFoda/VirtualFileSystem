package fileStructure;

import java.util.ArrayList;
import java.util.HashMap;

public class IndexedAllocation extends AllocationStrategy {

	public static HashMap<String, IndexMemoryBlock> pathToBlock = new HashMap<String, IndexMemoryBlock>();

	public IndexedAllocation() {
		super(0,0);
	}
	public IndexedAllocation(Integer n, Integer sz) {
		super(n, sz);
	}

	@Override
	public Boolean allocate(FileStructure f) {
		f.fileSize += AllocationStrategy.singleTone.blockSize; // adding index block
		Integer mnExpectedBlocks = (f.fileSize + blockSize - 1) / blockSize;
		ArrayList<MemoryBlock> freeBlocks = new ArrayList<MemoryBlock>();
		for (int i = 0; i < numberOfBlocks && freeBlocks.size() < mnExpectedBlocks; ++i) {
			if (memory.get(i).allocatedFile == null) {
				freeBlocks.add(memory.get(i));
			}
		}
		if (freeBlocks.size() == mnExpectedBlocks) {

			ArrayList<Integer> indicies = new ArrayList<Integer>();
			for (int i = 1; i < freeBlocks.size(); ++i) {
				freeBlocks.get(i).allocatedFile = f;
				indicies.add(freeBlocks.get(i).blockId);
			}
			IndexMemoryBlock newIndex = new IndexMemoryBlock(freeBlocks.get(0).blockId, indicies);
			memory.set(freeBlocks.get(0).blockId, newIndex);
			newIndex.allocatedFile = f;
			pathToBlock.put(f.path, newIndex);
			
			return true;
		}
		return false;
	}

	@Override
	public Boolean deAllocate(FileStructure f) {
		if (!pathToBlock.containsKey(f.path))
			return false;
		IndexMemoryBlock idxBlock = pathToBlock.get(f.path);
		for (int i = 0; i < idxBlock.blocksList.size(); ++i) {
			memory.get(idxBlock.blocksList.get(i)).freeBlock();
		}
		idxBlock.freeBlock();
		return true;
	}

}
