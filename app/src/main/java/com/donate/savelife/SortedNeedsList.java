package com.donate.savelife;


import com.donate.savelife.core.requirement.model.Need;

import java.util.Comparator;

/**
 * Provides a {@code SortedList} which sorts the elements by their
 * natural order. 
 *
 * @author Mark Rhodes
 * @version 1.1
 * @see SortedList
 */
public class SortedNeedsList
		extends SortedList<Need> {

	private static final long serialVersionUID = -8834713008973648930L;

	/**
	 * Constructs a new {@code NaturalSortedList} which sorts elements
	 * according to their <i>natural order</i>.
	 */
	public SortedNeedsList(){
		super(new Comparator<Need>(){
			public int compare(Need one, Need two){
				if(one.equals(two)) {
					return 0;
				} else if(one.getTimeStamp() < two.getTimeStamp()) { //Compares in descending order
					return 1;
				} else {
					return -1;
				}
			}
		});
	}
}
