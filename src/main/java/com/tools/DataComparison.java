package com.tools;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.*;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.*;
import java.util.List;

public class DataComparison {

    public static void main(String[] args) throws Exception {
        String fileName = "/data/file/history/数据量对比表.xlsx";
        List<String> hiveTableNames = new ArrayList<String>();
        List<String> businessTableNames = new ArrayList<String>();
        Map<String, Integer> hiveTableCount = new HashMap<String, Integer>();
        Map<String, Integer> businessTableCount = new HashMap<String, Integer>();
        String hiveDriverClass = "org.apache.hive.jdbc.HiveDriver";
        String cxjwHiveDatabase = "cxjw_lwjd";
        String yswHiveDatabase = "ysw_lwjd";
        String jkwwwHiveDatabase = "jkwww_lwjd";
        String hqwswHiveDatabase = "hqwsw_lwjd";
        String ncnywHiveDatabase = "ncnyw_lwjd";
        String cxjwHiveUrl = "jdbc:hive2://10.69.44.17:10000/" + cxjwHiveDatabase;
        String yswHiveUrl = "jdbc:hive2://10.69.44.17:10000/" + yswHiveDatabase;
        String jkwwwHiveUrl = "jdbc:hive2://10.69.44.17:10000/" + jkwwwHiveDatabase;
        String hqwswHiveUrl = "jdbc:hive2://10.69.44.17:10000/" + hqwswHiveDatabase;
        String ncnywHiveUrl = "jdbc:hive2://10.69.44.17:10000/" + ncnywHiveDatabase;
        String hiveUser = "gzrd_db";
        String hivePwd = "gzrd1234";
        hiveTableNames.addAll(getTableNames(hiveDriverClass, cxjwHiveUrl, hiveUser, hivePwd));
        hiveTableNames.addAll(getTableNames(hiveDriverClass, yswHiveUrl, hiveUser, hivePwd));
        hiveTableNames.addAll(getTableNames(hiveDriverClass, jkwwwHiveUrl, hiveUser, hivePwd));
        hiveTableNames.addAll(getTableNames(hiveDriverClass, hqwswHiveUrl, hiveUser, hivePwd));
        hiveTableNames.addAll(getTableNames(hiveDriverClass, ncnywHiveUrl, hiveUser, hivePwd));
        hiveTableCount.putAll(getTableCount(hiveDriverClass, cxjwHiveUrl, hiveUser, hivePwd));
        hiveTableCount.putAll(getTableCount(hiveDriverClass, yswHiveUrl, hiveUser, hivePwd));
        hiveTableCount.putAll(getTableCount(hiveDriverClass, jkwwwHiveUrl, hiveUser, hivePwd));
        hiveTableCount.putAll(getTableCount(hiveDriverClass, hqwswHiveUrl, hiveUser, hivePwd));
        hiveTableCount.putAll(getTableCount(hiveDriverClass, ncnywHiveUrl, hiveUser, hivePwd));
        String mysqlDriverClass = "com.mysql.jdbc.Driver";
        String businessHqwswDatabase = "gzrd_sw";
        String businessJkwwwDatabase = "gzrd_jkww";
        String businessNcnywDatabase = "gzrd_ncny";
        String businessCxjwDatabase = "gzrd_cxjs";
        String businessYswDatabase = "mingtai";
        String officialDatabase = "renda";
        String businessHqwswUrl = "jdbc:mysql://10.69.44.34:3306/" + businessHqwswDatabase;
        String businessJkwwwUrl = "jdbc:mysql://10.69.44.34:3306/" + businessJkwwwDatabase;
        String businessNcnywUrl = "jdbc:mysql://10.69.44.34:3306/" + businessNcnywDatabase;
        String businessCxjwUrl = "jdbc:mysql://10.69.44.34:3306/" + businessCxjwDatabase;
        String businessYswUrl = "jdbc:mysql://10.69.44.36:3306/" + businessYswDatabase;
        String businessHqwswUser = "gzrd_sw";
        String businessHqwswPwd = "sw_01";
        String businessJkwwwUser = "gzrd_jkww";
        String businessJkwwwPwd = "jkww_#01";
        String businessNcnywUser = "gzrd_ncny";
        String businessNcnywPwd = "ncny_#04";
        String businessCxjwUser = "gzrd_cxjs";
        String businessCxjwPwd = "cxjs_#05";
        String businessYswUser = "mingtai";
        String businessYswPwd = "mingtai";
        String officialUrl = "jdbc:mysql://10.124.153.61:3306/" + officialDatabase;
        String officialUser = "root";
        String officialPassword = "Gzrd@1234";
        businessTableNames.addAll(getTableNames(mysqlDriverClass, businessHqwswUrl, businessHqwswUser, businessHqwswPwd));
        businessTableNames.addAll(getTableNames(mysqlDriverClass, businessJkwwwUrl, businessJkwwwUser, businessJkwwwPwd));
        businessTableNames.addAll(getTableNames(mysqlDriverClass, businessNcnywUrl, businessNcnywUser, businessNcnywPwd));
        businessTableNames.addAll(getTableNames(mysqlDriverClass, businessCxjwUrl, businessCxjwUser, businessCxjwPwd));
        businessTableNames.addAll(getTableNames(mysqlDriverClass, businessYswUrl, businessYswUser, businessYswPwd));
        List<String> officialTableNames = getTableNames(mysqlDriverClass, officialUrl, officialUser, officialPassword);
        List<String> removeTableNames = getRemoveTableNames();
        officialTableNames.removeAll(removeTableNames);
        businessTableNames.removeAll(removeTableNames);
        List<String> officialMoreTableNames = getDifferenceList(officialTableNames, hiveTableNames, businessTableNames);
        List<String> officialNotTableNames = getDifferenceList(getSameList(businessTableNames, hiveTableNames), officialTableNames);
        List<String> businessNotTableNames = getDifferenceList(getSameList(officialTableNames, hiveTableNames), businessTableNames);
        List<String> businessMoreTableNames = getDifferenceList(businessTableNames, officialTableNames, hiveTableNames);
        List<String> sameList = getSameList(businessTableNames, officialTableNames, hiveTableNames);
        businessTableCount.putAll(getTableCount(mysqlDriverClass, businessHqwswUrl, businessHqwswUser, businessHqwswPwd));
        businessTableCount.putAll(getTableCount(hiveDriverClass, businessJkwwwUrl, businessJkwwwUser, businessJkwwwPwd));
        businessTableCount.putAll(getTableCount(hiveDriverClass, businessNcnywUrl, businessNcnywUser, businessNcnywPwd));
        businessTableCount.putAll(getTableCount(hiveDriverClass, businessCxjwUrl, businessCxjwUser, businessCxjwPwd));
        businessTableCount.putAll(getTableCount(hiveDriverClass, businessYswUrl, businessYswUser, businessYswPwd));
        Map<String, Integer> officialTableCount = getTableCount(mysqlDriverClass, officialUrl, officialUser, officialPassword);
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet countSheet = workbook.createSheet("数据量对比表");
        int firstColumnLength = getMaxLengthString(businessTableNames, officialTableNames);
        setTableHeader(countSheet, firstColumnLength);
        CellStyle columnStyle = getColumnStyle(workbook);
        int count = 1;
        for (String tableName : officialMoreTableNames){
            XSSFRow row = countSheet.createRow(count);
            XSSFCell firstColumnCell = row.createCell(0);
            firstColumnCell.setCellValue(tableName);
            XSSFCellStyle firstColumnStyle = workbook.createCellStyle();
            firstColumnStyle.cloneStyleFrom(columnStyle);
            firstColumnStyle.setFillForegroundColor(new XSSFColor(new Color(255, 255, 0)));
            firstColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            firstColumnCell.setCellStyle(firstColumnStyle);
            XSSFCell secondColumnCell = row.createCell(1);
            secondColumnCell.setCellValue(officialTableCount.get(tableName));
            XSSFCellStyle secondColumnStyle = workbook.createCellStyle();
            secondColumnStyle.cloneStyleFrom(columnStyle);
            secondColumnStyle.setFillForegroundColor(new XSSFColor(new Color(255, 0, 0)));
            secondColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            secondColumnCell.setCellStyle(secondColumnStyle);
            XSSFCell thirdColumnCell = row.createCell(2);
            thirdColumnCell.setCellValue("无此表");
            XSSFCellStyle thirdColumnStyle = workbook.createCellStyle();
            thirdColumnStyle.cloneStyleFrom(columnStyle);
            thirdColumnStyle.setFillForegroundColor(new XSSFColor(new Color(255, 0, 0)));
            thirdColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            thirdColumnCell.setCellStyle(thirdColumnStyle);
            XSSFCell fourthColumnCell = row.createCell(3);
            fourthColumnCell.setCellValue("无此表");
            XSSFCellStyle fourthColumnStyle = workbook.createCellStyle();
            fourthColumnStyle.cloneStyleFrom(columnStyle);
            fourthColumnStyle.setFillForegroundColor(new XSSFColor(new Color(255, 0, 0)));
            fourthColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            fourthColumnCell.setCellStyle(fourthColumnStyle);
            count++;
        }
        for (String tableName : officialNotTableNames){
            XSSFRow row = countSheet.createRow(count);
            XSSFCell firstColumnCell = row.createCell(0);
            firstColumnCell.setCellValue(tableName);
            XSSFCellStyle firstColumnStyle = workbook.createCellStyle();
            firstColumnStyle.cloneStyleFrom(columnStyle);
            firstColumnStyle.setFillForegroundColor(new XSSFColor(new Color(255, 255, 0)));
            firstColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            firstColumnCell.setCellStyle(firstColumnStyle);
            XSSFCell secondColumnCell = row.createCell(1);
            secondColumnCell.setCellValue("无此表");
            XSSFCellStyle secondColumnStyle = workbook.createCellStyle();
            secondColumnStyle.cloneStyleFrom(columnStyle);
            secondColumnStyle.setFillForegroundColor(new XSSFColor(new Color(255, 0, 0)));
            secondColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            secondColumnCell.setCellStyle(secondColumnStyle);
            XSSFCell thirdColumnCell = row.createCell(2);
            thirdColumnCell.setCellValue(hiveTableCount.get(tableName));
            XSSFCellStyle thirdColumnStyle = workbook.createCellStyle();
            thirdColumnStyle.cloneStyleFrom(columnStyle);
            thirdColumnStyle.setFillForegroundColor(new XSSFColor(new Color(255, 0, 0)));
            thirdColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            thirdColumnCell.setCellStyle(thirdColumnStyle);
            XSSFCell fourthColumnCell = row.createCell(3);
            fourthColumnCell.setCellValue(businessTableCount.get(tableName));
            XSSFCellStyle fourthColumnStyle = workbook.createCellStyle();
            fourthColumnStyle.cloneStyleFrom(columnStyle);
            fourthColumnStyle.setFillForegroundColor(new XSSFColor(new Color(255, 0, 0)));
            fourthColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            fourthColumnCell.setCellStyle(fourthColumnStyle);
            count++;
        }
        for (String tableName : businessNotTableNames){
            XSSFRow row = countSheet.createRow(count);
            XSSFCell firstColumnCell = row.createCell(0);
            firstColumnCell.setCellValue(tableName);
            XSSFCellStyle firstColumnStyle = workbook.createCellStyle();
            firstColumnStyle.cloneStyleFrom(columnStyle);
            firstColumnStyle.setFillForegroundColor(new XSSFColor(new Color(255, 255, 0)));
            firstColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            firstColumnCell.setCellStyle(firstColumnStyle);
            XSSFCell secondColumnCell = row.createCell(2);
            secondColumnCell.setCellValue(officialTableCount.get(tableName));
            XSSFCellStyle secondColumnStyle = workbook.createCellStyle();
            secondColumnStyle.cloneStyleFrom(columnStyle);
            secondColumnStyle.setFillForegroundColor(new XSSFColor(new Color(255, 0, 0)));
            secondColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            secondColumnCell.setCellStyle(secondColumnStyle);
            XSSFCell thirdColumnCell = row.createCell(3);
            thirdColumnCell.setCellValue(hiveTableCount.get(tableName));
            XSSFCellStyle thirdColumnStyle = workbook.createCellStyle();
            thirdColumnStyle.cloneStyleFrom(columnStyle);
            thirdColumnStyle.setFillForegroundColor(new XSSFColor(new Color(255, 0, 0)));
            thirdColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            thirdColumnCell.setCellStyle(thirdColumnStyle);
            XSSFCell fourthColumnCell = row.createCell(1);
            fourthColumnCell.setCellValue("无此表");
            XSSFCellStyle fourthColumnStyle = workbook.createCellStyle();
            fourthColumnStyle.cloneStyleFrom(columnStyle);
            fourthColumnStyle.setFillForegroundColor(new XSSFColor(new Color(255, 0, 0)));
            fourthColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            fourthColumnCell.setCellStyle(secondColumnStyle);
            count++;
        }
        for (String tableName : businessMoreTableNames){
            XSSFRow row = countSheet.createRow(count);
            XSSFCell firstColumnCell = row.createCell(0);
            firstColumnCell.setCellValue(tableName);
            XSSFCellStyle firstColumnStyle = workbook.createCellStyle();
            firstColumnStyle.cloneStyleFrom(columnStyle);
            firstColumnStyle.setFillForegroundColor(new XSSFColor(new Color(255, 255, 0)));
            firstColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            firstColumnCell.setCellStyle(firstColumnStyle);
            XSSFCell secondColumnCell = row.createCell(1);
            secondColumnCell.setCellValue("无此表");
            XSSFCellStyle secondColumnStyle = workbook.createCellStyle();
            secondColumnStyle.cloneStyleFrom(columnStyle);
            secondColumnStyle.setFillForegroundColor(new XSSFColor(new Color(255, 0, 0)));
            secondColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            secondColumnCell.setCellStyle(secondColumnStyle);
            XSSFCell thirdColumnCell = row.createCell(2);
            thirdColumnCell.setCellValue("无此表");
            XSSFCellStyle thirdColumnStyle = workbook.createCellStyle();
            thirdColumnStyle.cloneStyleFrom(columnStyle);
            thirdColumnStyle.setFillForegroundColor(new XSSFColor(new Color(255, 0, 0)));
            thirdColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            thirdColumnCell.setCellStyle(thirdColumnStyle);
            XSSFCell fourthColumnCell = row.createCell(3);
            fourthColumnCell.setCellValue(businessTableCount.get(tableName));
            XSSFCellStyle fourthColumnStyle = workbook.createCellStyle();
            fourthColumnStyle.cloneStyleFrom(columnStyle);
            fourthColumnStyle.setFillForegroundColor(new XSSFColor(new Color(255, 0, 0)));
            fourthColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            fourthColumnCell.setCellStyle(fourthColumnStyle);
            count++;
        }
        for (String tableName : sameList){
            XSSFRow row = countSheet.createRow(count);
            XSSFCell firstColumnCell = row.createCell(0);
            firstColumnCell.setCellValue(tableName);
            firstColumnCell.setCellStyle(columnStyle);
            XSSFCell secondColumnCell = row.createCell(1);
            Integer officialCount = officialTableCount.get(tableName);
            secondColumnCell.setCellValue(officialCount);
            secondColumnCell.setCellStyle(columnStyle);
            XSSFCell thirdColumnCell = row.createCell(2);
            Integer hiveCount = hiveTableCount.get(tableName);
            thirdColumnCell.setCellValue(hiveCount);
            thirdColumnCell.setCellStyle(columnStyle);
            XSSFCell fourthColumnCell = row.createCell(3);
            Integer businessCount = businessTableCount.get(tableName);
            fourthColumnCell.setCellValue(businessCount);
            fourthColumnCell.setCellStyle(columnStyle);
            XSSFCellStyle firstColumnStyle = workbook.createCellStyle();
            firstColumnStyle.cloneStyleFrom(columnStyle);
            firstColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            XSSFCellStyle secondColumnStyle = workbook.createCellStyle();
            secondColumnStyle.cloneStyleFrom(columnStyle);
            secondColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            XSSFCellStyle thirdColumnStyle = workbook.createCellStyle();
            thirdColumnStyle.cloneStyleFrom(columnStyle);
            thirdColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            XSSFCellStyle fourthColumnStyle = workbook.createCellStyle();
            fourthColumnStyle.cloneStyleFrom(columnStyle);
            fourthColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            if (officialCount.equals(hiveCount) && officialCount.equals(businessCount)){
                firstColumnStyle.setFillForegroundColor(new XSSFColor(new Color(50, 205, 50)));
                secondColumnStyle.setFillForegroundColor(new XSSFColor(new Color(255, 250, 250)));
                thirdColumnStyle.setFillForegroundColor(new XSSFColor(new Color(255, 250, 250)));
                fourthColumnStyle.setFillForegroundColor(new XSSFColor(new Color(255, 250, 250)));
            }else {
                firstColumnStyle.setFillForegroundColor(new XSSFColor(new Color(255, 255, 0)));
                secondColumnStyle.setFillForegroundColor(new XSSFColor(new Color(255, 0, 0)));
                thirdColumnStyle.setFillForegroundColor(new XSSFColor(new Color(255, 0, 0)));
                fourthColumnStyle.setFillForegroundColor(new XSSFColor(new Color(255, 0, 0)));
            }
            firstColumnCell.setCellStyle(firstColumnStyle);
            secondColumnCell.setCellStyle(secondColumnStyle);
            thirdColumnCell.setCellStyle(thirdColumnStyle);
            fourthColumnCell.setCellStyle(fourthColumnStyle);
            count++;
        }
        File file = new File(fileName);
        workbook.write(new FileOutputStream(file));
    }


