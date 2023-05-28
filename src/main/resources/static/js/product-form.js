$('.summernote-simple').summernote('disable');

$('#type-select').change(function () {
    if ($(this).val() == 1) {
        $('.summernote-simple').summernote('disable');
        $('#color-picker').prop('disabled', false)
    } else {
        $('.summernote-simple').summernote('enable');
        $('#color-picker').prop('disabled', true)
    }

})
const formatter = new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
});
$(document).ready(function () {
    if ($('#productId').val() == 0)
        $('#productId').val("")
})
$('#default-price').on('input', function (e) {
    var price = $(this).val();
    $('#preview-price-1').val(formatter.format(price))
})
$('#min-price').on('input', function (e) {
    var price = $(this).val();
    $('#preview-price-2').val(formatter.format(price))
})
$('#min-price, #default-price').on('click', function (e) {
    if ($(this).val() == 0.0) {
        $(this).val("")
    }
})

$('#add-type').on('click', function (e) {
    $('#type-modal').modal('show')
})
$('#remove-product').click(function (e) {
    console.log('Run')
    Swal.fire({
        title: 'Bạn có muốn xóa sản phẩm này?',
        text: 'Sản phẩm sẽ không hiển thị trên web nhưng vẫn lưu trữ dữ liệu trong hệ thống',
        icon: 'warning',
        buttons: true,
        dangerMode: true,
    }).then((willDelete) => {
        if (willDelete) {
            var code = $('#productCode').val()
            $.ajax({
                url: '/supplier/san-pham/xoa/',
                method: 'POST',
                data: {productCode: code},
                success: function () {
                    window.location.href = '/supplier/san-pham'
                },
                error: function () {
                    Swal.fire("Thất bại !", "Xóa sản phẩm không thành công thử lại sau !", "error");
                }
            })
        } else {
            Swal.fire('Hủy thao tác thành công');
        }
    });
})

$('#subDetail').on('input', function () {
    var detail_str = $(this).val()
    $(this).removeClass('is-valid, is-invalid');
    if (detail_str.length < 255) {
        $(this).addClass('is-valid');
    } else {
        $(this).addClass('is-invalid');
        $(this).after('<div class="invalid-feedback">\n' + 'Vượt quá chiều dài cho phép 255\n' + '</div>')

    }
})