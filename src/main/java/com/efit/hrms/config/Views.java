package com.efit.hrms.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class Views {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initialize() {
        try {
            executeQueries();
        } catch (Exception e) {
            throw new RuntimeException("Error initializing database", e);
        }
    }

    private void executeQueries() {
    	
    	// Exrates View
    	jdbcTemplate.execute("CREATE OR REPLACE VIEW attendancetime AS\r\n"
    			+ "SELECT \r\n"
    			+ "    MAX(in_entry.id) AS id,\r\n"
    			+ "    in_entry.empcode,\r\n"
    			+ "    emp.empname,\r\n"
    			+ "    DATE(in_entry.createdon) AS entrydate,\r\n"
    			+ "    MIN(in_entry.entrytime) AS CheckInTime,\r\n"
    			+ "    IFNULL(MAX(out_entry.entrytime), '00:00:00') AS CheckOutTime,\r\n"
    			+ "    \r\n"
    			+ "    -- Effective From: Time between first IN and the earliest OUT after that\r\n"
    			+ "    IFNULL(\r\n"
    			+ "      SEC_TO_TIME(SUM(\r\n"
    			+ "        TIME_TO_SEC(TIMEDIFF(\r\n"
    			+ "          (\r\n"
    			+ "            SELECT MIN(c2.entrytime)\r\n"
    			+ "            FROM checkin c2\r\n"
    			+ "            WHERE c2.empcode = in_entry.empcode\r\n"
    			+ "              AND c2.status = 'Out'\r\n"
    			+ "              AND DATE(c2.createdon) = DATE(in_entry.createdon)\r\n"
    			+ "              AND c2.createdon >= in_entry.createdon\r\n"
    			+ "          ),\r\n"
    			+ "          in_entry.entrytime\r\n"
    			+ "        ))\r\n"
    			+ "      )),\r\n"
    			+ "      '00:00:00'\r\n"
    			+ "    ) AS effectivefrom,\r\n"
    			+ "\r\n"
    			+ "    -- Total working hours between first IN and last OUT\r\n"
    			+ "    CASE \r\n"
    			+ "        WHEN MAX(out_entry.entrytime) IS NOT NULL\r\n"
    			+ "        THEN SEC_TO_TIME(TIME_TO_SEC(MAX(out_entry.entrytime)) - TIME_TO_SEC(MIN(in_entry.entrytime)))\r\n"
    			+ "        ELSE '00:00:00'\r\n"
    			+ "    END AS TotalWorkingHours\r\n"
    			+ "\r\n"
    			+ "FROM \r\n"
    			+ "    (\r\n"
    			+ "        SELECT \r\n"
    			+ "            c1.checkinid AS id,\r\n"
    			+ "            c1.empcode,\r\n"
    			+ "            c1.entrytime,\r\n"
    			+ "            c1.createdon\r\n"
    			+ "        FROM checkin c1\r\n"
    			+ "        WHERE c1.status = 'In'\r\n"
    			+ "    ) in_entry\r\n"
    			+ "\r\n"
    			+ "LEFT JOIN\r\n"
    			+ "    (\r\n"
    			+ "        SELECT \r\n"
    			+ "            c2.empcode,\r\n"
    			+ "            c2.entrytime,\r\n"
    			+ "            c2.createdon\r\n"
    			+ "        FROM checkin c2\r\n"
    			+ "        WHERE c2.status = 'Out'\r\n"
    			+ "    ) out_entry\r\n"
    			+ "    ON in_entry.empcode = out_entry.empcode \r\n"
    			+ "    AND DATE(in_entry.createdon) = DATE(out_entry.createdon)\r\n"
    			+ "    AND out_entry.createdon = (\r\n"
    			+ "        SELECT MAX(c3.createdon)\r\n"
    			+ "        FROM checkin c3\r\n"
    			+ "        WHERE c3.empcode = in_entry.empcode \r\n"
    			+ "        AND c3.status = 'Out'\r\n"
    			+ "        AND DATE(c3.createdon) = DATE(in_entry.createdon)\r\n"
    			+ "    )\r\n"
    			+ "\r\n"
    			+ "-- Join to get empname\r\n"
    			+ "LEFT JOIN \r\n"
    			+ "    (\r\n"
    			+ "        SELECT empcode, empname\r\n"
    			+ "        FROM checkin\r\n"
    			+ "        WHERE empname IS NOT NULL\r\n"
    			+ "        GROUP BY empcode, empname\r\n"
    			+ "    ) emp ON emp.empcode = in_entry.empcode\r\n"
    			+ "\r\n"
    			+ "GROUP BY in_entry.empcode, emp.empname, DATE(in_entry.createdon)\r\n"
    			+ "ORDER BY MAX(in_entry.createdon) DESC");
    }

}
