$(document).ready(function () {
    const now = new Date();
    const month = now.getMonth() + 1; // getMonth() returns 0-indexed month, so add 1
    const year = now.getFullYear();
    callApiDrawChart(month, year, '/api/v1/admin/totalCommission/', drawAdminLineChart);
    callApiDrawChart(month, year, '/api/v1/admin/totalOrderInMonth/', drawAdminPieChart);
    callApiDrawChart(month, year, '/api/v1/admin/totalTransactionInMonth/', drawAdminDoughnutChart)
    showTable(month, year)
})

function callApiDrawChart(month, year, api, callback) {
    $.ajax({
        url: api + month + '/' + year,
        method: 'get',
        success: function (json) {
            console.log('GET:' + api)
            console.log(json)
            callback(json);
        }
    })
}

function showTable(month, year) {
    $.ajax({
        url: '/api/v1/admin/topBestSellerInMonth/' + month + '/' + year,
        method: 'get',
        success: function (json) {
            console.log('GET:' + '/api/v1/admin/topBestSellerInMonth/')
            console.log(json)
            json.forEach(t => {
                $('tbody').append('<tr>' +
                    '<td>' + t.productId + '</td>' +
                    '<td><a href="/chi-tiet-san-pham/' + t.productCode + '">' + t.productName.substring(0, 31) + '...</a></td>' +
                    '<td>' + t.total + '</td>' +
                    '</tr>')
            })
        }
    })
}

function drawAdminDoughnutChart(json) {
    var ctx = document.getElementById("doughnut-chart").getContext('2d');
    var myChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            datasets: [{
                data: [
                    json.filter(e => e.type == true).map(e => e.value),
                    json.filter(e => e.type == false).map(e => e.value),
                ],
                backgroundColor: [
                    '#33FF57',
                    '#FF5733'
                ],
                label: 'Dữ liệu'
            }],
            labels: [
                'Nạp tiền',
                'Thanh toán',
            ],
        },
        options: {
            responsive: true,
            legend: {
                position: 'bottom',
            },
        }
    });
}

function drawAdminLineChart(json) {
    var lineChart = document.getElementById('line-chart');
    var myChart = new Chart(lineChart, {
        type: 'line',
        data: {
            labels: json.map(t => t.createDate),
            datasets: [
                {
                    label: 'Lợi nhuận từ người dùng',
                    data: json.map(t => t.fromBuyer),
                    backgroundColor: 'rgba(255, 87, 51, 0.7)',
                    borderColor: '#FF5733',
                    borderWidth: 1
                },
                {
                    label: 'Lợi nhuận từ người bán',
                    data: json.map(t => t.fromSupplier),
                    backgroundColor: 'rgba(0, 128, 128, 0.7)',
                    borderColor: 'rgba(0, 128, 128, 0.7)',
                    borderWidth: 1
                }
            ]
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            },
        }
    });
}

function drawAdminPieChart(json) {
    var ctx = document.getElementById("donut-chart").getContext('2d');
    var myChart = new Chart(ctx, {
        type: 'pie',
        data: {
            datasets: [{
                data: [
                    json.filter(e => e.type == 1).map(e => e.total),
                    json.filter(e => e.type == 2).map(e => e.total),
                    json.filter(e => e.type == 3).map(e => e.total),
                    json.filter(e => e.type == 4).map(e => e.total),
                    json.filter(e => e.type == 0).map(e => e.total)
                ],
                backgroundColor: [
                    '#DBFF33',
                    '#33FFBD',
                    '#FFBD33',
                    '#33FF57',
                    '#FF5733'
                ],
                label: 'Dataset 1'
            }],
            labels: [
                'Chờ xác nhận',
                'Đang xử lý',
                'Vận chuyển',
                'Hoàn tất',
                'Huỷ đơn'
            ],
        },
        options: {
            responsive: true,
            legend: {
                position: 'bottom',
            },
        }
    });
}

