package fileStructure;

import java.util.ArrayList;

public class ExtentAllocation implements AllocationStrategy {
	private Integer blockSize, numberOfBlocks;
	private ArrayList<MemoryBlock> memory;

	public ExtentAllocation(Integer n, Integer sz) {
		setNumberOfBlocks(n);
		setBlockSize(sz);
		memory = new ArrayList<MemoryBlock>();
		for (int i = 0; i < n; ++i) {
			memory.add(i, new MemoryBlock(i, sz));
		}
	}

	@Override
	public MemoryBlock allocate(FileStructure f) { // using best-fit
		// <left,right>>
		ArrayList<Pair<Integer, Integer>> allPossible = new ArrayList<Pair<Integer, Integer>>();
		Integer mnExpectedBlocks = (f.fileSize + blockSize - 1) / blockSize;
		// this called ceil divison:V
		Integer l = -1, r = -1;
		MemoryBlock ret = null;
		for (int i = 0; i < numberOfBlocks; ++i) {
			if (memory.get(i).allocatedFile == null) {
				if (l == -1) {
					l = r = i;
				} else {
					r++;
				}
			} else {
				if (r - l + 1 >= mnExpectedBlocks) {
					allPossible.add(new Pair<Integer, Integer>(l, r));
				}
				l = r = -1;
			}
		}
		if (l != -1) {
			if (r - l + 1 >= mnExpectedBlocks) {
				allPossible.add(new Pair<Integer, Integer>(l, r));
			}
		}
		if (allPossible.size() > 0) {
			Integer mnVal = 100000000, mnIdx = -1;
			for (int i = 0; i < allPossible.size(); ++i) {
				if (r - l + 1 < mnVal) {
					mnVal = r - l + 1;
					mnIdx = i;
				}

			}
			int start = allPossible.get(mnIdx).first;
			int end = Math.min(start + mnExpectedBlocks-1, allPossible.get(mnIdx).second);
			for (int i = start; i <= end; ++i) {
				MemoryBlock block = memory.get(i);
				block.allocatedFile = f;
				block.nextBlock = ((i == end) ? -1 : i + 1);
				if (ret == null)
					ret = block;
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
		while (true) {
			cur.allocatedFile = null;
			int nxt = cur.nextBlock;
			cur.nextBlock = -1;
			if (nxt == -1)
				break;
			cur = memory.get(nxt);
		}
	}

}
