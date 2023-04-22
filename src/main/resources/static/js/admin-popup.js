$('.btn-ban-product').click(function (e) {
    console.log('Run')
    Swal.fire({
        title: 'Bạn có muốn vô hiệu sản phâm này?',
        text: 'Sản phẩm của người bán sẽ bị khóa',
        icon: 'warning',
        buttons: true,
        dangerMode: true,
    }).then((willDelete) => {
        if (willDelete.isConfirmed) {
            var code = $(this).data('id')
            $.ajax({
                url: '/admin/san-pham/khoa-san-pham/',
                method: 'POST',
                data: {code: code},
                success: function () {
                    window.location.href = '/admin/san-pham'
                },
                error: function () {
                    Swal.fire("Thất bại !", "Khóa sản phẩm không thành công thử lại sau !", "error");
                }
            })
        } else {
            Swal.fire('Hủy thao tác thành công');
        }
    });
})
$('.btn-ban-account').click(function (e) {
    console.log('Run')
    Swal.fire({
        title: 'Bạn có muốn khóa người dùng này',
        text: 'Tài khoản của người dùng sẽ bị khóa !',
        icon: 'warning',
        buttons: true,
        dangerMode: true,
    }).then((willDelete) => {
        if (willDelete.isConfirmed) {
            var email_user = $(this).data('id')
            $.ajax({
                url: '/admin/nguoi-dung/khoa-tai-khoan/',
                method: 'POST',
                data: {email: email_user},
                success: function () {
                    window.location.href = '/admin/nguoi-dung'
                },
                error: function () {
                    Swal.fire("Thất bại !", "Khóa tài khoản không thành công !", "error");
                }
            })
        } else {
            Swal.fire('Hủy thao tác thành công');
        }
    });
})