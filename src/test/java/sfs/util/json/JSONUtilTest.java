package sfs.util.json;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sfs.structure.Node;

public class JSONUtilTest {

	@Test
	public void testGet() {

		String str1 = "{\"timestamp\":\"Wed, Oct 24, 2012 09:58:05 PM GMT\",\"port\":60625,\"cpuInfo\":[{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"0\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"1\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"2\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"3\",\"address sizes\":\"40 bits physical, 48 bits virtual\"}],\"origin\":\"192.168.40.129\",\"internal\":60626}";
		Node node1 = JSONUtil.get( Node.class, str1 );
		assertTrue( "expecting node1.getTimestamp().equals('Wed, Oct 24, 2012 09:58:05 PM GMT')", node1.getTimestamp()
				.equals( "Wed, Oct 24, 2012 09:58:05 PM GMT" ) );
		assertTrue( "expecting node1.getPort()==60625", node1.getPort() == 60625 );
		assertTrue( "expecting node1.getOrigin().equals('192.168.40.129')", node1.getOrigin().equals( "192.168.40.129" ) );
		assertTrue( "expecting node1.getInternal()==60626", node1.getInternal() == 60626 );
	}
}
