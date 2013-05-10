package org.eclipse.recommenders.test.args.entities;

import static org.junit.Assert.assertTrue;

import org.eclipse.recommenders.args.completion.rcp.entities.BaseVariable;
import org.junit.Test;

public class BaseVariableTest {
	
	@Test
	public void testBaseVariableHashcode(){
		String bName = "b0";
		
		BaseVariable b0 = new BaseVariable();
		b0.setName(bName);
		b0.setType(null);
		
		BaseVariable b1 = new BaseVariable();
		b1.setName(null);
		b1.setType(null);
		
		assertTrue(b0.hashCode() == 37 *  b0.getName().hashCode());
		assertTrue(b1.hashCode() == 0);
	}
	
	@Test
	public void testBaseVariableEquals(){
		BaseVariable b0 = new BaseVariable();
		b0.setName("b1");
		b0.setType(null);
		
		BaseVariable b1 = new BaseVariable();
		b1.setName("b1");
		b1.setType(null);
		
		BaseVariable b2 = new BaseVariable();
		b2.setName("b2");
		b2.setType(null);
		
		BaseVariable b3 = new BaseVariable();
		b3.setName(null);
		b3.setType(null);
		
		BaseVariable b4 = new BaseVariable();
		b4.setName(null);
		b4.setType(null);
		
		assertTrue(b0.equals(b1));
		assertTrue(!b1.equals(b2));
		assertTrue(b3.equals(b4));
	}
}
