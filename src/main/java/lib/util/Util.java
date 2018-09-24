package lib.util;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public final class Util {

	public static <T1, T2> Stream<Pair<T1, T2>> zip(List<T1> a, List<T2> b) {
		return IntStream
				.range(0, Math.min(a.size(), b.size()))
				.mapToObj(i -> new Pair<>(a.get(i), b.get(i)));
	}

	private Util() {
	}

}