    private static void setTableHeader(XSSFSheet sheet, int firstColumnLength){
        XSSFWorkbook workbook = sheet.getWorkbook();
        sheet.setColumnWidth(0, firstColumnLength * 256);
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        XSSFRow firstRow = sheet.createRow(0);
        XSSFCellStyle firstRowCellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontName("仿宋");
        font.setFontHeight(11);
        firstRowCellStyle.setFont(font);
        firstRowCellStyle.setFillForegroundColor(new XSSFColor(new Color(190,190,190)));
        firstRowCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        firstRowCellStyle.setAlignment(HorizontalAlignment.CENTER);
        firstRowCellStyle.setBorderTop(BorderStyle.THIN);
        firstRowCellStyle.setBorderBottom(BorderStyle.THIN);
        firstRowCellStyle.setBorderLeft(BorderStyle.THIN);
        firstRowCellStyle.setBorderRight(BorderStyle.THIN);
        XSSFCell firstColumnCell = firstRow.createCell(0);
        firstColumnCell.setCellValue("表名");
        firstColumnCell.setCellStyle(firstRowCellStyle);
        XSSFCell secondColumnCell = firstRow.createCell(1);
        secondColumnCell.setCellValue("采集库");
        secondColumnCell.setCellStyle(firstRowCellStyle);
        XSSFCell thirdColumnCell = firstRow.createCell(2);
        thirdColumnCell.setCellValue("hive库");
        thirdColumnCell.setCellStyle(firstRowCellStyle);
        XSSFCell fourthColumnCell = firstRow.createCell(3);
        fourthColumnCell.setCellValue("业务库");
        fourthColumnCell.setCellStyle(firstRowCellStyle);
    }

