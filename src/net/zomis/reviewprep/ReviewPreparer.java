package net.zomis.reviewprep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReviewPreparer {
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

	public int createFormattedQuestion(OutputStream out) {
		CountingStream counter = new CountingStream(out);
		PrintStream ps = new PrintStream(counter);
		outputHeader(ps);
		outputFileNames(ps);
		outputFileContents(ps);
		outputDependencies(ps);
		outputFooter(ps);
		ps.print("Question Length: ");
		ps.println(counter.getBytesWritten());
		return counter.getBytesWritten();
	}

	private void outputFooter(PrintStream ps) {
		ps.println("#Usage / Test");
		ps.println();
		ps.println();
		ps.println("#Questions");
		ps.println();
		ps.println();
		ps.println();
	}

	private void outputDependencies(PrintStream ps) {
		List<String> dependencies = new ArrayList<>();
		for (File file : files) {
			try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
				String line;
				while ((line = in.readLine()) != null) {
					if (!line.startsWith("import ")) continue;
					if (line.startsWith("import java.")) continue;
					if (line.startsWith("import javax.")) continue;
					String importStatement = line.substring("import ".length());
					importStatement = importStatement.substring(0, importStatement.length() - 1); // cut the semicolon
					dependencies.add(importStatement);
				}
			}
			catch (IOException e) {
				ps.println("Could not read " + file.getAbsolutePath());
				ps.println();
				// this will be handled by another function
			}
			
		}
		if (!dependencies.isEmpty()) {
			ps.println("#Dependencies");
			ps.println();
			for (String str : dependencies)
				ps.println("- " + str + ": ");
		}
		ps.println();
	}

	private int countLines(File file) throws IOException {
		return Files.readAllLines(file.toPath(), StandardCharsets.UTF_8).size();
	}
	
	private void outputFileContents(PrintStream ps) {
		ps.println();
		for (File file : files) {
			try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
				ps.printf("**%s:** (approximately %d bytes in %d lines)", className(file), file.length(), countLines(file));
				ps.println();
				ps.println();
				String line;
				int importStatementsFinished = 0;
				while ((line = in.readLine()) != null) {
					// skip package and import declarations
					if (line.startsWith("package ")) continue;
					if (line.startsWith("import ")) {
						importStatementsFinished = 1;
						continue;
					}
					if (importStatementsFinished >= 0) importStatementsFinished = -1;
					if (importStatementsFinished == -1 && line.trim().isEmpty()) continue;
					importStatementsFinished = -2;
					line = line.replaceAll("    ", "\t"); // replace four spaces with tabs, since that takes less space
					ps.print("    "); // format as code for StackExchange, this needs to be spaces.
					ps.println(line);
				}
			}
			catch (IOException e) {
				ps.print("> ");
				e.printStackTrace(ps);
			}
			ps.println();
		}
	}

	private void outputFileNames(PrintStream ps) {
		int totalLength = 0;
		int totalLines = 0;
		for (File file : files) {
			totalLength += file.length();
			try {
				totalLines += countLines(file);
			}
			catch (IOException e) {
				ps.println("Unable to determine line count for " + file.getAbsolutePath());
			}
		}
		ps.printf("###Class Summary (%d bytes in %d lines in %d files)", totalLength, totalLines, files.size());
		ps.println();
		ps.println();
		for (File file : files) {
			ps.println("- " + className(file) + ": ");
		}
	}
	
	private String className(File file) {
		String str = file.getName();
		return str.substring(0, str.lastIndexOf('.'));
	}

	private void outputHeader(PrintStream ps) {
		ps.println("#Description");
		ps.println();
		ps.println("- Add some [description for what the code does](http://meta.codereview.stackexchange.com/questions/1226/code-should-include-a-description-of-what-the-code-does)");
		ps.println("- Is this a follow-up question? Answer [What has changed, Which question was the previous one, and why you are looking for another review](http://meta.codereview.stackexchange.com/questions/1065/how-to-post-a-follow-up-question)");
		ps.println();
		ps.println("#Code download");
		ps.println();
		ps.println("For convenience, this code can be downloaded from [somewhere](http://github.com repository perhaps?)");
		ps.println();
	}
	
	public static void main(String[] args) throws IOException {
//		List<File> fileList = Arrays.asList(new File("C:/_zomisnet/_reviewtest").listFiles());
		List<File> fileList = Arrays.asList(new File("./src/net/zomis/reviewprep").listFiles());
		new ReviewPreparer(fileList).createFormattedQuestion(System.out);
	}
	
}
