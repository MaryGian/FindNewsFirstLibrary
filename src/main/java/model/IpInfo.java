package model;

import model.ipApi.IpResults;

public class IpInfo {
    private String country_code;

    public IpInfo(String country_code) {
        this.country_code = country_code;
    }

    public IpInfo(IpResults country_code){
        this.country_code=country_code.getCountryCode();
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }
}
