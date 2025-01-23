package org.example.currencyexchangerefactoring.model;


import java.util.Objects;

public class CurrencyE {
    private Long id;
    private String code;
    private String name;
    private String sign;


    public CurrencyE(Long id, String code, String name, String sign) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.sign = sign;
    }

    public CurrencyE() {
    }

    public CurrencyE(String code, String name, String sign) {
        this.code = code;
        this.name = name;
        this.sign = sign;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyE currencyE = (CurrencyE) o;
        return Objects.equals(id, currencyE.id) && Objects.equals(code, currencyE.code) && Objects.equals(name, currencyE.name) && Objects.equals(sign, currencyE.sign);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, sign);
    }

    @Override
    public String toString() {
        return "CurrencyE{" +
               "id=" + id +
               ", code='" + code + '\'' +
               ", name='" + name + '\'' +
               ", sign='" + sign + '\'' +
               '}';
    }
}