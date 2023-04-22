$(function () {
    "use strict";

    //------- Parallax -------//
    skrollr.init({
        forceHeight: false
    });

    /*   //------- Active Nice Select --------//
       $('select').niceSelect();*/

    //------- hero carousel -------//
    $(".hero-carousel").owlCarousel({
        items: 3,
        margin: 10,
        autoplay: false,
        autoplayTimeout: 5000,
        loop: true,
        nav: false,
        dots: false,
        responsive: {
            0: {
                items: 1
            },
            600: {
                items: 2
            },
            810: {
                items: 3
            }
        }
    });

    //------- Best Seller Carousel -------//
    if ($('.owl-carousel').length > 0) {
        $('#bestSellerCarousel').owlCarousel({
            loop: true,
            margin: 30,
            nav: true,
            navText: ["<i class='ti-arrow-left'></i>", "<i class='ti-arrow-right'></i>"],
            dots: false,
            responsive: {
                0: {
                    items: 1
                },
                600: {
                    items: 2
                },
                900: {
                    items: 3
                },
                1130: {
                    items: 4
                }
            }
        })
    }

    //------- single product area carousel -------//
    $(".s_Product_carousel").owlCarousel({
        items: 1,
        autoplay: false,
        autoplayTimeout: 5000,
        loop: true,
        nav: false,
        dots: false
    });

    //------- mailchimp --------//
    function mailChimp() {
        $('#mc_embed_signup').find('form').ajaxChimp();
    }

    mailChimp();

    //------- fixed navbar --------//
    $(window).scroll(function () {
        var sticky = $('.header_area'),
            scroll = $(window).scrollTop();

        if (scroll >= 100) sticky.addClass('fixed');
        else sticky.removeClass('fixed');
    });

    //------- Price Range slider -------//
    if (document.getElementById("price-range")) {

        var nonLinearSlider = document.getElementById('price-range');

        noUiSlider.create(nonLinearSlider, {
            connect: true,
            behaviour: 'tap',
            start: [500, 4000],
            range: {
                // Starting at 500, step the value by 500,
                // until 4000 is reached. From there, step by 1000.
                'min': [0],
                '10%': [500, 500],
                '50%': [4000, 1000],
                'max': [10000]
            }
        });


        var nodes = [
            document.getElementById('lower-value'), // 0
            document.getElementById('upper-value')  // 1
        ];

        // Display the slider value and how far the handle moved
        // from the left edge of the slider.
        nonLinearSlider.noUiSlider.on('update', function (values, handle, unencoded, isTap, positions) {
            nodes[handle].innerHTML = values[handle];
        });

    }

});
$('.search-input').keypress(function (e) {
    if (e.which == 13) {
        $('form#search-form').submit();
    }
});

function updateTotal() {
    var product = $('.product-price').text();
    var shipping = $('#shipping-price').text();
    var newTotal = parseInt(product) + parseInt(shipping);
    $('#total-price').text(newTotal);
}

$("#select-address").change(function () {
    var orderId = $('#order-id').text()
    var addressId = $("#select-address").val()
    $.ajax({
        url: '/api/v1/shipping-cost',
        method: 'get',
        data: {
            'orderId': orderId,
            'addressId': addressId
        },
        success(data) {
            $('#shipping-price').text(data.cost)
            updateTotal();
        }
    })
})
$("#confirm-order").on('click', function () {
    var total = $('#total-price').text()
    Swal.fire({
        title: "Xác nhận",
        text: "Xác nhận thanh toán số tiền :" + total,
        icon: "question",
        showCancelButton: true,
        cancelButtonColor: '#d33',
        cancelButtonText: "Huỷ",
        showConfirmButton: true,
        confirmButtonColor: '#28A745'
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.showLoading();
            var addressId = $("#select-address").val();
            var orderId = $('#select-address').data('id');
            if (addressId != -1) {
                $.post({
                    url: '/xac-nhan-don-hang',
                    data: {
                        'addressId': addressId,
                        'orderId': orderId
                    },
                    success(data) {
                        Swal.close()
                        Swal.fire("Xác nhận đơn hàng thành công", "Đơn hàng của bạn đã thanh toán. Hệ thống chuyển trang sau 5s !", "success")
                        setTimeout(function () {
                            window.location.href = "/don-hang";
                        }, 5000);
                    },
                    error(data) {
                        swal.close()
                        console.log(data)
                        Swal.fire("Không thể tạo đơn hàng", "Số dư của bạn không đủ đê thực hiện thanh toán", "error")
                    }
                })
            } else {
                Swal.close()
                Swal.fire("Thất bại !", "Vui lòng chọn địa chỉ giao hàng !", "error");
            }
        }
    })

})


