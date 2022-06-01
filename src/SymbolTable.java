import java.util.*;

public class SymbolTable { // ------------v hash table implementation of a Map interface
    final Map<String, Integer> OffsetMap = new HashMap<>();
    // ^ object that maps keys to values

    public void insert(String s, int address) {
        if (!OffsetMap.containsValue(address)) // returns true if this map maps one or more keys to the specified value
            OffsetMap.put(s, address); // associates "address" value with "s" key
        else
            throw new IllegalArgumentException(
                    "Riferimento ad una locazione di memoria gia` occupata da un'altra variabile");
    }

    public int lookupAddress(String s) {
        // return true if this map contains a mapping for "s" key
        return OffsetMap.getOrDefault(s, -1); // returns value of "s" key
    }
}
