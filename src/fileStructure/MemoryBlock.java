package fileStructure;

public class MemoryBlock {
	public MemoryBlock(Integer id, Integer size) {
		// TODO Auto-generated constructor stub
		this.blockSize = size;
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
