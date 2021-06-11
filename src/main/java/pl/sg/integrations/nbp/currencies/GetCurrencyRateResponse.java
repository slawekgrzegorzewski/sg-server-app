package pl.sg.integrations.nbp.currencies;

import java.math.BigDecimal;

public class GetCurrencyRateResponse {
    private String table;
    private String currency;
    private String code;
    private Rate[] rates;

    public GetCurrencyRateResponse(String table, String currency, String code, Rate[] rates) {
        this.table = table;
        this.currency = currency;
        this.code = code;
        this.rates = rates;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Rate[] getRates() {
        return rates;
    }

    public void setRates(Rate[] rates) {
        this.rates = rates;
    }

    public static class Rate {
        private String no;
        private String effectiveDate;
        private BigDecimal mid;

        public Rate(String no, String effectiveDate, BigDecimal mid) {
            this.no = no;
            this.effectiveDate = effectiveDate;
            this.mid = mid;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getEffectiveDate() {
            return effectiveDate;
        }

        public void setEffectiveDate(String effectiveDate) {
            this.effectiveDate = effectiveDate;
        }

        public BigDecimal getMid() {
            return mid;
        }

        public void setMid(BigDecimal mid) {
            this.mid = mid;
        }
    }
}
