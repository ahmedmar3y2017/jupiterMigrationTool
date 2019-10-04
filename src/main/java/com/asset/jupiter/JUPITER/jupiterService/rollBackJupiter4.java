package com.asset.jupiter.JUPITER.jupiterService;

import com.asset.jupiter.JUPITER.Dao.*;
import com.asset.jupiter.JUPITER.Model.Entities.*;
import com.asset.jupiter.Util.configurationService.config;
import com.asset.jupiter.Util.exceptions.JupiterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

@Service
@Transactional(value = "transactionManager", rollbackFor = JupiterException.class, noRollbackFor = ObjectOptimisticLockingFailureException.class)
public class rollBackJupiter4 {
    @Autowired
    itemRepo itemRepo;
    @Autowired
    linksRepo linksRepo;
    @Autowired
    itemIndexclassRepo itemIndexclassRepo;
    @Autowired
    itemsCountRepo itemsCountRepo;
    @Autowired
    accessRuleDao accessRuleDao;
    @Autowired
    dynamicService dynamicService;
    @Autowired
    JdbcTemplate jdbcTemplate;

    // properties
    @Autowired
    config config;

    public void rollBack(Item item, AccessrulePK accessrulePK, LinkPK linkPK,
                         ItemIndexclassPK itemIndexclassPK,
                         ItemscountPK itemscountPK

    ) {


        if (itemIndexclassPK != null) {
//            itemIndexclassRepo.deleteById(itemIndexclassPK); // 4

            // jdbc
            Object[] params = {itemIndexclassPK.getItemId(), itemIndexclassPK.getIndexclassId()};

            jdbcTemplate.update("delete from " + config.getJUPITER_SCHEMA() + ".ITEM_INDEXCLASS where ITEM_ID=? and INDEXCLASS_ID=?", params);
        }
        if (linkPK != null) {
//            linksRepo.deleteById(linkPK); // 3
            // jdbc
            Object[] params = {linkPK.getItemid(), linkPK.getParentid(), linkPK.getParenttype()};
            jdbcTemplate.update("delete from " + config.getJUPITER_SCHEMA() + ".LINKS WHERE ITEMID=? AND PARENTID=? AND PARENTTYPE=?", params);
        }
        if (accessrulePK != null) {
            // delete 2 times
            // 1
            accessrulePK.setUgid(1);
            accessrulePK.setUgtype(1);

//            accessRuleDao.deleteById(accessrulePK);


            // jdbc
            Object[] params = {accessrulePK.getItemid()};
            jdbcTemplate.update("delete from " + config.getJUPITER_SCHEMA() + ".ACCESSRULES WHERE ITEMID=?", params);
            // 2
            accessrulePK.setUgid(3);
            accessrulePK.setUgtype(2);
//            accessRuleDao.deleteById(accessrulePK);

//            // jdbc
            Object[] params1 = {accessrulePK.getItemid()};
            jdbcTemplate.update("delete from " + config.getJUPITER_SCHEMA() + ".ACCESSRULES WHERE ITEMID=?", params1);
        }
        if (item != null) {
            if (!item.getItemid().equalsIgnoreCase("")) {
                //      itemRepo.delete(item); // 1
                Object[] params = {item.getItemid()};

                jdbcTemplate.update("delete from " + config.getJUPITER_SCHEMA() + ".ITEMS where ITEMID =?", params);
            }
        }
        if (itemscountPK != null) {
            if (item != null) {
                // update Only item count - 1
                // get byId
//                Optional<Itemscount> byId = itemsCountRepo.findById(itemscountPK);
//                Itemscount itemscount = byId.get();
                Object[] params = {itemscountPK.getParentid().toString(), itemscountPK.getParenttype(), itemscountPK.getClassid()};
                Itemscount itemscount = jdbcTemplate.queryForObject(
                        "select * from " + config.getJUPITER_SCHEMA() + ".ITEMSCOUNT where PARENTID=? and PARENTTYPE=? and CLASSID=?",
                        params, new UserRowMapper());
                int newVal = itemscount.getItemscount().intValue() - 1;

//                Itemscount itemscountNew = new Itemscount(itemscount.getId(), new BigDecimal(newVal));

                // parameters
                Object[] params1 = {newVal, itemscountPK.getParentid().toString(), itemscountPK.getParenttype(), itemscountPK.getClassid()};

                // update item count
//                itemsCountRepo.save(itemscountNew);
                jdbcTemplate.update("update " + config.getJUPITER_SCHEMA() + ".ITEMSCOUNT set ITEMSCOUNT=? where PARENTID=? and PARENTTYPE=? and CLASSID=?", params1);

            }
        }


    }


    // return using jdbc template
    class UserRowMapper implements RowMapper<Itemscount> {
        @Override
        public Itemscount mapRow(ResultSet rs, int rowNum) throws SQLException {
            ItemscountPK itemscountPK = new ItemscountPK();
            itemscountPK.setClassid(rs.getString("CLASSID"));
            itemscountPK.setParentid(rs.getString("PARENTID"));
            itemscountPK.setParenttype(rs.getLong("PARENTTYPE"));
            Itemscount itemscount = new Itemscount();
            itemscount.setId(itemscountPK);
            itemscount.setItemscount(rs.getBigDecimal("ITEMSCOUNT"));
            return itemscount;
        }
    }


    // -------- rollback Dynamic Table ----------

    public void rollBackDynamicTable(String tableName, String itemId) {
        HashMap<String, String> columnValueMappingForCondition = new HashMap<>();
        columnValueMappingForCondition.put("FIELD0", itemId);
        // generate Generic delete Sql
        String deleteSql = dynamicService.deleteSQL(tableName, columnValueMappingForCondition);

        // begin execute
        // jdbc delete JDBCTEMPLATE
        jdbcTemplate.update(deleteSql);


    }
}
