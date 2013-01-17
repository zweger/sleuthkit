/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sleuthkit.datamodel;

import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author smoss
 */
@RunWith(Parameterized.class)
public class TopDownTraversal {
		

	private List<String> imagePaths;

	
	public TopDownTraversal(List<String> imagePaths) {
		this.imagePaths = imagePaths;
	}
	/**
	 * Get the sets of filenames for each test image, they should be located in 
	 * a folder called "testimages" in either the TSK parent directory or the 
	 * top level TSK directory
	 * @return A Collection of one-element Object arrays, where that one element
	 * is a List<String> containing the image file paths (the type is weird
	 * because that's what JUnit wants for parameters).
	 */
	@Parameters
	public static Collection<Object[]> testImageData() {
		Collection<Object[]> data = new ArrayList<Object[]>();
		
		for (Object imagePaths : DiffUtil.getImagePaths()) {
			data.add(new Object[]{imagePaths});
		}
		return data;
	}

	
	@Test
	public void testDataModelDiff() {
		try {
			String title = (new java.io.File(imagePaths.get(0))).getName();
			java.io.File testFolder=new java.io.File("test"+java.io.File.separator+"Output"+java.io.File.separator+"Results");
			String out = title.replace(".001", "").replace(".img","").replace(".dd", "").replace(".E01", "");
			java.io.File testStandard = new java.io.File(testFolder.getAbsolutePath()+java.io.File.separator+out+"_Results_TD.txt");
			String testStandardPath = testStandard.getPath();
			String oldStandardPath = DiffUtil.standardPath(imagePaths,"_TD_sorted");
			DiffUtil.createStandardTopDown(testStandardPath, testFolder.getAbsolutePath(), imagePaths);
			String sortedname = testStandard.getName().replace(".txt", "_sorted.txt");
			String sortedloc = testStandardPath.replace(".txt", "_sorted.txt");;
			DiffUtil.addTempStore(sortedname);
			long wait=System.currentTimeMillis();
			while((System.currentTimeMillis()-wait)<3000){}
			assertEquals("Generated results ("+testStandardPath+") differ with gold standard ("+oldStandardPath+") .", DiffUtil.comparecontent(oldStandardPath, sortedloc),true);
		} catch (Exception ex) {
			fail("Couldn't open gold standard file.");
		}
	}
}