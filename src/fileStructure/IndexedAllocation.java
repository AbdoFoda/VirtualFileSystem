package fileStructure;

import java.util.ArrayList;

public class IndexedAllocation implements AllocationStrategy {
	private Integer blockSize, numberOfBlocks;
	private ArrayList<MemoryBlock> memory;

	public static IndexedAllocation singleTone = new IndexedAllocation();

	public IndexedAllocation(Integer n, Integer sz) {
		setNumberOfBlocks(n);
		setBlockSize(sz);
		memory = new ArrayList<MemoryBlock>(n);
		for (int i = 0; i < n; ++i) {
			memory.set(i, new MemoryBlock(i, sz));
		}
	}

	public IndexedAllocation() {
		blockSize = 0;
		numberOfBlocks = 0;
		memory = new ArrayList<MemoryBlock>();
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

	@Override
	public Integer getBlockSize() {
		// TODO Auto-generated method stub
		return blockSize;
	}

	@Override
	public Integer getNumberOfBlocks() {
		// TODO Auto-generated method stub
		return numberOfBlocks;
	}

	@Override
	public void setBlockSize(Integer blockSize) {
		// TODO Auto-generated method stub
		this.blockSize = blockSize;
	}

	@Override
	public void setNumberOfBlocks(Integer numberOfBlocks) {
		// TODO Auto-generated method stub
		this.numberOfBlocks = numberOfBlocks;
	}

	@Override
	public ArrayList<MemoryBlock> getMemory() {
		// TODO Auto-generated method stub
		return memory;
	}

	@Override
	public void setMemory(ArrayList<MemoryBlock> memory) {
		// TODO Auto-generated method stub
		this.memory = memory;

	}

	@Override
	public void deAllocate(FileStructure f) {
		// TODO Auto-generated method stub
		MemoryBlock cur = f.startingBlock;
		int nxt = cur.nextBlock;
		cur.freeBlock();
		while (nxt != -1) {
			cur = memory.get(nxt);
			nxt = cur.nextBlock;
			cur.freeBlock();
		}
	}

}
