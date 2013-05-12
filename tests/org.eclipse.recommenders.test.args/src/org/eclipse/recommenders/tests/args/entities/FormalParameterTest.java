package org.eclipse.recommenders.tests.args.entities;

import static org.junit.Assert.assertTrue;

import org.eclipse.recommenders.args.completion.rcp.entities.FormalParameter;
import org.junit.Before;
import org.junit.Test;

public class FormalParameterTest {
	private FormalParameter fp1;
	private FormalParameter fp2;
	private FormalParameter fp3;
	private FormalParameter fp4;
	private FormalParameter fp5;
	
	@Before
	public void before() {
		String className1 = "fp1";
		String className2 = "fp2";
		String methodName1 = "mn1";
		String methodName2 = "mn2";
		String pList1 = "pl1";
		String pList2 = "pl2";
		String pType1 = "pl1";
		String pType2 = "pl2";
		
		fp1 = new FormalParameter();
		fp1.setClassName(className1);
		fp1.setMethodName(methodName1);
		fp1.setParamIndex(1);
		fp1.setParamListStr(pList1);
		fp1.setParamType(pType1);
		
		fp2 = new FormalParameter();
		fp2.setClassName(className2);
		fp2.setMethodName(methodName2);
		fp2.setParamIndex(2);
		fp2.setParamListStr(pList2);
		fp2.setParamType(pType2);
		
		fp3 = new FormalParameter();
		fp3.setClassName(className1);
		fp3.setMethodName(methodName1);
		fp3.setParamIndex(1);
		fp3.setParamListStr(pList1);
		fp3.setParamType(pType1);
		
		fp4 = new FormalParameter();
		fp4.setClassName(null);
		fp4.setMethodName(null);
		fp4.setParamIndex(0);
		fp4.setParamListStr(null);
		fp4.setParamType(null);
		
		fp5 = new FormalParameter();
		fp5.setClassName(null);
		fp5.setMethodName(null);
		fp5.setParamIndex(0);
		fp5.setParamListStr(null);
		fp5.setParamType(null);
	}
	
	@Test
	public void testFormalParameterHashcode(){
		assertTrue(fp1.hashCode() == fp3.hashCode());
		assertTrue(fp4.hashCode() == fp5.hashCode());
		assertTrue(fp4.hashCode() == 0);
		assertTrue(fp1.hashCode() != fp2.hashCode());
		assertTrue(fp1.hashCode() != fp4.hashCode());
	}
	
	@Test
	public void testFormalParameterEquals(){
		assertTrue(fp1.equals(fp3));
		assertTrue(!fp1.equals(fp2));
		assertTrue(!fp1.equals(fp4));
//		assertTrue(fp4.equals(fp5));
	}
}
