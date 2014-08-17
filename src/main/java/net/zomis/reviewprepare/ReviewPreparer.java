package net.zomis.reviewprepare;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ReviewPreparer {
	public static double detectAsciiness(File input) throws IOException {
		if (input.length() == 0)
			return 0;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(input)))) {
			int read;
			long asciis = 0;
			char[] cbuf = new char[1024];
			while ((read = reader.read(cbuf)) != -1) {
				for (int i = 0; i < read; i++) {
					char c = cbuf[i];
					if (c <= 0x7f)
						asciis++;
				}
			}
			return asciis / (double) input.length();
		}
	}

	private final List<File> files;

	public ReviewPreparer(List<File> files) {
		this.files = new ArrayList<>();

		for (File file : files) {
			if (file.getName().lastIndexOf('.') == -1)
				continue;

			if (file.length() < 10)
				continue;

			this.files.add(file);
		}
	}

	public String createFormattedQuestion() {
		StringBuilder strBuilder = new StringBuilder();
		outputHeader(strBuilder);
		outputFileNames(strBuilder);
		outputDependencies(strBuilder);
		outputFileContents(strBuilder);
		outputFooter(strBuilder);
		strBuilder.append("Question Length: ");
		strBuilder.append(strBuilder.length());
		strBuilder.append("\n");
		return strBuilder.toString();
	}

	private void outputFooter(StringBuilder strBuilder) {
		strBuilder.append("#Usage / Test\n");
		strBuilder.append("\n");
		strBuilder.append("\n");
		strBuilder.append("#Questions\n");
		strBuilder.append("\n");
		strBuilder.append("\n");
		strBuilder.append("\n");
	}

	private void outputDependencies(StringBuilder strBuilder) {
		Set<String> dependencies = new TreeSet<>();
		List<String> todos = new ArrayList<>();
		List<String> readErrors = new ArrayList<>();

		for (File file : files) {
			try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
				String line;
				while ((line = in.readLine()) != null) {
					String todo = todoCheck(line);
					if (todo != null)
						todos.add(todo);

					if (!line.startsWith("import ")) continue;
					if (line.startsWith("import java.")) continue;
					if (line.startsWith("import javax.")) continue;
					String importStatement = line.substring("import ".length());
					importStatement = importStatement.substring(0, importStatement.length() - 1); // cut the semicolon
					dependencies.add(importStatement);
				}
			}
			catch (IOException e) {
				readErrors.add("Could not read " + file.getAbsolutePath() + "\n");
				// more detailed handling of this exception will be handled by another function
			}
		}

		outputItems(strBuilder, todos, "#TODO items");
		outputItems(strBuilder, dependencies, "#Dependencies");
		outputItems(strBuilder, readErrors, "!!!!!! #Read Errors !!!!!!");

	}

	private void outputItems(StringBuilder strBuilder, Collection<String> lines, String header) {
		if (!lines.isEmpty()) {
			strBuilder.append(header).append("\n\n");
			for (String str : lines) {
				strBuilder.append("- ").append(str).append(": \n");
            }
			strBuilder.append("\n");
		}
	}

	private String todoCheck(String line) {
		int index = line.indexOf("TODO");
		if (index == -1)
			return null;
		return line.substring(index);

	}

	private int countLines(File file) throws IOException {
		return Files.readAllLines(file.toPath(), StandardCharsets.UTF_8).size();
	}

	private void outputFileContents(StringBuilder strBuilder) {
		strBuilder.append("#Code\n");
		strBuilder.append("\n");
		strBuilder.append("This code can also be downloaded from [somewhere](http://github.com repository perhaps?)\n");
		strBuilder.append("\n");
		strBuilder.append("\n");
		for (File file : files) {
			try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
				int lines = -1;
				try {
					lines = countLines(file);
				}
				catch (IOException e) {
				}
				strBuilder.append(String.format("**%s:** (%d lines, %d bytes)", file.getName(), lines, file.length()));

				strBuilder.append("\n");
				strBuilder.append("\n");
				String line;
				int importStatementsFinished = 0;
				while ((line = in.readLine()) != null) {
					// skip package and import declarations
					if (line.startsWith("package "))
						continue;
					if (line.startsWith("import ")) {
						importStatementsFinished = 1;
						continue;
					}
					if (importStatementsFinished >= 0) importStatementsFinished = -1;
					if (importStatementsFinished == -1 && line.trim().isEmpty()) // skip empty lines directly after import statements
						continue;
					importStatementsFinished = -2;
					strBuilder.append("    "); // format as code for StackExchange, this needs to be four spaces.
					strBuilder.append(line).append("\n");
				}
			}
			catch (IOException e) {
				strBuilder.append("> Unable to read ").append(file).append(": "); // use a block-quote for exceptions
				strBuilder.append(e);
				strBuilder.append("\n");
			}
			strBuilder.append("\n");
		}
	}

	private void outputFileNames(StringBuilder strBuilder) {
		int totalLength = 0;
		int totalLines = 0;
		for (File file : files) {
			totalLength += file.length();
			try {
				totalLines += countLines(file);
			}
			catch (IOException e) {
				strBuilder.append("Unable to determine line count for ").append(file.getAbsolutePath()).append("\n");
			}
		}
		strBuilder.append(String.format("###Class Summary (%d lines in %d files, making a total of %d bytes)", totalLines, files.size(), totalLength));
		strBuilder.append("\n");
		strBuilder.append("\n");
		for (File file : files) {
			strBuilder.append("- ").append(file.getName()).append(": \n");
		}
		strBuilder.append("\n");
	}

	private void outputHeader(StringBuilder strBuilder) {
		strBuilder.append("#Description\n");
		strBuilder.append("\n");
		strBuilder.append("- Add some [description for what the code does](http://meta.codereview.stackexchange.com/questions/1226/code-should-include-a-description-of-what-the-code-does)\n");
		strBuilder.append("- Is this a follow-up question? Answer [What has changed, Which question was the previous one, and why you are looking for another review](http://meta.codereview.stackexchange.com/questions/1065/how-to-post-a-follow-up-question)\n");
		strBuilder.append("- Which Java compiler version is being used?\n");
		strBuilder.append("- Is GWT compatibility needed? (i.e. can String.format and some other stuff be used?)\n");
		strBuilder.append("- Are there any other requirements causing some possible rewrites of the code to be invalid?\n");
		strBuilder.append("- Does the code contain any // TODO comments that should be fixed before putting it up for review?\n");
		strBuilder.append("\n");
	}

	public static boolean isAsciiFile(File file) {
		try {
			return detectAsciiness(file) >= 0.99;
		}
		catch (IOException e) {
			return true; // if an error occoured, we want it to be added to a list and the error shown in the output
		}
	}

	public static List<File> fileList(String pattern) {
		List<File> files = new ArrayList<>();

		File file = new File(pattern);
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files2 = file.listFiles();
				if (files2 != null) {
					for (File f : files2) {
						if (!f.isDirectory() && isAsciiFile(f)) {
							files.add(f);
						}
					}
				}
			}
			else files.add(file);
		}
		else {
			// extract path
			int lastSeparator = pattern.lastIndexOf('\\');
			lastSeparator = Math.max(lastSeparator, pattern.lastIndexOf('/'));
			String path = lastSeparator < 0 ? "." : pattern.substring(0, lastSeparator);
			file = new File(path);

			// path has been extracted, check if path exists
			if (file.exists()) {
				// create a regex for searching for files, such as *.java, Test*.java
				String regex = lastSeparator < 0 ? pattern : pattern.substring(lastSeparator + 1);
				regex = regex.replaceAll("\\.", "\\.").replaceAll("\\?", ".?").replaceAll("\\*", ".*");
				File[] files2 = file.listFiles();
				if (files2 != null) {
					for (File f : files2) {
						// loop through directory, skip directories and filenames that don't match the pattern
						if (!f.isDirectory() && f.getName().matches(regex) && isAsciiFile(f)) {
							files.add(f);
						}
					}
				}
			}
			else System.out.println("Unable to find path " + file);
		}
		return files;
	}

	public static void start(String[] args) {
		List<File> files = new ArrayList<>();
		if (args.length == 0)
			files.addAll(fileList("."));
		for (String arg : args) {
			files.addAll(fileList(arg));
		}
		System.out.println(new ReviewPreparer(files).createFormattedQuestion());
	}
}
