/*
 * package com.scube.chargingstation; import java.sql.Connection; import
 * java.sql.DriverManager; import java.sql.SQLException;
 * 
 * public class SQLDatabaseConnection { // Connect to your database. // Replace
 * server name, username, and password with your credentials public static void
 * main(String[] args) { String connectionUrl = "jdbc:sqlserver://127.0.0.1;" +
 * "database=OCPP.Core;" + "user=sa;" + "password=Pa5sw0rd1!;" + "encrypt=true;"
 * + "trustServerCertificate=false;" + "loginTimeout=30;";
 * 
 * try (Connection connection = DriverManager.getConnection(connectionUrl);) {
 * // Code here. } // Handle any errors that may have occurred. catch
 * (SQLException e) { e.printStackTrace(); } } }
 * 
 * 
 * 
 * <!-- https://mvnrepository.com/artifact/com.microsoft.sqlserver/mssql-jdbc
 * --> <dependency> <groupId>com.microsoft.sqlserver</groupId>
 * <artifactId>mssql-jdbc</artifactId> </dependency>
 */