    private static CellStyle getColumnStyle(XSSFWorkbook workbook){
        CellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontName("仿宋");
        font.setFontHeight(9);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        return cellStyle;
    }

    private static int getMaxLengthString(List<String> stringList1, List<String> stringList2){
        int sourceMaxLength = Collections.max(stringList1).length();
        int targetMaxLength = Collections.max(stringList2).length();
        return sourceMaxLength > targetMaxLength ? sourceMaxLength : targetMaxLength;
    }

    private static List<String> getRemoveTableNames(){
        ArrayList<String> removeTableNames = new ArrayList<String>();
        removeTableNames.add("gzrd_etl_department");
        removeTableNames.add("gzrd_etl_org");
        removeTableNames.add("gzrd_etl_organization_group");
        removeTableNames.add("gzrd_etl_position");
        removeTableNames.add("gzrd_etl_region");
        removeTableNames.add("gzrd_etl_user");
        removeTableNames.add("gzrd_etl_user_post");
        removeTableNames.add("gzrd_sjcj_bmcdglb");
        removeTableNames.add("gzrd_sjcj_bmdzb");
        removeTableNames.add("gzrd_sjcj_bmxxb");
        removeTableNames.add("gzrd_sjcj_bmztglb");
        removeTableNames.add("gzrd_sjcj_cdqxb");
        removeTableNames.add("gzrd_sjcj_gwbmglb");
        removeTableNames.add("gzrd_sjcj_gwxxb");
        removeTableNames.add("gzrd_sjcj_gwyhglb");
        removeTableNames.add("gzrd_sjcj_holiday");
        removeTableNames.add("gzrd_sjcj_jscdglb");
        removeTableNames.add("gzrd_sjcj_role");
        removeTableNames.add("gzrd_sjcj_scjlb");
        removeTableNames.add("gzrd_sjcj_yhcdglb");
        removeTableNames.add("gzrd_sjcj_yhxxb");
        removeTableNames.add("gzrd_subject_info");
        removeTableNames.add("jkwww_project_img");
        removeTableNames.add("jkwww_sjcj_fjwjb");
        removeTableNames.add("jkwww_sjcj_ztshjlb");
        removeTableNames.add("jkwww_subject_project");
        return removeTableNames;
    }

