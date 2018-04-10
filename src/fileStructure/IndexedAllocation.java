package fileStructure;

import java.util.ArrayList;

public class IndexedAllocation extends AllocationStrategy {

	

	public IndexedAllocation(Integer n, Integer sz) {
		super(n,sz);
	}
	
	@Override
	public MemoryBlock allocate(FileStructure f) {
		Integer mnExpectedBlocks = (f.fileSize + blockSize - 1) / blockSize;
		ArrayList<MemoryBlock> freeBlocks = new ArrayList<MemoryBlock>();
		MemoryBlock ret = null;
		for (int i = 0; i < numberOfBlocks && freeBlocks.size() < mnExpectedBlocks; ++i) {
			if (memory.get(i).allocatedFile == null) {
				freeBlocks.add(memory.get(i));
			}
		}
		if (freeBlocks.size() == mnExpectedBlocks) {
			for (int i = 0; i < freeBlocks.size(); ++i) {
				freeBlocks.get(i).allocatedFile = f;
				if (i + 1 == freeBlocks.size())
					freeBlocks.get(i).nextBlock = -1;
				freeBlocks.get(i).nextBlock = i + 1;
				if (ret == null)
					ret = freeBlocks.get(i);

			}
		}
		return ret;
	}

	
	

}
