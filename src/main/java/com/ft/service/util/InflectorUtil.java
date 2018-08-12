package com.ft.service.util;

import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

import com.ibm.icu.text.Normalizer2;
import com.ibm.icu.text.Transliterator;

public class InflectorUtil {

	public static String transliterate(String input) {
		return Transliterator.getInstance("Any-Latin; Latin-ASCII").transliterate(input);
	}

	private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
	private static final Pattern EDGESDHASHES = Pattern.compile("(^-|-$)");

	public static String toSlug(String filePath) {
		String path = FilenameUtils.getPath(filePath);
		String fileName = FilenameUtils.getBaseName(filePath);
		String fileExt = FilenameUtils.getExtension(filePath);
		String nowhitespace = WHITESPACE.matcher(fileName).replaceAll("-");
	    String normalized = Normalizer2.getNFDInstance().normalize(nowhitespace);
	    String slug = transliterate(normalized);
	    slug = EDGESDHASHES.matcher(slug).replaceAll("");
	    return path + slug + FilenameUtils.EXTENSION_SEPARATOR + fileExt;
	}

}
