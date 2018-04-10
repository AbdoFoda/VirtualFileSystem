package fileStructure;

public class MemoryBlock {
	public MemoryBlock(Integer id) {
		this.blockSize = AllocationStrategy.singleTone.blockSize;
		this.blockId = id;
		allocatedFile = null;
		nextBlock = -1;
	}
	public MemoryBlock(Integer id,Integer blockSize) {
		this.blockSize = blockSize;
		this.blockId = id;
		allocatedFile = null;
		nextBlock = -1;
	}

	public void freeBlock() {
		allocatedFile = null;
		nextBlock = -1;
	}

	public Integer blockId;
	public Integer blockSize;
	public FileStructure allocatedFile;
	public Integer nextBlock;
}
