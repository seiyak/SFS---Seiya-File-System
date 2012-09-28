package sfs.cpuinfo;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import sfs.cpuinfo.CPUInfo;

public class CPUInfoTest {

	private CPUInfo info;

	@Before
	public void setUp() throws Exception {

		info = new CPUInfo();
	}

	@Test
	public void testGetCPUInfo() throws IOException {
		String res = info.getCPUInfo();
		assertFalse( "expecting res.isEmpty() false but found " + res, res.isEmpty() );
	}

}
