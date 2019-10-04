package com.asset.jupiter.JUPITER.jupiterService;


import com.asset.jupiter.Aop.TimeTrack;
import com.asset.jupiter.JUPITER.Model.Entities.Indexclass;
import com.asset.jupiter.JUPITER.Model.Entities.LookupListCode;
import com.asset.jupiter.Util.defines.Defines;
import com.asset.jupiter.Util.exceptions.JupiterException;
import com.asset.jupiter.Util.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@org.springframework.transaction.annotation.Transactional(value = "transactionManager", rollbackFor = JupiterException.class)
public class dynamicService {

    @Autowired
    DataSource dataSource;

    @Autowired
    lookupListCodeService lookupListCodeService;


    @TimeTrack
    public ArrayList<String> getDynamicTableInfo(String tableName, String ownerName) throws SQLException {

        ArrayList<String> columnNames = new ArrayList<>();
        Connection connection = dataSource.getConnection();
        // prepared Statement
        Statement statement = connection.createStatement();
        // execute and return resultSet
        ResultSet resultSet = statement.executeQuery("SELECT COLUMN_NAME FROM ALL_TAB_COLUMNS WHERE TABLE_NAME='" + tableName + "' AND OWNER = '" + ownerName + "' ORDER BY COLUMN_ID");
        // get columns Name From Table
        while (resultSet.next()) {
            columnNames.add(resultSet.getString("COLUMN_NAME"));

        }

        statement.close();
        connection.close();
        return columnNames;
    }


