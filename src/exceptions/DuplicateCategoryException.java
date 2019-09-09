package exceptions;

/**
 * thrown when a category has a sibling with the same name
 */
public class DuplicateCategoryException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public DuplicateCategoryException(String message) {
		super(message);
	}
}
