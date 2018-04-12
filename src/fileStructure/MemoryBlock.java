package fileStructure;

public class MemoryBlock {
	public Integer blockId;
	public Integer blockSize;
	public FileStructure allocatedFile;
	
	public MemoryBlock(Integer id) {
		this.blockSize = AllocationStrategy.singleTone.blockSize;
		this.blockId = id;
		allocatedFile = null;
	}
	
	public MemoryBlock(Integer id,Integer blockSize) {
		this.blockSize = blockSize;
		this.blockId = id;
		allocatedFile = null;
	}

	public void freeBlock() {
		allocatedFile = null;
	}

	
	
}
