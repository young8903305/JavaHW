package apjp2016;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.out;

/**
 * Implement all methods given in this class using lambda expressions.
 * 
 * @author chencc
 *
 */
public class HW3 {

	static final String s1 = "The researchers said that women tend to take on a bigger share of responsibilities at home "
			+ "and may face more pressure and stress than men when they work long hours. "
			+ "Work for women is also less satisfying due to the need to balance demands at work "
			+ "and family obligations.";

	final static String[] s2 = s1.split("[ .]+");

	// Do not change words, words1 and words2 etc.!
	public final static List<String> words = Collections.unmodifiableList(Arrays.asList(s2));

	public final static List<String> words1 = words.subList(11, 20);

	public final static List<String> words2 = Collections
			.unmodifiableList(Arrays.asList("100", "300", "20", "40", "77", "88", "1001", "6", "57", "90"));

	/**
	 * Sort words by the length (in increasing order) of each word, and then by
	 * lexicographical order if they are of the same length.
	 * Collections.sort(List<T>, Comparator<? super T>)
	 * 
	 * @return
	 */
	public static List<String> sortWordsByLength(List<String> nWords) {

		// sort non-null nWords in place using Collections.sort
		// Put your code here! ...
		Collections.sort(nWords, String::compareTo); // Collections.sort(nWords, (p1, p2) -> p1.compareTo(p2) );is ok
		Collections.sort(nWords, (p1, p2) -> p1.length() - p2.length() );
		return nWords;

	}

	/**
	 * Sort words by the reverse length(i.e., from the longest to the shortest
	 * length ) of each word.
	 * 
	 * @return
	 */
	public static List<String> sortWordsByReverseLength(List<String> nWords) {

		// sort non-null nWords IN PLACE using Collections.sort
		// Put your code here! ...
		Collections.sort(nWords, (p1, p2) -> p2.length() - p1.length() );
		return nWords;
	}

	/**
	 * words by the number of characters which occurs in the input cs character
	 * array.
	 * 
	 * @return
	 */
	public static List<String> sortWordsByNumberOfChar(List<String> nWords, char... cs) {

		// sort non-null nWords IN PLACE using Collections.sort
		// Put your code here! ...		
		Collections.sort(nWords, new Comparator<String>() {
			
			public int compare(String o1, String o2) {
				int count_1 = 0, count_2 = 0;
				char[] s1 = o1.toCharArray();
				char[] s2 = o2.toCharArray();
				for(char c: cs){
					for(int i = 0; i < s1.length; i++){
						if(s1[i] == c){
							count_1++;
						}
					}
					for(int i = 0; i < s2.length; i++){
						if(s2[i] == c){
							count_2++;
						}
					}
				}
				return count_1 - count_2;
			}
		});
		return nWords;
	}

	/**
	 * Implement the NoneMatch method which will return true if some element of
	 * the input list passes the pred.test method but not all elements pass the
	 * test.
	 * 
	 * @param list
	 *            : list of element to be test
	 * @param pred
	 *            : the predicate
	 * @return true iff some elements of list pass the predicate test but some
	 *         not.
	 */
	public static <T> boolean someButNotAllMatch(List<T> list, Predicate<? super T> pred) {

		// Put your code here! ...
		
/*		if( list.stream().allMatch(pred) == true){
			return false;
		}
		else{
			if(list.stream().anyMatch(pred)){
				return true;
			}
		}
		return false;*/
		
		boolean[] judge = new boolean[list.size()];
		
		for(int i = 0; i < list.size(); i++){
			if( pred.test( list.get(i)) ){
				judge[i] = true;
			}
			else{
				judge[i] = false;
			}
			if(i > 0 && (judge[i] != judge[i-1])){
				return true;
			}
		}
		return false;
	}

	/**
	 * Make a static method called map. It should take a List<T> l and a
	 * Function<? super T, ?extends U> f and return a new List { f(e) | e in l }
	 * of type List<U> that contains the results of applying the Function to
	 * each element of the original List. E.g.: List<String> excitingWords =
	 * map(words, s -> s + "!"); List<String> eyeWords = map(words, s ->
	 * s.replace("i", "eye")); List<String> upperCaseWords = map(words,
	 * String::toUpperCase); The result should be equivalent to the execution of
	 * the code:
	 * 
	 * <pre>
	 * list.stream().map(f).collect(Collectors.toList()) ; BUT YOU CANNOT USE
	 * Stream API in this implementation.
	 */

	public static <T, U> List<U> map(List<T> list, Function<? super T, ? extends U> f) {

		List<U> rlt = new ArrayList<>();

		// Put your code here! ...		
		list.forEach(l -> rlt.add( f.apply(l) ) );
		return rlt;
	}

	/**
	 * Implement the flatMap function on list<T>. The result should be
	 * equivalent to the execution of the code:
	 * 
	 * <pre>
	 * list.stream().flatMap(f).collect(Collectors.toList()) ; BUT YOU CANNOT
	 * USE Stream API in this implementation.
	 * 
	 * @param list
	 * @param f
	 *            a function from T to List<R>
	 * @return a list fo type list<R>
	 */

