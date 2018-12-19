package javalinstagram

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

val Hikari = HikariDataSource(HikariConfig().apply {
    setJdbcUrl("jdbc:sqlite:javalinstagram.db");
    addDataSourceProperty("cachePrepStmts", "true");
    addDataSourceProperty("prepStmtCacheSize", "250");
    addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
})
