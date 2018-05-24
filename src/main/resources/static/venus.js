let subRegions = (code = "", clazz) => {
    $("select").attr("disabled", true);
    $.get(`/v1/api/region/sub/${code}`, function (resp, status) {
        if (resp.status != 200) {
            return;
        }

        let regions = resp.data;

        $(clazz).empty();
        $(clazz).append("<option selected='selected'>请选择</option>");
        regions.forEach((region, index) => {
            $(clazz).append(`<option value='${region.code}'>${region.name}</option>`);
        });
    });
    $("select").attr("disabled", false);
};

let clearRegions = (index) => {
    for (let i = index; i < 6; i++) {
        $(`.select-${i}`).empty();
        $(`.select-${i}`).append("<option selected='selected'>请选择</option>");
    }
};

subRegions("", ".select-1");

$("select").change(function (e) {
    e.stopPropagation();
    e.preventDefault();

    let _this = $(this);

    let order = parseInt(_this.data("order"));
    clearRegions(order + 1);

    if (_this.get(0).selectedIndex < 1) {
        return;
    }

    subRegions(_this.val(), `.select-${order + 1}`);
});

let keywordsSearch = () => {
    $("#table_keywords").empty();

    let keywords = $("#keywords").val().trim();
    if (keywords.length < 1) {
        return;
    }

    $.get(`/v1/api/region?name=${keywords}`, function (resp, status) {
        if (resp.status != 200) {
            return;
        }

        let regions = resp.data;

        $("#table_keywords").append(
            "<tr>" +
            "<th>序号</th>" +
            "<th>地区编码</th>" +
            "<th>地区名称</th>" +
            "<th>地区全称</th>" +
            "<th>地区级别</th>" +
            "</tr>"
        );

        regions.forEach((region, index) => {
            $("#table_keywords").append(
                "<tr>" +
                `<td>${index + 1}</td>` +
                `<td>${region.code}</td>` +
                `<td>${region.name}</td>` +
                `<td>${region.fullName}</td>` +
                `<td>${region.level}</td>` +
                "</tr>"
            );
        });
    });
};

$('#keywords').bind('keypress', function (e) {
    e.stopPropagation();
    e.preventDefault();

    if (e.keyCode == "13") {
        keywordsSearch();
    }
});

$("#btn_keywords").click(function (e) {
    e.stopPropagation();
    e.preventDefault();

    keywordsSearch();
});