	public static <T, R> List<R> flatMap(List<T> list, Function<? super T, List<R>> f) {
		// The result should be equivalent to the code:
		// list.stream().flatMap(f) ;
		// but YOU CANNOT USE this stream API in this implementation.
		List<R> rlt = new ArrayList<>();

		// Put your code here! ...
		list.forEach(l -> f.apply(l).forEach(r -> rlt.add(r)) );
		return rlt;
	}

	/**
	 * Partition the input list into two lists, one being the list [ e | p(e) is
	 * true ] and the other the list [ e | p(e) returns false ]. The result are
	 * put in a map (HashMap or TreeMap) under the key Boolean.True and
	 * Boolean.False, respectively. The result should be equivalent to the
	 * execution of the code:
	 * 
	 * <pre>
	 * list.stream().collect(Collectors.partitoningBy(p));
	 * </pre>
	 * 
	 * But YOU CANNOT USE Stream API in this implementation.
	 * 
	 * @param list
	 * @param p
	 * @return
	 */
	public static <T> Map<Boolean, List<T>> partition(List<T> list, Predicate<? super T> p) {
		// The result should be equivalent to the code:
		// list.stream().collect(Collectors.partitoningBy(p)) ;
		// but YOU CANNOT USE this stream API in this implementation.
		Map<Boolean, List<T>> rlt = new HashMap<>();
		List <T> t1 = new ArrayList<>();
		List <T> t2 = new ArrayList<>();
		// Put your code here! ...
		
		for(T t : list){
			if(p.test(t)){
				t1.add(t);
			}
			else{
				t2.add(t);
			}
		}
		rlt.put(true, t1);
		rlt.put(false, t2);
		
		return rlt;
	}

	/**
	 * Implement the collect function on list<T>. The result should be
	 * equivalent to the execution of the code:
	 * 
	 * <pre>
	 * list.stream().collect(col);
	 * </pre>
	 * 
	 * But YOU CANNOT USE stream API in this implementation.
	 * 
	 * @param list
	 * @param col
	 *            a collector of type Collector<T,A,R>
	 * @return a value of type R.
	 */

	public static <T, A, R> R collect(List<T> list, Collector<T, A, R> col) {
		// The result should be equivalent to the code:
		// list.stream().collect(col) ;
		// but YOU CANNOT USE this stream API in this implementation.

		// Put your code here! ...		
		A a = col.supplier().get();
		
		list.forEach( l -> col.accumulator().accept( a, l ));
		
		return col.finisher().apply( a );
		
	}

	public static void selfTest() {

		out.println("\n>>> Sort strings in words by length first and by lexicogrphical order secondly:");
		out.println(">>> words = " + words);
		out.println(" result: " + sortWordsByLength(new ArrayList<>(words)));

		out.println("\n>>> Sort strings in words by length in decreasing order:");
		out.println(">>> words = " + words);
		out.println("result: " + sortWordsByReverseLength(new ArrayList<>(words)));

		out.println("\n>>> Sort strings in words by the number of occurrences of 'm', 'o' and 'e': ");
		out.println(">>> words = " + words);
		out.println("result: " + sortWordsByNumberOfChar(new ArrayList<>(words), 'm', 'o', 'e'));

		out.println("\n>>> There is some string but not all strings in words whose length is in [1,10):");
		out.println(">>> words = " + words);
		out.println(someButNotAllMatch(words, w -> w.length() > 0 && w.length() < 10));

		out.println("\n>>> There is some string but not all strings in words whose length is in [1,20):");
		out.println(">>> words = " + words);
		out.println(someButNotAllMatch(words, w -> w.length() > 0 && w.length() < 20));

		out.println("\n>>> There is some string but not all strings in words2 contains '5' :");
		out.println(">>> words2 = " + words2);
		out.println(someButNotAllMatch(words2, w -> w.contains("5")));

		out.println("\n>>> Map word in words1 to upperCase:");
		out.println(">>> words1 = " + words1);
		out.println(map(words1, String::toUpperCase));

		out.println("\n>>> flatMap word in words2 to character list:");
		out.println(">>> words2 = " + words2);
		Function<String, List<Character>> as = s -> s.chars().mapToObj(c -> new Character((char) c))
				.collect(Collectors.toList());
		List<Character> rlt = flatMap(words2, as);
		out.println(rlt);

		out.println("\n>>> partition word in words2 into two list depending on whether its length>2:");
		out.println(">>> worsd2 = " + words2);
		out.println(partition(words2, s -> s.length() > 2));

		out.println("\n>>> group strings in words2 according to their length:");
		out.println(">>> words2 = " + words2);
		out.println(collect(words2, Collectors.groupingBy(s -> s.length())));

	}

	public static void main(String[] args) {		
		
		// Your result for selftest() should look like the text shown in file HW3ExpectedResult.txt
		selfTest();

	}

}
