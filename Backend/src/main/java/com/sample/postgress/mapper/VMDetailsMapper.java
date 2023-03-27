package com.sample.postgress.mapper;

import com.sample.postgress.model.PricingApiResources;
import com.sample.postgress.model.VMDetails;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VMDetailsMapper implements RowMapper<VMDetails> {
    @Override
    public VMDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        VMDetails resources = new VMDetails();

        resources.setOs(rs.getString("os"));
        resources.setVmName(rs.getString("vmname"));
        resources.setSkuOrInstanceName(rs.getString("skuorinstancename"));
        resources.setRegion(rs.getString("region"));
        resources.setVmInstanceId(rs.getString("vminstanceid"));
        resources.setGroupName(rs.getString("groupname"));
        resources.setCreationTime(rs.getString("creationtime"));
        return resources;
    }
}
