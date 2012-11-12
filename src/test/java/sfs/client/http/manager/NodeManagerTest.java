package sfs.client.http.manager;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sfs.entry.Entry;
import sfs.entry.HostEntry;
import sfs.entry.StatusEntry;
import sfs.structure.Node;
import sfs.structure.StructureNode;
import sfs.structure.tree.MultiNode;
import sfs.structure.tree.Star;
import sfs.util.json.JSONUtil;

public class NodeManagerTest {

	private NodeManager<StructureNode> nodeManager;
	private MultiNode node1;
	private MultiNode node2;
	private MultiNode node3;

	@Before
	public void setUp() throws Exception {

		String str1 = "{\"timestamp\":\"Wed, Oct 24, 2012 09:58:05 PM GMT\",\"port\":60625,\"cpuInfo\":[{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"0\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"1\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"2\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"3\",\"address sizes\":\"40 bits physical, 48 bits virtual\"}],\"origin\":\"192.168.40.129\",\"internal\":60626}";
		node1 = new MultiNode( JSONUtil.get( Node.class, str1 ) );
		nodeManager = new NodeManager<StructureNode>( new HostEntry( "127.0.0.1", 2071 ), new Star( new MultiNode(
				node1.getNode() ) ) );

		String str2 = "{\"timestamp\":\"Wed, Oct 24, 2011 09:58:05 PM GMT\",\"port\":60626,\"cpuInfo\":[{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"0\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"1\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"2\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"3\",\"address sizes\":\"40 bits physical, 48 bits virtual\"}],\"origin\":\"192.168.40.128\",\"internal\":60626}";
		node2 = new MultiNode( JSONUtil.get( Node.class, str2 ) );

		String str3 = "{\"timestamp\":\"Wed, Oct 24, 2013 09:58:05 PM GMT\",\"port\":60625,\"cpuInfo\":[{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"0\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"1\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"2\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"3\",\"address sizes\":\"40 bits physical, 48 bits virtual\"}],\"origin\":\"192.168.40.127\",\"internal\":60626}";
		node3 = new MultiNode( JSONUtil.get( Node.class, str3 ) );
	}

	@Test
	public void testAdd() {

		StatusEntry[] entries = nodeManager.add( node2 );
		assertNotNull( "expecting entries != null but found null", entries );
		assertTrue( "expecting entries[0].getStatus().length == 2 but found " + entries[0].getStatus().length, entries[0].getStatus().length == 2 );
		assertTrue( "expecting entries[0].getKey() == 'status' but found " + entries[0].getStatus()[0].getKey(),
				"status".equals( entries[0].getStatus()[0].getKey() ) );
		assertTrue( "expecting entries[0].getValue() == 'OK' but found " + entries[0].getStatus()[0].getValue(),
				"OK".equals( entries[0].getStatus()[0].getValue() ) );

		entries = null;

		entries = nodeManager.add( node3 );
		assertTrue( "expecting size == 2 but found " + nodeManager.size(), nodeManager.size() == 2 );

		entries = null;
		entries = nodeManager.delete( node2 );
		assertTrue( "expecting entries[0].getKey() == 'status' but found " + entries[0].getStatus()[0].getKey(),
				"status".equals( entries[0].getStatus()[0].getKey() ) );
		assertTrue( "expecting entries[0].getValue() == 'OK' but found " + entries[0].getStatus()[0].getValue(),
				"OK".equals( entries[0].getStatus()[0].getValue() ) );
		
		entries = null;
		entries = nodeManager.delete( node2 );
		assertTrue( "expecting entries[0].getKey() == 'status' but found " + entries[0].getStatus()[0].getKey(),
				"status".equals( entries[0].getStatus()[0].getKey() ) );
		assertTrue( "expecting entries[0].getValue() == 'FALIED' but found " + entries[0].getStatus()[0].getValue(),
				"FAILED".equals( entries[0].getStatus()[0].getValue() ) );
	}
}