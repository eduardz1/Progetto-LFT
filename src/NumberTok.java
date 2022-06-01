public class NumberTok extends Token {
	public final int value;

	public NumberTok(int tag, String s){
		super(tag);
		value = Integer.parseInt(s);
	}

	public String toString() {
		return "<" + tag + ", " + value + ">";
	}
}
