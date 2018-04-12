package fileStructure;

import java.util.ArrayList;
import java.util.HashMap;

public class ExtentAllocation extends AllocationStrategy {

	public ExtentAllocation(Integer n, Integer sz) {
		super(n, sz);
	}

	public static HashMap<String, Pair<Integer, Integer>> pathToBlock = new HashMap<String, Pair<Integer, Integer>>();

	@Override
	public Boolean allocate(FileStructure f) { // using best-fit
		// <left,right>>
		ArrayList<Pair<Integer, Integer>> allPossible = new ArrayList<Pair<Integer, Integer>>();
		Integer mnExpectedBlocks = (f.fileSize + blockSize - 1) / blockSize;
		// this called ceil divison:V
		Integer l = -1, r = -1;
		for (int i = 0; i < numberOfBlocks; ++i) {
			if (memory.get(i).allocatedFile == null) {
				if (l == -1) {
					l = r = i;
				} else {
					r++;
				}
			} else {
				if (l != -1 && r - l + 1 >= mnExpectedBlocks) {
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
			int end = Math.min(start + mnExpectedBlocks - 1, allPossible.get(mnIdx).second);
			for (int i = start; i <= end; ++i) {
				MemoryBlock block = memory.get(i);
				block.allocatedFile = f;
			}
			pathToBlock.put(f.path, new Pair<Integer, Integer>(start, end));
			return true;
		}
		return false;
	}

	@Override
	public Boolean deAllocate(FileStructure f) {
		// TODO Auto-generated method stub
		if (!pathToBlock.containsKey(f.path))
			return false;
		Integer start = pathToBlock.get(f.path).first;
		Integer end = pathToBlock.get(f.path).second;
		for (int cur = start; cur <= end; ++cur) {
			memory.get(cur).freeBlock();
		}
		return true;
	}

}
