package patagonia.edn.parser;

import java.util.ArrayList;
import java.util.Collections;

final class DefaultVectorFactory implements CollectionBuilder.Factory {
    public CollectionBuilder builder() {
        return new CollectionBuilder() {
            ArrayList<Object> list = new ArrayList<Object>();
            public void add(Object o) {
                list.add(o);
            }
            public Object build() {
                return Collections.unmodifiableList(list);
            }
        };
    }
}