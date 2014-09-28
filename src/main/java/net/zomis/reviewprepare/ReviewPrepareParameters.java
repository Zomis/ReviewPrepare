package net.zomis.reviewprepare;

import com.beust.jcommander.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * JCommander parameters class
 */
public class ReviewPrepareParameters {

	@Parameter
	private List<String> parameters = new ArrayList<>();

	@Parameter(names = {"--tabwidth"}, description = "Replace tabs with N spaces.")
	private Integer tabWidth = 4;

	@Parameter(names = "--replace-tabs", description = "Replace tabs with spaces. Default size is 4.")
	private boolean replaceTabsEnabled = false;

	@Parameter(names = "--strip-todo", description = "Remove lines matching ^\\s+//\\s*TODO.")
	private boolean stripTodoEnabled = false;

	@Parameter(names = "--keep-imports", description = "Keep imports.")
	private boolean keepImportsEnabled = false;

	public List<String> getParameters() {
		return parameters;
	}

	public Integer getTabWidth() {
		return tabWidth;
	}

	public boolean isReplaceTabsEnabled() {
		return replaceTabsEnabled;
	}

	public boolean isStripTodoEnabled() {
		return stripTodoEnabled;
	}

	public boolean isKeepImportsEnabled() {
		return keepImportsEnabled;
	}
	
	
}
