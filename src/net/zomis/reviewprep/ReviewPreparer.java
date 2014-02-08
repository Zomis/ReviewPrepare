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
import java.util.List;

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
				// more detailed handling of this exception will be handled by another function
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
		ps.println("#Code");
		ps.println();
		ps.println("This code can also be downloaded from [somewhere](http://github.com repository perhaps?)");
		ps.println();
		for (File file : files) {
			try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
				int lines = -1;
				try {
					lines = countLines(file);
				}
				catch (IOException e) { 
				}
				ps.printf("**%s:** (%d lines, %d bytes)", file.getName(), lines, file.length());
				
				ps.println();
				ps.println();
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
					ps.print("    "); // format as code for StackExchange, this needs to be four spaces.
					ps.println(line);
				}
			}
			catch (IOException e) {
				ps.print("> Unable to read " + file + ": "); // use a block-quote for exceptions
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
		ps.printf("###Class Summary (%d lines in %d files, making a total of %d bytes)", totalLines, files.size(), totalLength);
		ps.println();
		ps.println();
		for (File file : files) {
			ps.println("- " + file.getName() + ": ");
		}
		ps.println();
	}
	
	private void outputHeader(PrintStream ps) {
		ps.println("#Description");
		ps.println();
		ps.println("- Add some [description for what the code does](http://meta.codereview.stackexchange.com/questions/1226/code-should-include-a-description-of-what-the-code-does)");
		ps.println("- Is this a follow-up question? Answer [What has changed, Which question was the previous one, and why you are looking for another review](http://meta.codereview.stackexchange.com/questions/1065/how-to-post-a-follow-up-question)");
		ps.println();
	}
	
	public static boolean isAsciiFile(File file) {
		try {
			return detectAsciiness(file) >= 0.99;
		}
		catch (IOException e) {
			return true; // if an error occoured, we want it to be added to a list and the error shown in the output
		}
	}
	
	public static void main(String[] args) {
		List<File> files = new ArrayList<>();
		if (args.length == 0)
			files.addAll(fileList("."));
		for (String arg : args) {
			files.addAll(fileList(arg));
		}
		new ReviewPreparer(files).createFormattedQuestion(System.out);
	}
	
	public static List<File> fileList(String pattern) {
		List<File> files = new ArrayList<>();
		
		File file = new File(pattern);
		if (file.exists()) {
			if (file.isDirectory()) {
				for (File f : file.listFiles())
					if (!f.isDirectory() && isAsciiFile(f))
						files.add(f);
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
				for (File f : file.listFiles()) {
					// loop through directory, skip directories and filenames that don't match the pattern
					if (!f.isDirectory() && f.getName().matches(regex) && isAsciiFile(f)) {
						files.add(f);
					}
				}
			}
			else System.out.println("Unable to find path " + file);
		}
		return files;
	}
}