    @TimeTrack
    // insert into database
    public boolean insertDynamicTableInfo(String tableName,
                                          ArrayList<String> columnNames,
                                          ArrayList columnValues,
                                          Indexclass indexClass,
                                          HashMap<String, String> indexClassMetaData
    ) throws SQLException {
        // get connection From database
        Connection connection = dataSource.getConnection();
        // map to Insert
        LinkedHashMap<String, Object> columnValueMappingForInsert = new LinkedHashMap<>();

        columnValueMappingForInsert.put(columnNames.get(0), columnValues.get(0).toString());
        columnValueMappingForInsert.put(columnNames.get(1), columnValues.get(1).toString());
        columnNames.remove(0);
        columnNames.remove(0);

        // execute query to select all fields and types by indexClassId
        PreparedStatement st = connection.prepareStatement(
                "SELECT IndexClass.ClassId,ClassName,MediaServerId,VIEWID" +
                        ",ClassTables.TABLE_ID,TABLENAME,Fields.FIELDID,VIEWFIELDS.DISPLAYNAME ," +
                        "Fields.NAME,Fields.TYPE,Fields.FieldOrder,Fields.LOOKUP_LIST_ID ," +
                        "Fields.RefTabId,Fields.RefFldOrder,Fields.RefFldDispOrder ,Fields.MULTIVALUE" +
                        " FROM IndexClass,ClassTables,Tables,ViewFields,Fields " +
                        "WHERE IndexClass.ClassId= ? AND VIEWFIELDS.VIEWID=? " +
                        "AND IndexClass.ClassId=ClassTables.ClassId AND" +
                        " (ClassTables.Table_Id=Tables.ID AND ClassTables.Table_Id=Fields.Table_Id) " +
                        "AND Fields.FieldId=ViewFields.FieldId ORDER BY Fields.FieldOrder");
        st.setString(1, indexClass.getClassid());
        st.setString(2, indexClass.getDefviewid());
        ResultSet resultSet = st.executeQuery();

        // get all metadata From Database For Specific IndexClass
        int count = 0;
        while (resultSet.next()) {
            String displaynameColumn = resultSet.getString("DISPLAYNAME");
            Byte columnType = resultSet.getByte("TYPE");
            // order Value
            String displaynameColumnValue = indexClassMetaData.get(displaynameColumn);
            // if displaynameColumnValue == null set = " "
            displaynameColumnValue = displaynameColumnValue == null ? "" : displaynameColumnValue;
            // get lockup List Id If exists = lookup Field
            String lookUpField = resultSet.getString("LOOKUP_LIST_ID");

            // get refernece values
            // - refernce table Id
            String RefTabId = resultSet.getString("REFTABID");


            //check lockup Field
            if (lookUpField != null) {
                // this lockup
                // get by value And lookUp ListId
                CompletableFuture<LookupListCode> byValue = lookupListCodeService.findByValue(lookUpField, displaynameColumnValue);
                LookupListCode lookupListCode = null;
                try {
                    lookupListCode = byValue.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                // if database object = null set value = "" else set value = codeId
                displaynameColumnValue = lookupListCode == null ? "" : lookupListCode.getCodeId();
            }
            // check Refernce Field
            if (!RefTabId.equals("0")) {

                String tableNameRef = "";
                // this is Refernce
                // - refernce Field order
                int RefFldOrder = resultSet.getInt("REFFLDORDER");
                // - refernce Field Display order
                int RefFldDispOrder = resultSet.getInt("REFFLDDISPORDER");
                int multiValue = resultSet.getInt("MULTIVALUE");
//                displaynameColumnValue =

                // select from tables Where tableId = refTable Id
                PreparedStatement preparedStatement = connection.
                        prepareStatement(
                                "select * from TABLES WHERE ID=?");
                preparedStatement.setString(1, RefTabId);
                ResultSet resultSet1 = preparedStatement.executeQuery();
                while (resultSet1.next()) {
                    // get table Name
                    tableNameRef = resultSet1.getString("TABLENAME");
                }
                preparedStatement.close();
                // select code from tableName By order
                PreparedStatement preparedStatementTable = connection.
                        prepareStatement(
                                "select * from " + tableNameRef + " where " + "FIELD" + RefFldDispOrder + "_" + multiValue + " = '" + displaynameColumnValue + "'");
                ResultSet resultSet2 = preparedStatementTable.executeQuery();
                if (resultSet2.next()) {
                    // add to hashMap References
                    String code = resultSet2.getString("FIELD" + RefFldOrder + "_" + multiValue);

                    displaynameColumnValue = code;

                    System.out.println(code);
                } else {
                    displaynameColumnValue = "";
                }
                preparedStatementTable.close();
                preparedStatement.close();
            }


            // check Type
            switch (columnType) {
                case Defines.TYPE_DATE:
                    // check json date format
                    boolean dateValid;
                    // cast It To Date
                    SimpleDateFormat format = null;
                    // date format 1
                    if (displaynameColumnValue.contains("/")) {
                        //check Date Valid Or Not
                        dateValid = isDateValid1(displaynameColumnValue);
                        // date formate
                        if (dateValid)
                            format = new SimpleDateFormat("dd/MM/yyyy");
                    }
                    // date format 2
                    else {
                        //check Date Valid Or Not
                        dateValid = isDateValid2(displaynameColumnValue);
                        // date formate
                        if (dateValid)
                            format = new SimpleDateFormat("dd-MM-yyyy");
                    }
                    // if valid
                    if (format != null) {
                        long dateLong = 0;
                        try {
                            Date date = format.parse(displaynameColumnValue);
                            dateLong = dateToLong(date);


                        } catch (ParseException e) {
                            Log.error("", "Error in parsing date value: " + e.getMessage(), e);
                        }

                        // insert into hashMap
                        columnValueMappingForInsert.put(columnNames.get(count), dateLong);

                    }
                    break;
                default:
                    // any DataType
                    columnValueMappingForInsert.put(columnNames.get(count), displaynameColumnValue);
                    break;

            }
            count++;
        }
        st.close();

        // create insert sql query
        String sql = insertSQL(tableName, columnValueMappingForInsert);

        PreparedStatement st1 = connection.prepareStatement(sql);
        st1.execute();
        st1.close();
        connection.close();


        return true;
    }

    /**
     * <h1>Get INSERT SQL Query</h1>
     * <p>It is a generic function. It can be use for any DB Table</p>
     *
     * @param tableName                   Table on which the INSERT Operation will be performed.
     * @param columnValueMappingForInsert List of Column & Value pair to Insert.
     * @return Final generated INSERT SQL Statement.
     * @author Debopam Pal, Software Developer, NIC, India.
     */
    public static String insertSQL(String tableName, Map<String, Object> columnValueMappingForInsert) {
        StringBuilder insertSQLBuilder = new StringBuilder();

        /**
         * Removing column that holds NULL value or Blank value...
         */
//        if (!columnValueMappingForInsert.isEmpty()) {
//            for (Map.Entry<String, Object> entry : columnValueMappingForInsert.entrySet()) {
//                if (entry.getValue() == null || entry.getValue().equals("")) {
//                    columnValueMappingForInsert.remove(entry.getKey());
//                }
//            }
//        }

        /* Making the INSERT Query... */
        insertSQLBuilder.append("INSERT INTO");
        insertSQLBuilder.append(" ").append(tableName);
        insertSQLBuilder.append("(");

        if (!columnValueMappingForInsert.isEmpty()) {
            for (Map.Entry<String, Object> entry : columnValueMappingForInsert.entrySet()) {
                insertSQLBuilder.append(entry.getKey());
                insertSQLBuilder.append(",");
            }
        }

        insertSQLBuilder = new StringBuilder(insertSQLBuilder.subSequence(0, insertSQLBuilder.length() - 1));
        insertSQLBuilder.append(")");
        insertSQLBuilder.append(" VALUES");
        insertSQLBuilder.append("(");

        if (!columnValueMappingForInsert.isEmpty()) {
            for (Map.Entry<String, Object> entry : columnValueMappingForInsert.entrySet()) {
                insertSQLBuilder.append("'" + entry.getValue() + "'");
                insertSQLBuilder.append(",");
            }
        }

        insertSQLBuilder = new StringBuilder(insertSQLBuilder.subSequence(0, insertSQLBuilder.length() - 1));
        insertSQLBuilder.append(")");

        // Returning the generated INSERT SQL Query as a String...
        return insertSQLBuilder.toString();
    }

    /**
     * <h1>Get DELETE SQL Query</h1>
     * <p>It is a generic function. It can be use for any DB Table.</p>
     *
     * @param tableName                      Table on which the DELETE Operation will be performed.
     * @param columnValueMappingForCondition List of Column & Value pair for WHERE clause.
     * @return Final generated DELETE SQL Statement.
     * @author Debopam Pal, Software Developer, NIC, India.
     */
    public static String deleteSQL(String tableName, Map<String, String> columnValueMappingForCondition) {
        StringBuilder deleteSQLBuilder = new StringBuilder();

        /**
         * Removing column that holds NULL value or Blank value...
         */
        if (!columnValueMappingForCondition.isEmpty()) {
            for (Map.Entry<String, String> entry : columnValueMappingForCondition.entrySet()) {
                if (entry.getValue() == null || entry.getValue().equals("")) {
                    columnValueMappingForCondition.remove(entry.getKey());
                }
            }
        }

        /* Making the DELETE Query */
        deleteSQLBuilder.append("DELETE FROM");
        deleteSQLBuilder.append(" ").append(tableName);
        deleteSQLBuilder.append(" WHERE");
        deleteSQLBuilder.append(" ");

        if (!columnValueMappingForCondition.isEmpty()) {
            for (Map.Entry<String, String> entry : columnValueMappingForCondition.entrySet()) {
                deleteSQLBuilder.append(entry.getKey()).append("=").
                        append("'" + entry.getValue() + "'");
                deleteSQLBuilder.append(" AND ");
            }
        }

        deleteSQLBuilder = new StringBuilder(deleteSQLBuilder.subSequence(0, deleteSQLBuilder.length() - 5));

        // Returning the generated DELETE SQL Query as a String...
        return deleteSQLBuilder.toString();
    }

    /**
     * <h1>Get UPDATE SQL Query</h1>
     * <p>It is a generic function. It can be use for any DB Table</p>
     *
     * @param tableName                      Table on which the UPDATE Operation will be performed.
     * @param columnValueMappingForSet       List of Column & Value pair to Update.
     * @param columnValueMappingForCondition List of Column & Value pair for WHERE clause.
     * @return Final generated UPDATE SQL Statement.
     * @author Debopam Pal, Software Developer, NIC, India.
     */
    public static String updateSQL(String tableName, Map<String,
            String> columnValueMappingForSet, Map<String, String> columnValueMappingForCondition) {
        StringBuilder updateQueryBuilder = new StringBuilder();

        /**
         * Removing column that holds NULL value or Blank value...
         */
        if (!columnValueMappingForSet.isEmpty()) {
            for (Map.Entry<String, String> entry : columnValueMappingForSet.entrySet()) {
                if (entry.getValue() == null || entry.getValue().equals("")) {
                    columnValueMappingForSet.remove(entry.getKey());
                }
            }
        }

        /**
         * Removing column that holds NULL value or Blank value...
         */
        if (!columnValueMappingForCondition.isEmpty()) {
            for (Map.Entry<String, String> entry : columnValueMappingForCondition.entrySet()) {
                if (entry.getValue() == null || entry.getValue().equals("")) {
                    columnValueMappingForCondition.remove(entry.getKey());
                }
            }
        }

        /* Making the UPDATE Query */
        updateQueryBuilder.append("UPDATE");
        updateQueryBuilder.append(" ").append(tableName);
        updateQueryBuilder.append(" SET");
        updateQueryBuilder.append(" ");

        if (!columnValueMappingForSet.isEmpty()) {
            for (Map.Entry<String, String> entry : columnValueMappingForSet.entrySet()) {
                updateQueryBuilder.append(entry.getKey()).append("=").append(entry.getValue());
                updateQueryBuilder.append(",");
            }
        }

        updateQueryBuilder = new StringBuilder
                (updateQueryBuilder.subSequence(0, updateQueryBuilder.length() - 1));
        updateQueryBuilder.append(" WHERE");
        updateQueryBuilder.append(" ");

        if (!columnValueMappingForCondition.isEmpty()) {
            for (Map.Entry<String, String> entry : columnValueMappingForCondition.entrySet()) {
                updateQueryBuilder.append(entry.getKey()).append("=").append(entry.getValue());
                updateQueryBuilder.append(",");
            }
        }

        updateQueryBuilder = new StringBuilder
                (updateQueryBuilder.subSequence(0, updateQueryBuilder.length() - 1));

        // Returning the generated UPDATE SQL Query as a String...
        return updateQueryBuilder.toString();
    }

    /**
     * Insert the method's description here.
     * Creation date: (2000-Mar-00 11:09:38 AM)
     *
     * @param date java.util.Date
     * @return long
     */
    public static long dateToLong(Date date) {
        String dayFormula = null;
        String monthFormula = null;
        String hourFormula = null;
        String minuteFormula = null;
        String secondFormula = null;


        if (date.getHours() < 10) {
            hourFormula = ("0" + date.getHours());
        } else {
            hourFormula = Integer.toString(date.getHours());
        }
        if (date.getMinutes() < 10) {
            minuteFormula = ("0" + date.getMinutes());
        } else {
            minuteFormula = Integer.toString(date.getMinutes());
        }
        if (date.getSeconds() < 10) {
            secondFormula = ("0" + date.getSeconds());
        } else {
            secondFormula = Integer.toString(date.getSeconds());
        }
        if (date.getDate() < 10) {
            dayFormula = ("0" + date.getDate());
        } else {
            dayFormula = Integer.toString(date.getDate());
        }
        if (date.getMonth() < 9) {
            monthFormula = ("0" + (date.getMonth() + 1));
        } else {
            monthFormula = Integer.toString(date.getMonth() + 1);
        }
        String returnedFormula =
                (Integer.toString(1900 + date.getYear()) + monthFormula + dayFormula);
        dayFormula = null;
        monthFormula = null;
        hourFormula = null;
        minuteFormula = null;
        secondFormula = null;
        return Long.parseLong(returnedFormula);
    }

    // check Validate Jupiter Date
    public static boolean isDateValid1(String date) {
        if (date == null) {
            return false;
        } //sourror
        else if (date.equalsIgnoreCase("today")) {
            return true;
        } //sourror
        try {
            String dateToCheck = date;
            int day = Integer.parseInt(((dateToCheck.trim()).substring(0, dateToCheck.indexOf("/"))).trim());
            dateToCheck = ((dateToCheck.trim()).substring(dateToCheck.indexOf("/") + 1)).trim();
            int month = Integer.parseInt(((dateToCheck.trim()).substring(0, dateToCheck.indexOf("/"))).trim());
            dateToCheck = ((dateToCheck.trim()).substring(dateToCheck.indexOf("/") + 1)).trim();
            int year = Integer.parseInt(dateToCheck.trim());
            if ((month > 12) || (month < 1)) {
                return false;
            } else {
                if (month == 2) {
                    if (((year % 4) == 0) && (day > 29)) {
                        return false;
                    } else if (((year % 4) > 0) && (day > 28)) {
                        return false;
                    }
                } else if (((month == 1) || (month == 3) || (month == 5) || (month == 7) || (month == 8) ||
                        (month == 10) || (month == 12)) && (day > 31)) {
                    return false;
                } else if (((month == 4) || (month == 6) || (month == 9) || (month == 11)) && (day > 30)) {
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            return false;
        } catch (StringIndexOutOfBoundsException ex) {
            return false;
        }
        return true;
    }


    // check Validate Jupiter Date
    public static boolean isDateValid2(String date) {
        if (date == null) {
            return false;
        } //sourror
        else if (date.equalsIgnoreCase("today")) {
            return true;
        } //sourror
        try {
            String dateToCheck = date;
            int day = Integer.parseInt(((dateToCheck.trim()).substring(0, dateToCheck.indexOf("-"))).trim());
            dateToCheck = ((dateToCheck.trim()).substring(dateToCheck.indexOf("-") + 1)).trim();
            int month = Integer.parseInt(((dateToCheck.trim()).substring(0, dateToCheck.indexOf("-"))).trim());
            dateToCheck = ((dateToCheck.trim()).substring(dateToCheck.indexOf("-") + 1)).trim();
            int year = Integer.parseInt(dateToCheck.trim());
            if ((month > 12) || (month < 1)) {
                return false;
            } else {
                if (month == 2) {
                    if (((year % 4) == 0) && (day > 29)) {
                        return false;
                    } else if (((year % 4) > 0) && (day > 28)) {
                        return false;
                    }
                } else if (((month == 1) || (month == 3) || (month == 5) || (month == 7) || (month == 8) ||
                        (month == 10) || (month == 12)) && (day > 31)) {
                    return false;
                } else if (((month == 4) || (month == 6) || (month == 9) || (month == 11)) && (day > 30)) {
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            return false;
        } catch (StringIndexOutOfBoundsException ex) {
            return false;
        }
        return true;
    }

}
