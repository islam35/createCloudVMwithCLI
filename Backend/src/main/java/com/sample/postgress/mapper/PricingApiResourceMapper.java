package com.sample.postgress.mapper;

import com.sample.postgress.model.Prices;
import com.sample.postgress.model.PricingApiResources;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PricingApiResourceMapper implements RowMapper<PricingApiResources> {

    @Override
    public PricingApiResources mapRow(ResultSet rs, int arg1) throws SQLException {
        PricingApiResources resources = new PricingApiResources();

        resources.setVcpu(rs.getString("vcpu"));
        resources.setMemory(rs.getString("memory"));

        return resources;
    }
}
