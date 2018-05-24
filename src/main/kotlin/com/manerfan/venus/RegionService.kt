package com.manerfan.venus

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.sql.ResultSet

/**
 * @author manerfan
 * @date 2018/5/24
 */

enum class RegionLevel {
    PROVINCE, /*省*/
    CITY, /* 市 */
    COUNTY, /* 县 */
    TOWN, /* 镇 */
    VILLAGE /* 村 */
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RegionEntity(
        var code: String? = null,
        var fullCode: String? = null,
        var parentCode: String? = null,
        var name: String? = null,
        var fullName: String? = null,
        var level: RegionLevel? = null
) {
    companion object {
        val TOP_LEVEL: String = "000000000000"
    }
}

class RegionMapper : RowMapper<RegionEntity> {
    override fun mapRow(result: ResultSet?, p1: Int): RegionEntity? {
        return result?.let { r ->
            RegionEntity(
                    r.getString("code"),
                    r.getString("fullcode"),
                    r.getString("parentcode"),
                    r.getString("name"),
                    r.getString("fullname"),
                    RegionLevel.values()[r.getInt("level")]
            )
        }
    }
}

@Service
class RegionService {
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    fun findWithParent(code: String): List<RegionEntity> {
        return jdbcTemplate.query("SELECT * FROM region WHERE parentcode = ?", arrayOf(code), RegionMapper())
    }

    fun findByCode(code: String): RegionEntity? {
        return try {
            jdbcTemplate.queryForObject("SELECT * FROM region WHERE code = ?", arrayOf(code), RegionMapper())
        } catch (err: EmptyResultDataAccessException) {
            null
        }
    }

    fun findByName(name: String?, skip: Int, num: Int): List<RegionEntity> {
        return when {
            StringUtils.hasText(name) -> jdbcTemplate.query("SELECT * FROM region WHERE name like ? LIMIT ?, ?", arrayOf("%${name?.trim()}%", skip, num), RegionMapper())
            else -> jdbcTemplate.query("SELECT * FROM region LIMIT ?, ?", arrayOf(skip, num), RegionMapper())
        }
    }
}