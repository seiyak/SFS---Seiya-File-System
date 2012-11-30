package sfs.client.http.manager;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sfs.client.http.manager.periodic.DummyHTTPClient;
import sfs.entry.Entry;
import sfs.entry.HostEntry;
import sfs.entry.StatusEntry;
import sfs.entry.StatusEntryable;
import sfs.structure.Node;
import sfs.structure.StructureNode;
import sfs.structure.tree.MultiNode;
import sfs.structure.tree.Star;
import sfs.util.ipaddress.LocalIPAddress;
import sfs.util.json.JSONUtil;

public class NodeManagerTest {

	private NodeManager<StructureNode> nodeManager;
	private MultiNode node1;
	private MultiNode node2;
	private MultiNode node3;
	private static Logger log = Logger.getLogger( NodeManagerTest.class );

	@Before
	public void setUp() throws Exception {

		String str1 = "{\"timestamp\":\"Wed, Oct 24, 2012 09:58:05 PM GMT\",\"port\":60625,\"cpuInfo\":[{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"0\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"1\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"2\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"3\",\"address sizes\":\"40 bits physical, 48 bits virtual\"}],\"origin\":\"" + LocalIPAddress
						.getLocalIPAddress().get( "v4" ) + "\",\"internal\":60626}";
		node1 = new MultiNode( JSONUtil.get( Node.class, str1 ) );
		String str2 = "{\"timestamp\":\"Wed, Oct 24, 2011 09:58:05 PM GMT\",\"port\":60626,\"cpuInfo\":[{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"0\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"1\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"2\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"3\",\"address sizes\":\"40 bits physical, 48 bits virtual\"}],\"origin\":\"192.168.40.128\",\"internal\":60626}";
		node2 = new MultiNode( JSONUtil.get( Node.class, str2 ) );
		String str3 = "{\"timestamp\":\"Wed, Oct 24, 2013 09:58:05 PM GMT\",\"port\":60625,\"cpuInfo\":[{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"0\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"1\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"2\",\"address sizes\":\"40 bits physical, 48 bits virtual\"},{\"power management\":\"\",\"model\":\"42\",\"flags\":\"fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts nopl xtopology tsc_reliable nonstop_tsc aperfmperf pni pclmulqdq ssse3 cx16 sse4_1 sse4_2 popcnt xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dtherm\",\"bogomips\":\"6784.71\",\"stepping\":\"7\",\"fpu\":\"yes\",\"wp\":\"yes\",\"cpu family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"microcode\":\"0x14\",\"cpu MHz\":\"3392.359\",\"fpu_exception\":\"yes\",\"cache size\":\"8192 KB\",\"cpuid level\":\"13\",\"cache_alignment\":\"64\",\"clflush size\":\"64\",\"model name\":\"Intel(R) Core(TM) i7-2600 CPU @ 3.40GHz\",\"processor\":\"3\",\"address sizes\":\"40 bits physical, 48 bits virtual\"}],\"origin\":\"192.168.40.127\",\"internal\":60626}";
		node3 = new MultiNode( JSONUtil.get( Node.class, str3 ) );
	}

	@Test
	public void testAdd() {
		
		new Thread( new Runnable() {

			DummyHTTPClient asServer;

			public void run() {
				asServer = new DummyHTTPClient( new HostEntry( LocalIPAddress.getLocalIPAddress().get( "v4" ), 60625 ) );
				try {
					asServer.initiate();
					Thread.sleep( 10000 );
				}
				catch ( IOException e ) {
					log.error( e );
					try {
						asServer.close();
					}
					catch ( IOException e1 ) {
						log.error( e1 );
					}
				}
				catch ( InterruptedException e ) {
					log.info( "about to finish the test" );
					try {
						asServer.close();
					}
					catch ( IOException e1 ) {
						log.error( e1 );
					}
				}
			}

		} ).start();

		nodeManager = new NodeManager<StructureNode>( new Star( new MultiNode( node1.getNode() ) ) );

		try {
			Thread.sleep( 5000 );
		}
		catch ( InterruptedException e ) {
			log.info( "about to assert other aspects of nodeManager" );
		}

		StatusEntryable entry = nodeManager.add( node2 );
		assertNotNull( "expecting entry != null but found null", entry );
		assertTrue( "expecting entry.getStatus().length == 2 but found " + entry.getStatus().length,
				entry.getStatus().length == 2 );
		assertTrue( "expecting entry.getKey() == 'status' but found " + entry.getStatus()[0].getKey(),
				"status".equals( entry.getStatus()[0].getKey() ) );
		assertTrue( "expecting entries[0].getValue() == 'OK' but found " + entry.getStatus()[0].getValue(),
				"OK".equals( entry.getStatus()[0].getValue() ) );

		entry = nodeManager.add( node3 );
		assertTrue( "expecting size == 2 but found " + nodeManager.size(), nodeManager.size() == 2 );

		entry = nodeManager.delete( node2 );
		assertTrue( "expecting entry.getKey() == 'status' but found " + entry.getStatus()[0].getKey(),
				"status".equals( entry.getStatus()[0].getKey() ) );
		assertTrue( "expecting entry.getValue() == 'OK' but found " + entry.getStatus()[0].getValue(),
				"OK".equals( entry.getStatus()[0].getValue() ) );

		entry = nodeManager.delete( node2 );
		assertTrue( "expecting entry.getKey() == 'status' but found " + entry.getStatus()[0].getKey(),
				"status".equals( entry.getStatus()[0].getKey() ) );
		assertTrue( "expecting entry.getValue() == 'FALIED' but found " + entry.getStatus()[0].getValue(),
				"FAILED".equals( entry.getStatus()[0].getValue() ) );
	}

	@After
	public void tearDown() {
		nodeManager.clearPeriodicTasks();
	}
}
