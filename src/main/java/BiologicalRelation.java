import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BiologicalRelation {

	int cousinship;
	int separation;
	
	BiologicalRelation(){
		this.cousinship = 0;
		this.separation = 0;
	}
	
	/**
	 * this method finds the counsinship and removalship of two persons.
	 * 
	 * @param p1 object of the personIdentity class
	 * @param p2 object of the personIdentity class
	 * @return object of the BiologolicalRelation class that contains two values counsinship and removal
	 */
	BiologicalRelation findRelation(PersonIdentity p1, PersonIdentity p2) {
		
		BiologicalRelation bR = new BiologicalRelation();
		
		FamilyMap fM = new FamilyMap();

		// HashMap that has key as parent and values as children
		Map<Integer, List<Integer>> fTree = fM.createMap();
		
		// List of parent 
		List<Integer> p11List = new ArrayList<>();
		List<Integer> p12List = new ArrayList<>();
		
		// temp list of parent
		List<Integer> p1List = new ArrayList<>();
		List<Integer> p2List = new ArrayList<>();
		
		p1List.add(p1.id);
		p11List.add(p1List.get(0));
		p2List.add(p2.id);
		p12List.add(p2List.get(0));
		
		boolean flag = true;
		
		int findId1 = p1.id;
		int findId2 = p2.id;
		
		List<List<Integer>> savePoint1 = new ArrayList<>();
		List<List<Integer>> savePoint2 = new ArrayList<>();
		
		int location1 = 0;
		int location2 = 0;
		
		int size1 = 0;
		int size2 = 0;
		
		while(flag) {
		
			p1List = new ArrayList<>();	
			p2List = new ArrayList<>();	
				
			// find the parent 
			for(int key : fTree.keySet()) {
				
				if(fTree.get(key).contains(findId1)) {
				
					p1List.add(key);
					
				}
				
				if(fTree.get(key).contains(findId2)) {
				
					p2List.add(key);
					
				}
			}


			// if it has multiple parents, save the parent values.
			if(!p1List.isEmpty()) {
				if(p1List.size() > 1) {
					savePoint1.add(p1List);
				}
				p11List.add(p1List.get(0));
				findId1 = p11List.get(p11List.size() - 1);
			}

			if(!p2List.isEmpty()) {
				if(p2List.size() > 1) {
					savePoint2.add(p2List);
				}
				p12List.add(p2List.get(0));
				findId2 = p12List.get(p12List.size() - 1) ;
			}
			
			Boolean over = false;
			
			// finding the common parent 
			for(Integer common : p11List) {
				if(p12List.contains(common)) {
					location1 = p11List.indexOf(common);
					location2 = p12List.indexOf(common);
					over = true;
					break;
				}
			}
			
			if(over) {
					
					flag = false;
					bR.cousinship = Math.min(location1, location2) - 1;
					bR.separation = Math.abs(location1 - location2);
					break;
			}
			
			// check termination condition, by checking size of the previous list and current list
			if(p11List.size() == size1 && p12List.size() == size2 ) {
				if(savePoint1.isEmpty() && savePoint2.isEmpty()) {
					flag = false;
				}
				
				
				if(!savePoint1.isEmpty()) {
					findId1 = savePoint1.get(0).get(1);
					p11List.subList(p11List.indexOf(savePoint1.get(0).get(0)), p11List.size()).clear();
					p11List.add(findId1);
					savePoint1.remove(0);
				}
				if(!savePoint2.isEmpty()) {
					findId2 = savePoint2.get(0).get(1);
					p12List.subList(p12List.indexOf(savePoint2.get(0).get(0)), p12List.size()).clear();
					p12List.add(findId2);
					savePoint2.remove(0);
				}
				
			
				bR.cousinship = -99;
				bR.separation = -99;
			
			}
			
			size1 = p11List.size();
			size2 = p12List.size();
			
		}
		
		if(bR.cousinship == -99 && bR.separation == -99) {
			bR = null;
		}
		
		return bR;
	
		
	}

	/**
	 * This method returns the number of the cousinship from the objects.
	 * 
	 * @return cousinship value
	 */
	int getCousinship() {
		return this.cousinship;
	}

	/**
	 * This method returns the number of the removal from the objects.
	 * 
	 * @return removal value
	 */
	int getRemoval() {
		return this.separation;
	}
	
	
	
	
}
