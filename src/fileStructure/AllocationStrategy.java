package fileStructure;

import java.util.ArrayList;

public interface AllocationStrategy {
	public Integer getBlockSize();

	public void setBlockSize(Integer blockSize);

	public Integer getNumberOfBlocks();

	public void setNumberOfBlocks(Integer numberOfBlocks);

	public ArrayList<MemoryBlock> getMemory();

	public void setMemory(ArrayList<MemoryBlock> memory);

	public void deAllocate(FileStructure f);

	MemoryBlock allocate(FileStructure f);
}
