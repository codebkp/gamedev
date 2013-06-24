package patagonia.edn.parser;

import patagonia.edn.EdnSyntaxException;
import patagonia.edn.Tag;

/**
 * This class may be extended to support additional {@code #inst}
 * representations.
 *
 * @see InstantToCalendar
 * @see InstantToDate
 * @see InstantToTimestamp
 * @see ParsedInstant
 */
public abstract class AbstractInstantHandler implements TagHandler {

    public final Object transform(Tag tag, Object value) {
        if (!(value instanceof String)) {
            throw new EdnSyntaxException(tag.toString() + " expects a String.");
        }
        return transform(InstantUtils.parse((String) value));
    }

    /**
     * This will be called by the Parser when parsing an {@code #inst} value.
     * Implement it to return an instance of your chosen instant representation.
     *
     * @param pi
     *            The contents fields of string following the {@code #inst} tag
     *            as a {@link ParsedInstant}.
     * @return Some value representing the instant pi. Never null.
     */
    protected abstract Object transform(ParsedInstant pi);

}
