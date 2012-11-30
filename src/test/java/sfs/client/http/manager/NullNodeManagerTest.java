package sfs.client.http.manager;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sfs.entry.StatusEntryable;
import sfs.structure.Node;
import sfs.structure.StructureNode;
import sfs.structure.tree.MultiNode;
import sfs.util.ipaddress.LocalIPAddress;
import sfs.util.json.JSONUtil;

public class NullNodeManagerTest {

	private NullNodeManager<StructureNode> nodeManager;
	private MultiNode node1;
	private MultiNode node2;

	@Before
	public void setUp() throws Exception {

		String str1 = "{\"timestamp\":\"Wed, Oct 24, 2012 09:58:05 PM GMT\",\"port\":60625,\"cpuInfo\":[{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"0\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"1\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"2\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"3\",\"address sizes\":\"40 bits physical, 48 bits virtual\"}],\"origin\":\""
				+ LocalIPAddress.getLocalIPAddress().get( "v4" ) + "\",\"internal\":60626}";
		node1 = new MultiNode( JSONUtil.get( Node.class, str1 ) );

		String str2 = "{\"timestamp\":\"Wed, Oct 24, 2011 09:58:05 PM GMT\",\"port\":60626,\"cpuInfo\":[{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"0\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"1\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"2\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"3\",\"address sizes\":\"40 bits physical, 48 bits virtual\"}],\"origin\":\"192.168.40.128\",\"internal\":60626}";
		node2 = new MultiNode( JSONUtil.get( Node.class, str2 ) );

		nodeManager = new NullNodeManager<StructureNode>();
	}

	@Test
	public void testAdd() {
		checkNull( nodeManager.add( node1 ) );
		checkNull( nodeManager.add( node2 ) );
	}

	@Test
	public void testDelete() {
		checkNull( nodeManager.delete( node1 ) );
		checkNull( nodeManager.delete( node2 ) );
	}

	@Test
	public void testRotate() {
		checkNull( nodeManager.add( node1 ) );
		checkNull( nodeManager.add( node2 ) );
	}

	private void checkNull(StatusEntryable statusEntry) {
		assertTrue(
				"expecting statusEntry.getHostEntries().length==1 but found " + statusEntry.getHostEntries().length,
				statusEntry.getHostEntries().length == 1 );
		assertTrue( "expecting statusEntry.getStatus().length==1 but found " + statusEntry.getStatus().length,
				statusEntry.getHostEntries().length == 1 );
		assertNull( "expecting statusEntry.getHostEntries()[0]== null but found " + statusEntry.getHostEntries()[0],
				statusEntry.getHostEntries()[0] );
		assertNull( "expecting statusEntry.getStatus()[0]== null but found " + statusEntry.getStatus()[0],
				statusEntry.getStatus()[0] );
	}
}
