package com.ec.recauctionec.controller;

import com.ec.recauctionec.variable.Router;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MessageController {
    public static final int VERIFY_TOKEN = 4;
    public static final int NOT_FOUND_AUCTION = 2;
    public static final int CREATE_FAIL = 10;
    public static final int CHANGE_PASS_SUCCESS = 1;
    public static final int FAIL_SUPPLIER = 3;
    public static final int LOGOUT_MESS = 5;

    @GetMapping(value = Router.MESSAGE)
    public String getMessage(@RequestParam("type") int type, ModelMap modelMap) {
        switch (type) {
            case CHANGE_PASS_SUCCESS:
                modelMap.addAttribute("message", "Đặt yêu cầu đổi mật khẩu thành công");
                modelMap.addAttribute("description", "Vui lòng kiểm tra email để xác nhận");
                modelMap.addAttribute("btn_text", "Trang chủ");
                modelMap.addAttribute("btn_href", "trang-chu");
                break;
            case NOT_FOUND_AUCTION:
                modelMap.addAttribute("message", "Phiên đấu giá không tồn tại");
                modelMap.addAttribute("description", "Hệ thống không tìm thấy phiên đấu bạn mong muốn");
                modelMap.addAttribute("btn_text", "Trang chủ");
                modelMap.addAttribute("btn_href", "trang-chu");
                break;
            case CREATE_FAIL:
                modelMap.addAttribute("message", "Tạo phiên thất bại");
                modelMap.addAttribute("description", "Xin lỗi ! Có thể bạn hết lượt tạo trong hôm nay hoặc " +
                        "số dư trong ví không đủ 30% giá trị hàng !");
                modelMap.addAttribute("btn_text", "Trang chủ");
                modelMap.addAttribute("btn_href", "trang-chu");
                break;
            case FAIL_SUPPLIER:
                modelMap.addAttribute("message", "Đăng ký thất bại");
                modelMap.addAttribute("description", "Xin lỗi có vẽ hệ thống bị lỗi liên hệ QTV để giải quyết !");
                modelMap.addAttribute("btn_text", "Trang chủ");
                modelMap.addAttribute("btn_href", "trang-chu");
                break;
            case VERIFY_TOKEN:
                modelMap.addAttribute("message", "Đăng ký thành công !");
                modelMap.addAttribute("description", "Vui lòng kiểm tra email để xác nhận taì khoản !");
                modelMap.addAttribute("btn_text", "Trang chủ");
                modelMap.addAttribute("btn_href", "trang-chu");
            case LOGOUT_MESS:
                modelMap.addAttribute("message", "Đăng ký thông tin thành công !");
                modelMap.addAttribute("description", "Vui lòng đăng nhập tài khoản để hoàn tất cập nhật !");
                modelMap.addAttribute("btn_text", "Đăng xuất");
                modelMap.addAttribute("btn_href", "logout");
        }
        return "message";
    }
}
