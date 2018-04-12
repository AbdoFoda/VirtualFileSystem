package fileStructure;

import java.util.ArrayList;
public class IndexMemoryBlock extends MemoryBlock {

	public ArrayList <Integer> blocksList ;
	public IndexMemoryBlock(Integer id) {
		super(id);
		blocksList = new ArrayList<Integer>();
	}
	
	public IndexMemoryBlock(Integer id,ArrayList<Integer> blocks) {
		super(id);
		this.blocksList = blocks;
	}
}
