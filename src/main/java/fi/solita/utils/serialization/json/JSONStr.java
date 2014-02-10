package fi.solita.utils.serialization.json;

/**
 * JSON serial format. Just a wrapper for String.
 */
public final class JSONStr implements CharSequence {
    private final CharSequence json;

    public JSONStr(CharSequence json) {
        this.json = json;
    }

    @Override
    public int length() {
        return json.length();
    }

    @Override
    public char charAt(int index) {
        return json.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return json.subSequence(start, end);
    }
    
    @Override
    public String toString() {
        return json.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((json == null) ? 0 : json.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JSONStr other = (JSONStr) obj;
        if (json == null) {
            if (other.json != null)
                return false;
        } else if (!json.equals(other.json))
            return false;
        return true;
    }
}
