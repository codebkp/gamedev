package patagonia.edn.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

final class DefaultMapFactory implements CollectionBuilder.Factory {
    public CollectionBuilder builder() {
        return new CollectionBuilder() {
            final Object none = new Object();
            final Map<Object,Object> map = new HashMap<Object,Object>();
            Object key = none;
            public void add(Object o) {
                if (key == none) {
                    key = o;
                } else {
                    map.put(key, o);
                    key = none;
                }
            }
            public Object build() {
                if (key != none) {
                    throw new IllegalStateException(
                            "Every map must have an equal number of keys and values.");
                }
                return Collections.unmodifiableMap(map);
            }
        };
    }
}