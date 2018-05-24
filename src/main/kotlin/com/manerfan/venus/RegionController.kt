package com.manerfan.venus

import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

/**
 * @author manerfan
 * @date 2018/5/24
 */

@ApiModel("数据", description = "请求数据")
data class ResponseData(
        @ApiModelProperty("响应码")
        val status: Int = HttpStatus.OK.value(),

        @ApiModelProperty("响应消息")
        val message: String? = "success",

        @ApiModelProperty("响应数据")
        val data: Any? = null
)

@Api(description = "城市列表查询")
@RestController
@RequestMapping("/v1/api/region")
class RegionController {
    @Autowired
    private lateinit var regionService: RegionService

    @ApiOperation("获取某区域详情", notes = "获取某区域详情", httpMethod = "GET")
    @ApiImplicitParam("区域code", name = "code", paramType = "path", required = true)
    @GetMapping("/{code}")
    fun detail(@PathVariable code: String): ResponseData {
        return ResponseData(data = regionService.findByCode(code))
    }

    @ApiOperation("按名称查找", notes = "按名称查找区域", httpMethod = "GET")
    @ApiImplicitParams(
            ApiImplicitParam("关键词", name = "name", paramType = "query", required = false),
            ApiImplicitParam("页数，1起始", name = "page", paramType = "query", defaultValue = "1", required = false),
            ApiImplicitParam("返回个数", name = "num", paramType = "query", defaultValue = "10", required = false)
    )
    @GetMapping
    fun findByName(
            @RequestParam(required = false) name: String?,
            @RequestParam(required = false, defaultValue = "1") page: Int,
            @RequestParam(required = false, defaultValue = "10") num: Int): ResponseData {
        val p = if (page < 1) 1 else page
        val n = if (num < 1) 1 else num
        return ResponseData(data = regionService.findByName(name, (p - 1) * n, n))
    }

    @ApiOperation("获取顶层区域", notes = "获取顶层区域", httpMethod = "GET")
    @GetMapping("/sub")
    fun topRegions(): ResponseData {
        return regions(RegionEntity.TOP_LEVEL)
    }

    @ApiOperation("获取子区域", notes = "获取子区域", httpMethod = "GET")
    @ApiImplicitParam("区域code", name = "code", paramType = "path", required = true)
    @GetMapping("/sub/{code}")
    fun regions(@PathVariable code: String): ResponseData {
        return ResponseData(data = regionService.findWithParent(code))
    }
}