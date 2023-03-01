$('#forgot-pass-btn').on('click', function (e) {
    let email = $('#forgot-pass-input').val();
    $.ajax({
        url: '/reset-password',
        method: 'post',
        data: {'email': email},
        success: function (data) {
            window.location.href = "/thong-bao?type=1";
        }
    })
})

$('#submit-auction').on('click', function (e) {
    let productKey = $('#productKey').val()
    let createDate = $('#date-input').val()
    let reservePrice = $('#reservePrice').val()
    let endDate = parseInt($('#date-end').text())
    let enable = ($('#auto_enable').val()) == 'on' ? true : false
    let tag_str = $('#hagtag').val();
    let categoryId = $('#categoryId').val()
    let description = $("#description").val()

    let formData = new FormData($('#upload-image')[0])
    formData.append('description', description)
    formData.append('productKey', productKey)
    formData.append('startDate', createDate)
    formData.append('countDay', endDate)
    formData.append('reservePrice', reservePrice)
    formData.append('productTagStr', tag_str)
    formData.append('categoryId', categoryId)
    formData.append('auto', enable.toString())

    $.post({
        url: '/dau-gia/tao-phien',
        data: formData,
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        success: function (e) {
            window.location.href = "/dau-gia/quan-ly-phien";
        },
        error: function (e) {
            window.location.href = "/thong-bao?type=10";
        }
    })
})

$('#set-new-price').on('click', function (e) {
    let id = $('#auction-id-input').val();
    let price = $('#new-price').val();
    $.post({
        url: '/dau-gia/dat-gia-moi',
        data: {
            'auctionId': id,
            'price': price
        },
        success: function () {
            location.reload();
        },
        error: function (e) {

        }
    })
})
