package com.storedobject.vaadin;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A field that allows you to select multiple values from a fixed list of Strings. The value is returned as
 * a bit pattern with its positional values set for the selected items. The first item in the list uses the least significant bit.
 *
 * @author Syam
 */
public class TokenChoicesField extends TranslatedField<Integer, Set<String>> {
    
    private static Integer ZERO = 0;
    private int valueMask;

    /**
     * Constructor.
     *
     * @param choices Choices delimited by comma
     */
    public TokenChoicesField(String choices) {
        this(null, choices);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param choices Choices delimited by comma
     */
    public TokenChoicesField(String label, String choices) {
        this(label, Arrays.asList(choices.split(",")));
    }

    /**
     * Constructor.
     *
     * @param choices Choices
     */
    public TokenChoicesField(String[] choices) {
        this(null, choices);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param choices Choices
     */
    public TokenChoicesField(String label, String[] choices) {
        this(label, Arrays.asList(choices));
    }

    /**
     * Constructor.
     * @param choices Choices
     */
    public TokenChoicesField(Iterable<?> choices) {
        this(null, choices);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param choices Choices
     */
    public TokenChoicesField(String label, Iterable<?> choices) {
        this(label, createList(choices));
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param choices Choices
     */
    public TokenChoicesField(String label, Collection<String> choices) {
        this(label, createList(choices));
    }

    private TokenChoicesField(String label, List<String> choices) {
        super(new TField(choices), (f, s) -> ((TField)f).toValue(s), (f, i) -> ((TField)f).toSet(i), ZERO);
        setLabel(label);
        addValueChangeListener(e -> checkMask(e.getValue()));
    }

    private void checkMask(int newValue) {
        if((newValue & valueMask) != valueMask) {
            setValue(newValue | valueMask);
        }
    }

    private static List<String> createList(Iterable<?> list) {
        ArrayList<String> a = new ArrayList<>();
        list.forEach(item -> {
            String s = null;
            if(item != null) {
                s = item.toString();
            }
            if(s != null) {
                a.add(s);
            }
        });
        return a;
    }

    /**
     * A "mask" allows one to fix certain bits in the value of the field. For example, if the mask is set to
     * 5 (bit pattern 101), the zeroth and the third bits from the right will be always set to 1.
     *
     * @param valueMask Mask to set.
     */
    public void setValueMask(int valueMask) {
        this.valueMask = valueMask;
        checkMask(getValue());
    }

    private static class TField extends TokensField<String> {

        private final List<String> choices;

        private TField(List<String> choices) {
            super(choices);
            this.choices = choices;
            setLabel(null);
        }

        private int toValue(Set<String> set) {
            int v = 0;
            for(int i = choices.size() - 1; i >= 0; i--) {
                v <<= 1;
                if(set.contains(choices.get(i))) {
                    v |= 1;
                }
            }
            return v;
        }

        private Set<String> toSet(int i) {
            Set<String> s = new HashSet<>();
            int p = 0;
            while(i > 0 && p < choices.size()) {
                if((i & 1) == 1) {
                    s.add(choices.get(p));
                }
                i >>= 1;
                ++p;
            }
            return s;
        }
    }
}
