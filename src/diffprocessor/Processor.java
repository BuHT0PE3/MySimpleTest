package diffprocessor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import diffprocessor.SortedLimitedList.Entry;

/**
 * Created by VavilauA on 6/19/2015.
 */
public class Processor {
	private static long limit;
	
    Processor(long limit) {
        // TODO: initialize.
    	Processor.limit = limit;
    }

    public void doProcess(SortedLimitedList<Double> mustBeEqualTo, SortedLimitedList<Double> expectedOutput) {
    	
        // TODO: make "mustBeEqualTo" list equal to "expectedOutput".
        // 0. Processor will be created once and then will be used billion times.
        // 1. Use methods: AddFirst, AddLast, AddBefore, AddAfter, Remove to modify list.
        // 2. Do not change expectedOutput list.
        // 3. At any time number of elements in list could not exceed the "Limit".
        // 4. "Limit" will be passed into Processor's constructor. All "mustBeEqualTo" and "expectedOutput" lists will have the same "Limit" value.
        // 5. At any time list elements must be in non-descending order.
        // 6. Implementation must perform minimal possible number of actions (AddFirst, AddLast, AddBefore, AddAfter, Remove).
        // 7. Implementation must be fast and do not allocate excess memory.
    	
    	Map<Double, Integer> numberOfValuesOfEqualList = convertToMap(mustBeEqualTo);
		Map<Double, Integer> numberOfValuesOfExpectedList = convertToMap(expectedOutput);
		Map<Double, Integer> numberOfActionsWithEqualList = combineMaps(numberOfValuesOfEqualList, numberOfValuesOfExpectedList);
		
		Entry<Double> currentEntry = mustBeEqualTo.getFirst();
		Iterator<Map.Entry<Double, Integer>> iterator = numberOfActionsWithEqualList.entrySet().iterator();
		Map.Entry<Double, Integer> currentPair;
		
		if (expectedOutput.getCount() == 0) {
			int counter = mustBeEqualTo.getCount();
			for (int i = 0; i < counter; i++) {
				mustBeEqualTo.remove(currentEntry);
				currentEntry = mustBeEqualTo.getFirst();
			}
		} else {
			while (iterator.hasNext()) {
				currentPair = iterator.next();
				
				if (currentPair.getValue() < 0) {
					while (currentEntry != null) {
						if (currentEntry.getValue().equals(currentPair.getKey())) {
							for (int i = 0; i > currentPair.getValue(); i--) {
								if (!currentEntry.getValue().equals(currentPair.getKey())) {
									currentEntry = currentEntry.getNext();
								}
								mustBeEqualTo.remove(currentEntry);
								currentEntry = mustBeEqualTo.getFirst();
							}
							currentEntry = mustBeEqualTo.getFirst();
							break;
						}
						currentEntry = currentEntry.getNext();
					}
				}
			}
			iterator = numberOfActionsWithEqualList.entrySet().iterator();
			currentEntry = mustBeEqualTo.getFirst();
			
			while (iterator.hasNext()) {
				
				currentPair = iterator.next();
				
				if (currentPair.getValue() > 0 && mustBeEqualTo.getCount() > 0) {
					
					while (currentEntry!=null) {
						if (currentPair.getKey() < currentEntry.getValue()) {
							for (int i = 0; i < currentPair.getValue(); i++) {
								mustBeEqualTo.addFirst(currentPair.getKey());
							}
							break;
						} else if (currentPair.getKey().equals(currentEntry.getValue())) {
							for (int i = 0; i < currentPair.getValue(); i++) {
								mustBeEqualTo.addBefore(currentEntry, currentPair.getKey());
							}
							break;
						} else if (currentPair.getKey() > currentEntry.getValue()) {
							if (currentPair.getKey() > mustBeEqualTo.getLast().getValue()) {
								for (int i = 0; i < currentPair.getValue(); i++) {
									mustBeEqualTo.addLast(currentPair.getKey());
								}
								break;
							} else if (currentPair.getKey() < currentEntry.getNext().getValue()) {
								for (int i = 0; i < currentPair.getValue(); i++) {
									mustBeEqualTo.addAfter(currentEntry, currentPair.getKey());
								}
								break;
							} else {
								currentEntry = currentEntry.getNext();
							}
						}
					}
					
				} else if (currentPair.getValue() > 0 && mustBeEqualTo.getCount()==0) {
					for (int i = 0; i < currentPair.getValue(); i++) {
						mustBeEqualTo.addFirst(currentPair.getKey());
					}
					currentEntry = mustBeEqualTo.getFirst();
				}
			}
		}
    }
    
    private static Map<Double, Integer> combineMaps(Map<Double, Integer> mustBeEqual, Map<Double, Integer> expected) {
		
		Map<Double, Integer> finalAmountOfElements = new TreeMap<Double, Integer>();
		
		Set<Double> expectedSet = expected.keySet();
		Iterator<Double> iterator = expectedSet.iterator();
		while (iterator.hasNext())
	    {
	        Double currentElement = iterator.next();
	        if (mustBeEqual.containsKey(currentElement)) {
	        	Integer amountOfElementsToInsert = expected.get(currentElement) - mustBeEqual.get(currentElement);
	        	finalAmountOfElements.put(currentElement, amountOfElementsToInsert);
	        } else {
	        	finalAmountOfElements.put(currentElement, expected.get(currentElement));
	        }
	    }
		
		Set<Double> equalSet = mustBeEqual.keySet();
		iterator = equalSet.iterator();
		while (iterator.hasNext())
	    {
	        Double currentElement = iterator.next();
	        if (!expected.containsKey(currentElement)) {
	        	Integer amountOfElementsToRemove = mustBeEqual.get(currentElement);
	        	amountOfElementsToRemove = -amountOfElementsToRemove;
	        	finalAmountOfElements.put(currentElement, amountOfElementsToRemove);
	        }
	    }
		return finalAmountOfElements;
		
	}
	
	private static Map<Double, Integer> convertToMap(SortedLimitedList<Double> list) {
		
		Map<Double, Integer> amountOfElementsMap = new HashMap<Double, Integer>();
		Entry<Double> curretnEntry = list.getFirst();
		
		while (curretnEntry != null) {
			if (amountOfElementsMap.containsKey(curretnEntry.getValue())) {
				Integer amountOfElements = amountOfElementsMap.get(curretnEntry.getValue());
				amountOfElementsMap.put(curretnEntry.getValue(), ++amountOfElements);
			} else {
				amountOfElementsMap.put(curretnEntry.getValue(), 1);
			}
			curretnEntry = curretnEntry.getNext();
		}
		return amountOfElementsMap;
	}
    
}