    private static List<String> getDifferenceList(List<String> tableNames, List<String> tableNames1){
        List<String> differenceList = new ArrayList<String>(tableNames);
        differenceList.removeAll(tableNames1);
        return differenceList;
    }

    private static List<String> getDifferenceList(List<String> tableNames, List<String> tableNames1, List<String> tableNames2){
        List<String> differenceList = getDifferenceList(tableNames, tableNames1);
        differenceList.removeAll(tableNames2);
        return differenceList;
    }

    private static List<String> getSameList(List<String> tableNames, List<String> tableNames1){
        List<String> sameList = new ArrayList<String>(tableNames);
        sameList.retainAll(tableNames1);
        return sameList;
    }

    private static List<String> getSameList(List<String> tableNames, List<String> tableNames1, List<String> tableNames2){
        List<String> sameList = getSameList(tableNames, tableNames1);
        sameList.retainAll(tableNames2);
        return sameList;
    }

    private static List<String> getTableNames(String driverClass, String url, String user, String password) throws Exception {
        Class.forName(driverClass);
        List<String> tableNames = new ArrayList<String>();
        Connection con = DriverManager.getConnection(url, user, password);
        PreparedStatement ps = null;
        ResultSet tableNameResult = null;
        try {
            String sql = "show tables";
            ps = con.prepareStatement(sql);
            tableNameResult = ps.executeQuery();
            while (tableNameResult.next()) {
                String tableName = tableNameResult.getString(1);
                if (url.contains("ncny")){
                    if (StringUtils.contains(tableName, "ncnyw")){
                        tableNames.add(tableName);
                    }
                }else if (url.contains("jkww")){
                    if (StringUtils.contains(tableName, "jkwww")){
                        tableNames.add(tableName);
                    }
                }else if (url.contains("cxjs")){
                    if (StringUtils.contains(tableName, "cxjw")){
                        tableNames.add(tableName);
                    }
                }else if (url.contains("sw")){
                    if (StringUtils.contains(tableName, "hqwsw")){
                        tableNames.add(tableName);
                    }
                }else {
                    if (StringUtils.containsAny(tableName, "ysw", "cxjw", "hqwsw", "ncnyw", "jkwww")){
                        tableNames.add(tableName);
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (ps != null){
                ps.close();
            }
            if (tableNameResult != null){
                tableNameResult.close();
            }
            if (con != null){
                con.close();
            }
        }
        return tableNames;

    }

    private static Map<String, Integer> getTableCount(String driverClass, String url, String user, String password) throws Exception {
        Class.forName(driverClass);
        Map<String, Integer> tableCount = new HashMap<String, Integer>();
        Connection con = DriverManager.getConnection(url, user, password);
        PreparedStatement ps = null;
        ResultSet countResult = null;
        try {
            List<String> tableNames = getTableNames(driverClass, url, user, password);
            for (String tableName : tableNames) {
                if (url.contains("hive")){
                    String countSql = "select * from " + tableName;
                    ps = con.prepareStatement(countSql);
                    countResult = ps.executeQuery();
                    int sum = 0;
                    while (countResult.next()) {
                        sum ++;
                    }
                    tableCount.put(tableName, sum);
                }else {
                    String countSql = "select count(*) from " + tableName;
                    ps = con.prepareStatement(countSql);
                    countResult = ps.executeQuery();
                    if (countResult.next()) {
                        tableCount.put(tableName, countResult.getInt(1));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (countResult != null){
                countResult.close();
            }
            if (ps != null){
                ps.close();
            }
            if (con != null){
                con.close();
            }
        }
        return tableCount;
    }
}
