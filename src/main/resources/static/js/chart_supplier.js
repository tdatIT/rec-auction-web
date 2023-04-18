$(document).ready(function (e) {
    const current = new Date().toJSON().slice(0, 10)
    getLineChartDate(current)
    getDonutChartDate(current)
})
$('#lchart-btn').click(function (e) {
    var filter = $('#lchart-input').val()+'-01';
    getLineChartDate(filter)
})
$('#dchart-btn').click(function (e) {
    var filter = $('#dchart-input').val()+'-01';
    getDonutChartDate(filter)
})

function getLineChartDate(date) {
    $.ajax({
        url: '/api/v1/supplier/statistic-wallet/line-chart',
        method: 'get',
        data: {
            'filterDate': date
        },
        success: function (json) {
            console.log('Line chart data');
            drawLineChart(json)
        }
    })
}

function getDonutChartDate(date) {
    $.ajax({
        url: '/api/v1/supplier/statistic-wallet/pie-chart',
        method: 'get',
        data: {
            'filterDate': date
        },
        success: function (json) {
            console.log('GET Donut Chart');
            console.log(json)
            drawDonutChart(json)
        }
    })
}

function drawLineChart(json) {
    var lineChart = document.getElementById('line-chart');
    var myChart = new Chart(lineChart, {
        type: 'line',
        data: {
            labels: json.data.map(element => element.createDate),
            datasets: [
                {
                    label: 'Thanh toán/Dịch vụ',
                    data: json.data.filter(e => e.type == false).map(e => e.value),
                    backgroundColor: 'rgba(0, 128, 128, 0.3)',
                    borderColor: 'rgba(0, 128, 128, 0.7)',
                    borderWidth: 1
                },
                {
                    label: 'Nạp tiền/Đơn hàng',
                    data: json.data.filter(e => e.type == true).map(e => e.value),
                    backgroundColor: 'rgba(0, 128, 128, 0.7)',
                    borderColor: 'rgba(0, 128, 128, 1)',
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

function drawDonutChart(json) {
    var ctx = document.getElementById("donut-chart").getContext('2d');
    var myChart = new Chart(ctx, {
        type: 'pie',
        data: {
            datasets: [{
                data: [
                    json.data.filter(e => e.type == false).map(e => e.value),
                    json.data.filter(e => e.type == true).map(e => e.value)
                ],
                backgroundColor: [
                    '#fc544b',
                    '#63ed7a',
                ],
                label: 'Dataset 1'
            }],
            labels: [
                'Thanh toán/Dịch vụ',
                'Nạp tiền/Đơn hàng'
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

