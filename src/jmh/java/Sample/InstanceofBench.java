package Sample;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

/**
 * 실험 목적:
 * - 인터페이스 instanceof 대상 타입을 번갈아 검사할 때(교차 체크) 비용/스케일링 변화 관찰
 * - 클래스 상속 instanceof(비교군)과도 비교
 *
 * 벤치마크 결과:
 Benchmark                                                                         Mode      Cnt       Score   Error  Units
 Sample.InstanceofBench.classAlternatingTargets                                  sample  2464393      31.965 ± 0.306  ns/op
 Sample.InstanceofBench.classAlternatingTargets:classAlternatingTargets·p0.00    sample                  ? 0          ns/op
 Sample.InstanceofBench.classAlternatingTargets:classAlternatingTargets·p0.50    sample                  ? 0          ns/op
 Sample.InstanceofBench.classAlternatingTargets:classAlternatingTargets·p0.90    sample              100.000          ns/op
 Sample.InstanceofBench.classAlternatingTargets:classAlternatingTargets·p0.95    sample              100.000          ns/op
 Sample.InstanceofBench.classAlternatingTargets:classAlternatingTargets·p0.99    sample              100.000          ns/op
 Sample.InstanceofBench.classAlternatingTargets:classAlternatingTargets·p0.999   sample              100.000          ns/op
 Sample.InstanceofBench.classAlternatingTargets:classAlternatingTargets·p0.9999  sample              300.000          ns/op
 Sample.InstanceofBench.classAlternatingTargets:classAlternatingTargets·p1.00    sample            75136.000          ns/op
 Sample.InstanceofBench.ifaceAlternatingTargets                                  sample  1779991     214.187 ± 0.684  ns/op
 Sample.InstanceofBench.ifaceAlternatingTargets:ifaceAlternatingTargets·p0.00    sample                  ? 0          ns/op
 Sample.InstanceofBench.ifaceAlternatingTargets:ifaceAlternatingTargets·p0.50    sample              200.000          ns/op
 Sample.InstanceofBench.ifaceAlternatingTargets:ifaceAlternatingTargets·p0.90    sample              400.000          ns/op
 Sample.InstanceofBench.ifaceAlternatingTargets:ifaceAlternatingTargets·p0.95    sample              400.000          ns/op
 Sample.InstanceofBench.ifaceAlternatingTargets:ifaceAlternatingTargets·p0.99    sample              700.000          ns/op
 Sample.InstanceofBench.ifaceAlternatingTargets:ifaceAlternatingTargets·p0.999   sample             1000.000          ns/op
 Sample.InstanceofBench.ifaceAlternatingTargets:ifaceAlternatingTargets·p0.9999  sample             6696.083          ns/op
 Sample.InstanceofBench.ifaceAlternatingTargets:ifaceAlternatingTargets·p1.00    sample           135168.000          ns/op
 Sample.InstanceofBench.ifaceSingleTarget                                        sample  2131303      32.588 ± 0.405  ns/op
 Sample.InstanceofBench.ifaceSingleTarget:ifaceSingleTarget·p0.00                sample                  ? 0          ns/op
 Sample.InstanceofBench.ifaceSingleTarget:ifaceSingleTarget·p0.50                sample                  ? 0          ns/op
 Sample.InstanceofBench.ifaceSingleTarget:ifaceSingleTarget·p0.90                sample              100.000          ns/op
 Sample.InstanceofBench.ifaceSingleTarget:ifaceSingleTarget·p0.95                sample              100.000          ns/op
 Sample.InstanceofBench.ifaceSingleTarget:ifaceSingleTarget·p0.99                sample              100.000          ns/op
 Sample.InstanceofBench.ifaceSingleTarget:ifaceSingleTarget·p0.999               sample              100.000          ns/op
 Sample.InstanceofBench.ifaceSingleTarget:ifaceSingleTarget·p0.9999              sample              300.000          ns/op
 Sample.InstanceofBench.ifaceSingleTarget:ifaceSingleTarget·p1.00                sample           153088.000          ns/op
 */
@BenchmarkMode(Mode.SampleTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(2)
@State(Scope.Benchmark)
@Threads(4)
public class InstanceofBench {

	// Interface test types
	public interface IfaceA {}
	public interface IfaceB {}
	public interface DualIface extends IfaceA, IfaceB {}

	public static class DualImpl1 implements DualIface {}
	public static class DualImpl2 implements DualIface {}

	// Class inheritance test types
	public static class Base {}
	public static class Sub1 extends Base {}
	public static class Sub2 extends Sub1 {}

	private final DualIface dual1 = new DualImpl1();
	private final DualIface dual2 = new DualImpl2();

	private final Base baseAsSub1 = new Sub1();
	private final Base baseAsSub2 = new Sub2();

	public boolean isIfaceA(DualIface value) {
		return value instanceof IfaceA;
	}

	public boolean isIfaceB(DualIface value) {
		return value instanceof IfaceB;
	}

	public boolean isBase(Object value) {
		return value instanceof Base;
	}

	public boolean isSub1(Object value) {
		return value instanceof Sub1;
	}

	// 같은 인터페이스 타입(IfaceA)만 반복 체크
	@Benchmark
	public void ifaceSingleTarget(Blackhole bh) {
		bh.consume(isIfaceA(dual1));
		bh.consume(isIfaceA(dual2));
		bh.consume(isIfaceA(dual1));
		bh.consume(isIfaceA(dual2));
	}

	// 인터페이스 타입을 번갈아가며 체크(IfaceA <-> IfaceB)
	@Benchmark
	public void ifaceAlternatingTargets(Blackhole bh) {
		bh.consume(isIfaceA(dual1));
		bh.consume(isIfaceA(dual2));
		bh.consume(isIfaceB(dual1));
		bh.consume(isIfaceB(dual2));
	}

	// 상속받은 상위클래스를 번갈아가며 체크
	@Benchmark
	public void classAlternatingTargets(Blackhole bh) {
		bh.consume(isBase(baseAsSub1));
		bh.consume(isBase(baseAsSub2));
		bh.consume(isSub1(baseAsSub1));
		bh.consume(isSub1(baseAsSub2));
	}
}
