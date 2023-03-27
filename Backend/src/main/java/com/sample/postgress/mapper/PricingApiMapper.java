package com.sample.postgress.mapper;

import com.sample.postgress.model.Prices;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PricingApiMapper implements RowMapper<Prices> {
    @Override
    public Prices mapRow(ResultSet rs, int arg1) throws SQLException {
        Prices pricingApi = new Prices();
        pricingApi.setPrices(rs.getString("prices"));
        pricingApi.setRegion(rs.getString("region"));
        pricingApi.setSkuOrInstanceName(rs.getString("instancetype"));
       try {
           pricingApi.setProcessor(rs.getString("processor"));
       }
       catch (Exception e ){
           //Do nothing
       }

        return pricingApi;
    }

}
