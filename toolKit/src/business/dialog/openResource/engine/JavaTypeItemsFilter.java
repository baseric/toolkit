package business.dialog.openResource.engine;

import org.eclipse.ui.dialogs.SearchPattern;

import business.dialog.openResource.dialogs.JavaTypeNameMatch;
/**
 * 
 * @author CuiKun cuikunbj@cn.ibm.com
 *
 */
public class JavaTypeItemsFilter {

	private SearchPattern searchPattern;

	public JavaTypeItemsFilter(String pattern) {
		super();
		searchPattern = new SearchPattern();
		searchPattern.setPattern(pattern);
	}

	public boolean isConsistentItem(Object item) {
		return true;
	}

	public String getPattern() {
		return searchPattern.getPattern();
	}

	public boolean equalsFilter(JavaTypeItemsFilter newFilter) {
		if (newFilter != null) {
			return newFilter.getSearchPattern().equals(this.searchPattern);
		}
		return false;
	}

	public boolean isSubFilter(JavaTypeItemsFilter itemsFilter) {
		if (itemsFilter != null) {
			return this.searchPattern.isSubPattern(itemsFilter
					.getSearchPattern());
		}
		return false;
	}

	public SearchPattern getSearchPattern() {
		return searchPattern;
	}
	
	public boolean matchItem(Object item) {
		if (!(item instanceof JavaTypeNameMatch)) {
			return false;
		}
		JavaTypeNameMatch type = (JavaTypeNameMatch) item;
		return matches(type.getAuthorName());
	}

	public boolean matches(String text) {
		if (text == null || "".equals(text)) {
			return false;
		}
		return searchPattern.matches(text);
	}

}
