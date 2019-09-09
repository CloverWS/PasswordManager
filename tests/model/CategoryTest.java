package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.fail;

import java.time.Period;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class CategoryTest {
	Category emptyCat1;
	Category emptyCat2;
	Category emptyCat3;
	
	Category catWithSubs1;
	Category catWithSubs2;
	Category catWithSubs3;
	
	Category catWithMoreSubs1;
	Category catWithMoreSubs2;
	
	Category sub1;
	Category sub2;
	

	
	//
	TreeSet<UserPassword> rootList;

	UserPassword pass1;
	UserPassword pass2;
	UserPassword pass3;
	
	
	
	// Define Categories 
		private Category root; 
		private Category firstChild; //Has no Passwords
		private Category secondChild; // Has Passwords
		
		// Define passwords 
		private UserPassword password1;
		private UserPassword password2;
		private UserPassword password3;

	@Before
	public void setUp() throws Exception {
		this.emptyCat1 = new Category("c");
		this.emptyCat2 = new Category("a");
		this.emptyCat3 = new Category("c");
		
		this.catWithSubs1 = new Category("a");
		this.catWithSubs2 = new Category("a");
		this.catWithSubs3 = new Category("a");
		this.catWithSubs1.addSubCategory(emptyCat1);
		this.catWithSubs2.addSubCategory(emptyCat3);
		this.catWithSubs3.addSubCategory(new Category("c"));
		
		this.pass1 = new UserPassword("pass1", Period.ofDays(3));
		this.pass2 = new UserPassword("pass2", Period.ofDays(3));
		this.pass3 = new UserPassword("pass3", Period.ofDays(3));
		
		this.catWithSubs1.addPassword(pass1);
		this.catWithSubs1.addPassword(pass2);
		
		this.catWithSubs2.addPassword(pass1);
		this.catWithSubs2.addPassword(pass2);
		
		this.catWithMoreSubs1 = new Category("x");
		this.catWithMoreSubs2 = new Category("x");
		
		this.sub1 = new Category("b");
		this.sub1.addSubCategory(emptyCat1);
		this.sub2 = new Category("b");
		this.sub2.addSubCategory(emptyCat3);
		
		emptyCat1.addPassword(pass1);
		emptyCat3.addPassword(pass1);
		emptyCat2.addPassword(pass2);
		
		this.catWithMoreSubs1.addSubCategory(sub1);
		this.catWithMoreSubs2.addSubCategory(sub2);
		root = new Category("root", true);
		
		firstChild = new Category("firstChild");
		secondChild = new Category("secondChild");
		
		
		// Add SubCategories to root
		root.addSubCategory(firstChild);
		root.addSubCategory(secondChild);
		
		// initialize passwords 
		password1 = new UserPassword("123",Period.ofDays(1));
		password2 = new UserPassword("44",Period.ofDays(2));
		password3 = new UserPassword("123",Period.ofDays(3));
		
		
		// Add Passwords to secondChild
		secondChild.addPassword(password1);
		secondChild.addPassword(password2);
		secondChild.addPassword(password3);
		
		
		// create allPasswords list for root node
		rootList = new TreeSet<UserPassword>();
		rootList.add(password1);
		rootList.add(password2);
		rootList.add(password3);
		
		
	}

	@Test
	public void testIsRoot() {
		// root is as root defined
		assertTrue(root.isRoot());
		// firstChild is not a root
		assertFalse(firstChild.isRoot());
		
	}

	@Test
	public void testIsLeaf() {
		// Check if root node is leaf
		assertFalse(root.isLeaf());
		// Check if child of root node is leaf
		assertTrue(firstChild.isLeaf());
		
	}
	
	@Test
	public void testIsEmpty() {
		// firstChild has no passwords in passwords list.
		assertTrue(firstChild.isEmpty());
		// secondChild has three passwords in passwords list.
		assertFalse(secondChild.isEmpty());
	}

	@Test
	public void testAddSubCategory() {
		assertEquals(0, emptyCat1.getSubCategoriesClone().size());
		
		catWithSubs1.addSubCategory(emptyCat1);
		assertEquals(1, catWithSubs1.getSubCategoriesClone().size());
		
		catWithSubs1.addSubCategory(emptyCat2);
		assertEquals(2, catWithSubs1.getSubCategoriesClone().size());
	}

	@Test
	public void testRemoveSubCategory() {
		catWithSubs1.removeSubCategory(emptyCat1);
		assertTrue(catWithSubs1.isLeaf());
		
		assertTrue(emptyCat1.isLeaf());
	}

	@Test
	public void testGetAllPasswords() {
		
		
		// before adding passwords to firstChild -> firstChild is empty
		assertTrue(rootList.equals(root.getAllPasswords()));
		
		//passwords list of firstChild (empty)
		assertTrue(new TreeSet<UserPassword>().equals( firstChild.getAllPasswords()));
		
		
		// create two extra passwords 
		UserPassword pass1 = new UserPassword("dafewxx22", Period.ofDays(4));
		UserPassword pass2 = new UserPassword("dafewxx44", Period.ofDays(5));
		
		// make a set of them 
		TreeSet<UserPassword> passList = new TreeSet<UserPassword>();
		passList.add(pass1);passList.add(pass2); 
		
		
		// add two extra passwords to the empty node firstChild 
		firstChild.addPassword(pass1);
		firstChild.addPassword(pass2);
		
		
		// firstChild has new passwords  
		assertTrue(passList.equals(firstChild.getAllPasswords()));
		
		// update root passwordsList
		rootList.add(pass1);
		rootList.add(pass2);
		
		
		// after adding passwords to firstChild -> firstChild is not empty 
		assertTrue( rootList.equals(root.getAllPasswords()));

		// passwords list of secondChild is no more equals to rootList
		assertFalse(root.getAllPasswords().equals(secondChild.getAllPasswords()));
		
		
		
	}

	@Test
	public void testContainsPassword() {
		// root has no passwords
		assertFalse(root.containsPassword(password1));
		
		// secondChild has list of passwords that has password2
		assertTrue(secondChild.containsPassword(password2));
		
		// firstChild has no passwords in its passwordslist
		assertFalse(firstChild.containsPassword(new UserPassword("66666", Period.ofDays(3))));
	}

	@Test
	public void testContainsPasswordInAll() {
		// password is in category
		assertTrue(emptyCat1.containsPasswordInAll(pass1));
		// password is in sub category
		UserPassword pass4 = new UserPassword("aa", Period.ofDays(3));
		emptyCat1.addPassword(pass4);
		assertTrue(catWithSubs1.containsPasswordInAll(pass4));
		// password is null
		assertFalse(emptyCat1.containsPasswordInAll(null));
		// category is empty
		Category cat = new Category("cc");
		assertFalse(cat.containsPasswordInAll(pass4));
	}

	@Test
	public void testAddPassword() {
		// assert first addPassword in setUp works
		assertEquals(1,emptyCat1.getPasswordsClone().size());
		emptyCat1.addPassword(pass3);
		assertEquals(2,emptyCat1.getPasswordsClone().size());
	}

	@Test
	public void testRemovePassword() {
		// remove pass from cat that only has one pass
		emptyCat1.removePassword(pass1);
		assertTrue(emptyCat1.isEmpty());
		// remove pass from empty cat
		emptyCat1.removePassword(pass2);
		assertTrue(emptyCat1.isEmpty());
		// remove null from normal cat
		emptyCat2.removePassword(null);
		assertFalse(emptyCat2.isEmpty());
	}

	@Test
	public void testReplace() {
		emptyCat1.addPassword(new UserPassword("pass2", Period.ofDays(3)));
		emptyCat1.addPassword(new UserPassword("pass1", Period.ofDays(3)));
		emptyCat1.replace(pass1);
		assertFalse(emptyCat1.isEmpty());
		assertEquals(2, emptyCat1.getPasswordsClone().size());
	}

	@Test
	public void testCompareTo() {
		assertEquals(0, emptyCat1.compareTo(emptyCat3));
		assertEquals(0, catWithMoreSubs1.compareTo(catWithMoreSubs2));
		assertEquals(0, catWithSubs1.compareTo(catWithSubs2));
	}

}
