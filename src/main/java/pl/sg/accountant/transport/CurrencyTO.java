package pl.sg.accountant.transport;

public class CurrencyTO {
    private final String code;
    private final String displayName;

    public CurrencyTO(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }
}