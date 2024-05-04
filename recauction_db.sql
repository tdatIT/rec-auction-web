USE recauction_db;
-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, 'Macbook', 'Macbook có lẽ là thương hiệu quá quen thuộc và được đông đảo người dùng yêu thích lựa chọn không chỉ bởi thiết kế sang trọng mà chúng còn mang đến cho người dùng những trải nghiệm cực kỳ mượt mà và ấn tượng.', 'https://cdn2.cellphones.com.vn/x,webp,q100/media/wysiwyg/laptop/macbook/Macbook-1-1644627973.jpg');
INSERT INTO `category` VALUES (2, 'IPhone', 'iPhone với hệ điều hành IOS của Apple luôn là mẫu điện thoại được người dùng đánh giá cao với cấu hình mạnh mẽ và độ hoàn thiện cao. ', 'https://cdn2.cellphones.com.vn/x,webp,q100/media/wysiwyg/mobile/apple/apple-iphone-cellphones.jpg');
INSERT INTO `category` VALUES (3, 'Samsung', 'Được sản xuất ngay tại Việt Nam với công nghệ đạt chuẩn quốc tế của Hàn Quốc', 'https://cdn2.cellphones.com.vn/x,webp,q100/media/wysiwyg/Phone/Samsung/dien-thoai-samsung-1.jpg');
INSERT INTO `category` VALUES (4, 'Laptop HP', 'Laptop HP là một trong những thương hiệu laptop hàng đầu thế giới hiện hay. Những chiếc máy tính xách tay của hãng luôn mang lại sự yên tâm cho người dùng bởi hiệu năng ổn định và chất lượng tốt qua từng năm tháng', 'https://cdn2.cellphones.com.vn/x,webp,q100/media/wysiwyg/laptop/HP/laptop-hp-1_1.jpg');
INSERT INTO `category` VALUES (5, 'Apple Airpods', 'Từ lâu nay, Apple đã trở thành một thương hiệu của sự đẳng cấp và sang trọng. Các sản phẩm của hãng có vẻ ngoài cuốn hút và chất lượng tuyệt vời, mang lại trải nghiệm đầy ấn tượng. Trong lĩnh vực nghe nhạc cũng vậy, những mẫu tai nghe', 'https://cdn2.cellphones.com.vn/x,webp,q100/media/wysiwyg/accessories/headphones/tai-nghe-apple-1.jpg');
INSERT INTO `category` VALUES (6, 'Apple Watch', 'Ngày nay, các thiết bị đeo tay đã trở nên thông minh hơn rất nhiều và được tích hợp nhiều công nghệ hiện đại giúp người dùng sinh hoạt, học tập, làm việc và giải trí tiện lợi hơn, dễ dàng hơn, đặc biệt là sản phẩm của những thương hiệu nổi tiếng như Apple', 'https://cdn2.cellphones.com.vn/x,webp,q100/media/wysiwyg/Watch/Apple/apple-watch-1.jpg');
-- ----------------------------
-- Records of delivery
-- ----------------------------
INSERT INTO `delivery` VALUES (1, 18000, 'Viettel Post');
-- ----------------------------
-- Records of product_tag
-- ----------------------------
INSERT INTO `product_tag` VALUES (1, 'San pham tu apple', 'Apple');
INSERT INTO `product_tag` VALUES (2, 'Dien thoai Iphone', 'iPhone');
INSERT INTO `product_tag` VALUES (3, 'Dien Thoai Samsung', 'Samsung');
INSERT INTO `product_tag` VALUES (4, 'May tinh ban Ipad', 'Ipad');
INSERT INTO `product_tag` VALUES (5, 'Macbook', 'Macbook');
INSERT INTO `product_tag` VALUES (6, 'IMac', 'iMac');
INSERT INTO `product_tag` VALUES (7, 'Xiaomi', 'Xiaomi');
INSERT INTO `product_tag` VALUES (8, 'Laptop', 'HP');

SET FOREIGN_KEY_CHECKS = 1;
