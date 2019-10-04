package com.asset.jupiter.Aop;

import com.asset.jupiter.Util.configurationService.databaseUnicode;
import com.asset.jupiter.Util.defines.Defines;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.activation.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Aspect
public class UnicodeAspect {

    @Autowired
    javax.sql.DataSource dataSource;

    @Pointcut("execution(@com.asset.jupiter.Aop.Unicode * com.asset.jupiter.Util.handlers.JsonHandler.readJson(..))")
    public void readJsonPointCut() {
    }


    // read Unicode Before Read Json
    @Before(value = "readJsonPointCut()")
    public void beforeReadJson(JoinPoint jp) throws SQLException {

        String unicode = databaseUnicode.getUnicode();
        if (unicode == null || unicode.trim().isEmpty()) {

            // select From Database CharacterSet
            Connection connection = dataSource.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("select value" +
                    " from v$nls_parameters" +
                    " where parameter = 'NLS_CHARACTERSET'");

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String characterSetValue = resultSet.getString("value");
                // set init = database characterset
                String dbEncoding = characterSetValue;
                if (characterSetValue.equalsIgnoreCase(Defines.ARA_DB_CHARSET_2))
                    dbEncoding = Defines.CODE_PAGE_ARABIC;
                else if (characterSetValue.equalsIgnoreCase(Defines.UTF8_DB_CHARSET) ||
                        characterSetValue.equalsIgnoreCase(Defines.AL32UTF8_DB_CHARSET))
                    dbEncoding = Defines.CODE_PAGE_UTF8;
                else if (characterSetValue.equalsIgnoreCase(Defines.ARA_DB_CHARSET_1))
                    dbEncoding = Defines.CODE_PAGE_P6;
                else
                    dbEncoding = Defines.CODE_PAGE_ENGLISH;


                // set unicode to static value to use It
                databaseUnicode.setUnicode(dbEncoding);

            }
        }


    }

}
