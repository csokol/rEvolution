package br.com.caelum.revolution.scm.svn;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SVNDiffParserTests {

	@Test
	public void shouldRemoveHeaderFromDiff() {
		String diff = "Index: ustrings.inc\n"
				+ "===================================================================\n"
				+ "--- ustrings.inc	(revision 19197)\n"
				+ "+++ ustrings.inc	(revision 19198)\n"
				+ "@@ -1356,14 +1356,14 @@\n"
				+ "\n"
				+ " Procedure fpc_UnicodeStr_CheckRange(len,index : SizeInt);[Public,Alias : 'FPC_UNICODESTR_RANGECHECK']; compilerproc;\n"
				+ " begin\n"
				+ "-  if (index>len div 2) or (Index<1) then\n"
				+ "+  if (index>len) or (Index<1) then\n"
				+ "     HandleErrorFrame(201,get_frame);\n"
				+ " end;\n"
				+ " \n"
				+ " {$else VER2_4}\n"
				+ " Procedure fpc_UnicodeStr_CheckRange(p: Pointer; index: SizeInt);[Public,Alias : 'FPC_UNICODESTR_RANGECHECK']; compilerproc;\n"
				+ " begin\n"
				+ "-  if (p=nil) or (index>PUnicodeRec(p-UnicodeFirstOff)^.len div 2) or (Index<1) then\n"
				+ "+  if (p=nil) or (index>PUnicodeRec(p-UnicodeFirstOff)^.len) or (Index<1) then\n"
				+ "     HandleErrorFrame(201,get_frame);\n" + " end;\n"
				+ " {$endif VER2_4}\n" + " \n";
		
		assertEquals(
				"@@ -1356,14 +1356,14 @@\n"
						+ "\n"
						+ " Procedure fpc_UnicodeStr_CheckRange(len,index : SizeInt);[Public,Alias : 'FPC_UNICODESTR_RANGECHECK']; compilerproc;\n"
						+ " begin\n"
						+ "-  if (index>len div 2) or (Index<1) then\n"
						+ "+  if (index>len) or (Index<1) then\n"
						+ "     HandleErrorFrame(201,get_frame);\n"
						+ " end;\n"
						+ " \n"
						+ " {$else VER2_4}\n"
						+ " Procedure fpc_UnicodeStr_CheckRange(p: Pointer; index: SizeInt);[Public,Alias : 'FPC_UNICODESTR_RANGECHECK']; compilerproc;\n"
						+ " begin\n"
						+ "-  if (p=nil) or (index>PUnicodeRec(p-UnicodeFirstOff)^.len div 2) or (Index<1) then\n"
						+ "+  if (p=nil) or (index>PUnicodeRec(p-UnicodeFirstOff)^.len) or (Index<1) then\n"
						+ "     HandleErrorFrame(201,get_frame);\n" + " end;\n"
						+ " {$endif VER2_4}\n" + " \n"
				, new SVNDiffParser().parse(diff));

	}
}
