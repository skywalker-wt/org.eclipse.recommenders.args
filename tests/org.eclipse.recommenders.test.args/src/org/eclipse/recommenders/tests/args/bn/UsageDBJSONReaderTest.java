package org.eclipse.recommenders.tests.args.bn;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.eclipse.recommenders.args.completion.rcp.usagedb.ParamInfo;
import org.eclipse.recommenders.args.completion.rcp.usagedb.UsageDBJSONReader;
import org.eclipse.recommenders.tests.args.util.TestResource;
import org.junit.Test;

public class UsageDBJSONReaderTest {
	
	/**
	 * Test Case for getting information from JSON File.
	 * @throws IOException
	 */
	@Test
	public final void testReadJson() throws IOException {
		String[] jsonFiles = TestResource.getJSONFiles();
		for (String jsonFile : jsonFiles) {
			assertJsonReader(jsonFile);
		}
	}
	
	/**
	 * Check the information generated from file is correct.
	 * @param jsonFile - the URL for json file in eclipse platform
	 * @throws IOException
	 */
	private void assertJsonReader(final String jsonFile) throws IOException {
		URL jsonFileURL = new URL(jsonFile);
		InputStream jsonInputStream = jsonFileURL.openConnection().getInputStream();
	
		UsageDBJSONReader jsonReader = new UsageDBJSONReader(jsonInputStream);	
		jsonReader.readMapFromJSON();
		Map<Integer, ParamInfo> index2paramInfo = jsonReader.getIndex2param();
		
		assertTrue(index2paramInfo.size() > 0);		
		for (ParamInfo p : index2paramInfo.values()) {
			if (p.getStructureType().equals("MethodInvocation")) {
				assertNotNull(p.getMethodInvType());
			} else if (p.getStructureType().equals("QualifiedName")) {
				assertNotNull(p.getQualifiedNaType());
			}
		}
	}
}
