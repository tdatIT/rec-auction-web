$('.btn-ban-product').click(function (e) {
    console.log('Run')
    swal({
        title: 'Bạn có muốn vô hiệu sản phâm này?',
        text: 'Sản phẩm của người bán sẽ bị khóa',
        icon: 'warning',
        buttons: true,
        dangerMode: true,
    }).then((willDelete) => {
        if (willDelete) {
            var code = $(this).data('id')
            $.ajax({
                url: '/admin/san-pham/khoa-san-pham/',
                method: 'POST',
                data: {code: code},
                success: function () {
                    window.location.href = '/admin/san-pham'
                },
                error: function () {
                    swal("Thất bại !", "Khóa sản phẩm không thành công thử lại sau !", "error");
                }
            })
        } else {
            swal('Hủy thao tác thành công');
        }
    });
})
$('.btn-ban-account').click(function (e) {
    console.log('Run')
    swal({
        title: 'Bạn có muốn khóa người dùng này',
        text: 'Tài khoản của người dùng sẽ bị khóa !',
        icon: 'warning',
        buttons: true,
        dangerMode: true,
    }).then((willDelete) => {
        if (willDelete) {
            var email_user = $(this).data('id')
            $.ajax({
                url: '/admin/nguoi-dung/khoa-tai-khoan/',
                method: 'POST',
                data: {email: email_user},
                success: function () {
                    window.location.href = '/admin/nguoi-dung'
                },
                error: function () {
                    swal("Thất bại !", "Khóa tài khoản không thành công !", "error");
                }
            })
        } else {
            swal('Hủy thao tác thành công');
        }
    });
})